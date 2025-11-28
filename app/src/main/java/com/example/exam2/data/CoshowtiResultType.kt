package com.example.exam2.data

import com.example.exam2.InformationByThemeActivity
import com.example.exam2.R

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

data class CoshowtiResultType(
    val code: String,
    val category: String,
    val name: String,
    val description: String,
    val booths: List<ThemeItem>
)

object CoshowtiResults {
    private val resultMap = mapOf(
        // 모빌리티형 (Mobility Type)
        "MNAE" to CoshowtiResultType(
            code = "MNAE",
            category = "모빌리티형",
            name = "움직임으로 세상을 바꾸는 혁신가",
            description = "세상을 바꾸는 방법을 머리로만 생각하지 않는다.\n직접 손대고 실험하며 새로운 이동 방식을 만들어낸다.",
            booths = listOf(
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
                )
            )
        ),
        "MNLE" to CoshowtiResultType(
            code = "MNLE",
            category = "모빌리티형",
            name = "인간 중심 이동을 설계하는 연구자",
            description = "기술이 사람의 안전과 편안함 속에서 작동해야 한다고 믿는다.\n작은 디테일까지 신경 쓰는 설계형.",
            booths = listOf(
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
                )
            )
        ),
        "MBAE" to CoshowtiResultType(
            code = "MBAE",
            category = "모빌리티형",
            name = "직접 만지고 실험하며 배우는 탐구가",
            description = "'직접 해보는 것'이 최고의 배움이라 생각한다.\n실패도 실험의 일부라 여기며 반복을 즐긴다.",
            booths = listOf(
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
                )
            )
        ),
        "MBLE" to CoshowtiResultType(
            code = "MBLE",
            category = "모빌리티형",
            name = "만드는 과정을 즐거워하는 창작자",
            description = "완성보다 과정이 중요하다.\n손끝의 움직임 속에서 아이디어가 살아난다고 느낀다.",
            booths = listOf(
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
                )
            )
        ),

        // 그린테크형 (Green Tech Type)
        "MNAS" to CoshowtiResultType(
            code = "MNAS",
            category = "그린테크형",
            name = "지속가능한 해답을 찾는 실천가",
            description = "환경 문제를 단순한 담론이 아닌 기술로 풀어내려 한다.\n작더라도 '지속 가능한 변화'를 만드는 데 의미를 둔다.",
            booths = listOf(
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
                )
            )
        ),
        "MNLS" to CoshowtiResultType(
            code = "MNLS",
            category = "그린테크형",
            name = "기술로 움직임을 회복시키는 연구자",
            description = "기술은 사람을 대체하는 것이 아니라 도와야 한다고 생각한다.\n보조와 회복, 인간 중심의 접근을 중시한다.",
            booths = listOf(
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
                )
            )
        ),
        "DNAS" to CoshowtiResultType(
            code = "DNAS",
            category = "그린테크형",
            name = "데이터로 환경을 개선하는 기술가",
            description = "환경 변화 속 숫자와 패턴을 읽어낸다.\n데이터를 통해 문제를 분석하고 더 나은 방향을 설계한다.",
            booths = listOf(
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
                )
            )
        ),
        "DNLS" to CoshowtiResultType(
            code = "DNLS",
            category = "그린테크형",
            name = "자연의 질서를 연구하는 분석가",
            description = "자연의 구조와 흐름 속에서 기술의 원리를 찾는다.\n인간과 생태의 균형을 수학적으로 이해하려 한다.",
            booths = listOf(
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
                )
            )
        ),

        // 스마트소재형 (Smart Material Type)
        "MBAS" to CoshowtiResultType(
            code = "MBAS",
            category = "스마트소재형",
            name = "정밀한 구조를 설계하는 엔지니어",
            description = "복잡한 시스템을 세밀히 쪼개어 이해하고, 정확한 계산으로 설계한다.\n완벽한 구조 속에서 안정감을 느낀다.",
            booths = listOf(
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
                )
            )
        ),
        "MBLS" to CoshowtiResultType(
            code = "MBLS",
            category = "스마트소재형",
            name = "완벽한 균형을 만드는 기술자",
            description = "기술도 예술처럼 조화를 이뤄야 한다고 믿는다.\n형태, 무게, 흐름의 균형을 잡는 데 집중한다.",
            booths = listOf(
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
                )
            )
        ),
        "DBLS" to CoshowtiResultType(
            code = "DBLS",
            category = "스마트소재형",
            name = "정보를 체계적으로 다루는 설계자",
            description = "직관보다 근거를 중시한다.\n숫자와 로직으로 가장 효율적인 해답을 찾아낸다.",
            booths = listOf(
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
                )
            )
        ),

        // 디지털지능형 (Digital Intelligence Type)
        "DBAS" to CoshowtiResultType(
            code = "DBAS",
            category = "디지털지능형",
            name = "데이터를 기반으로 구조를 설계하는 전략가",
            description = "혼란 속에서도 규칙을 발견한다.\n복잡한 정보를 정리하며 구조화하는 과정 자체를 즐긴다.",
            booths = listOf(
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
                )
            )
        ),
        "DNLE" to CoshowtiResultType(
            code = "DNLE",
            category = "디지털지능형",
            name = "기술로 인간을 이해하는 연구자",
            description = "감정과 데이터를 함께 다룬다.\n기술이 사람을 '이해할 수 있는 존재'가 되길 바란다.",
            booths = listOf(
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
                )
            )
        ),
        "DBAE" to CoshowtiResultType(
            code = "DBAE",
            category = "디지털지능형",
            name = "새로운 연결을 만드는 실험가",
            description = "분야의 경계를 넘나들며 실험한다.\n기술, 미디어, 예술이 만나는 지점을 찾는 걸 즐긴다.",
            booths = listOf(
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
                )
            )
        ),
        "DNAE" to CoshowtiResultType(
            code = "DNAE",
            category = "디지털지능형",
            name = "사람과 지구를 잇는 따뜻한 혁신가",
            description = "기술의 발전보다 그로 인해 더 나은 세상이 만들어지는지를 본다.\n사람을 위한 변화를 꿈꾼다.",
            booths = listOf(
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
                )
            )
        ),
        "DBLE" to CoshowtiResultType(
            code = "DBLE",
            category = "디지털지능형",
            name = "감각으로 세상을 표현하는 디자이너",
            description = "시각과 청각, 감정을 기술로 재해석한다.\n데이터 속에서 아름다움을 발견하는 사람.",
            booths = listOf(
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
                    title = "Scent Memory",
                    category = "빅데이터",
                    imageResId = R.drawable.img4,
                    detailImageResId = R.drawable.img4_1,
                    periodstart = "10:00",
                    periodend = "17:00",
                    time = 5,
                    description = "참가자의 취향 정보를 입력하면, 빅데이터 기반 분석을 통해 어울리는 향수 레시피를 추천하 는 체험이다. 좋아하는 분위기, 이미지, 키워드 등 감성적인 정보가 데이터로 변환되어 향 조합으로 이어지는 과정을 볼 수 있다.\n" +
                            "추천 결과를 통해 취향 데이터가 어떻게 구조화되고, 어떤 기준으로 향료를 조합하는지 이해 할 수 있다. 감각적인 영역도 데이터 분석을 통해 정교하게 다룰 수 있다는 점을 보여주는 프로그램이다.",
                    how = "현장접수",
                    target = "초•중•고 학생 및 일반 관람객",
                    periodRangestart = "2025-11-26",
                    periodRangeend = "2025-11-29"
                )
            )
        )
    )

    fun getResult(code: String): CoshowtiResultType? {
        return resultMap[code]
    }

    fun calculateResultType(
        mScore: Int, dScore: Int,
        nScore: Int, bScore: Int,
        aScore: Int, lScore: Int,
        eScore: Int, sScore: Int
    ): String {
        val char1 = if (mScore > dScore) "M" else "D"
        val char2 = if (nScore > bScore) "N" else "B"
        val char3 = if (aScore > lScore) "A" else "L"
        val char4 = if (eScore > sScore) "E" else "S"

        return "$char1$char2$char3$char4"
    }
}
