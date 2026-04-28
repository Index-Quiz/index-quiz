// 목데이터 사용 여부 (개발 중에는 true, 프로덕션에서는 false)

// API를 통한 퀴즈 데이터 관리
let totalQuestions = 7; // 목데이터 사용 시 2문제, 실제 API는 10문제
let currentQuestion = null;

let bestDifficultQuestions = [];
let isBestDifficultMode = false;

const QuizStorage = {
    KEY: 'quizProgress',

    save(data) {
        localStorage.setItem(this.KEY, JSON.stringify(data));
    },

    load() {
        const data = localStorage.getItem(this.KEY);
        return data ? JSON.parse(data) : null;
    },

    clear() {
        localStorage.removeItem(this.KEY);
    }
};
// 저장할 데이터 구조: { quizSet, currentQuestionIndex, total, score }

// quiz.html의 홈 버튼에 이벤트 리스너 추가
document.querySelector('.home-btn').addEventListener('click', (e) => {
    e.preventDefault();

    const savedProgress = QuizStorage.load();

    if (savedProgress) {
        const confirmed = confirm('풀던 기록이 모두 삭제됩니다. 홈으로 이동하시겠습니까?');
        if (confirmed) {
            QuizStorage.clear();
            window.location.href = 'index.html';
        }
    } else {
        window.location.href = 'index.html';
    }
});

