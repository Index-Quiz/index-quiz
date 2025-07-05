// DB 인덱스 퀴즈 데이터
const quizData = [
    {
        question: "데이터베이스 인덱스의 주요 목적은 무엇입니까?",
        options: [
            "데이터의 저장 공간을 줄이기 위해서",
            "쿼리의 검색 속도를 향상시키기 위해서",
            "데이터의 보안을 강화하기 위해서",
            "데이터베이스의 백업을 용이하게 하기 위해서"
        ],
        correct: 1,
        explanation: "인덱스의 주요 목적은 쿼리의 검색 속도를 향상시키는 것입니다. 인덱스는 테이블의 특정 컬럼에 대한 빠른 접근 경로를 제공하여 데이터 검색 시간을 단축시킵니다."
    },
    {
        question: "B-Tree 인덱스의 특징으로 올바른 것은?",
        options: [
            "모든 리프 노드가 같은 레벨에 위치한다",
            "범위 검색에 적합하지 않다",
            "삽입과 삭제 시 트리의 균형이 유지되지 않는다"
        ],
        correct: 0,
        explanation: "B-Tree 인덱스는 균형 트리 구조로, 모든 리프 노드가 같은 레벨에 위치합니다. 이는 모든 검색 경로의 길이가 동일함을 보장하여 일관된 성능을 제공합니다."
    },
    {
        question: "다음 중 인덱스를 생성하면 성능 향상을 기대할 수 있는 경우는?",
        options: [
            "WHERE 절에서 자주 사용되는 컬럼",
            "ORDER BY 절에서 자주 사용되는 컬럼",
            "JOIN 조건에서 자주 사용되는 컬럼",
            "위의 모든 경우"
        ],
        correct: 3,
        explanation: "WHERE, ORDER BY, JOIN 조건에서 자주 사용되는 컬럼들은 모두 인덱스를 생성했을 때 성능 향상을 기대할 수 있습니다. 이러한 절들은 데이터 접근 패턴을 결정하기 때문입니다."
    },
    {
        question: "클러스터드 인덱스(Clustered Index)의 특징은?",
        options: [
            "테이블당 여러 개 생성할 수 있다",
            "실제 데이터의 물리적 순서를 결정한다",
            "별도의 저장 공간이 필요하다"
        ],
        correct: 1,
        explanation: "클러스터드 인덱스는 실제 데이터의 물리적 순서를 결정합니다. 테이블당 하나만 생성할 수 있으며, 데이터 페이지 자체가 인덱스의 리프 노드 역할을 합니다."
    },
    {
        question: "다음 중 인덱스 사용을 방해하는 WHERE 조건은?",
        options: [
            "WHERE age = 25",
            "WHERE name LIKE 'John%'",
            "WHERE UPPER(name) = 'JOHN'",
            "WHERE age BETWEEN 20 AND 30"
        ],
        correct: 2,
        explanation: "WHERE UPPER(name) = 'JOHN'과 같이 컬럼에 함수를 적용하면 인덱스 사용이 방해됩니다. 옵티마이저가 함수 결과를 예측할 수 없어 인덱스 스캔 대신 풀 테이블 스캔을 수행할 가능성이 높습니다."
    },
    {
        question: "복합 인덱스(Composite Index)에서 컬럼 순서가 중요한 이유는?",
        options: [
            "저장 공간을 절약하기 위해서",
            "인덱스 스캔의 효율성을 높이기 위해서",
            "데이터 타입 호환성을 위해서",
            "백업 속도를 향상시키기 위해서"
        ],
        correct: 1,
        explanation: "복합 인덱스에서 컬럼 순서는 인덱스 스캔의 효율성에 직접적인 영향을 미칩니다. 선택도가 높은 컬럼을 앞에 배치하면 더 효율적인 검색이 가능합니다."
    },
    {
        question: "인덱스의 단점으로 올바른 것은?",
        options: [
            "추가적인 저장 공간이 필요하다",
            "INSERT, UPDATE, DELETE 작업이 느려질 수 있다",
            "인덱스 유지 관리 비용이 발생한다",
            "위의 모든 것"
        ],
        correct: 3,
        explanation: "인덱스는 추가 저장 공간을 필요로 하고, 데이터 변경 시 인덱스도 함께 갱신되어야 하므로 DML 작업이 느려질 수 있으며, 지속적인 유지 관리가 필요합니다."
    },
    {
        question: "해시 인덱스(Hash Index)의 특징은?",
        options: [
            "범위 검색에 매우 효율적이다",
            "등등 비교(=) 검색에 매우 빠르다",
            "ORDER BY 절에서 정렬을 피할 수 있다",
            "B-Tree보다 저장 공간을 많이 사용한다"
        ],
        correct: 1,
        explanation: "해시 인덱스는 등등 비교(=) 검색에서 O(1)의 시간복잡도로 매우 빠른 성능을 제공합니다. 하지만 범위 검색이나 정렬에는 적합하지 않습니다."
    },
    {
        question: "다음 중 인덱스 힌트(Index Hint)를 사용하는 이유는?",
        options: [
            "옵티마이저의 잘못된 실행 계획을 수정하기 위해",
            "인덱스 생성 속도를 높이기 위해",
            "메모리 사용량을 줄이기 위해"
        ],
        correct: 0,
        explanation: "인덱스 힌트는 옵티마이저가 잘못된 실행 계획을 선택했을 때, 개발자가 직접 사용할 인덱스를 지정하여 성능을 개선하기 위해 사용됩니다."
    },
    {
        question: "인덱스 스캔 방식 중 'Index Full Scan'의 특징은?",
        options: [
            "인덱스의 루트에서 리프까지만 스캔한다",
            "인덱스의 모든 리프 블록을 순차적으로 읽는다",
            "특정 키 값만을 찾아서 스캔한다",
            "인덱스를 역순으로 스캔한다"
        ],
        correct: 1,
        explanation: "Index Full Scan은 인덱스의 모든 리프 블록을 순차적으로 읽는 방식입니다. 주로 인덱스에 포함된 모든 컬럼이 SELECT나 WHERE 절에서 사용될 때 발생합니다."
    }
];

