---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SHOW: 프로바이더, 서비스, 리소스, 메서드를 나열하고 INSERT 템플릿을 생성하며 실행 중인 바이너리의 버전, 기여자, 종속성을 확인합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

컬렉션의 항목을 나열하는 데 사용합니다. 예를 들어 프로바이더에서 사용할 수 있는 모든 서비스나 특정 서비스에서 사용할 수 있는 모든 리소스를 표시합니다. 실행 중인 StackQL 바이너리의 버전, 기여자, 소프트웨어 종속성도 제공합니다.

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy)

* * * 

## 구문 {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS`, `DEPENDENCIES`에는 프로바이더나 인증 구성이 필요하지 않습니다. `DEPENDENCIES`는 선택적 `LIKE` 절만 지원하며 `IN`, `FROM`, `WHERE`, `LIMIT` 절은 지원되지 않습니다.

* * *

## 예제 {#examples}

### 모든 프로바이더 나열 {#list-all-providers}
로컬 StackQL 환경에서 사용할 수 있는 프로바이더를 나열합니다. 프로바이더 레지스트리에서 가져온 프로바이더와 StackQL에 기본 제공되는 프로바이더가 포함됩니다.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### 프로바이더의 모든 서비스 나열 {#list-all-services-in-a-cloud-provider}
특정 프로바이더에서 사용할 수 있는 모든 서비스를 나열합니다. 선택적 EXTENDED 키워드를 사용하면 추가 정보를 반환합니다.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### 프로바이더 서비스의 모든 리소스 나열 {#list-all-resources-within-a-cloud-provider-service}
프로바이더의 특정 서비스에서 사용할 수 있는 모든 리소스를 나열합니다. 선택적 `EXTENDED` 키워드를 사용하면 리소스 이름과 함께 리소스 설명을 반환합니다.
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### 프로바이더 서비스에 속한 리소스의 사용 가능한 메서드 표시 {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
특정 리소스에 사용할 수 있는 모든 메서드를 나열합니다.
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### `SHOW INSERT` 명령을 사용한 `INSERT` 템플릿 생성 {#generating-an-insert-template-using-the-show-insert-command}

다음과 같이 `SHOW` 명령을 사용하여 `INSERT` 문의 템플릿을 생성할 수 있습니다.

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> 반환되는 필드는 프로바이더에 필요한 필드로만 제한할 수 있습니다. 예제는 [__인프라 템플릿 생성__](/getting-started/templating)을 참조하세요.

> 그런 다음 이 템플릿을 `json` 또는 `jsonnet` 데이터 파일과 함께 사용하여 런타임에 값을 제공할 수 있습니다. 자세한 내용은 [__변수 사용__](/getting-started/variables)을 참조하세요.

### StackQL 버전 표시 {#show-the-stackql-version}

실행 중인 StackQL 프로세스의 버전을 반환합니다. 셸, exec, Postgres 와이어 서버 모드에서 어떤 빌드가 쿼리에 응답하는지 확인해야 할 때 유용합니다. 같은 정보는 MCP `server_info` 도구로도 제공됩니다.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

