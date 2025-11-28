package com.example.exam2

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import android.graphics.Color

class TodaysEventActivity : BaseActivity() {

    companion object {
        private const val DB_URL = "https://exam-afefa-default-rtdb.firebaseio.com"
    }

    private lateinit var imgEventBackground: ImageView
    private lateinit var txtPage: TextView
    private lateinit var txtClock: TextView
    private lateinit var btnDownload: MaterialButton

    private lateinit var temiNavigator: TemiNavigationHelper

    // 보여줄 이미지 목록
    private val eventImages = listOf(
        R.drawable.fri_1,
        R.drawable.fri_2,
        R.drawable.sat_1,
        R.drawable.sat_2
    )
    private data class EventTimeRange(
        val startMillis: Long,
        val endMillis: Long
    )

    private val seoulTimeZone = java.util.TimeZone.getTimeZone("Asia/Seoul")

    private fun eventTimeMillis(
        year: Int,
        month: Int,   // 1~12
        day: Int,
        hour: Int,
        minute: Int
    ): Long {
        return Calendar.getInstance(seoulTimeZone).apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Calendar 는 0부터 시작
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // eventImages 와 index 를 맞춰서 시간 범위 설정
    private val eventTimeRanges = listOf(
        // index 0 : fri_1 → 2025.11.28 10:00 ~ 11:00
        EventTimeRange(
            startMillis = eventTimeMillis(2025, 11, 28, 10, 0),
            endMillis   = eventTimeMillis(2025, 11, 28, 11, 0)
        ),
        // index 1 : fri_2 → 2025.11.28 15:00 ~ 16:30
        EventTimeRange(
            startMillis = eventTimeMillis(2025, 11, 28, 15, 0),
            endMillis   = eventTimeMillis(2025, 11, 28, 16, 30)
        ),
        // index 2 : sat_1 → 2025.11.29 14:00 ~ 15:00
        EventTimeRange(
            startMillis = eventTimeMillis(2025, 11, 29, 14, 0),
            endMillis   = eventTimeMillis(2025, 11, 29, 15, 0)
        ),
        // index 3 : sat_2 → 2025.11.29 10:00 ~ 11:00
        EventTimeRange(
            startMillis = eventTimeMillis(2025, 11, 29, 10, 0),
            endMillis   = eventTimeMillis(2025, 11, 29, 11, 0)
        )
    )

    // 현재 시간이 해당 index 페이지의 운영 시간 안에 있는지 체크
    private fun isCurrentTimeInEvent(index: Int): Boolean {
        if (index < 0 || index >= eventTimeRanges.size) return true   // 범위 밖이면 제한 없음

        val range = eventTimeRanges[index]
        val nowMillis = Calendar.getInstance(seoulTimeZone).timeInMillis

        return nowMillis in range.startMillis..range.endMillis
    }

    private var currentIndex = 0   // 현재 몇 번째 이미지인지(0부터 시작)

    private val clockHandler = Handler(Looper.getMainLooper())
    private val clockRunnable = object : Runnable {
        override fun run() {
            val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).time

            val formatter = SimpleDateFormat("HH:mm", Locale.KOREA).apply {
                timeZone = TimeZone.getTimeZone("Asia/Seoul")
            }

            txtClock.text = formatter.format(now)
            clockHandler.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) 처음 진입 시 오늘의 행사 화면 띄우기
        setContentView(R.layout.activity_todays_event)

        // 2) 상단바 세팅
        setupTopBar()

        // 3) 오늘의 행사 화면 뷰/리스너 바인딩
        bindAndInitViewsAfterSetContent(isInitial = true)

        // 4) Temi Navigation Helper 초기화
        temiNavigator = TemiNavigationHelper(
            activity = this,
            showMapLayout = {
                // 안내가 끝나고 다시 "오늘의 행사" 화면으로 돌아올 때 호출
                runOnUiThread {
                    setContentView(R.layout.activity_todays_event)
                    setupTopBar()
                    bindAndInitViewsAfterSetContent(isInitial = false)
                }
            },
            showFaceLayout = {
                // 안내 중 "얼굴 화면" 보여줄 때 호출
                runOnUiThread {
                    setContentView(R.layout.activity_face)
                    setupTopBar()
                }
            },
            dbUrl = DB_URL,
            directionsNode = "Directions"
        )
    }

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

