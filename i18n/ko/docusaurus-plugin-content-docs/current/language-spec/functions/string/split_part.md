---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SPLIT_PART 함수: 구분 기호로 문자열을 분할하고 1부터 시작하는 위치의 부분을 반환하며, 리소스 ID와 ARN을 분해할 때 유용합니다."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

구분 기호를 기준으로 원본 문자열을 분할하고 지정한 부분(1부터 계산)을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*source*__  
분할할 리터럴 문자열 또는 문자열 열입니다.

__*sep*__  
구분 기호로 사용할 하나 이상의 문자입니다.  

> `sep` 인수가 여러 문자로 구성된 경우 각 문자가 구분 기호로 처리됩니다.  

> 구분 기호로는 ASCII(1바이트) 기호만 지원됩니다.  

__*part*__  
반환할 분할 문자열의 요소(1부터 시작)입니다.  음수 값은 마지막 요소부터 역방향으로 계산하는 데 사용됩니다.  

## 반환 값 {#return-values}
문자열을 반환합니다.

* * *

## 예제 {#examples}

### 슬래시로 구분된 문자열에서 요소 추출 {#extract-elements-from-a-string-seperated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '결과', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### URL에서 마지막 요소 추출 {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: '결과', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

자세한 내용은 [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md) 문서를 참조하세요.
