---
title: 如何使用 StackQL 查詢 GitHub 存放庫
description: 將 GitHub 存放庫當作 SQL 資料表 github.repos.repos 查詢，依組織或使用者列出、依語言、可見性與活動篩選，並以 SQL 更新存放庫設定。
keywords: [stackql, github, 存放庫, sql, github api, devops, 治理]
proficiencyLevel: Beginner
faq:
  - question: 查詢公開的 GitHub 存放庫需要憑證嗎？
    answer: 不需要。公開的組織與存放庫無須驗證即可查詢，但受 GitHub 未驗證速率限制（每小時 60 個要求）約束。若要提高限制並存取私人資料，請設定 STACKQL_GITHUB_USERNAME 和 STACKQL_GITHUB_PASSWORD（個人存取權杖）。
  - question: StackQL 還能查詢哪些其他 GitHub 資源？
    answer: github Provider 涵蓋有文件記載的 REST 介面，其中 github.repos 公開了分支、提交、發行版本、共同作業者、webhook、部署、環境、標籤、團隊與流量統計資料等資源。SHOW RESOURCES IN github.repos 會列出全部資源。
  - question: StackQL 可以變更存放庫設定嗎？
    answer: 可以。github.repos.repos 公開了 UPDATE（需要 owner 和 repo）、INSERT（create_in_org）和 DELETE 方法。要求本文欄位透過帶有 data__ 前置詞的資料行設定，各方法的必要參數由 SHOW METHODS IN github.repos.repos 列出。
---

# 如何使用 StackQL 查詢 GitHub 存放庫

GitHub 存放庫以資料表 `github.repos.repos` 的形式公開。依組織或使用者列出存放庫，會對應到相應的 GitHub REST 作業，並由 `WHERE` 子句選擇：`org = '...'` 使用依組織列出的作業，`username = '...'` 使用依使用者列出的作業。

## 必要條件 {#prerequisites}

- 已安裝 GitHub Provider：`REGISTRY PULL github;`
- 選用：用於存取私人資料與提高速率限制的憑證，請參閱[如何讓 StackQL 向 GitHub 進行驗證](/ai/how-tos/authenticate-stackql-to-github)

## 列出組織的存放庫 {#list-an-organizations-repositories}

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

對公開的 `stackql` 組織執行時，此查詢會傳回 `stackql` 引擎存放庫（Go，撰寫本文時有 861 顆星）和 `stackql-provider-registry` 等資料列，這些都是即時 API 資料，不涉及任何快照。

## 依存放庫屬性篩選 {#filter-on-repository-attributes}

標準 SQL 述詞可套用至任何資料行：

```sql
SELECT name, visibility, default_branch, archived
FROM github.repos.repos
WHERE org = 'my-org'
AND archived = false
AND visibility = 'public';
```

實用的治理資料行包括 `private`、`visibility`、`archived`、`fork`、`default_branch`、`delete_branch_on_merge`、`has_wiki`、`topics`、`created_at`、`pushed_at` 和 `security_and_analysis`。`DESCRIBE github.repos.repos` 會列出所有資料行。

## 存放庫清單之外 {#beyond-the-repository-list}

`github.repos` 服務將存放庫的其餘介面公開為同層級的資料表：`github.repos.branches`、`github.repos.commits`、`github.repos.releases`、`github.repos.collaborators`、`github.repos.webhooks`、`github.repos.deployments` 等。使用下列陳述式探索這些資料表：

```sql
SHOW RESOURCES IN github.repos;
```

## 更新存放庫 {#updating-repositories}

變更存放庫設定使用 `UPDATE` 陳述式；此方法需要 `owner` 和 `repo`，本文欄位使用 `data__` 前置詞：

```sql
UPDATE github.repos.repos
SET data__delete_branch_on_merge = true
WHERE owner = 'my-org' AND repo = 'my-repo';
```

將其套用至清單查詢的結果集，只需兩個陳述式即可治理所有存放庫。這種模式很適合搭配將 GitHub 資料與雲端資源聯結的跨 Provider 查詢。

## 相關概念 {#related-concepts}

- [如何讓 StackQL 向 GitHub 進行驗證](/ai/how-tos/authenticate-stackql-to-github)：權杖與速率限制
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：此處使用的 REST 對 SQL 對應方式
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：以 GitHub 作為無須憑證的第一個 Provider
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：代理程式透過 MCP 執行這些查詢
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：比較唯讀的替代方案
