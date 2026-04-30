-- 학습자료 시딩 SQL
-- 각 문제세트(A~H)에 해당하는 학습자료를 삽입합니다.
-- content 컬럼에는 마크다운 형식의 학습 콘텐츠가 들어갑니다.

-- Set A: 인덱스 기초 1
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('A', '인덱스는 왜 필요한가?', '풀 테이블 스캔의 문제점과 인덱스의 역할',
'# 인덱스는 왜 필요한가?

## 풀 테이블 스캔의 문제점

데이터베이스에서 특정 데이터를 찾을 때, 인덱스가 없으면 테이블의 모든 행을 처음부터 끝까지 순차적으로 확인해야 합니다. 이를 **풀 테이블 스캔(Full Table Scan)** 이라고 합니다.

테이블에 100만 건의 데이터가 있다면, 단 하나의 행을 찾기 위해 100만 건을 모두 읽어야 할 수 있습니다.

```sql
SELECT * FROM users WHERE email = ''test@example.com'';
```

위 쿼리에서 `email` 컬럼에 인덱스가 없다면, MySQL은 `users` 테이블의 모든 행을 확인합니다.

## 인덱스의 역할

인덱스는 **책의 색인**과 같은 역할을 합니다. 원하는 내용을 찾기 위해 책 전체를 읽을 필요 없이, 색인을 통해 해당 페이지로 바로 이동할 수 있습니다.

데이터베이스 인덱스도 마찬가지로, 특정 컬럼의 값과 해당 행의 위치를 매핑하여 빠르게 데이터를 찾을 수 있게 해줍니다.

## 인덱스의 장단점

| 장점 | 단점 |
|------|------|
| 검색 속도 향상 | 추가 저장 공간 필요 |
| 정렬 성능 개선 | INSERT/UPDATE/DELETE 성능 저하 |
| 유니크 제약 보장 | 인덱스 관리 비용 발생 |

---

인덱스는 **읽기 성능**과 **쓰기 성능** 사이의 트레이드오프입니다. 읽기가 많은 워크로드에서는 인덱스가 큰 효과를 발휘합니다.', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('A', '해쉬 인덱스의 구조와 특징', '해쉬 함수 기반 인덱스의 장단점 분석',
'# 해쉬 인덱스의 구조와 특징

## 해쉬 인덱스란?

해쉬 인덱스는 **해쉬 함수**를 사용하여 키 값을 버킷(bucket) 위치로 변환하는 인덱스 구조입니다.

해쉬 함수에 컬럼 값을 입력하면, 해당 값이 저장된 위치를 바로 알 수 있어 **O(1)** 의 시간 복잡도로 데이터를 찾을 수 있습니다.

## 해쉬 인덱스의 특징

**등호(=) 비교에 최적화**되어 있습니다:
```sql
-- 해쉬 인덱스가 효과적인 경우
SELECT * FROM users WHERE id = 100;

-- 해쉬 인덱스가 사용될 수 없는 경우
SELECT * FROM users WHERE id > 100;
SELECT * FROM users WHERE name LIKE ''김%'';
```

## 해쉬 인덱스의 한계

1. **범위 검색 불가** - 해쉬 값은 원래 순서를 유지하지 않음
2. **정렬 불가** - ORDER BY에 활용할 수 없음
3. **부분 검색 불가** - LIKE 연산에 사용 불가
4. **해쉬 충돌** - 서로 다른 키가 같은 버킷에 매핑될 수 있음

---

MySQL의 Memory 스토리지 엔진에서 해쉬 인덱스를 지원하며, InnoDB는 **Adaptive Hash Index**를 내부적으로 사용합니다.', 2);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('A', 'B-Tree 인덱스 완전 정복', '루트, 브랜치, 리프 노드의 역할과 탐색 과정',
'# B-Tree 인덱스 완전 정복

## B-Tree 구조 개요

B-Tree(Balanced Tree)는 MySQL InnoDB의 기본 인덱스 구조입니다. 세 가지 유형의 노드로 구성됩니다:

- **루트 노드(Root Node)**: 트리의 최상위, 탐색의 시작점
- **브랜치 노드(Branch Node)**: 중간 경로, 탐색 방향을 결정
- **리프 노드(Leaf Node)**: 실제 데이터 위치(포인터) 저장

## 탐색 과정

B-Tree에서 데이터를 찾는 과정은 다음과 같습니다:

1. 루트 노드에서 시작
2. 찾으려는 값과 노드의 키를 비교
3. 적절한 자식 노드로 이동
4. 리프 노드에 도달하면 데이터 위치 확인

```sql
-- 아래 쿼리가 B-Tree 인덱스를 사용할 때
SELECT * FROM employees WHERE emp_no = 10001;
-- 루트 → 브랜치 → 리프 순으로 탐색 (O(log N))
```

## B-Tree의 특성

| 특성 | 설명 |
|------|------|
| 시간 복잡도 | O(log N) |
| 범위 검색 | 지원 (리프 노드가 정렬됨) |
| 정렬 | 지원 (인덱스 순서 활용) |
| 균형 유지 | 항상 균형 트리 유지 |

## 리프 노드의 연결

B-Tree의 리프 노드들은 **이중 연결 리스트**로 연결되어 있어, 범위 검색 시 다음 리프 노드로 순차적으로 이동할 수 있습니다.

---

B-Tree 인덱스는 등호 비교, 범위 검색, 정렬 모두에 효과적이어서 가장 범용적으로 사용됩니다.', 3);

-- Set B: 인덱스 기초 2
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('B', '클러스터링 인덱스란?', 'PK와 데이터 정렬의 관계',
'# 클러스터링 인덱스란?

## 개념

클러스터링 인덱스(Clustered Index)는 테이블의 데이터가 **인덱스 순서대로 물리적으로 정렬**되어 저장되는 인덱스입니다.

InnoDB에서는 **Primary Key**가 자동으로 클러스터링 인덱스가 됩니다.

## 특징

- 테이블당 **하나만** 존재할 수 있음
- 리프 노드에 **실제 데이터**가 저장됨
- PK 순서대로 데이터가 정렬됨

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,  -- 이 컬럼이 클러스터링 인덱스
    name VARCHAR(100),
    email VARCHAR(255)
);
```

## PK 선택의 중요성

클러스터링 인덱스는 데이터의 물리적 정렬 순서를 결정하므로, PK 선택이 매우 중요합니다. **AUTO_INCREMENT** 값을 PK로 사용하면 순차적으로 데이터가 추가되어 페이지 분할이 최소화됩니다.', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('B', '세컨더리 인덱스와 테이블 룩업', '논클러스터링 인덱스의 내부 동작 이해',
'# 세컨더리 인덱스와 테이블 룩업

## 세컨더리 인덱스란?

클러스터링 인덱스가 아닌 모든 인덱스를 **세컨더리 인덱스(Secondary Index)** 또는 **논클러스터링 인덱스**라고 합니다.

## 내부 구조

세컨더리 인덱스의 리프 노드에는 실제 데이터가 아닌 **PK 값**이 저장됩니다.

따라서 세컨더리 인덱스로 데이터를 찾으면:
1. 세컨더리 인덱스에서 PK 값을 찾음
2. PK 값으로 클러스터링 인덱스를 다시 검색
3. 클러스터링 인덱스에서 실제 데이터를 가져옴

이 과정을 **테이블 룩업(Table Lookup)** 이라고 합니다.

```sql
-- name에 세컨더리 인덱스가 있을 때
SELECT * FROM users WHERE name = ''홍길동'';
-- 1단계: name 인덱스에서 PK(id) 찾기
-- 2단계: PK로 클러스터링 인덱스에서 전체 데이터 조회
```

## 성능 영향

테이블 룩업은 추가적인 디스크 I/O를 발생시키므로, 많은 행을 조회할 때는 옵티마이저가 인덱스 사용 대신 풀 테이블 스캔을 선택할 수도 있습니다.', 2);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('B', '클러스터링 vs 논클러스터링 비교', '두 인덱스 타입의 성능 차이와 사용 시나리오',
'# 클러스터링 vs 논클러스터링 비교

## 핵심 차이

| 구분 | 클러스터링 인덱스 | 논클러스터링 인덱스 |
|------|-----------------|-------------------|
| 개수 | 테이블당 1개 | 여러 개 가능 |
| 리프 노드 | 실제 데이터 | PK 값 (포인터) |
| 데이터 정렬 | 물리적 정렬 | 논리적 정렬 |
| 검색 속도 | 빠름 (직접 접근) | 추가 룩업 필요 |

## 사용 시나리오

**클러스터링 인덱스가 유리한 경우:**
- PK 기반 범위 검색이 빈번한 경우
- 데이터를 PK 순서로 자주 읽는 경우

**논클러스터링 인덱스가 유리한 경우:**
- 다양한 컬럼 조건으로 검색하는 경우
- 커버링 인덱스로 활용 가능한 경우

---

두 인덱스의 특성을 이해하고 적절히 활용하는 것이 인덱스 설계의 핵심입니다.', 3);

-- Set C: 인덱스 활용 1
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('C', '인덱스 레인지 스캔', '범위 검색에서 인덱스가 동작하는 방식',
'# 인덱스 레인지 스캔

## 개념

인덱스 레인지 스캔(Index Range Scan)은 인덱스에서 **조건에 맞는 범위의 리프 노드**를 찾아 순차적으로 읽는 방식입니다.

```sql
SELECT * FROM orders WHERE order_date BETWEEN ''2024-01-01'' AND ''2024-12-31'';
```

## 동작 과정

1. B-Tree를 통해 시작 지점(''2024-01-01'')을 찾음
2. 리프 노드의 연결 리스트를 따라 순차 탐색
3. 종료 지점(''2024-12-31'')까지 읽음

## 효율적인 조건

인덱스 레인지 스캔이 효과적인 조건:
- `=`, `<`, `>`, `<=`, `>=`
- `BETWEEN`
- `IN` (여러 등호 조건의 조합)
- `LIKE ''prefix%''` (접두사 검색)

---

레인지 스캔의 효율은 **선택도(Selectivity)** 에 따라 달라집니다. 전체 데이터의 20~25% 이상을 읽어야 한다면, 옵티마이저는 풀 테이블 스캔을 선택할 수 있습니다.', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('C', 'Left-Most 규칙의 이해', '복합 인덱스에서 왼쪽 컬럼부터 사용해야 하는 이유',
'# Left-Most 규칙의 이해

## 복합 인덱스란?

두 개 이상의 컬럼으로 구성된 인덱스를 **복합 인덱스(Composite Index)** 라고 합니다.

```sql
CREATE INDEX idx_name_age ON users (name, age);
```

## Left-Most 규칙

복합 인덱스는 **왼쪽 컬럼부터 순서대로** 사용해야 인덱스가 활용됩니다.

```sql
-- 인덱스 사용 O
SELECT * FROM users WHERE name = ''홍길동'';
SELECT * FROM users WHERE name = ''홍길동'' AND age = 30;

-- 인덱스 사용 X
SELECT * FROM users WHERE age = 30;
```

## 왜 그럴까?

복합 인덱스 `(name, age)`는 먼저 `name`으로 정렬하고, 같은 `name` 안에서 `age`로 정렬합니다. 따라서 `name` 없이 `age`만으로는 정렬 순서를 활용할 수 없습니다.

---

복합 인덱스 설계 시 **가장 자주 검색 조건으로 사용되는 컬럼**을 왼쪽에 배치하는 것이 중요합니다.', 2);

-- Set D: 인덱스 활용 2
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('D', 'AND 조건과 인덱스 활용', '복합 인덱스로 AND 조건 최적화하기',
'# AND 조건과 인덱스 활용

## AND 조건에서의 인덱스

AND 조건은 복합 인덱스와 궁합이 좋습니다. 여러 조건을 하나의 복합 인덱스로 처리할 수 있기 때문입니다.

```sql
-- 복합 인덱스 (department, status) 가 있을 때
SELECT * FROM employees
WHERE department = ''개발팀'' AND status = ''재직'';
-- 하나의 인덱스로 두 조건 모두 처리 가능
```

## 최적의 복합 인덱스 설계

AND 조건의 컬럼들을 복합 인덱스로 묶을 때, 컬럼 순서가 중요합니다:
1. **등호(=) 조건** 컬럼을 앞에 배치
2. **범위 조건** 컬럼을 뒤에 배치
3. **카디널리티가 높은** 컬럼을 우선 배치', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('D', 'OR 조건의 인덱스 제약', 'OR 조건이 인덱스 사용을 방해하는 이유',
'# OR 조건의 인덱스 제약

## OR 조건의 문제

OR 조건은 **각 조건이 독립적**이므로 하나의 인덱스로 두 조건을 동시에 처리할 수 없습니다.

```sql
-- name 인덱스와 email 인덱스가 각각 있을 때
SELECT * FROM users
WHERE name = ''홍길동'' OR email = ''hong@test.com'';
```

## 인덱스 머지

MySQL은 OR 조건에서 **인덱스 머지(Index Merge)** 최적화를 사용할 수 있습니다. 각 인덱스를 개별적으로 검색한 후 결과를 합칩니다(Union).

하지만 인덱스 머지가 항상 효율적이지는 않으며, 옵티마이저가 풀 테이블 스캔을 선택할 수도 있습니다.

## 대안

OR 조건 대신 **UNION** 을 사용하면 각 쿼리가 독립적으로 인덱스를 활용할 수 있습니다:

```sql
SELECT * FROM users WHERE name = ''홍길동''
UNION
SELECT * FROM users WHERE email = ''hong@test.com'';
```', 2);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('D', 'IN절의 내부 최적화', 'IN절이 등호 비교로 처리되는 메커니즘',
'# IN절의 내부 최적화

## IN절은 여러 등호의 조합

MySQL에서 IN절은 내부적으로 **여러 개의 등호(=) 비교**로 처리됩니다.

```sql
SELECT * FROM users WHERE status IN (''active'', ''pending'', ''review'');
-- 내부적으로 아래와 동일하게 처리
-- status = ''active'' OR status = ''pending'' OR status = ''review''
```

## 인덱스 활용

IN절은 일반 OR과 달리 **하나의 인덱스를 효율적으로 활용**합니다. 각 값에 대해 인덱스 레인지 스캔을 수행합니다.

## 복합 인덱스에서의 IN절

복합 인덱스에서 IN절은 **등호 조건**으로 취급되므로, 다음 컬럼의 인덱스도 활용됩니다.

```sql
-- 인덱스: (department, status, created_at)
SELECT * FROM employees
WHERE department IN (''개발'', ''디자인'')
  AND status = ''재직''
  AND created_at > ''2024-01-01'';
-- 세 컬럼 모두 인덱스 활용 가능
```', 3);

-- Set E: 인덱스 응용 1
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('E', '복합 인덱스 설계 전략', '카디널리티와 컬럼 순서 최적화',
'# 복합 인덱스 설계 전략

## 카디널리티(Cardinality)

카디널리티는 컬럼의 **고유 값 개수**를 의미합니다. 카디널리티가 높을수록 인덱스의 선택도가 좋습니다.

- **높은 카디널리티**: 사용자 ID, 이메일 (거의 모든 값이 유니크)
- **낮은 카디널리티**: 성별, 상태값 (소수의 고유 값)

## 컬럼 순서 결정 원칙

1. **등호 조건** 컬럼을 범위 조건 컬럼보다 앞에
2. **자주 사용되는 조건** 컬럼을 앞에
3. 등호 조건 내에서는 **카디널리티가 높은** 컬럼을 앞에

```sql
-- 예: 주문 테이블 검색 패턴
-- WHERE status = ? AND user_id = ? AND created_at > ?
-- 최적 인덱스: (user_id, status, created_at)
-- user_id의 카디널리티 > status의 카디널리티
```', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('E', '루스 인덱스 스캔', 'GROUP BY 최적화를 위한 루스 스캔 기법',
'# 루스 인덱스 스캔

## 개념

루스 인덱스 스캔(Loose Index Scan)은 인덱스에서 **필요한 부분만 건너뛰며** 읽는 최적화 기법입니다. 주로 `GROUP BY` 쿼리에서 사용됩니다.

## 동작 방식

```sql
-- 인덱스: (department, salary)
SELECT department, MIN(salary)
FROM employees
GROUP BY department;
```

루스 인덱스 스캔은:
1. 각 department 그룹의 **첫 번째 값만** 읽음
2. 나머지 값은 건너뜀
3. 다음 그룹의 첫 번째 값으로 이동

## 사용 조건

- `GROUP BY`가 인덱스의 Left-Most 컬럼을 사용
- 집계 함수가 `MIN()`, `MAX()` 중 하나
- SELECT 절에 GROUP BY 컬럼과 집계 함수만 포함

---

EXPLAIN에서 `Extra` 컬럼에 `Using index for group-by`가 표시되면 루스 인덱스 스캔이 사용된 것입니다.', 2);

-- Set F: 인덱스 응용 2
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('F', '커버링 인덱스', '테이블 룩업 없이 인덱스만으로 쿼리 처리',
'# 커버링 인덱스

## 개념

커버링 인덱스(Covering Index)는 쿼리에 필요한 모든 컬럼이 인덱스에 포함되어, **테이블 룩업 없이** 인덱스만으로 쿼리를 처리하는 것입니다.

```sql
-- 인덱스: (name, email)
SELECT name, email FROM users WHERE name = ''홍길동'';
-- 인덱스에 name, email이 모두 있으므로 테이블 룩업 불필요
```

## 성능 이점

- 테이블 룩업(랜덤 I/O) 제거
- 인덱스는 테이블보다 크기가 작아 캐시 효율 향상
- 디스크 I/O 감소

## 확인 방법

```sql
EXPLAIN SELECT name, email FROM users WHERE name = ''홍길동'';
```

`Extra` 컬럼에 **Using index**가 표시되면 커버링 인덱스가 사용된 것입니다.

---

커버링 인덱스를 위해 불필요한 컬럼을 인덱스에 추가하면 인덱스 크기가 커지므로, 자주 실행되는 쿼리에 한해 적용하는 것이 좋습니다.', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('F', '스킵 스캔 최적화', 'MySQL 8.0의 스킵 스캔 동작 조건과 원리',
'# 스킵 스캔 최적화

## 개념

스킵 스캔(Skip Scan)은 MySQL 8.0에서 도입된 최적화로, 복합 인덱스의 **선행 컬럼을 건너뛰고** 후행 컬럼으로 인덱스를 사용할 수 있게 해줍니다.

## 동작 원리

```sql
-- 인덱스: (gender, age)
SELECT * FROM users WHERE age = 30;
```

기존에는 Left-Most 규칙에 의해 `gender` 없이 `age`만으로는 인덱스를 사용할 수 없었습니다.

스킵 스캔은 선행 컬럼(`gender`)의 고유 값을 순회하며 각각에 대해 인덱스를 사용합니다:
- `gender = ''M'' AND age = 30` 으로 인덱스 검색
- `gender = ''F'' AND age = 30` 으로 인덱스 검색

## 사용 조건

- 선행 컬럼의 **카디널리티가 낮아야** 효과적
- 선행 컬럼의 고유 값이 많으면 오히려 비효율적

---

EXPLAIN에서 `Extra` 컬럼에 `Using index for skip scan`이 표시됩니다.', 2);

-- Set G: 인덱스 고급 1
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('G', 'ORDER BY와 인덱스', '인덱스를 활용한 정렬 최적화',
'# ORDER BY와 인덱스

## 인덱스를 활용한 정렬

인덱스는 이미 정렬되어 있으므로, ORDER BY 절이 인덱스 순서와 일치하면 **별도의 정렬 작업(filesort) 없이** 결과를 반환할 수 있습니다.

```sql
-- 인덱스: (created_at)
SELECT * FROM posts ORDER BY created_at DESC;
-- 인덱스를 역순으로 읽으면 정렬 완료
```

## filesort 발생 조건

다음의 경우 인덱스를 활용하지 못하고 filesort가 발생합니다:

```sql
-- 인덱스: (a, b)
SELECT * FROM t ORDER BY b;          -- Left-Most 위반
SELECT * FROM t ORDER BY a ASC, b DESC; -- 정렬 방향 불일치
SELECT * FROM t WHERE a > 10 ORDER BY b; -- 범위 조건 후 정렬
```

## 확인 방법

EXPLAIN에서 `Extra` 컬럼에 **Using filesort**가 표시되면 별도 정렬이 발생한 것입니다. `Using index`만 표시되면 인덱스 정렬을 활용한 것입니다.', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('G', 'Index Condition Pushdown', '스토리지 엔진 레벨 조건 평가로 I/O 절감',
'# Index Condition Pushdown (ICP)

## 개념

ICP는 MySQL 5.6에서 도입된 최적화로, 인덱스에 포함된 컬럼의 조건을 **스토리지 엔진 레벨**에서 먼저 평가하여 불필요한 테이블 룩업을 줄이는 기법입니다.

## ICP 없이 동작하는 경우

```sql
-- 인덱스: (last_name, first_name)
SELECT * FROM people
WHERE last_name = ''김'' AND first_name LIKE ''%수'';
```

ICP 없이:
1. 인덱스에서 `last_name = ''김''` 조건으로 검색
2. 매칭되는 모든 행에 대해 테이블 룩업
3. 서버 레이어에서 `first_name LIKE ''%수''` 필터링

## ICP로 동작하는 경우

ICP 적용:
1. 인덱스에서 `last_name = ''김''` 조건으로 검색
2. **인덱스 레벨에서** `first_name LIKE ''%수''` 조건 평가
3. 조건에 맞는 행만 테이블 룩업

## 성능 이점

테이블 룩업(랜덤 I/O) 횟수가 크게 줄어 성능이 향상됩니다.

---

EXPLAIN에서 `Extra` 컬럼에 **Using index condition**이 표시되면 ICP가 사용된 것입니다.', 2);

-- Set H: 인덱스 고급 2
INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('H', '페이지 분할과 쓰기 성능', 'B-Tree 리프 페이지 분할이 발생하는 조건',
'# 페이지 분할과 쓰기 성능

## 페이지 분할이란?

B-Tree의 리프 노드(페이지)에 더 이상 데이터를 추가할 공간이 없을 때, 페이지를 **둘로 나누는** 작업을 페이지 분할(Page Split)이라고 합니다.

## 페이지 분할 과정

1. 리프 페이지가 가득 참
2. 새 페이지를 할당
3. 기존 페이지의 데이터를 절반씩 분배
4. 브랜치 노드에 새 페이지 정보 추가
5. 필요시 상위 노드까지 연쇄 분할

## 성능 영향

- 추가적인 디스크 I/O 발생
- 페이지 이동으로 인한 단편화(Fragmentation)
- INSERT 성능 저하

## 최소화 방법

**AUTO_INCREMENT PK 사용**: 항상 마지막에 추가되므로 중간 페이지 분할 없음
**UUID PK 지양**: 랜덤 값이라 중간 삽입이 빈번, 페이지 분할 다수 발생', 1);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('H', '함수 기반 인덱스', 'MySQL 8.0 표현식 인덱스 생성과 활용',
'# 함수 기반 인덱스

## 개념

MySQL 8.0부터 **표현식(함수) 기반 인덱스**를 생성할 수 있습니다. 컬럼에 함수를 적용한 결과값을 인덱스로 저장합니다.

## 기존 문제

```sql
-- created_at에 인덱스가 있어도 함수 적용 시 인덱스 사용 불가
SELECT * FROM orders WHERE YEAR(created_at) = 2024;
```

컬럼에 함수를 적용하면 인덱스를 사용할 수 없었습니다.

## 함수 기반 인덱스 생성

```sql
-- 표현식 인덱스 생성
CREATE INDEX idx_year ON orders ((YEAR(created_at)));

-- 이제 인덱스 사용 가능
SELECT * FROM orders WHERE YEAR(created_at) = 2024;
```

## 활용 예시

```sql
-- 대소문자 무시 검색
CREATE INDEX idx_lower_email ON users ((LOWER(email)));
SELECT * FROM users WHERE LOWER(email) = ''test@example.com'';

-- JSON 필드 인덱스
CREATE INDEX idx_json ON products ((CAST(data->>''$.price'' AS DECIMAL(10,2))));
```', 2);

INSERT INTO learn_material (question_set, title, description, content, display_order)
VALUES ('H', '인덱스 머지 최적화', '여러 인덱스를 결합하는 최적화 전략',
'# 인덱스 머지 최적화

## 개념

인덱스 머지(Index Merge)는 하나의 쿼리에서 **여러 인덱스를 동시에 사용**하여 결과를 합치는 최적화 기법입니다.

## 유형

### 1. Union (합집합)
OR 조건에서 각 인덱스 결과를 합칩니다.

```sql
-- name 인덱스, email 인덱스 각각 존재
SELECT * FROM users
WHERE name = ''홍길동'' OR email = ''hong@test.com'';
-- 두 인덱스 결과를 UNION
```

### 2. Intersection (교집합)
AND 조건에서 각 인덱스 결과의 교집합을 구합니다.

```sql
SELECT * FROM users
WHERE name = ''홍길동'' AND department = ''개발'';
-- 두 인덱스 결과의 교집합
```

### 3. Sort-Union
범위 조건의 OR에서 정렬 후 합집합을 구합니다.

## 주의사항

인덱스 머지가 항상 최적은 아닙니다. **복합 인덱스**가 가능하다면 복합 인덱스가 더 효율적입니다. 인덱스 머지는 적절한 복합 인덱스가 없을 때의 차선책입니다.

---

EXPLAIN에서 `type` 컬럼에 **index_merge**가 표시되고, `Extra`에 사용된 머지 유형이 표시됩니다.', 3);
