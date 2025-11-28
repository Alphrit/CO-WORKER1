package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase

class Select1p2pActivity : BaseActivity() {
    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            setOnClickListener {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select1p2p)  // select1p2p.xml과 연결

        //  singleplaybutton 클릭 시 singleplay_nickname로 이동
        val singleplaybutton = findViewById<MaterialCardView>(R.id.p1_button)
        singleplaybutton.setOnClickListener {
            val intent = Intent(this, SinglePlayNicknameActivity::class.java)
            startActivity(intent)
        }

        val multiplaybutton = findViewById<MaterialCardView>(R.id.p2_button)
        multiplaybutton.setOnClickListener {
            // Firebase에 멀티플레이 세션 정보 설정
            val db = com.google.firebase.database.FirebaseDatabase.getInstance()
            val sessionRef = db.getReference("session")

            // 멀티플레이 한 판을 구분하는 matchId 생성 (여기서 한 번만 생성)
            val matchId = db.reference.push().key ?: System.currentTimeMillis().toString()

            // 세션 초기 상태 기록
            val sessionData = mapOf(
                "mode" to "ACTIVATE_MULTIPLAY",
                "matchId" to matchId,
                "p1Ready" to false,
                "p2Ready" to false,
                "cancel" to false
            )
            sessionRef.setValue(sessionData)

            // 멀티플레이 1P 닉네임 화면으로 이동
            val intent = Intent(this, Multiplay1NicknameActivity::class.java)
            startActivity(intent)
        }
        setupTopBar()
    }
}