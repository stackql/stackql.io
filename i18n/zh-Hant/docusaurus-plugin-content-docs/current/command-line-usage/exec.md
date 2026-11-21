---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql exec 命令可執行單一 StackQL 陳述式或陳述式指令碼，並提供輸出格式、驗證和 Provider 登錄庫的旗標。"
image: "/img/stackql-featured-image.png"
---

用於執行 StackQL 查詢的命令，可以是單一陳述式，也可以是來自 StackQL 指令碼（IQL 檔案）的一批陳述式。  

* * * 

### 語法 {#syntax}

`stackql exec [query] [flags]`

* * *

### 引數 {#arguments}

| 引數 | 描述 | 範例 |
|--|--|--|
|`query`|要執行的 StackQL 查詢<br/>或命令 | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>*請注意，查詢必須以單引號或雙引號括住* |    


### 旗標 {#flags}

| 旗標 | 描述 |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|輸出模式，有效值包括：`table`（預設）、`json`、`jsonl`（以換行字元分隔的 JSON，也接受 `ndjson` 作為別名）、`csv`、`text` 和 `otel`（OpenTelemetry 記錄項目，請參閱[輸出模式](/getting-started/output-modes#otel-output)），範例：`--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|視需要從指定的檔案執行查詢；若指定此旗標，以引數提供的任何查詢都會被忽略，範例：`--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|使用 `json` 或 `jsonnet` 檔案作為命令的來源資料，範例：`--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|試執行旗標；只會執行前置處理器並傳回輸出 |
|<span class="nowrap">`--var`</span>|由 `--iqldata` 旗標所指定 jsonnet 檔案取用的外部變數（可以是環境變數或祕密），範例：`--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|視需要寫入結果的檔案（預設行為是僅將結果輸出至主控台），範例：`--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|顯示說明資訊|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|以詳細模式執行查詢，並將額外輸出傳送至 stdout；若選取 `-f` 選項，這些額外的記錄資訊會連同查詢結果一起寫入輸出檔案|
<br/>
<br/>
> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)


* * *

### 範例 {#examples}

以表格格式將查詢結果輸出至主控台：
```shell
stackql exec "SHOW services IN google"
```

以 json 格式將查詢結果輸出至主控台：
```shell
stackql exec "SHOW services IN google" -o json
```

以 json 格式將查詢結果輸出至檔案：
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

從輸入檔案（IQL 檔案）執行查詢，並以 json 格式將結果輸出至檔案：
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### 結束狀態 {#exit-status}

`exec` 在處理完輸入後會以狀態 `0` 結束；個別陳述式的錯誤會寫入主控台，因此指令碼應檢查輸出。自 `v1.0` 起，當 [`SET`](/language-spec/set) 或 [`UNSET`](/language-spec/unset) 設定陳述式失敗、預覽 IaC 執行回報資源失敗，或在已啟用[存取管理](/command-line-usage/access-management)的情況下有任何陳述式失敗或遭拒時，此命令會以狀態 `1` 結束。
