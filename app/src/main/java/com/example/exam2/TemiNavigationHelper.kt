package com.example.exam2

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import android.os.Handler
import android.os.Looper
import android.content.Intent

class TemiNavigationHelper(
    private val activity: AppCompatActivity,
    private val showMapLayout: () -> Unit,     // 지도(기본) 화면으로 돌아갈 때 호출
    private val showFaceLayout: () -> Unit,    // 안내 중 얼굴 화면으로 전환할 때 호출
    dbUrl: String = "https://exam-afefa-default-rtdb.firebaseio.com",
    private val directionsNode: String = "Directions"
) : OnGoToLocationStatusChangedListener {

    companion object {
        private const val TAG = "TemiNavigationHelper"
    }

    private val robot: Robot = Robot.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(dbUrl)
    private val directionsRef: DatabaseReference = db.getReference(directionsNode)

    private var isNavigating: Boolean = false
    private var currentLocationKey: String? = null        // Firebase 에 기록할 키 (예: "rest")
    private var currentTemiLocationName: String? = null   // Temi 에 저장된 위치 이름 (예: "소화기2")
    // 🔹 추가: 어떤 부스에서 출발했는지(말할 때 쓸 이름)
    private var currentBoothTitle: String? = null

    // 🔹 추가: 현재 진행 중인 설명 TTS 요청(필요시 취소용)
    private var currentGuideTts: TtsRequest? = null
    private val ttsHandler = Handler(Looper.getMainLooper())
    private val gestureDetector: GestureDetectorCompat =
        GestureDetectorCompat(
            activity,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (isNavigating) {
                        cancelNavigation("안내를 취소했습니다.")
                        return true
                    }
                    return false
                }
            }
        )

    init {
        if (activity is BaseActivity) {
            activity.registerGlobalTouchHandler { ev ->
                gestureDetector.onTouchEvent(ev)
            }
        }
        showMapLayoutInternal()
    }

    fun onStart() {
        Log.d(TAG, "onStart() - Temi listener 등록")
        robot.addOnGoToLocationStatusChangedListener(this)
    }

    fun onStop() {
        Log.d(TAG, "onStop() - Temi listener 해제")
        robot.removeOnGoToLocationStatusChangedListener(this)
    }

    private fun showMapLayoutInternal() {
        if (activity is BaseActivity) {
            activity.setAutoReturnEnabled(true)
        }

        activity.runOnUiThread {
            showMapLayout.invoke()
        }
    }

    private fun showFaceLayoutInternal() {
        if (activity is BaseActivity) {
            activity.setAutoReturnEnabled(false)
        }

        activity.runOnUiThread {
            showFaceLayout.invoke()
        }
    }
    private fun goToUsabilitySurvey() {
        val intent = Intent(activity, UsabilityActivity::class.java).apply {
            putExtra("surveyType", "길안내")   // 필요하면 다른 값으로 변경 가능
        }
        activity.startActivity(intent)
    }
    /**
     * 길찾기 시작
     *
     * @param locationKey        Firebase Directions 하위 노드 이름
     * @param temiLocationName   Temi 에 저장된 위치 이름
     * @param boothTitle         Temi 가 말로 소개할 부스 이름 (팝업 제목 등)
     * @param guideMessage       이동 중에 Temi 가 할 설명 멘트 전체
     */
    fun startNavigation(
        locationKey: String,
        temiLocationName: String,
        boothTitle: String? = null,
        guideMessage: String
    ) {
        // 0. Temi 안에 해당 위치가 실제로 있는지 확인
        val savedLocations = robot.locations ?: emptyList()
        val isKnownLocation = savedLocations.any {
            it.equals(temiLocationName, ignoreCase = true)
        }

        if (!isKnownLocation) {
            Log.w(
                TAG,
                "Unknown Temi location name: $temiLocationName, saved=${savedLocations.joinToString()}"
            )
            Toast.makeText(activity, "알 수 없는 장소입니다.", Toast.LENGTH_LONG).show()
            return
        }

        // 1. 상태 저장
        currentLocationKey = locationKey
        currentTemiLocationName = temiLocationName
        currentBoothTitle = boothTitle

        // 2. Firebase 플래그 1로 세팅
        directionsRef.child(locationKey).setValue(1).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Directions/$locationKey -> 1 저장 완료")
            } else {
                Log.e(TAG, "Directions/$locationKey -> 1 저장 실패", task.exception)
            }
        }

        // 3. Temi 이동 시작
        robot.goTo(temiLocationName)
        isNavigating = true

        // 4. UI: 얼굴 화면으로 전환
        showFaceLayoutInternal()

        ttsHandler.removeCallbacksAndMessages(null)
        robot.cancelAllTtsRequests()

