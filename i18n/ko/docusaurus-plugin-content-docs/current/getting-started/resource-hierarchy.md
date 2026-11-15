---
title: 리소스 계층 구조
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 리소스 계층 구조: 프로바이더, 서비스, 리소스, 메서드가 클라우드 및 SaaS API를 쿼리 가능한 SQL 테이블에 매핑하는 방식."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

참고 항목:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

StackQL의 객체는 다음 계층 구조로 표현됩니다. 아래는 Google Cloud 프로바이더를 사용한 예입니다.

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL 리소스 계층 구조"/>

### StackQL 객체 표기법 사용 {#using-the-stackql-object-notation}

이 계층 구조는 특정 프로바이더의 컨텍스트 안에서 `<provider>.<service>.<resource>` 객체 표기법으로 표현됩니다. 다음은 [`SELECT`](/language-spec/select) 문에서 이 객체 표기법을 사용하는 예입니다.  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

리소스나 서비스 이름에 마침표(`.`)가 포함되어 있으면 백틱 기호(__`__)로 리소스 이름을 묶으세요. 예를 들면 다음과 같습니다.

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### 프로바이더의 서비스 표시 {#showing-services-in-a-provider}

프로바이더 내의 서비스는 다음과 같이 [`SHOW SERVICES`](/language-spec/show) 명령을 사용하여 나열할 수 있습니다.  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### 서비스의 리소스 표시 {#showing-resources-within-a-service}

마찬가지로 서비스 내의 리소스는 다음과 같이 [`SHOW RESOURCES`](/language-spec/show) 명령을 사용하여 나열할 수 있습니다.

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### 리소스의 필드 표시 {#showing-fields-within-a-resource}

리소스 내의 필드는 다음과 같이 [`DESCRIBE`](/language-spec/describe) 문을 사용하여 표시할 수 있습니다.

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### 리소스의 실행 가능한 메서드 표시 {#showing-executable-methods-for-a-resource}

리소스에 사용할 수 있는 메서드는 다음과 같이 [`SHOW METHODS`](/language-spec/show) 명령을 사용하여 표시할 수 있습니다.

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
