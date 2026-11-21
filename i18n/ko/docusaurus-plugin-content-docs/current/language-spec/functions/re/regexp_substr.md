---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REGEXP_SUBSTR 함수: 입력에서 정규식과 일치하는 부분 문자열을 추출합니다."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

입력 문자열에서 지정한 패턴과 일치하는 부분 문자열을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[ 정규식 참조 ]](/language-spec/functions/re/reference) 

* * * 

## 구문 {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*source*__  
정규식(`pattern`)으로 정의된 패턴을 기준으로 부분 문자열을 추출할 리터럴 문자열 또는 문자열 열입니다.  

__*pattern*__  
검색할 정규식 패턴입니다.  

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 정규식을 기준으로 부분 문자열 추출 {#extract-a-substring-based-upon-a-regular-expression}

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
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

자세한 내용은 [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax) 문서를 참조하세요.