// 1) 항상 먼저 나오는 안내 시작 멘트
        val startName = when {
            !boothTitle.isNullOrBlank() -> boothTitle
            !temiLocationName.isBlank() -> temiLocationName
            else -> null
        }

        startName?.let { name ->
            val startText = "$name 로 안내를 시작합니다."
            val startTts = TtsRequest.create(startText, false)
            robot.speak(startTts)
        }

// 2) 그 다음에 나오는 상세 설명 멘트 (guideMessage가 있을 때만)
        val explainText = guideMessage.trim()
        if (explainText.isNotEmpty()) {
            val explainTts = TtsRequest.create(explainText, false)
            currentGuideTts = explainTts

            // 시작 멘트가 어느 정도 끝난 뒤에 재생 (1.5~2초 정도 텀)
            ttsHandler.postDelayed({
                // 혹시 그 사이에 안내가 취소/종료되었다면 재생 안 함
                if (isNavigating) {
                    robot.speak(explainTts)
                }
            }, 8000L)
        }
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        Log.d(
            TAG, "goTo status: location=$location, status=$status, " +
                    "descriptionId=$descriptionId, desc=$description"
        )

        if (!isNavigating) return

        val targetTemiName = currentTemiLocationName
        if (targetTemiName == null || !location.equals(targetTemiName, ignoreCase = true)) {
            return
        }

        if (status.equals("complete", ignoreCase = true)) {
            handleComplete()
            return
        }

        if (status.equals("abort", ignoreCase = true) ||
            status.equals("aborted", ignoreCase = true) ||
            description.contains("위치 사용 불가") ||
            description.contains("Location unavailable", ignoreCase = true)
        ) {
            cancelNavigation("길 안내에 실패했습니다.\n사유: $description")
        }
    }

    private fun handleComplete() {
        val locationKey = currentLocationKey
        val boothTitle = currentBoothTitle
        val temiName = currentTemiLocationName

        isNavigating = false

        // 🔹 예약된 설명 TTS 딜레이 제거
        ttsHandler.removeCallbacksAndMessages(null)

        // Firebase 플래그 0으로
        if (locationKey != null) {
            directionsRef.child(locationKey).setValue(0).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "도착 -> Directions/$locationKey 0으로 리셋")
                } else {
                    Log.e(TAG, "도착 -> Directions/$locationKey 0으로 리셋 실패", task.exception)
                }
            }
        }

        // 🔹 현재 말하고 있던 안내 멘트는 즉시 끊기
        robot.cancelAllTtsRequests()

        val arrivalName = when {
            !boothTitle.isNullOrBlank() -> boothTitle
            !temiName.isNullOrBlank() -> temiName
            else -> null
        }

        val finText = arrivalName?.let { "$it 에 도착했습니다." }
            ?: "목적지에 도착했습니다."

        robot.speak(TtsRequest.create(finText, false))

        showMapLayoutInternal()
        goToUsabilitySurvey()
    }
    fun cancelNavigation(message: String? = null) {
        if (!isNavigating) return

        isNavigating = false

        // 🔹 예약된 설명 TTS 딜레이 제거
        ttsHandler.removeCallbacksAndMessages(null)

        // 이동 중단 + 말도 중단
        robot.stopMovement()
        robot.cancelAllTtsRequests()

        currentLocationKey?.let { key ->
            directionsRef.child(key).setValue(0).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "취소 -> Directions/$key 0으로 리셋")
                } else {
                    Log.e(TAG, "취소 -> Directions/$key 0으로 리셋 실패", task.exception)
                }
            }
        }

        if (!message.isNullOrEmpty()) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            robot.speak(TtsRequest.create(message, false))
        }

        showMapLayoutInternal()
    }

    fun showMapOnly() {
        isNavigating = false
        showMapLayoutInternal()
    }
}









