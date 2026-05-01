let currentMaterial = null;
let materialsList = [];
let currentSet = '';
let currentId = 0;

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    currentSet = urlParams.get('set') || 'A';
    currentId = parseInt(urlParams.get('id')) || 1;

    initLearnPage();
});

async function initLearnPage() {
    try {
        const [material, listResponse] = await Promise.all([
            fetchLearnMaterial(currentId),
            fetchLearnMaterialsBySet(currentSet)
        ]);

        currentMaterial = material;
        materialsList = listResponse.materials;

        renderHeader();
        renderContent();
        renderNavigation();
        renderQuizFloatingBtn();
        revealPage();
    } catch (error) {
        console.error('학습자료 로드 실패:', error);
        showError();
    }
}

async function fetchLearnMaterial(id) {
    const response = await fetch(`/api/learn-materials/${id}`);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

async function fetchLearnMaterialsBySet(set) {
    const response = await fetch(`/api/learn-materials?set=${set}`);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

function renderHeader() {
    const header = document.getElementById('learnHeader');
    const badge = document.getElementById('learnSetBadge');
    const title = document.getElementById('learnTitle');
    const description = document.getElementById('learnDescription');

    const currentIndex = materialsList.findIndex(m => m.id === currentMaterial.id);
    const orderLabel = currentIndex >= 0 ? `${currentIndex + 1} / ${materialsList.length}` : '';

    badge.textContent = `${currentSet} 세트 · ${orderLabel}`;
    title.textContent = currentMaterial.title;
    description.textContent = currentMaterial.description;

    header.style.opacity = '0';
    header.style.transform = 'translateY(-10px)';
}

function renderContent() {
    const contentEl = document.getElementById('learnContent');
    contentEl.innerHTML = parseMarkdownToHtml(currentMaterial.content);
    bindImageEvents(contentEl);
    contentEl.style.opacity = '0';
    contentEl.style.transform = 'translateY(20px)';
}

function renderNavigation() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    const currentIndex = materialsList.findIndex(m => m.id === currentMaterial.id);
    const prevMaterial = currentIndex > 0 ? materialsList[currentIndex - 1] : null;
    const nextMaterial = currentIndex < materialsList.length - 1 ? materialsList[currentIndex + 1] : null;

    if (prevMaterial) {
        prevBtn.href = `learn.html?set=${currentSet}&id=${prevMaterial.id}`;
        prevBtn.classList.remove('disabled');
    } else {
        prevBtn.removeAttribute('href');
        prevBtn.classList.add('disabled');
    }

    if (nextMaterial) {
        nextBtn.href = `learn.html?set=${currentSet}&id=${nextMaterial.id}`;
        nextBtn.classList.remove('disabled');
    } else {
        nextBtn.removeAttribute('href');
        nextBtn.classList.add('disabled');
    }
}

function renderQuizFloatingBtn() {
    const btn = document.getElementById('quizFloatingBtn');
    if (!btn || !currentSet) return;
    btn.href = `quiz.html?set=${currentSet}`;
    btn.style.display = 'flex';
    initScrollAttention(btn);
}

function initScrollAttention(btn) {
    window.addEventListener('scroll', () => {
        const scrollRatio = window.scrollY / (document.documentElement.scrollHeight - window.innerHeight);
        if (scrollRatio >= 0.7) {
            btn.classList.add('quiz-floating-attention');
        } else {
            btn.classList.remove('quiz-floating-attention');
        }
    });
}

function revealPage() {
    const header = document.getElementById('learnHeader');
    const contentEl = document.getElementById('learnContent');
    const nav = document.getElementById('learnNav');

    nav.style.opacity = '0';
    nav.style.transform = 'translateY(10px)';

    requestAnimationFrame(() => {
        header.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        header.style.opacity = '1';
        header.style.transform = 'translateY(0)';

        setTimeout(() => {
            contentEl.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            contentEl.style.opacity = '1';
            contentEl.style.transform = 'translateY(0)';
        }, 150);

        setTimeout(() => {
            nav.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            nav.style.opacity = '1';
            nav.style.transform = 'translateY(0)';
        }, 350);
    });
}

function showError() {
    const contentEl = document.getElementById('learnContent');
    contentEl.innerHTML = `
        <div class="learn-error">
            <p>학습자료를 불러올 수 없습니다.</p>
            <a href="index.html" class="learn-error-btn">홈으로 돌아가기</a>
        </div>
    `;
}
