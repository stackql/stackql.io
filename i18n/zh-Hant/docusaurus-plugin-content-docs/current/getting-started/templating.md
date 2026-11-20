---
title: 建立基礎架構範本
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "在 StackQL 中使用 SHOW INSERT 為任何資源產生完整或僅含必要參數的 INSERT 範本，作為基礎架構定義的起點。"
image: "/img/stackql-featured-image.png"
---
另請參閱：  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ 使用變數 ]](/getting-started/variables)

在 StackQL 中，雲端資源是使用 `INSERT` 陳述式部署的。可以使用 `SHOW INSERT` 命令為各種雲端資源產生 `INSERT` 範本。

### 使用 `SHOW INSERT` 命令產生 `INSERT` 範本 {#generating-an-insert-template-using-the-show-insert-command}

以下是產生範本的範例：

```sql
SHOW INSERT INTO google.compute.addresses;
```
此陳述式可以透過 `shell` 執行，也可以使用 `exec` 命令執行，如下所示：

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__傳回：__*  

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

> 接著可以搭配 `json` 或 `jsonnet` 資料檔案使用此範本，在執行階段提供值；如需詳細資訊，請參閱[使用變數](/getting-started/variables)。

### 產生僅含必要參數的 `INSERT` 範本 {#generating-an-insert-template-with-only-required-parameters}

若要產生僅含必要屬性的 StackQL `INSERT` 範本，請使用 `/*+ REQUIRED */` 查詢提示，如下所示：

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
此陳述式可以透過 `shell` 執行，也可以使用 `exec` 命令執行，如下所示：

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__傳回：__*  

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
