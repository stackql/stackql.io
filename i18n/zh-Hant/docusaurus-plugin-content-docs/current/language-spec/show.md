---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 SHOW：列出 Provider、服務、資源與方法，產生 INSERT 範本，並檢查執行中二進位檔的版本、貢獻者與相依性。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

用於列出集合中的項目，例如顯示 Provider 中所有可用的服務，或顯示指定服務中所有可用的資源。也會公開執行中 StackQL 二進位檔的版本、貢獻者與軟體相依性。

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy)

* * * 

## 語法 {#syntax}

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

`VERSION`、`CONTRIBUTORS` 與 `DEPENDENCIES` 不需要任何 Provider 或驗證設定。`DEPENDENCIES` 僅支援選用的 `LIKE` 子句；不支援 `IN`、`FROM`、`WHERE` 與 `LIMIT`。

* * *

## 範例 {#examples}

### 列出所有 Provider {#list-all-providers}
列出本機 StackQL 環境中可用的 Provider：包括從 Provider 登錄庫提取的 Provider，以及 StackQL 內建的 Provider。
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

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

### 列出 Provider 中的所有服務 {#list-all-services-in-a-cloud-provider}
列出指定 Provider 中所有可用的服務，使用選用的 EXTENDED 關鍵字可傳回額外資訊。
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

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


### 列出 Provider 服務中的所有資源 {#list-all-resources-within-a-cloud-provider-service}
列出 Provider 指定服務中所有可用的資源。選用的 `EXTENDED` 關鍵字除了資源名稱之外，還會傳回資源描述。
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

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

### 顯示 Provider 服務中某個資源的可用方法 {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
列出指定資源所有可用的方法。
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

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

### 使用 `SHOW INSERT` 命令產生 `INSERT` 範本 {#generating-an-insert-template-using-the-show-insert-command}

`SHOW` 命令可用來產生 `INSERT` 陳述式的範本，如下所示：

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

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

> 傳回的欄位可以限制為僅包含 Provider 所需的欄位；相關範例請參閱[**建立基礎架構範本**](/getting-started/templating)。

> 接著可以搭配 `json` 或 `jsonnet` 資料檔案使用此範本，在執行階段提供值；如需詳細資訊，請參閱[**使用變數**](/getting-started/variables)。

### 顯示 StackQL 版本 {#show-the-stackql-version}

傳回執行中 StackQL 處理程序的版本。在 shell、exec 與 Postgres 線路協定伺服器模式下，當您需要確認是哪個建置在回應查詢時，這個陳述式很有用。相同的資訊也會透過 MCP `server_info` 工具提供。

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

使用選用的 `EXTENDED` 關鍵字，可一併傳回 git commit、建置時間戳記與建置平台：

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>傳回結果：</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` 不需要任何 Provider、登錄庫或驗證設定，可在任何工作階段中安全地發出。

### 列出 StackQL 貢獻者 {#list-stackql-contributors}

傳回內嵌於執行中 StackQL 二進位檔的貢獻者排行榜。此快照列出曾為 StackQL 存放庫加上星號的真人 GitHub 貢獻者，依貢獻次數由高至低排序。快照在每個建置中固定不變，執行階段不會查詢 GitHub。

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| 資料行 | 描述 |
| --- | --- |
| `contributor` | GitHub 使用者名稱 |
| `contributions` | 快照中記錄的貢獻次數；使用 `EXTENDED` 時包含 |

### 列出二進位檔的軟體相依性 {#list-the-binarys-software-dependencies}

傳回執行中 StackQL 可執行檔所包含之 Go 模組的離線清單。這為二進位檔提供輕量的軟體物料清單（SBOM），使用者與代理程式可在執行階段查詢。清單包含直接與間接的 Go 相依性；已安裝的 Provider 定義、獨立的包裝程式、CI 工具、僅供測試的模組、容器映像檔與作業系統套件則不在範圍內。

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

預設資料行為 `name`、`version`、`license` 與 `description`。`EXTENDED` 會附加下方其餘的資料行，而不會改變傳回哪些資料列。

| 資料行 | 描述 |
| --- | --- |
| `name` | 有效的 Go 模組路徑，套用 Go 模組取代時使用取代路徑 |
| `version` | 確切的模組版本，包括發行前版本與虛擬版本 |
| `license` | 偵測到的授權識別碼；無法識別或不明確時為 `UNKNOWN` |
| `description` | 元件描述，若有提供 |
| `source_url` | 原始碼存放庫 URL；使用 `EXTENDED` 時包含 |
| `license_url` | 釘選至模組標籤或來源修訂版本的授權檔案 URL；使用 `EXTENDED` 時包含 |
| `purl` | 識別模組與版本的套件 URL；使用 `EXTENDED` 時包含 |
| `checksum` | 帶有 `h1:` 前置詞的 Go 模組總和檢查碼，而非可執行檔或封存檔的 SHA-256 摘要；使用 `EXTENDED` 時包含 |
| `original_name` | 套用取代時，原本要求的模組路徑；使用 `EXTENDED` 時包含 |
| `original_version` | 套用取代時，原本要求的版本；使用 `EXTENDED` 時包含 |

資料列依有效模組名稱排序，其次依版本排序。取代會使用取代項目的版本、總和檢查碼與歸屬資訊。沒有取代的模組，其 `original_name` 與 `original_version` 為 `NULL`。其他無法取得的中繼資料也是 `NULL`，但無法識別的授權為 `UNKNOWN`。本機檔案系統取代不會填入虛構的版本、URL 或總和檢查碼。

使用 `LIKE` 搜尋有效模組名稱、原始模組名稱或描述：

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

比對不區分大小寫。`%` 比對零個或多個字元，`_` 恰好比對一個字元。不會搜尋授權與版本資料行。在 SQL 字串常值中，單引號需重複兩次；經過 SQL 字串解碼後，反斜線會依原樣處理，不會逸出 `LIKE` 萬用字元。

沒有任何模組相符時，結果為零個資料列。CSV 會保留資料行標頭，PostgreSQL 線路協定用戶端會保留資料行中繼資料，而 JSON 輸出為 `null`。

此清單結合了建置時內嵌的中繼資料與可執行檔的 Go 建置資訊。查詢不需要網路存取、Go 安裝、原始碼簽出或模組快取。沒有產生中繼資料的建置仍會回報其連結的模組，缺少的歸屬資訊會顯示為 `UNKNOWN` 或 `NULL`。

輸出僅涵蓋二進位檔的 Go 相依性。它不包含相依關係，也不會匯出 SPDX 或 CycloneDX 文件，且並非完整的部署 SBOM，也不構成授權合規性判定。

* * *
