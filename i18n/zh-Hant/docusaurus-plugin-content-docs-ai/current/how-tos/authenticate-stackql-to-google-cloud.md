---
title: 如何讓 StackQL 向 Google Cloud 進行驗證
description: StackQL 使用服務帳戶金鑰向 Google Cloud 進行驗證，並透過 GOOGLE_CREDENTIALS 環境變數提供 JSON 金鑰內容。
keywords: [stackql, google cloud 驗證, gcp, 服務帳戶, google 憑證]
proficiencyLevel: Beginner
faq:
  - question: GOOGLE_CREDENTIALS 接受的是檔案路徑還是金鑰內容？
    answer: 金鑰內容。文件記載的模式是 export GOOGLE_CREDENTIALS=$(cat /path/to/key.json)，這會將 JSON 內容本身放入變數中。或者，--auth 旗標可以使用 credentialsfilepath 設定，以檔案路徑參照金鑰。
  - question: 服務帳戶需要哪個 IAM 角色？
    answer: 視您的查詢而定的角色。專案範圍的 roles/viewer 涵蓋唯讀的資源清單與稽核查詢；INSERT 和 DELETE 等變更則需要對應的 editor 角色或服務專屬的 admin 角色。
  - question: 為什麼 Google 查詢的 WHERE 子句中需要專案（通常還有可用區域）？
    answer: Google Cloud API 以專案為範圍，可用區域層級的服務則以可用區域為範圍。這些是 API 呼叫的必要參數。對於 google.compute.instances，aggregated_list 方法只需要 project，而 list 需要 project 和 zone；執行 SHOW METHODS IN google.compute.instances 即可查看兩者。
---

# 如何讓 StackQL 向 Google Cloud 進行驗證

StackQL 使用服務帳戶金鑰向 Google Cloud 進行驗證，金鑰以 JSON 內容的形式透過 `GOOGLE_CREDENTIALS` 環境變數提供。

## 步驟 {#steps}

1. 建立服務帳戶並授予它目標專案上的角色（唯讀查詢只需 `roles/viewer` 即可），然後建立並下載 JSON 金鑰：

```bash
gcloud iam service-accounts keys create stackql-key.json \
  --iam-account=stackql-sa@my-project.iam.gserviceaccount.com
```

2. 匯出金鑰內容：

```bash
export GOOGLE_CREDENTIALS=$(cat ./stackql-key.json)
```

另一種做法是使用 `--auth` 旗標以路徑傳遞金鑰，這對伺服器與 MCP 部署很有用：

```bash
stackql shell \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/stackql-key.json"}}'
```

3. 提取 Google Provider（僅首次使用時）：

```sql
REGISTRY PULL google;
```

4. 以查詢確認：

```sql
SELECT name, status, machineType
FROM google.compute.instances
WHERE project = 'my-project';
```

這會使用 `aggregated_list` 存取方法，它只需要 `project`，並傳回所有可用區域中的執行個體。可用區域層級的 `list` 方法需要 `project` 和 `zone`；`SHOW METHODS IN google.compute.instances` 會顯示每個方法的必要參數。

## 憑證的適用範圍 {#where-the-credentials-apply}

相同的變數（或 `--auth` 物件）適用於 `stackql shell`、`stackql exec`、`stackql srv` 與 `stackql mcp`。對於 Claude Desktop 和類似的 MCP 用戶端，請在伺服器設定的 `env` 區塊中設定 `GOOGLE_CREDENTIALS`。在 CI 中，請從平台的祕密存放區取得金鑰，切勿提交金鑰。

## 相關概念 {#related-concepts}

- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的對應做法
- [如何讓 StackQL 向 Azure 進行驗證](/ai/how-tos/authenticate-stackql-to-azure)：Azure 的對應做法
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 部署中的憑證
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：為什麼必要參數會出現在 WHERE 子句中
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：包括驗證失敗
