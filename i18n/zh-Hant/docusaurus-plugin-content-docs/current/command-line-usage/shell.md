---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql shell 命令會啟動互動式 StackQL 命令 shell，用於對您的雲端與 SaaS 帳戶執行查詢和 Provider 命令。"
image: "/img/stackql-featured-image.png"
---

用於啟動互動式 StackQL 命令 shell 的命令。如需詳細資訊，請參閱[使用 StackQL 互動式 shell](/getting-started/using-stackql#using-the-stackql-interactive-shell)。

* * * 

### 語法 {#syntax}

`stackql shell [flags]`

* * *

### 旗標 {#flags}

| 旗標 | 描述 |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|顯示說明資訊|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|以詳細模式執行查詢，並將額外輸出傳送至 stdout；若設定了全域 `-f`（`--outfile`）旗標，這些額外的記錄資訊會連同查詢結果一起寫入輸出檔案|
<br/>
<br/>
> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)

* * *

### 範例 {#examples}

啟動 StackQL shell，並使用服務帳戶金鑰檔案進行驗證。
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
