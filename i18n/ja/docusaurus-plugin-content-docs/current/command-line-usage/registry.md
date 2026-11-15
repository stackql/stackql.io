---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "stackql registry コマンドは、コマンドラインから StackQL Provider Registry のプロバイダーを一覧表示、プル、管理します。"
image: "/img/stackql-featured-image.png"
---

StackQL Provider Registry を操作するためのコマンドです。レジストリの場所と、プロバイダードキュメントの署名を検証するかどうかは、[`--registry`](/command-line-usage/global-flags#--registry) グローバルフラグで設定します。[アクセス管理](/command-line-usage/access-management)が有効な間は `registry` コマンドを使用できません。代わりに、保護された `exec` または `shell` セッションで `REGISTRY PULL` を使用してください。

* * * 

### 構文 {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### 引数 {#arguments}

| 引数 | 説明 | 例 |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|実行するレジストリ操作（`list` または `pull`） | `list` または `pull` |    
|<span class="nowrap">`provider`</span>|`pull` サブコマンドでインストールするプロバイダー| `okta` |
|<span class="nowrap">`version`</span>|省略可能。`pull` サブコマンドでインストールするプロバイダーのバージョン *（StackQL プロバイダードキュメントのバージョン）* | `v23.03.00121` |


### フラグ {#flags}

| フラグ | 説明 |
|--|--|
|<span class="nowrap">`-H, --help`</span>|ヘルプ情報を表示する|
<br/>
<br/>
> その他のオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください

* * *

### 例 {#examples}

パブリックレジストリで利用可能なプロバイダーを一覧表示します。
```shell
stackql registry list
```

パブリックレジストリからプロバイダーの最新バージョンをプルします。
```shell
stackql registry pull okta
```
