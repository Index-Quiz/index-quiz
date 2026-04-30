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

// marked.js 커스텀 렌더러 설정
const renderer = new marked.Renderer();

// 헤딩: 기존 클래스명 + ID 유지
renderer.heading = function({ tokens, depth }) {
    const text = this.parser.parseInline(tokens);
    const rawText = tokens.map(t => t.raw || t.text || '').join('');
    const id = slugify(rawText);
    const tag = `h${depth}`;
    const cls = `markdown-h${depth}`;
    return `<${tag} class="${cls}" id="${id}">${text}</${tag}>`;
};

// 단락
renderer.paragraph = function({ tokens }) {
    const text = this.parser.parseInline(tokens);
    // 이미 블록 요소를 포함하면 감싸지 않음
    if (/^<(div|table|h[1-6]|hr|ul|ol|blockquote|details|pre|img)/i.test(text.trim())) {
        return text;
    }
    return `<p class="markdown-paragraph">${text}</p>`;
};

// 블록쿼트(인용구)
renderer.blockquote = function({ tokens }) {
    const body = this.parser.parse(tokens);
    return `<blockquote class="markdown-blockquote">${body}</blockquote>`;
};

// 코드 블록
renderer.code = function({ text, lang }) {
    const language = lang || '';
    const langClass = language ? ` class="language-${language}"` : '';
    let processedCode = text;
    if (language.toLowerCase() === 'sql') {
        processedCode = highlightSQL(processedCode);
    }
    return `<div class="code-block">
        ${language ? `<div class="code-header">${language.toUpperCase()}</div>` : ''}
        <pre><code${langClass}>${processedCode}</code></pre>
    </div>`;
};

// 인라인 코드
renderer.codespan = function({ text }) {
    return `<code class="inline-code">${text}</code>`;
};

// 테이블
renderer.table = function({ header, rows }) {
    const headerCells = header.map(cell => {
        const text = this.parser.parseInline(cell.tokens);
        return `<th>${text}</th>`;
    });
    const headerTexts = header.map(cell => {
        const text = this.parser.parseInline(cell.tokens);
        return text;
    });

    const bodyRows = rows.map(row => {
        const cells = row.map((cell, i) => {
            const text = this.parser.parseInline(cell.tokens);
            const label = headerTexts[i] !== undefined ? escapeHtmlAttr(headerTexts[i]) : '';
            return `<td data-label="${label}">${text}</td>`;
        });
        return `<tr>${cells.join('')}</tr>`;
    });

    return `<div class="table-container">
        <table class="markdown-table">
            <thead><tr>${headerCells.join('')}</tr></thead>
            <tbody>${bodyRows.join('')}</tbody>
        </table>
    </div>`;
};

// 구분선
renderer.hr = function() {
    return '<hr class="markdown-divider">';
};

// 이미지
renderer.image = function({ href, title, text }) {
    return `<div class="image-container"><img src="${href}" alt="${text}" class="question-image" onerror="this.style.display='none'; this.nextElementSibling.style.display='block';" onload="this.style.opacity='1';" style="opacity: 0; transition: opacity 0.5s ease; cursor: pointer;"><div class="image-placeholder" style="display: none;">&#x1f4f7; 이미지를 불러올 수 없습니다</div></div>`;
};

// 링크
renderer.link = function({ href, title, tokens }) {
    const text = this.parser.parseInline(tokens);
    if (href.startsWith('#')) {
        return `<a href="${href}" class="markdown-link anchor-link">${text}</a>`;
    }
    return `<a href="${href}" class="markdown-link" target="_blank" rel="noopener noreferrer">${text}</a>`;
};

// 볼드
renderer.strong = function({ tokens }) {
    const text = this.parser.parseInline(tokens);
    return `<strong class="markdown-bold">${text}</strong>`;
};

// 리스트
renderer.list = function({ ordered, start, items }) {
    const tag = ordered ? 'ol' : 'ul';
    const startAttr = (ordered && start !== 1) ? ` start="${start}"` : '';

    // 체크리스트인지 확인 (하나라도 task 항목이 있으면)
    const isTaskList = items.some(item => item.task);
    const cls = isTaskList ? 'markdown-list markdown-checklist' : 'markdown-list';

    const body = items.map(item => this.listitem(item)).join('');
    return `<${tag} class="${cls}"${startAttr}>${body}</${tag}>`;
};

// 리스트 아이템
renderer.listitem = function(item) {
    let content = '';

    if (item.task) {
        const checked = item.checked ? 'checked' : '';
        const checkedClass = item.checked ? ' checked' : '';
        content = `<label class="markdown-checkbox-label${checkedClass}"><input type="checkbox" class="markdown-checkbox" ${checked}><span class="markdown-checkbox-custom"></span><span class="markdown-checkbox-text">${this.parser.parse(item.tokens, !!this.parser.options.async)}</span></label>`;
        return `<li class="markdown-list-item markdown-task-item">${content}</li>`;
    }

    content = this.parser.parse(item.tokens, !!this.parser.options.async);
    return `<li class="markdown-list-item">${content}</li>`;
};

// details/summary 지원 (marked의 html 패스스루)
renderer.html = function({ text }) {
    // raw HTML 내의 <a> 태그에 markdown-link 클래스 추가
    let processed = text.replace(/<a\s+href=/g, '<a class="markdown-link" href=');
    // raw HTML <ul>/<ol>/<li>에 클래스 추가
    processed = processed.replace(/<ul>/g, '<ul class="markdown-list">');
    processed = processed.replace(/<ol>/g, '<ol class="markdown-list">');
    processed = processed.replace(/<li>/g, '<li class="markdown-list-item">');
    // summary 태그에 클래스 추가
    processed = processed.replace(/<summary>(.*?)<\/summary>/g,
        '<summary class="explanation-summary">$1</summary>');
    return processed;
};

// marked 옵션 설정
marked.setOptions({
    renderer: renderer,
    breaks: false,
    gfm: true
});

// 마크다운 텍스트를 HTML로 변환
function parseMarkdownToHtml(content) {
    // XSS 방지: script 태그 제거
    let cleaned = content;
    cleaned = cleaned.replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '');
    cleaned = cleaned.replace(/<script\b[^>]*>/gi, '');

    return marked.parse(cleaned);
}

// 체크박스 인터랙션: 클릭 시 체크/해제 토글
document.addEventListener('change', function(e) {
    if (!e.target.classList.contains('markdown-checkbox')) return;

    const label = e.target.closest('.markdown-checkbox-label');
    if (!label) return;

    if (e.target.checked) {
        label.classList.add('checked');
    } else {
        label.classList.remove('checked');
    }
});
