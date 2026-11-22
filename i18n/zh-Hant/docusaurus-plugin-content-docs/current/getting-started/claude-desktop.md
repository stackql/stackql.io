---
title: 在 Claude Desktop 中使用 StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - 多雲端
description: 搭配 Claude Desktop 使用 StackQL，以自然語言查詢及佈建雲端資源
image: "/img/stackql-featured-image.png"
---

StackQL 透過 Model Context Protocol（MCP）與 Claude Desktop 整合，讓您能以自然語言查詢及佈建跨多個雲端服務供應商的雲端資源。這個強大的組合讓您能以對話方式與雲端基礎架構互動，同時在底層運用 StackQL 以 SQL 為基礎的介面。

共有三種安裝選項，以下依建議順序說明：從 Anthropic Connector Directory 安裝、從下載的安裝套件安裝，或手動註冊本機安裝的 StackQL 二進位檔。

## 從 Connector Directory 安裝（建議） {#install-from-the-connector-directory-recommended}

在 Claude Desktop 中使用 StackQL MCP 伺服器最簡單的方式，是從 Anthropic Connector Directory 安裝。在 Claude Desktop 中前往 **Settings -> Connectors -> Add -> Browse Connectors**，搜尋 **StackQL**，然後按一下 **Install**。不需要下載任何檔案，也不需要另外安裝 StackQL。

![Anthropic Connector Directory 中的 StackQL](/img/stackql-anthropic-directory-listing.png)

您也可以直接在 [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) 檢視此上架頁面。

