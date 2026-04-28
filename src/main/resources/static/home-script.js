// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    // ✅ 저장된 진행 상황이 있으면 quiz.html로 리다이렉션
    const savedProgress = localStorage.getItem('quizProgress');
    if (savedProgress) {
        const data = JSON.parse(savedProgress);
        window.location.href = `quiz.html?set=${data.quizSet}`;
        return; // 나머지 초기화 중단
    }
    initAnimations();
    setupBookmarkTabs();
    setupLearnModal();
    setupParallaxEffect();
    loadQuestionSetAverages();
    console.log('Index Quiz 초기화 완료');
});

// 스크롤 기반 애니메이션 초기화
function initAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -100px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry, index) => {
            if (entry.isIntersecting) {
                setTimeout(() => {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }, index * 100); // 순차적 애니메이션
            }
        });
    }, observerOptions);

    // 책갈피 탭 애니메이션
    const bookmarkTabs = document.querySelectorAll('.bookmark-tab');
    bookmarkTabs.forEach((tab, index) => {
        tab.style.opacity = '0';
        tab.style.transform = 'translateY(30px)';
        tab.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(tab);
    });
}

// 책갈피 탭 설정
function setupBookmarkTabs() {
    const bookmarkTabs = document.querySelectorAll('.bookmark-tab');

    bookmarkTabs.forEach(tab => {
        const setName = tab.dataset.set;
        const hasActions = tab.querySelector('.bookmark-actions');

        // 클릭 이벤트 - 액션 버튼이 없는 탭(BEST_DIFFICULT)은 바로 퀴즈 이동
        tab.addEventListener('click', (e) => {
            if (hasActions && (e.target.closest('.action-btn') || e.target.closest('.bookmark-actions'))) {
                return; // 액션 버튼 클릭은 별도 처리
            }
            if (!hasActions) {
                console.log(`${setName}-SET 선택됨`);
                const content = tab.querySelector('.bookmark-content');
                content.style.transform = 'translateX(15px) scale(0.98)';
                setTimeout(() => {
                    window.location.href = `quiz.html?set=${setName}`;
                }, 200);
            }
        });

        // 액션 버튼 클릭 처리
        if (hasActions) {
            const quizBtn = tab.querySelector('.action-btn-quiz');
            const learnBtn = tab.querySelector('.action-btn-learn');

            quizBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                window.location.href = `quiz.html?set=${setName}`;
            });

            learnBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                openLearnModal(setName);
            });
        }

        // 호버 시 아이콘 애니메이션
        tab.addEventListener('mouseenter', () => {
            const icon = tab.querySelector('.bookmark-icon');
            icon.style.transition = 'all 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
            icon.style.transform = 'rotate(10deg) scale(1.1)';
        });

        tab.addEventListener('mouseleave', () => {
            const icon = tab.querySelector('.bookmark-icon');
            icon.style.transform = 'rotate(0deg) scale(1)';
        });

    });
}

