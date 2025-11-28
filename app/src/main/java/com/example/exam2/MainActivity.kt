package com.example.exam2

import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.database.FirebaseDatabase

class MainActivity : BaseActivity() {

    companion object {
        private const val DB_URL = "https://exam-afefa-default-rtdb.firebaseio.com"
        private const val MOVE_MODE_TIMEOUT = 10_000L // 10초
    }

     private lateinit var generalMoveHelper: GeneralMoveModeHelper

    private val moveModeHandler = Handler(Looper.getMainLooper())
    private val moveModeRunnable = Runnable {
        generalMoveHelper.tryStartMoveMode()
    }

    // ---------- 이미지 슬라이드 관련 ----------
    private val images = arrayOf(
        R.drawable.homeimg1,
        R.drawable.homeimg2,
        R.drawable.homeimg3,
        R.drawable.homeimg4,
        R.drawable.homeimg5
    )

    private var index = 0
    private val imgHandler = Handler(Looper.getMainLooper())
    private lateinit var homeLeftImg: ImageView

    private val imageSwitcher = object : Runnable {
        override fun run() {
            val currentDrawable = homeLeftImg.drawable
            val nextResId = images[index]
            index = (index + 1) % images.size

            if (currentDrawable == null) {
                homeLeftImg.setImageResource(nextResId)
            } else {
                val nextDrawable = ContextCompat.getDrawable(this@MainActivity, nextResId)
                if (nextDrawable != null) {
                    val transition = TransitionDrawable(arrayOf(currentDrawable, nextDrawable))
                    homeLeftImg.setImageDrawable(transition)
                    transition.startTransition(1000)
                } else {
                    homeLeftImg.setImageResource(nextResId)
                }
            }
            imgHandler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 메인 화면은 자동 홈복귀 기능을 끈다
        setAutoReturnEnabled(false)

        // 📌 처음 진입 시 메인 화면 표시
        setContentView(R.layout.activity_main)
        setupTopBar()

        setupImageSlide()
        setupButtons()
        resetMoveModeTimer()

        // 📌 여기서 행사장 이동 모드 Helper 연결
        generalMoveHelper = GeneralMoveModeHelper(
            activity = this,
            showHomeLayout = {
                // 메인 화면으로 복귀
                setContentView(R.layout.activity_main)
                setupTopBar()
                setupImageSlide()
                setupButtons()
            },
            showFaceLayout = {
                // Temi 안내에서 이미 사용 중인 얼굴 화면으로 전환
                setContentView(R.layout.activity_face)
            }
        )
    }

    override fun onResume() {
        super.onResume()
        resetMoveModeTimer()
    }

    override fun onPause() {
        super.onPause()
        moveModeHandler.removeCallbacks(moveModeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        imgHandler.removeCallbacks(imageSwitcher)
    }

    // -------------------- 기능 구현 --------------------

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            resetMoveModeTimer()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resetMoveModeTimer() {
        moveModeHandler.removeCallbacks(moveModeRunnable)
        moveModeHandler.postDelayed(moveModeRunnable, MOVE_MODE_TIMEOUT)
    }

    private fun setupImageSlide() {
        homeLeftImg = findViewById(R.id.homeleftimg)
        imgHandler.post(imageSwitcher)
    }

    private fun setupButtons() {
        val db = FirebaseDatabase.getInstance(DB_URL)
        val restRef = db.reference.child("Directions").child("rest")
        restRef.removeValue().addOnCompleteListener { restRef.setValue(0) }

        val btnCoshoti = findViewById<CardView>(R.id.csti_button)
        btnCoshoti.setOnClickListener {
            startActivity(Intent(this, CoshowtiActivity::class.java))
        }

        findViewById<View>(R.id.minigame_button).setOnClickListener {
            startActivity(Intent(this, GameSelectActivity::class.java))
        }

        val cardLocation: View = findViewById(R.id.navigation_button)
        cardLocation.setOnClickListener {
            startActivity(Intent(this, MapCoShowActivity::class.java))
        }
    }

    private fun setupTopBar() {
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE
            isEnabled = false
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnHome?.setOnClickListener {}

        val btnHelp = findViewById<ImageButton>(R.id.btnHelp)
        btnHelp?.setOnClickListener {
            showHelpPopup()
        }
    }
}

