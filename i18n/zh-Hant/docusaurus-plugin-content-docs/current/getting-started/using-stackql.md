---
title: 使用 StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "使用 StackQL：在 shell 中以互動方式執行查詢、以 exec 非互動方式執行或作為伺服器執行，並隨時探索 Provider、服務與資源。"
image: "/img/stackql-featured-image.png"
---
另請參閱：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL 有幾種使用模式，包括：StackQL 互動式 shell、透過 StackQL 陳述式使用 StackQL 命令列，或執行輸入檔案（IQL 檔案）中的一個或多個 StackQL 陳述式。以下將更詳細地說明每種模式：

- 使用 StackQL 互動式 shell（[`stackql shell`](/command-line-usage/shell)）
- 使用 StackQL 命令列公用程式（[`stackql exec`](/command-line-usage/exec)）
- 使用輸入檔案（IQL 檔案）（[`stackql exec -i`](/command-line-usage/exec)）

### 使用 StackQL 互動式 shell {#using-the-stackql-interactive-shell}
StackQL shell 提供互動式程式設計環境，讓工程師部署雲端資源並與其互動，也能（使用 [`SELECT`](/language-spec/select) 陳述式）以互動方式分析其雲端環境。此 shell 在功能上類似 MySQL shell 或其他 REPL（Read Evaluate Print Loop）shell。shell 接受有效的 StackQL 陳述式，並將結果傳回給使用者。陳述式可以跨越多行，並在輸入分號結束字元後執行。

可以使用 `shell` 命令叫用 StackQL shell，如下所示（如需 Provider 驗證的相關資訊，請參閱 [StackQL Provider 登錄庫](/providers)）：

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
進入已向雲端服務供應商驗證的 shell 後，您就可以開始執行 StackQL 查詢（`SELECT` 查詢或 DDL 作業，也就是基礎架構即程式碼作業）。

根據預設，StackQL shell 會傳回表格式結果，但也可以使用 `--output` 旗標選擇 `json`、`jsonl`、`csv` 與 `text` 輸出格式，請參閱[輸出模式](/getting-started/output-modes)。  

### `.stackqlrc` 初始化檔案 {#the-stackqlrc-initialization-file}

StackQL 命令選項的預設值（例如輸出格式）可以使用 `.stackqlrc` 初始化檔案設定。如果此檔案存在於 StackQL 可執行檔所使用的目前目錄中，就會自動載入；也可以使用 `--configfile` 旗標指定此檔案，如下所示：

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
如需命令選項的詳細資訊，請參閱[全域旗標](/command-line-usage/global-flags)。

### 使用 StackQL 命令列公用程式 {#using-the-stackql-command-line-utility}
可以使用 StackQL 可執行檔的 exec 命令，以非互動方式執行 StackQL 命令。只要每個獨立命令都以分號（`;`）結尾，就可以在一個批次中執行多個命令。命令旗標（例如 `--output` 旗標）可以在命令列中指定，也可以使用先前討論的 `.stackqlrc` 初始化檔案指定。  

StackQL 可執行檔的輸出會傳送到所用系統的 stdout，可以在 BASH 或 PowerShell 指令碼中剖析此輸出。  

以下是包含 StackQL 命令的 Windows PowerShell 指令碼範例：  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
上述 PowerShell 命令示範如何選取 Compute Engine 執行個體，並使用 `Out-GridView` 顯示輸出。  

### 使用輸入檔案（IQL 檔案） {#using-an-input-file-iql-file}
StackQL 輸入檔案可用來執行數個以分號結尾的命令。這是非互動式或批次基礎架構程式設計的另一個選項。使用 `-i` 或 `--infile` 選項指定輸入檔案，如下所示：

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
