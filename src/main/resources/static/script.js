// 목데이터 사용 여부 (개발 중에는 true, 프로덕션에서는 false)
const USE_MOCK_DATA = true;

// API를 통한 퀴즈 데이터 관리
let totalQuestions = USE_MOCK_DATA ? 2 : 10; // 목데이터 사용 시 2문제, 실제 API는 10문제
let currentQuestion = null;

// 목데이터 (백엔드 개발 완료 전까지 사용)
const MOCK_DATA = {
    questions: {
        1: {
            questionId: 1,
            type: "SINGLE_CHOICE",
            content: "데이터베이스 인덱스의 주요 목적은 무엇입니까?\n\n인덱스는 데이터베이스에서 **검색 성능을 향상**시키는 중요한 자료구조입니다.\n\n다음 중 인덱스의 **주요 목적**으로 가장 적절한 것은?",
            options: [
                "데이터의 저장 공간을 줄이기 위해서",
                "쿼리의 검색 속도를 향상시키기 위해서",
                "데이터의 보안을 강화하기 위해서",
                "데이터베이스의 백업을 용이하게 하기 위해서"
            ]
        },
        2: {
            questionId: 2,
            type: "SINGLE_CHOICE",
            content: "다음과 같이 인덱스가 없는 `table_a`와 인덱스가 설정된 `table_b`가 있습니다.\n\n```sql\nCREATE TABLE table_a (\n    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n    name VARCHAR(255),\n    age INT,\n    city VARCHAR(255)\n);\n\nCREATE TABLE table_b(\n    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n    name VARCHAR(255),\n    age INT,\n    city VARCHAR(255),\n    INDEX idx_name (name),\n    INDEX idx_age_city (age, city)\n);\n```\n\n다음 두 쿼리를 실행했을 때 각각 **어느 테이블에서 더 빠른 성능을 보이는지** 고르세요.\n\n```sql\n-- A쿼리: 10만 건의 더미 데이터를 삽입\nINSERT INTO 테이블 (name, age, city)\nSELECT \n    CONCAT('NewName', FLOOR(RAND() * 1000000)),\n    FLOOR(RAND() * 100),\n    CONCAT('NewCity', FLOOR(RAND() * 1000))\nFROM\n    (SELECT 1 FROM information_schema.tables LIMIT 100000) a;\n\n-- B쿼리: 특정 name 값 검색\nSELECT * FROM 테이블 WHERE name = 'Name12345';\n```",
            options: [
                "A(insert) : table_a - B(select) : table_a",
                "A(insert) : table_a - B(select) : table_b",
                "A(insert) : table_b - B(select) : table_a",
                "A(insert) : table_b - B(select) : table_b"
            ]
        }
    },
    answers: {
        1: {
            questionId: 1,
            isCorrect: true,
            submittedAnswers: [2],
            correctAnswers: [2],
            solution: "정답은 **2번**입니다.\n\n인덱스의 주요 목적은 **쿼리의 검색 속도를 향상**시키는 것입니다.\n\n인덱스는 테이블의 특정 컬럼에 대한 빠른 접근 경로를 제공하여 데이터 검색 시간을 단축시킵니다.\n\n다른 선택지들은 다음과 같은 이유로 부적절합니다:\n- **1번**: 인덱스는 오히려 추가적인 저장 공간을 필요로 합니다\n- **3번**: 보안은 인덱스의 주요 목적이 아닙니다\n- **4번**: 백업과는 직접적인 관련이 없습니다"
        },
        2: {
            questionId: 2,
            isCorrect: true,
            submittedAnswers: [2],
            correctAnswers: [2],
            solution: "정답은 **2번**입니다.\n\n**A쿼리 (INSERT)**: `table_a`가 더 빠름\n- 인덱스가 없는 `table_a`는 단순히 데이터만 삽입하면 됩니다\n- 인덱스가 있는 `table_b`는 데이터 삽입 시 인덱스도 함께 업데이트해야 하므로 더 느립니다\n\n```sql\n-- table_b에서는 다음 인덱스들이 모두 업데이트됨\nINDEX idx_name (name)        -- name 컬럼 인덱스 업데이트\nINDEX idx_age_city (age, city)  -- age, city 복합 인덱스 업데이트\n```\n\n**B쿼리 (SELECT)**: `table_b`가 더 빠름\n- `table_b`는 `idx_name` 인덱스를 사용하여 빠르게 검색할 수 있습니다\n- `table_a`는 인덱스가 없어 전체 테이블을 스캔해야 합니다\n\n```sql\n-- table_b: 인덱스 스캔 (빠름)\nSELECT * FROM table_b WHERE name = 'Name12345';\n-- table_a: 풀 테이블 스캔 (느림)\nSELECT * FROM table_a WHERE name = 'Name12345';\n```"
        }
    }
};

