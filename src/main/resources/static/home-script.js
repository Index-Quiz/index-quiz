// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    initAnimations();
    setupBookmarkTabs();
    setupParallaxEffect();
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
        // 클릭 이벤트
        tab.addEventListener('click', (e) => {
            const setName = tab.dataset.set;
            console.log(`${setName}-SET 선택됨`);

            // 클릭 애니메이션
            const content = tab.querySelector('.bookmark-content');
            content.style.transform = 'translateX(15px) scale(0.98)';

            setTimeout(() => {
                window.location.href = `quiz.html?set=${setName}`;
            }, 200);
        });

        // 호버 시 아이콘 애니메이션
        tab.addEventListener('mouseenter', () => {
            const icon = tab.querySelector('.bookmark-icon');
            icon.style.transition = 'all 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
            icon.style.transform = 'rotate(10deg) scale(1.1)';

            // 뱃지 애니메이션
            const badges = tab.querySelectorAll('.badge-level, .badge-count');
            badges.forEach((badge, index) => {
                setTimeout(() => {
                    badge.style.transform = 'scale(1.05)';
                }, index * 50);
            });
        });

        tab.addEventListener('mouseleave', () => {
            const icon = tab.querySelector('.bookmark-icon');
            icon.style.transform = 'rotate(0deg) scale(1)';

            const badges = tab.querySelectorAll('.badge-level, .badge-count');
            badges.forEach(badge => {
                badge.style.transform = 'scale(1)';
            });
        });

        // 마우스 이동에 따른 효과
        tab.addEventListener('mousemove', (e) => {
            const rect = tab.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const centerX = rect.width / 2;
            const centerY = rect.height / 2;

            const rotateX = (y - centerY) / 20;
            const rotateY = (centerX - x) / 20;

            const content = tab.querySelector('.bookmark-content');
            content.style.transform = `
                translateX(20px) 
                perspective(1000px) 
                rotateX(${rotateX}deg) 
                rotateY(${rotateY}deg)
            `;
        });

        tab.addEventListener('mouseleave', () => {
            const content = tab.querySelector('.bookmark-content');
            content.style.transform = 'translateX(0) perspective(1000px) rotateX(0) rotateY(0)';
        });
    });
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
