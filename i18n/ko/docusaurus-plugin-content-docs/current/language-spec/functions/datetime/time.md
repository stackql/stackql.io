---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 TIME 함수: 시간 값과 선택적 한정자로부터 HH:MM:SS 형식의 시간 문자열을 반환합니다."
image: "/img/stackql-featured-image.png"
---
HH:MM:SS 형식의 시간 문자열을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 날짜 한정자 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 구문 {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*datetime_expression*__  
날짜/시간 값입니다.

__*modifier*__  
*datetime_expression*에 적용할 0개 이상의 한정자입니다. 한정자에 대한 자세한 내용은 [**날짜 한정자**](/language-spec/functions/datetime/date_modifiers)를 참조하세요.

## 반환 값 {#return-values}

*modifier*가 적용된 입력 *datetime_expression*의 시간 부분을 나타내는 문자열을 반환합니다.

* * *

## 예제 {#examples}

### 날짜/시간 값의 시간 부분 반환 {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 날짜/시간 값의 로컬 시간 부분 반환 {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

자세한 내용은 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) 문서를 참조하세요.
