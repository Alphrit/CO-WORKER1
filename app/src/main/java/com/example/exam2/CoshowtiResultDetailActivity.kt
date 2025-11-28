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
import com.example.exam2.data.ThemeItem
import com.example.exam2.data.CoshowtiResults
import com.example.exam2.InformationByThemeActivity.Companion.EXTRA_FROM_RESULT
import com.example.exam2.InformationByThemeActivity.Companion.EXTRA_BOOTH_TITLE
import com.example.exam2.InformationByThemeActivity.Companion.EXTRA_BOOTH_CATEGORY

class CoshowtiResultDetailActivity : BaseActivity() {

    private lateinit var tvResultCategory: TextView
    private lateinit var tvResultCode: TextView
    private lateinit var tvResultName: TextView
    private lateinit var btnTemiCharacter: ImageButton
    private lateinit var btnBackArrow: ImageButton

    private lateinit var boothCard1: View
    private lateinit var boothCard2: View
    private lateinit var boothCard3: View

    private var resultCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체 화면 모드
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContentView(R.layout.activity_coshowti_result_detail)

        // 결과 코드 받기
        resultCode = intent.getStringExtra("RESULT_CODE") ?: "DBAS"

        // 뷰 바인딩
        tvResultCategory = findViewById(R.id.tvResultCategory)
        tvResultCode = findViewById(R.id.tvResultCode)
        tvResultName = findViewById(R.id.tvResultName)
        btnTemiCharacter = findViewById(R.id.btnTemiCharacter)

        // 공통 상단바 설정
        val topBar = findViewById<View>(R.id.topBarDetail)
        setupCommonTopBar(
            topBar,
            backAction = null,              // 이 화면에서 별도 뒤로가기 액션 없음
            homeAction = { navigateToHome() }
        )

        // 상단의 btnBack 숨기기
        val btnBack = topBar.findViewById<ImageButton>(R.id.btnBack)
        btnBack?.apply {
            visibility = View.GONE
            isEnabled = false
        }

        // 부스 카드 뷰
        boothCard1 = findViewById(R.id.boothCard1)
        boothCard2 = findViewById(R.id.boothCard2)
        boothCard3 = findViewById(R.id.boothCard3)

        // 데이터 로드
        loadResultData()

        btnTemiCharacter.setOnClickListener {
            val intent = Intent(this, UsabilityActivity::class.java).apply {
                // 코쇼TI에서 온 설문이라는 의미
                putExtra("surveyType", "코쇼TI")
            }
            startActivity(intent)
        }

        // 결과 화면 왼쪽 상단 화살표(자체 back 버튼) → 이전 결과 화면으로
        btnBackArrow = findViewById(R.id.btnBackArrow)
        btnBackArrow.setOnClickListener {
            finish()
        }
    }

    // TemiNavigationHelper는 이 액티비티에서 더 이상 쓰지 않으므로 onStart/onStop 에서도 호출 X

    private fun loadResultData() {
        val result = CoshowtiResults.getResult(resultCode)

        if (result != null) {
            tvResultCategory.text = result.category
            tvResultCode.text = "(${result.code})"
            tvResultName.text = result.name

            if (result.booths.size >= 3) {
                setupBoothCard(boothCard1, result.booths[0])
                setupBoothCard(boothCard2, result.booths[1])
                setupBoothCard(boothCard3, result.booths[2])
            }
        } else {
            tvResultCategory.text = "디지털지능형"
            tvResultCode.text = "($resultCode)"
            tvResultName.text = "결과를 찾을 수 없습니다"
        }
    }

    private fun setupBoothCard(cardView: View, themeItem: ThemeItem) {
        val tvBoothTitle = cardView.findViewById<TextView>(R.id.tvBoothTitle)
        val tvBoothDescription = cardView.findViewById<TextView>(R.id.tvBoothDescription)
        val ivBoothImage = cardView.findViewById<ImageView>(R.id.ivBoothImage)
        val tvImagePlaceholder = cardView.findViewById<TextView>(R.id.tvImagePlaceholder)
        val btnFindBoothLocation = cardView.findViewById<Button>(R.id.btnFindBoothLocation)

        // 제목
        tvBoothTitle.text = themeItem.title

        // 설명
        if (themeItem.description.isNotEmpty()) {
            tvBoothDescription.text = themeItem.description
            tvBoothDescription.visibility = View.VISIBLE
        } else {
            tvBoothDescription.visibility = View.GONE
        }

        // 이미지
        ivBoothImage.setImageResource(themeItem.imageResId)
        ivBoothImage.visibility = View.VISIBLE
        tvImagePlaceholder.visibility = View.GONE

        // 버튼 클릭 → InformationByThemeActivity로 이동 + 부스 정보 전달
        btnFindBoothLocation.setOnClickListener {
            val intent = Intent(
                this@CoshowtiResultDetailActivity,
                InformationByThemeActivity::class.java
            ).apply {
                putExtra(EXTRA_FROM_RESULT, true)
                putExtra(EXTRA_BOOTH_TITLE, themeItem.title)
                putExtra(EXTRA_BOOTH_CATEGORY, themeItem.category)
            }
            startActivity(intent)
        }
    }

    // (선택) exam4에 남아있던 팝업 함수 – TemiNavigationHelper 안 쓰는 버전
    private fun showBoothNavigationDialog(boothTitle: String, category: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_booth_navigation)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvMessage = dialog.findViewById<TextView>(R.id.tvBoothNavigationMessage)
        val btnClose = dialog.findViewById<Button>(R.id.btnCloseDialog)

        val boothLocationName = "${category}"
        tvMessage.text = "\"$boothTitle\" 부스로\n안내를 시작합니다."

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}