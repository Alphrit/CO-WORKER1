package com.example.exam2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

abstract class BaseActivity : AppCompatActivity() {

    // [변경] 사용자 지정 Firebase Realtime Database URL
    private val FIREBASE_URL = "https://exam-afefa-default-rtdb.firebaseio.com"

    // 현재 시간 "yyyy-MM-dd HH:mm" 형식
    private fun nowTimestamp(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    // [추가] 일반 버튼 클릭 로그 저장 함수
    // "click_logs"라는 키 아래에 [버튼이름 -> 로그] 형태로 쌓입니다.
    protected fun logButtonAction(buttonName: String) {
        // 지정된 URL의 DB 인스턴스 가져오기
        val db = FirebaseDatabase.getInstance(FIREBASE_URL)
        val logRef = db.getReference("click_logs").child(buttonName)

        val callerActivityName = this::class.java.simpleName

        val data = mapOf(
            "timestamp" to nowTimestamp(),
            "caller" to callerActivityName,
            "action" to "clicked"
        )

        // push()를 사용하여 클릭할 때마다 고유 ID로 로그가 쌓이게 함 (개수를 셀 수 있음)
        logRef.push().setValue(data)
    }

    // [수정] Firebase 에 도움 요청 기록 (URL 적용)
    private fun logHelpRequest(reasonKey: String) {
        val db = FirebaseDatabase.getInstance(FIREBASE_URL) // URL 적용
        val helpRef = db.getReference("help")

        val callerActivityName = this::class.java.simpleName

        val data = mapOf(
            "timestamp" to nowTimestamp(),
            "caller" to callerActivityName,
            "reason" to reasonKey
        )

        helpRef.child(reasonKey).push().setValue(data)
    }

    // 👇 Temi 등에서 전역 터치 이벤트를 듣기 위한 핸들러 목록
    private val globalTouchHandlers = mutableListOf<(MotionEvent) -> Unit>()

    fun registerGlobalTouchHandler(handler: (MotionEvent) -> Unit) {
        globalTouchHandlers.add(handler)
    }

    private val inactivityHandler = Handler(Looper.getMainLooper())
    private var returnHomeDialog: Dialog? = null
    private var autoReturnEnabled: Boolean = true
    private val inactivityTimeout = 120_000L // 2분 (기존 코드 유지)

    private val countdownDuration = 5_000L
    private val countdownHandler = Handler(Looper.getMainLooper())
    private var countdownSeconds = 5

    private val inactivityRunnable = Runnable {
        if (autoReturnEnabled) {
            showReturnHomeDialog()
        }
    }

    fun setAutoReturnEnabled(enabled: Boolean) {
        autoReturnEnabled = enabled
        if (enabled) {
            resetInactivityTimer()
        } else {
            cancelInactivityTimer()
            dismissReturnHomeDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (autoReturnEnabled) {
            resetInactivityTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        cancelInactivityTimer()
        dismissReturnHomeDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelInactivityTimer()
        dismissReturnHomeDialog()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (returnHomeDialog?.isShowing != true && autoReturnEnabled) {
            resetInactivityTimer()
        }
        ev?.let { e ->
            globalTouchHandlers.forEach { handler ->
                handler(e)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
        inactivityHandler.postDelayed(inactivityRunnable, inactivityTimeout)
    }

    private fun cancelInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
    }

    private fun showReturnHomeDialog() {
        if (returnHomeDialog?.isShowing == true) return

        returnHomeDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_return_home)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)

            val countdownTextView = findViewById<TextView>(R.id.tv_countdown)
            val cancelButton = findViewById<View>(R.id.btn_cancel)

            cancelButton.setOnClickListener {
                // [선택 사항] 취소 버튼도 로그를 남기고 싶다면:
                // logButtonAction("btnTimeoutCancel")
                dismissReturnHomeDialog()
                resetInactivityTimer()
            }

            countdownSeconds = 5
            startCountdown(countdownTextView)

            show()
        }
    }

    private fun startCountdown(countdownTextView: TextView) {
        countdownTextView.text = countdownSeconds.toString()

        if (countdownSeconds > 0) {
            countdownHandler.postDelayed({
                countdownSeconds--
                if (countdownSeconds > 0 && returnHomeDialog?.isShowing == true) {
                    startCountdown(countdownTextView)
                } else if (countdownSeconds == 0) {
                    returnToHome()
                }
            }, 1000)
        }
    }

    private fun dismissReturnHomeDialog() {
        countdownHandler.removeCallbacksAndMessages(null)
        returnHomeDialog?.dismiss()
        returnHomeDialog = null
    }

    private fun returnToHome() {
        dismissReturnHomeDialog()
        // [선택 사항] 타임아웃으로 인한 홈 복귀 로그
        // logButtonAction("timeoutReturnHome")

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    protected fun showHelpPopup() {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_help_popup)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)

            val btnAdminHelp = findViewById<Button>(R.id.btnAdminHelp)
            val btnNoSound = findViewById<Button>(R.id.btnNoSound)
            val btnTemiStopped = findViewById<Button>(R.id.btnTemiStopped)
            val btnScreenNotWorking = findViewById<Button>(R.id.btnScreenNotWorking)
            val btnCancel = findViewById<Button>(R.id.btnCancel)

            btnAdminHelp.setOnClickListener {
                dismiss()
                showAdminCallPopup("adminHelp")
            }
            btnNoSound.setOnClickListener {
                dismiss()
                showAdminCallPopup("noSound")
            }
            btnTemiStopped.setOnClickListener {
                dismiss()
                showAdminCallPopup("temiStopped")
            }
            btnScreenNotWorking.setOnClickListener {
                dismiss()
                showAdminCallPopup("screenNotWorking")
            }
            btnCancel.setOnClickListener { dismiss() }

            show()
        }
    }

    private fun showAdminCallPopup(reasonKey: String) {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_admin_call)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)

            val btnConfirm = findViewById<Button>(R.id.btnConfirm)
            btnConfirm.setOnClickListener {
                logHelpRequest(reasonKey)
                dismiss()
            }

            show()
        }
    }

    protected fun showStopPopup(onConfirm: () -> Unit) {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_stop_popup)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)

            val btnGoBack = findViewById<Button>(R.id.btnGoBack)
            val btnCancel = findViewById<Button>(R.id.btnCancel)

            btnGoBack.setOnClickListener {
                // [선택 사항] 종료 팝업에서 확인 버튼 로그
                // logButtonAction("btnStopConfirm")
                dismiss()
                onConfirm()
            }

            btnCancel.setOnClickListener { dismiss() }

            show()
        }
    }

    protected fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    protected fun navigateToCoshowtiIntro() {
        val intent = Intent(this, CoshowtiActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    /**
     * 공통 상단바 세팅
     * [수정] 각 버튼 클릭 시 logButtonAction() 호출 추가
     */
    protected fun setupCommonTopBar(
        topBarView: View,
        backAction: (() -> Unit)? = null,
        homeAction: (() -> Unit)? = { navigateToHome() }
    ) {
        val btnBack = topBarView.findViewById<ImageButton>(R.id.btnBack)
        val btnHome = topBarView.findViewById<ImageButton>(R.id.btnHome)
        val btnHelp = topBarView.findViewById<ImageButton>(R.id.btnHelp)

        btnBack?.setOnClickListener {
            // 1. 뒤로가기 버튼 로그 전송
            logButtonAction("btnBack")
            // 2. 실제 뒤로가기 동작 수행
            backAction?.invoke()
        }

        btnHome?.setOnClickListener {
            // 1. 홈 버튼 로그 전송
            logButtonAction("btnHome")
            // 2. 실제 홈 이동 동작 수행
            homeAction?.invoke()
        }

        btnHelp?.setOnClickListener {
            // 1. 도움말 버튼 로그 전송
            logButtonAction("btnHelp")
            // 2. 팝업 표시
            showHelpPopup()
        }
    }
}