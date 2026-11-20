---
title: 使用 Provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "在 StackQL 中使用 Provider：從登錄庫提取 Provider，以環境變數或憑證進行驗證，並查詢其服務與資源。"
image: "/img/stackql-featured-image.png"
---

另請參閱：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider 登錄庫 ]](/providers)

StackQL 查詢可以針對多種不同的雲端服務供應商執行，包括 Google Cloud Platform、Amazon Web Services、Microsoft Azure、Digital Ocean 等。StackQL Provider 規格是從 [**StackQL Provider 登錄庫**](/providers) 提取的，提取方式可以是使用 [__`REGISTRY PULL`__](/language-spec/registry) StackQL 命令，或是使用 [__`stackql registry`__](/command-line-usage/registry) 命令列公用程式。


## 安裝 Provider {#installing-a-provider}

若要查看可用 Provider 的清單，請執行下列命令：  

```sql
REGISTRY LIST;
```

若要安裝 Provider，請執行下列命令：

```sql
REGISTRY PULL okta;
```

若要查看目前系統上已安裝的 Provider，請執行下列命令：

```sql
SHOW PROVIDERS;
```

## 向 Provider 進行驗證 {#authenticating-to-a-provider}

向雲端服務供應商進行驗證，通常是 StackQL 工作流程的第一步。[`SHOW`](/language-spec/show) 與 [`DESCRIBE`](/language-spec/describe) 等中繼命令不需驗證即可對 Provider 執行，但 [`SELECT`](/language-spec/select)、[`INSERT`](/language-spec/insert) 與 [`EXEC`](/language-spec/exec) 命令通常需要已驗證的工作階段。  

驗證是使用系統上設定的環境變數（或在 CI 系統中以祕密形式提供的變數）來執行，每個 Provider 都有各自預設的專用驗證變數，因此請透過 [StackQL Provider 登錄庫文件](/providers) 查閱您所用 Provider 的文件。也可以在啟動時使用 [`--env.file`](/command-line-usage/global-flags) 全域旗標，從 dotenv 樣式的檔案載入這些變數。  

:::tip

您可以設定每個 Provider 的驗證變數，在同一個互動式 shell 或非互動式批次工作流程中向多個 Provider 進行驗證，例如：  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

當您在單一查詢中聯結多個 Provider 的資料時，這特別有用。

:::