// API 호출 함수
async function fetchQuestion(questionId) {
    // 실제 API 호출
    try {
        const response = await fetch(`/api/questions/${questionId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const questionData = await response.json();
        return questionData;
    } catch (error) {
        console.error('퀴즈 데이터를 불러오는 중 오류가 발생했습니다:', error);
        throw error;
    }
}

// BEST_DIFFICULT 문제 목록 가져오기 - 새로 추가
async function fetchBestDifficultQuestions() {
    try {
        const response = await fetch('/api/questions?type=BEST_DIFFICULT');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data.questions;
    } catch (error) {
        console.error('BEST_DIFFICULT 퀴즈 데이터를 불러오는 중 오류가 발생했습니다:', error);
        throw error;
    }
}

// 답 제출 API 호출 함수 (다중 선택 지원)
async function submitAnswerToAPI(questionId, selectedAnswers) {
    // selectedAnswers 배열을 1부터 시작하는 인덱스로 변환
    const choices = selectedAnswers.map(optionId => optionId);

    // 실제 API 호출
    try {

        //저장 API 호출
        const response = await fetch(`/api/user-answers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                questionId: questionId,
                options: choices
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        responseSubmitId = result.submitId;

        // 채점 API 호출
        const response2 = await fetch(`/api/user-answers/${responseSubmitId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response2.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result2 = await response2.json();
        return result2; //{isCorrect, userOptions, answerOptions, solution}

    } catch (error) {
        console.error('답 제출 중 오류가 발생했습니다:', error);
        throw error;
    }
}

// SQL 키워드 하이라이팅 함수
function highlightSQL(code) {
    const sqlKeywords = [
        'SELECT', 'FROM', 'WHERE', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP', 'ALTER',
        'TABLE', 'INDEX', 'PRIMARY', 'KEY', 'FOREIGN', 'REFERENCES', 'CONSTRAINT',
        'AUTO_INCREMENT', 'NOT', 'NULL', 'DEFAULT', 'UNIQUE', 'CHECK',
        'AND', 'OR', 'IN', 'LIKE', 'BETWEEN', 'ORDER', 'BY', 'GROUP', 'HAVING',
        'LIMIT', 'OFFSET', 'JOIN', 'LEFT', 'RIGHT', 'INNER', 'OUTER', 'ON',
        'UNION', 'DISTINCT', 'AS', 'CASE', 'WHEN', 'THEN', 'ELSE', 'END',
        'COUNT', 'SUM', 'AVG', 'MIN', 'MAX', 'CONCAT', 'FLOOR', 'RAND',
        'VARCHAR', 'INT', 'BIGINT', 'DECIMAL', 'DATE', 'DATETIME', 'TIMESTAMP'
    ];

    let highlightedCode = code;

    // SQL 키워드 하이라이팅
    sqlKeywords.forEach(keyword => {
        const regex = new RegExp(`\\b${keyword}\\b`, 'gi');
        highlightedCode = highlightedCode.replace(regex, `<span class="sql-keyword">${keyword}</span>`);
    });

    // 문자열 하이라이팅 ('문자열')
    highlightedCode = highlightedCode.replace(/'([^']*)'/g, '<span class="sql-string">\'$1\'</span>');

    // 숫자 하이라이팅
    highlightedCode = highlightedCode.replace(/\b(\d+)\b/g, '<span class="sql-number">$1</span>');

    // 주석 하이라이팅 (-- 주석)
    highlightedCode = highlightedCode.replace(/(--.*$)/gm, '<span class="sql-comment">$1</span>');

    return highlightedCode;
}

// HTML 속성용 이스케이프 (data-label 등에 사용)
function escapeHtmlAttr(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

// 마크다운 텍스트를 HTML로 변환 (완전한 마크다운 지원)
function parseMarkdownToHtml(content) {
    let html = content;

    // 코드 블록 변환 (```언어\n코드\n``` 형식) - 먼저 처리하여 다른 변환과 충돌 방지
    html = html.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, language, code) => {
        const lang = language || '';
        const langClass = lang ? ` class="language-${lang}"` : '';

        // SQL 코드인 경우 하이라이팅 적용
        let processedCode = code.trim();
        if (lang.toLowerCase() === 'sql') {
            processedCode = highlightSQL(processedCode);
        }

        return `<div class="code-block">
            ${lang ? `<div class="code-header">${lang.toUpperCase()}</div>` : ''}
            <pre><code${langClass}>${processedCode}</code></pre>
        </div>`;
    });

    // 테이블 변환 (| 헤더 | 헤더 | 형식)
    html = html.replace(/(\|.*\|\n\|.*\|\n(?:\|.*\|\n?)*)/g, (match) => {
        const rows = match.trim().split('\n');
        if (rows.length < 2) return match;

        const headerRow = rows[0];
        const separatorRow = rows[1];
        const dataRows = rows.slice(2);

        // 헤더 파싱
        const headers = headerRow.split('|').map(h => h.trim()).filter(h => h);

        // 데이터 행 파싱 - 각 td에 data-label 속성으로 헤더 이름 주입 (모바일 카드형 레이아웃용)
        const dataRowsHtml = dataRows.map(row => {
            const cells = row.split('|').map(c => c.trim()).filter(c => c);
            const tdsHtml = cells.map((cell, i) => {
                const label = headers[i] !== undefined ? escapeHtmlAttr(headers[i]) : '';
                return `<td data-label="${label}">${cell}</td>`;
            }).join('');
            return `<tr>${tdsHtml}</tr>`;
        }).join('');

        return `<div class="table-container">
            <table class="markdown-table">
                <thead>
                    <tr>${headers.map(header => `<th>${header}</th>`).join('')}</tr>
                </thead>
                <tbody>
                    ${dataRowsHtml}
                </tbody>
            </table>
        </div>`;
    });

    // 헤딩 변환 (## 헤딩)
    html = html.replace(/^### (.*$)/gm, '<h3 class="markdown-h3">$1</h3>');
    html = html.replace(/^## (.*$)/gm, '<h2 class="markdown-h2">$1</h2>');
    html = html.replace(/^# (.*$)/gm, '<h1 class="markdown-h1">$1</h1>');

    // 구분선 변환 (---)
    html = html.replace(/^---$/gm, '<hr class="markdown-divider">');

    // 이미지 변환 (onclick 제거 - 이벤트 리스너에서 처리)
    html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g,
        '<div class="image-container"><img src="$2" alt="$1" class="question-image" onerror="this.style.display=\'none\'; this.nextElementSibling.style.display=\'block\';" onload="this.style.opacity=\'1\';" style="opacity: 0; transition: opacity 0.5s ease; cursor: pointer;"><div class="image-placeholder" style="display: none;">📷 이미지를 불러올 수 없습니다</div></div>'
    );

    // 마크다운 링크 변환 (공백 허용)
    html = html.replace(/\[([^\]]+)\]\(\s*([^\)]+)\s*\)/g, (match, text, url) => {
        return `<a href="${url}" class="markdown-link" target="_blank" rel="noopener noreferrer">${text}</a>`;
    });

    // 인라인 코드 변환 (`코드` 형식)
    html = html.replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>');

    // 볼드 텍스트 변환
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong class="markdown-bold">$1</strong>');

    // details/summary 변환 (마우스 인터랙션 있는 summary 적용)
    html = html.replace(/<details>\s*<summary>(.*?)<\/summary>/g, (match, summaryText) => {
        return `<details><summary class="explanation-summary">${summaryText}</summary>`;
    });

    // 줄바꿈을 단락으로 변환
    html = html.split('\n\n').map(paragraph => {
        paragraph = paragraph.trim();
        if (!paragraph) return '';

        // 이미 HTML 태그로 시작하는 경우 그대로 반환
        if (paragraph.startsWith('<h') || paragraph.startsWith('<div') ||
            paragraph.startsWith('<hr') || paragraph.startsWith('<table')) {
            return paragraph;
        }

        // 일반 텍스트는 p 태그로 감싸기
        return `<p class="markdown-paragraph">${paragraph.replace(/\n/g, '<br>')}</p>`;
    }).join('');

    return html;
}

// 퀴즈 상태 변수들
let quizStartId = 0;
let currentQuestionIndex = 0;
let quizSet = "A";
let score = 0;
let selectedAnswers = []; // 다중 선택 지원을 위해 배열로 변경
let isAnswered = false;

// DOM 요소들
const questionText = document.getElementById('questionText');
const optionsContainer = document.getElementById('optionsContainer');
const submitBtn = document.getElementById('submitBtn');
const resultContainer = document.getElementById('resultContainer');
const resultMessage = document.getElementById('resultMessage');
const explanation = document.getElementById('explanation');
const nextBtn = document.getElementById('nextBtn');
const quizContainer = document.getElementById('quizContainer');
const finalResult = document.getElementById('finalResult');
const currentQuestionSpan = document.getElementById('currentQuestion');
const totalQuestionsSpan = document.getElementById('totalQuestions');
const progress = document.getElementById('progress');
const finalScore = document.getElementById('finalScore');
const finalTotal = document.getElementById('finalTotal');
const scorePercentage = document.getElementById('scorePercentage');

// 로딩 상태 표시
function showLoading() {
    const loadingDiv = document.createElement('div');
    loadingDiv.id = 'loadingIndicator';
    loadingDiv.innerHTML = `
        <div style="display: flex; align-items: center; justify-content: center; padding: 20px;">
            <div style="border: 3px solid #f3f3f3; border-top: 3px solid #3498db; border-radius: 50%; width: 30px; height: 30px; animation: spin 1s linear infinite; margin-right: 10px;"></div>
            <span>퀴즈를 불러오는 중...</span>
        </div>
        <style>
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        </style>
    `;

    // 기존 로딩 인디케이터가 있으면 제거
    const existingLoading = document.getElementById('loadingIndicator');
    if (existingLoading) {
        existingLoading.remove();
    }

    questionText.appendChild(loadingDiv);
}

function hideLoading() {
    const loadingIndicator = document.getElementById('loadingIndicator');
    if (loadingIndicator) {
        loadingIndicator.remove();
    }
}

async function initQuiz() {
    console.log('initQuiz() 실행됨');

    // URL 파라미터에서 세트 읽기
    const urlParams = new URLSearchParams(window.location.search);
    quizSet = urlParams.get("set") || "A";

    // BEST_DIFFICULT 모드인지 확인
    isBestDifficultMode = (quizSet === "BEST_DIFFICULT");

    const savedProgress = QuizStorage.load();

    if (savedProgress && savedProgress.quizSet === quizSet) {
        if (isBestDifficultMode) {
            try {
                bestDifficultQuestions = await fetchBestDifficultQuestions();
                totalQuestions = bestDifficultQuestions.length;
                quizStartId = 0;
            } catch (error) {
                console.error('BEST_DIFFICULT 문제를 불러오는 데 실패했습니다:', error);
                alert('문제를 불러오는 데 실패했습니다. 홈으로 돌아갑니다.');
                window.location.href = 'index.html';
                return;
            }
        } else {
            quizStartId = (quizSet.charCodeAt(0) - "A".charCodeAt(0)) * 7;
        }

        currentQuestionIndex = savedProgress.currentQuestionIndex ?? (isBestDifficultMode ? 0 : quizStartId);
        totalQuestions = savedProgress.total ?? (isBestDifficultMode ? bestDifficultQuestions.length : 7);
        score = savedProgress.score;

        if (isBestDifficultMode) {
            if (currentQuestionIndex >= totalQuestions) {
                showFinalResult();
                return;
            }
        } else {
            if (currentQuestionIndex - quizStartId >= totalQuestions) {
                showFinalResult();
                return;
            }
        }
    } else {
        if (isBestDifficultMode) {
            try {
                bestDifficultQuestions = await fetchBestDifficultQuestions();
                totalQuestions = bestDifficultQuestions.length;
                quizStartId = 0;
                currentQuestionIndex = 0;
            } catch (error) {
                console.error('BEST_DIFFICULT 문제를 불러오는 데 실패했습니다:', error);
                alert('문제를 불러오는 데 실패했습니다. 홈으로 돌아갑니다.');
                window.location.href = 'index.html';
                return;
            }
        } else {
            quizStartId = (quizSet.charCodeAt(0) - "A".charCodeAt(0)) * 7;
            currentQuestionIndex = quizStartId;
            totalQuestions = 7;
        }
        score = 0;
    }

    selectedAnswers = [];
    isAnswered = false;

    totalQuestionsSpan.textContent = totalQuestions;
    finalTotal.textContent = totalQuestions;

    const allTotalElements = document.querySelectorAll('[id*="totalQuestions"], [class*="total-questions"]');
    allTotalElements.forEach(el => {
        if (el.textContent === '10' || el.textContent === '') {
            el.textContent = totalQuestions;
        }
    });

    quizContainer.style.display = 'block';
    finalResult.style.display = 'none';
    resultContainer.style.display = 'none';

    await loadQuestion();
}

// 질문 로드 (API 호출)
async function loadQuestion() {
    try {
        showLoading();
        if (isBestDifficultMode) {
            currentQuestion = bestDifficultQuestions[currentQuestionIndex];
        } else {
            const questionId = currentQuestionIndex + 1;
            currentQuestion = await fetchQuestion(questionId);
        }

        hideLoading();

        // 질문 애니메이션
        questionText.style.opacity = '0';
        questionText.style.transform = 'translateY(-20px)';

        setTimeout(() => {
            // 마크다운 콘텐츠를 HTML로 변환하여 표시
            questionText.innerHTML = parseMarkdownToHtml(currentQuestion.content);
            questionText.style.transition = 'all 0.5s ease';
            questionText.style.opacity = '1';
            questionText.style.transform = 'translateY(0)';
        }, 100);

        if (isBestDifficultMode) {
            currentQuestionSpan.textContent = currentQuestionIndex + 1;
        } else {
            currentQuestionSpan.textContent = currentQuestionIndex + 1 - quizStartId;
        }

        // 진행률 업데이트 애니메이션
        let progressPercent;
        if (isBestDifficultMode) {
            progressPercent = ((currentQuestionIndex + 1) / totalQuestions) * 100;
        } else {
            progressPercent = ((currentQuestionIndex + 1 - quizStartId) / totalQuestions) * 100;
        }
        setTimeout(() => {
            progress.style.width = progressPercent + '%';
        }, 300);

        // 선택지 생성 애니메이션
        optionsContainer.style.opacity = '0';
        optionsContainer.innerHTML = '';

        setTimeout(() => {
            // 문제 타입에 따른 안내 메시지 추가
            const instructionDiv = document.createElement('div');
            instructionDiv.className = 'selection-instruction';

            const questionType = currentQuestion?.type || "MULTIPLE_CHOICE";
            let instructionText = "";

            if (questionType === "SINGLE_CHOICE") {
                instructionText = `
                        <p style="font-size: 0.9rem; color: #b8c5d6; margin-bottom: 15px; text-align: center;">
                            💡 <strong>선택지를 클릭하여 답을 선택하세요.</strong> 하나만 선택할 수 있습니다.
                        </p>
                    `;
            } else {
                instructionText = `
                        <p style="font-size: 0.9rem; color: #b8c5d6; margin-bottom: 15px; text-align: center;">
                            💡 <strong>선택지를 클릭하여 답을 선택하세요.</strong> 다중 선택이 가능합니다.
                        </p>
                    `;
            }

            instructionDiv.innerHTML = instructionText;
            optionsContainer.appendChild(instructionDiv);

            currentQuestion.options.forEach((option, index) => {
                const optionElement = document.createElement('div');
                optionElement.className = questionType === "SINGLE_CHOICE" ? 'option single-choice' : 'option';
                optionElement.id = option.id;
                optionElement.onclick = () => selectOption(index);

                const optionLabel = document.createElement('span');
                optionLabel.className = 'option-label';
                optionLabel.textContent = String.fromCharCode(65 + index); // A, B, C, D

                const optionText = document.createElement('span');
                optionText.textContent = option.content;

                optionElement.appendChild(optionLabel);
                optionElement.appendChild(optionText);
                optionsContainer.appendChild(optionElement);
            });

            optionsContainer.style.transition = 'opacity 0.3s ease';
            optionsContainer.style.opacity = '1';
        }, 200);

        // 상태 초기화
        selectedAnswers = [];
        isAnswered = false;
        submitBtn.disabled = true;
        submitBtn.textContent = '답 제출하기';
        resultContainer.style.display = 'none';

        // 버튼 애니메이션
        submitBtn.style.transform = 'scale(0.9)';
        setTimeout(() => {
            submitBtn.style.transition = 'all 0.3s ease';
            submitBtn.style.transform = 'scale(1)';
        }, 800);

    } catch (error) {
        hideLoading();
        console.error('문제를 불러오는 중 오류가 발생했습니다:', error);

        // 오류 메시지 표시
        questionText.innerHTML = `
            <div style="text-align: center; padding: 20px; color: #e74c3c;">
                <h3>❌ 문제를 불러올 수 없습니다</h3>
                <p>네트워크 연결을 확인하거나 잠시 후 다시 시도해주세요.</p>
                <button onclick="loadQuestion()" style="margin-top: 10px; padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 5px; cursor: pointer;">
                    다시 시도
                </button>
            </div>
        `;
    }
}

// 선택지 선택 (단일/다중 선택 지원)
function selectOption(index) {
    if (isAnswered) return;

    const options = document.querySelectorAll('.option');
    const selectedOption = options[index];
    const questionType = currentQuestion?.type || "MULTIPLE_CHOICE";

    const optionId = currentQuestion.options[index].id; // ← id 기준

    // 이미 선택된 항목인지 확인
    const isAlreadySelected = selectedAnswers.includes(optionId);

    if (questionType === "SINGLE_CHOICE") {
        // 단일 선택: 기존 선택을 모두 해제하고 새로운 선택만 유지
        if (isAlreadySelected) {
            // 이미 선택된 항목을 다시 클릭하면 선택 해제
            selectedAnswers = [];
            selectedOption.classList.remove('selected');
            selectedOption.style.transform = 'scale(1)';
        } else {
            // 모든 선택 해제
            options.forEach(opt => {
                opt.classList.remove('selected');
                opt.style.transform = 'scale(1)';
            });
            selectedAnswers = [optionId];

            // 새로운 선택 표시
            selectedOption.classList.add('selected');
            selectedOption.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
            selectedOption.style.transform = 'scale(1.02)';

            // 선택 피드백 효과
            selectedOption.style.animation = 'pulse 0.6s ease';
            setTimeout(() => {
                selectedOption.style.animation = '';
            }, 600);
        }
    } else {
        // 다중 선택: 기존 로직 유지
        if (isAlreadySelected) {
            // 선택 해제
            selectedAnswers = selectedAnswers.filter(i => i !== optionId);
            selectedOption.classList.remove('selected');
            selectedOption.style.transform = 'scale(1)';
        } else {
            // 새로운 선택 추가
            selectedAnswers.push(optionId);
            selectedOption.classList.add('selected');
            selectedOption.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
            selectedOption.style.transform = 'scale(1.02)';

            // 선택 피드백 효과
            selectedOption.style.animation = 'pulse 0.6s ease';
            setTimeout(() => {
                selectedOption.style.animation = '';
            }, 600);
        }
    }

    // 선택된 답안 수에 따른 버튼 텍스트 업데이트
    updateSubmitButton();
}

// 제출 버튼 상태 업데이트
function updateSubmitButton() {
    const questionType = currentQuestion?.type || "MULTIPLE_CHOICE";

    if (selectedAnswers.length === 0) {
        submitBtn.disabled = true;
        submitBtn.textContent = '답 선택하기';
    } else if (questionType === "SINGLE_CHOICE") {
        // 단일 선택 문제
        submitBtn.disabled = false;
        submitBtn.textContent = '답 제출하기';
        // 버튼 활성화 애니메이션
        submitBtn.style.transform = 'scale(1.05)';
        setTimeout(() => {
            submitBtn.style.transform = 'scale(1)';
        }, 150);
    } else {
        // 다중 선택 문제
        submitBtn.disabled = false;
        if (selectedAnswers.length === 1) {
            submitBtn.textContent = '답 제출하기';
        } else {
            submitBtn.textContent = `${selectedAnswers.length}개 답안 제출하기`;
        }
        // 버튼 활성화 애니메이션
        submitBtn.style.transform = 'scale(1.05)';
        setTimeout(() => {
            submitBtn.style.transform = 'scale(1)';
        }, 150);
    }
}

// 답 제출
async function submitAnswer() {
    if (selectedAnswers.length === 0 || isAnswered) return;

    isAnswered = true;
    submitBtn.disabled = true;
    submitBtn.textContent = '제출 중...';

    try {
        // API에 답 제출
        const questionId = currentQuestion.id;
        const result = await submitAnswerToAPI(questionId, selectedAnswers);

        const isCorrect = result.isCorrect;
        const correctAnswers = result.answerOptions.map(answer => answer); // 0부터 시작하는 인덱스로 변환
        const submittedAnswers = result.userOptions.map(answer => answer); // 0부터 시작하는 인덱스로 변환

        if (isCorrect) {
            score++;
        }
        console.log("currentQuestionIndex : " + currentQuestionIndex);

        QuizStorage.save({
            quizSet: quizSet,
            currentQuestionIndex: currentQuestionIndex +1,
            total : totalQuestions,
            score: score
        });

        // 모든 선택지에 결과 표시
        const options = document.querySelectorAll('.option');
        console.log(Array.from(options));

        options.forEach((option, index) => {

            option.classList.add('disabled');

            // 정답 표시
            if (correctAnswers.includes(option.id)) {
                option.classList.add('correct');
            }

            // 사용자가 선택했지만 틀린 답 표시
            if (submittedAnswers.includes(option.id) && !correctAnswers.includes(option.id)) {
                option.classList.add('incorrect');
            }
        });

        const firstOptionId = currentQuestion.options[0].id;

        // 결과 메시지 표시
        resultMessage.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');

        if (isCorrect) {
            resultMessage.innerHTML = '🎉 정답입니다!';
        } else {
            const correctLabels = correctAnswers.map(optionId => {
                console.log("mappedAnswer : " + String.fromCharCode((65 + optionId - firstOptionId)));
                return String.fromCharCode((65 + optionId - firstOptionId));
            }).join(', ');
            resultMessage.innerHTML = `❌ 틀렸습니다. 정답은 ${correctLabels}번입니다.`;
        }

        // 해설 표시 (API에서 받은 해설 사용)
        displayExplanationFromAPI(result.solution);

        // 다음 버튼 텍스트 설정
        let isLastQuestion;
        if (isBestDifficultMode) {
            isLastQuestion = currentQuestionIndex === totalQuestions - 1;
        } else {
            isLastQuestion = currentQuestionIndex - quizStartId === totalQuestions - 1;
        }

        if (isLastQuestion) {
            nextBtn.textContent = '결과 보기';
        } else {
            nextBtn.textContent = '다음 문제';
        }

        resultContainer.style.display = 'block';
        submitBtn.style.display = 'none';

    } catch (error) {
        console.error('답 제출 중 오류가 발생했습니다:', error);

        // 오류 시 UI 복원
        isAnswered = false;
        submitBtn.disabled = false;
        updateSubmitButton();

        // 오류 메시지 표시
        alert('답 제출 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
}

// 다음 문제 또는 결과 표시
async function nextQuestion() {
    let isLastQuestion;
    if (isBestDifficultMode) {
        isLastQuestion = currentQuestionIndex === totalQuestions - 1;
    } else {
        isLastQuestion = currentQuestionIndex - quizStartId === totalQuestions - 1;
    }

    if (isLastQuestion) {
        showFinalResult();
    } else {
        currentQuestionIndex++;
        submitBtn.style.display = 'block';
        await loadQuestion();
    }

    requestAnimationFrame(() => {
        requestAnimationFrame(() => {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    });
}

// 최종 결과 표시
let isSubmittingResult = false;

function showFinalResult() {
    if (isSubmittingResult) return;
    isSubmittingResult = true;

    // ✅ 퀴즈 완료 시 진행 상황 삭제
    QuizStorage.clear();

    quizContainer.style.opacity = '0';
    quizContainer.style.transform = 'translateX(-100px)';

    // 통계 요청을 비동기로 발송 (UI 전환을 차단하지 않음)
    fetchAndRenderStats();

    setTimeout(() => {
        quizContainer.style.display = 'none';
        finalResult.style.display = 'block';

        // 점수 카운트업 애니메이션
        animateScore();

        const percentage = Math.round((score / totalQuestions) * 100);

        // 백분율 카운트업 애니메이션
        animatePercentage(percentage);

        // 성과에 따른 메시지
        let message = '';
        let emoji = '';
        if (percentage >= 90) {
            message = '완벽합니다! DB 인덱스 전문가네요!';
            emoji = '🏆';
        } else if (percentage >= 70) {
            message = '훌륭합니다! 좋은 이해도를 보여주셨네요!';
            emoji = '👍';
        } else if (percentage >= 50) {
            message = '괜찮습니다! 조금 더 학습하면 완벽해질 거예요!';
            emoji = '📚';
        } else {
            message = '화이팅! 다시 도전해보세요!';
            emoji = '💪';
        }

        // 메시지 타이핑 효과
        setTimeout(() => {
            showSoftMessage(emoji + ' ' + message);
        }, 1500);

    }, 300);
}

// 통계 요청 및 분포 그래프 후속 렌더링
async function fetchAndRenderStats() {
    try {
        const response = await fetch(`/api/user-answers/results`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                questionSetName: quizSet,
                score: score
            })
        });
        if (response.ok) {
            const statsData = await response.json();
            if (statsData && statsData.scoreDistribution) {
                renderScoreStats(statsData);
            }
        }
    } catch (e) {
        console.error('Failed to save result:', e);
    }
}

// Catmull-Rom → Cubic Bezier 변환
function catmullRomToBezier(points) {
    const d = [];
    for (let i = 0; i < points.length - 1; i++) {
        const p0 = points[Math.max(i - 1, 0)];
        const p1 = points[i];
        const p2 = points[i + 1];
        const p3 = points[Math.min(i + 2, points.length - 1)];

        const cp1x = p1[0] + (p2[0] - p0[0]) / 6;
        const cp1y = p1[1] + (p2[1] - p0[1]) / 6;
        const cp2x = p2[0] - (p3[0] - p1[0]) / 6;
        const cp2y = p2[1] - (p3[1] - p1[1]) / 6;

        if (i === 0) d.push(`M${p1[0]},${p1[1]}`);
        d.push(`C${cp1x},${cp1y} ${cp2x},${cp2y} ${p2[0]},${p2[1]}`);
    }
    return d.join(' ');
}

// 곡선 위의 특정 x좌표에서 y값 보간
function getYOnCurve(points, targetX) {
    for (let i = 0; i < points.length - 1; i++) {
        if (targetX >= points[i][0] && targetX <= points[i + 1][0]) {
            const t = (targetX - points[i][0]) / (points[i + 1][0] - points[i][0]);
            return points[i][1] + t * (points[i + 1][1] - points[i][1]);
        }
    }
    return points[points.length - 1][1];
}

// 점수 분포도 렌더링 (SVG 곡선 그래프)
function renderScoreStats(data) {
    if (!Number.isFinite(data.score) || !Number.isFinite(data.averageScore) || !Number.isFinite(data.topPercentage)) {
        return;
    }

    const statsContainer = document.getElementById('scoreStats');
    const histogram = document.getElementById('histogram');
    const avgScoreEl = document.getElementById('avgScore');
    const topPercentEl = document.getElementById('topPercent');

    const distribution = data.scoreDistribution;
    const maxCount = Math.max(...distribution, 1);
    const maxIndex = distribution.length - 1;

    // SVG 좌표 설정
    const W = 280, H = 110;
    const padL = 20, padR = 20, padT = 15, padB = 25;
    const chartW = W - padL - padR;
    const chartH = H - padT - padB;

    // 데이터 포인트 생성
    const points = distribution.map((count, i) => {
        const x = padL + (i / maxIndex) * chartW;
        const y = padT + chartH - (count / maxCount) * chartH;
        return [x, y];
    });

    const baseline = padT + chartH;
    const curvePath = catmullRomToBezier(points);
    const lastPoint = points[points.length - 1];
    const firstPoint = points[0];

    // 유저 점수의 x좌표와 y좌표
    const userX = padL + (data.score / maxIndex) * chartW;
    const userY = getYOnCurve(points, userX);

    // 전체 영역 path (곡선 아래 전체)
    const fullAreaPath = curvePath + ` L${lastPoint[0]},${baseline} L${firstPoint[0]},${baseline} Z`;

    // 상위 영역 path (유저 점수 이상 영역) - clipPath 사용
    const clipId = 'clip-top-' + Date.now();

    // X축 라벨 생성
    let labels = '';
    for (let i = 0; i <= maxIndex; i++) {
        const x = padL + (i / maxIndex) * chartW;
        const isUser = i === data.score;
        const fill = isUser ? '#4ade80' : 'rgba(255,255,255,0.5)';
        const weight = isUser ? 'bold' : 'normal';
        const size = isUser ? '11' : '10';
        labels += `<text x="${x}" y="${baseline + 16}" text-anchor="middle" fill="${fill}" font-size="${size}" font-weight="${weight}">${i}</text>`;
    }

    // "당신" 라벨
    const youLabel = `<text x="${userX}" y="${userY - 12}" text-anchor="middle" fill="#4ade80" font-size="10" font-weight="bold">당신</text>`;

    const svg = `
    <svg viewBox="0 0 ${W} ${H}" class="distribution-chart" xmlns="http://www.w3.org/2000/svg">
        <defs>
            <clipPath id="${clipId}">
                <rect x="${userX}" y="0" width="${W - userX}" height="${H}" />
            </clipPath>
        </defs>

        <!-- 전체 곡선 아래 영역 -->
        <path d="${fullAreaPath}" fill="rgba(255,255,255,0.07)" />

        <!-- 상위 분포 영역 (유저 점수 이상) -->
        <path d="${fullAreaPath}" fill="rgba(74,222,128,0.2)" clip-path="url(#${clipId})" />

        <!-- 곡선 -->
        <path d="${curvePath}" stroke="rgba(255,255,255,0.4)" stroke-width="2" fill="none" />

        <!-- 상위 영역 곡선 강조 -->
        <path d="${curvePath}" stroke="#4ade80" stroke-width="2" fill="none" clip-path="url(#${clipId})" />

        <!-- 유저 위치 세로선 -->
        <line x1="${userX}" y1="${padT}" x2="${userX}" y2="${baseline}" stroke="#4ade80" stroke-width="1" stroke-dasharray="3,3" opacity="0.6" />

        <!-- 유저 위치 점 -->
        <circle cx="${userX}" cy="${userY}" r="4" fill="#4ade80" />
        <circle cx="${userX}" cy="${userY}" r="7" fill="none" stroke="#4ade80" stroke-width="1" opacity="0.4" />

        ${youLabel}
        ${labels}
    </svg>`;

    histogram.innerHTML = svg;

    avgScoreEl.textContent = data.averageScore + '점';
    topPercentEl.textContent = '상위 ' + data.topPercentage + '%';

    statsContainer.style.display = 'block';
}

// 점수 카운트업 애니메이션
function animateScore() {
    let currentScore = 0;
    const increment = score / 30; // 30프레임에 걸쳐 애니메이션

    const scoreTimer = setInterval(() => {
        currentScore += increment;
        if (currentScore >= score) {
            currentScore = score;
            clearInterval(scoreTimer);
        }
        finalScore.textContent = Math.floor(currentScore);
    }, 50);
}

// 백분율 카운트업 애니메이션
function animatePercentage(targetPercentage) {
    let currentPercentage = 0;
    const increment = targetPercentage / 40; // 40프레임에 걸쳐 애니메이션

    setTimeout(() => {
        const percentageTimer = setInterval(() => {
            currentPercentage += increment;
            if (currentPercentage >= targetPercentage) {
                currentPercentage = targetPercentage;
                clearInterval(percentageTimer);

                // 최종 애니메이션 효과
                scorePercentage.style.transform = 'scale(1.1)';
                setTimeout(() => {
                    scorePercentage.style.transform = 'scale(1)';
                }, 200);
            }
            scorePercentage.textContent = Math.floor(currentPercentage) + '%';
        }, 60);
    }, 800);
}

// ✅ 페이드 인만 적용 (확대효과 없음)
function showSoftMessage(message) {
    const messageElement = document.querySelector('.final-result h2');
    messageElement.textContent = message;

    messageElement.style.opacity = '0';
    messageElement.style.transition = 'opacity 0.8s ease';

    requestAnimationFrame(() => {
        messageElement.style.opacity = '1';
    });
}

// API에서 받은 해설 표시 함수 (마크다운 지원)
function displayExplanationFromAPI(explanationText) {
    const explanationContent = document.getElementById('explanationContent');

    // 마크다운 텍스트를 HTML로 변환
    const htmlContent = parseMarkdownToHtml(explanationText);

    explanationContent.innerHTML = htmlContent;

    // 이미지 클릭 시 확대 기능
    const images = explanationContent.querySelectorAll('.question-image');
    images.forEach(img => {
        img.addEventListener('click', () => {
            openImageModal(img.src, img.alt);
        });
    });
}

// 이미지 모달 기능
function openImageModal(src, alt) {
    // 모달 생성
    const modal = document.createElement('div');
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10000;
        backdrop-filter: blur(10px);
        cursor: pointer;
    `;

    const img = document.createElement('img');
    img.src = src;
    img.alt = alt;
    img.style.cssText = `
        max-width: 90%;
        max-height: 90%;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
        transition: transform 0.3s ease;
    `;

    const closeBtn = document.createElement('button');
    closeBtn.innerHTML = '✕';
    closeBtn.style.cssText = `
        position: absolute;
        top: 30px;
        right: 30px;
        background: rgba(255, 255, 255, 0.2);
        border: none;
        color: white;
        font-size: 2rem;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        cursor: pointer;
        backdrop-filter: blur(10px);
        transition: all 0.3s ease;
    `;

    closeBtn.addEventListener('mouseenter', () => {
        closeBtn.style.background = 'rgba(255, 255, 255, 0.3)';
        closeBtn.style.transform = 'scale(1.1)';
    });

    closeBtn.addEventListener('mouseleave', () => {
        closeBtn.style.background = 'rgba(255, 255, 255, 0.2)';
        closeBtn.style.transform = 'scale(1)';
    });

    modal.appendChild(img);
    modal.appendChild(closeBtn);
    document.body.appendChild(modal);

    // 모달 닫기 이벤트
    const closeModal = () => {
        modal.style.opacity = '0';
        setTimeout(() => {
            document.body.removeChild(modal);
        }, 300);
    };

    modal.addEventListener('click', (e) => {
        if (e.target === modal) closeModal();
    });

    closeBtn.addEventListener('click', closeModal);

    // ESC 키로 닫기
    const escHandler = (e) => {
        if (e.key === 'Escape') {
            closeModal();
            document.removeEventListener('keydown', escHandler);
        }
    };
    document.addEventListener('keydown', escHandler);

    // 애니메이션
    modal.style.opacity = '0';
    setTimeout(() => {
        modal.style.transition = 'opacity 0.3s ease';
        modal.style.opacity = '1';
    }, 10);
}

// 퀴즈 재시작
function restartQuiz() {
    // 재시작 애니메이션
    finalResult.style.opacity = '0';
    finalResult.style.transform = 'translateY(-50px)';

    setTimeout(() => {
        // 현재 URL로 리다이렉트 (페이지 새로 로드)
        window.location.href = window.location.href;
    }, 300);
}

// 초기 로딩 애니메이션
function initLoadingAnimation() {
    const header = document.querySelector('header');
    const container = document.querySelector('.quiz-container');

    header.style.opacity = '0';
    header.style.transform = 'translateY(-30px)';
    container.style.opacity = '0';
    container.style.transform = 'translateY(30px)';

    setTimeout(() => {
        header.style.transition = 'all 0.8s ease';
        header.style.opacity = '1';
        header.style.transform = 'translateY(0)';
    }, 200);

    setTimeout(() => {
        container.style.transition = 'all 0.8s ease';
        container.style.opacity = '1';
        container.style.transform = 'translateY(0)';
    }, 400);
}

// 페이지 로드 시 퀴즈 초기화
document.addEventListener('DOMContentLoaded', () => {
    initLoadingAnimation();
    setTimeout(initQuiz, 600);
});

// 키보드 단축키 지원
document.addEventListener('keydown', (e) => {
    if (isAnswered) return;

    // A, B, C, D 키로 선택지 선택
    const keyMap = {'KeyA': 0, 'KeyB': 1, 'KeyC': 2, 'KeyD': 3};
    if (keyMap.hasOwnProperty(e.code)) {
        const optionIndex = keyMap[e.code];
        if (currentQuestion && optionIndex < currentQuestion.options.length) {
            selectOption(optionIndex);
        }
    }

    // Enter 키로 답 제출
    if (e.code === 'Enter' && selectedAnswers.length > 0 && !isAnswered) {
        submitAnswer();
    }
});
