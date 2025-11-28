package com.example.exam2

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.exam2.data.CoshowtiResults

class CoshowtiActivity : BaseActivity() {
    // View references for intro screen
    private lateinit var btnStart: Button

    // View references for Stage 1 question screen (5-point Likert)
    private lateinit var tvProgress: TextView
    private lateinit var tvQuestion: TextView

    private lateinit var tvQuestion2: TextView
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton

    // Stage 1 answer button containers
    private lateinit var btnAnswer1: FrameLayout
    private lateinit var btnAnswer2: FrameLayout
    private lateinit var btnAnswer3: FrameLayout
    private lateinit var btnAnswer4: FrameLayout
    private lateinit var btnAnswer5: FrameLayout

    // Stage 1 answer button circles and checks
    private lateinit var btnAnswer1Circle: View
    private lateinit var btnAnswer2Circle: View
    private lateinit var btnAnswer3Circle: View
    private lateinit var btnAnswer4Circle: View
    private lateinit var btnAnswer5Circle: View
    private lateinit var btnAnswer1Check: ImageView
    private lateinit var btnAnswer2Check: ImageView
    private lateinit var btnAnswer3Check: ImageView
    private lateinit var btnAnswer4Check: ImageView
    private lateinit var btnAnswer5Check: ImageView

    // Stage 2 view references

    private lateinit var btnOption1: TextView
    private lateinit var btnOption2: TextView
    private lateinit var btnResult: ImageButton

    // State management
    private var currentStage = 1 // 1 or 2
    private var currentQuestion = 1 // 1-4 within each stage
    private val stage1Answers = IntArray(4) { 0 } // 0 = not answered, 1-5 = answer choice
    private val stage2Answers = IntArray(4) { 0 } // 0 = not answered, 1-2 = option choice

    // Scoring (M/D, N/B, A/L, E/S)
    private var scoreM = 0
    private var scoreD = 0
    private var scoreN = 0
    private var scoreB = 0
    private var scoreA = 0
    private var scoreL = 0
    private var scoreE = 0
    private var scoreS = 0

    // Stage 1 Questions (4 questions, 5-point Likert scale)
    private val stage1Questions = arrayOf(
        R.string.coshoti_stage1_q1, // M ↔ D
        R.string.coshoti_stage1_q2, // N ↔ B
        R.string.coshoti_stage1_q3, // A ↔ L
        R.string.coshoti_stage1_q4  // E ↔ S
    )

    private val stage2Questions = arrayOf(
        R.string.coshoti_stage2_q1, // M ↔ D
        R.string.coshoti_stage2_q2, // N ↔ B
        R.string.coshoti_stage2_q3, // A ↔ L
        R.string.coshoti_stage2_q4  // E ↔ S
    )


