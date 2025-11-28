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
    // 현재 시간 "yyyy-MM-dd HH:mm" 형식
    private fun nowTimestamp(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    // Firebase 에 도움 요청 기록
    private fun logHelpRequest(reasonKey: String) {
        val db = FirebaseDatabase.getInstance()
        val helpRef = db.getReference("help")

        // 이 BaseActivity 를 상속한 실제 액티비티 이름
        val callerActivityName = this::class.java.simpleName

        val data = mapOf(
            "timestamp" to nowTimestamp(),
            "caller" to callerActivityName,
            "reason" to reasonKey
        )

        // DialogHelpPopup 에서 쓰던 구조와 동일:
        // help/adminHelp, help/noSound, help/temiStopped, help/screenNotWorking
        helpRef.child(reasonKey).push().setValue(data)
    }
    // 👇 Temi 등에서 전역 터치 이벤트를 듣기 위한 핸들러 목록
    private val globalTouchHandlers = mutableListOf<(MotionEvent) -> Unit>()

    fun registerGlobalTouchHandler(handler: (MotionEvent) -> Unit) {
        globalTouchHandlers.add(handler)
    }

    private val inactivityHandler = Handler(Looper.getMainLooper())
    private var returnHomeDialog: Dialog? = null
    private var autoReturnEnabled: Boolean = true   // 👈 자동 홈복귀 on/off 스위치

    // 30초 무조작 타임아웃
    private val inactivityTimeout = 120_000L

    // 5초 카운트다운 (상수는 그대로 두었지만 직접 쓰진 않음)
    private val countdownDuration = 5_000L
    private val countdownHandler = Handler(Looper.getMainLooper())
    private var countdownSeconds = 5

    // 30초 후 실행되는 Runnable
    private val inactivityRunnable = Runnable {
        if (autoReturnEnabled) {
            showReturnHomeDialog()
        }
    }

    /**
     * Temi 안내 중에는 false, 일반 화면에서는 true 로 설정
     */
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
        // 타이머는 onResume 에서 시작
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

    /**
     * 모든 터치 이벤트를 가로채서
     * - 자동 홈복귀 타이머 리셋
     * - Temi 더블탭 제스처 리스너에게도 전달
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (returnHomeDialog?.isShowing != true && autoReturnEnabled) {
            resetInactivityTimer()
        }

        // TemiNavigationHelper 에서 등록한 제스처 리스너 호출
        ev?.let { e ->
            globalTouchHandlers.forEach { handler ->
                handler(e)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    /** 30초 타이머 리셋 */
    private fun resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
        inactivityHandler.postDelayed(inactivityRunnable, inactivityTimeout)
    }

    /** 30초 타이머 취소 */
    private fun cancelInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
    }

    /** 5초 카운트다운 팝업 표시 */
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
                // 취소 -> 팝업 닫고 다시 30초 타이머 시작
                dismissReturnHomeDialog()
                resetInactivityTimer()
            }

            countdownSeconds = 5
            startCountdown(countdownTextView)

            show()
        }
    }

    /** 5초 카운트다운 로직 */
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

    /** 카운트다운 + 팝업 닫기 */
    private fun dismissReturnHomeDialog() {
        countdownHandler.removeCallbacksAndMessages(null)
        returnHomeDialog?.dismiss()
        returnHomeDialog = null
    }

    /** 메인으로 이동 */
    private fun returnToHome() {
        dismissReturnHomeDialog()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    /** HELP 버튼 팝업 */
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
                // 관리자 호출 일반
                showAdminCallPopup("adminHelp")
            }
            btnNoSound.setOnClickListener {
                dismiss()
                // 소리가 안 나요
                showAdminCallPopup("noSound")
            }
            btnTemiStopped.setOnClickListener {
                dismiss()
                // Temi 가 멈췄어요
                showAdminCallPopup("temiStopped")
            }
            btnScreenNotWorking.setOnClickListener {
                dismiss()
                // 화면이 작동하지 않아요
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

    /** 설문 중지 팝업 */
    protected fun showStopPopup(onConfirm: () -> Unit) {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_stop_popup)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)

            val btnGoBack = findViewById<Button>(R.id.btnGoBack)
            val btnCancel = findViewById<Button>(R.id.btnCancel)

            btnGoBack.setOnClickListener {
                dismiss()
                onConfirm()
            }

            btnCancel.setOnClickListener { dismiss() }

            show()
        }
    }

    /** 홈으로 이동 */
    protected fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    /** 코쇼티 인트로로 이동 */
    protected fun navigateToCoshowtiIntro() {
        val intent = Intent(this, CoshowtiActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finishAffinity()
    }

    /**
     * 공통 상단바 세팅
     */
    protected fun setupCommonTopBar(
        topBarView: View,
        backAction: (() -> Unit)? = null,
        homeAction: (() -> Unit)? = { navigateToHome() }
    ) {
        val btnBack = topBarView.findViewById<ImageButton>(R.id.btnBack)
        val btnHome = topBarView.findViewById<ImageButton>(R.id.btnHome)
        val btnHelp = topBarView.findViewById<ImageButton>(R.id.btnHelp)

        btnBack?.setOnClickListener { backAction?.invoke() }
        btnHome?.setOnClickListener { homeAction?.invoke() }
        btnHelp?.setOnClickListener { showHelpPopup() }
    }
}