// 퀴즈 상태 변수들
let currentQuestionIndex = 0;
let score = 0;
let selectedAnswer = null;
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

// 퀴즈 초기화
function initQuiz() {
    currentQuestionIndex = 0;
    score = 0;
    selectedAnswer = null;
    isAnswered = false;
    
    // 총 문제 수 업데이트 (HTML과 JavaScript 모두)
    totalQuestionsSpan.textContent = quizData.length;
    finalTotal.textContent = quizData.length;
    
    // Thymeleaf가 설정하지 않은 경우를 위한 추가 업데이트
    const allTotalElements = document.querySelectorAll('[id*="totalQuestions"], [class*="total-questions"]');
    allTotalElements.forEach(el => {
        if (el.textContent === '10' || el.textContent === '') {
            el.textContent = quizData.length;
        }
    });
    
    quizContainer.style.display = 'block';
    finalResult.style.display = 'none';
    resultContainer.style.display = 'none';
    
    loadQuestion();
}

// 질문 로드
function loadQuestion() {
    const currentQuestion = quizData[currentQuestionIndex];
    
    // 질문 애니메이션
    questionText.style.opacity = '0';
    questionText.style.transform = 'translateY(-20px)';
    
    setTimeout(() => {
        questionText.textContent = currentQuestion.question;
        questionText.style.transition = 'all 0.5s ease';
        questionText.style.opacity = '1';
        questionText.style.transform = 'translateY(0)';
    }, 100);
    
    currentQuestionSpan.textContent = currentQuestionIndex + 1;
    
    // 진행률 업데이트 애니메이션
    const progressPercent = ((currentQuestionIndex + 1) / quizData.length) * 100;
    setTimeout(() => {
        progress.style.width = progressPercent + '%';
    }, 300);
    
    // 선택지 생성 애니메이션
    optionsContainer.style.opacity = '0';
    optionsContainer.innerHTML = '';
    
    setTimeout(() => {
        currentQuestion.options.forEach((option, index) => {
            const optionElement = document.createElement('div');
            optionElement.className = 'option';
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
    selectedAnswer = null;
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
}

// 선택지 선택
function selectOption(index) {
    if (isAnswered) return;
    
    // 이전 선택 제거 애니메이션
    document.querySelectorAll('.option').forEach(opt => {
        opt.classList.remove('selected');
        opt.style.transform = 'scale(1)';
    });
    
    // 새로운 선택 표시 애니메이션
    const options = document.querySelectorAll('.option');
    const selectedOption = options[index];
    
    selectedOption.classList.add('selected');
    selectedOption.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
    selectedOption.style.transform = 'scale(1.02)';
    
    // 선택 피드백 효과
    selectedOption.style.animation = 'pulse 0.6s ease';
    setTimeout(() => {
        selectedOption.style.animation = '';
    }, 600);
    
    selectedAnswer = index;
    
    // 버튼 활성화 애니메이션
    submitBtn.disabled = false;
    submitBtn.style.transform = 'scale(1.05)';
    setTimeout(() => {
        submitBtn.style.transform = 'scale(1)';
    }, 150);
}

// 답 제출
function submitAnswer() {
    if (selectedAnswer === null || isAnswered) return;
    
    isAnswered = true;
    const currentQuestion = quizData[currentQuestionIndex];
    const isCorrect = selectedAnswer === currentQuestion.correct;
    
    if (isCorrect) {
        score++;
    }
    
    // 모든 선택지에 결과 표시
    const options = document.querySelectorAll('.option');
    options.forEach((option, index) => {
        option.classList.add('disabled');
        if (index === currentQuestion.correct) {
            option.classList.add('correct');
        } else if (index === selectedAnswer && !isCorrect) {
            option.classList.add('incorrect');
        }
    });
    
    // 결과 메시지 표시
    resultMessage.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');
    resultMessage.innerHTML = isCorrect ? 
        '🎉 정답입니다!' : 
        '❌ 틀렸습니다. 정답은 ' + String.fromCharCode(65 + currentQuestion.correct) + '번입니다.';
    
    // 해설 표시 (이미지 포함)
    displayExplanation(currentQuestion);
    
    // 다음 버튼 텍스트 설정
    if (currentQuestionIndex === quizData.length - 1) {
        nextBtn.textContent = '결과 보기';
    } else {
        nextBtn.textContent = '다음 문제';
    }
    
    resultContainer.style.display = 'block';
    submitBtn.style.display = 'none';
}

// 다음 문제 또는 결과 표시
function nextQuestion() {
    if (currentQuestionIndex === quizData.length - 1) {
        showFinalResult();
    } else {
        currentQuestionIndex++;
        submitBtn.style.display = 'block';
        loadQuestion();
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
        
        const percentage = Math.round((score / quizData.length) * 100);
        
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

// 해설 표시 함수 (이미지 지원)
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
        const currentQuestion = quizData[currentQuestionIndex];
        if (optionIndex < currentQuestion.options.length) {
            selectOption(optionIndex);
        }
    }
    
    // Enter 키로 답 제출
    if (e.code === 'Enter' && selectedAnswer !== null && !isAnswered) {
        submitAnswer();
    }
});