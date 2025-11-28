package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase
import android.os.CountDownTimer
import android.widget.ImageButton

class BaseQuizActivity : BaseActivity() {

    private lateinit var quiz4cLayout: View
    private lateinit var quizoxLayout: View

    private lateinit var questionText4c: TextView
    private lateinit var questionTextOx: TextView

    private lateinit var answer1_4c: TextView
    private lateinit var answer2_4c: TextView
    private lateinit var answer3_4c: TextView
    private lateinit var answer4_4c: TextView

    private lateinit var answerO: TextView
    private lateinit var answerX: TextView
    private lateinit var scoreboardText: TextView

    private var currentIndex = 0
    private var nickname: String? = null
    private var correctCount: Int = 0

    private var quizTimer: CountDownTimer? = null
    private var isQuizActive: Boolean = false
    private var finishedByTimeout: Boolean = false

    // 멀티플레이 여부 플래그
    private var isMultiplay: Boolean = false

    // 이 기기가 플레이어1인지 여부
    private var isPlayer1: Boolean = false
    private var matchId: String = ""
    // 문제 유형 정의
    enum class QuestionType {
        FOUR_CHOICE,
        OX
    }

    // 문제 모델
    data class Question(
        val quizNumber: Int,
        val question: String,
        val answers: List<String>,
        val correctIndex: Int,
        val type: QuestionType = QuestionType.FOUR_CHOICE
    )


