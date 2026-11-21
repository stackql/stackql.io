---
title: 如何讓 StackQL 向 AWS 進行驗證
description: StackQL 使用標準的 AWS_ACCESS_KEY_ID 和 AWS_SECRET_ACCESS_KEY 環境變數向 AWS 驗證，並支援擔任角色和 AWS CloudShell 使用的工作階段權杖。
keywords: [stackql, AWS 驗證, AWS 存取金鑰, 環境變數, cloudshell, iam]
proficiencyLevel: Beginner
faq:
  - question: StackQL 存取 AWS 需要哪些 IAM 權限？
    answer: 只需要您所執行作業的權限。對於唯讀的資源清單和稽核查詢，AWS 受管的 ReadOnlyAccess 政策（或範圍更小的自訂政策）就已足夠。INSERT、UPDATE、DELETE 和 EXEC 作業需要目標服務上對應的寫入權限。
  - question: StackQL 支援臨時 AWS 憑證嗎？
    answer: 支援。由 sts assume-role 核發或在 AWS CloudShell 中自動提供的憑證，只要在啟動 StackQL 之前設定包括工作階段權杖在內的標準環境變數即可使用。
  - question: 可以在同一個工作階段中同時向 AWS 和其他 Provider 驗證嗎？
    answer: 可以。每個 Provider 都會讀取自己的環境變數，因此同時匯出 AWS、GitHub 和 Azure 憑證，即可在單一 shell 或伺服器工作階段中進行跨 Provider 的查詢和聯結。
---

# 如何讓 StackQL 向 AWS 進行驗證

StackQL 使用標準的 AWS 憑證環境變數向 AWS 驗證。請在啟動 StackQL shell、`exec` 命令、伺服器或 MCP 伺服器之前設定這些變數；不需要任何設定檔。

## 步驟 {#steps}

1. 建立或找出具有您的查詢所需權限的 IAM 主體（使用者或角色）。若只需唯讀查詢，使用 `ReadOnlyAccess` 受管政策或範圍更小的同等政策即可。

2. 將憑證匯出為環境變數：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

若使用臨時憑證（透過 `sts assume-role` 擔任的角色，或會自動設定這些變數的 AWS CloudShell 工作階段），也需匯出工作階段權杖：

```bash
export AWS_SESSION_TOKEN=YOURSESSIONTOKEN
```

3. 提取 AWS Provider（僅首次使用時需要）：

```sql
REGISTRY PULL aws;
```

4. 以查詢確認：

```sql
SELECT instance_id, instance_type
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

傳回成功的結果集即確認驗證成功。請注意，AWS 查詢需要在 `WHERE` 子句中加入 `region` 述詞，這是路由參數，而不是憑證設定。

## 變數的適用範圍 {#where-the-variables-apply}

相同的環境變數適用於所有 StackQL 執行模式：

- **互動式 shell**：`stackql shell`
- **批次**：`stackql exec "..."`
- **伺服器模式**：`stackql srv`（PostgreSQL 線路協定）
- **MCP 伺服器**：`stackql mcp`，或透過 MCP 用戶端設定（例如 `claude_desktop_config.json`）中的 `env` 區塊

在 CI 系統中，請從平台的祕密存放區取得這些變數，而不要將其寫死在程式碼中。中繼資料作業（`SHOW`、`DESCRIBE`、`REGISTRY`）不需要憑證即可運作；只有資料作業（`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`EXEC`）需要已驗證的工作階段。

## 相關概念 {#related-concepts}

- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：驗證後的第一批查詢
- [如何使用 StackQL 查詢 S3 儲存貯體](/ai/how-tos/query-s3-buckets-with-stackql)：S3 查詢模式
- [如何讓 StackQL 向 Azure 進行驗證](/ai/how-tos/authenticate-stackql-to-azure)：Azure 的對應做法
- [如何讓 StackQL 向 Google Cloud 進行驗證](/ai/how-tos/authenticate-stackql-to-google-cloud)：GCP 的對應做法
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：包括驗證失敗
