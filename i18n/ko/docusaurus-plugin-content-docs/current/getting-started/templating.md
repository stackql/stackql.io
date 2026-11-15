---
title: 인프라 템플릿 생성
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SHOW INSERT로 모든 리소스에 대해 전체 또는 필수 매개 변수만 포함한 INSERT 템플릿을 생성하여 인프라 정의의 시작점으로 사용합니다."
image: "/img/stackql-featured-image.png"
---
참고 항목:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ 변수 사용 ]](/getting-started/variables)

StackQL에서는 `INSERT` 문을 사용하여 클라우드 리소스를 배포합니다. 다양한 클라우드 리소스의 `INSERT` 템플릿은 `SHOW INSERT` 명령을 사용하여 생성합니다.

### `SHOW INSERT` 명령을 사용한 `INSERT` 템플릿 생성 {#generating-an-insert-template-using-the-show-insert-command}

다음은 템플릿을 생성하는 예입니다.

```sql
SHOW INSERT INTO google.compute.addresses;
```
이 명령은 다음과 같이 `shell`에서 실행하거나 `exec` 명령을 사용하여 실행할 수 있습니다.

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__반환 결과:__*  

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

> 그런 다음 이 템플릿을 `json` 또는 `jsonnet` 데이터 파일과 함께 사용하여 런타임에 값을 제공할 수 있습니다. 자세한 내용은 [변수 사용](/getting-started/variables)을 참조하세요.

### 필수 매개 변수만 포함한 `INSERT` 템플릿 생성 {#generating-an-insert-template-with-only-required-parameters}

필수 속성만 포함한 StackQL `INSERT` 템플릿을 생성하려면 다음과 같이 `/*+ REQUIRED */` 쿼리 힌트를 사용하세요.

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
이 명령은 다음과 같이 `shell`에서 실행하거나 `exec` 명령을 사용하여 실행할 수 있습니다.

```shell
./stackql exec 'show insert /*+ REQUIRED */ into compute.instances' --output text -H
```

*__반환 결과:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