    private val questions = listOf(
        Question(
            quizNumber = 1,
            question = "Q1. 2025 CO-SHOW는 어디에서 열릴까?",
            answers = listOf("서울", "부산", "대전", "인천"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 2,
            question = "Q2. CO-SHOW는 며칠간 진행될까?",
            answers = listOf("2일", "3일", "4일", "5일"),
            correctIndex = 2
        ),
        Question(
            quizNumber = 3,
            question = "Q3. CO-SHOW 입장료는 유료다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 4,
            question = "Q4. CO-SHOW의 주최는 교육부와 한국연구재단이다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 5,
            question = "Q5. '첨단교육,SHOW로 펼쳐지다!'는 CO-SHOW 슬로건이다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 6,
            question = "Q6. COSS는 무엇의 약자일까?",
            answers = listOf(
                "Creative Open Sharing System",
                "Convergence Open Sharing System",
                "Connected Open Smart System",
                "Core Open Science System"
            ),
            correctIndex = 1
        ),
        Question(
            quizNumber = 7,
            question = "Q7. COSS는 대학 간 벽을 높이는 사업이다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 8,
            question = "Q8. 2024년 COSS 사업비는 얼마일까?",
            answers = listOf("500억", "700억", "816억", "1000억"),
            correctIndex = 2
        ),
        Question(
            quizNumber = 9,
            question = "Q9. COSS 사업기간은 2021년부터 2026년까지이다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 10,
            question = "Q10. COSS 사업의 최종 목표는 100만 명 인재 양성이다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 11,
            question = "Q11. CO-SHOW에 참여하는 첨단 분야 수는?",
            answers = listOf("12개", "15개", "18개", "20개"),
            correctIndex = 2
        ),
        Question(
            quizNumber = 12,
            question = "Q12. 인공지능 경진대회 이름은?",
            answers = listOf("AI Spark Challenge", "AIM Challenge", "COSS AI Contest", "Model Run 2025"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 13,
            question = "Q13. 3D펜 제작 체험은 어떤 분야일까?",
            answers = listOf("차세대디스플레이", "첨단소재나노융합", "실감미디어", "그린바이오"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 14,
            question = "Q14. COSS 사업의 핵심 가치가 아닌 것은?",
            answers = listOf("융합", "개방", "협력", "경쟁"),
            correctIndex = 3
        ),
        Question(
            quizNumber = 15,
            question = "Q15. COSS 사업은 전국 모든 대학이 개별적으로 운영한다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 16,
            question = "Q16. CO-SHOW에서 경진대회와 체험프로그램이 모두 열린다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 17,
            question = "Q17. CO-SHOW는 초·중·고 학생은 참가할 수 없다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 18,
            question = "Q18. 'AutoRace 2025'의 분야는?",
            answers = listOf("로봇", "자동차", "통신", "데이터"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 19,
            question = "Q19. ‘수소연료전지자동차 만들기’는 초등학생도 참여 가능하다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 20,
            question = "Q20. CO-SHOW는 ‘구경’보다 ‘참여’가 중요한 행사다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 21,
            question = "Q21. CO-SHOW는 미래를 '보는 곳'이 아니라 미래를 '___' 곳이다.",
            answers = listOf("만드는", "그리는", "배우는", "찍는"),
            correctIndex = 0
        ),
        Question(
            quizNumber = 22,
            question = "Q22. AI가 만든 가짜를 잡는 체험의 분야는?",
            answers = listOf("인공지능", "데이터보안활용융합", "실감미디어", "로봇"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 23,
            question = "Q23. 'T-OLED 액자 만들기'에서 볼 수 있는 기술은?",
            answers = listOf("투명 디스플레이", "홀로그램", "3D 인쇄", "로봇 비전"),
            correctIndex = 0
        ),
        Question(
            quizNumber = 24,
            question = "Q24. CO-SHOW의 체험존에서는 신발 벗고 들어간다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 25,
            question = "Q25. CO-SHOW에서 유일하게 멈추지 않는 것은?",
            answers = listOf("사람", "데이터", "조명", "카메라"),
            correctIndex = 2
        ),
        Question(
            quizNumber = 26,
            question = "Q26. 'AI 캐리커처'는 음성 인식 기술을 사용한다.",
            answers = listOf("O", "X"),
            correctIndex = 1,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 27,
            question = "Q27. 'ROBO SHOW'를 운영하는 대학은?",
            answers = listOf("상명대", "한양대 ERICA", "국민대", "단국대"),
            correctIndex = 1
        ),
        Question(
            quizNumber = 28,
            question = "Q28. 'SecureX challenge'는 보안 경진 대회이다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 29,
            question = "Q29. '그린바이오' 분야는 식물생명 관련 첨단기술을 다룬다.",
            answers = listOf("O", "X"),
            correctIndex = 0,
            type = QuestionType.OX
        ),
        Question(
            quizNumber = 30,
            question = "Q30. CO-SHOW의 로봇 테미(temi)는 어떤 기능을 수행할까?",
            answers = listOf("자율주행-음성인식", "3D프린팅", "영상편집", "드론비행"),
            correctIndex = 0
        )
    ).sortedBy { it.quizNumber }
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
        setContentView(R.layout.base_quiz)
        scoreboardText = findViewById(R.id.scoreboard)

        nickname = intent.getStringExtra("nickname")
        isMultiplay = intent.getBooleanExtra("isMultiplay", false)
        isPlayer1 = intent.getBooleanExtra("isPlayer1", false)
        matchId = intent.getStringExtra("matchId") ?: ""

        // 풀스크린 유지
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // 이전 화면에서 전달된 닉네임 / 멀티플레이 여부 / 플레이어1 여부 받기
        nickname = intent.getStringExtra("nickname")
        isMultiplay = intent.getBooleanExtra("isMultiplay", false)
        isPlayer1 = intent.getBooleanExtra("isPlayer1", false)

        // include 레이아웃 참조
        quiz4cLayout = findViewById(R.id.quiz4c_layout)
        quizoxLayout = findViewById(R.id.quizox_layout)

        // quiz4c 레이아웃의 뷰들
        questionText4c = quiz4cLayout.findViewById(R.id.question4c)
        answer1_4c = quiz4cLayout.findViewById(R.id.answer1)
        answer2_4c = quiz4cLayout.findViewById(R.id.answer2)
        answer3_4c = quiz4cLayout.findViewById(R.id.answer3)
        answer4_4c = quiz4cLayout.findViewById(R.id.answer4)

        // quizox 레이아웃의 뷰들 (answero / answerx)
        questionTextOx = quizoxLayout.findViewById(R.id.question4c)
        answerO = quizoxLayout.findViewById(R.id.answero)
        answerX = quizoxLayout.findViewById(R.id.answerx)
        scoreboardText.text = "0"

        // 첫 문제 표시
        showQuestion(currentIndex)

        // 4지선다 보기 클릭 시
        answer1_4c.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(0) }
        }
        answer2_4c.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(1) }
        }
        answer3_4c.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(2) }
        }
        answer4_4c.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(3) }
        }

        // OX 보기 클릭 시
        answerO.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(0) }
        }
        answerX.setOnClickListener {
            animateAnswerClick(it) { onAnswerSelected(1) }
        }

        // 전체 퀴즈 제한 시간(99초) 타이머 시작
        startQuizTimer()
        setupTopBar()
    }

    private fun animateAnswerClick(view: View, onEnd: () -> Unit = {}) {
        val targetCard = (view as? MaterialCardView)
            ?: (view.parent as? MaterialCardView)
            ?: view

        targetCard.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .alpha(0.7f)
            .setDuration(80)
            .withEndAction {
                targetCard.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(80)
                    .withEndAction { onEnd() }
                    .start()
            }
            .start()
    }

    private fun onAnswerSelected(selectedIndex: Int) {
        if (!isQuizActive) return

        val q = questions[currentIndex]
        val isCorrect = selectedIndex == q.correctIndex

        if (isCorrect) {
            correctCount++
            val currentScore = scoreboardText.text.toString().toIntOrNull() ?: 0
            scoreboardText.text = (currentScore + 1).toString()
        }

        goToNextQuestion()
    }

    private fun goToNextQuestion() {
        if (currentIndex == questions.lastIndex) {
            finishedByTimeout = false
            isQuizActive = false
            quizTimer?.cancel()

            sendResultToFirebase()
            goToResultScreen()
            return
        }
        currentIndex += 1
        showQuestion(currentIndex)
    }

    private fun showQuestion(index: Int) {
        val q = questions[index]

        if (q.type == QuestionType.OX) {
            quiz4cLayout.visibility = View.GONE
            quizoxLayout.visibility = View.VISIBLE

            questionTextOx.text = q.question
            answerO.text = q.answers.getOrNull(0) ?: "O"
            answerX.text = q.answers.getOrNull(1) ?: "X"
        } else {
            quiz4cLayout.visibility = View.VISIBLE
            quizoxLayout.visibility = View.GONE

            questionText4c.text = q.question
            answer1_4c.text = q.answers.getOrNull(0) ?: ""
            answer2_4c.text = q.answers.getOrNull(1) ?: ""
            answer3_4c.text = q.answers.getOrNull(2) ?: ""
            answer4_4c.text = q.answers.getOrNull(3) ?: ""
        }
    }

    private fun sendResultToFirebase() {
        val name = nickname ?: "unknown"
        val db = FirebaseDatabase.getInstance(
            "https://exam-afefa-default-rtdb.firebaseio.com"
        )
        val dbRef = db.getReference("scores")

        // 멀티/싱글 공통 + 멀티 플래그들 포함
        val data = mapOf(
            "nickname" to name,
            "correctCount" to correctCount,
            "totalQuestions" to questions.size,
            "isMultiplay" to isMultiplay,
            "isPlayer1" to isPlayer1,
            "matchId" to matchId,                 // ★ 추가
            "timestamp" to System.currentTimeMillis()
        )

        dbRef.push().setValue(data)

        // ★ Temi(플레이어1) 쪽에서 결과 저장 후, 다음 사용자를 위해 세션 초기화
        if (isMultiplay && isPlayer1) {
            val sessionDb = FirebaseDatabase.getInstance()   // session은 Temi 프로젝트(default) 사용
            val sessionRef = sessionDb.getReference("session")

            val resetData: Map<String, Any?> = mapOf(
                "matchId" to null,
                "p1Ready" to false,
                "p2Ready" to false
            )
            sessionRef.updateChildren(resetData)
        }
    }

    private fun startQuizTimer() {
        quizTimer?.cancel()
        isQuizActive = true

        quizTimer = object : CountDownTimer(99_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                val countdownView = findViewById<TextView>(R.id.countdowntext)
                countdownView?.text = secondsLeft.toString()
            }

            override fun onFinish() {
                if (!isQuizActive) return

                finishedByTimeout = true
                isQuizActive = false

                sendResultToFirebase()
                goToResultScreen()
            }
        }.start()
    }

    // 싱글/멀티에 따라 결과 화면 분기
    private fun goToResultScreen() {
        val intent = if (isMultiplay) {
            Intent(this, MultiPlayResultActivity::class.java)
        } else {
            Intent(this, SinglePlayResultActivity::class.java)
        }

        intent.putExtra("nickname", nickname)
        intent.putExtra("correctCount", correctCount)
        intent.putExtra("totalQuestions", questions.size)
        intent.putExtra("finishedByTimeout", finishedByTimeout)

        // 멀티 정보도 같이 넘기기
        intent.putExtra("isMultiplay", isMultiplay)
        intent.putExtra("isPlayer1", isPlayer1)
        intent.putExtra("matchId", matchId)

        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        quizTimer?.cancel()
    }
}