// 세트별 학습자료 목록 (추후 API에서 받아올 데이터)
const learnMaterials = {
    A: { icon: '📚', title: '인덱스 기초 1', subtitle: '#해쉬 인덱스 #B-Tree', materials: [
        { id: 1, title: '인덱스는 왜 필요한가?', desc: '풀 테이블 스캔의 문제점과 인덱스의 역할' },
        { id: 2, title: '해쉬 인덱스의 구조와 특징', desc: '해쉬 함수 기반 인덱스의 장단점 분석' },
        { id: 3, title: 'B-Tree 인덱스 완전 정복', desc: '루트, 브랜치, 리프 노드의 역할과 탐색 과정' },
    ]},
    B: { icon: '🎯', title: '인덱스 기초 2', subtitle: '#클러스터링 인덱스 #내부 구조', materials: [
        { id: 4, title: '클러스터링 인덱스란?', desc: 'PK와 데이터 정렬의 관계' },
        { id: 5, title: '세컨더리 인덱스와 테이블 룩업', desc: '논클러스터링 인덱스의 내부 동작 이해' },
        { id: 6, title: '클러스터링 vs 논클러스터링 비교', desc: '두 인덱스 타입의 성능 차이와 사용 시나리오' },
    ]},
    C: { icon: '📦', title: '인덱스 활용 1', subtitle: '#레인지 스캔 #Left-Most', materials: [
        { id: 7, title: '인덱스 레인지 스캔', desc: '범위 검색에서 인덱스가 동작하는 방식' },
        { id: 8, title: 'Left-Most 규칙의 이해', desc: '복합 인덱스에서 왼쪽 컬럼부터 사용해야 하는 이유' },
    ]},
    D: { icon: '🧩', title: '인덱스 활용 2', subtitle: '#AND #OR #IN절', materials: [
        { id: 9, title: 'AND 조건과 인덱스 활용', desc: '복합 인덱스로 AND 조건 최적화하기' },
        { id: 10, title: 'OR 조건의 인덱스 제약', desc: 'OR 조건이 인덱스 사용을 방해하는 이유' },
        { id: 11, title: 'IN절의 내부 최적화', desc: 'IN절이 등호 비교로 처리되는 메커니즘' },
    ]},
    E: { icon: '⚡', title: '인덱스 응용 1', subtitle: '#복합 인덱스 #루스 스캔', materials: [
        { id: 12, title: '복합 인덱스 설계 전략', desc: '카디널리티와 컬럼 순서 최적화' },
        { id: 13, title: '루스 인덱스 스캔', desc: 'GROUP BY 최적화를 위한 루스 스캔 기법' },
    ]},
    F: { icon: '⏭️', title: '인덱스 응용 2', subtitle: '#커버링 인덱스 #스킵 스캔', materials: [
        { id: 14, title: '커버링 인덱스', desc: '테이블 룩업 없이 인덱스만으로 쿼리 처리' },
        { id: 15, title: '스킵 스캔 최적화', desc: 'MySQL 8.0의 스킵 스캔 동작 조건과 원리' },
    ]},
    G: { icon: '🔢', title: '인덱스 고급 1', subtitle: '#정렬 #ICP', materials: [
        { id: 16, title: 'ORDER BY와 인덱스', desc: '인덱스를 활용한 정렬 최적화' },
        { id: 17, title: 'Index Condition Pushdown', desc: '스토리지 엔진 레벨 조건 평가로 I/O 절감' },
    ]},
    H: { icon: '🔀', title: '인덱스 고급 2', subtitle: '#페이지 분할 #함수 인덱스 #인덱스 머지', materials: [
        { id: 18, title: '페이지 분할과 쓰기 성능', desc: 'B-Tree 리프 페이지 분할이 발생하는 조건' },
        { id: 19, title: '함수 기반 인덱스', desc: 'MySQL 8.0 표현식 인덱스 생성과 활용' },
        { id: 20, title: '인덱스 머지 최적화', desc: '여러 인덱스를 결합하는 최적화 전략' },
    ]}
};

// 학습자료 모달 열기
function openLearnModal(setName) {
    const data = learnMaterials[setName];
    if (!data) return;

    document.getElementById('modalIcon').textContent = data.icon;
    document.getElementById('modalTitle').textContent = data.title;
    document.getElementById('modalSubtitle').textContent = data.subtitle + ' · 학습자료 선택';

    const tabList = document.getElementById('learnTabList');
    tabList.innerHTML = data.materials.map((m, i) =>
        `<a class="learn-tab-item" href="learn.html?set=${setName}&id=${m.id}">
            <div class="learn-tab-number">${i + 1}</div>
            <div class="learn-tab-info">
                <div class="learn-tab-title">${m.title}</div>
                <div class="learn-tab-desc">${m.desc}</div>
            </div>
            <div class="learn-tab-arrow">\u2192</div>
        </a>`
    ).join('');

    document.getElementById('modalQuizBtn').href = `quiz.html?set=${setName}`;

    const overlay = document.getElementById('learnModal');
    overlay.classList.add('active');
    document.body.style.overflow = 'hidden';
}

