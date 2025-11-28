package com.example.exam2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.ImageButton

class SinglePlayResultActivity : BaseActivity() {

    private lateinit var name1: TextView
    private lateinit var name2: TextView
    private lateinit var name3: TextView
    private lateinit var score1: TextView
    private lateinit var score2: TextView
    private lateinit var score3: TextView
    private lateinit var myplace: TextView
    private lateinit var myscore: TextView
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
        setContentView(R.layout.singleplay_result) // singleplayresult.xml 연결

        // surveybutton 클릭 시 usability 화면으로 이동
        val surveyButton = findViewById<ImageView>(R.id.surveybutton)
        surveyButton?.setOnClickListener {
            val intent = Intent(this, UsabilityActivity::class.java)
            // 설문 유형: singlequiz (퀴즈 결과 화면에서 진입)
            intent.putExtra("surveyType", "코쇼퀴즈")
            startActivity(intent)
        }

        // 상위 1,2,3등 이름/점수 TextView 연결
        name1 = findViewById(R.id.name1)
        name2 = findViewById(R.id.name2)
        name3 = findViewById(R.id.name3)
        score1 = findViewById(R.id.score1)
        score2 = findViewById(R.id.score2)
        score3 = findViewById(R.id.score3)
        myplace = findViewById(R.id.myplace)
        myscore = findViewById(R.id.myscore)

        // Firebase에서 점수 불러와서 1,2,3등 표시
        loadTop3FromFirebase()

        // 시간초과 여부를 받아서 resultlog 텍스트 변경
        val isTimeout = intent.getBooleanExtra("finishedByTimeout", false)
        val resultLogView = findViewById<TextView>(R.id.resultlog)
        if (!isTimeout) {
            // 시간이 아니라 문제를 모두 풀어서 들어온 경우
            resultLogView?.text = "모든 문제 완료"
        }
        // 시간초과인 경우(resultLogView 기존 텍스트 유지) → 아무것도 하지 않음


        val goCert = findViewById<MaterialCardView>(R.id.gotocertification)
        goCert.setOnClickListener {
            val intent = Intent(this, CertificationActivity::class.java)
            startActivity(intent)
        }
        val retryBtn = findViewById<MaterialCardView>(R.id.retry)
        retryBtn.setOnClickListener {
            val intent = Intent(this, SinglePlayNicknameActivity::class.java)
            startActivity(intent)
        }
        setupTopBar()
    }

    private fun loadTop3FromFirebase() {
        val db = FirebaseDatabase.getInstance(
            "https://exam-afefa-default-rtdb.firebaseio.com"
        )
        val dbRef = db.getReference("scores")

        dbRef.orderByChild("correctCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChildren()) {
                        // 데이터가 하나도 없으면 전부 디폴트
                        setRankView(name1, score1, null)
                        setRankView(name2, score2, null)
                        setRankView(name3, score3, null)
                        myplace.text = "내 순위 -"
                        myscore.text = "(기록 없음)"
                        return
                    }

                    var latestEntry: ScoreEntry? = null
                    val list = mutableListOf<ScoreEntry>()

                    for (child in snapshot.children) {
                        val nickname = child.child("nickname").getValue(String::class.java) ?: ""
                        val correctCount = child.child("correctCount").getValue(Long::class.java) ?: 0L
                        val totalQuestions = child.child("totalQuestions").getValue(Long::class.java) ?: 0L
                        val timestamp = child.child("timestamp").getValue(Long::class.java) ?: 0L

                        val entry = ScoreEntry(
                            nickname = nickname,
                            correctCount = correctCount,
                            totalQuestions = totalQuestions,
                            timestamp = timestamp
                        )
                        list.add(entry)

                        if (latestEntry == null || entry.timestamp > latestEntry!!.timestamp) {
                            latestEntry = entry
                        }
                    }

                    // 점수 내림차순 정렬 (correctCount 기준, 같으면 timestamp 기준)
                    val sorted = list.sortedWith(
                        compareByDescending<ScoreEntry> { it.correctCount }
                            .thenBy { it.timestamp }
                    )

                    // 상위 1,2,3등 적용 (없으면 디폴트)
                    setRankView(name1, score1, sorted.getOrNull(0))
                    setRankView(name2, score2, sorted.getOrNull(1))
                    setRankView(name3, score3, sorted.getOrNull(2))

                    // 가장 최근 기록을 "내 기록"으로 간주하여 순위와 점수 표시
                    if (latestEntry == null) {
                        myplace.text = "내 순위 -"
                        myscore.text = "(기록 없음)"
                    } else {
                        val me = latestEntry!!
                        val myIndex = sorted.indexOfFirst {
                            it.nickname == me.nickname &&
                                    it.correctCount == me.correctCount &&
                                    it.timestamp == me.timestamp
                        }
                        if (myIndex >= 0) {
                            val myRank = myIndex + 1
                            val rankText = "내 순위 ${myRank}위"
                            val spannable = android.text.SpannableString(rankText)
                            val start = rankText.indexOf(myRank.toString())
                            val end = start + myRank.toString().length
                            spannable.setSpan(
                                android.text.style.ForegroundColorSpan(android.graphics.Color.parseColor("#007AFF")),
                                start,
                                end,
                                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            myplace.text = spannable
                            myscore.text = "(${me.correctCount}점)"
                        } else {
                            myplace.text = "내 순위 -"
                            myscore.text = "(기록 없음)"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 에러 시에도 디폴트 값으로 표시
                    setRankView(name1, score1, null)
                    setRankView(name2, score2, null)
                    setRankView(name3, score3, null)
                    myplace.text = "내 순위 -"
                    myscore.text = "(기록 없음)"
                }
            })
    }

    private fun setRankView(nameView: TextView, scoreView: TextView, entry: ScoreEntry?) {
        if (entry == null) {
            nameView.text = "---"
            scoreView.text = "--점"
        } else {
            nameView.text = entry.nickname
            scoreView.text = "${entry.correctCount}점"
        }
    }


}

// 파일 하단으로 ScoreEntry 이동

data class ScoreEntry(
    val nickname: String = "",
    val correctCount: Long = 0,
    val totalQuestions: Long = 0,
    val timestamp: Long = 0L
)
