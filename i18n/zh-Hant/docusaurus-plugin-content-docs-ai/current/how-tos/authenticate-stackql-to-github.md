---
title: 如何讓 StackQL 向 GitHub 進行驗證
description: StackQL 透過 STACKQL_GITHUB_USERNAME 與 STACKQL_GITHUB_PASSWORD 環境變數，以個人存取權杖向 GitHub 進行基本驗證；公開資料可免驗證查詢，但速率限制較低。
keywords: [stackql, github 驗證, 個人存取權杖, github api, 環境變數]
proficiencyLevel: Beginner
faq:
  - question: 可以不經驗證就使用 StackQL 查詢 GitHub 嗎？
    answer: 公開資料可以。未驗證的要求可以讀取公開的存放庫、組織與使用者，但受限於 GitHub 偏低的未驗證速率限制。經過驗證的要求可獲得大幅提高的限制，並能存取權杖可見的私人資源。
  - question: 需要哪些權杖範圍？
    answer: 對於公開資料查詢，不含任何範圍的權杖即可提高您的速率限制。讀取私人存放庫需要 repo 範圍；組織資料可能需要 read:org。變更（INSERT、UPDATE、DELETE）需要與作業相符的範圍，例如 repo 或 delete_repo。
  - question: StackQL 支援 GitHub Enterprise 嗎？
    answer: GitHub Provider 以 StackQL Provider 登錄庫中所描述的 github.com REST API 介面為目標。對於透過 github.com 端點提供服務的 GitHub Enterprise Cloud，使用範圍適當的權杖即可執行組織與存放庫查詢。
---

# 如何讓 StackQL 向 GitHub 進行驗證

StackQL 使用基本驗證向 GitHub 進行驗證：您的 GitHub 使用者名稱與個人存取權杖（PAT），以 `STACKQL_GITHUB_USERNAME` 與 `STACKQL_GITHUB_PASSWORD` 提供。公開資料無需任何憑證即可查詢，但適用 GitHub 的未驗證速率限制。

## 步驟 {#steps}

1. 在 GitHub 中建立個人存取權杖（Settings -> Developer settings）。只授予您的查詢所需的範圍：若要以較高的速率限制查詢公開資料，不需要任何範圍；私人存放庫則需要 `repo`。

2. 匯出憑證：

```bash
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

3. 提取 GitHub Provider（僅首次使用時）：

```sql
REGISTRY PULL github;
```

4. 以查詢確認：

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

`org` 述詞會選取 `list_for_org` 存取方法；`WHERE username = '...'` 則會改為選取 `list_for_user`。`SHOW METHODS IN github.repos.repos` 會列出每個方法及其必要參數，包括變更方法（`create_in_org`、`update`、`delete`）。

## 未驗證存取 {#unauthenticated-access}

上述查詢無需憑證即可運作，因為 `stackql` 組織是公開的，這對於評估，以及讓代理程式在佈建憑證之前探索查詢模型都很有用。GitHub 的未驗證速率限制很低（每個 IP 每小時 60 個要求），因此任何持續性的使用都應進行驗證。

## 相關概念 {#related-concepts}

- [如何使用 StackQL 查詢 GitHub 存放庫](/ai/how-tos/query-github-repositories-with-stackql)：完整的查詢模式
- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的對應做法
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：REST 作業如何對應到 SQL 動詞
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：GitHub 是最快的免憑證起點
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：包括速率限制的徵兆
