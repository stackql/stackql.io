---
title: 날짜 한정자
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 날짜 및 시간 한정자: DATE, TIME, DATETIME, JULIANDAY, STRFTIME 결과를 일, 월, 연 단위나 기간 시작, 요일 등으로 이동합니다."
image: "/img/stackql-featured-image.png"
---

## 개요 {#overview}

StackQL의 날짜/시간 함수에서 날짜 또는 시간 값 뒤에는 날짜, 시간 또는 둘 다를 변경하는 0개 이상의 한정자가 올 수 있습니다. 각 한정자는 왼쪽에 있는 시간 값에 적용되는 변환입니다. 한정자는 왼쪽에서 오른쪽 순서로 적용됩니다. 사용할 수 있는 한정자는 다음과 같습니다.

| 한정자               | 설명                                                |
|----------------------|-----------------------------------------------------|
| NNN day(s)           | 날짜와 시간에 ±NNN일 추가                           |
| NNN hour(s)          | 날짜와 시간에 ±NNN시간 추가                         |
| NNN minute(s)        | 날짜와 시간에 ±NNN분 추가                           |
| NNN[.NNNN] second(s) | 날짜와 시간에 ±NNN초 가감                           |
| NNN month(s)         | 날짜와 시간에 ±NNN개월 추가                         |
| NNN year(s)          | 날짜와 시간에 ±NNN년 추가                           |
| start of month       | 해당 월의 시작 시점으로 되돌림                      |
| start of year        | 해당 연도의 시작 시점으로 되돌림                    |
| start of day         | 해당 일의 시작 시점으로 되돌림                      |
| weekday N            | 요일 번호가 N인 다음 날짜로 날짜를 앞으로 이동      |
| localtime            | 로컬 시간 반환                                      |
| utc                  | UTC 시간 반환                                       |
| unixepoch            | Unix 시간                                           |

## 예제 {#examples}

### `now` 한정자를 사용한 현재 날짜 및 시간 반환 {#return-the-current-date-and-time-using-the-now-modifier}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### `localtime` 한정자를 사용한 UTC에서 로컬 시간으로의 변환 {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### 날짜/시간을 UTC로 변환 {#covert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### 다른 날짜를 기준으로 한 날짜 계산 {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### 주어진 날짜 이후의 다음 지정 요일 조회 {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### 주어진 날짜가 속한 월의 시작 조회 {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Unix 타임스탬프를 날짜/시간으로 변환 {#covert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

날짜/시간을 Unix epoch 타임스탬프로 변환하려면 [**STRFTIME**](/language-spec/functions/datetime/strftime) 함수를 참조하세요. 두 날짜/시간 객체의 차이를 계산하려면 [**STRFTIME**](/language-spec/functions/datetime/strftime) 또는 [**JULIANDAY**](/language-spec/functions/datetime/julianday) 함수를 참조하세요.

:::
