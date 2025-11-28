package com.example.exam2

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.exam2.data.CoshowtiResults

class CoshowtiResultActivity : BaseActivity() {

    private lateinit var tvResultCategory: TextView
    private lateinit var tvResultCode: TextView
    private lateinit var tvResultName: TextView
    private lateinit var tvResultDescription: TextView
    private lateinit var ivResultCharacter: ImageView
    private lateinit var btnNext: ImageButton
    private lateinit var btnTemiCharacter: ImageButton

    private var resultCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply fullscreen mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContentView(R.layout.activity_coshowti_result)

        // Get result code from intent
        resultCode = intent.getStringExtra("RESULT_CODE") ?: "DBAS"

        // Initialize views
        tvResultCategory = findViewById(R.id.tvResultCategory)
        tvResultCode = findViewById(R.id.tvResultCode)
        tvResultName = findViewById(R.id.tvResultName)
        tvResultDescription = findViewById(R.id.tvResultDescription)
        ivResultCharacter = findViewById(R.id.ivResultCharacter)
        btnNext = findViewById(R.id.btnNext)
        btnTemiCharacter = findViewById(R.id.btnTemiCharacter)

        // Setup common top bar
        // Back button: Hide and disable
        // Home button: Go to MainActivity
        val topBar = findViewById<View>(R.id.topBarResult)
        setupCommonTopBar(
            topBar,
            backAction = null,
            homeAction = { navigateToHome() }
        )

        // Hide and disable back button
        val btnBack = topBar.findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE
            isEnabled = false
        }

        // Load result data
        loadResultData()

        // Set click listeners
        btnNext.setOnClickListener {
            navigateToDetail()
        }

        btnTemiCharacter.setOnClickListener {
            val intent = Intent(this, UsabilityActivity::class.java).apply {
                putExtra("surveyType", "코쇼TI")
            }
            startActivity(intent)
        }
    }

    private fun loadResultData() {
        val result = CoshowtiResults.getResult(resultCode)

        if (result != null) {
            tvResultCategory.text = result.category
            tvResultCode.text = "(${result.code})"
            tvResultName.text = result.name
            tvResultDescription.text = result.description

            // Set image based on category
            val imageResId = when (result.category) {
                "그린테크형" -> R.drawable.greentechimage
                "스마트소재형" -> R.drawable.smartingredientimage
                "모빌리티형" -> R.drawable.mobilityimage
                "디지털지능형" -> R.drawable.coshowti_result_category_charactor
                else -> R.drawable.coshowti_result_category_charactor // Default fallback
            }
            ivResultCharacter.setImageResource(imageResId)
        } else {
            // Fallback data
            tvResultCategory.text = "디지털지능형"
            tvResultCode.text = "($resultCode)"
            tvResultName.text = "결과를 찾을 수 없습니다"
            tvResultDescription.text = "다시 시도해주세요."
            ivResultCharacter.setImageResource(R.drawable.coshowti_result_category_charactor)
        }
    }

    private fun navigateToDetail() {
        val intent = Intent(this, CoshowtiResultDetailActivity::class.java)
        intent.putExtra("RESULT_CODE", resultCode)
        startActivity(intent)
    }
}
