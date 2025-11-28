package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.card.MaterialCardView
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*
import com.example.exam2.R

class Multiplay1NicknameActivity : BaseActivity() {
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
        setContentView(com.example.exam2.R.layout.multiplay1_nickname) // multiplay1.xml 연결

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setOnClickListener {
                // ★ 멀티플레이 취소 플래그 설정
                val db = FirebaseDatabase.getInstance()
                db.getReference("session").child("cancel").setValue(true)

                // 현재 화면만 닫고 이전 화면으로
                onBackPressedDispatcher.onBackPressed()
            }
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnHome?.setOnClickListener {
            // ★ 멀티플레이 취소 플래그 설정
            val db = FirebaseDatabase.getInstance()
            db.getReference("session").child("cancel").setValue(true)

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finishAffinity()
        }

        val playerEditText = findViewById<EditText>(R.id.player)
        val startbutton = findViewById<MaterialCardView>(R.id.startbutton)
        startbutton.isEnabled = false

        val startText = findViewById<TextView>(R.id.starttext)

        playerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                startbutton.isEnabled = !s.isNullOrBlank()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        startbutton.setOnClickListener {
            val nickname = playerEditText.text.toString().trim()

            // 1) matchId 생성 (이 게임의 공통 키)
            val db = FirebaseDatabase.getInstance()
            val matchesRef = db.getReference("matches")
            val newMatchId = matchesRef.push().key ?: System.currentTimeMillis().toString()

            // 2) 다른 기기(플레이어2)도 이 matchId를 쓸 수 있도록 session에 저장
            val ref = db.getReference("session").child("matchId")
            ref.setValue(newMatchId)

            // ★ Temi 플레이어1이 준비 완료 표시
            db.getReference("session").child("p1Ready").setValue(true)
            startText?.text = "준비 완료"
        }

        // ★ p1Ready, p2Ready 모두 true가 되었을 때 자동으로 넘어가는 감시 리스너
        val db = FirebaseDatabase.getInstance()
        val sessionRef = db.getReference("session")

        sessionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val p1 = snapshot.child("p1Ready").getValue(Boolean::class.java) ?: false
                val p2 = snapshot.child("p2Ready").getValue(Boolean::class.java) ?: false

                if (p1 && p2) {
                    val nickname = playerEditText.text.toString().trim()
                    val matchId = snapshot.child("matchId").getValue(String::class.java) ?: return

                    val intent = Intent(this@Multiplay1NicknameActivity, BaseQuizActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("isMultiplay", true)
                    intent.putExtra("isPlayer1", true)
                    intent.putExtra("matchId", matchId)
                    startActivity(intent)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        setupTopBar()
    }
}
