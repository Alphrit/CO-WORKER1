package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView

class GameSelectActivity : BaseActivity() {
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
        setContentView(R.layout.gameselect) // gameselect.xml 연결

        //  quiz_button 클릭 시 select1p2p.xml로 이동
        val quizButton = findViewById<MaterialCardView>(R.id.quiz_button)
        quizButton.setOnClickListener {
            val intent = Intent(this, Select1p2pActivity::class.java)
            startActivity(intent)
        }

        //  photobutton 클릭 시 photo.xml로 이동
        val photoButton = findViewById<MaterialCardView>(R.id.photo_button)
        photoButton.setOnClickListener {
            val intent = Intent(this, FrameSelectActivity::class.java)
            startActivity(intent)
        }
        setupTopBar()
    }

}