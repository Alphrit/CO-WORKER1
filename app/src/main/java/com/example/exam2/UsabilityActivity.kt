package com.example.exam2

import android.os.Bundle
import android.widget.ImageView
import android.view.View
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.EditText
import android.widget.RatingBar
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase

class UsabilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usability) // usability.xml 연결

        // 상단 상태바 + 하단 내비게이션바 숨기기 (구 버전 호환 방식)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // 이전 버튼
        val cancel1 = findViewById<MaterialCardView>(R.id.cancel1)
        cancel1.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // include된 usability1 / usability2 레이아웃 참조
        val usability1 = findViewById<View>(R.id.usability1_container)
        val usability2 = findViewById<View>(R.id.usability2_container)
        val dimOverlay = findViewById<View>(R.id.dimOverlay)

        // 질문 텍스트뷰 연결 (include 내부에서 findViewById 가능)
        val q1 = usability1.findViewById<android.widget.TextView>(R.id.q1)
        val q2 = usability1.findViewById<android.widget.TextView>(R.id.q2)
        val q3 = usability1.findViewById<android.widget.TextView>(R.id.q3)

        val q4 = usability2.findViewById<android.widget.TextView>(R.id.q4)
        val q5 = usability2.findViewById<android.widget.TextView>(R.id.q5)
        val q6 = usability2.findViewById<android.widget.TextView>(R.id.q6)


        // 어떤 화면에서 넘어왔는지에 따라 질문 문구를 달리하기 위한 타입
        // singleplay_result -> "singlequiz"
        // PrintPhotoActivity -> "stickerphoto"
        val surveyType = intent.getStringExtra("surveyType") ?: "사진유형"

        // 설문 유형에 따라 질문 텍스트 변경
        when (surveyType) {
            "코쇼퀴즈" -> {
                // 퀴즈(singleplay_result)에서 온 경우 질문 세트
                q1.text = "코쇼퀴즈 기능 사용은 전반적으로 어땠나요?"
                q2.text = "코쇼퀴즈를 푸는 과정은 전반적으로 얼마나 쉬웠나요?"
                q3.text = "코쇼퀴즈를 풀면서 좋았던 점이나 아쉬웠던 점이 있었다면 적어주세요."
                q4.text = "코쇼퀴즈를 다른 사람에게 추천하고 싶으신가요?"
                q5.text = "코쇼퀴즈의 난이도는 전반적으로 적절했다고 느끼셨나요?"
                q6.text = "코쇼퀴즈에서 특히 재미있던 점이나 어렵거나\n이해하기 어려웠던 점이 있었다면 적어주세요."
            }
            "스티커사진" -> {
                // 스티커사진(PrintPhotoActivity)에서 온 경우 질문 세트
                q1.text = "스티커사진 기능 사용은 전반적으로 어땠나요?"
                q2.text = "스티커사진 촬영 과정(템플릿 선택·촬영·저장)은 전반적으로 얼마나 쉬웠나요?"
                q3.text = "스티커사진을 쓰면서 좋았던 점이나 아쉬웠던 점이 있었다면 적어주세요."
                q4.text = "스티커사진 기능을 다른 사람에게 추천하고 싶으신가요?"
                q5.text = "스티커사진 기능이 CO-SHOW 관람을 더 재미있게 해줬다고 느끼셨나요?"
                q6.text = "스티커사진의 촬영 방식이나 결과물(프레임· 사진 구성 등)에서\n개선되었으면 하는 점이 있다면 적어주세요."
            }
            "길안내" -> {
                // 길안내에서 온 경우 질문 세트
                q1.text = "길안내 기능 사용은 전반적으로 어땠나요?"
                q2.text = "길안내 기능을 사용하는 것은 전반적으로 얼마나 쉬웠나요?"
                q3.text = "길안내 기능을 쓰면서 좋았던 점이나 아쉬웠던 점이 있었다면 적어주세요."
                q4.text = "CO-SHOW에서 다른 사람에게 Temi 길안내 기능을 추천하고 싶으신가요?"
                q5.text = "Temi가 안내한 길이 내가 원한 목적지까지 잘 데려다주었다고 느끼셨나요??"
                q6.text = "길안내 과정(목적지 찾기·선택·이동·도착) 중 특히 헷갈리거나\n개선되면 좋았다고 느낀 부분을 적어주세요."
            }
            "코쇼TI" -> {
                // 길안내에서 온 경우 질문 세트
                q1.text = "코쇼TI 기능 사용은 전반적으로 어땠나요?"
                q2.text = "코쇼TI 설문을 진행하는 과정은 전반적으로 얼마나 쉬웠나요?"
                q3.text = "코쇼TI를 사용하면서 좋았던 점이나 아쉬웠던 점이 있었다면 적어주세요."
                q4.text = "코쇼TI 검사를 다른 사람에게도 해 보라고 권하고 싶으신가요? "
                q5.text = "코쇼TI가 제시한 유형과 추천 부스가 나와 잘 맞는다고 느끼셨나요?"
                q6.text = "코쇼TI의 질문, 결과 화면, 추천 부스 중에서\n특히 바뀌면 좋겠다고 느낀 점이 있다면 적어주세요."
            }

        }

        // 초기 상태: usability1만 보이게, usability2는 숨김
        usability1.visibility = View.VISIBLE
        usability2.visibility = View.GONE

        // next / prev 이미지뷰 버튼 참조 (id는 xml과 맞춰야 함)
        val nextButton = findViewById<ImageView>(R.id.next)
        val prevButton = findViewById<ImageView>(R.id.prev)

        // next: usability1을 GONE, usability2를 VISIBLE
        nextButton?.setOnClickListener {
            usability1.visibility = View.GONE
            usability2.visibility = View.VISIBLE
        }

        // prev: usability1을 VISIBLE, usability2를 GONE
        prevButton?.setOnClickListener {
            usability1.visibility = View.VISIBLE
            usability2.visibility = View.GONE
        }

        // ===== 답변 요소 가져오기 =====
        val a1 = usability1.findViewById<RatingBar>(R.id.a1)
        val a2 = usability1.findViewById<LinearLayout>(R.id.a2)
        val a3 = usability1.findViewById<EditText>(R.id.a3)

        val a4 = usability2.findViewById<RatingBar>(R.id.a4)
        val a5 = usability2.findViewById<LinearLayout>(R.id.a5)
        val a6 = usability2.findViewById<EditText>(R.id.a6)

        // 전화번호 입력 EditText (모달 안에서 사용)
        val phoneEdit = findViewById<EditText>(R.id.phonenumber)

        // ===== 이모지 기본 흑백 처리 =====
        val grayMatrix = android.graphics.ColorMatrix()
        grayMatrix.setSaturation(0f)
        val grayFilter = android.graphics.ColorMatrixColorFilter(grayMatrix)

        // a2, a5 내부 이모지 전체 흑백 초기화
        for (i in 0 until a2.childCount) {
            val child = a2.getChildAt(i) as ImageView
            child.colorFilter = grayFilter
        }
        for (i in 0 until a5.childCount) {
            val child = a5.getChildAt(i) as ImageView
            child.colorFilter = grayFilter
        }

        val submit = findViewById<MaterialCardView>(R.id.submit1)

        val modal = findViewById<View>(R.id.modal)

        // Firebase Realtime Database 참조 (usabilitytest 디렉토리)
        val usabilityRef = FirebaseDatabase.getInstance(
            "https://exam-afefa-default-rtdb.firebaseio.com"
        ).reference
            .child("usability")
            .child(surveyType)

        // 모달 내부 버튼 참조
        val cancel2 = findViewById<MaterialCardView>(R.id.cancel2)
        val submit2 = findViewById<MaterialCardView>(R.id.submit2)

        // 이모지 선택 인덱스 구하는 함수 (1~N, 선택 없으면 0)
        fun getEmojiIndex(container: LinearLayout): Int {
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is ImageView) {
                    if (child.colorFilter == null) {
                        // colorFilter == null 이면 컬러 상태 → 선택된 이모지
                        return i + 1
                    }
                }
            }
            return 0
        }

        // 설문 응답을 Firebase에 저장하는 함수
        fun saveUsabilityToFirebase(includePhone: Boolean) {
            val q1Score = a1.rating
            val q2Choice = getEmojiIndex(a2)
            val q3Text = a3.text?.toString() ?: ""

            val q4Score = a4.rating
            val q5Choice = getEmojiIndex(a5)
            val q6Text = a6.text?.toString() ?: ""

            val phone = if (includePhone) {
                phoneEdit?.text?.toString() ?: ""
            } else {
                ""
            }

            val data = HashMap<String, Any?>()
            data["surveyType"] = surveyType
            data["q1_rating"] = q1Score
            data["q2_choice"] = q2Choice
            data["q3_text"] = q3Text
            data["q4_rating"] = q4Score
            data["q5_choice"] = q5Choice
            data["q6_text"] = q6Text

            if (includePhone) {
                data["phone"] = phone
            }

            usabilityRef.push().setValue(data)
        }

        // 모달에서 '그냥 제출'(cancel2) → 전화번호 없이 응답만 저장
        cancel2.setOnClickListener {
            saveUsabilityToFirebase(includePhone = false)

            // 모달 및 딤 해제
            modal.visibility = View.GONE
            dimOverlay.visibility = View.GONE
            usability1.alpha = 1f
            usability2.alpha = 1f

            // 메인(이전 화면)으로 돌아가기
            finish()
        }

        // 모달에서 '전화번호와 함께 제출'(submit2) → phone 포함 저장
        submit2.setOnClickListener {
            saveUsabilityToFirebase(includePhone = true)

            // 모달 및 딤 해제
            modal.visibility = View.GONE
            dimOverlay.visibility = View.GONE
            usability1.alpha = 1f
            usability2.alpha = 1f

            // 메인(이전 화면)으로 돌아가기
            finish()
        }

        fun updateSubmit() {
            // 1, 4: 별점이 0보다 큰지
            val a1Answered = a1.rating > 0f
            val a4Answered = a4.rating > 0f

            // 2, 5: 이모지 중 하나라도 컬러(= colorFilter == null)인지
            fun isEmojiAnswered(container: LinearLayout): Boolean {
                for (i in 0 until container.childCount) {
                    val child = container.getChildAt(i)
                    if (child is ImageView) {
                        if (child.colorFilter == null) {
                            return true
                        }
                    }
                }
                return false
            }
            val a2Answered = isEmojiAnswered(a2)
            val a5Answered = isEmojiAnswered(a5)

            // 3, 6: 텍스트가 비어 있지 않은지
            val a3Answered = !a3.text.isNullOrBlank()
            val a6Answered = !a6.text.isNullOrBlank()

            val all = a1Answered && a2Answered && a3Answered && a4Answered && a5Answered && a6Answered

            android.util.Log.d(
                "SUBMIT_CHECK",
                "1=${a1Answered} 2=${a2Answered} 3=${a3Answered} 4=${a4Answered} 5=${a5Answered} 6=${a6Answered}"
            )

            if (all) {
                submit.isClickable = true
                submit.isEnabled = true
                submit.setCardBackgroundColor(Color.parseColor("#FFDD57"))
            } else {
                submit.isClickable = false
                submit.isEnabled = false
                submit.setCardBackgroundColor(Color.parseColor("#D7D7D7"))
            }
        }

        // 초기 상태 한 번 갱신
        updateSubmit()

        // RatingBar 체크 (a1, a4)
        a1.setOnRatingBarChangeListener { _, _, _ ->
            updateSubmit()
        }
        a4.setOnRatingBarChangeListener { _, _, _ ->
            updateSubmit()
        }

        // 선택형(이모지) a2 / a5
        fun setupOptionClicks(container: LinearLayout) {
            for (i in 0 until container.childCount) {
                container.getChildAt(i).setOnClickListener { v ->
                    // 먼저 전체를 흑백으로
                    for (j in 0 until container.childCount) {
                        val child = container.getChildAt(j)
                        if (child is ImageView) {
                            child.colorFilter = grayFilter
                        }
                    }
                    // 클릭된 것만 컬러 복구
                    val clicked = v
                    if (clicked is ImageView) {
                        clicked.colorFilter = null
                    }
                    updateSubmit()
                }
            }
        }

        setupOptionClicks(a2)
        setupOptionClicks(a5)

        // EditText (a3, a6)
        fun setupEditWatcher(edit: EditText) {
            edit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateSubmit()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        setupEditWatcher(a3)
        setupEditWatcher(a6)

        submit.setOnClickListener {
            dimOverlay.visibility = View.VISIBLE
            usability1.alpha = 0.3f
            usability2.alpha = 0.3f
            modal.visibility = View.VISIBLE
        }
    }
}
