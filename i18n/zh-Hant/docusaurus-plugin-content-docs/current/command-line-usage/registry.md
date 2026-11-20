---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "stackql registry 命令可從命令列列出、提取及管理 StackQL Provider 登錄庫中的 Provider。"
image: "/img/stackql-featured-image.png"
---

用於與 StackQL Provider 登錄庫互動的命令。登錄庫的位置，以及是否驗證 Provider 文件的簽章，皆透過 [`--registry`](/command-line-usage/global-flags#--registry) 全域旗標設定。啟用[存取管理](/command-line-usage/access-management)時無法使用 `registry` 命令；請改為在受保護的 `exec` 或 `shell` 工作階段中使用 `REGISTRY PULL`。

* * * 

### 語法 {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### 引數 {#arguments}

| 引數 | 描述 | 範例 |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|要執行的登錄庫作業（`list` 或 `pull`） | `list` 或 `pull` |    
|<span class="nowrap">`provider`</span>|使用 `pull` 子命令安裝的 Provider| `okta` |
|<span class="nowrap">`version`</span>|選用，使用 `pull` 子命令安裝的 Provider 版本 *（StackQL Provider 文件版本）* | `v23.03.00121` |


### 旗標 {#flags}

| 旗標 | 描述 |
|--|--|
|<span class="nowrap">`-h, --help`</span>|顯示說明資訊|
<br/>
<br/>
> 如需其他選項，請參閱[全域旗標](/command-line-usage/global-flags)

* * *

### 範例 {#examples}

列出公用登錄庫中可用的 Provider：
```shell
stackql registry list
```

從公用登錄庫提取 Provider 的最新版本：
```shell
stackql registry pull okta
```
