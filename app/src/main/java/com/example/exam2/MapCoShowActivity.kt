package com.example.exam2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton

class MapCoShowActivity : BaseActivity() {

    companion object {
        private const val DB_URL = "https://exam-afefa-default-rtdb.firebaseio.com"

        private const val RESTROOM_LOCATION_KEY = "화장실"
        private const val RESTROOM_LOCATION_NAME = "화장실"

        private const val ENTRANCE_LOCATION_KEY = "입구"
        private const val ENTRANCE_LOCATION_NAME = "입구"

        private const val EXIT_LOCATION_KEY = "출구"
        private const val EXIT_LOCATION_NAME = "출구"

        private const val MAINHALL_LOCATION_KEY = "메인홀"
        private const val MAINHALL_LOCATION_NAME = "메인홀"
    }

    private lateinit var temiNavigator: TemiNavigationHelper
    private lateinit var txtClock: TextView

    // 시계용 핸들러
    private val clockHandler = Handler(Looper.getMainLooper())
    private val clockRunnable = object : Runnable {
        override fun run() {
            val now = java.util.Calendar.getInstance(
                java.util.TimeZone.getTimeZone("Asia/Seoul")
            ).time
            val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.KOREA).apply {
                timeZone = java.util.TimeZone.getTimeZone("Asia/Seoul")
            }
            // 최신 txtClock 뷰에 시간 표시
            txtClock.text = formatter.format(now)
            clockHandler.postDelayed(this, 1000L)
        }
    }

    // 더블탭 취소용 제스처
    private lateinit var gestureDetector: GestureDetector
    private lateinit var btnDownload: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) 처음 진입 시 지도 레이아웃 표시
        setContentView(R.layout.activity_map_coshow)

        // 2) 상단바 버튼(Home, Help 등) 세팅
        setupTopBar()

        // 3) 뷰 바인딩 + 클릭 리스너 + 시계 시작(처음 한 번만)
        bindAndInitViewsAfterSetContent(isInitial = true)

        // 4) Temi Navigation Helper 초기화
        //    - 길안내 시작 시 얼굴 화면으로 전환(showFaceLayout)
        //    - 안내 종료/취소 시 다시 지도 화면으로 전환(showMapLayout)
        temiNavigator = TemiNavigationHelper(
            activity = this,
            showMapLayout = {
                runOnUiThread {
                    setContentView(R.layout.activity_map_coshow)
                    setupTopBar()
                    bindAndInitViewsAfterSetContent(isInitial = false)
                }
            },
            showFaceLayout = {
                runOnUiThread {
                    setContentView(R.layout.activity_face)
                    // 얼굴 화면에도 상단에 홈/도움말 버튼이 있다면 동일하게 설정 가능
                    setupTopBar()
                }
            },
            dbUrl = DB_URL,
            directionsNode = "Directions"
        )
        val btnViewSem = findViewById<MaterialButton>(R.id.btnViewSem)

        btnViewSem.setOnClickListener {
            // 1) 커스텀 다이얼로그 레이아웃 inflate
            val dialogView = layoutInflater.inflate(R.layout.dialog_facilities, null)

            // 2) 다이얼로그 생성
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            // 3) dialog_facilities.xml 안의 버튼들 가져오기
            val btnCancel = dialogView.findViewById<View>(R.id.btnCancel)
            val btnRestPlace = dialogView.findViewById<View>(R.id.btnRestPlace)
            val btnMainHall = dialogView.findViewById<View>(R.id.btnMainHall)
            val btnEntrance = dialogView.findViewById<View>(R.id.btnEntrance)
            val btnExit = dialogView.findViewById<View>(R.id.btnExit)

            // 4) 각 버튼 클릭 시 동작 설정
            btnCancel.setOnClickListener {
                dialog.dismiss()      // 그냥 닫기만
            }

            btnRestPlace.setOnClickListener {
                onRestroomClicked()   // 화장실 버튼 동작
                dialog.dismiss()
            }

            btnMainHall.setOnClickListener {
                onMainhallClicked()
                dialog.dismiss()
            }

            btnEntrance.setOnClickListener {
                onEntranceClicked()
                dialog.dismiss()
            }

            btnExit.setOnClickListener {
                onExitClicked()
                dialog.dismiss()
            }

            // 5) 다이얼로그 표시
            dialog.show()
        }


        btnDownload = findViewById(R.id.btnDownload)
        btnDownload.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_pamphlet_qr, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnClose = dialogView.findViewById<ImageButton>(R.id.btnClose)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        // 5) 더블탭 제스처: 안내 중이면 취소
        gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    temiNavigator.cancelNavigation("안내를 취소했습니다.")
                    return true
                }
            }
        )
    }

    /**
     * 상단 공통바(뒤로, 홈, Help) 설정
     * 레이아웃에 해당 id 가 없으면 null 이라서 그냥 무시됨.
     */
    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE
            isEnabled = false
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnHome?.setOnClickListener {
            val intent = Intent(this@MapCoShowActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finishAffinity()
        }

        val btnHelp = findViewById<ImageButton>(R.id.btnHelp)
        btnHelp?.setOnClickListener {
            showHelpPopup()
        }
    }

    /**
     * setContentView() 이후에 항상 호출해서
     * - 시계 텍스트뷰
     * - 오른쪽 메뉴 타일
     * - 휴게/출입구/안내데스크 버튼
     * 을 다시 바인딩하고 리스너를 붙임
     */
    private fun bindAndInitViewsAfterSetContent(isInitial: Boolean = false) {
        // 시계
        txtClock = findViewById(R.id.txtClock)

        // "테마별 안내"로 이동
        val tileTime: View? = findViewById(R.id.tileTime)
        tileTime?.setOnClickListener {
            startActivity(Intent(this, InformationByThemeActivity::class.java))
        }

        // "오늘의 행사"로 이동 (레이아웃에 있을 경우)
        val tileEvent: View? = findViewById(R.id.tileEvent)
        tileEvent?.setOnClickListener {
            startActivity(Intent(this, TodaysEventActivity::class.java))
        }

        // 휴게시설 버튼
        val restroom: View? = findViewById(R.id.restroom)
        restroom?.setOnClickListener { onRestroomClicked() }

        // 출입구 버튼
        val entrance: View? = findViewById(R.id.entrance)
        entrance?.setOnClickListener { onEntranceClicked() }

        // 안내데스크 버튼
        val exit: View? = findViewById(R.id.exit)
        exit?.setOnClickListener { onExitClicked() }

        // 시계는 처음 한 번만 Runnable 시작
        if (isInitial) {
            startClock()
        }
    }

    private fun startClock() {
        clockHandler.removeCallbacks(clockRunnable)
        clockHandler.post(clockRunnable)
    }

    // Temi 콜백 등록 / 해제
    override fun onStart() {
        super.onStart()
        temiNavigator.onStart()
    }

    override fun onStop() {
        temiNavigator.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        clockHandler.removeCallbacks(clockRunnable)
    }

    // 화면 전체 터치에서 더블탭 감지
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let { gestureDetector.onTouchEvent(it) }
        return super.dispatchTouchEvent(ev)
    }

    // ----- 버튼 클릭 시 Temi 이동 요청 -----

    private fun onRestroomClicked() {
        temiNavigator.startNavigation(
            locationKey = RESTROOM_LOCATION_KEY,
            temiLocationName = RESTROOM_LOCATION_NAME,
            guideMessage = "${RESTROOM_LOCATION_NAME}로 안내중입니다."
        )
    }

    private fun onEntranceClicked() {
        temiNavigator.startNavigation(
            locationKey = ENTRANCE_LOCATION_KEY,
            temiLocationName = ENTRANCE_LOCATION_NAME,
            guideMessage = "${ENTRANCE_LOCATION_NAME}로 안내중입니다."
        )
    }

    private fun onExitClicked() {
        temiNavigator.startNavigation(
            locationKey = EXIT_LOCATION_KEY,
            temiLocationName = EXIT_LOCATION_NAME,
            guideMessage = "${EXIT_LOCATION_NAME}로 안내중입니다."
        )
    }

    private fun onMainhallClicked() {
        temiNavigator.startNavigation(
            locationKey = MAINHALL_LOCATION_KEY,
            temiLocationName = MAINHALL_LOCATION_NAME,
            guideMessage = "${MAINHALL_LOCATION_NAME}로 안내중입니다."
        )
    }
}