// 학습자료 모달 닫기
function closeLearnModal() {
    document.getElementById('learnModal').classList.remove('active');
    document.body.style.overflow = '';
}

// 모달 이벤트 바인딩
function setupLearnModal() {
    const overlay = document.getElementById('learnModal');
    if (!overlay) return;

    document.getElementById('modalCloseBtn').addEventListener('click', closeLearnModal);
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) closeLearnModal();
    });
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closeLearnModal();
    });
}

// 세트별 평균 점수 로드
async function loadQuestionSetAverages() {
    try {
        const response = await fetch('/api/user-answers/results/averages');
        if (!response.ok) return;
        const data = await response.json();
        const averages = data.averages;

        document.querySelectorAll('.bookmark-tab').forEach(tab => {
            const setName = tab.dataset.set;
            if (averages[setName] == null) return;

            const badge = tab.querySelector('.bookmark-badge');
            const avgDiv = document.createElement('div');
            avgDiv.className = 'bookmark-avg';
            avgDiv.innerHTML = `<span class="avg-label">평균</span><span class="avg-value">${averages[setName]}점</span>`;
            badge.appendChild(avgDiv);
        });
    } catch (e) {
        console.error('평균 점수 로드 실패:', e);
    }
}

// 패럴랙스 효과
function setupParallaxEffect() {
    window.addEventListener('scroll', throttle(() => {
        const scrolled = window.pageYOffset;

        // 배경 원형 요소 패럴랙스
        const circles = document.querySelectorAll('.floating-circle');
        circles.forEach((circle, index) => {
            const speed = 0.1 + (index * 0.05);
            const yPos = scrolled * speed;
            circle.style.transform = `translate(${Math.sin(scrolled * 0.01) * 20}px, ${yPos}px)`;
        });

        // 로고 아이콘 패럴랙스
        const logoIcon = document.querySelector('.logo-icon');
        if (logoIcon) {
            const yPos = scrolled * 0.15;
            logoIcon.style.transform = `translateY(${yPos}px) rotate(${scrolled * 0.1}deg)`;
        }
    }, 16));
}

// 쓰로틀 함수 (성능 최적화)
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

// 마우스 커서 따라다니는 효과 (선택적)
document.addEventListener('mousemove', throttle((e) => {
    const circles = document.querySelectorAll('.floating-circle');
    circles.forEach((circle, index) => {
        const speed = (index + 1) * 0.01;
        const x = (e.clientX * speed);
        const y = (e.clientY * speed);

        circle.style.transform += ` translate(${x}px, ${y}px)`;
    });
}, 30));

// 키보드 접근성 향상
document.addEventListener('keydown', (e) => {
    const bookmarkTabs = document.querySelectorAll('.bookmark-tab');
    const focusedElement = document.activeElement;
    const currentIndex = Array.from(bookmarkTabs).indexOf(focusedElement);

    if (currentIndex !== -1) {
        let nextIndex;

        switch(e.key) {
            case 'ArrowDown':
                e.preventDefault();
                nextIndex = (currentIndex + 1) % bookmarkTabs.length;
                bookmarkTabs[nextIndex].focus();
                break;
            case 'ArrowUp':
                e.preventDefault();
                nextIndex = (currentIndex - 1 + bookmarkTabs.length) % bookmarkTabs.length;
                bookmarkTabs[nextIndex].focus();
                break;
            case 'Enter':
                e.preventDefault();
                focusedElement.click();
                break;
        }
    }
});

// 탭에 포커스 가능하도록 설정
document.querySelectorAll('.bookmark-tab').forEach(tab => {
    tab.setAttribute('tabindex', '0');
    tab.setAttribute('role', 'button');
    tab.setAttribute('aria-label', `${tab.dataset.set} 세트 퀴즈 시작`);
});

// 페이지 로드 완료 애니메이션
window.addEventListener('load', () => {
    document.body.style.opacity = '1';

    // 헤더가 완전히 표시되도록 보장
    const header = document.querySelector('header');
    if (header) {
        header.style.opacity = '1';
    }

    console.log('모든 리소스 로드 완료');
});
