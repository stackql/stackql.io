---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REGISTRY: 프로바이더 레지스트리의 프로바이더와 버전을 나열하고, 최신 버전 또는 특정 버전을 설치 환경으로 가져옵니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

구성된 프로바이더 레지스트리에서 사용할 수 있는 프로바이더를 나열하거나, 프로바이더를 로컬 설치 환경으로 가져옵니다.  

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> `v1.0`부터는 프로바이더 문서를 읽을 때 유효한 ML-DSA-65 서명을 요구하도록 설정할 수 있습니다.  검증은 기본적으로 비활성화되어 있으며, [`--registry`](/command-line-usage/global-flags#--registry) 플래그의 `verifyConfig` 키로 활성화합니다.

* * * 

## 구문 {#syntax}

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## 예제 {#examples}

### 사용 가능한 프로바이더 나열 {#list-available-providers}
사용 가능한 각 프로바이더의 최신 버전 목록을 조회합니다.  
```sql
REGISTRY LIST;
```
### 특정 프로바이더의 모든 버전 나열 {#list-all-versions-for-a-specific-provider}
특정 프로바이더에서 사용할 수 있는 모든 버전의 목록을 조회합니다.    
```sql
REGISTRY LIST google;
```

### 프로바이더의 최신 버전 설치 {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### 프로바이더의 특정 버전 설치 {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
