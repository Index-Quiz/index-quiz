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

// 헤더 텍스트 → URL 앵커 ID 변환 (한글 보존, GitHub 스타일)
function slugify(text) {
    return text.toLowerCase()
        .trim()
        .replace(/[^\w\s가-힣-]/g, '')   // 영숫자/언더스코어/공백/한글/하이픈 외 제거
        .replace(/\s+/g, '-')             // 공백 → 하이픈
        .replace(/-+/g, '-')              // 연속 하이픈 → 하나로
        .replace(/^-|-$/g, '');           // 앞뒤 하이픈 제거
}

// 마크다운 텍스트를 HTML로 변환 (완전한 마크다운 지원)
function parseMarkdownToHtml(content) {
    let html = content;

    // XSS 방지: script 태그 제거
    html = html.replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '');
    html = html.replace(/<script\b[^>]*>/gi, '');

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

    // 헤딩 변환 (## 헤딩) - id 자동 부여 (목차 앵커 링크용)
    html = html.replace(/^### (.*$)/gm, (m, t) =>
        `<h3 class="markdown-h3" id="${slugify(t)}">${t}</h3>`);
    html = html.replace(/^## (.*$)/gm, (m, t) =>
        `<h2 class="markdown-h2" id="${slugify(t)}">${t}</h2>`);
    html = html.replace(/^# (.*$)/gm, (m, t) =>
        `<h1 class="markdown-h1" id="${slugify(t)}">${t}</h1>`);

    // 구분선 변환 (---)
    html = html.replace(/^---$/gm, '<hr class="markdown-divider">');

    // 이미지 변환 (onclick 제거 - 이벤트 리스너에서 처리)
    html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g,
        '<div class="image-container"><img src="$2" alt="$1" class="question-image" onerror="this.style.display=\'none\'; this.nextElementSibling.style.display=\'block\';" onload="this.style.opacity=\'1\';" style="opacity: 0; transition: opacity 0.5s ease; cursor: pointer;"><div class="image-placeholder" style="display: none;">📷 이미지를 불러올 수 없습니다</div></div>'
    );

    // 마크다운 링크 변환 (공백 허용, 페이지 내 앵커는 같은 탭에서 부드러운 스크롤)
    html = html.replace(/\[([^\]]+)\]\(\s*([^\)]+)\s*\)/g, (match, text, url) => {
        if (url.startsWith('#')) {
            return `<a href="${url}" class="markdown-link anchor-link">${text}</a>`;
        }
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
