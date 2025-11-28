package com.example.exam2

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.button.MaterialButton
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import android.widget.ImageView
import android.view.MotionEvent
import android.widget.CheckBox
import android.graphics.Color
import android.view.Gravity
import android.view.WindowManager
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Button
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class InformationByThemeActivity : BaseActivity() {
    companion object {
        const val EXTRA_FROM_RESULT = "extra_from_result"
        const val EXTRA_BOOTH_TITLE = "extra_booth_title"
        const val EXTRA_BOOTH_CATEGORY = "extra_booth_category"
    }
    data class ThemeItem(
        val title: String,        // 이름
        val category: String,     // 종류
        val imageResId: Int,      // 그리드 썸네일 이미지
        val detailImageResId: Int = imageResId, // 팝업 상세 이미지(기본은 썸네일과 동일)
        val periodstart: String,       // 체험시간시작
        val periodend: String,         // 체험시간끝
        val time: Int,            // 소요시간(분)
        val description: String,  // 소개
        val how: String,          // 체험방법
        val target: String,       // 참여대상
        val periodRangestart: String,   // 체험기간시작
        val periodRangeend: String   // 체험기간끝
    )
    private lateinit var txtClock: TextView
    private lateinit var temiNavigator: TemiNavigationHelper
    private var onlyCurrentRunning: Boolean = false

    private val clockHandler = Handler(Looper.getMainLooper())
    private val clockRunnable = object : Runnable {
        override fun run() {
            val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).time

            val formatter = SimpleDateFormat("HH:mm", Locale.KOREA).apply {
                timeZone = TimeZone.getTimeZone("Asia/Seoul")
            }

            txtClock.text = formatter.format(now)

            clockHandler.postDelayed(this, 1000L)
        }
    }

    private val allThemeItems: List<ThemeItem> by lazy {
        listOf(
            ThemeItem(
                title = "ROBO SHOW(4족보행 로봇 및 테미 체험)",
                category = "지능형로봇",
                imageResId = R.drawable.img24,
                detailImageResId = R.drawable.img24_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 30,
                description = "4족보행 로봇과 안내 로봇 테미를 함께 체험하는 로봇 쇼케이스이다. 4족 로봇은 네 다리로. 균형을 잡고 걷는 과정을 보여주고, 테미는 음성 인식과 자율주행 기능을 활용해 사람을 안내하거나 상호작용을 수행한다.\n" +
                        "각 로봇의 움직임과 반응을 관찰하면서, 지능형 로봇이 주변 환경을 인식하고 상황에 맞게 행동을 선택하는 기본 구조를 이해할 수 있다. 서비스 로봇과 산업형 로봇의 활용 가능성을 함께 떠올려볼 수 있는 부스이다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AI 드로잉 로봇 및 오목 로봇 체험",
                category = "지능형로봇",
                imageResId = R.drawable.img23,
                detailImageResId = R.drawable.img23_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 20,
                description = "AI가 탑재된 로봇이 그림을 그리거나 오목을 두는 모습을 직접 보는 체험이다. 로봇이 입력을 인식하고, 수를 계산하거나 그림 경로를 계획한 뒤 움직이는 과정을 통해 AI 기반 로봇의 판단 구조를 이해할 수 있다.\n" +
                        "단순한 놀이처럼 보이지만, (센서 입력 → 알고리즘 분석 → 모터 제어)라는 전형적인 로봇 동작 흐름이 그대로 드러난다. 게임을 즐기면서도 로봇이 ‘어떻게 생각하는지’를 자연스럽게 알 수 있는 프로그램이다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "일반인 로봇 교육 프로그램 1(경주로봇 만들기)",
                category = "지능형로봇",
                imageResId = R.drawable.img22,
                detailImageResId = R.drawable.img22_1,
                periodstart = "11:00",
                periodend = "12:00",
                time = 60,
                description = "경주용 로봇을 직접 조립하고, 주행을 시험해보는 제작형 프로그램이다. 바퀴, 모터, 전원, 제어 회로를 하나씩 연결하며 로봇이 움직이기 위한 최소 구성 요소를 이해할 수 있다.\n" +
                        "조립 후 트랙을 달려보면서, 무게 배분, 바퀴 마찰, 전원 공급 등이 주행 결과에 어떤 영향을 미치는지 체감할 수 있다. 로봇을 처음 접하는 참가자에게 기초를 익히기 좋은 입문형 체험이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "일반인 로봇 교육 프로그램2(다이노 랩터 로봇 만들기)",
                category = "지능형로봇",
                imageResId = R.drawable.img25,
                detailImageResId = R.drawable.img25_1,
                periodstart = "12:00",
                periodend = "13:00",
                time = 60,
                description = "로봇 키트를 활용해 다이노 랩터 형태의 로봇을 직접 조립해보는 제작형 교육 프로그램이다. 바퀴 대신 다리를 사용해 이동하는 구조를 통해, 관절 움직임과 기계적 연결 방식이 로봇 동작에 어떤 영향을 주는지 자연스럽게 이해할 수 있다.\n" +
                        "조립이 끝난 후에는 실제로 로봇을 움직여보며 동작 반응과 메커니즘을 확인한다. 단순한 조립 체험을 넘어, 구성 요소가 연결되어 하나의 움직임을 만드는 기본 구조를 배울 수 있어 초등학생도 부담 없이 참여할 수 있는 프로그램이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "일반인 로봇 교육 프로그램3(하프휠 로봇 만들기)",
                category = "지능형로봇",
                imageResId = R.drawable.img26,
                detailImageResId = R.drawable.img26_1,
                periodstart = "13:00",
                periodend = "14:00",
                time = 60,
                description = "하프휠 구조의 로봇을 로봇 키트로 직접 조립해보는 제작형 교육 프로그램이다. 바퀴 절반 형태의 독특한 구조 덕분에 균형 유지와 회전 방식이 일반 로봇과 다르게 작동하며, 이를 조립 과정에서 자연스럽게 이해할 수 있다.\n" +
                        "조립 후 실제로 로봇을 굴려보면, 무게 중심 변화와 회전 반응이 어떻게 움직임을 만들어내는지 바로 확인할 수 있다. 간단한 구조지만 기계적 안정성과 동작 메커니즘을 익히기에 좋아 초등학생도 부담 없이 참여할 수 있는 프로그램이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "일반인 로봇 교육 프로그램4(유선 스파이더로봇 만들기)",
                category = "지능형로봇",
                imageResId = R.drawable.img27,
                detailImageResId = R.drawable.img27_1,
                periodstart = "14:00",
                periodend = "15:00",
                time = 60,
                description = "여러 개의 다리를 가진 거미형 로봇을 직접 조립해보는 프로그램이다. 다리 관절을 어떤 순서와 패턴으로 움직여야 자연스럽게 이동하는지, 기계적 연결 구조가 어떤 역할을 하는지 제작 과정에서 알 수 있다.\n" +
                        "유선 제어 방식을 사용해 사용자가 조작 신호를 직접 보내면서, 다리 개수·관절 구조·동작 패턴이 로봇 전체 움직임에 어떤 차이를 만들어내는지 확인할 수 있다. 복잡한 다리 구조를 가진 로봇 메커니즘을 이해하기 좋은 체험이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "일반인 로봇 교육프로그램5(휴머노이드 이론교육 및 미션수행)",
                category = "지능형로봇",
                imageResId = R.drawable.img21,
                detailImageResId = R.drawable.img21_1,
                periodstart = "15:00",
                periodend = "16:30",
                time = 90,
                description = "고성능 소형 휴머노이드의 운동 원리와 제어 방식을 배우고, 직접 미션 경기를 수행해보는 교육형 체험이다. 관절 구조, 균형 제어, 동작 계획 등 휴머노이드 로봇의 핵심 개념을 이론으로 먼저 이해한 뒤 실제 미션 수행으로 이어지는 구성이다.\n" +
                        "교육 이후에는 참가자가 직접 로봇을 조작해 미션을 해결해보며 이론과 실제 동작이 어떻게 연결되는지 확인할 수 있다. 로봇의 동작 반응, 균형 유지, 보행 제어가 어떻게 이루어지는지 직관적으로 체감할 수 있어 로봇 제어를 처음 접하는 사람도 쉽게 이해할 수 있는 프로그램이다.",
                how = "사전모집",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-26"
            ),
            ThemeItem(
                title = "태양전지 패널",
                category = "에너지신산업",
                imageResId = R.drawable.img28,
                detailImageResId = R.drawable.img28_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "햇빛이 전기로 바뀌는 과정을 눈앞에서 확인하는 태양광 기초 체험이다. 패널에 닿는 빛의 양과 각도에 따라 전력 생산량이 달라지는 모습을 직접 보며 태양광 발전의 기본 원리를 자연스럽게 이해할 수 있다.\n" +
                        "빛·전기·소재 기술이 하나의 시스템으로 연결되는 과정을 관찰하며 재생에너지 기술이 어떻게 작동하는지 감각적으로 파악할 수 있다. 친환경 에너지의 출발점을 가장 직관적으로 경험할 수 있는 프로그램이다",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "에코루프 키친스테이션",
                category = "에너지신산업",
                imageResId = R.drawable.img29,
                detailImageResId = R.drawable.img29_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "주방에서 발생하는 열과 에너지를 다시 회수해 활용하는 히트펌프 기반 미래형 주방 시스템이다. 식기세척기·음식물처리기·히트펌프가 하나의 장치로 연결되면서 폐열을 재활용하는 과정을 직접 확인할 수 있다.\n" +
                        "가정 속 에너지 순환 구조가 어떻게 효율을 높이고 탄소를 줄이는지 명확하게 드러나며, 생활 환경이 기술을 통해 어떻게 변화해가는지 잘 보여주는 체험이다. 미래 친환경 주방의 모습을 가까이에서 확인할 수 있다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "수소 스테이션",
                category = "에너지신산업",
                imageResId = R.drawable.img30,
                detailImageResId = R.drawable.img30_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "수소전기차가 연료를 공급받기까지의 과정(저장, 압축, 충전)을 한눈에 볼 수 있는 프로그램이다. 수소가 어떻게 이동하고 어떤 기술로 안전하게 다뤄지는지 단계별 흐름을 통해 쉽게 이해할 수 있다.\n" +
                        "수소 인프라가 왜 미래 교통의 핵심으로 불리는지, 수소가 가진 특성과 기술적 난이도가 어떤 의미인지 자연스럽게 체감하게 된다. 수소 모빌리티 시대의 기반 기술을 가장 기본부터 배울 수 있다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "플러스에너지빌딩",
                category = "에너지신산업",
                imageResId = R.drawable.img31,
                detailImageResId = R.drawable.img31_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "건물이 스스로 에너지를 생산해 소비량보다 더 많은 전력을 만드는 차세대 스마트 건축 모델이다. 태양광 발전, 고효율 단열, 에너지 관리 시스템이 결합된 구조를 모형을 통해 직관적으로 확인할 수 있다.\n" +
                        "BIPV(건물일체형 태양광) 기술과 에너지 저장 시스템이 실제 도시 환경에서 어떻게 활용되는지 이해하게 되며, 건물이 에너지 생산 주체가 되는 미래 도시의 모습을 실감할 수 있다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "태양전지 자동차경주 체험물",
                category = "에너지신산업",
                imageResId = R.drawable.img32_1,
                detailImageResId = R.drawable.img32_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 20,
                description = "태양전지를 장착한 미니카라는 간단한 형태지만, 빛의 세기·각도·패널 반응에 따라 속도가 달라지는 과정이 명확하게 드러나는 체험이다. 직접 조립하고 달리게 하면서 태양광 → 전력 → 모터 → 주행이라는 에너지 흐름을 자연스럽게 익힐 수 있다. \n" +
                        "게임 요소와 학습 요소가 결합되어 어린이부터 어른까지 모두 몰입하는 인기 프로그램이다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "압전에너지 체험물",
                category = "에너지신산업",
                imageResId = R.drawable.img33,
                detailImageResId = R.drawable.img33_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 20,
                description = "발판을 밟는 작은 힘이 전기로 변환되는 압전(Piezo) 원리를 직접 체험할 수 있다. 밟는 순간 변화하는 전압 수치를 실시간으로 확인하며 기계적 에너지가 전기 에너지로 바뀌는 과정을 눈으로 확인하게 된다.\n" +
                        "센서, 환경 기술, IoT 장치에서 왜 압전 소재가 널리 사용되는지 자연스럽게 이해할 수 있는 프로그램이다. 한 걸음이 전력을 만드는 새로운 가능성을 경험할 수 있다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "수력 풍력에너지 체험",
                category = "에너지신산업",
                imageResId = R.drawable.img34,
                detailImageResId = R.drawable.img34_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "물의 흐름과 바람의 회전력이 전기를 만드는 원리를 모형으로 확인할 수 있는 프로그램이다. 수차·풍차가 회전하며 발전기로 전기를 생산하는 순간을 눈으로 보며 재생에너지의 기본 개념을 쉽게 이해하게 된다.\n" +
                        "직접 돌리고 조작하면서 물리적 힘 → 전기 에너지 변환 구조를 확실히 체득할 수 있다. 자연 에너지가 어떻게 기술로 변환되는지 배우기 좋은 콘텐츠다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "수소밸류체인 VR 체험",
                category = "에너지신산업",
                imageResId = R.drawable.img35,
                detailImageResId = R.drawable.img35_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 20,
                description = "수소가 (생산 → 저장 → 운송 → 활용)까지 이어지는 전 과정을 VR 속에서 몰입감 있게 체험하는 프로그램이다. 복잡했던 수소 인프라의 흐름을 눈앞에서 따라가며 수소경제의 전체 구조를 쉽게 파악할 수 있다.\n" +
                        "각 단계의 기술적 차이, 역할, 안전 요소 등을 직관적으로 배우게 되며 미래 에너지 체계가 어떻게 구축되는지 감각적으로 이해할 수 있다.",
                how = "현장접수",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "태양광 강아지 로봇",
                category = "에너지신산업",
                imageResId = R.drawable.img36,
                detailImageResId = R.drawable.img36_1,
                periodstart = "14:00",
                periodend = "16:00",
                time = 50,
                description = "태양광 패널을 연결하고 실험해 빛으로 움직이는 강아지 로봇을 직접 제작하는 체험**이다. 빛의 변화가 로봇 움직임으로 바로 연결되면서 태양광 변환 원리를 자연스럽게 배울 수 있다.\n" +
                        "조립·연결·동작 확인 과정을 통해 에너지 흐름이 어떻게 기계 움직임으로 이어지는지 실전적으로 이해할 수 있는 제작형 프로그램이다.",
                how = "사전모집",
                target = "중학생 이상",
                periodRangestart = "2025-11-29",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "수소연료전지자동차",
                category = "에너지신산업",
                imageResId = R.drawable.img37,
                detailImageResId = R.drawable.img37_1,
                periodstart = "10:30",
                periodend = "13:30",
                time = 40,
                description = "수소연료전지가 전기를 만들어 모터를 구동시키는 구조를 직접 실험하고 조립해보는 체험이다. 전극, 전해질, 연료 흐름 같은 교과서 개념이 실제 부품을 통해 눈앞에서 구조화되어 이해도가 크게 높아진다.\n" +
                        "작은 차량이지만 (에너지 생성–제어–구동)의 흐름이 모두 담겨 있어 미래 모빌리티 기술을 가장 직관적으로 배울 수 있다.",
                how = "사전모집",
                target = "중학생 이상",
                periodRangestart = "2025-11-27",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AICOSS 메타버스캠퍼스 체험",
                category = "인공지능",
                imageResId = R.drawable.img1,
                detailImageResId = R.drawable.img1_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "AI와 실감 기술로 구현된 AICOSS 메타버스 캠퍼스를 탐험하는 체험이다. 가상 공간 속 강의실, 복도, 광장 등을 이동하며 미래형 디지털 캠퍼스가 어떤 모습으로 구성되는지 살펴볼 수 있다.\n" +
                        "아바타로 이동하고, 공간 속 오브젝트와 상호작용하는 과정에서 온라인 수업 화면을 넘어서, 공간 전체가 학습 환경이 되는 메타버스 교육 방식을 자연스럽게 이해할 수 있다. AI와 가상 공간이 결합된 새로운 캠퍼스 형태를 한 번에 볼 수 있는 프로그램이다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-27",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "내 손안의 작은 발전기",
                category = "인공지능",
                imageResId = R.drawable.img38,
                detailImageResId = R.drawable.img38_1,
                periodstart = "15:00",
                periodend = "17:00",
                time = 40,
                description = "코일과 자석을 이용해 핸드 발전기를 직접 제작하는 실습형 프로그램이다. 회전 속도에 따라 전압이 달라지는 모습을 확인하며 전기 생성의 기본 원리를 확실하게 이해할 수 있다.\n" +
                        "만들고 바로 작동시켜보며 기계적 움직임이 전기가 되는 구조를 감각적으로 체득할 수 있는 입문형 에너지 제작 체험이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "미래자동차 VR 레이싱 체험",
                category = "미래자동차",
                imageResId = R.drawable.img10,
                detailImageResId = R.drawable.img10_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 40,
                description = "4D VR 레이싱 기기를 활용해 속도감 넘치는 주행을 직접 경험하는 몰입형 체험이다. 헤드셋 속 화면과 실제 진동·기울기 효과가 동시에 작동해 가상 트랙을 달리는 느낌이 생생하게 전달된다. 단순한 게임을 넘어, 화면–기기–신체 반응이 하나로 연결되는 VR 인터랙션 방식을 자연스럽게 이해할 수 있다.\n" +
                        "주행은 약 5분 동안 진행되며, 초등학생 이상이면 누구나 참여할 수 있다. 현장에서 신청 후 바로 체험할 수 있어 접근성이 높고, 짧은 시간에도 강한 몰입감을 제공하는 미래형 모빌리티 프로그램이다.",
                how = "현장접수",
                target = "초등학생 이상 누구나",
                periodRangestart = "2025-11-29",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "[이차전지 컨소시엄] 교육 프로그램(다면형 실감콘텐츠 영상 체험 및 VR 영상 체험)",
                category = "이차전지",
                imageResId = R.drawable.img45,
                detailImageResId = R.drawable.img45_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 15,
                description = "이차전지의 구조와 제조 과정을 다면형 실감 콘텐츠와 VR로 살펴보는 체험이다. 전극·분리막·전해질 등 배터리의 내부 구성 요소가 시각적으로 펼쳐져, 배터리가 어떻게 전기를 저장하고 방출하는지 원리를 쉽게 이해할 수 있다.\n" +
                        "VR 환경에서는 실제 공장에서 이루어지는 공정 흐름(전극 코팅, 조립, 화성(활성화) 과정)을 따라가며 이차전지가 완성되는 전 과정을 생생하게 볼 수 있다. 복잡하게 느껴지던 배터리 기술을 가장 이해하기 쉬운 방식으로 체험할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "제한없음",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AI를 활용한 명화속 몰입체험",
                category = "인공지능",
                imageResId = R.drawable.img2,
                detailImageResId = R.drawable.img2_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "AI가 분석·재구성한 명화 속 장면을 실제 공간처럼 구현한 몰입형 콘텐츠를 체험하는 프로그램이다. 평면 그림으로 보던 작품이 벽과 바닥, 주변 환경으로 확장되어, 관람자가 그림 속을 직접 걷는 형태로 구성된다.\n" +
                        "색감·구도·질감을 AI가 다시 표현한 화면을 따라 이동하며, 예술 작품을 ‘감상’하는 단계를 넘어 ‘공간 안에서 체험하는 방식’으로 즐길 수 있다. 예술과 기술이 만나면 전시 방식이 어떻게 달라지는지 확인하기에 좋은 체험이다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "업사이클링 카드지갑",
                category = "에너지신산업",
                imageResId = R.drawable.img39,
                detailImageResId = R.drawable.img39_1,
                periodstart = "14:00",
                periodend = "14:30",
                time = 30,
                description = "폐자원 또는 친환경 소재를 활용해 카드지갑을 만드는 지속가능 디자인 체험이다. 제작 과정 속에서 자원순환의 개념과 친환경 소재의 특성을 자연스럽게 배울 수 있다.\n" +
                        "완성된 지갑은 실용성과 디자인 모두 갖춘 결과물로, 환경 교육과 창작 경험을 동시에 제공하는 프로그램이다.",
                how = "사전모집",
                target = "전국민대상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "보조배터리 제작 체험(보조배터리 만들어봤어? 안 만들어 봤으면 말을 하지마!)",
                category = "에너지신산업",
                imageResId = R.drawable.img46,
                detailImageResId = R.drawable.img46_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 60,
                description = "실제 배터리 셀과 보호회로를 사용해 **보조배터리를 직접 제작하는 실습형 프로그램**이다. 전류가 흐르는 구조, 보호회로가 안전을 담당하는 방식 등 배터리의 기본 원리가 조립 과정에서 자연스럽게 드러난다.\n" +
                        "케이스 결합, 회로 연결, 작동 테스트까지 직접 수행하며 이차전지가 왜 정밀한 설계와 안전장치가 중요한지를 체감하게 된다. 완성된 보조배터리는 가져갈 수 있어, 학습과 성취감을 동시에 얻을 수 있는 체험이다.",
                how = "사전모집 & 현장접수",
                target = "제한없음",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "수소자동차 롱롱",
                category = "인공지능",
                imageResId = R.drawable.img40,
                detailImageResId = R.drawable.img40_1,
                periodstart = "10:30",
                periodend = "13:30",
                time = 40,
                description = "수소가 어떤 과정을 거쳐 전기에너지로 변환되고, 그 전기가 다시 모터를 움직이는 동력으로 이어지는지를 손으로 조립하며 배워볼 수 있는 체험이다. 또한 전지 반응을 눈으로 확인하면서 수소연료전지의 구조를 자연스럽게 이해할 수 있다.\n" +
                        "완성된 수소자동차가 실제로 움직이기 때문에 친환경 에너지가 이동수단의 동력으로 이어지는 흐름을 쉽게 체감할 수 있다. 중학생 이상 관람객이 미래형 친환경 모빌리티를 간단하면서도 확실하게 경험하기에 적합한 구성이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AI를 활용한 캐리커쳐 체험",
                category = "인공지능",
                imageResId = R.drawable.img3,
                detailImageResId = R.drawable.img3_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "참가자의 얼굴을 촬영하거나 입력하면 AI가 특징을 분석해 캐릭터 형태의 캐리커처 이미지로 변환하는 체험이다. 눈·입·윤곽·표정 등 여러 요소를 AI가 스스로 중요 포인트로 선택해 과장하거나 단순화한다.\n" +
                        "완성된 캐리커처를 통해, AI가 사람의 얼굴을 어떻게 인식하고 어떤 기준으로 ‘나만의 특징’을 잡아내는지 확인할 수 있다. 재미있는 결과물과 함께 컴퓨터 비전과 이미지 생성 기술 의 기본 원리를 간단히 살펴볼 수 있는 프로그램이다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "미래자동차 CO-SHOW 자율주행 체험",
                category = "미래자동차",
                imageResId = R.drawable.img11,
                detailImageResId = R.drawable.img11_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 60,
                description = "자율주행 자동차 모형을 직접 조립하고 색칠한 뒤, 마련된 트랙에서 주행까지 경험하는 제작·주행형 체험이다. 모형 조립을 통해 자율주행 차량의 기본 구조를 이해하고, 주행 과정에서 속도·회전·센서 작동 원리를 자연스럽게 익힐 수 있다. 손으로 만들고 실제 트랙에서 달려보는 흐름이 이어지기 때문에 자율주행 기술의 개념을 가장 직관적으로 체험할 수 있다.\n" +
                        "체험은 총 60분 동안 진행되며, 조립 30분 + 주행 30분 구성이다. 사전신청 후 예약된 회차에 맞춰 부스로 방문하면 참여할 수 있으며, 초등학생부터 성인까지 누구나 부담 없이 즐길 수 있는 프로그램이다.",
                how = "사전모집 & 현장접수",
                target = "누구나 가능~!",
                periodRangestart = "2025-11-29",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "나의 왕자님, 공주님을 찾아라!",
                category = "빅데이터",
                imageResId = R.drawable.img7_1,
                detailImageResId = R.drawable.img7,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "참가자가 이상형과 관련된 키워드와 조건을 입력하면, AI가 그 정보를 바탕으로 캐릭터 이미지를 생성하는 체험이다. 말로 표현한 취향이 데이터로 변환되고, 다시 시각적 결과물로 재구성되는 전 과정을 한 번에 볼 수 있다.\n" +
                        "생성된 캐릭터는 참가자의 취향을 기반으로 한 ‘데이터 버전 이상형’이다. 취향 분석, 이미지 생성, 특징 조합이 어떤 흐름으로 이루어지는지 간단하게 체험할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "초•중•고 학생 및 일반 관람객",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "웨어러블 보행 보조 로봇 : 보행 로봇 체험존",
                category = "바이오헬스",
                imageResId = R.drawable.img16,
                detailImageResId = R.drawable.img16_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 10,
                description = "보행 보조 로봇과 재활 로봇을 직접 착용해보고, 센서와 모터가 사용자의 움직임을 어떻게 인식하고 보조하는지 확인하는 체험이다. 걸음걸이, 균형, 힘의 전달을 로봇이 어떻게 읽고 지원하는지 몸으로 느낄 수 있다.\n" +
                        "장비를 착용하고 움직여보면서 재활 로봇이 운동 기능 회복과 일상 복귀에 어떤 도움을 줄 수 있는지 구체적으로 이해할 수 있다. 의료·헬스케어와 로봇 기술의 접점을 명확히 보여주는 프로그램이다",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "장애인을 위한 디지털 치료제 : e 스포츠 휠체어 레이싱 체험존",
                category = "바이오헬스",
                imageResId = R.drawable.img15,
                detailImageResId = R.drawable.img15_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 4,
                description = "휠체어 등의 보조 기기 조작을 게임과 연동해, 움직임이 디지털 환경 속 행동으로 변환되는 과정을 체험하는 프로그램이다. 이동 보조 장치의 움직임이 그대로 게임 속 캐릭터의 행동으 로 이어지면서, 접근성 기술과 인터랙티브 콘텐츠의 결합을 체감할 수 있다.\n" +
                        "물리적 조작과 화면 속 결과가 실시간으로 연결되는 구조를 통해, 장애 보조 기술이 단순 편의성을 넘어 새로운 경험 방식을 만들어갈 수 있음을 보여준다. 기술과 포용성을 함께 생각해볼 수 있는 콘텐츠이다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "생명의 코드, DNA 사이언스랩",
                category = "바이오헬스",
                imageResId = R.drawable.img14,
                detailImageResId = R.drawable.img14_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 60,
                description = "DNA 추출 실험과 화학 반응 실험(코끼리 치약)을 통해 유전 정보와 생명 현상을 직접 관찰하는 교육형 프로그램이다. 눈에 보이지 않던 DNA가 실제 시료에서 분리되어 나타나는 과정을 지켜보며, 유전 정보가 어떤 형태로 존재하는지 확인할 수 있다.\n" +
                        "시약 혼합과 반응 과정을 따라가면서, 교과서 속 개념으로만 접하던 유전자·세포·반응 메커니즘을 구체적인 경험으로 연결할 수 있다. 초등학생 이상이 참여하기에 적합한 생명과학 입문 체험이다.",
                how = "사전모집",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "휴먼아나토미 아트랩 : 바디페인팅, XR해부학체험",
                category = "바이오헬스",
                imageResId = R.drawable.img13,
                detailImageResId = R.drawable.img13_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 15,
                description = "바디페인팅으로 인체를 예술적으로 표현해보고, 3D 인체 모델 테이블(아나토마지)을 통해 인체 구조를 살펴보는 체험이다. 피부 위에서는 색과 패턴으로, 스크린에서는 장기·근육·혈관 구조로 같은 몸을 전혀 다른 방식으로 바라볼 수 있다.\n" +
                        "예술과 의학 시각 자료를 함께 접하면서, 인체 구조에 대한 이해가 감각적인 경험과 결합될수 있음을 보여준다. 인체 해부학을 부담 없이 접해보고 싶은 관람객에게 적절한 프로그램이다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),


            ThemeItem(
                title = "기택이의 대모험",
                category = "빅데이터",
                imageResId = R.drawable.img5,
                detailImageResId = R.drawable.img5_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "오류가 포함된 화면과 데이터를 비교·분석하며 문제의 원인을 찾아가는 게임형 디버깅 체험이다. 잘못된 부분을 찾고, 왜 문제가 발생했는지 추리하면서 데이터 오류 탐색 과정을 자연스럽게 따라가게 된다.\n" +
                        "플레이 과정 자체가 ‘문제 확인 → 패턴 분석 → 오류 수정’이라는 디버깅 절차와 닮아 있다. 빅데이터 분석에서 왜 작은 오류 하나가 큰 결과 차이를 만드는지 체감할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "초•중•고 학생 및 일반 관람객",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "나는 누구일까?",
                category = "빅데이터",
                imageResId = R.drawable.img6,
                detailImageResId = R.drawable.img6_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "AI가 제시하는 다섯 개의 키워드를 바탕으로 정답을 추리하는 퍼즐형 체험이다. 랜덤처럼 보이는 단서들이 사실은 데이터 필터링과 요약 과정을 거쳐 뽑힌 특징 정보라는 점을 직접 확인할 수 있다.\n" +
                        "단서들을 조합해 인물을 맞히는 과정은, 데이터가 흩어진 정보에서 공통된 의미를 추출하고 ‘맥락’을 만드는 과정과 유사하다. 빅데이터가 사람과 사물을 어떻게 요약하고 대표 특징으로 정리하는지 쉽게 이해할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "초•중•고 학생 및 일반 관람객",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "3D펜으로 만드는 첨단소재 창의공작소",
                category = "첨단소재나노융합",
                imageResId = R.drawable.img52,
                detailImageResId = R.drawable.img52_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 20,
                description = "3D 펜으로 키링·오브제 등을 직접 만들며 입체 구조가 어떻게 형성되는지 체험하는 프로그램이다. 라인을 겹쳐 올리는 간단한 동작만으로 형태가 잡히는 모습을 보며 첨단소재가 가진 가벼움·유연성·변형성의 개념을 자연스럽게 이해할 수 있다.\n" +
                        "직접 제작하는 과정에서 구조 안정성, 재료 강도 같은 기본 개념이 손끝으로 느껴져 첨단소재 연구의 기초를 쉽게 배울 수 있는 입문형 콘텐츠다.",
                how = "현장접수",
                target = "누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "빅데이터 어워즈",
                category = "빅데이터",
                imageResId = R.drawable.img8,
                detailImageResId = R.drawable.img8_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "빅데이터 컨소시엄에 참여한 학생들이 수행한 데이터 분석 프로젝트를 전시하는 공간이다. 시각화 결과물, 예측 모델, 문제 해결 사례 등 다양한 형태의 결과물을 통해 데이터가 실제 문 제 해결에 어떻게 사용되는지 확인할 수 있다.\n" +
                        "사회 문제, 산업 현장, 생활 속 이슈를 데이터로 분석하고 해석한 작품들이 전시되어 있어, 빅데이터 기술이 이론을 넘어 실질적인 인사이트를 만드는 도구라는 점을 보여준다. 데이터 기반 프로젝트의 흐름과 성과를 한눈에 볼 수 있는 부스이다.",
                how = "현장접수",
                target = "초•중•고 학생 및 일반 관람객",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "반짝반짝 스튜디오 : 반짝반짝 작은LED",
                category = "차세대디스플레이",
                imageResId = R.drawable.img61,
                detailImageResId = R.drawable.img61_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "작은 LED 모듈을 활용해 나만의 라이트템을 만드는 제작형 체험이다. 간단한 부품 조립과 문자 코딩을 통해 LED의 점등 방식과 메시지를 직접 설정할 수 있어, 기초 전자 회로와 디지털 코딩의 원리를 부담 없이 경험할 수 있다.\n" +
                        "(센서 연결 → LED 모듈 조립 → 문자·패턴 입력 → 작동 확인)까지 짧은 과정으로 구성되어 있어 누구나 쉽게 참여할 수 있으며, 완성된 라이트 아이템은 기념품으로 가져갈 수 있다. 디스플레이 기술과 코딩의 기초를 손으로 만져보며 배울 수 있는 입문형 프로그램이다.",
                how = "현장접수",
                target = "누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "네모의방, 디스플레이의 꿈 : 마음을 비추는 창문",
                category = "차세대디스플레이",
                imageResId = R.drawable.img62,
                detailImageResId = R.drawable.img62_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "짧은 문장을 입력하면 AI가 그 감정과 생각을 이미지로 변환해 ‘창문 디스플레이’에 비춰주는 감성형 인터랙티브 체험이다. 관람객이 적은 문장이 하나의 장면으로 펼쳐지며, 디스플레이가 단순한 화면이 아니라 마음을 드러내는 창처럼 작동하는 경험을 제공한다.\n" +
                        "AI 생성 이미지가 방 안의 창문 공간을 채우는 순간, 관람자는 자신의 내면을 외부 풍경처럼 바라보게 된다. 기술과 예술이 결합해 감정과 상상이 디스플레이 위에서 시각화되는 과정을 직관적으로 체험할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "차세대디스플레이 FAB : 디스플레이 공정속으로",
                category = "차세대디스플레이",
                imageResId = R.drawable.img63,
                detailImageResId = R.drawable.img63_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "디스플레이가 만들어지는 6.5세대(6.5G) 공정 프로세스를 실제 크기와 장비 구조를 기반으로 체험하는 제조형 콘텐츠이다. 평소에는 볼 수 없는 증착기 내부 구조와 패널 제작 흐름을 가까이에서 살펴보며, 디스플레이 패널이 어떤 단계를 거쳐 완성되는지 직관적으로 이해할 수 있다.\n" +
                        "공정 라인을 따라 이동하며 기판 세정, 박막 증착, 패터닝 등 주요 공정이 어떤 장비와 기술로 이루어지는지 직접 확인할 수 있다. 실제 FAB 내부를 축소·재현해놓은 형태라 제조 기술의 복잡함과 정밀함이 그대로 전달되며, 차세대 디스플레이 산업이 어떤 기반 위에서 만들어지는지를 한눈에 볼 수 있는 프로그램이다.",
                how = "현장접수",
                target = "누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "티프레임 스냅샷 : 테이크어샷 포토슛 팝팝팝!",
                category = "차세대디스플레이",
                imageResId = R.drawable.img64,
                detailImageResId = R.drawable.img64_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "투명 OLED(T-OLED)를 액자 형태로 구현한 디스플레이 앞에서 특별한 인생샷을 찍는 포토 체험 프로그램이다. 원하는 이미지를 선택하면 투명 화면 위에 선명하게 나타나고, 그 화면 뒤에 서서 촬영하면 마치 공중에 이미지가 떠 있는 듯한 독특한 사진을 만들 수 있다. 투명성과 화면 표현이 동시에 가능한 차세대 디스플레이 기술을 가장 직관적으로 경험할 수 있는 콘텐츠다.\n" +
                        "사진을 찍는 짧은 순간 안에 T-OLED가 가진 구조와 표현 방식이 자연스럽게 드러난다. 전원이 꺼지면 투명한 유리처럼 보이지만, 켜지는 순간 이미지가 떠오르며 ‘빛으로 만든 창문’ 같은 화면을 보여주는 기술적 특징을 직접 체감할 수 있는 프로그램이다.",
                how = "현장접수",
                target = "누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "Drone Under Attack: 하늘 위의 드론 위협",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img55,
                detailImageResId = R.drawable.img55_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 15,
                description = "드론을 대상으로 한 사이버 공격 시나리오를 직접 확인하는 체험이다. 위치 조작, 신호 방해, 원격 제어 등 실제 발생 가능한 위협을 눈앞에서 보며 드론이 단순한 비행체가 아니라 ‘데이터 기반 시스템’임을 이해할 수 있다.\n" +
                        "공격이 일어났을 때 어떤 문제가 발생하고 어떤 보안 기술이 필요한지 자연스럽게 파악할 수 있어 항공·물류 분야에서 보안이 왜 중요한지 쉽게 배울 수 있는 프로그램이다.",
                how = "현장접수",
                target = "COSS 참여자 전체(고등학생 이상)",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "센서가 듣고, AI가 생각한다",
                category = "사물인터넷",
                imageResId = R.drawable.img65,
                detailImageResId = R.drawable.img65_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "음성·심장소리 등 생체 신호를 센서가 읽고, AI가 이를 분석해 로봇을 바로 움직이게 하는 IoT–AI 융합 체험이다. 짧은 시간 안에 (센서 → 데이터 해석 → 기기 동작)의 흐름을 한 번에 볼 수 있다.\n" +
                        "소리가 로봇 행동으로 곧바로 바뀌는 순간, 데이터 처리가 어떤 방식으로 이루어지고 AI 판단이 어떻게 기계 제어로 전달되는지 직관적으로 이해할 수 있다. 처음 IoT를 접하는 관람객도 쉽게 참여할 수 있는 입문형 프로그램이다.",
                how = "현장접수",
                target = "제한없음",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "꿈의 학교 속으로! 메타버스 캠퍼스 & 콘텐츠 여행 (실감미디어 메타버스 플랫폼 투어)",
                category = "실감미디어",
                imageResId = R.drawable.img17,
                detailImageResId = R.drawable.img17_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "실감미디어 기술로 구현된 메타버스 캠퍼스를 탐험하는 체험이다. 아바타로 이동하며 강의 공간, 체험 구역, 전시형 콘텐츠 등을 둘러보면서, 가상 공간이 어떻게 학습 환경으로 활용될 수 있는지 확인할 수 있다.\n" +
                        "단순히 화면을 보는 것이 아니라, 가상 공간 안을 직접 돌아다니며 정보를 만나는 방식이라 몰입감이 높다. 기존 교실과 다른 형태의 학습 환경이 어떤 구조와 연출로 구성되는지 살펴보기에 좋은 프로그램이다.",
                how = "현장접수",
                target = "초등학생(고학년) 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "스마트양식 디지털 트윈 체험: 모바일 로봇과 수질센서를 활용한 자율 환경 관리",
                category = "사물인터넷",
                imageResId = R.drawable.img66,
                detailImageResId = R.drawable.img66_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 8,
                description = "사용자가 음성으로 감정을 말하면 AI가 감정을 분석하고 조명이 해당 색으로 즉시 바뀌는 감성형 IoT 체험이다. (음성 인식 → 감정 분석 → 조명 제어) 과정이 빠르게 이어져 기술적 반응이 마치 감정 표현처럼 느껴진다.\n" +
                        "짧은 시간에도 AI와 IoT가 결합된 인터랙션 구조를 명확하게 이해할 수 있어, 가볍지만 인상적인 경험을 제공한다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "디지털 숲 명상 오디세이",
                category = "실감미디어",
                imageResId = R.drawable.img18,
                detailImageResId = R.drawable.img18_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 30,
                description = "VR 장비를 착용하고 디지털로 구현된 숲 속 환경에서 명상을 체험하는 프로그램이다. 빛과 기상 변화, 주변 환경 사운드가 함께 연출되어 실제 자연에 있는 것과 비슷한 안정감을 제공한다.\n" +
                        "가상 환경이지만, 시각·청각 자극을 통해 몸과 마음이 점차 이완되는 과정을 경험할 수 있다. 실감미디어 기술이 휴식·힐링 콘텐츠로도 활용될 수 있음을 보여주는 사례이다.",
                how = "사전모집",
                target = "초등학생(고학년) 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "톡톡 튀는 콘텐츠! 실감 팡팡 체험존 (실감미디어 경진대회 우수작품 체험)",
                category = "실감미디어",
                imageResId = R.drawable.img19,
                detailImageResId = R.drawable.img19_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "실감미디어 경진대회에서 선정된 우수 학생 작품들을 직접 체험하는 공간이다. AR, VR, 인터랙티브 콘텐츠 등 다양한 형식의 실감형 콘텐츠가 전시되어 있으며, 관람자가 직접 조작하고 반응을 확인할 수 있다.\n" +
                        "작품마다 사용된 기술과 상호작용 방식이 달라, 여러 콘텐츠를 경험할수록 실감미디어가 표현할 수 있는 범위를 넓게 볼 수 있다. 학생들의 아이디어가 실제 동작하는 콘텐츠로 구현된 모습을 가까이에서 확인할 수 있는 부스이다.",
                how = "현장접수",
                target = "초등학생(고학년) 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "Space Green 우주 농부 인증 미션을 위한 탑승을 시작합니다!",
                category = "그린바이오",
                imageResId = R.drawable.img51,
                detailImageResId = R.drawable.img51_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 25,
                description = "우주 수직농장–스마트 온실–노지 수확으로 이어지는 미래 농업 과정을 단계별로 체험하는 프로그램이다. 자동 제어 시스템과 센서 기술이 식물 재배 환경을 어떻게 관리하는지 직접 확인하며 애그테크(Ag-Tech)의 원리를 자연스럽게 이해할 수 있다.\n" +
                        "우주 환경을 모사한 농장 모델부터 실제 수확 체험까지 이어지면서, 기술 기반 농업이 식량 생산과 어떤 방식으로 연결되는지를 한 흐름으로 경험할 수 있는 그린바이오 입문 콘텐츠다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "인공위성 통신 체험관",
                category = "차세대통신",
                imageResId = R.drawable.img47,
                detailImageResId = R.drawable.img47_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 15,
                description = "인공위성이 (발사 → 궤도 진입 → 신호 송수신)을 거쳐 지구와 어떻게 통신하는지 전 과정을 살펴보는 체험이다. LEO·MEO·GEO 같은 궤도 차이부터 신호가 우주 공간을 이동하며 겪는 지연과 감쇠까지, 교과서에서는 이해하기 어려웠던 개념들이 시각 자료로 명확하게 정리된다.\n" +
                        "위성이 지상국과 데이터를 주고받는 구조를 직접 확인하면서 우리가 사용하는 내비게이션·기상정보·방송 신호 등이 어떤 통신 기술로 유지되고 있는지 자연스럽게 이해할 수 있다. 우주에서 지구로, 지구에서 우주로 이어지는 차세대 통신의 흐름을 한눈에 볼 수 있는 프로그램이다.",
                how = "사전모집 & 현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "블록코딩을 활용한 드론제어",
                category = "항공드론",
                imageResId = R.drawable.img41,
                detailImageResId = R.drawable.img41_1,
                periodstart = "10:00",
                periodend = "17:30",
                time = 90,
                description = "블록처럼 생긴 명령어를 조합해 드론의 비행을 직접 설계하는 교육형 체험이다. 코드를 어떻게 쌓느냐에 따라 드론이 이동하고 회전하며 미션을 수행하기 때문에, 프로그래밍 구조와 기기 제어가 어떤 방식으로 이어지는지 한눈에 이해할 수 있다.\n" +
                        "(명령 설정 → 경로 설계 → 실행 → 결과 확인)의 흐름이 자연스럽게 연결되어 있어 어린이도 쉽게 참여할 수 있고, 드론 제어의 기초를 직관적으로 익히기 좋다. ‘내가 짠 코드대로’ 드론이 움직이는 순간이 가장 재미있는 순간이다.",
                how = "사전모집 & 현장접수",
                target = "초등학생(고학년)이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "UAM 조종 시뮬레이터 체험",
                category = "항공드론",
                imageResId = R.drawable.img42,
                detailImageResId = R.drawable.img42_1,
                periodstart = "10:00",
                periodend = "17:30",
                time = 30,
                description = "미래 도심항공교통(UAM)을 실제 조종하듯 체험하는 비행 시뮬레이터다. 상승·하강·회전·이동 등 조작감을 그대로 구현해 플라잉 택시가 어떤 원리로 움직이고, 도심을 어떤 방식으로 비행하는지 생생하게 느낄 수 있다.\n" +
                        "가상 도시를 직접 비행하다 보면 UAM이 미래 교통을 어떻게 바꿀지, 안전한 운항을 위해 어떤 기술이 필요한지 자연스럽게 이해된다. 빠르게 다가오는 도심항공 시대를 가장 현실감 있게 체험할 수 있는 프로그램이다.",
                how = "사전모집 & 현장접수",
                target = "초등학생(고학년)이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "드론 장애물 경주 체험",
                category = "항공드론",
                imageResId = R.drawable.img43,
                detailImageResId = R.drawable.img43_1,
                periodstart = "10:00",
                periodend = "17:30",
                time = 30,
                description = "직접 드론을 조종해 장애물 코스를 통과하며 점수를 겨루는 액션형 체험이다. 속도 조절, 방향 전환, 자세 제어가 모두 필요하기 때문에 단순 비행보다 훨씬 다이내믹한 조종 경험을 제공한다.\n" +
                        "비행 중 작은 흔들림 하나도 결과에 크게 영향을 주기 때문에 집중력과 손 조작 감각이 자연스럽게 향상된다. 초등학생부터 성인까지 모두 몰입하는 드론 입문형 인기 콘텐츠다.",
                how = "현장접수",
                target = "초등학생(고학년)이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "공감 조명: 오늘 너의 색은?",
                category = "사물인터넷",
                imageResId = R.drawable.img67,
                detailImageResId = R.drawable.img67_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 5,
                description = "사용자가 음성으로 감정을 말하면 AI가 감정을 분석하고 조명이 해당 색으로 즉시 바뀌는 감성형 IoT 체험이다. (음성 인식 → 감정 분석 → 조명 제어) 과정이 빠르게 이어져 기술적 반응이 마치 감정 표현처럼 느껴진다.\n" +
                        "짧은 시간에도 AI와 IoT가 결합된 인터랙션 구조를 명확하게 이해할 수 있어, 가볍지만 인상적인 경험을 제공한다.",
                how = "현장접수",
                target = "제한없음",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AWS DeepRacer 자율주행 체험",
                category = "미래자동차",
                imageResId = R.drawable.img12,
                detailImageResId = R.drawable.img12_1,
                periodstart = "10:00",
                periodend = "17:30",
                time = 5,
                description = "강화학습 기반 AI가 주행 전략을 학습한 소형 자율주행 차량을 활용해 레이싱을 체험하는 프로그램이다. 참가자는 AI 모드와 사람이 직접 조종하는 모드를 비교하며 주행 결과의 차이를 확인할 수 있다.\n" +
                        "차량이 코스를 돌며 속도 조절, 코너 처리, 충돌 회피를 수행하는 과정이 그대로 드러나 자율주행 알고리즘의 기본 원리를 쉽게 이해할 수 있다. 키 100cm 이상이면 참여할 수 있어, 어린이부터 어른까지 모두 즐길 수 있는 미래 모빌리티 체험이다.",
                how = "사전모집 & 현장접수",
                target = "키 100cm 이상이면 누구나 가능~!",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "손끝에서 펼쳐지는 스트레쳐블 첨단유연소재",
                category = "첨단소재나노융합",
                imageResId = R.drawable.img53,
                detailImageResId = R.drawable.img53_1,
                periodstart = "10:00",
                periodend = "17:30",
                time = 60,
                description = "늘어나고 휘어지고 다시 원래대로 돌아오는 스트레쳐블 소재를 직접 만져보는 체험이다. 고분자 네트워크가 외부 힘을 흡수하고 복원되는 과정을 손으로 확인하며 유연소재의 핵심 원리를 이해할 수 있다.\n" +
                        "웨어러블 기기나 바이오센서 등 실제 산업에서 이 소재가 어떻게 활용되는지도 안내되어, 미래 유연 전자 기술의 가능성을 간단하게 조망할 수 있는 프로그램이다.",
                how = "사전모집",
                target = "초등학생이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "탈출하라, 사이버 보안 위기속 탈출기",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img56,
                detailImageResId = R.drawable.img56_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 30,
                description = "방탈출 형식으로 구성된 사이버 보안 체험이다. 문제 원인을 찾고, 단서를 연결하고, 취약점을 해결하는 과정이 실제 보안 사고 대응 절차와 유사하다.\n" +
                        "참가자는 추리·관찰·분석을 통해 공격의 흐름을 파악하며 사이버 위기 대응의 기본 원리를 게임처럼 익힐 수 있는 프로그램이다.",
                how = "현장접수",
                target = "COSS 참여자 전체(중학생 이상)",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "어서와, 반도체 회로제작은 처음이지?",
                category = "차세대반도체",
                imageResId = R.drawable.img9,
                detailImageResId = R.drawable.img9_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 30,
                description = "게임기용 반도체 기초 회로를 직접 설계·조립·확인해보며 반도체 회로 구조를 이해하는 체험 프로그램이다. 이론으로만 보던 회로도가 실제 부품과 배선으로 구현되는 과정을 따라가며, 신호가 어떻게 흐르고 동작하는지 살펴볼 수 있다.\n" +
                        "작은 부품 하나의 연결 상태가 전체 회로 동작에 어떤 영향을 주는지 직접 경험하면서, 반도체 기술이 왜 높은 정밀도를 요구하는지 알 수 있다. 반도체 기초 구조를 손으로 익혀보고 싶은 참가자에게 적합한 체험이다.",
                how = "현장접수",
                target = "초등학생 4학년 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "스마트홈 캠 해킹, 누군가 지켜보고 있다.",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img57,
                detailImageResId = R.drawable.img57_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "스마트홈 카메라가 해킹되었을 때 나타나는 실제 상황을 짧은 데모로 확인하는 체험이다. 권한 탈취, 원격 조작, 사생활 노출 등이 어떻게 발생하는지 직접 확인하며 일상 속 IoT 기기 보안의 중요성을 배울 수 있다.\n" +
                        "작은 취약점 하나가 큰 위험으로 이어지는 구조를 보여주는 대표적인 생활형 보안 콘텐츠다.",
                how = "현장접수",
                target = "초등학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "해킹이 부른 의료 사고",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img58,
                detailImageResId = R.drawable.img58_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "의료기기가 해킹될 경우 발생할 수 있는 위험을 시연으로 확인하는 프로그램이다. 심박조절기, 인슐린 펌프 등 생명과 직결되는 기기에 공격이 가해졌을 때 어떤 문제가 일어나는지 실제 사례 기반으로 살펴본다.\n" +
                        "보안이 단순한 정보 문제가 아니라 안전과 직결된 기술이라는 점을 가장 직관적으로 이해할 수 있다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "우리의 친절한 로봇의 반란",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img59,
                detailImageResId = R.drawable.img59_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 15,
                description = "서비스 로봇이 해킹되었을 때 생길 수 있는 오작동·정지·경로 변경 등을 직접 시연으로 보여주는 체험이다. 로봇 제어 시스템이 어떤 취약점을 갖고 있는지, 데이터 조작이 어떤 결과를 만드는지 쉽게 확인할 수 있다.\n" +
                        "로봇이 일상에 가까워질수록 보안이 필수 요소가 된다는 점을 명확하게 이해하게 되는 프로그램이다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "AI가 만든 가짜를 잡아라",
                category = "데이터보안활용융합",
                imageResId = R.drawable.img60,
                detailImageResId = R.drawable.img60_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 10,
                description = "AI가 만든 이미지·음성·문장을 사람의 진짜와 비교해 구별해보는 딥페이크 탐지 체험이다. 참가자는 단서를 찾고 패턴을 분석하며 생성형 AI가 남기는 특징을 직접 확인할 수 있다.\n" +
                        "디지털 시대에 필요한 정보 판별력과 탐지 감각을 자연스럽게 익힐 수 있는 실전형 콘텐츠다.",
                how = "현장접수",
                target = "중학생 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "반도체를 분해해보자",
                category = "반도체소부장",
                imageResId = R.drawable.img44,
                detailImageResId = R.drawable.img44_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 30,
                description = "평소에는 볼 수 없던 반도체 장비의 내부 구조를 VR로 직접 분해하고 다시 조립해보는 체험 프로그램이다. 장비 안쪽의 부품들이 어떤 순서로 연결되어 있고, 공정 단계가 어떻게 이어지는지 눈앞에서 확인할 수 있어 복잡한 반도체 제조 기술을 쉽게 이해할 수 있다.\n" +
                        "각 부품의 움직임과 역할을 하나씩 따라가다 보면 반도체 장비가 왜 정밀한 설계와 높은 안정성을 요구하는지 자연스럽게 느껴진다. 현실에서는 접근하기 어려운 장비 내부를 가상 공간에서 자유롭게 탐색하며, 반도체 공정 전체의 흐름을 기술자의 관점에서 체험할 수 있는 프로그램이다.",
                how = "사전모집 & 현장접수",
                target = "누구나 참여 가능",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "방탈출 프로그램 '교수님의 수상한 과제'",
                category = "에코업",
                imageResId = R.drawable.img48,
                detailImageResId = R.drawable.img48_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 20,
                description = "탄소중립·자원순환·친환경 기술 등 에코업 6대 과제와 관련된 단서를 따라가며 문제를 해결하는 방탈출형 체험이다. 단서를 조합해 위협 요소를 제거하는 과정이 실제 환경 문제 해결 과정과 닮아 있어, 게임을 즐기면서도 핵심 개념을 자연스럽게 익힐 수 있다.\n" +
                        "관람자는 추리와 관찰을 통해 방 안에 숨겨진 환경 메시지를 찾아가며, 탄소중립이 왜 필요한지 스스로 깨닫게 된다. 몰입감 있는 스토리와 학습 요소가 결합된 인기 콘텐츠다.",
                how = "현장접수",
                target = "초, 중, 고 누구나",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "LED로 그리는 탄소중립",
                category = "에코업",
                imageResId = R.drawable.img49,
                detailImageResId = R.drawable.img49_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 20,
                description = "아크릴 판에 그림을 새기고 LED 조명을 결합해 나만의 조명 작품을 만드는 업사이클링 제작 체험이다. 조명이 켜지는 순간, 빛의 흐름과 에너지 사용량에 대한 감각적 이해가 자연스럽게 생긴다.\n" +
                        "‘적은 에너지로 더 큰 효과를 만드는’ 탄소중립 기술의 개념을 손으로 만들며 배우게 된다. 완성된 조명은 작은 친환경 메시지를 담은 개인 작품으로 가져갈 수 있다.",
                how = "사전모집 & 현장접수",
                target = "초등학생(고학년) 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "우리집 알뜰에너지 연구소",
                category = "에코업",
                imageResId = R.drawable.img50,
                detailImageResId = R.drawable.img50_1,
                periodstart = "10:00",
                periodend = "18:00",
                time = 10,
                description = "집에서 흔히 사용하는 가전제품의 대기전력을 직접 측정해보는 생활형 에너지 체험이다. 겉으로는 보이지 않지만 꾸준히 소비되는 전력이 얼마나 큰 낭비를 만드는지 수치로 확인할 수 있다.\n" +
                        "(측정 → 비교 → 절약 방법 찾기) 과정을 따라가다 보면, 생활 속 에너지 절약이 왜 중요한지 스스로 깨닫게 된다. 어린이부터 성인까지 누구나 쉽게 참여할 수 있는 실용적인 에너지 교육 콘텐츠다.",
                how = "사전모집 & 현장접수",
                target = "누구나 참여 가능",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            ),
            ThemeItem(
                title = "첨단소재워터랩:모링가의 비밀",
                category = "첨단소재나노융합",
                imageResId = R.drawable.img54,
                detailImageResId = R.drawable.img54_1,
                periodstart = "10:00",
                periodend = "17:00",
                time = 40,
                description = "모링가 씨앗을 이용해 오염된 물을 정화하는 과정을 실험하며 친환경 나노소재의 개념을 배우는 프로그램이다. 씨앗 속 성분이 오염 물질을 응집·침전시키는 모습을 직접 관찰해 ‘소재 기술로 환경 문제를 해결한다’는 개념을 쉽게 이해할 수 있다.\n" +
                        "생물 기반 소재가 산업 정화 기술과 어떻게 연결되는지를 체험 속에서 확인할 수 있어, 환경·소재 융합 기술의 기초를 배우기 좋은 콘텐츠다.",
                how = "사전모집",
                target = "초등학생(고학년) 이상",
                periodRangestart = "2025-11-26",
                periodRangeend = "2025-11-29"
            )
        )
    }

    private lateinit var gridButtons: List<MaterialButton>
    private lateinit var txtPage: TextView
    private lateinit var btnDownload: MaterialButton
    private lateinit var btnViewSem: MaterialButton
    private var filteredThemeItems: List<ThemeItem> = emptyList()
    private val selectedCategories = mutableSetOf<String>()
    private val pageSize = 6        // 한 페이지당 최대 6개
    private var currentPage = 0     // 현재 페이지 (0 기반 인덱스)
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

        // 1) 처음 진입할 때 테마 화면을 먼저 띄움
        setContentView(R.layout.activity_information_by_theme)

        // 2) 상단 공통 탑바 세팅 (include 된 topBar가 있다면)
        val topBar = findViewById<View>(R.id.topBar)
        topBar?.let {
            setupCommonTopBar(
                topBarView = it,
                backAction = { finish() },
                homeAction = { navigateToHome() }
            )
        }
        // 필요하다면 exam2에도 아래 함수 만들어서 뒤로 버튼 숨기기
        setupTopBar()

        // 3) Temi Navigation Helper 초기화
        //    - showMapLayout: 이동 끝나고 다시 "테마별 안내" 화면으로 돌아올 때 호출
        //    - showFaceLayout: 이동 중 "얼굴 화면" 보여줄 때 호출
        temiNavigator = TemiNavigationHelper(
            activity = this,
            showMapLayout = {
                setContentView(R.layout.activity_information_by_theme)

                val topBarInner = findViewById<View>(R.id.topBar)
                topBarInner?.let { bar ->
                    setupCommonTopBar(
                        topBarView = bar,
                        backAction = { finish() },
                        homeAction = { navigateToHome() }
                    )
                }
                setupTopBar()
                bindAndInitViewsAfterSetContent()
            },
            showFaceLayout = {
                setContentView(R.layout.activity_face)
            },
            dbUrl = "https://exam-afefa-default-rtdb.firebaseio.com",
            directionsNode = "Directions"
        )

        // 4) 처음 진입했을 때도 동일한 초기화 수행
        bindAndInitViewsAfterSetContent()
    }
    private fun bindAndInitViewsAfterSetContent() {
        // 하단바 뷰들
        txtPage = findViewById(R.id.txtPage)
        btnDownload = findViewById(R.id.btnDownload)
        btnViewSem = findViewById(R.id.btnViewSem)

        // 오른쪽 메뉴 - 오늘의 행사
        val tileEvent =
            findViewById<com.google.android.material.card.MaterialCardView>(R.id.tileEvent)
        tileEvent?.setOnClickListener {
            startActivity(Intent(this, TodaysEventActivity::class.java))
        }

        // 오른쪽 메뉴 - 행사장 지도
        val tileMap =
            findViewById<com.google.android.material.card.MaterialCardView>(R.id.tileMap)
        tileMap?.setOnClickListener {
            startActivity(Intent(this, MapCoShowActivity::class.java))
        }

        // 시계
        txtClock = findViewById(R.id.txtClock)
        startClock()

        // GridLayout 안의 6개 버튼
        gridButtons = listOf(
            findViewById(R.id.btngrid1),
            findViewById(R.id.btngrid2),
            findViewById(R.id.btngrid3),
            findViewById(R.id.btngrid4),
            findViewById(R.id.btngrid5),
            findViewById(R.id.btngrid6)
        )

        // 첫 페이지 or 현재 필터된 페이지 표시
        if (filteredThemeItems.isEmpty()) {
            filteredThemeItems = allThemeItems
            showPage(0)
        } else {
            showPage(currentPage)
        }

        // CoshowtiResultDetailActivity에서 넘어온 경우 → 해당 부스 팝업 띄우기
        val fromResult = intent.getBooleanExtra(EXTRA_FROM_RESULT, false)
        if (fromResult) {
            val boothTitle = intent.getStringExtra(EXTRA_BOOTH_TITLE)
            val boothCategory = intent.getStringExtra(EXTRA_BOOTH_CATEGORY)

            val targetItem = allThemeItems.firstOrNull { it.title == boothTitle && it.category == boothCategory }
                ?: allThemeItems.firstOrNull { it.category == boothCategory }
                ?: allThemeItems.firstOrNull { it.title == boothTitle }

            if (targetItem != null) {
                showThemeDetailDialog(targetItem)
            }
        }

        // 페이지 텍스트 좌/우 터치로 페이지 넘기기
        txtPage.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val totalPages = calcTotalPages(filteredThemeItems.size, pageSize)
                if (totalPages <= 1) return@setOnTouchListener true

                val x = event.x
                val half = v.width / 2f

                if (x < half) {
                    // 왼쪽 → 이전 페이지 (맨 앞이면 마지막으로)
                    val prevPage =
                        if (currentPage - 1 < 0) totalPages - 1 else currentPage - 1
                    showPage(prevPage)
                } else {
                    // 오른쪽 → 다음 페이지 (마지막이면 처음으로)
                    val nextPage = (currentPage + 1) % totalPages
                    showPage(nextPage)
                }
            }
            true
        }

        btnDownload.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_pamphlet_qr, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnClose = dialogView.findViewById<ImageButton>(R.id.btnClose)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        // "테마 필터 적용하기"
        btnViewSem.setOnClickListener {
            showThemeFilterDialog()
        }
    }
    override fun onStart() {
        super.onStart()
        temiNavigator.onStart()
    }

    override fun onStop() {
        temiNavigator.onStop()
        super.onStop()
    }

    private fun startClock() {
        clockHandler.post(clockRunnable)
    }
    private fun showThemeFilterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_theme_filter, null)

        // 체크박스들 찾기
        val cbOnlyCurrentRunning = dialogView.findViewById<CheckBox>(R.id.cbOnlyCurrentRunning)
        val cbFutureCar = dialogView.findViewById<CheckBox>(R.id.cbFutureCar)
        val cbIntelligentRobot = dialogView.findViewById<CheckBox>(R.id.cbIntelligentRobot)
        val cbAeroDrone = dialogView.findViewById<CheckBox>(R.id.cbAeroDrone)

        val cbBioHealth = dialogView.findViewById<CheckBox>(R.id.cbBioHealth)
        val cbGreenBio = dialogView.findViewById<CheckBox>(R.id.cbGreenBio)
        val cbEnergyNewIndustry = dialogView.findViewById<CheckBox>(R.id.cbEnergyNewIndustry)
        val cbEcoUp = dialogView.findViewById<CheckBox>(R.id.cbEcoUp)

        val cbNextGenSemi = dialogView.findViewById<CheckBox>(R.id.cbNextGenSemi)
        val cbSemiParts = dialogView.findViewById<CheckBox>(R.id.cbSemiParts)
        val cbSecondaryBattery = dialogView.findViewById<CheckBox>(R.id.cbSecondaryBattery)
        val cbAdvancedMaterial = dialogView.findViewById<CheckBox>(R.id.cbAdvancedMaterial)
        val cbNextGenDisplay = dialogView.findViewById<CheckBox>(R.id.cbNextGenDisplay)

        val cbAI = dialogView.findViewById<CheckBox>(R.id.cbAI)
        val cbBigData = dialogView.findViewById<CheckBox>(R.id.cbBigData)
        val cbImmersiveMedia = dialogView.findViewById<CheckBox>(R.id.cbImmersiveMedia)
        val cbNextGenComm = dialogView.findViewById<CheckBox>(R.id.cbNextGenComm)
        val cbDataSecurityFusion = dialogView.findViewById<CheckBox>(R.id.cbDataSecurityFusion)
        val cbIoT = dialogView.findViewById<CheckBox>(R.id.cbIoT)

        val btnApply = dialogView.findViewById<MaterialButton>(R.id.btnFilterApply)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnFilterCancel)

        // 이미 선택돼 있던 카테고리들을 체크 상태로 복원
        fun initCheckBox(cb: CheckBox, category: String) {
            cb.isChecked = selectedCategories.contains(category)
        }
        cbOnlyCurrentRunning.isChecked = onlyCurrentRunning
        initCheckBox(cbFutureCar, "미래자동차")
        initCheckBox(cbIntelligentRobot, "지능형로봇")
        initCheckBox(cbAeroDrone, "항공드론")

        initCheckBox(cbBioHealth, "바이오헬스")
        initCheckBox(cbGreenBio, "그린바이오")
        initCheckBox(cbEnergyNewIndustry, "에너지신산업")
        initCheckBox(cbEcoUp, "에코업")

        initCheckBox(cbNextGenSemi, "차세대반도체")
        initCheckBox(cbSemiParts, "반도체소부장")
        initCheckBox(cbSecondaryBattery, "이차전지")
        initCheckBox(cbAdvancedMaterial, "첨단소재나노융합")
        initCheckBox(cbNextGenDisplay, "차세대디스플레이")

        initCheckBox(cbAI, "인공지능")
        initCheckBox(cbBigData, "빅데이터")
        initCheckBox(cbImmersiveMedia, "실감미디어")
        initCheckBox(cbNextGenComm, "차세대통신")
        initCheckBox(cbDataSecurityFusion, "데이터보안활용융합")
        initCheckBox(cbIoT, "사물인터넷")

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnApply.setOnClickListener {
            val newSelected = mutableSetOf<String>()

            fun collect(cb: CheckBox, category: String) {
                if (cb.isChecked) newSelected.add(category)
            }

            // ─ 기존 카테고리 수집 부분 그대로 ─
            collect(cbFutureCar, "미래자동차")
            collect(cbIntelligentRobot, "지능형로봇")
            collect(cbAeroDrone, "항공드론")

            collect(cbBioHealth, "바이오헬스")
            collect(cbGreenBio, "그린바이오")
            collect(cbEnergyNewIndustry, "에너지신산업")
            collect(cbEcoUp, "에코업")

            collect(cbNextGenSemi, "차세대반도체")
            collect(cbSemiParts, "반도체소부장")
            collect(cbSecondaryBattery, "이차전지")
            collect(cbAdvancedMaterial, "첨단소재나노융합")
            collect(cbNextGenDisplay, "차세대디스플레이")

            collect(cbAI, "인공지능")
            collect(cbBigData, "빅데이터")
            collect(cbImmersiveMedia, "실감미디어")
            collect(cbNextGenComm, "차세대통신")
            collect(cbDataSecurityFusion, "데이터보안활용융합")
            collect(cbIoT, "사물인터넷")

            // 🔹 새 필터 상태 저장
            onlyCurrentRunning = cbOnlyCurrentRunning.isChecked

            // 선택 상태 업데이트
            selectedCategories.clear()
            selectedCategories.addAll(newSelected)

            // 🔹 카테고리 + "현재 진행중" 조건을 함께 적용
            filteredThemeItems = allThemeItems.filter { item ->
                val categoryOk =
                    selectedCategories.isEmpty() || item.category in selectedCategories

                val timeOk =
                    !onlyCurrentRunning || item.isCurrentlyRunningNow()

                categoryOk && timeOk
            }

            // 항상 첫 페이지부터 다시 표시
            showPage(0)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        // 화면 전체를 살짝 덮고 가운데에 카드가 보이도록
        dialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setGravity(Gravity.CENTER)
        }
    }


    private fun calcTotalPages(itemCount: Int, pageSize: Int): Int {
        if (itemCount <= 0) return 0
        return (itemCount - 1) / pageSize + 1
    }

    private fun showPage(page: Int) {
        val totalItems = filteredThemeItems.size
        val totalPages = calcTotalPages(totalItems, pageSize)

        if (totalPages == 0) {
            gridButtons.forEach { it.visibility = View.INVISIBLE }
            txtPage.text = "<    0 / 0    >"
            return
        }

        val safePage = page.coerceIn(0, totalPages - 1)
        currentPage = safePage

        val startIndex = safePage * pageSize
        val endIndex = minOf(startIndex + pageSize, totalItems)
        val pageItems = filteredThemeItems.subList(startIndex, endIndex)

        gridButtons.forEachIndexed { index, button ->
            if (index < pageItems.size) {
                val item = pageItems[index]
                button.visibility = View.VISIBLE

                // ↓↓↓ 기존 이미지/텍스트 설정 코드 그대로 ↓↓↓
                val rawDrawable = AppCompatResources.getDrawable(this, item.imageResId)

                val targetWidthDp = 280
                val targetHeightDp = 200

                val targetWidthPx = dpToPx(targetWidthDp)
                val targetHeightPx = dpToPx(targetHeightDp)

                val topDrawable = when (rawDrawable) {
                    is BitmapDrawable -> {
                        val sourceBitmap = rawDrawable.bitmap
                        val scaledBitmap = Bitmap.createScaledBitmap(
                            sourceBitmap,
                            targetWidthPx,
                            targetHeightPx,
                            true
                        )

                        RoundedBitmapDrawableFactory.create(resources, scaledBitmap).apply {
                            cornerRadius = dpToPx(16).toFloat()
                        }
                    }
                    else -> rawDrawable
                }

                topDrawable?.setBounds(0, 0, targetWidthPx, targetHeightPx)
                button.setCompoundDrawables(null, topDrawable, null, null)
                button.compoundDrawablePadding = dpToPx(8)

                button.text = item.title

                button.setOnClickListener {
                    showThemeDetailDialog(item)
                }
            } else {
                button.visibility = View.INVISIBLE
            }
        }

        txtPage.text = "< ${currentPage + 1} / $totalPages >"
    }


    private fun showThemeDetailDialog(item: ThemeItem) {
        // 1. 레이아웃 inflate
        val dialogView = layoutInflater.inflate(R.layout.dialog_theme_detail, null)

        // 2. 뷰 찾기
        val imgTheme = dialogView.findViewById<ImageView>(R.id.imgTheme)
        val btnClose = dialogView.findViewById<ImageButton>(R.id.btnClose)
        val txtTitle = dialogView.findViewById<TextView>(R.id.txtTitle)
        val txtCategory = dialogView.findViewById<TextView>(R.id.txtCategory)
        val txtPeriod = dialogView.findViewById<TextView>(R.id.txtPeriod)
        val txtTime = dialogView.findViewById<TextView>(R.id.txtTime)
        val txtTarget = dialogView.findViewById<TextView>(R.id.txtTarget)
        val txtHow = dialogView.findViewById<TextView>(R.id.txtHow)
        val txtDescription = dialogView.findViewById<TextView>(R.id.txtDescription)

        val btnStartNavigation =
            dialogView.findViewById<com.google.android.material.button.MaterialButton>(
                R.id.btnStartNavigation
            )
        // 3. 데이터 바인딩
        imgTheme.setImageResource(item.detailImageResId)
        txtTitle.text = item.title
        txtCategory.text = item.category

        txtPeriod.text = "체험 기간: ${item.periodRangestart} ~ ${item.periodRangeend}"
        txtTime.text = "체험 시간: ${item.periodstart} ~ ${item.periodend}\n소요 시간: ${item.time}분"
        txtTarget.text = "참여 대상: ${item.target}"

        if (item.how.isBlank()) {
            txtHow.visibility = View.GONE
        } else {
            txtHow.visibility = View.VISIBLE
            txtHow.text = "체험 방법: ${item.how}"
        }

// 🔹 description 왼쪽에 '한줄소개:' 붙이기
        txtDescription.text = "한줄소개: ${item.description}"

        txtDescription.text = item.description

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        btnStartNavigation?.setOnClickListener {
            val temiLocationName = item.category
            val locationKey = item.category

            val boothTitleForSpeech = when (item.title) {
                "AI를 활용한 캐리커쳐 체험" -> "AI 캐리커처 체험"   // 말할 때만 이름 살짝 줄이기
                else -> item.title
            }

            val guideMessage = buildTemiGuideMessage(item)

            temiNavigator.startNavigation(
                locationKey = locationKey,
                temiLocationName = temiLocationName,
                boothTitle = boothTitleForSpeech,
                guideMessage = guideMessage
            )

            dialog.dismiss()
        }

        dialog.show()


        dialog.window?.let { window ->
            val params = window.attributes
            params.gravity = android.view.Gravity.CENTER
            params.y = dpToPx(20)
            window.attributes = params
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }
    override fun onDestroy() {
        super.onDestroy()
        clockHandler.removeCallbacks(clockRunnable)
    }
    fun ThemeItem.isCurrentlyRunningNow(): Boolean {
        return try {
            val tz = TimeZone.getTimeZone("Asia/Seoul")

            // 현재 시각을 "날짜 문자열" / "시간 문자열" 로 나눠서 구함
            val now = Calendar.getInstance(tz).time

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).apply {
                timeZone = tz
            }
            val timeFormat = SimpleDateFormat("HH:mm", Locale.KOREA).apply {
                timeZone = tz
            }

            val todayStr = dateFormat.format(now)      // 예: "2025-11-27"
            val nowTimeStr = timeFormat.format(now)    // 예: "14:23"

            // "yyyy-MM-dd", "HH:mm" 형식은 문자열 비교(>=, <=)가 시간 순서와 같음
            val inDateRange =
                todayStr >= periodRangestart && todayStr <= periodRangeend

            val inTimeRange =
                nowTimeStr >= periodstart && nowTimeStr <= periodend

            inDateRange && inTimeRange
        } catch (e: Exception) {
            false
        }
    }
    /**
     * Temi가 길찾기 중에 말할 안내 멘트
     * - 특정 부스는 별도 스크립트
     * - 나머지는 ThemeItem 정보 기반 기본 스크립트
     */
    private fun buildTemiGuideMessage(item: ThemeItem): String {
        return when (item.title) {
            "AICOSS 메타버스캠퍼스 체험" -> """
        지금 이동 중인 부스는 AICOSS 메타버스캠퍼스 체험입니다.

        이 프로그램은 AICOSS에서 제작한 메타버스 캠퍼스 공간을 직접 탐색해보는 프로그램으로, 가상 강의동과 공원, 전시 공간 등 다양한 장소를 자유롭게 돌아다니며 AI 기반 인터랙티브 콘텐츠를 직접 체험할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지, 체험 소요 시간은 약 5분입니다.

        참여 방법은 메타버스 캠퍼스에 접속해 제공된 콘텐츠를 자유 탐구하시면 되며, 중학생 이상 누구나 체험할 수 있습니다.

        잠시 후,  AICOSS 메타버스캠퍼스 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "AI를 활용한 명화속 몰입체험" -> """
        지금 이동 중인 부스는 AI 명화 속 몰입체험 입니다.

        이 프로그램은 본인의 얼굴을 촬영해 명화 속 장면에 실시간으로 투영해보는 몰입형 예술 체험 프로그램입니다. 고흐, 모네와 같은 다양한 명화가 AI 기술로 재해석되어,  관람객의 얼굴과 자연스럽게 합성된 이미지가 대형 디스플레이에 바로 등장하는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 전체 소요 시간은 약 5분입니다.

        참여 방법은 현장에서 얼굴을 촬영한 뒤 원하는 명화를 선택하면 되고, 중학생 이상 누구나 체험할 수 있습니다.

        잠시 후, AI 명화 속 몰입체험 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요
    """.trimIndent()

            "AI를 활용한 캐리커쳐 체험" -> """
        지금 이동 중인 부스는 'AI 캐리커처 체험'입니다.

        이 프로그램은 관람객의 얼굴을 촬영한 뒤, AI가 이를 기반으로 10가지 스타일의 캐리커처 이미지로 자동 변환해주는 프로그램입니다. 어린아이 콘셉트부터 애니메이션, 픽셀아트, 웹툰, 인형 스타일까지 다양한 캐릭터 표현을 즉시 확인할 수 있어 개인 취향에 맞는 캐리커처를 선택해 즐길 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 체험 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분입니다.

        참여 방법은 얼굴을 촬영하고 원하는 스타일을 선택하면 되고, 중학생 이상 누구나 체험할 수 있습니다.

        잠시 후, AI 캐리커처 체험 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요
    """.trimIndent()

            "Scent Memory" -> """
        지금 이동 중인 부스는 'Scent Memory' 입니다.

        이 프로그램은 관람객이 터치 스크린으로 간단한 취향 설문을 입력하면, 향료 데이터베이스를 기반으로 한 빅데이터 분석 프로그램이 개인 맞춤형 향수 레시피를 자동으로 추천해주는 체험입니다. 좋아하는 이미지, 분위기, 선호 향을 선택하기만 해도 AI가 나만의 향 조합을 즉시 제안해 주는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분입니다.

        참여 대상은 초·중·고 학생부터 일반 관람객까지 누구나 참여 가능하며, 일부 시간대에는 추천받은 레시피를 기반으로 실제 향수 샘플을 만들어보는 체험도 진행됩니다. 또한, 관람객들의 향 취향 데이터를 활용해 스트링 아트 형태의 시각화 전시도 함께 제공됩니다.

        잠시 후, Scent Memory 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "기택이의 대모험" -> """
        지금 이동 중인 부스는 ‘기택이의 대모험’ 입니다.

        이 프로그램은 데이터 오류를 시각적으로 표현한 두 화면을 비교해, 틀린 그림 찾기처럼 잘못된 부분을 찾아내는 방식으로 디버깅 개념을 체험할 수 있는 교육형 게임입니다. 게임 속 캐릭터 ‘기택이’와 함께 데이터에서 발생하는 오류와 이상 패턴이 어떻게 발견되는지 직접 눈으로 확인하며 배울 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5~10분입니다.

        참여 대상은 초·중·고 학생부터 일반 관람객까지 누구나 가능하며, 본 프로그램은 경기과학기술대학교 COSS 서포터즈가 직접 개발한 교육용 콘텐츠로 제작되었습니다.

        잠시 후, ‘기택이의 대모험’ 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "나는 누구일까?" -> """
        지금 이동 중인 부스는 ‘나는 누구일까?’ 입니다.

        이 프로그램은 참가자가 랜덤으로 제공되는 5개의 키워드를 기반으로, 그 키워드들이 가리키는 공통 개념이나 단어를 추리해 정답을 맞히는 게임입니다. 키워드를 잘 조합해 단서를 찾아가는 과정에서, 데이터 수집과 정보 연결의 중요성을 자연스럽게 이해할 수 있는 교육형 콘텐츠입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5~10분입니다.

        참여 대상은 초·중·고 학생부터 일반 관람객까지 누구나 참여 가능하며, 본 프로그램은 경상국립대학교 COSS 서포터즈가
        데이터사이언스 교육을 기반으로 직접 제작한 게임 콘텐츠입니다.

        잠시 후, ‘나는 누구일까?’ 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "나의 왕자님, 공주님을 찾아라!" -> """
        지금 이동 중인 부스는 ‘나의 왕자님·공주님을 찾아라!’ 입니다.

        이 프로그램은 참가자가 선택한 단어와 취향 정보를 기반으로, 빅데이터로 학습된 생성형 AI가 나만의 이상형 이미지를 즉시 만들어주는 체험입니다. 단어만 골라도 AI가 얼굴 특징을 분석해 가상의 이상형 캐릭터를 자동 생성해주는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 체험 소요 시간은 약 5~10분입니다.

        참여 대상은 초·중·고 학생부터 일반 관람객까지 누구나 참여 가능하며, 체험을 통해 생성된 이상형 이미지는 포토카드 형태로 받아볼 수 있습니다. 또한 올해 CO-SHOW에서 만들어진 누적 이상형 데이터를 시각화한 전시물도 함께 관람할 수 있습니다.
    """.trimIndent()

            "잠시 후, 빅데이터관 ‘나의 왕자님·공주님을 찾아라!’ 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요." -> """
        빅데이터 어워즈   "지금 이동 중인 부스는 ‘빅데이터 어워즈’ 입니다.

        이 전시는 전국 빅데이터 컨소시엄에 참여한 대학생들이
        각종 경진대회와 프로젝트에서 만들어낸 우수 분석 사례와 결과물을 한자리에서 볼 수 있는 공간입니다. 데이터 수집·분석·시각화가 실제 문제 해결과 정책 제안에 어떻게 활용되는지 현장 사례 중심으로 확인할 수 있는 것이 특징입니다.

        전시 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 관람 소요 시간은 약 5분입니다.

        주요 성과물에는 서울시립대학교의 수도권 생활이동 데이터 해커톤 우수작, 숙명여자대학교의 월간 빅데이터 캠퍼스 대회 수상작, 한동대학교의 경상북도 공공데이터 활용 경진대회 우수작 등이 포함되어 있으며, 각 작품의 분석 과정과 시각화, 정책 제안까지 한눈에 살펴볼 수 있습니다.

        잠시 후, 빅데이터 어워즈 전시존에 도착합니다.
    """.trimIndent()

            "어서와, 반도체 회로제작은 처음이지?" -> """
        지금 이동 중인 부스는 '어서와, 반도체 회로제작은 처음이지?' 입니다.

        이 프로그램은 반도체 회로의 기초 원리를 배우고, 실제로 게임기용 PCB 회로를 직접 만들고 조립해보는 실습형 프로그램입니다. 반도체가 어떻게 전기 신호를 처리하고 동작을 만들어내는지 회로를 직접 만져보며 이해할 수 있는 것이 특징이며, 완성된 회로는 레트로 게임기로 작동되는 실물 키트로 제공됩니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 30분입니다. 회차별로 운영되며, 한 회차당 최대 30명까지 참여 가능합니다.

        참여 대상은 초등학교 4학년 이상, 참여 방법은 현장 선착순 접수입니다.

        잠시 후, '어서와, 반도체 회로제작은 처음이지?' 부스에 도착합니다. 도착 후 스채프의 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "미래자동차 VR 레이싱 체험" -> """
        지금 이동하실 부스는 미래자동차 VR 레이싱 체험 입니다.

        이 프로그램은 4D VR 레이싱 시뮬레이터를 활용해, 실제 차량에 탑승한 것처럼 가속, 회전, 충격까지 전신으로 느낄 수 있는 몰입형 레이싱 프로그램입니다. VR 화면과 모션 플랫폼이 함께 작동하면서 속도감과 진동, 방향 전환이 실제 운전과 유사하게 재현되는 것이 특징입니다.

        체험 기간은 11월 29일 하루, 운영 시간은 오전 10시부터 오후 5시까지, 체험 소요 시간은 약 5분입니다.

        참여 방법은 현장 신청 후 순번대로 진행되며, 초등학생 이상 누구나 참여할 수 있습니다. 게임 난이도는 과도하게 높지 않아 누구나 부담 없이 즐길 수 있으며, 레이싱 초보자도 VR 화면의 주행 가이드에 따라 쉽게 체험할 수 있습니다.

        잠시 후, 미래자동차 VR 레이싱 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "미래자동차 CO-SHOW 자율주행 체험" -> """
        지금 이동하실 부스는 미래자동차 CO-SHOW 자율주행 체험입니다.

        이 프로그램은 자율주행 자동차 모형을 직접 조립하고 채색한 뒤, 전용 레이싱 트랙에서 실제 주행까지 체험하는 구성으로 이루어져 있습니다. 조립 과정은 약 30분, 트랙 주행은 약 30분으로, 전체 체험 소요 시간은 총 60분입니다.

        운영은 11월 29일 하루, 오전 10시부터 오후 5시까지 6회차로 나누어 진행되며, 회차당 10명, 하루 총 60명까지 참여할 수 있습니다.

        참여 방법은 사전 신청 후 예약된 시간에 부스를 방문하시면 되며, 누구나 참여 가능합니다.

        잠시 후, 미래자동차 CO-SHOW 자율주행 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "AWS DeepRacer 자율주행 체험" -> """
        지금 이동하실 부스는 AWS DeepRacer 자율주행 체험입니다.

        이 프로그램에서는 자율주행 AI가 직접 운전하는 레이싱 카와
        참가자가 조종하는 차량이 속도와 기록을 겨루는 대결 형식으로 진행됩니다. AI 모델의 판단과 주행 방식이 눈앞에서 실시간으로 나타나 인공지능과 인간 운전의 차이를 직접 체감할 수 있는 체험입니다.

        운영 기간은 11월 26일부터 29일까지,  체험 시간은 오전 10시부터 오후 5시 30분, 소요 시간은 약 5분입니다.

        참여 방법은 사전 신청 후 예약된 시간에 부스를 방문하시면 되며, 키 100cm 이상이면 누구나 참여할 수 있습니다.

        잠시 후,  AWS DeepRacer 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "휴먼아나토미 아트랩 : 바디페인팅, XR해부학체험" -> """
        지금 이동 중인 부스는 휴먼아나토미 아트랩, 바디페인팅·XR 해부학 체험존입니다.

        이 프로그램은 아나토마지 테이블을 활용한 초정밀 3D 인체 해부 시각화와, 직접 몸에 그림을 그려보는 바디페인팅 체험을 함께 경험하는 융합형 콘텐츠입니다. 실제 인체와 유사한 구조를 손쉽게 관찰하며 디지털 해부학 기술과 예술 표현이 결합된 새로운 방식의 인체 탐구를 직접 체감할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 15분이며, 바디페인팅과 XR 기반 해부 탐색을 직접 체험하고 관람하는 방식으로 진행됩니다.

        참여 대상은 초등학생 이상 누구나입니다.

        잠시 후, 휴먼아나토미 아트랩 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "생명의 코드, DNA 사이언스랩" -> """
        지금 이동 중인 부스는 생명의 코드, DNA 사이언스랩입니다.

        이 프로그램은 바나나에서 DNA를 실제로 추출하고, 전기 영동을 통해 DNA가 분리되는 모습을 직접 관찰해보는 실험형 체험입니다. 또한 개인식별에 활용되는 DNA 지문 판독의 원리를 쉽고 흥미롭게 배울 수 있어, 유전자 구조와 생명의 코드를 과학 실험으로 이해할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 60분이며, 실험 키트와 안내에 따라 DNA 추출과 분석 과정을 직접 수행하는 방식으로 진행됩니다.

        참여 대상은 초등학생 이상 누구나입니다.

        잠시 후, DNA 사이언스랩 부스에 도착합니다. 도착 후 스태프 안내에 따라 실험을 진행해주세요.
    """.trimIndent()

            "장애인을 위한 디지털 치료제 : e 스포츠 휠체어 레이싱 체험존" -> """
        지금 이동 중인 부스는 e-스포츠 휠체어 레이싱 체험존입니다.

        이 프로그램은 휠체어 사용자와 일반 관람객이 함께 참여하는 디지털 헬스케어 기반의 체험형 게임 전시입니다. 실제 휠체어 움직임을 게임 플레이와 연동하여, 이동성이 어떻게 즐거움·몰입·재활 경험으로 확장되는지 직접 체감할 수 있는 것이 특징입니다. 디지털 기술과 보조 기기가 결합해 새로운 재활 방식과 건강 증진 가능성을 제시하는 프로그램이기도 합니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 4분이며, 휠체어 또는 제공된 장비를 활용해 직접 게임 레이스에 참여하는 방식으로 체험이 진행됩니다.

        참여 대상은 초등학생 이상 누구나입니다.
    """.trimIndent()

            "잠시 후, e-스포츠 휠체어 레이싱 체험존 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요." -> """
        웨어러블 보행 보조 로봇 : 보행 로봇 체험존   "" 지금 이동 중인 부스는 '웨어러블 보행 보조 로봇 체험존'입니다.

        이 프로그램은 로봇 재활기기 ‘Botfit’을 직접 착용해, 로봇이 인간의 보행을 어떻게 보조하고 강화하는지 몸으로 느끼며 이해할 수 있는 체험형 전시입니다. 기기가 사용자의 다리 움직임을 감지해 보행을 안정적으로 돕고 움직임을 보정해 주어, 재활 로봇 기술의 실제 동작 원리와 기술적 변화를 직관적으로 경험할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 10분이며, 주어진 동선을 따라 로봇 기기를 착용한 채 직접 걸어보는 방식으로 진행됩니다.

        참여 대상은 초등학생 이상입니다.
    """.trimIndent()

            "잠시 후, 보행 보조 로봇 체험존 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해주세요." -> """
        꿈의 학교 속으로! 메타버스 캠퍼스 & 콘텐츠 여행 (실감미디어 메타버스 플랫폼 투어)   "" 지금 이동 중인 부스는 '메타버스 캠퍼스 & 콘텐츠 여행 체험' 입니다.

        이 프로그램은 실감미디어 기술로 구현된 가상 캠퍼스를 자유롭게 탐험하며, 현실에서는 경험하기 어려운 미래형 교육 공간과 창의 콘텐츠를 직접 만나볼 수 있는 체험형 전시입니다. 건국대학교, 경희대학교, 계원예술대학교 등 여러 대학이 참여하여 다양한 메타버스 학습 환경과 작품 콘텐츠를 몰입형 방식으로 즐길 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 10분이며, 참여 방법은 현장 접수 후 아바타로 캠퍼스를 탐험하는 방식으로 진행됩니다.

        참여 대상은 초등학생 고학년 이상입니다.

        잠시 후, 메타버스 캠퍼스 & 콘텐츠 여행 부스에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "디지털 숲 명상 오디세이" -> """
        지금 이동 중인 부스는 '디지털 숲 명상 오디세이' 입니다.

        이 프로그램은 가상현실로 구현된 숲속 환경과 사계절의 기상 변화를 배경으로, 시각과 청각을 활용해 정신과 신체의 긴장을 완화하는 몰입형 명상 프로그램입니다. 생생한 숲의 영상과 공간 사운드가 관람객을 감싸며, 현실에서는 느끼기 어려운 깊고 조용한 휴식 경험을 제공하는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 30분이며, 참여 방법은 사전 접수 후 VR 명상 공간에 입장하는 방식으로 진행됩니다.

        참여 대상은 초등학생 고학년 이상입니다.

        잠시 후, 디지털 숲 명상 오디세이 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "톡톡 튀는 콘텐츠! 실감 팡팡 체험존 (실감미디어 경진대회 우수작품 체험)" -> """
        지금 이동 중인 부스는 '실감 팡팡 체험존'입니다.

        이 프로그램은 전국 대학생들이 제작한 창의적인 실감미디어 작품 15종을 직접 체험할 수 있는 전시입니다. 현실을 넘어서는 몰입형 기술과 독창적인 아이디어가 결합된 콘텐츠를 경험하며, 미래 실감미디어가 가진 가능성과 상상력을 체감할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 10분이며, 참여 방법은 현장 접수 후 자유 체험 방식으로 진행됩니다.

        참여 대상은 초등학생 고학년 이상입니다.

        잠시 후, 실감 팡팡 체험존 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "진짜처럼 짜릿! 햅틱 손맛 체험존 (VR 기반의 햅틱 디바이스 체험)" -> """
        지금 이동 중인 부스는 ‘진짜처럼 짜릿! 햅틱 손맛 체험존’ 입니다.

        이 프로그램은 VR 환경에서 햅틱 장갑을 직접 착용하고, 손끝으로 물체의 질감과 움직임을 느끼며 조작하는 차세대 체험형 콘텐츠입니다. 요리를 하거나 가야금을 연주하는 등의 동작을 가상 공간에서 재현할 수 있으며, 장갑이 전달하는 진동·압력·촉감 피드백을 통해 실제를 만지는 듯한 생생한 손맛을 경험할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 체험 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 10분입니다.
        참여 방법은 현장 접수로 진행되며, 초등학생 고학년 이상이면 누구나 참여할 수 있습니다.

        잠시 후, 햅틱 손맛 체험존 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "일반인 로봇 교육프로그램5(휴머노이드 이론교육 및 미션수행)" -> """
        지금 이동 중인 부스는 '휴머노이드 이론교육 및 미션 수행 체험'입니다.

        이 프로그램은 고성능 소형 휴머노이드 로봇의 원리와 제어법을 배우고, 직접 로봇을 조종해 실전 미션을 수행해보는 교육형 체험으로 구성되어 있습니다. 로봇의 관절 구조, 균형 유지 방식, 동작 제어 등 휴머노이드 기술의 핵심 원리를 이해한 뒤, 실제 로봇을 활용해 주어진 미션을 해결하며 로봇 공학의 응용을 몸소 경험할 수 있는 프로그램입니다.

        체험 기간은 11월 26일부터 29일까지이며, 운영 시간은 오후 3시부터 4시 30분까지, 소요 시간은 약 90분입니다.

        참여 대상은 중학생 이상으로, 로봇 기술에 관심 있는 누구나 참여할 수 있습니다.

        잠시 후, 휴머노이드 로봇 교육 프로그램 부스에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "일반인 로봇 교육 프로그램 1(경주로봇 만들기)" -> """
        지금 이동 중인 부스는 '경주로봇 만들기 체험'입니다.

        이 프로그램은 경주용 로봇을 직접 조립하고, 완성된 로봇을 트랙에서 주행시켜보는 실습형 로봇 교육 프로그램입니다. 참가자들은 제공된 로봇 키트를 이용해 바퀴·모터·프레임을 조립하고 기본 구동 방식을 익히며, 로봇이 어떻게 속도를 내고 방향을 유지하는지 직접 체험을 통해 이해할 수 있습니다. 조립이 끝난 후에는 트랙에서 다른 참가자들과 미니 레이스를 진행하여 로봇 조종의 재미와 성취감을 함께 느낄 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 11시부터 오후 12시까지, 소요 시간은 약 60분입니다.

        참여 대상은 초등학생 이상입니다.

        잠시 후, 경주로봇 만들기 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해주세요.
    """.trimIndent()

            "AI 드로잉 로봇 및 오목 로봇 체험" -> """
        지금 이동 중인 부스는 'AI 드로잉 로봇 및 오목 로봇 체험'입니다.

        이 프로그램은 AI 기반 로봇을 직접 조작하며 동작 원리를 이해하는 체험형 전시로, 그림을 스스로 그리는 ‘드로잉 로봇’과 인간과 대결하는 ‘오목 로봇’을 직접 다뤄볼 수 있는 것이 특징입니다.

        드로잉 로봇은 입력된 명령이나 알고리즘에 따라 그림을 생성하며, 로봇이 선을 인식하고 패턴을 구현하는 과정을 눈앞에서 확인할 수 있습니다. 오목 로봇은 간단한 인공지능 알고리즘을 기반으로 플레이 전략을 세워 참여자와 대전을 펼치며, AI의 판단 방식과 규칙 기반 의사결정이 어떻게 이루어지는지를 자연스럽게 체험할 수 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시까지이며, 소요 시간은 약 20분입니다.

        참여 대상은 초등학생 이상으로, 누구나 쉽게 이해하고 즐길 수 있도록 구성되어 있습니다.

        잠시 후, AI 드로잉 로봇 및 오목 로봇 체험 부스에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "ROBO SHOW(4족보행 로봇 및 테미 체험)" -> """
        지금 이동 중인 부스는 'ROBO SHOW – 4족보행 로봇 & 테미 로봇 체험'입니다.

        이 프로그램은 실제 현장에서 활용되는 최신 로봇 기술을 가까이에서 보고, 직접 체험하며 이해하는 로봇 쇼케이스형 프로그램입니다. 특히 4족보행 로봇과 모바일 서비스 로봇 ‘테미(Temi)’가 함께 등장해, 로봇의 이동·균형·환경 인지·상호작용 기술을 실시간으로 관람객에게 선보입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 30분입니다.

        참여 대상은 초등학생 이상이며, 특히 로봇 기술에 흥미가 있는 학생·가족 단위 관람객에게 큰 호응을 얻는 프로그램입니다.

        잠시 후, ROBO SHOW 로봇 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "일반인 로봇 교육 프로그램2(다이노 랩터 로봇 만들기)" -> """
        지금 이동 중인 부스는 '다이노 랩터 로봇 만들기 체험'입니다.

        이 프로그램은 로봇의 기본 원리와 구조를 직접 조립하면서 이해할 수 있도록 구성된 실습 중심 로봇 교육 프로그램입니다. 참가자들은 제공된 다이노 랩터 로봇 키트를 활용해 로봇의 몸체를 조립하고, 구동 방식과 센서 작동 원리를 자연스럽게 익히게 됩니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 정오 12시부터 오후 1시까지, 소요 시간은 약 60분입니다.

        참여 대상은 초등학생 이상이며, 특히 로봇 제작과 DIY 교육 체험에 관심 있는 학생들에게 매우 인기 있는 프로그램입니다.

        잠시 후, 다이노 랩터 로봇 만들기 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "일반인 교육 프로그램3(하프휠 로봇 만들기)" -> """
        지금 이동 중인 부스는 '하프휠 로봇 만들기 체험'입니다.

        이 프로그램은 두 개의 바퀴로 균형을 잡고 움직이는 하프휠 로봇을 직접 조립하며 로봇의 구조·구동 원리·균형 제어 방식을 이해할 수 있는 실습형 교육 프로그램입니다. 참가자들은 로봇 키트를 활용해 프레임 조립부터 모터 연결, 기본 제어 동작 확인까지 차근차근 따라가며 로봇이 어떻게 안정적으로 움직임을 유지하는지 직접 체험하게 됩니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오후 1시부터 2시까지, 소요 시간은 약 60분입니다.

        참여 대상은 초등학생 이상이며, 로봇 조립 체험과 DIY 활동을 좋아하는 관람객에게 특히 인기 있는 프로그램입니다.

        잠시 후, 하프휠 로봇 만들기 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "일반인 로봇 교육 프로그램4(유선 스파이더로봇 만들기)" -> """
        지금 이동 중인 부스는 '유선 스파이더 로봇 만들기 체험'입니다.

        이 프로그램은 여러 개의 다리를 사용해 움직이는 스파이더 로봇을 직접 조립하고 유선 조종기로 동작을 확인해보는 실습형 로봇 교육 프로그램입니다. 참가자들은 제공된 로봇 키트를 활용해 몸체·다리 관절·구동부를 조립하며 다족 로봇의 이동 방식과 동작 구조를 자연스럽게 이해하게 됩니다. 완성된 로봇은 유선 조종기로 앞·뒤·좌·우 움직임을 직접 테스트해보며 로봇 제어의 기본 개념을 몸으로 익힐 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오후 2시부터 3시까지, 소요 시간은 약 60분입니다.

        참여 대상은 초등학생 이상이며, 로봇 조립과 메커니즘 구조에 관심 있는 관람객에게 특히 적합한 프로그램입니다.

        잠시 후, 유선 스파이더 로봇 만들기 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "태양전지 패널" -> """
        지금 이동 중인 부스는 '태양전지 패널' 입니다.

        이 프로그램은 태양광을 이용해 전력을 생산하는 태양전지 패널의 원리와 구조를 직접 확인해볼 수 있는 체험형 전시입니다. 특히 건물 외장재와 태양광 모듈을 하나로 결합한 BIPV 시스템이 어떻게 적용되는지, 친환경 에너지 생산 방식이 건축 자재와 어떤 방식으로 통합되는지를 실제 패널을 통해 확인할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분입니다. 참여 대상은 전 국민 누구나이며, 태양광 에너지 기술을 간단하고 직관적으로 이해할 수 있도록 구성되어 있습니다.

        이 체험은 POSCO BIPV 시스템 협찬으로 운영됩니다.

        잠시 후, 태양전지 패널 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "에코루프 키친스테이션" -> """
        지금 이동 중인 부스는 '에코루프 키친스테이션' 입니다.

        이 프로그램은 식기세척기와 음식물처리기를 소형 히트펌프 기술로 결합해, 발생하는 폐열을 다시 활용하는 미래형 주방 시스템을 직접 살펴볼 수 있는 체험입니다. 이 시스템은 하나의 장치에서 세척·건조·음식물 처리까지 수행하며, 에너지 절감·편의성·친환경성을 동시에 구현한 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분이며, 참여 대상은 전 국민 누구나입니다.

        이 체험은 삼성전자와 고려대학교가 공동으로 진행한 미래 친환경 가전 아이디어 공모전 대상작을 기반으로 제작되었습니다.
    """.trimIndent()

            "잠시 후, 에코루프 키친스테이션 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요." -> """
        수소 스테이션   "“ 지금 이동 중인 부스는 '수소 스테이션' 입니다.

        이 프로그램은 수소전기차에 연료를 공급하기 위해 사용되는 ‘수소 저장·충전 인프라’의 구조와 원리를 이해할 수 있는 체험형 전시입니다. 수소가 어떻게 생산·운송·저장되고,
        고압 상태로 수소 충전소에서 차량에 주입되는 과정이 어떤 기술로 이루어지는지 모형과 안내를 통해 직관적으로 확인할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분이며, 참여 대상은 전 국민 누구나입니다.

        잠시 후, 수소 스테이션 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "플러스에너지빌딩" -> """
        지금 이동 중인 부스는 '플러스에너지빌딩' 입니다.

        이 프로그램은 건물이 스스로 사용하는 에너지보다 더 많은 에너지를 생산해, 오히려 순 에너지 이익을 만들어내는 차세대 친환경 건축 기술을 소개하는 전시입니다. 태양광·단열·환기·스마트 제어 기술 등이 통합되어 건물 내부의 에너지 소비를 최소화하고, 생산된 에너지가 소비량을 초과하는 ‘플러스 에너지 구조’가 어떻게 구현되는지 모형과 시스템 설명을 통해 직관적으로 확인할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 5분이며, 참여 대상은 전 국민 누구나입니다.

        잠시 후, 플러스에너지빌딩 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "태양전지 자동차경주 체험물" -> """
        지금 이동 중인 부스는 '태양전지 자동차경주 체험물'입니다.

        이 프로그램은 충전된 태양전지를 장착한 미니카를 트랙에서 직접 주행시켜보는 체험형 에너지 교육 프로그램입니다. 햇빛을 전기로 변환하는 태양전지의 원리를 실제 주행에 적용해보며, 친환경 에너지 변환이 어떤 방식으로 동작하는지 직관적으로 이해할 수 있는 것이 특징입니다. 참가자들은 미니카를 트랙 위에서 조종하며 태양광 충전 상태와 주행 성능의 관계를 직접 체감하게 됩니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 20분이며, 참여 대상은 전 국민 누구나입니다.

        잠시 후, 태양전지 자동차경주 체험물 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "압전에너지" -> """
        지금 이동 중인 부스는 '압전에너지' 입니다.

        이 프로그램은 발판을 눌러 발생하는 압력을 전기 에너지로 변환하는 ‘압전 기술’을 직접 체험해볼 수 있는 전시입니다.
        참가자가 발판 위를 밟으면, 그 순간 생성된 전력이 즉시 수치로 표시되어 우리의 움직임이 에너지로 전환되는 과정을 직관적으로 확인할 수 있는 것이 특징입니다. 압전소자가 어떻게 압력을 전기에너지로 바꾸는지, 몸으로 체감하며 이해할 수 있도록 구성되어 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 20분이며, 참여 대상은 전 국민 누구나입니다.

        잠시 후, 압전에너지 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "수력 풍력에너지 체험" -> """
        지금 이동 중인 부스는 '수력 풍력에너지 체험' 입니다.

        이 프로그램은 물의 흐름(수력)과 바람의 힘(풍력)이 어떻게 전기를 만들어내는지, 그 전체 과정을 눈으로 직접 확인할 수 있도록 설계된 에너지 체험 전시입니다. 참가자가 수차와 풍차를 직접 돌려보며 회전 운동이 발전기 → 전기 생산 과정으로 이어지는 원리를 이해할 수 있어, 재생에너지의 작동 구조를 쉽게 배우도록 구성되어 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 10분이며, 참여 대상은 전 국민 누구나입니다.

        잠시 후, 수력·풍력 에너지 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "수소밸류체인 VR 체험" -> """
        지금 이동 중인 부스는 '수소밸류체인 VR 체험'입니다.

        이 프로그램은 수소가 어떻게 만들어지고, 저장되고, 운송되고, 다시 에너지로 활용되는지 전체 흐름을 VR 환경에서 한눈에 따라가며 체험할 수 있는 몰입형 콘텐츠입니다. 가상의 산업 시설과 공정 흐름을 직접 보며, 수소 에너지의 생산부터 활용까지 이어지는 수소밸류체인 구조를 시각적으로 이해할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 20분이며, 전 국민 누구나 참여 가능합니다.

        잠시 후, 수소밸류체인 VR 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "태양광 강아지 로봇" -> """
        지금 이동 중인 부스는 '태양광 강아지 로봇' 입니다.

        이 프로그램은 태양광이 전기를 만드는 원리를 직접 실험하고, 태양광 전지판을 이용해 스스로 움직이는 강아지 로봇(자동체)을 만들어보는 체험형 교육 프로그램입니다. 태양광 패널에 빛을 비추면 전류가 어떻게 발생하는지, 그 에너지가 로봇 구동으로 어떻게 연결되는지 눈으로 보고 손으로 배우는 실습 중심 활동이 특징입니다.

        체험 기간은 11월 29일 하루, 운영 시간은 오후 2시부터 4시까지, 소요 시간은 약 50분이며, 중학생 이상이면 누구나 참여할 수 있습니다.

        잠시 후, 태양광 강아지 로봇 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "수소연료전지자동차" -> """
        지금 이동 중인 부스는 '수소연료전지 자동차' 입니다.

        이 프로그램은 수소연료전지가 전기를 만들어내는 원리를 직접 실험하고, 그 전기로 움직이는 친환경 자동차 모형을 직접 제작·구동해보는 체험형 과학 교육 프로그램입니다. 수소가 전기 에너지로 변환되는 과정, 연료전지 내부 반응, 모터 구동까지 에너지 흐름을 단계별로 몸소 확인할 수 있도록 구성되어 있어 학생들이 친환경 에너지 기술을 실제로 이해하기에 적합한 활동입니다.

        체험 기간은 11월 27일부터 29일까지, 운영 시간은 오전 10시 30분부터 오후 1시 30분, 소요 시간은 약 40분이며, 참여 방식은 사전 모집입니다. 중학생 이상이면 참여 가능합니다.

        잠시 후, 수소연료전지 자동차 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "내 손안의 작은 발전기" -> """
        지금 이동 중인 부스는 ‘내 손안의 작은 발전기’ 입니다.

        이 프로그램은 친환경 에너지가 생성되는 원리를 직접 이해하고, 손으로 작동시키는 소형 발전기 키트를 만들어보는 체험형 교육 프로그램입니다. 발전기의 구조를 직접 조립하고, 손의 움직임으로 전기가 어떻게 만들어지는지 빛·전류 변화 등을 통해 눈으로 확인할 수 있어 에너지 변환 원리를 쉽고 흥미롭게 경험할 수 있습니다.

        체험 기간은 11월 26일부터 28일까지, 운영 시간은 오후 3시부터 5시까지, 소요 시간은 약 40분이며, 대상은 초등학생 이상입니다.

        잠시 후, 내 손안의 작은 발전기 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "업사이클링 카드지갑" -> """
        지금 이동 중인 부스는 '업사이클링 카드지갑' 입니다.

        이 프로그램은 일상에서 버려지는 친환경 업사이클링 소재를 활용해 직접 카드지갑을 제작해보는 체험입니다. 간단한 공구 사용과 접기·재단 과정을 통해 친환경 재료가 새로운 생활용품으로 다시 태어나는 과정을 경험할 수 있어 지속가능한 소비와 자원순환의 의미를 자연스럽게 배울 수 있습니다.

        체험 기간은 11월 26일부터 28일까지, 운영 시간은 오후 2시부터 2시 30분까지, 소요 시간은 약 30분입니다. 참여 대상은 전 국민 누구나입니다.

        잠시 후, 업사이클링 카드지갑 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "수소자동차 롱롱" -> """
        지금 이동 중인 부스는 '수소자동차 롱롱' 입니다
        .
        이 프로그램은 수소자동차가 움직이는 원리를 쉽고 재미있게 이해할 수 있도록 구성된 제작형 체험 프로그램입니다. 참가자는 수소 에너지가 어떻게 전기로 변환되고, 그 전기가 모터를 구동해 자동차를 움직이는지 직접 실험하며 배울 수 있습니다. 키트 조립을 통해 친환경 수소자동차 모형을 완성해 보는 과정이 포함되어 있어, 어린 학생들도 과학적 개념을 자연스럽게 익힐 수 있다는 점이 특징입니다.

        체험 기간은 11월 26일 하루, 운영 시간은 오전 10시 30분부터 오후 1시 30분까지, 소요 시간은 약 40분이며, 참여 대상은 초등학생 이상입니다.

        잠시 후, 수소자동차 롱롱 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "블록코딩을 활용한 드론제어" -> """
        지금 이동 중인 부스는 '블록코딩을 활용한 드론제어' 입니다.

        이 프로그램은 블록코딩 기반 드론 프로그래밍 교육을 통해 드론 제어의 기본 구조부터 실제 비행까지 직접 경험해볼 수 있는 단계형 체험 교육입니다. 참가자는 드론이 어떻게 명령을 해석하고 움직이는지 이해하며, 시뮬레이션 → 기본 실습 → 고급 실습으로 이어지는 체계적인 과정 속에서 드론 제어 원리를 자연스럽게 익힐 수 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시 30분까지, 소요 시간은 최대 90분입니다.
        참여 대상은 초등학생 고학년 이상이며, 기본 교육부터 고급 실습까지 모두 체험해볼 수 있습니다.

        잠시 후, 블록코딩을 활용한 드론제어 부스에 도착합니다. 도착 후, 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "UAM 조종 시뮬레이터 체험" -> """
        지금 이동 중인 부스는 'UAM 조종 시뮬레이터 체험' 입니다.

        이 프로그램은 미래 도심 이동수단인 UAM(도심항공교통) 의 조종법을 배우고, 실제 비행 환경과 유사하게 구성된 시뮬레이터에서 직접 조종해보는 체험형 교육입니다. 기초 교육을 통해 이륙·천이·선회·착륙과 같은 기본 기동 절차를 익힌 뒤, 시뮬레이터에 탑승해 조종 스틱을 직접 운용하며 비행을 체험할 수 있는 점이 특징입니다. 복잡한 항공 원리를 쉽게 이해하도록 구성되어 있어, 미래 항공 모빌리티에 대한 직관적인 경험을 제공합니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시 30분까지, 소요 시간은 약 30분 이내이며, 참여 대상은 초등학생 고학년 이상입니다.

        잠시 후, 'UAM 조종 시뮬레이터 체험' 부스에 도착합니다. 도착 후, 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "드론 장애물 경주 체험" -> """
        지금 이동 중인 부스는 '드론 장애물 경주 체험' 입니다.

        이 프로그램은 드론 볼을 직접 조종하여 장애물을 피하고, 다양한 미션을 수행해보는 체험형 드론 교육입니다. 드론의 기본 비행 원리를 몸으로 익히며, 스로틀·피치·요와 같은 조종 감각을 실제 조작을 통해 체험할 수 있습니다. 장애물 구간을 통과하는 과정에서 순발력·조종 안정성·공간 감각을 자연스럽게 연습할 수 있어 긴장감과 재미가 모두 살아있는 프로그램입니다.

        체험 기간은 11월 28일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시 30분까지, 소요 시간은 약 30분이며, 참여 대상은 초등학생 고학년 이상입니다.

        잠시 후, '드론 장애물 경주 체험' 부스에 도착합니다. 도착 후, 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "반도체를 분해해보자" -> """
        지금 이동 중인 부스는 ‘반도체를 분해해보자!' ’입니다.

        이 프로그램은 반도체 장비의 내부를 실제처럼 들여다보고, 직접 분해하고 다시 조립해보는 과정을 VR 환경에서 체험할 수 있는 교육형 콘텐츠입니다. 장비 구조를 눈앞에서 생생하게 확인하며, 평소 접근하기 어려운 반도체 제조 과정의 핵심 원리를 쉽게 이해할 수 있습니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 6시, 체험 소요 시간은 약 15~30분입니다.

        VR 헤드셋과 컨트롤러를 착용한 뒤 화면의 안내에 따라 조작하는 방식으로 진행되며, 누구나 자유롭게 참여할 수 있습니다. 사전 신청과 현장 접수 모두 가능하며, 단체는 최대 30명, 현장 참여는 최대 15명까지 체험이 가능합니다.

        잠시 후 ‘반도체를 분해해보자!’ 부스에 도착합니다. 도착 후, 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "[이차전지 컨소시엄] 교육 프로그램(다면형 실감콘텐츠 영상 체험 및 VR 영상 체험)" -> """
        지금 이동 중인 부스는 '다면형 실감콘텐츠 영상 체험 및 VR 영상 체험' 입니다.

        이 프로그램은 이차전지 산업을 VR과 실감형 영상으로 쉽게 이해할 수 있도록 구성된 교육형 체험입니다. 다면형 실감 콘텐츠를 통해 전지의 구조와 제조 과정을 한눈에 확인할 수 있고, VR 체험에서는 실제 공정을 기반으로 제작된 가상 투어와 화재 대응 시뮬레이션을 체험할 수 있습니다.

        운영 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 5시, 체험 시간은 약 10~15분입니다.

        참여 대상에 제한이 없으며, 누구나 자유롭게 참여하실 수 있습니다.

        잠시 후 '다면형 실감콘텐츠 영상 체험 및 VR 영상 체험' 부스에 도착합니다. 도착 후, 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "보조배터리 제작 체험(보조배터리 만들어봤어? 안 만들어 봤으면 말을 하지마!)" -> """
        지금 이동 중인 부스는 ‘보조배터리 제작 체험’ 입니다.

        이 프로그램은 배터리 원리를 가장 직관적으로 이해할 수 있는 실제 제작 체험형 프로그램입니다. 참여자는 배터리 조립 키트를 활용해 보조배터리를 직접 조립하며, 이차전지가 어떻게 작동하는지 과정을 한눈에 확인할 수 있습니다.

        운영 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 5시, 체험 시간은 약 30~60분입니다. 사전 신청과 현장 접수 모두 가능하며, 참여 대상 제한 없이 누구나 참여할 수 있습니다.

        체험을 완료하면 직접 만든 귀여운 보조배터리 기념품도 받아가실 수 있습니다.

        잠시 후 ‘보조배터리 제작 체험’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "인공위성 통신 체험관" -> """
        지금 이동 중인 부스는 ‘인공위성 통신 체험관' 입니다.

        이 프로그램은 메타버스로 구현된 가상 인공위성 관제실에서
        위성이 발사되는 순간부터 통신이 연결되는 과정까지 직접 체험할 수 있는 교육형 콘텐츠입니다. 인공위성이 어떻게 궤도에 안착하고, 어떤 방식으로 지상국과 데이터를 주고받는지
        미래 우주통신 기술을 직관적으로 이해할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 5시, 체험 소요 시간은 약 10~15분이며, 참여 대상은 초등학생 이상입니다.

        사전 신청자와 현장 신청자 구간이 나누어 운영되므로 도착 후 스태프 안내에 따라 지정된 시간에 맞춰 체험을 진행해 주세요.

        잠시 후 ‘인공위성 통신 체험관’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 시작해 주세요.
    """.trimIndent()

            "방탈출 프로그램 '교수님의 수상한 과제'" -> """
        지금 이동 중인 부스는 방탈출 프로그램 ‘교수님의 수상한 과제’ 입니다.

        이 프로그램은 방탈출 형식으로 진행되며, 참가자가 직접 ‘에코업 6대 과제’ 속 환경 문제를 해결해보는 교육형 미션 프로그램입니다.  주어진 단서를 분석하고 문제를 풀어가는 과정에서 환경 위기와 지속가능성에 대한 개념을 자연스럽게 배울 수 있어, 미래 혁신 인재에게 필요한 핵심 역량을 게임처럼 흥미롭게 체험할 수 있습니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 20분이며, 참여 대상은 초·중·고 학생 누구나 가능합니다.

        잠시 후 ‘교수님의 수상한 과제’ 방탈출 프로그램 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "LED로 그리는 탄소중립" -> """
        지금 이동 중인 부스는 ‘LED로 그리는 탄소중립’ 입니다.

        이 프로그램은 탄소중립의 개념을 쉽고 재미있게 배울 수 있도록 구성된 체험형 교육 프로그램입니다. 1단계에서는 뽑기와 퀴즈를 통해 탄소중립 생활 실천법을 알아보고, 2단계에서는 직접 LED 아크릴 조명등을 제작하며 친환경 메시지를 나만의 작품으로 완성하게 됩니다. 마지막으로 온라인 만족도 조사를 통해 체험을 마무리합니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 20분이며, 참여 대상은 초등학생 고학년 이상입니다.

        잠시 후 ‘LED로 그리는 탄소중립’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "우리집 알뜰에너지 연구소" -> """
        지금 이동 중인 부스는 ‘우리집 알뜰에너지 연구소’ 입니다.

        이 프로그램은 가정에서 매일 사용하는 전자제품이 얼마나 전기를 소비하는지 직접 확인해보는 대기전력 측정 체험 프로그램입니다. 1단계에서는 가전제품의 대기전력과 에너지소비효율등급을 배우고, 2단계에서는 실제 가전제품 3종의 대기전력을 직접 측정해보며 우리 집에서 새어 나가는 전기를 눈으로 확인할 수 있습니다. 체험 후에는 만족도 조사를 진행하고, 건전지 잔량 측정기 기념품도 받을 수 있습니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 6시, 소요 시간은 약 10분이며, 참여 대상은 누구나 참여 가능합니다.

        잠시 후 ‘우리집 알뜰에너지 연구소’ 부스에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "Space Green 우주 농부 인증 미션을 위한 탑승을 시작합니다!" -> """
        지금 이동 중인 부스는 ‘Space Green 우주 농부 인증 미션’ 입니다.

        이 프로그램은 2050년 우주 농부가 되기 위한 자격 미션을 수행하는 스토리 기반 체험으로, 애그테크(Ag-Tech) 기술이 접목된 미래 농업 환경을 단계별로 경험할 수 있는 immersive 체험 프로그램입니다.

        체험 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 25분, 참여 대상은 초등학생 이상입니다.

        잠시 후 ‘Space Green 우주 농부 인증 미션’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "3D펜으로 만드는 첨단소재 창의공작소" -> """
        지금 이동 중인 부스는 ‘3D펜으로 만드는 첨단소재 창의공작소’입니다.

        이 프로그램은 3D펜을 활용해 이름표 키링, 책갈피 등 간단한 입체 구조물을 직접 제작하며 기초 3D 메이킹을 경험할 수 있는 창의 제작 체험입니다. 제공되는 도면과 안내 리플렛을 참고하며 누구나 쉽게 따라 만들 수 있어, 입문자도 부담 없이 참여할 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시이며 소요 시간은 약 20분입니다. 참여 대상에는 제한이 없어 누구나 체험 가능합니다.

        잠시 후 ‘3D펜 창의공작소’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "손끝에서 펼쳐지는 스트레쳐블 첨단유연소재" -> """
        지금 이동 중인 부스는 ‘스트레쳐블 첨단유연소재 체험존’입니다.

        이 프로그램은 고분자 네트워크로 이루어진 스트레쳐블(늘어나는) 첨단 유연소재를 직접 만지고 늘려보며, 탄성과 복원력을 눈으로 확인할 수 있는 실험형 체험입니다. 기초 이론 설명 후, 실제 소재를 잡아당기고 변형해보며 고분자 구조가 어떻게 변하는지 비교 관찰할 수 있어, 소재 과학을 직관적으로 이해할 수 있는 프로그램입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시이며, 소요 시간은 약 60분입니다. 참여 대상은 초등학생 이상입니다.

        잠시 후 ‘스트레쳐블 첨단유연소재 체험존’에 도착합니다. 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "첨단소재워터랩:모링가의 비밀" -> """
        지금 이동 중인 부스는 ‘첨단소재워터랩: 모링가의 비밀’입니다.

        이 프로그램은 자연 유래 첨단소재인 ‘모링가 씨앗’이 물 속의 불순물을 제거하는 정화 원리를 실험을 통해 직접 확인하는 체험입니다. 간단히 만든 오염수에 모링가 추출액을 넣고 침전 과정을 관찰하면서, 물이 맑아지는 정화 과정을 눈앞에서 확인할 수 있어 환경·소재 융합 체험으로 구성되어 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 40분이며 초등학생 고학년 이상 누구나 참여할 수 있습니다.

        잠시 후 ‘첨단소재워터랩: 모링가의 비밀’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "Drone Under Attack: 하늘 위의 드론 위협" -> """
        지금 이동 중인 부스는 ‘Drone Under Attack: 하늘 위의 드론 위협’ 입니다.

        이 프로그램은 실제로 비행 중인 드론이 해킹 공격을 받는 순간을 눈앞에서 확인하며, 하늘 위에서 발생할 수 있는 사이버 위협의 실체를 이해할 수 있도록 구성된 안전·보안 교육형 체험입니다. 정상적으로 움직이던 드론이 공격을 받는 즉시 비정상적인 궤적과 오작동을 보이게 되며, 이를 통해 드론 보안의 필요성과 위험성을 직관적으로 배울 수 있는 것이 특징입니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시, 소요 시간은 약 15분입니다. 참여 대상은 고등학생 이상이며, 사전예약과 현장접수 모두 가능합니다.

        잠시 후 ‘Drone Under Attack’ 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "탈출하라, 사이버 보안 위기속 탈출기" -> """
        지금 이동 중인 부스는 ‘탈출하라, 사이버 보안 위기 속 탈출기’입니다.

        이 프로그램은 사이버 공격이 발생한 상황을 배경으로, 방 안 곳곳에 숨겨진 단서를 찾아 위험에서 탈출하는 체험형 방탈출 프로그램입니다. 참가자는 실제 보안 위협을 상징하는 PC, 금고, 암호 장치 등을 직접 다루며 퍼즐을 풀어야 하고, 제한 시간 안에 최종 문을 여는 것이 목표입니다. 사이버 보안의 핵심 원리를 게임처럼 자연스럽게 익힐 수 있는 것이 특징입니다.

        운영 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 30분입니다. 참여 대상은 중학생 이상이며, 사전예약과 현장접수 모두 가능합니다.

        잠시 후 ‘사이버 보안 위기 속 탈출기’ 체험 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "스마트홈 캠 해킹, 누군가 지켜보고 있다." -> """
        지금 이동 중인 부스는 ‘스마트홈 캠 해킹, 누군가 지켜보고 있다’입니다.

        이 프로그램은 스마트홈 기기, 특히 집안을 비추는 홈캠이 해킹될 경우 어떤 일이 벌어지는지를 직접 눈으로 확인할 수 있도록 구성된 보안 시연 프로그램입니다. 정상적으로 작동 중이던 홈캠이 공격자의 침입으로 사생활을 노출하는 순간을 체험하며, 일상 속 보안 위협을 실감 있게 이해할 수 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 10분입니다. 참여 대상은 초등학생 이상 누구나이며, 사전예약과 현장접수 모두 가능합니다.

        잠시 후 ‘스마트홈 캠 해킹’ 체험 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "해킹이 부른 의료 사고" -> """
        지금 이동 중인 부스는 ‘해킹이 부른 의료 사고’입니다.

        이 프로그램은 실제 의료기기를 활용해, 사이버 공격이 환자 생명에 어떤 위험을 초래할 수 있는지를 직접 확인할 수 있도록 구성된 보안 시연 프로그램입니다. 정상적으로 작동하던 인퓨전 펌프가 해킹 공격을 받는 순간 어떤 이상 동작이 발생하는지, 그리고 그 결과가 얼마나 치명적일 수 있는지를 생생하게 체험할 수 있습니다.

        체험 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 10분입니다. 참여 대상은 중학생 이상이며, 사전예약과 현장접수 모두 가능합니다.

        잠시 후 ‘해킹이 부른 의료 사고’ 체험 부스에 도착합니다. 도착 후 스태프의 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "우리의 친절한 로봇의 반란" -> """
        지금 이동 중인 부스는 ‘우리의 친절한 로봇의 반란’입니다.

        이 프로그램은 일상에서 흔히 보이는 서빙 로봇이 해킹 공격을 받는 순간 어떤 위험이 발생할 수 있는지를 직접 확인할 수 있는 사이버 보안 체험입니다. 정상적으로 움직이던 로봇이 해커의 조작으로 멈추거나 엉뚱한 방향으로 움직이며, 로봇 해킹이 실제 산업·생활 환경에서 어떤 위협으로 이어질 수 있는지를 눈앞에서 체감할 수 있습니다.

        운영 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 15분입니다. 참여 대상은 중학생 이상이며 현장접수와 사전예약 모두 가능합니다.

        잠시 후 ‘우리의 친절한 로봇의 반란’ 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "AI가 만든 가짜를 잡아라" -> """
        지금 이동 중인 부스는 ‘AI가 만든 가짜를 잡아라’입니다.

        이 프로그램은 AI가 생성한 이미지·텍스트·영상과 사람이 만든 결과물을 비교하며, 우리가 일상에서 마주칠 수 있는 ‘AI 가짜(Fake)’를 스스로 식별해보는 체험형 보안 프로그램입니다. 단서를 기반으로 진짜와 가짜를 구분하는 퀴즈 형식으로 진행되며, AI 생성물의 특징과 그로 인해 발생할 수 있는 위험성을 직관적으로 이해할 수 있습니다.

        운영 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 10분입니다. 참여 대상은 초등학생 이상이며, 현장접수와 사전예약 모두 가능합니다.

        잠시 후 ‘AI가 만든 가짜를 잡아라’ 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "반짝반짝 스튜디오 : 반짝반짝 작은LED" -> """
        지금 이동 중인 부스는 ‘반짝반짝 스튜디오 : 반짝반짝 작은 LED’입니다.

        이 프로그램은 작은 LED 모듈과 간단한 코딩을 활용해 ‘빛나는 나만의 라이트템’을 직접 만들어보는 체험입니다. 참가자는 키트를 선택하고 부품을 조립한 뒤, LED에 들어갈 문구나 패턴을 직접 코딩해 완성하는 방식으로 진행됩니다. 손으로 조립하고, 문자 코딩으로 불빛을 구현하며 IoT 기초 원리를 자연스럽게 배울 수 있다는 점이 특징입니다.

        운영 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 체험 시간은 약 10분 내외입니다. 참여 대상은 제한 없이 누구나 자유롭게 참여할 수 있습니다.

        잠시 후 ‘반짝반짝 스튜디오 : 반짝반짝 작은 LED’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "네모의방, 디스플레이의 꿈 : 마음을 비추는 창문" -> """
        지금 이동 중인부스는 ‘네모의 방, 디스플레이의 꿈 : 마음을 비추는 창문’입니다.

        이 프로그램은 관람객의 감정과 생각을 AI가 이미지로 변환해, ‘창문 형태의 디스플레이’에 비춰주는 감성 인터랙티브 체험입니다. 참가자는 짧은 질문에 답하거나 자신의 감정을 직접 적어 입력하게 되며, 그 문장이 AI를 통해 시각적 이미지로 변화되어 방 안의 창문 디스플레이에 나타납니다. 마치 마음속 풍경이 창 밖으로 펼쳐지는 듯한 독특한 연출을 경험할 수 있는 것이 특징입니다.

        운영 기간은 11월 26일부터 29일까지, 운영 시간은 오전 10시부터 오후 5시까지이며, 체험 시간은 약 10분 내외입니다. 참여 대상은 누구나 자유롭게 참여 가능합니다.

        잠시 후 ‘네모의 방, 디스플레이의 꿈 : 마음을 비추는 창문’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "차세대디스플레이 FAB : 디스플레이 공정속으로" -> """
        지금 이동 중인 부스는 ‘차세대디스플레이 FAB : 디스플레이 공정 속으로’입니다.

        이 프로그램은 디스플레이가 만들어지는 전 과정을 실제 공정 규모와 동일한 환경에서 체험할 수 있도록 구성된 기술 체험형 전시입니다. 6.5세대(6.5G) 디스플레이 제조 공정을 기반으로, 증착기 내부 구조와 패널 형성 과정 등을 현실감 있게 재현하여 관람객이 공정 라인을 직접 둘러보듯 이해할 수 있는 것이 특징입니다.

        참가자는 디스플레이의 핵심 제조 단계가 어떻게 진행되는지 눈으로 확인하고, 복잡한 공정이 ‘빛나는 화면’으로 완성되기까지의 기술 원리를 생생하게 경험할 수 있습니다.

        운영 기간은 11월 26일부터 29일, 운영 시간은 오전 10시부터 오후 5시까지이며, 체험 시간은 약 10분 내외입니다. 참여 대상은 누구나 자유롭게 참여 가능합니다.

        잠시 후 ‘차세대디스플레이 FAB : 디스플레이 공정 속으로’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "티프레임 스냅샷 : 테이크어샷 포토슛 팝팝팝!" -> """
        지금 이동 중인 부스는 ‘티프레임 스냅샷 : 테이크어샷 포토슛 팝팝팝!’ 입니다.

        이 프로그램은 투명한 T-OLED 액자 속에 다양한 이미지를 띄워놓고, 그 뒤에서 사진을 촬영하는 방식으로 진행되는 미래형 포토 체험입니다. 전원이 꺼지면 유리처럼 투명하지만, 켜지는 순간 생생한 화면을 구현하는 투명 OLED 기술을 활용해, 공중에 떠 있는 듯한 특별한 인생샷을 찍을 수 있는 것이 특징입니다.

        원하는 이미지를 선택한 뒤 투명 디스플레이 뒤에 서기만 하면, 빛과 화면이 하나가 되는 독특한 촬영 컷이 완성됩니다. 차세대 디스플레이 기술을 ‘직접 찍고 남기는 사진’으로 경험해볼 수 있는 체험입니다.

        운영 기간은 11월 26일부터 29일, 체험 시간은 오전 10시부터 오후 5시까지이며, 소요 시간은 약 5분입니다. 참여 대상은 누구나 자유롭게 참여할 수 있습니다.

        잠시 후 ‘티프레임 스냅샷 : 테이크어샷 포토슛 팝팝팝!’ 부스에 도착합니다. 도착 후 스태프 안내에 따라 촬영을 진행해 주세요.
    """.trimIndent()

            "센서가 듣고, AI가 생각한다" -> """
        지금 이동 중인 부스는 ‘센서가 듣고, AI가 생각한다’ 입니다.

        이 프로그램은 센서-데이터-AI-로봇이 하나의 흐름으로 연결되는 미래형 인터랙션을 직접 체험할 수 있도록 구성된 실험형 부스입니다. 체험자의 심장 소리(심박)와 음성 명령을 센서가 읽어들이고, AI가 그 정보를 분석해 로봇팔이 바로 동작하는 과정을 눈앞에서 확인할 수 있다는 점이 특징입니다.

        운영 기간은 11월 26일부터 29일까지, 체험 시간은 오전 10시부터 오후 5시까지이며
        소요 시간은 약 5분입니다. 참여 대상은 제한 없이 누구나 참여할 수 있습니다.

        잠시 후 ‘센서가 듣고, AI가 생각한다’ 체험존에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "스마트양식 디지털 트윈 체험: 모바일 로봇과 수질센서를 활용한 자율 환경 관리" -> """
        지금 이동 중인 부스는 ‘스마트양식 디지털 트윈 체험: 모바일 로봇과 수질센서를 활용한 자율 환경 관리’ 입니다.

        이 프로그램은 스마트양식 환경을 디지털 트윈으로 구현하여, 실제 양식장의 수질 변화·환경 데이터를 IoT 센서로 수집하고 시각화하는 과정을 직접 확인할 수 있는 체험입니다. 제주도 특화 수질 데이터를 모사한 센서를 기반으로, 참가자는 모바일 로봇을 원격 제어하며 자율 환경 관리가 어떻게 이루어지는지 경험할 수 있습니다.

        운영 기간은 11월 26일부터 29일까지이며,
        운영 시간은 오전 10시부터 오후 5시까지입니다.
        소요 시간은 설명 3분, 체험 약 5분으로 진행되며, 참여 대상은 중학생 이상입니다.

        잠시 후 ‘스마트양식 디지털 트윈 체험’ 부스에 도착합니다.
        도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            "공감 조명: 오늘 너의 색은?" -> """
        지금 이동하실 부스는 ‘공감 조명: 오늘 너의 색은?’ 입니다.

        이 프로그램은 AI 음성 인식과 감정 분석 기술을 활용해, 참가자의 말 한마디로 전구의 색상과 밝기가 즉시 변화하는 인터랙티브 조명 체험입니다. 현재 기분을 말하면 AI가 감정을 분석하여, 그 감정을 표현하는 색을 조명에 바로 반영합니다. 감정 데이터를 빛으로 시각화해 보는 독특한 경험을 제공하는 것이 특징입니다.

        운영 기간은 11월 26일부터 29일까지이며, 운영 시간은 오전 10시부터 오후 5시까지, 체험 소요 시간은 약 5분입니다. 참여 대상에 제한이 없어 누구나 자유롭게 참여하실 수 있습니다.

        잠시 후, ‘공감 조명: 오늘 너의 색은?’ 체험 부스에 도착합니다. 도착 후 스태프 안내에 따라 체험을 진행해 주세요.
    """.trimIndent()

            else -> """
    지금 이동 중인 부스는 ${item.title}입니다.

    ${item.description}

    체험 기간은 ${item.periodRangestart}부터 ${item.periodRangeend}까지,
    체험 시간은 ${item.periodstart}부터 ${item.periodend}까지이며,
    예상 소요 시간은 약 ${item.time}분입니다.

    참여 대상은 ${item.target}이며,
    체험 방법은 ${
                if (item.how.isBlank()) "현장 안내를 참고해 주세요."
                else item.how
            } 입니다.

    잠시 후, ${item.title} 부스에 도착합니다.
    도착 후 스태프 안내에 따라 체험을 진행해 주세요.
""".trimIndent()
        }
    }
}