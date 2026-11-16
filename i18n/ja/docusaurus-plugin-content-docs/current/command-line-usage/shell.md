---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "stackql shell コマンドは、クラウドや SaaS のアカウントに対してクエリやプロバイダーコマンドを実行するための、対話型 StackQL コマンドシェルを起動します。"
image: "/img/stackql-featured-image.png"
---

対話型 StackQL コマンドシェルを起動するためのコマンドです。詳細については、[StackQL 対話型シェルの使用](/getting-started/using-stackql#using-the-stackql-interactive-shell)を参照してください。

* * * 

### 構文 {#syntax}

`stackql shell [flags]`

* * *

### フラグ {#flags}

| フラグ | 説明 |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|ヘルプ情報を表示する|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|詳細モードでクエリを実行し、追加の出力を stdout に送信します。グローバルフラグ `-f`（`--outfile`）を設定した場合、この追加のログ情報はクエリ結果とともに出力ファイルに書き込まれます。|
<br/>
<br/>
> その他のオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください

* * *

### 例 {#examples}

認証にサービスアカウントキーファイルを使用して、StackQL シェルを起動します。
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