// API 호출 함수
async function fetchQuestion(questionId) {
    // 목데이터 사용 시
    if (USE_MOCK_DATA) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const mockQuestion = MOCK_DATA.questions[questionId];
                if (mockQuestion) {
                    console.log(`[MOCK] 문제 ${questionId} 조회:`, mockQuestion);
                    resolve(mockQuestion);
                } else {
                    console.warn(`⚠️ [MOCK] 문제 ${questionId}에 대한 목데이터가 없습니다. 사용 가능한 문제: ${Object.keys(MOCK_DATA.questions).join(', ')}`);
                    reject(new Error(`Mock data not found for question ${questionId}`));
                }
            }, 500); // 실제 API 호출처럼 지연 시뮬레이션
        });
    }
    
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

// 답 제출 API 호출 함수 (다중 선택 지원)
async function submitAnswerToAPI(questionId, selectedAnswers) {
    // selectedAnswers 배열을 1부터 시작하는 인덱스로 변환
    const choices = selectedAnswers.map(index => index + 1);
    
    // 목데이터 사용 시
    if (USE_MOCK_DATA) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const mockAnswer = MOCK_DATA.answers[questionId];
                if (mockAnswer) {
                    // 실제 사용자의 답안으로 업데이트
                    const result = {
                        ...mockAnswer,
                        submittedAnswers: choices,
                        isCorrect: arraysEqual(choices.sort(), mockAnswer.correctAnswers.sort())
                    };
                    console.log(`[MOCK] 문제 ${questionId} 채점:`, result);
                    resolve(result);
                } else {
                    reject(new Error(`Mock answer not found for question ${questionId}`));
                }
            }, 800); // 실제 API 호출처럼 지연 시뮬레이션
        });
    }
    
    // 실제 API 호출
    try {
        const response = await fetch(`/api/questions/${questionId}/userAnswers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                choices: choices
            })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        return result; // { questionId, isCorrect, submittedAnswers, correctAnswers, solution }
    } catch (error) {
        console.error('답 제출 중 오류가 발생했습니다:', error);
        throw error;
    }
}

// 배열 비교 헬퍼 함수
function arraysEqual(a, b) {
    if (a.length !== b.length) return false;
    return a.every((val, index) => val === b[index]);
}

// 마크다운 이미지 파싱 함수
function parseMarkdownImages(content) {
    const imageRegex = /!\[([^\]]*)\]\(([^)]+)\)/g;
    const images = [];
    let match;
    
    while ((match = imageRegex.exec(content)) !== null) {
        images.push({
            alt: match[1],
            src: match[2]
        });
    }
    
    return images;
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

// 마크다운 텍스트를 HTML로 변환 (개선된 변환)
function parseMarkdownToHtml(content) {
    let html = content;
    
    // 코드 블록 변환 (```언어\n코드\n``` 형식)
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
    
    // 인라인 코드 변환 (`코드` 형식)
    html = html.replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>');
    
    // 이미지 변환
    html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, 
        '<img src="$2" alt="$1" class="question-image" onclick="openImageModal(\'$2\', \'$1\')" style="max-width: 100%; height: auto; margin: 10px 0; border-radius: 8px; cursor: pointer; transition: transform 0.3s ease;" onmouseover="this.style.transform=\'scale(1.02)\'" onmouseout="this.style.transform=\'scale(1)\'">'
    );
    
    // 볼드 텍스트 변환
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    
    // 줄바꿈 변환 (코드 블록 내부는 제외)
    html = html.replace(/\n\n/g, '</p><p>');
    html = html.replace(/\n/g, '<br>');
    
    // 단락으로 감싸기
    if (!html.startsWith('<p>')) {
        html = '<p>' + html + '</p>';
    }
    
    return html;
}

// 퀴즈 상태 변수들
let currentQuestionIndex = 0;
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

// 퀴즈 초기화
async function initQuiz() {
    console.log('initQuiz() 실행됨');
    if (USE_MOCK_DATA) {
        console.log('🧪 [MOCK MODE] 목데이터를 사용하여 퀴즈를 진행합니다.');
        console.log('📝 총 문제 수:', totalQuestions);
    }
    currentQuestionIndex = 0;
    score = 0;
    selectedAnswers = [];
    isAnswered = false;
    
    // 총 문제 수 업데이트
    totalQuestionsSpan.textContent = totalQuestions;
    finalTotal.textContent = totalQuestions;
    
    // Thymeleaf가 설정하지 않은 경우를 위한 추가 업데이트
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
        
        // API에서 현재 문제 번호(1부터 시작)로 데이터 가져오기
        const questionId = currentQuestionIndex + 1;
        currentQuestion = await fetchQuestion(questionId);
        
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
        
        currentQuestionSpan.textContent = currentQuestionIndex + 1;
        
        // 진행률 업데이트 애니메이션
        const progressPercent = ((currentQuestionIndex + 1) / totalQuestions) * 100;
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
                    optionElement.onclick = () => selectOption(index);
                    
                    const optionLabel = document.createElement('span');
                    optionLabel.className = 'option-label';
                    optionLabel.textContent = String.fromCharCode(65 + index); // A, B, C, D
                    
                    const optionText = document.createElement('span');
                    optionText.textContent = option;
                    
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
    
    // 이미 선택된 항목인지 확인
    const isAlreadySelected = selectedAnswers.includes(index);
    
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
            selectedAnswers = [index];
            
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
            selectedAnswers = selectedAnswers.filter(i => i !== index);
            selectedOption.classList.remove('selected');
            selectedOption.style.transform = 'scale(1)';
        } else {
            // 새로운 선택 추가
            selectedAnswers.push(index);
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
        const questionId = currentQuestionIndex + 1;
        const result = await submitAnswerToAPI(questionId, selectedAnswers);
        
        const isCorrect = result.isCorrect;
        const correctAnswers = result.correctAnswers.map(answer => answer - 1); // 0부터 시작하는 인덱스로 변환
        const submittedAnswers = result.submittedAnswers.map(answer => answer - 1); // 0부터 시작하는 인덱스로 변환
        
        if (isCorrect) {
            score++;
        }
        
        // 모든 선택지에 결과 표시
        const options = document.querySelectorAll('.option');
        options.forEach((option, index) => {
            option.classList.add('disabled');
            
            // 정답 표시
            if (correctAnswers.includes(index)) {
                option.classList.add('correct');
            }
            
            // 사용자가 선택했지만 틀린 답 표시
            if (submittedAnswers.includes(index) && !correctAnswers.includes(index)) {
                option.classList.add('incorrect');
            }
        });
        
        // 결과 메시지 표시
        resultMessage.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');
        
        if (isCorrect) {
            resultMessage.innerHTML = '🎉 정답입니다!';
        } else {
            const correctLabels = correctAnswers.map(index => String.fromCharCode(65 + index)).join(', ');
            resultMessage.innerHTML = `❌ 틀렸습니다. 정답은 ${correctLabels}번입니다.`;
        }
        
        // 해설 표시 (API에서 받은 해설 사용)
        displayExplanationFromAPI(result.solution);
        
        // 다음 버튼 텍스트 설정
        if (currentQuestionIndex === totalQuestions - 1) {
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
    if (currentQuestionIndex === totalQuestions - 1) {
        showFinalResult();
    } else {
        currentQuestionIndex++;
        submitBtn.style.display = 'block';
        await loadQuestion();
    }
}

// 최종 결과 표시
function showFinalResult() {
    quizContainer.style.opacity = '0';
    quizContainer.style.transform = 'translateX(-100px)';
    
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
            typeMessage(emoji + ' ' + message);
        }, 1500);
        
    }, 300);
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

// 타이핑 효과
function typeMessage(message) {
    const messageElement = document.querySelector('.final-result h2');
    messageElement.textContent = '';
    let i = 0;
    
    const typeTimer = setInterval(() => {
        if (i < message.length) {
            messageElement.textContent += message.charAt(i);
            i++;
        } else {
            clearInterval(typeTimer);
            
            // 메시지 완성 후 효과
            messageElement.style.transform = 'scale(1.05)';
            setTimeout(() => {
                messageElement.style.transform = 'scale(1)';
            }, 300);
        }
    }, 80);
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

// 해설 표시 함수 (이미지 지원) - 기존 버전 (호환성을 위해 유지)
function displayExplanation(question) {
    const explanationContent = document.getElementById('explanationContent');
    
    let htmlContent = `<p>${question.explanation}</p>`;
    
    // 이미지가 있는 경우 추가
    if (question.explanationImage) {
        htmlContent += `
            <div class="explanation-image-container">
                <img src="${question.explanationImage}" 
                     alt="해설 이미지" 
                     class="explanation-image"
                     onerror="this.style.display='none'; this.nextElementSibling.style.display='block';"
                     onload="this.style.opacity='1';"
                     style="opacity: 0; transition: opacity 0.5s ease;">
                <div class="image-placeholder" style="display: none;">
                    📷 이미지를 불러올 수 없습니다
                </div>
            </div>
        `;
    }
    
    explanationContent.innerHTML = htmlContent;
    
    // 이미지 클릭 시 확대 기능
    const images = explanationContent.querySelectorAll('.explanation-image');
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
        initQuiz();
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
    const keyMap = { 'KeyA': 0, 'KeyB': 1, 'KeyC': 2, 'KeyD': 3 };
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
