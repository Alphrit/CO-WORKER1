package com.example.exam2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.graphics.Bitmap
import android.os.CountDownTimer
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs


class TakePhotoActivity_1 : BaseActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var progressBar: ProgressBar
    private lateinit var countdownText: TextView
    private lateinit var shotCountText: TextView

    private lateinit var stickerOverlayRight1: ImageView
    private lateinit var stickerOverlayRight2: ImageView
    private lateinit var stickerOverlayBottom: TextView

    private val totalShots = 4
    private var shotIndex = 0

    private val capturedPhotoPaths = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takephoto_1)
        setAutoReturnEnabled(false)


        // UI 초기화
        previewView = findViewById(R.id.previewView)
        previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
//        previewView.scaleType = PreviewView.ScaleType.FIT_CENTER
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

        stickerOverlayRight1 = findViewById(R.id.stickerOverlayRight1)
        stickerOverlayRight2 = findViewById(R.id.stickerOverlayRight2)
        stickerOverlayRight1.visibility = View.GONE
        stickerOverlayRight2.visibility = View.GONE

        stickerOverlayBottom = findViewById(R.id.stickerOverlayBottom)
        stickerOverlayBottom.visibility = View.GONE

        progressBar = findViewById(R.id.captureProgress)
        countdownText = findViewById(R.id.countdowntext)
        shotCountText = findViewById(R.id.photoIndexText)
        shotCountText.text = "1/4"

        // 카메라 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            previewView.post { startCamera() }
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) previewView.post { startCamera() }
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val rotation = previewView.display?.rotation ?: Surface.ROTATION_0

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .build()
                .apply { setSurfaceProvider(previewView.surfaceProvider) }

            val camSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA))
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, camSelector, preview)

            // 첫번째 샷 시작
            startNextShot()
        }, ContextCompat.getMainExecutor(this))
    }

    // 현재 프리뷰 화면을 비트맵으로 캡쳐해서 캐시 폴더에 저장
    private fun captureCurrentPreviewFrame() {
        // PreviewView에 그려진 비트맵을 그대로 가져옴 (프리뷰 비율/크롭 상태 그대로)
        val bitmap: Bitmap = previewView.bitmap ?: return

        // 샷 인덱스별로 파일 이름 지정 (예: shot_1.jpg, shot_2.jpg ...)
        val outFile = File(cacheDir, "shot_${shotIndex}.jpg")
        FileOutputStream(outFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }

        capturedPhotoPaths.add(outFile.absolutePath)
    }

    // 다음 샷 + 카운트다운
    private fun startNextShot() {
        shotIndex++
        if (shotIndex <= totalShots) {
            shotCountText.text = "$shotIndex/$totalShots"
        }
        if (shotIndex > totalShots) return

        updateStickerOverlayForCurrentShot()
        startCountdown(9000)
    }

    private fun startCountdown(totalMs: Int) {
        progressBar.max = totalMs
        progressBar.progress = totalMs
        countdownText.text = (totalMs / 1000).toString()

        object : CountDownTimer(totalMs.toLong(), 50L) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = millisUntilFinished.toInt().coerceAtLeast(0)
                countdownText.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                progressBar.progress = 0
                countdownText.text = "0"

                // 1) 현재 프리뷰 모습 그대로 한 장 캡쳐
                captureCurrentPreviewFrame()

                // 2) 4장 모두 찍었으면 결과 화면으로 이동, 아니면 다음 샷 진행
                if (shotIndex < totalShots) {
                    startNextShot()
                } else {
                    // 모든 샷 끝나면 오버레이 숨기고 printphoto 화면으로 이동
                    stickerOverlayRight1.visibility = View.GONE
                    stickerOverlayRight2.visibility = View.GONE
                    stickerOverlayBottom.visibility = View.GONE

                    val intent = Intent(this@TakePhotoActivity_1, PrintPhotoActivity::class.java)
                    intent.putStringArrayListExtra("photo_paths", ArrayList(capturedPhotoPaths))
                    intent.putExtra("frame_type", 1)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    // 샷번호에 맞는 스티커 세팅
    private fun updateStickerOverlayForCurrentShot() {
        stickerOverlayRight1.visibility = View.GONE
        stickerOverlayRight2.visibility = View.GONE
        stickerOverlayBottom.visibility = View.GONE

        when (shotIndex) {
            1 -> {
                stickerOverlayRight1.setImageResource(R.drawable.sticker1_1)
                stickerOverlayRight1.visibility = View.VISIBLE
            }
            2 -> {
                stickerOverlayBottom.text = "코쇼 오길 잘햇다"
                stickerOverlayBottom.visibility = View.VISIBLE
            }
            3 -> {
                stickerOverlayRight2.setImageResource(R.drawable.sticker1_3)
                stickerOverlayRight2.visibility = View.VISIBLE
            }
            4 -> {
                stickerOverlayBottom.text = "2025 코쇼 굿이예요 구웃"
                stickerOverlayBottom.visibility = View.VISIBLE
            }
        }
    }

    // 비율 표기를 위한 GCD
    private fun gcdForRatio(a: Int, b: Int): Int {
        var x = a
        var y = b
        while (y != 0) {
            val t = x % y
            x = y
            y = t
        }
        return if (x == 0) 1 else abs(x)
    }
}