    /**
     * 상단 공통바(뒤로, 홈, 도움말) 설정
     * 레이아웃에 없으면 null 이라 그냥 무시됨.
     */
    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE   // 이 화면은 뒤로가기 숨김
            isEnabled = false
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnHome?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
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
     * setContentView() 이후 매번 호출해서
     * - 시계
     * - 오른쪽 메뉴(tileTime, tileMap)
     * - 이벤트 이미지 / 페이지 텍스트
     * - 행사장 안내 버튼
     * 을 다시 바인딩한다.
     */
    private fun bindAndInitViewsAfterSetContent(isInitial: Boolean) {
        imgEventBackground = findViewById(R.id.imgEventBackground)
        txtPage = findViewById(R.id.txtPage)
        txtClock = findViewById(R.id.txtClock)
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

        // 오른쪽 메뉴 - 테마별 안내
        val tileTime = findViewById<MaterialCardView>(R.id.tileTime)
        tileTime.setOnClickListener {
            startActivity(Intent(this, InformationByThemeActivity::class.java))
        }

        // 오른쪽 메뉴 - 행사장 지도
        val tileMap = findViewById<MaterialCardView>(R.id.tileMap)
        tileMap.setOnClickListener {
            startActivity(Intent(this, MapCoShowActivity::class.java))
        }

        val btnViewSem = findViewById<MaterialButton>(R.id.btnViewSem)
        btnViewSem?.setOnClickListener {
            // 현재 currentIndex 에 해당하는 이벤트 시간 안인지 확인
            if (isCurrentTimeInEvent(currentIndex)) {
                // 운영 중이면 바로 이동
                startMainHallNavigation()
            } else {
                // 운영 시간이 아니면 팝업으로 한 번 더 확인
                showNotRunningDialog {
                    // "예" 를 눌렀을 때만 이동
                    startMainHallNavigation()
                }
            }
        }

        // 처음 or 재진입 시 현재 이벤트 표시
        if (isInitial) {
            showEvent(0)
        } else {
            showEvent(currentIndex)
        }

        // txtPage 좌/우 터치로 이전/다음 이벤트 보기
        txtPage.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val total = eventImages.size
                if (total <= 1) return@setOnTouchListener true

                val x = event.x
                val half = v.width / 2f

                val targetIndex = if (x < half) {
                    // 👈 왼쪽 → 이전
                    if (currentIndex - 1 < 0) total - 1 else currentIndex - 1
                } else {
                    // 👉 오른쪽 → 다음
                    (currentIndex + 1) % total
                }

                showEvent(targetIndex)
            }
            true
        }

        // 시계는 처음 진입할 때만 시작
        if (isInitial) {
            startClock()
        }
    }

    private fun showNotRunningDialog(onConfirm: () -> Unit) {
        // 1) 커스텀 레이아웃 inflate
        val dialogView = layoutInflater.inflate(R.layout.dialog_event_notnow, null)

        // 2) AlertDialog 생성 + 커스텀 뷰 적용
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // 배경을 투명하게 해서 CardView 모양만 보이도록
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        // 3) 버튼 바인딩
        val btnGoBack =
            dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnGoBack)
        val btnCancel =
            dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnCancel)

        // "행사장으로 이동하기" → onConfirm 호출
        btnGoBack.setOnClickListener {
            dialog.dismiss()
            onConfirm()
        }

        // "취소" → 팝업만 닫기
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // 4) 다이얼로그 표시
        dialog.show()
    }

    private fun startMainHallNavigation() {
        val locationKey = "메인홀"
        val temiLocationName = "메인홀"
        val guideMessage = "$temiLocationName 로 안내를 시작합니다."

        temiNavigator.startNavigation(
            locationKey = locationKey,
            temiLocationName = temiLocationName,
            guideMessage = guideMessage
        )
    }



    private fun startClock() {
        clockHandler.removeCallbacks(clockRunnable)
        clockHandler.post(clockRunnable)
    }

    private fun showEvent(index: Int) {
        currentIndex = index
        imgEventBackground.setImageResource(eventImages[index])

        // 페이지 표시: "< 1 / 3 >" 이런 형식
        val total = eventImages.size
        txtPage.text = "< ${index + 1} / $total >"
    }
}