這會以 in-process 方式為您的平台安裝 StackQL MCP 伺服器：上架頁面提供一個合併的多平台安裝套件，內含適用於 macOS universal、Windows x64 以及 Linux x64 和 arm64 的已簽署二進位檔，Claude Desktop 會自動為您的電腦選取正確的版本。若是原生 Windows ARM64，請從[下方的下載項目](#install-from-a-downloaded-bundle)安裝另外提供的 `windows-arm64` 安裝套件。Windows 二進位檔經過 Authenticode 簽署，macOS 二進位檔經過 Apple 公證，與獨立的 StackQL 發行版本使用相同的二進位檔。

![安裝 StackQL connector](/img/stackql-anthropic-directory-install.png)

從目錄安裝的 connector 會繼承 Claude Desktop 的環境，因此請在啟動 Claude Desktop 之前，於作業系統層級設定 Provider 所需的環境變數（請參閱[各 Provider 專屬憑證](#provider-specific-credentials)）。Claude Desktop 會在啟動時擷取其環境：變更變數後，您必須完全結束 Claude Desktop（在 Windows 上包括系統匣圖示）並重新啟動。要求代理程式執行 [`reload_credentials`](/mcp/reload_credentials) 會回報每個 Provider 的憑證狀態，這是確認伺服器能看到與看不到哪些內容最快的方式。

Claude Desktop 會宣告 MCP 徵詢（elicitation）功能，因此預設的伺服器模式（`safe`）會在每次變更時要求您核准（請參閱[伺服器模式](/command-line-usage/mcp#server-modes)）。

## 從下載的安裝套件安裝 {#install-from-a-downloaded-bundle}

若要固定特定版本、在無法存取 Connector Directory 的情況下安裝，或在安裝前自行驗證總和檢查碼，請使用此安裝選項。

預先建置的 MCP Bundle（`.mcpb`）是可一鍵安裝的套件，內含已簽署的 `stackql` 二進位檔與伺服器設定。不需要另外安裝 StackQL。

從最新的發行版本下載適用於您平台的安裝套件：

| 平台 | 安裝套件 |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64（原生） | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

接著在 Claude Desktop 中前往 **Settings -> Extensions -> Install Extension**，然後選取下載的檔案。內嵌的二進位檔經過 Authenticode 簽署（Windows）及 Apple 公證（macOS）；在[發行版本頁面](https://github.com/stackql/stackql/releases/latest)上，每個安裝套件旁都附有 `.sha256` 總和檢查碼。若要在安裝前驗證：

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

此伺服器也以 `io.github.stackql/stackql-mcp` 的名稱列於 [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)，該登錄庫固定了這些總和檢查碼。

## 手動設定 {#manual-configuration}

如果您已安裝 StackQL，可以直接在 Claude Desktop 的設定檔中註冊 StackQL MCP 伺服器。

### 必要條件 {#prerequisites}

在 Claude Desktop 中設定 StackQL 之前，請確認您已具備：

1. **已安裝 StackQL**：從 [stackql.io](/installing-stackql) 下載並安裝 StackQL，或使用套件管理員
2. **Claude Desktop**：從 [Anthropic 網站](https://claude.ai/desktop) 下載
3. **雲端服務供應商憑證**：您要使用之雲端服務供應商的 API 金鑰或憑證

執行下列命令，確認 StackQL 位於系統 PATH 中：

```bash
stackql --version
```

### 設定 MCP 伺服器 {#setting-up-the-mcp-server}

若要讓 Claude Desktop 與 StackQL 通訊，您需要在 Claude Desktop 設定檔中設定 MCP 伺服器。

#### Windows {#windows}

編輯位於下列位置的設定檔：
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

編輯位於下列位置的設定檔：
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

編輯位於下列位置的設定檔：
```
~/.config/Claude/claude_desktop_config.json
```

### 設定範例 {#configuration-example}

將下列設定新增至您的 `claude_desktop_config.json` 檔案：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
您只需要包含您打算使用之雲端服務供應商的環境變數。請從設定中移除任何未使用的 Provider 憑證。
:::

:::warning
絕對不要將含有實際憑證的 `claude_desktop_config.json` 檔案提交至版本控制。請採用安全的憑證管理做法。
:::

Claude Desktop 會宣告 MCP 徵詢功能，因此預設的伺服器模式（`safe`）會在每次變更時要求您核准。若只需資源清單的存取權限，請切換為 `mode: read_only`；若要略過核准要求，請切換為 `mode: full_access`（請參閱[伺服器模式](/command-line-usage/mcp#server-modes)）。

### 使用憑證檔案（`--env.file`） {#using-a-credentials-file---envfile}

除了 `env` 區塊之外，另一種做法（在 Windows 上也是建議的做法）是將憑證存放在 dotenv 格式的檔案中，並以 [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) 旗標傳入：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

搭配一個最精簡的憑證檔案：

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

這種做法優於 `env` 區塊的原因：

- 變更 `env` 區塊後，必須完全重新啟動 Claude Desktop（包括結束系統匣圖示），MCP 子處理程序才會看到變更；而在 Windows 上，`setx` 寫入的是登錄庫，永遠不會傳達到正在執行的 Desktop 處理程序。
- `--env.file` 檔案可以隨時建立或輪替，伺服器啟動時甚至不需要存在；只要要求代理程式執行 [`reload_credentials`](/mcp/reload_credentials)，即可在不重新啟動的情況下套用變更。

:::note

`--env.file` 和 `reload_credentials` 適用於 `v0.10.542` 之後的 StackQL 發行版本。

:::

### 各 Provider 專屬憑證 {#provider-specific-credentials}

不同的雲端服務供應商需要不同的驗證方法，請參閱 Provider 文件（[https://stackql.io/providers](/providers)），了解您所需 Provider 要求的環境變數。


### 重新啟動 Claude Desktop {#restarting-claude-desktop}

更新設定檔後，請重新啟動 Claude Desktop 以載入新的 MCP 伺服器設定。當您開始新的對話時，StackQL MCP 伺服器會自動啟動。

## 您可以做什麼 {#what-you-can-do}

設定完成後，您可以在 Claude Desktop 中使用自然語言：

### 查詢雲端資源 {#query-cloud-resources}

要求 Claude 擷取雲端基礎架構的相關資訊：

- 「顯示我在 us-central1 區域中所有的 GCP 運算執行個體」
- 「列出我 AWS 帳戶中所有的 S3 儲存貯體」
- 「我的訂用帳戶中有哪些 Azure 虛擬機器正在執行？」
- 「顯示 default 命名空間中所有的 Kubernetes Pod」

### 佈建資源 {#provision-resources}

使用對話式命令建立及部署雲端資源：

- 「在 us-west-2 中建立名為『my-data-bucket』的 S3 儲存貯體」
- 「部署一個啟用版本控制的 GCP Cloud Storage 儲存貯體」
- 「在 us-east-1 中啟動一個小型 EC2 執行個體」
- 「在 westus2 中建立 Azure 資源群組」

### 分析與稽核 {#analyze-and-audit}

執行安全性稽核與合規性檢查：

- 「顯示所有可公開存取的 S3 儲存貯體」
- 「列出 90 天未登入的 IAM 使用者」
- 「找出所有未正確加上標籤的 GCP 執行個體」
- 「找出未啟用備份的 Azure VM」

### 跨雲端作業 {#cross-cloud-operations}

查詢及比較多個雲端服務供應商的資源：

- 「顯示 AWS、GCP 和 Azure 中所有的運算執行個體」
- 「比較 AWS S3 與 GCP Cloud Storage 的儲存成本」
- 「列出所有雲端服務供應商中的所有資料庫」

### 基礎架構即程式碼 {#infrastructure-as-code}

產生並執行基礎架構部署查詢：

- 「建立一個包含 VPC、子網路和 EC2 執行個體的資源堆疊」
- 「部署一個含節點集區的完整 GKE 叢集」
- 「在 AWS 中設定多區域部署」

### 修改資源 {#modify-resources}

更新現有的雲端資源：

- 「為所有未加上標籤的 EC2 執行個體新增標籤」
- 「為 S3 儲存貯體『my-bucket』啟用加密」
- 「更新 GCP 執行個體『web-server-1』的機器類型」
- 「將我的 Cloud Storage 儲存貯體的 ACL 變更為私人」

### 刪除資源 {#delete-resources}

移除不再需要的雲端資源：

- 「刪除 S3 儲存貯體『temp-bucket』」
- 「移除所有已停止的 EC2 執行個體」
- 「清除未使用的 GCP 磁碟」

## 對話範例 {#example-conversation}

以下範例說明您可以如何透過 StackQL 與 Claude Desktop 互動：

**您**：「可以顯示我所有的 GCP 運算執行個體嗎？」

**Claude**：*執行 StackQL 查詢，並以格式化表格傳回結果，顯示執行個體名稱、可用區域、機器類型和狀態*

**您**：「在 us-central1 區域中建立一個名為『analytics-data-2025』的新 Cloud Storage 儲存貯體，並使用統一儲存貯體層級存取權」

**Claude**：*執行 StackQL INSERT 陳述式以建立儲存貯體，並確認建立完成*

**您**：「現在列出我所有的 Cloud Storage 儲存貯體」

**Claude**：*執行 SELECT 查詢，並顯示所有儲存貯體，包括剛建立的那一個*

## 提示與最佳做法 {#tips-and-best-practices}

1. **具體明確**：您的要求越具體，得到的結果就越好。請盡可能包含資源名稱、區域和具體屬性。

2. **使用自然語言**：您不需要了解 SQL 語法，Claude 會將您的自然語言要求轉譯為 StackQL 查詢。

3. **執行前先檢閱**：對於破壞性作業（DELETE、UPDATE），Claude 通常會在繼續之前向您顯示將要執行的內容。

4. **多步驟作業**：您可以在單一對話中串連多項作業。Claude 會在整個工作階段中維持上下文。

5. **錯誤處理**：如果查詢失敗，Claude 可以協助進行疑難排解，並根據錯誤訊息建議修正方式。

6. **探索功能**：詢問 Claude「我可以用 StackQL 做什麼？」，以取得針對您已設定之 Provider 的建議。

## 疑難排解 {#troubleshooting}

### MCP 伺服器無法啟動 {#mcp-server-not-starting}

如果 Claude Desktop 無法連線到 StackQL MCP 伺服器：

1. 確認 StackQL 位於您的 PATH 中
2. 檢查您的 `claude_desktop_config.json` 檔案是否為有效的 JSON
3. 確認設定中的檔案路徑對您的作業系統而言是正確的
4. 檢閱 Claude Desktop 記錄中的錯誤訊息

### 驗證錯誤 {#authentication-errors}

如果您收到驗證錯誤：

1. 確認您的雲端服務供應商憑證正確無誤
2. 確認憑證檔案存在於指定的路徑
3. 檢查設定中的環境變數格式是否正確；要求代理程式執行 [`reload_credentials`](/mcp/reload_credentials) 會回報每個 Provider 的憑證狀態，這是查看缺少哪個變數最快的方式
4. 確認您的憑證具有必要的權限

### 權限錯誤 {#permission-errors}

如果作業因權限不足而失敗：

1. 確認您的服務帳戶或 IAM 使用者具有必要的權限
2. 檢查 Provider 專屬的 IAM 政策
3. 確認已在您的雲端專案中啟用 API 服務

## 深入了解 {#learn-more}

- [StackQL MCP 命令參考](/command-line-usage/mcp)
- [StackQL Provider 登錄庫](/providers)
- [語言規格](/language-spec/select)
- [Model Context Protocol 文件](https://modelcontextprotocol.io)

## 安全性考量 {#security-considerations}

:::caution
- 安全地儲存憑證，絕對不要將其提交至版本控制
- 對服務帳戶採用最低權限存取原則
- 定期輪替憑證與 API 金鑰
- 監控 MCP 伺服器記錄中的異常活動
- 考慮使用雲端服務供應商的憑證管理工具（例如 Secret Manager、Parameter Store）
:::
