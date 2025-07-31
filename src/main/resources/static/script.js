// API를 통한 퀴즈 데이터 관리
let quizData = [];
let totalQuestions = 10; // 기본값, API에서 동적으로 설정될 수 있음
let currentQuestion = null;

// API 호출 함수
async function fetchQuestion(questionId) {
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

// 답 제출 API 호출 함수
async function submitAnswerToAPI(questionId, selectedOptionIndex) {
    try {
        const response = await fetch(`/api/questions/${questionId}/submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                selectedOption: selectedOptionIndex
            })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        return result; // { correct: boolean, correctAnswer: number, explanation: string }
    } catch (error) {
        console.error('답 제출 중 오류가 발생했습니다:', error);
        throw error;
    }
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

// 마크다운 텍스트를 HTML로 변환 (간단한 변환)
function parseMarkdownToHtml(content) {
    let html = content;
    
    // 이미지 변환
    html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, 
        '<img src="$2" alt="$1" class="question-image" onclick="openImageModal(\'$2\', \'$1\')" style="max-width: 100%; height: auto; margin: 10px 0; border-radius: 8px; cursor: pointer; transition: transform 0.3s ease;" onmouseover="this.style.transform=\'scale(1.02)\'" onmouseout="this.style.transform=\'scale(1)\'">'
    );
    
    // 볼드 텍스트 변환
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    
    // 줄바꿈 변환
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
    currentQuestionIndex = 0;
    score = 0;
    selectedAnswer = null;
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
async function submitAnswer() {
    if (selectedAnswer === null || isAnswered) return;
    
    isAnswered = true;
    submitBtn.disabled = true;
    submitBtn.textContent = '제출 중...';
    
    try {
        // API에 답 제출
        const questionId = currentQuestionIndex + 1;
        const result = await submitAnswerToAPI(questionId, selectedAnswer);
        
        const isCorrect = result.correct;
        const correctAnswer = result.correctAnswer;
        
        if (isCorrect) {
            score++;
        }
        
        // 모든 선택지에 결과 표시
        const options = document.querySelectorAll('.option');
        options.forEach((option, index) => {
            option.classList.add('disabled');
            if (index === correctAnswer) {
                option.classList.add('correct');
            } else if (index === selectedAnswer && !isCorrect) {
                option.classList.add('incorrect');
            }
        });
        
        // 결과 메시지 표시
        resultMessage.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');
        resultMessage.innerHTML = isCorrect ? 
            '🎉 정답입니다!' : 
            '❌ 틀렸습니다. 정답은 ' + String.fromCharCode(65 + correctAnswer) + '번입니다.';
        
        // 해설 표시 (API에서 받은 해설 사용)
        displayExplanationFromAPI(result.explanation);
        
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
        submitBtn.textContent = '답 제출하기';
        
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
    if (e.code === 'Enter' && selectedAnswer !== null && !isAnswered) {
        submitAnswer();
    }
});