    // Stage 2 Questions (4 questions, 2 options each)
    private val stage2Options = arrayOf(
        // Q1: M ↔ D
        Pair(R.string.coshoti_stage2_q1_option1, R.string.coshoti_stage2_q1_option2),
        // Q2: N ↔ B
        Pair(R.string.coshoti_stage2_q2_option1, R.string.coshoti_stage2_q2_option2),
        // Q3: A ↔ L
        Pair(R.string.coshoti_stage2_q3_option1, R.string.coshoti_stage2_q3_option2),
        // Q4: E ↔ S
        Pair(R.string.coshoti_stage2_q4_option1, R.string.coshoti_stage2_q4_option2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set fullscreen mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        // Show intro screen first
        showIntroScreen()
    }

    private fun showIntroScreen() {
        setContentView(R.layout.activity_coshowti_intro)

        val tvSubtitle = findViewById<TextView>(R.id.tvSubtitle)
        btnStart = findViewById(R.id.btnStart)

        // Setup common top bar for intro screen
        // Back button: Go to MainActivity
        // Home button: Go to MainActivity
        val topBar = findViewById<View>(R.id.topBar)
        setupCommonTopBar(
            topBar,
            backAction = { navigateToHome() },
            homeAction = { navigateToHome() }
        )

        // Parse HTML in subtitle to apply color formatting (API 23 compatible)
        tvSubtitle.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(getString(R.string.coshoti_subtitle), Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(getString(R.string.coshoti_subtitle))
        }

        btnStart.setOnClickListener {
            currentStage = 1
            currentQuestion = 1
            showStage1QuestionScreen()
        }
    }

    private fun showStage1QuestionScreen() {
        setContentView(R.layout.activity_coshowti_question)

        // Find all views
        tvProgress = findViewById(R.id.tvProgress)
        tvQuestion = findViewById(R.id.tvQuestion)
        tvQuestion2 = findViewById(R.id.tvQuestion2)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        // Setup common top bar for question screen
        // Back button: Show stop popup, then go to intro
        // Home button: Show stop popup, then go to MainActivity
        val topBar = findViewById<View>(R.id.topBar)
        setupCommonTopBar(
            topBar,
            backAction = {
                showStopPopup { showIntroScreen() }
            },
            homeAction = {
                showStopPopup { navigateToHome() }
            }
        )

        // Answer buttons
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        btnAnswer5 = findViewById(R.id.btnAnswer5)

        // Circles
        btnAnswer1Circle = findViewById(R.id.btnAnswer1Circle)
        btnAnswer2Circle = findViewById(R.id.btnAnswer2Circle)
        btnAnswer3Circle = findViewById(R.id.btnAnswer3Circle)
        btnAnswer4Circle = findViewById(R.id.btnAnswer4Circle)
        btnAnswer5Circle = findViewById(R.id.btnAnswer5Circle)

        // Check marks
        btnAnswer1Check = findViewById(R.id.btnAnswer1Check)
        btnAnswer2Check = findViewById(R.id.btnAnswer2Check)
        btnAnswer3Check = findViewById(R.id.btnAnswer3Check)
        btnAnswer4Check = findViewById(R.id.btnAnswer4Check)
        btnAnswer5Check = findViewById(R.id.btnAnswer5Check)

        btnPrevious.setOnClickListener {
            navigatePrevious()
        }

        btnNext.setOnClickListener {
            navigateNext()
        }

        // Answer button click listeners
        btnAnswer1.setOnClickListener { selectStage1Answer(1) }
        btnAnswer2.setOnClickListener { selectStage1Answer(2) }
        btnAnswer3.setOnClickListener { selectStage1Answer(3) }
        btnAnswer4.setOnClickListener { selectStage1Answer(4) }
        btnAnswer5.setOnClickListener { selectStage1Answer(5) }

        // Initial UI update
        updateStage1QuestionUI()
    }

    private fun showStage2QuestionScreen() {
        setContentView(R.layout.activity_coshowti_question_stage2)

        // Find all views
        tvProgress = findViewById(R.id.tvProgress)
        tvQuestion2 = findViewById(R.id.tvQuestion2)
        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnResult = findViewById(R.id.btnResult)

        // Setup common top bar for question screen
        // Back button: Show stop popup, then go to intro
        // Home button: Show stop popup, then go to MainActivity
        val topBar = findViewById<View>(R.id.topBar)
        setupCommonTopBar(
            topBar,
            backAction = {
                showStopPopup { showIntroScreen() }
            },
            homeAction = {
                showStopPopup { navigateToHome() }
            }
        )

        btnPrevious.setOnClickListener {
            navigatePrevious()
        }

        btnNext.setOnClickListener {
            navigateNext()
        }

        btnResult.setOnClickListener {
            navigateNext()
        }

        btnOption1.setOnClickListener {
            selectStage2Option(1)
        }

        btnOption2.setOnClickListener {
            selectStage2Option(2)
        }

        // Initial UI update
        updateStage2QuestionUI()
    }

    private fun selectStage1Answer(answer: Int) {
        val currentAnswer = stage1Answers[currentQuestion - 1]

        // Toggle selection
        if (currentAnswer == answer) {
            stage1Answers[currentQuestion - 1] = 0
        } else {
            stage1Answers[currentQuestion - 1] = answer
        }

        updateStage1AnswerButtonsUI()
        updateNextButtonState()
    }

    private fun selectStage2Option(option: Int) {
        val currentAnswer = stage2Answers[currentQuestion - 1]

        // Toggle selection
        if (currentAnswer == option) {
            stage2Answers[currentQuestion - 1] = 0
        } else {
            stage2Answers[currentQuestion - 1] = option
        }

        updateStage2OptionsUI()
        updateNextButtonState()
    }

    private fun navigatePrevious() {
        if (currentStage == 1 && currentQuestion > 1) {
            currentQuestion--
            updateStage1QuestionUI()
        } else if (currentStage == 2 && currentQuestion > 1) {
            currentQuestion--
            updateStage2QuestionUI()
        } else if (currentStage == 2 && currentQuestion == 1) {
            // Go back to Stage 1 last question
            currentStage = 1
            currentQuestion = 4
            showStage1QuestionScreen()
        }
    }

    private fun navigateNext() {
        val isAnswered = if (currentStage == 1) {
            stage1Answers[currentQuestion - 1] != 0
        } else {
            stage2Answers[currentQuestion - 1] != 0
        }

        if (!isAnswered) return

        if (currentStage == 1) {
            if (currentQuestion < 4) {
                currentQuestion++
                updateStage1QuestionUI()
            } else {
                // Move to Stage 2
                currentStage = 2
                currentQuestion = 1
                showStage2QuestionScreen()
            }
        } else if (currentStage == 2) {
            if (currentQuestion < 4) {
                currentQuestion++
                updateStage2QuestionUI()
            } else {
                // Calculate result and navigate to result screen
                calculateScoresAndShowResult()
            }
        }
    }

    private fun updateStage1QuestionUI() {
        val totalQuestionsDisplay = 8
        val currentQuestionDisplay = currentQuestion

        tvProgress.text = getString(R.string.coshoti_progress, currentQuestionDisplay, totalQuestionsDisplay)
        tvQuestion.text = getString(stage1Questions[currentQuestion - 1])

        btnPrevious.visibility = if (currentQuestion > 1) View.VISIBLE else View.GONE

        updateStage1AnswerButtonsUI()
        updateNextButtonState()
    }

    private fun updateStage2QuestionUI() {
        val totalQuestionsDisplay = 8
        val currentQuestionDisplay = 4 + currentQuestion

        tvProgress.text = getString(R.string.coshoti_progress, currentQuestionDisplay, totalQuestionsDisplay)
        tvQuestion2.text = getString(stage2Questions[currentQuestion - 1])

        val options = stage2Options[currentQuestion - 1]
        btnOption1.text = getString(options.first)
        btnOption2.text = getString(options.second)

        btnPrevious.visibility = View.VISIBLE

        updateStage2OptionsUI()
        updateNextButtonState()
    }

    private fun updateStage1AnswerButtonsUI() {
        val selectedAnswer = stage1Answers[currentQuestion - 1]

        // Reset all buttons to default state
        resetAnswerButton(1, btnAnswer1Circle, btnAnswer1Check, R.drawable.btn_circle_yellow_border)
        resetAnswerButton(2, btnAnswer2Circle, btnAnswer2Check, R.drawable.btn_circle_yellow_border)
        resetAnswerButton(3, btnAnswer3Circle, btnAnswer3Check, R.drawable.btn_circle_gray_border)
        resetAnswerButton(4, btnAnswer4Circle, btnAnswer4Check, R.drawable.btn_circle_blue_border)
        resetAnswerButton(5, btnAnswer5Circle, btnAnswer5Check, R.drawable.btn_circle_blue_border)

        // Highlight selected button
        when (selectedAnswer) {
            1 -> selectAnswerButton(btnAnswer1Circle, btnAnswer1Check, R.drawable.btn_circle_yellow_filled)
            2 -> selectAnswerButton(btnAnswer2Circle, btnAnswer2Check, R.drawable.btn_circle_yellow_filled)
            3 -> selectAnswerButton(btnAnswer3Circle, btnAnswer3Check, R.drawable.btn_circle_gray_filled)
            4 -> selectAnswerButton(btnAnswer4Circle, btnAnswer4Check, R.drawable.btn_circle_blue_filled)
            5 -> selectAnswerButton(btnAnswer5Circle, btnAnswer5Check, R.drawable.btn_circle_blue_filled)
        }
    }

    private fun updateStage2OptionsUI() {
        val selectedOption = stage2Answers[currentQuestion - 1]

        btnOption1.isSelected = (selectedOption == 1)
        btnOption2.isSelected = (selectedOption == 2)
    }

    private fun resetAnswerButton(answer: Int, circle: View, check: ImageView, borderDrawable: Int) {
        circle.setBackgroundResource(borderDrawable)
        check.visibility = View.GONE
    }

    private fun selectAnswerButton(circle: View, check: ImageView, filledDrawable: Int) {
        circle.setBackgroundResource(filledDrawable)
        check.visibility = View.VISIBLE
    }

    private fun updateNextButtonState() {
        val isAnswered = if (currentStage == 1) {
            stage1Answers[currentQuestion - 1] != 0
        } else {
            stage2Answers[currentQuestion - 1] != 0
        }

        val isLastQuestion = (currentStage == 2 && currentQuestion == 4)

        if (isLastQuestion) {
            // Show result button, hide next button
            btnNext.visibility = View.GONE
            btnResult.visibility = View.VISIBLE
            btnResult.isEnabled = isAnswered

            if (isAnswered) {
                btnResult.setImageResource(R.drawable.result_colored)
            } else {
                btnResult.setImageResource(R.drawable.result_monochrome)
            }
        } else {
            // Show next button, hide result button (for Stage 2 only)
            btnNext.visibility = View.VISIBLE
            if (currentStage == 2) {
                btnResult.visibility = View.GONE
            }
            btnNext.isEnabled = isAnswered

            if (isAnswered) {
                btnNext.setImageResource(R.drawable.nextbutton_coloered)
            } else {
                btnNext.setImageResource(R.drawable.nextbutton_monochrome)
            }
        }
    }

    private fun calculateScoresAndShowResult() {
        // Calculate Stage 1 scores (5-point Likert)
        // Button mapping: 1=매우 동의하지 않음(leftmost yellow), 2=동의하지 않음, 3=잘모르겠음, 4=동의함, 5=매우 동의함(rightmost blue)

        // Q1: M ↔ D (agree = D, disagree = M)
        when (stage1Answers[0]) {
            1 -> scoreM += 4  // 매우 동의하지 않음 (Button1 - leftmost yellow)
            2 -> scoreM += 2  // 동의하지 않음
            3 -> { }          // 잘 모르겠음 (no points)
            4 -> scoreD += 2  // 동의함
            5 -> scoreD += 4  // 매우 동의함 (Button5 - rightmost blue)
        }

        // Q2: N ↔ B (agree = B, disagree = N)
        when (stage1Answers[1]) {
            1 -> scoreN += 4
            2 -> scoreN += 2
            3 -> { }
            4 -> scoreB += 2
            5 -> scoreB += 4
        }

        // Q3: A ↔ L (agree = A, disagree = L)
        when (stage1Answers[2]) {
            1 -> scoreL += 4
            2 -> scoreL += 2
            3 -> { }
            4 -> scoreA += 2
            5 -> scoreA += 4
        }

        // Q4: E ↔ S (agree = E, disagree = S)
        when (stage1Answers[3]) {
            1 -> scoreS += 4
            2 -> scoreS += 2
            3 -> { }
            4 -> scoreE += 2
            5 -> scoreE += 4
        }

        // Calculate Stage 2 scores (2 options, +3 points each)
        // Q1: M ↔ D
        when (stage2Answers[0]) {
            1 -> scoreM += 3  // Option 1: Mechanical
            2 -> scoreD += 3  // Option 2: Digital
        }

        // Q2: N ↔ B
        when (stage2Answers[1]) {
            1 -> scoreN += 3  // Option 1: Nature
            2 -> scoreB += 3  // Option 2: Build
        }

        // Q3: A ↔ L
        when (stage2Answers[2]) {
            1 -> scoreA += 3  // Option 1: Analytic
            2 -> scoreL += 3  // Option 2: Living
        }

        // Q4: E ↔ S
        when (stage2Answers[3]) {
            1 -> scoreS += 3  // Option 1: Systematic
            2 -> scoreE += 3  // Option 2: Experimental
        }

        // Calculate final type code
        val resultCode = CoshowtiResults.calculateResultType(
            scoreM, scoreD,
            scoreN, scoreB,
            scoreA, scoreL,
            scoreE, scoreS
        )

        // Navigate to result screen
        val intent = Intent(this, CoshowtiResultActivity::class.java)
        intent.putExtra("RESULT_CODE", resultCode)
        startActivity(intent)
        finish()
    }
}
