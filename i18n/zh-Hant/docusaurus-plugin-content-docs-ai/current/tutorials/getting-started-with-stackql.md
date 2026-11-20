---
title: 開始使用 StackQL
description: 安裝 StackQL，無須憑證即可對即時公開 GitHub 資料執行第一個查詢，再向雲端供應商驗證，並以 SHOW 和 DESCRIBE 探索 schema。
keywords: [stackql, 開始使用, 教學, 安裝, 第一個查詢, github, aws]
proficiencyLevel: Beginner
faq:
  - question: 在沒有任何雲端憑證的情況下，試用 StackQL 最快的方式是什麼？
    answer: 查詢公開的 GitHub 資料。提取 github Provider，並以 org 述詞從 github.repos.repos 選取資料；公開組織可在 GitHub 匿名速率限制內無須驗證即可讀取，因此從安裝到取得即時 API 資料列的整個流程不到五分鐘。
  - question: 查詢之前需要先定義 schema 嗎？
    answer: 不需要。schema 隨 Provider 定義一起提供。SHOW PROVIDERS、SHOW SERVICES、SHOW RESOURCES、SHOW METHODS 和 DESCRIBE 會在執行階段公開完整的介面，包括每個方法需要哪些 WHERE 參數。
  - question: 如何從 Python 或 BI 工具執行 StackQL？
    answer: 使用 pystackql（pip install pystackql）整合 Python 與 pandas，或執行 stackql srv，並透過 Postgres 線路協定連線任何與 PostgreSQL 相容的用戶端或 BI 工具。
---

# 開始使用 StackQL

本教學分三個階段，從安裝一路到即時查詢：對公開 GitHub 資料執行未經驗證的查詢（不需要憑證）、使用中繼命令探索 schema，以及對 AWS 執行經過驗證的雲端查詢。

## 1. 安裝 {#1-install}

macOS (Homebrew):

```bash
brew install stackql
```

Linux:

```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
```

Windows、套件與 Docker 映像檔的安裝方式請參閱[下載頁面](https://stackql.io/downloads)。使用 `stackql --version` 驗證安裝，然後啟動互動式 shell：

```bash
stackql shell
```

## 2. 第一個查詢：不需要憑證 {#2-first-query---no-credentials-required}

提取 GitHub Provider 並查詢公開組織：

```sql
REGISTRY PULL github;

SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

傳回的資料列（包括 `stackql` 引擎存放庫本身）都是在查詢時轉換為關聯式資料的即時 GitHub API 回應。沒有任何資料經過同步、快取或匯入。（未經驗證的 GitHub 存取限制為每小時 60 個要求；[個人存取權杖](/ai/how-tos/authenticate-stackql-to-github)可提高此限制。）

## 3. 探索 schema {#3-discover-the-schema}

Provider 介面可自我描述，您（或 AI 代理程式）可藉此了解哪些內容可以查詢，而無須閱讀 Provider 文件：

```sql
SHOW PROVIDERS;                        -- installed providers
SHOW SERVICES IN github;               -- services in a provider
SHOW RESOURCES IN github.repos;        -- tables in a service
SHOW METHODS IN github.repos.repos;    -- operations + required parameters
DESCRIBE github.repos.repos;           -- columns
```

最需要記住的是 `SHOW METHODS`：其 `RequiredParams` 資料行會確切告訴您每個作業在 `WHERE` 子句中需要什麼，以及由哪個 SQL 動詞（`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`EXEC`）叫用該作業。

## 4. 查詢雲端供應商 {#4-query-a-cloud-provider}

匯出憑證（此處以 AWS 為例；[Azure](/ai/how-tos/authenticate-stackql-to-azure) 和 [Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) 的做法相同）、提取 Provider，然後查詢：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

```sql
REGISTRY PULL aws;

SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

彙總會自動扇出，以下查詢會以各區域平行的 API 呼叫執行：

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

## 5. 後續步驟 {#5-where-to-go-next}

- **變更**：`SHOW INSERT INTO <resource>` 會產生建立範本；`INSERT`/`UPDATE`/`DELETE` 作用於您所查詢的相同資料表。
- **伺服器模式**：`stackql srv` 公開 PostgreSQL 線路協定，供 psql、DBeaver、BI 工具與 pandas 使用。
- **AI 代理程式**：`stackql mcp` 會執行內建的 MCP 伺服器，請參閱[如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)。
- **Python**：`pip install pystackql`，用於筆記本與管線。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：標準定義
- [如何使用 StackQL 查詢 GitHub 存放庫](/ai/how-tos/query-github-repositories-with-stackql)：第一個查詢的延伸
- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：雲端查詢的延伸
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：發生問題時參考
- [StackQL 常見問題](/ai/faqs/stackql-faq)：常見問題
