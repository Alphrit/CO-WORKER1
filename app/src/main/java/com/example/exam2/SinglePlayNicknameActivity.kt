package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView


class SinglePlayNicknameActivity : BaseActivity() {
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
        setContentView(R.layout.singleplay_nickname)  // singleplay_nickname.xml과 연결

        // 상단 상태바 + 하단 내비게이션바 숨기기 (구 버전 호환 방식)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val playerEditText = findViewById<EditText>(R.id.player)
        val startbutton = findViewById<MaterialCardView>(R.id.startbutton)
        startbutton.isEnabled = false

        playerEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                startbutton.isEnabled = !s.isNullOrBlank()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        startbutton.setOnClickListener {
            val nickname = playerEditText.text.toString().trim()

            // 닉네임 넘기기
            val intent = Intent(this, BaseQuizActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }
        setupTopBar()
    }

}