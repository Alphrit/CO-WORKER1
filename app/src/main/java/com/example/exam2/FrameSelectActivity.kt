package com.example.exam2

import android.content.Intent
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.android.material.card.MaterialCardView

class FrameSelectActivity : BaseActivity() {
    private var selectedFrame: Int = 0   // 0: none, 1: frame1, 2: frame2
    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setOnClickListener {
                // 현재 화면만 닫고 이전 화면으로
                onBackPressedDispatcher.onBackPressed()
            }
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


    @RequiresApi(Build.VERSION_CODES.R)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frameselect)

        // ===== 카메라 해상도 로그 출력 (간단한 확인 용) =====
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val configMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                val sizes = configMap?.getOutputSizes(SurfaceTexture::class.java)
                sizes?.forEach { size ->
                    Log.d(
                        "CameraResolution",
                        "cameraId=$cameraId -> ${size.width} x ${size.height}"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("CameraResolution", "Error reading camera resolutions", e)
        }
        // ====================================================

        // 상단 상태바 + 하단 내비게이션바 숨기기 (구 버전 호환 방식)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


        // 프레임 선택 시 버튼 생기게
        val card = findViewById<MaterialCardView>(R.id.navigation_button)

        // 처음에는 비활성화
        card.isEnabled = false
        card.isClickable = false
        card.alpha = 0.5f

        fun enableStart() {
            card.isEnabled = true
            card.isClickable = true
            card.alpha = 1f

        }

        //  takephoto_button 클릭 시 takephoto로 이동
        val navigationbutton = findViewById<MaterialCardView>(R.id.navigation_button)
        navigationbutton.setOnClickListener {
            if (selectedFrame == 0) return@setOnClickListener

            val intent = when (selectedFrame) {
                1 -> Intent(this, TakePhotoActivity_1::class.java)
                2 -> Intent(this, TakePhotoActivity_2::class.java)
                else -> Intent(this, TakePhotoActivity_1::class.java)
            }
            startActivity(intent)
        }
        // ------------------------- 추가된 부분 -------------------------
        val frame1 = findViewById<ImageView>(R.id.frame1)
        val frame2 = findViewById<ImageView>(R.id.frame2)

        // dp 단위 변환 함수
        fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()

        // 테두리 Drawable 생성
        fun makeBorderDrawable(): GradientDrawable = GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            setStroke(4.dp(), Color.parseColor("#007AFF"))  // 파란색 테두리
            cornerRadius = 12.dp().toFloat()
        }

        val borderOn = makeBorderDrawable()
        val borderOff = ColorDrawable(Color.TRANSPARENT)

        // 선택 토글 함수
        fun selectFrame(selected: ImageView) {
            frame1.background = if (selected == frame1) borderOn else borderOff
            frame2.background = if (selected == frame2) borderOn else borderOff

            selectedFrame = if (selected == frame1) 1 else 2
            enableStart()
        }

        // 클릭 이벤트
        frame1.setOnClickListener { selectFrame(frame1) }
        frame2.setOnClickListener { selectFrame(frame2) }
        // -------------------------------------------------------------
        setupTopBar()
    }

}