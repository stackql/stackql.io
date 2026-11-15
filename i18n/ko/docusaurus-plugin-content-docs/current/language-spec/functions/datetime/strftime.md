---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 STRFTIME 함수: C strftime 함수와 같은 치환 규칙을 사용하여 형식 문자열로 날짜/시간 값의 형식을 지정합니다."
image: "/img/stackql-featured-image.png"
---
지정한 형식에 따라 형식이 지정된 날짜/시간 값을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 날짜 한정자 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 구문 {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*format*__  
*datetime_expression* 값으로 지정한 날짜/시간 값에 적용할 형식입니다. 아래의 형식 표를 참조하세요.

__*datetime_expression*__  
형식을 지정할 날짜 또는 시간 값입니다.

__*modifier*__  
*datetime_expression*에 적용할 0개 이상의 한정자입니다. 한정자에 대한 자세한 내용은 [**날짜 한정자**](/language-spec/functions/datetime/date_modifiers)를 참조하세요.

### 형식 {#formats}

| 형식   | 설명                           |
|--------|--------------------------------|
| %d     | 월의 일: 01-31                 |
| %f     | 소수 초: SS.SSS                |
| %H     | 시: 00-24                      |
| %j     | 연중 일: 001-366               |
| %J     | 율리우스일 번호                |
| %m     | 월: 01-12                      |
| %M     | 분: 00-59                      |
| %s     | 1970-01-01 이후 경과한 초      |
| %S     | 초: 00-59                      |
| %w     | 요일 0-6 (일요일==0)           |
| %W     | 연중 주: 00-53                 |
| %Y     | 연도: 0000-9999                |
| %%     | 퍼센트 기호 이스케이프: %      |

## 반환 값 {#return-values}

원하는 날짜/시간 형식을 나타내는, 형식이 지정된 문자열을 반환합니다.

* * *

## 예제 {#examples}

### 미국 형식의 날짜 반환 {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 날짜/시간을 epoch 타임스탬프로 변환 {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 두 날짜 사이의 일수 계산 {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html) 문서를 참조하세요.
