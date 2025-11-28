package com.example.exam2

import android.util.Log
import android.view.MotionEvent
import com.google.firebase.database.FirebaseDatabase
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener

class GeneralMoveModeHelper(
    private val activity: BaseActivity,
    private val showHomeLayout: () -> Unit,     // 메인(지도의 기본 화면)
    private val showFaceLayout: () -> Unit      // 얼굴 화면 전환
) : OnGoToLocationStatusChangedListener {

    companion object {
        private const val TAG = "GeneralMoveModeHelper"

        private const val DESC_COMPLETE = 500
        private const val DESC_OBSTACLE = 2000
        private const val DESC_GROUND_OBSTACLE = 2001
        private const val DESC_HIGH_OBSTACLE = 2002
        private const val DESC_LIDAR_OBSTACLE = 2003
    }

    private val db = FirebaseDatabase.getInstance("https://exam-afefa-default-rtdb.firebaseio.com")
    private val generalMoveRef = db.getReference("general_move_mode")

    private val robot = Robot.getInstance()

    private val corners = listOf("코너1", "코너2", "코너3", "코너4")
    private var currentCornerIndex = 0

    private var isMoveMode = false
    private var lastObstacleTts = 0L

    private val obstacleCooldown = 8000L  // 8초 간격으로 TTS

    init {
        robot.addOnGoToLocationStatusChangedListener(this)

        // 어떤 화면이든 터치 발생 시 즉시 이동 모드 종료
        activity.registerGlobalTouchHandler { ev: MotionEvent ->
            if (isMoveMode && ev.action == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "터치 감지 → 이동 모드 종료")
                stopMoveMode()
            }
        }
    }

    /** 홈 화면에서 2분 무조작 시 실행 — general_move_mode == 1 일 때만 수행 */
    fun tryStartMoveMode() {
        if (isMoveMode) return

        generalMoveRef.setValue(1)   // 상태 기록용
        startMoveMode()
    }

    /** 이동 모드 시작 */
    private fun startMoveMode() {
        if (isMoveMode) return

        isMoveMode = true
        currentCornerIndex = 0
        lastObstacleTts = 0L

        // BaseActivity 자동 복귀 꺼두기
        activity.setAutoReturnEnabled(false)

        // ✨ TemiNavigationHelper 와 동일한 방식으로 화면 전환
        activity.runOnUiThread {
            showFaceLayout.invoke()
        }

        goToCurrentCorner()
    }

    /** 현재 코너로 이동 */
    private fun goToCurrentCorner() {
        if (!isMoveMode) return
        val target = corners[currentCornerIndex]
        Log.d(TAG, "Temi 이동 → $target")
        robot.goTo(target)
    }

    /** 다음 코너로 순환 이동 */
    private fun goToNextCorner() {
        if (!isMoveMode) return
        currentCornerIndex = (currentCornerIndex + 1) % corners.size
        goToCurrentCorner()
    }

    /** 이동 모드 종료 */
    fun stopMoveMode() {
        if (!isMoveMode) return

        isMoveMode = false
        robot.stopMovement()

        // Firebase 값 복원
        generalMoveRef.setValue(0)

        // 자동 홈복귀 다시 ON
        activity.setAutoReturnEnabled(true)

        activity.runOnUiThread {
            showHomeLayout.invoke()
        }

        Log.d(TAG, "행사장 이동 모드 종료 완료")
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        if (!isMoveMode) return

        val currentCornerName = corners[currentCornerIndex]
        if (!location.equals(currentCornerName, ignoreCase = true)) return

        // 정상 도착 → 다음 코너로
        if (descriptionId == DESC_COMPLETE || status.equals("complete", true)) {
            goToNextCorner()
            return
        }

        // 장애물 감지 → TTS
        val isObstacle =
            descriptionId == DESC_OBSTACLE ||
                    descriptionId == DESC_GROUND_OBSTACLE ||
                    descriptionId == DESC_HIGH_OBSTACLE ||
                    descriptionId == DESC_LIDAR_OBSTACLE

        if (isObstacle) {
            val now = System.currentTimeMillis()
            if (now - lastObstacleTts > obstacleCooldown) {
                lastObstacleTts = now
                robot.speak(TtsRequest.create("Temi를 사용해보시겠어요? 얼굴을 터치해주세요.", false))
            }
        }
    }
}
