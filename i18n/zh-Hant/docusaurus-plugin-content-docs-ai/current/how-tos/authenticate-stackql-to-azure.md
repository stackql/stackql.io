---
title: 如何讓 StackQL 向 Azure 進行驗證
description: StackQL 透過 AZURE_TENANT_ID、AZURE_CLIENT_ID 與 AZURE_CLIENT_SECRET 環境變數，使用 Microsoft Entra ID 服務主體向 Azure 進行驗證。
keywords: [stackql, azure 驗證, 服務主體, entra id, 環境變數]
proficiencyLevel: Beginner
faq:
  - question: StackQL 需要哪個 Azure 角色？
    answer: 您的查詢所需的角色，範圍盡可能縮小。訂用帳戶範圍的內建 Reader 角色足以執行資源清單與稽核查詢；變更則需要目標資源類型上的 Contributor 角色或自訂角色。
  - question: 為什麼 Azure 查詢的 WHERE 子句中需要 subscriptionId？
    answer: Azure Resource Manager API 以訂用帳戶（通常還有資源群組）為範圍，因此這些值會以底層 API 呼叫的必要參數出現，而非驗證設定。SHOW METHODS IN azure.compute.virtual_machines 會列出每個方法的必要參數。
  - question: StackQL 可以在 Azure Cloud Shell 中運作嗎？
    answer: 可以。StackQL 可以像任何 Linux 二進位檔一樣在 Azure Cloud Shell 中執行；請透過本頁所述的環境變數提供服務主體憑證。
---

# 如何讓 StackQL 向 Azure 進行驗證

StackQL 使用 Microsoft Entra ID（前身為 Azure AD）服務主體向 Azure 進行驗證，並透過三個環境變數提供：`AZURE_TENANT_ID`、`AZURE_CLIENT_ID` 與 `AZURE_CLIENT_SECRET`。

## 步驟 {#steps}

1. 建立服務主體，並授予它目標訂用帳戶上的角色（唯讀查詢只需 `Reader` 角色即可）：

```bash
az ad sp create-for-rbac --name stackql-sp --role Reader \
  --scopes /subscriptions/<subscription-id>
```

2. 匯出它傳回的憑證：

```bash
export AZURE_TENANT_ID=your-tenant-id
export AZURE_CLIENT_ID=your-client-id
export AZURE_CLIENT_SECRET=your-client-secret
```

3. 提取 Azure Provider（僅首次使用時）：

```sql
REGISTRY PULL azure;
```

4. 以查詢確認：

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

`subscriptionId` 述詞是 Azure API 的必要路由參數，不屬於驗證的一部分。以資源群組為範圍的方法另外需要 `resourceGroupName`；執行 `SHOW METHODS IN azure.compute.virtual_machines` 可查看每個存取方法的必要參數（`list_all` 只需要 `subscriptionId`；`list` 還需要 `resourceGroupName`；`get` 需要 `vmName`）。

## 變數的適用範圍 {#where-the-variables-apply}

這些變數在 `stackql shell`、`stackql exec`、`stackql srv`（PostgreSQL 線路協定伺服器）與 `stackql mcp` 中的作用完全相同。對於 Claude Desktop 等 MCP 用戶端，請將它們放在 `claude_desktop_config.json` 中伺服器項目的 `env` 區塊。在 CI 中，請從管線的祕密存放區取得它們。

## 相關概念 {#related-concepts}

- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的對應做法
- [如何讓 StackQL 向 Google Cloud 進行驗證](/ai/how-tos/authenticate-stackql-to-google-cloud)：GCP 的對應做法
- [如何搭配 Claude 使用 StackQL](/ai/how-tos/use-stackql-with-claude)：將 Azure 憑證傳遞給 MCP 工作階段
- [控制平面與資料平面](/ai/canonical-definitions/control-plane-vs-data-plane)：Resource Manager API 涵蓋的範圍
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：包括驗證失敗
