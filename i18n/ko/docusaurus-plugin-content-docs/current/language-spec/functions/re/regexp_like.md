---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REGEXP_LIKE 함수: 문자열이 정규식과 일치하는지 검사하여 true이면 1, false이면 0을 반환하며, WHERE 절의 패턴 필터에 사용합니다."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

원본 문자열이 패턴과 일치하는지 확인하고 true이면 `1`, false이면 `0`을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 정규식 참조 ]](/language-spec/functions/re/reference) 

* * * 

## 구문 {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*source*__  
정규식(`pattern`)으로 정의된 패턴이 있는지 검사할 리터럴 문자열 또는 문자열 열입니다.  

__*pattern*__  
검색할 정규식 패턴입니다.  

## 반환 값 {#return-values}
true(원본 문자열이 패턴과 일치함)이면 `1`, false이면 `0`을 반환합니다.  

* * *

## 예제 {#examples}

### 문자열에 패턴이 포함되어 있는지 확인 {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

자세한 내용은 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) 문서를 참조하세요.
