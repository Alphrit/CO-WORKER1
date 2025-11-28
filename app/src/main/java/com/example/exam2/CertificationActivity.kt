package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class CertificationActivity : BaseActivity() {
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
        setContentView(R.layout.certification) // certification.xml 연결

        val dateTextView = findViewById<TextView>(R.id.date)
        val hashTextView = findViewById<TextView>(R.id.hash)
        val usernameTextView = findViewById<TextView>(R.id.username)
        val qrImageView = findViewById<ImageView>(R.id.qr)

        // 파이어베이스에서 최신 데이터 불러와서 date, hash, username 세팅 후
        // 인증서 화면을 캡처하여 Storage에 업로드하고, 해당 URL로 QR 생성
        loadLatestCertificateData(
            dateView = dateTextView,
            hashView = hashTextView,
            usernameView = usernameTextView,
            qrView = qrImageView
        )
        setupTopBar()
    }

    private fun loadLatestCertificateData(
        dateView: TextView,
        hashView: TextView,
        usernameView: TextView,
        qrView: ImageView
    ) {
        val database = FirebaseDatabase.getInstance(
            "https://exam-afefa-default-rtdb.firebaseio.com"
        )
        val ref = database.getReference("scores")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChildren()) {
                    dateView.text = "--"
                    hashView.text = "--"
                    usernameView.text = "GUEST"
                    return
                }

                val userCount = snapshot.childrenCount.toInt()

                // 1) 최신 기록 찾기 (timestamp 최대값 기준)
                var latest: DataSnapshot? = null
                var latestTimestamp = Long.MIN_VALUE
                for (child in snapshot.children) {
                    val ts = child.child("timestamp").getValue(Long::class.java) ?: continue
                    if (ts > latestTimestamp) {
                        latestTimestamp = ts
                        latest = child
                    }
                }

                if (latest == null) {
                    dateView.text = "--"
                    hashView.text = "--"
                    usernameView.text = "GUEST"
                    return
                }

                // 공통으로 쓸 값들
                val timestamp = latest.child("timestamp").getValue(Long::class.java)
                    ?: System.currentTimeMillis()

                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val dateStr = dateFormat.format(Date(timestamp))
                val issueStr = String.format("%04d", userCount)
                val firstLine = "$dateStr-$issueStr"

                val isMultiplay = latest.child("isMultiplay")
                    .getValue(Boolean::class.java) ?: false

                // 최종적으로 한 번에 화면/QR 세팅하는 함수
                fun applyCertificate(username: String) {
                    dateView.text = firstLine

                    val hashCode = generateCertificateHash(username, timestamp)
                    hashView.text = hashCode

                    usernameView.text = "${username} 님"

                    // 텍스트가 모두 세팅된 후 인증서 뷰를 캡처하고 업로드 + QR 생성
                    dateView.post {
                        val certView = findViewById<View>(R.id.certification)
                        if (certView.width > 0 && certView.height > 0) {
                            val certBitmap = captureViewToBitmap(certView)
                            uploadCertificateImage(certBitmap, qrView)
                        }
                    }
                }

                // 2) 싱글플레이면: latest 의 nickname 그대로 사용
                if (!isMultiplay) {
                    val username = latest.child("nickname")
                        .getValue(String::class.java) ?: "GUEST"
                    applyCertificate(username)
                    return
                }

                // 3) 멀티플레이면: 같은 matchId 중 isPlayer1 == true 인 닉네임을 사용
                val matchId = latest.child("matchId").getValue(String::class.java) ?: ""
                if (matchId.isBlank()) {
                    // 안전장치: matchId 없으면 그냥 latest 닉네임 사용
                    val username = latest.child("nickname")
                        .getValue(String::class.java) ?: "GUEST"
                    applyCertificate(username)
                    return
                }

                // 같은 matchId로 다시 조회해서 P1 닉네임 찾기
                ref.orderByChild("matchId").equalTo(matchId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(matchSnapshot: DataSnapshot) {
                            var p1Nick: String? = null

                            for (child in matchSnapshot.children) {
                                val isPlayer1 = child.child("isPlayer1")
                                    .getValue(Boolean::class.java) ?: false
                                if (isPlayer1) {
                                    p1Nick = child.child("nickname")
                                        .getValue(String::class.java)
                                    break
                                }
                            }

                            val finalUsername = p1Nick ?: latest.child("nickname")
                                .getValue(String::class.java).orEmpty().ifBlank { "GUEST" }

                            applyCertificate(finalUsername)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // 에러 시에도 latest 닉네임으로 fallback
                            val username = latest.child("nickname")
                                .getValue(String::class.java) ?: "GUEST"
                            applyCertificate(username)
                        }
                    })
            }

            override fun onCancelled(error: DatabaseError) {
                dateView.text = "--"
                hashView.text = "--"
                usernameView.text = "GUEST"
            }
        })
    }

    private fun generateCertificateHash(username: String, timestamp: Long): String {
        val raw = (username + timestamp.toString()).hashCode().toString(16).uppercase(Locale.getDefault())
        // 음수 방지를 위해 '-' 제거
        val cleaned = raw.replace("-", "")
        // 최소 12자리를 보장
        val padded = cleaned.padStart(12, '0')
        val part1 = padded.substring(0, 4)
        val part2 = padded.substring(4, 8)
        val part3 = padded.substring(8, 12)
        return "$part1-$part2-$part3"
    }

    private fun captureViewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun uploadCertificateImage(bitmap: Bitmap, qrView: ImageView) {
        // 인증서 비트맵을 JPEG 형식의 바이트 배열로 변환
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()

        // Firebase Storage 참조 생성 (certificates/타임스탬프.jpg 경로)
        val storage = FirebaseStorage.getInstance("gs://exam-afefa.firebasestorage.app")
        val filename = "certificates/${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(filename)

        // 업로드 시작
        ref.putBytes(data)
            .addOnSuccessListener {
                // 업로드 성공 시 다운로드 URL을 가져와서 QR 코드 생성
                ref.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        val qrBitmap = generateQrBitmap(downloadUrl)
                        qrView.setImageBitmap(qrBitmap)
                        Toast.makeText(this, "인증서 업로드 및 QR 생성 완료", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(this, "URL 가져오기 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
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