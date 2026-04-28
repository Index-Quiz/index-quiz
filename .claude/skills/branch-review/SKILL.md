---
name: branch-review
description: |
  현재 브랜치를 develop과 비교하여 코드 리뷰를 수행하고 Review.md를 생성합니다.
  트리거: "리뷰해줘", "코드 리뷰", "Review.md 만들어줘", "develop이랑 뭐 다른지 봐줘",
  "diff 리뷰", "공격적으로 리뷰", "빡세게 봐줘", "엄격하게 리뷰", "가볍게 리뷰",
  "보안 위주로 리뷰", "성능 위주로", "테스트 누락만".
  현재 브랜치가 develop이 아닐 때 사용합니다.
user_invocable: true
---

# branch-review 스킬

현재 git 브랜치의 변경사항을 develop 기준으로 추적하고, codex 플러그인으로 코드 리뷰를 수행한 뒤, 결과를 저장소 루트의 `Review.md` 파일로 정리합니다.

## 워크플로우

### 1단계: 사전 검증

1. `git branch --show-current`로 현재 브랜치 확인
2. **현재 브랜치가 `develop`이면**: AskUserQuestion으로 사용자에게 확인
   - "현재 develop 브랜치입니다. 다른 베이스 브랜치를 지정하시겠습니까?"
   - 사용자가 중단을 선택하면 종료
   - 다른 베이스를 지정하면 해당 브랜치를 베이스로 사용
3. `git status --porcelain`으로 uncommitted 변경사항 확인
   - 변경사항이 있으면 사용자에게 경고 메시지 출력 (중단하지는 않음)

### 2단계: develop 동기화 및 diff 추출

1. `git fetch origin develop` 실행
2. `git diff origin/develop...HEAD --stat`으로 변경 파일 요약 추출
3. `git diff origin/develop...HEAD`로 전체 diff 추출
4. **diff가 비어 있으면**: Review.md에 "변경사항 없음" 기록하고 종료

### 3단계: 리뷰 강도 판별

사용자의 원래 발화에서 키워드를 감지하여 리뷰 모드를 결정합니다:

| 모드 | 키워드 | codex 호출 방식 |
|------|--------|----------------|
| **aggressive** | "공격적", "빡세게", "엄격하게", "strict", "harsh" | `/codex:adversarial-review` 사용 |
| **balanced** (기본값) | 별도 지정 없음 | `/codex:review` 사용 |
| **lenient** | "가볍게", "관대하게", "lenient" | `/codex:review` 사용 (중요 이슈만 보고) |

**포커스 옵션** (중첩 가능, codex 호출 시 focus 텍스트로 전달):
- "보안 위주로" → `focus on security vulnerabilities, auth, injection`
- "성능 위주로" → `focus on performance, N+1 queries, unnecessary allocations`
- "테스트 누락만" → `focus on missing test coverage`

### 4단계: codex 리뷰 호출

모드에 따라 적절한 codex 명령을 Bash로 실행합니다:

**aggressive 모드:**
```bash
node "${CLAUDE_PLUGIN_ROOT}/scripts/codex-companion.mjs" adversarial-review "--wait --base origin/develop --scope branch <focus_text>"
```
`CLAUDE_PLUGIN_ROOT`는 `/Users/coli/.claude/plugins/cache/openai-codex/codex/1.0.4` 입니다.

**balanced / lenient 모드:**
```bash
node "${CLAUDE_PLUGIN_ROOT}/scripts/codex-companion.mjs" review "--wait --base origin/develop --scope branch"
```

- 반드시 `--wait` 플래그를 사용하여 결과를 동기적으로 받습니다.
- codex 호출이 실패하면 사용자에게 알리고 수동 리뷰 옵션을 제시합니다.
- 타임아웃: Bash 호출 시 `timeout: 600000` (10분)을 설정합니다.

### 5단계: Review.md 작성

codex 리뷰 결과와 diff 통계를 종합하여 저장소 루트에 `Review.md`를 **Write** 도구로 작성합니다 (항상 덮어쓰기, append 금지).

**출력 형식:**

```markdown
# Code Review: <branch-name> vs develop

- 리뷰 일시: <YYYY-MM-DD HH:MM>
- 리뷰 모드: <aggressive | balanced | lenient>
- 포커스: <보안 / 성능 / 테스트 / 전체>
- 변경 파일 수: <N>
- 추가/삭제 라인: +<X> / -<Y>

## 변경 요약
<자연어 요약 3-5줄>

## 변경 파일 목록
| 파일 | 추가 | 삭제 |
|------|------|------|
| path/to/file | +X | -Y |

## Codex 리뷰 결과
### <파일명 또는 카테고리>
- **[심각도] 항목명**: 설명
  - 위치: `path/to/file:42`
  - 권장 조치: ...

## 권장 후속 조치
- [ ] 우선순위 높은 항목
- [ ] ...
```

### 6단계: 완료 보고

Review.md 작성 완료 후 사용자에게 요약을 출력합니다:
- 리뷰 모드, 변경 파일 수, 발견된 이슈 수
- Review.md 경로 안내

## 제약사항

- Review.md는 항상 덮어쓰기 (append 금지)
- git commit, push, branch 변경 등 쓰기 작업은 사용자가 명시적으로 요청할 때만 수행
- diff가 2000줄을 초과하면 변경 파일 목록을 사용자에게 보여주고 중요 파일 위주로 분할 리뷰
- 모든 사용자 응답 및 Review.md 출력은 한국어로 작성

<!-- 
## 테스트 케이스 (트리거 검증용)

다음 발화들이 이 스킬을 트리거하는지 새 세션에서 검증:

1. "develop이랑 비교해서 빡세게 리뷰해줘"
   → aggressive 모드, 포커스: 전체

2. "코드 리뷰 해줘"
   → balanced 모드, 포커스: 전체

3. "Review.md 만들어줘"
   → balanced 모드, 포커스: 전체

4. "보안 위주로 엄격하게 리뷰해줘"
   → aggressive 모드, 포커스: 보안

5. "가볍게 성능 위주로 diff 리뷰"
   → lenient 모드, 포커스: 성능

6. "테스트 누락만 봐줘"
   → balanced 모드, 포커스: 테스트

7. "develop이랑 뭐 다른지 봐줘"
   → balanced 모드, 포커스: 전체
-->
