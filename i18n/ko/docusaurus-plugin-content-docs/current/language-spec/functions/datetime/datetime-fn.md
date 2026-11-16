---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 DATETIME 함수: 입력 값과 하나 이상의 한정자로 날짜/시간 값을 계산하여 YYYY-MM-DD HH:MM:SS 형식으로 반환합니다."
image: "/img/stackql-featured-image.png"
---
입력 날짜/시간과 하나 이상의 한정자로 계산한 날짜/시간 값을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 날짜 한정자 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 구문 {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*datetime_expression*__  
날짜/시간 값입니다.

__*modifier*__  
*datetime_expression*에 적용할 0개 이상의 한정자입니다. 한정자에 대한 자세한 내용은 [**날짜 한정자**](/language-spec/functions/datetime/date_modifiers)를 참조하세요.

## 반환 값 {#return-values}

*modifier*가 적용된 입력 *datetime_expression*의 날짜/시간을 나타내는 문자열을 반환합니다.

* * *

## 예제 {#examples}

### `localtime` 한정자를 사용한 UTC에서 로컬 시간으로의 변환 {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### 날짜/시간을 UTC로 변환 {#convert-datetime-to-utc}

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

### Unix 타임스탬프를 날짜/시간으로 변환 {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

날짜/시간을 Unix epoch 타임스탬프로 변환하려면 [**STRFTIME**](/language-spec/functions/datetime/strftime) 함수를 참조하세요. 두 날짜/시간 객체의 차이를 계산하려면 [**STRFTIME**](/language-spec/functions/datetime/strftime) 또는 [**JULIANDAY**](/language-spec/functions/datetime/julianday) 함수를 참조하세요.

:::

자세한 내용은 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) 문서를 참조하세요.
