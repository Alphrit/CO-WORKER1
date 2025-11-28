package com.example.exam2

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.TextView
import android.content.Intent
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class PrintPhotoActivity : BaseActivity() {
    private lateinit var loadingText: TextView

    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.printphoto)  // printphoto.xml과 연결
        // 상단 상태바 + 하단 내비게이션바 숨기기 (구 버전 호환 방식)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        loadingText = findViewById(R.id.loadingtext)
        loadingText.visibility = View.VISIBLE


        // TakePhotoActivity_1 에서 넘어온 4장의 사진 경로
        val paths = intent.getStringArrayListExtra("photo_paths")
        if (paths == null || paths.isEmpty()) return

        // 파일에서 비트맵 로드
        val bitmaps = paths.mapNotNull { BitmapFactory.decodeFile(it) }
        if (bitmaps.isEmpty()) return

        // 1) TakePhotoActivity에서 넘어온 frame_type 읽기
        val frameType = intent.getIntExtra("frame_type", 1)

        // 2) frame_type에 따라 sticker1 / sticker2 선택
        val frameResId = when (frameType) {
            2 -> R.drawable.sticker2
            else -> R.drawable.sticker1
        }

        // 3) 프레임 비트맵 로드
        val frameBitmap = BitmapFactory.decodeResource(resources, frameResId) ?: return

        // 프레임 크기를 기준으로 최종 비트맵 생성
        val frameWidth = frameBitmap.width
        val frameHeight = frameBitmap.height
        val combined = Bitmap.createBitmap(
            frameWidth,
            frameHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(combined)


        val baseWidthDp = 206.5f
        val baseHeightDp = 627.9f
        val scaleX = frameWidth / baseWidthDp
        val scaleY = frameHeight / baseHeightDp
        val photoWidthPx = (182.7f * scaleX).toInt()
        val photoHeightPx = (126f * scaleY).toInt()
        val firstTopPx = (19.6f * scaleY).toInt()
        val sideMarginPx = (11.9f * scaleX).toInt()
        val secondOffsetPx = (9.8f * scaleY).toInt()
        val thirdOffsetPx = (10.5f * scaleY).toInt()
        val fourthOffsetPx = (9.8f * scaleY).toInt()

        val topPositions = IntArray(4)
        topPositions[0] = firstTopPx
        topPositions[1] = topPositions[0] + photoHeightPx + secondOffsetPx
        topPositions[2] = topPositions[1] + photoHeightPx + thirdOffsetPx
        topPositions[3] = topPositions[2] + photoHeightPx + fourthOffsetPx

        val leftPx = sideMarginPx
        val rightPx = frameWidth - sideMarginPx

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val count = minOf(4, bitmaps.size)
        for (i in 0 until count) {
            val bmp = bitmaps[i]
            val topPx = topPositions[i]
            val bottomPx = topPx + photoHeightPx

            val dstRect = Rect(leftPx, topPx, rightPx, bottomPx)
            canvas.drawBitmap(bmp, null, dstRect, paint)
        }


        canvas.drawBitmap(
            frameBitmap,
            null,
            Rect(0, 0, frameWidth, frameHeight),
            null
        )


        val img = findViewById<ImageView>(R.id.frame1final)
        img.setImageBitmap(combined)

        // QR 이미지를 표시할 ImageView
        val qrImage = findViewById<ImageView>(R.id.qr)

        // 최종 스티커 이미지를 Firebase Storage에 업로드하고, 해당 URL로 QR 생성
        uploadFinalSticker(combined, qrImage)

        // retry_button을 눌렀을 때 다시 촬영 화면으로 이동
        val retryButton = findViewById<TextView>(R.id.retry_button)
        retryButton?.setOnClickListener {
            // frameType에 따라 다시 촬영 화면 분기
            val intent = if (frameType == 2) {
                Intent(this, TakePhotoActivity_2::class.java)
            } else {
                Intent(this, TakePhotoActivity_1::class.java)
            }
            startActivity(intent)
            finish()
        }

        // surveybutton 클릭 시 usability 화면으로 이동
        val surveyButton = findViewById<ImageView>(R.id.surveybutton)
        surveyButton?.setOnClickListener {
            val intent = Intent(this, UsabilityActivity::class.java)
            intent.putExtra("surveyType", "스티커사진")
            startActivity(intent)
        }
        setupTopBar()
    }

    private fun uploadFinalSticker(bitmap: Bitmap, qrImage: ImageView) {
        // 비트맵을 JPEG 형식의 바이트 배열로 변환
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()

        // Firebase Storage 참조 생성 (stickers/타임스탬프.jpg 경로)
        val storage = FirebaseStorage.getInstance("gs://exam-afefa.firebasestorage.app")
        val filename = "stickers/${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(filename)

        loadingText.visibility = View.VISIBLE

        // 업로드 시작
        ref.putBytes(data)
            .addOnSuccessListener {
                // 업로드 성공 시 다운로드 URL을 가져와서 QR 코드 생성
                ref.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        // URL로부터 QR 비트맵 생성
                        val qrBitmap = generateQrBitmap(downloadUrl)
                        qrImage.setImageBitmap(qrBitmap)
                        loadingText.visibility = View.GONE
                        Toast.makeText(this, "스티커 업로드 및 QR 생성 완료", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(this, "URL 가져오기 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                // 실패 시 에러 로그 및 토스트
                e.printStackTrace()
                Toast.makeText(this, "업로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generateQrBitmap(text: String, size: Int = 400): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}