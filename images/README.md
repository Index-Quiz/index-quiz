# DB 인덱스 퀴즈 이미지 폴더

이 폴더에는 퀴즈 해설에 사용되는 이미지들이 저장됩니다.

## 필요한 이미지 목록

### 1. btree-structure.png
- B-Tree 인덱스 구조를 보여주는 다이어그램
- 루트, 내부 노드, 리프 노드의 계층 구조
- 균형 트리의 특징을 시각적으로 표현

### 2. index-performance.png
- 인덱스 사용 전후의 성능 비교 차트
- WHERE, ORDER BY, JOIN 절에서의 성능 향상 그래프
- 쿼리 실행 시간 비교

### 3. clustered-index.png
- 클러스터드 인덱스와 논클러스터드 인덱스 비교
- 물리적 데이터 정렬 방식 설명
- 데이터 페이지와 인덱스 페이지의 관계

### 4. composite-index.png
- 복합 인덱스의 컬럼 순서 중요성
- 선택도에 따른 인덱스 효율성 차이
- 복합 인덱스 사용 패턴 예시

### 5. hash-index.png
- 해시 인덱스의 동작 원리
- 해시 함수와 버킷 구조
- B-Tree와의 성능 비교

### 6. index-scan-types.png
- Index Full Scan, Index Range Scan, Index Seek 비교
- 각 스캔 방식의 특징과 사용 시나리오
- 실행 계획에서의 표현

## 사용 방법

1. 해당 이미지들을 이 폴더에 저장
2. 퀴즈 데이터에서 `explanationImage` 필드에 경로 지정
3. 예시: `explanationImage: "/images/btree-structure.png"`

## 이미지 규격 권장사항

- 형식: PNG, JPG, WEBP
- 최대 크기: 800x600px
- 파일 크기: 500KB 이하
- 배경: 투명 또는 어두운 색상 (UI와 조화)
- 텍스트: 선명하고 읽기 쉬운 폰트