//package com.example.exam2
//
//import android.util.Log
//import android.view.GestureDetector
//import android.view.MotionEvent
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.GestureDetectorCompat
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//// ✅ 에뮬레이터용: Temi SDK 에 의존하지 않는 버전
//class TemiNavigationHelper(
//    private val activity: AppCompatActivity,
//    private val showMapLayout: () -> Unit,     // 지도(기본) 화면으로 돌아갈 때 호출
//    private val showFaceLayout: () -> Unit,    // 안내 중 얼굴 화면으로 전환할 때 호출
//    dbUrl: String = "https://exam-afefa-default-rtdb.firebaseio.com",
//    private val directionsNode: String = "Directions"
//) {
//
//    companion object {
//        private const val TAG = "TemiNavigationHelper"
//    }
//
//    // ✅ Temi SDK 제거: robot 관련 멤버 삭제
//    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(dbUrl)
//    private val directionsRef: DatabaseReference = db.getReference(directionsNode)
//
//    private var isNavigating: Boolean = false
//    private var currentLocationKey: String? = null        // Firebase 에 기록할 키 (예: "rest")
//    private var currentTemiLocationName: String? = null   // Temi 에 저장된 위치 이름 (예: "소화기2")
//
//    private val gestureDetector: GestureDetectorCompat =
//        GestureDetectorCompat(
//            activity,
//            object : GestureDetector.SimpleOnGestureListener() {
//                override fun onDoubleTap(e: MotionEvent): Boolean {
//                    if (isNavigating) {
//                        cancelNavigation("안내를 취소했습니다. (에뮬레이터)")
//                        return true
//                    }
//                    return false
//                }
//            }
//        )
//
//    init {
//        if (activity is BaseActivity) {
//            activity.registerGlobalTouchHandler { ev ->
//                gestureDetector.onTouchEvent(ev)
//            }
//        }
//        showMapLayoutInternal()
//    }
//
//    // ✅ 원래는 Temi listener 등록하던 자리 → 에뮬레이터에선 로그만
//    fun onStart() {
//        Log.d(TAG, "onStart() - [에뮬레이터] Temi SDK 미사용")
//    }
//
//    // ✅ 원래는 Temi listener 해제 → 에뮬레이터에선 로그만
//    fun onStop() {
//        Log.d(TAG, "onStop() - [에뮬레이터] Temi SDK 미사용")
//    }
//
//    private fun showMapLayoutInternal() {
//        if (activity is BaseActivity) {
//            activity.setAutoReturnEnabled(true)
//        }
//
//        activity.runOnUiThread {
//            showMapLayout.invoke()
//        }
//    }
//
//    private fun showFaceLayoutInternal() {
//        if (activity is BaseActivity) {
//            activity.setAutoReturnEnabled(false)
//        }
//
//        activity.runOnUiThread {
//            showFaceLayout.invoke()
//        }
//    }
//
//    /**
//     * 길찾기 시작 (에뮬레이터 버전)
//     *
//     * @param locationKey      Firebase Directions 하위 노드 이름 (예: "rest", "boothA" 등)
//     * @param temiLocationName Temi 에 저장된 위치 이름 (robot.goTo() 에 들어가는 값이었음)
//     * @param guideMessage     출발할 때 Temi 가 말할 멘트 (비어 있으면 기본 문구 사용)
//     */
//    fun startNavigation(
//        locationKey: String,
//        temiLocationName: String,
//        guideMessage: String
//    ) {
//        // 🔹 0. 에뮬레이터에선 Temi 의 실제 위치 목록을 확인할 수 없으므로
//        //     위치 존재 여부 체크는 스킵하고, 바로 진행하도록 처리.
//        Log.d(
//            TAG,
//            "[에뮬레이터] startNavigation: locationKey=$locationKey, temiLocationName=$temiLocationName"
//        )
//
//        currentLocationKey = locationKey
//        currentTemiLocationName = temiLocationName
//
//        // 🔹 1. Firebase 에 상태 기록: Directions/locationKey = 1
//        directionsRef.child(locationKey).setValue(1).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d(TAG, "Directions/$locationKey -> 1 저장 완료 (에뮬레이터)")
//            } else {
//                Log.e(TAG, "Directions/$locationKey -> 1 저장 실패 (에뮬레이터)", task.exception)
//            }
//        }
//
//        // 🔹 2. 에뮬레이터에선 Temi 이동 대신 로그 + 토스트만
//        isNavigating = true
//        showFaceLayoutInternal()
//
//        val text = if (guideMessage.isBlank()) {
//            "[에뮬레이터] $temiLocationName 로 안내를 시작했다고 가정합니다."
//        } else {
//            "[에뮬레이터] $guideMessage"
//        }
//
//        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
//        Log.d(TAG, text)
//    }
//
//    // ✅ Temi 의 onGoToLocationStatusChanged 는 SDK 에서만 호출되므로 에뮬레이터에선 제거.
//    // 필요하다면 테스트용으로 수동 호출 가능한 메서드를 추가해도 됨.
//    //
//    // fun simulateCompleteForTest() {
//    //     handleComplete()
//    // }
//
//    private fun handleComplete() {
//        val locationKey = currentLocationKey
//        val temiName = currentTemiLocationName
//
//        isNavigating = false
//
//        if (locationKey != null) {
//            directionsRef.child(locationKey).setValue(0).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "도착 -> Directions/$locationKey 0으로 리셋 (에뮬레이션)")
//                } else {
//                    Log.e(TAG, "도착 -> Directions/$locationKey 0으로 리셋 실패 (에뮬레이션)", task.exception)
//                }
//            }
//        }
//
//        val finText = if (!temiName.isNullOrBlank()) {
//            "[에뮬레이터] $temiName 에 도착했다고 가정합니다."
//        } else {
//            "[에뮬레이터] 목적지에 도착했다고 가정합니다."
//        }
//
//        Toast.makeText(activity, finText, Toast.LENGTH_LONG).show()
//        Log.d(TAG, finText)
//
//        showMapLayoutInternal()
//    }
//
//    fun cancelNavigation(message: String? = null) {
//        if (!isNavigating) return
//
//        isNavigating = false
//
//        // ✅ Temi 실제 이동 중지(robot.stopMovement()) 제거
//        Log.d(TAG, "[에뮬레이터] 이동 취소 처리 (실제 로봇 없음)")
//
//        currentLocationKey?.let { key ->
//            directionsRef.child(key).setValue(0).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "취소 -> Directions/$key 0으로 리셋 (에뮬레이터)")
//                } else {
//                    Log.e(TAG, "취소 -> Directions/$key 0으로 리셋 실패 (에뮬레이터)", task.exception)
//                }
//            }
//        }
//
//        if (!message.isNullOrEmpty()) {
//            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//        }
//
//        showMapLayoutInternal()
//    }
//
//    fun showMapOnly() {
//        isNavigating = false
//        showMapLayoutInternal()
//    }
//}
