---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 CHAR 함수: 하나 이상의 문자 코드에 해당하는 문자로, 문자열에 줄 바꿈, 따옴표, 탭 및 기타 제어 문자를 넣을 때 사용합니다."
image: "/img/stackql-featured-image.png"
---

`CHAR()` 함수는 하나 이상의 문자 코드에 해당하는 문자로 구성된 문자열을 반환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
PostgreSQL 백엔드와 함께 StackQL을 사용하는 경우 이에 해당하는 함수는 `CHR()`이며, 이 함수는 코드를 1개만 받습니다.
:::

## 구문 {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> 각 인수는 결과에 문자 1개를 추가하므로 `CHAR(72, 105)` 식은 `Hi`를 반환합니다.

## 인수 {#arguments}

**_ascii_code_**  
하나 이상의 정수이며, 각 정수는 문자의 코드입니다. ASCII 코드 또는 임의의 유니코드 코드 포인트를 사용할 수 있습니다.

## 반환 값 {#return-values}

*ascii_code* 인수 하나당 문자 1개로 구성된 문자열을 반환합니다.

* * *

## 일반적인 ASCII 코드 {#common-ascii-codes}

| 문자          | 설명                  | ASCII 코드 | 사용 예                         |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | 줄 바꿈               | 10         | `CHAR(10)`                      |
| `'`           | 작은따옴표            | 39         | `CHAR(39)`                      |
| `"`           | 큰따옴표              | 34         | `CHAR(34)`                      |
| (공백)        | 공백                  | 32         | `CHAR(32)`                      |
| `:`           | 콜론                  | 58         | `CHAR(58)`                      |
| `;`           | 세미콜론              | 59         | `CHAR(59)`                      |
| `\t`          | 가로 탭               | 9          | `CHAR(9)`                       |
| `\r`          | 캐리지 리턴           | 13         | `CHAR(13)`                      |
| `\b`          | 백스페이스            | 8          | `CHAR(8)`                       |
| `\f`          | 폼 피드               | 12         | `CHAR(12)`                      |

* * *

## 예제 {#examples}

### 특수 문자를 사용한 문자열 연결 {#concatenate-strings-with-special-characters}

문자열 사이에 줄 바꿈 문자를 넣어 연결하는 방법은 다음과 같습니다.

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**결과** (표시 이름이 `Jane Doe`이고 ID가 `1234567890123456`인 사용자의 경우):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
