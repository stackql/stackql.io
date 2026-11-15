---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 DELETE: WHERE 절에서 필수 매개 변수를 받아 SQL 문으로 클라우드 또는 SaaS 리소스의 인스턴스를 하나 이상 삭제합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 인스턴스를 하나 이상 삭제합니다.

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy)

* * * 

## 구문 {#syntax}

*deleteStatement::=*

<RailroadDiagram 
type="delete"
/>

&nbsp;  
&nbsp;  

```sql
DELETE FROM <multipartIdentifier>
WHERE <expression> ;
```

* * *

## 예제 {#examples}

### 기본 `DELETE` 문 {#basic-delete-statement}
인증된 세션에서 기본 DELETE 문을 실행하여 배포된 리소스를 삭제합니다.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
