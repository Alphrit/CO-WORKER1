package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*

class MultiPlayResultActivity : BaseActivity() {

    private lateinit var player1NameView: TextView
    private lateinit var player1ScoreView: TextView
    private lateinit var player2NameView: TextView
    private lateinit var player2ScoreView: TextView
    private lateinit var winnerTextView: TextView

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
        setContentView(R.layout.multiplay_result)



        player1NameView = findViewById(R.id.player1)
        player1ScoreView = findViewById(R.id.player1score)
        player2NameView = findViewById(R.id.player2)
        player2ScoreView = findViewById(R.id.player2score)
        winnerTextView = findViewById(R.id.winner)

        // BaseQuizActivity에서 넘겨준 방 번호
        val matchId = intent.getStringExtra("matchId") ?: ""

        loadMultiPlayResultFromFirebase(matchId)

        // 인증서 화면으로 이동
        val goCert = findViewById<com.google.android.material.card.MaterialCardView>(R.id.gotocertification)
        goCert.setOnClickListener {
            val intent = Intent(this, CertificationActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 메인화면(앱 첫 화면)으로 이동
        val retry = findViewById<com.google.android.material.card.MaterialCardView>(R.id.retry)
        retry.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        setupTopBar()
    }


    private fun loadMultiPlayResultFromFirebase(matchId: String) {
        val db = FirebaseDatabase.getInstance(
            "https://exam-afefa-default-rtdb.firebaseio.com"
        )
        val scoresRef = db.getReference("scores")

        // 이 matchId에 해당하는 점수만 실시간으로 지켜보기
        val query = scoresRef.orderByChild("matchId").equalTo(matchId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var p1Name = "Player1"
                var p1Score = 0
                var p2Name = "Player2"
                var p2Score = 0
                var hasP1 = false
                var hasP2 = false

                for (child in snapshot.children) {
                    val isMultiplay = child.child("isMultiplay")
                        .getValue(Boolean::class.java) ?: false
                    if (!isMultiplay) continue

                    val isPlayer1 = child.child("isPlayer1")
                        .getValue(Boolean::class.java) ?: false

                    val name = child.child("nickname").getValue(String::class.java) ?: ""
                    val score = (child.child("correctCount").getValue(Long::class.java) ?: 0L).toInt()

                    if (isPlayer1) {
                        hasP1 = true
                        p1Name = if (name.isNotBlank()) name else "Player1"
                        p1Score = score
                    } else {
                        hasP2 = true
                        p2Name = if (name.isNotBlank()) name else "Player2"
                        p2Score = score
                    }
                }

                // 일단 현재까지 들어온 정보 화면에 표시
                player1NameView.text = p1Name
                player1ScoreView.text = "${p1Score}점"
                player2NameView.text = p2Name
                player2ScoreView.text = "${p2Score}점"

                // 둘 중 한 명이라도 아직 점수가 없으면 승자 표시하지 않음
                if (!hasP1 || !hasP2) {
                    winnerTextView.text = "상대가 아직 문제를 풀고 있어요"
                    return
                }

                // 여기까지 왔으면 두 사람 점수가 다 들어온 상태 → 승자 계산
                val winnerMessage = when {
                    p1Score > p2Score -> "${p1Name}님이 승리하셨습니다"
                    p2Score > p1Score -> "${p2Name}님이 승리하셨습니다"
                    else -> "무승부입니다"
                }
                winnerTextView.text = winnerMessage
            }

            override fun onCancelled(error: DatabaseError) {
                winnerTextView.text = "결과를 불러오는 중 오류가 발생했습니다"
            }
        })
    }


}