선택적 `EXTENDED` 키워드를 사용하면 git 커밋, 빌드 타임스탬프, 빌드 플랫폼도 반환합니다.

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>반환 결과:</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` 문에는 프로바이더, 레지스트리, 인증 구성이 필요하지 않으며 어떤 세션에서든 안전하게 실행할 수 있습니다.

### StackQL 기여자 나열 {#list-stackql-contributors}

실행 중인 StackQL 바이너리에 내장된 기여자 순위표를 반환합니다. 이 스냅샷은 StackQL 리포지토리에 별표를 준 GitHub 기여자 중 사람인 기여자를 기여 횟수가 많은 순서로 표시합니다. 스냅샷은 빌드마다 고정되며 런타임에 GitHub를 쿼리하지 않습니다.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| 열 | 설명 |
| --- | --- |
| `contributor` | GitHub 사용자 이름 |
| `contributions` | 스냅샷에 기록된 기여 횟수(`EXTENDED` 사용 시 포함) |

### 바이너리의 소프트웨어 종속성 나열 {#list-the-binarys-software-dependencies}

실행 중인 StackQL 실행 파일에 포함된 Go 모듈의 오프라인 인벤토리를 반환합니다. 사용자와 에이전트가 런타임에 쿼리할 수 있는, 바이너리의 간단한 소프트웨어 자재 명세서(SBOM)를 제공합니다. 직접 및 전이적 Go 종속성을 포함합니다. 설치된 프로바이더 정의, 별도의 래퍼, CI 도구, 테스트 전용 모듈, 컨테이너 이미지, 운영 체제 패키지는 범위에 포함되지 않습니다.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

기본 열은 `name`, `version`, `license`, `description`입니다. `EXTENDED` 키워드는 반환되는 행을 바꾸지 않고 아래의 나머지 열을 추가합니다.

| 열 | 설명 |
| --- | --- |
| `name` | 유효 Go 모듈 경로(Go 모듈 대체가 적용되면 대체 경로를 사용) |
| `version` | 정확한 모듈 버전(시험판 버전과 의사 버전 포함) |
| `license` | 감지된 라이선스 식별자(식별할 수 없거나 모호하면 `UNKNOWN`) |
| `description` | 구성 요소 설명(있는 경우) |
| `source_url` | 소스 리포지토리 URL(`EXTENDED` 사용 시 포함) |
| `license_url` | 모듈 태그 또는 소스 리비전에 고정된 라이선스 파일 URL(`EXTENDED` 사용 시 포함) |
| `purl` | 모듈과 버전을 식별하는 패키지 URL(`EXTENDED` 사용 시 포함) |
| `checksum` | `h1:` 접두사가 붙은 Go 모듈 체크섬으로, 실행 파일이나 아카이브의 SHA-256 다이제스트가 아님(`EXTENDED` 사용 시 포함) |
| `original_name` | 대체가 적용될 때 원래 요청된 모듈 경로(`EXTENDED` 사용 시 포함) |
| `original_version` | 대체가 적용될 때 원래 요청된 버전(`EXTENDED` 사용 시 포함) |

행은 유효 모듈 이름순으로 정렬된 다음 버전순으로 정렬됩니다. 대체 항목에는 대체 모듈의 버전, 체크섬, 저작자 표시가 사용됩니다. 대체가 없는 모듈의 `original_name` 열과 `original_version` 열은 `NULL`입니다. 그 밖에 사용할 수 없는 메타데이터도 `NULL`이지만, 식별되지 않은 라이선스는 `UNKNOWN`입니다. 로컬 파일 시스템 대체에는 임의로 만든 버전, URL, 체크섬이 없습니다.

`LIKE` 절을 사용하면 유효 모듈 이름, 원래 모듈 이름 또는 설명을 검색할 수 있습니다.

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

일치 검사는 대소문자를 구분하지 않습니다. `%` 와일드카드는 0개 이상의 문자와 일치하고 `_` 와일드카드는 정확히 1개의 문자와 일치합니다. 라이선스 열과 버전 열은 검색되지 않습니다. SQL 문자열 리터럴 안의 작은따옴표는 두 번 입력하세요. SQL 문자열 디코딩 후 백슬래시는 리터럴 문자로 처리되며 `LIKE` 와일드카드를 이스케이프하지 않습니다.

일치하는 모듈이 없으면 결과는 0행입니다. CSV 출력에는 열 머리글이 유지되고, PostgreSQL 와이어 프로토콜 클라이언트에는 열 메타데이터가 유지되며, JSON 출력은 `null`입니다.

인벤토리는 빌드 시 내장된 메타데이터와 실행 파일의 Go 빌드 정보를 결합합니다. 쿼리에는 네트워크 액세스, Go 설치, 소스 체크아웃, 모듈 캐시가 필요하지 않습니다. 생성된 메타데이터가 없는 빌드도 링크된 모듈을 보고하며, 누락된 저작자 표시는 `UNKNOWN` 또는 `NULL`로 표시됩니다.

출력은 바이너리의 Go 종속성만 다룹니다. 종속성 관계를 포함하지 않고 SPDX 또는 CycloneDX 문서를 내보내지 않으며, 완전한 배포 SBOM이나 라이선스 규정 준수 판정이 아닙니다.

* * *
