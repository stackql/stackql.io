---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REGEXP_REPLACE 함수: 문자열에서 정규식과 일치하는 부분을 대체 문자열로 바꿉니다."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

일치하는 부분 문자열을 대체 문자열로 바꿉니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 정규식 참조 ]](/language-spec/functions/re/reference) 

* * * 

## 구문 {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## 인수 {#arguments}

__*source*__  
정규식 `pattern`과 일치하는 부분이 `replacement`로 바뀌는 리터럴 문자열 또는 문자열 열입니다.  

__*pattern*__  
검색할 정규식 패턴입니다.  

__*replacement*__  
대체 문자열입니다.

> 대체 문자열에서 `\1`부터 `\9`까지의 캡처된 그룹에 대한 역참조를 지원합니다.  

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 열에서 정규식 패턴 대체 {#replace-a-regular-expression-pattern-in-a-column}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '결과', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

자세한 내용은 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) 문서를 참조하세요.
