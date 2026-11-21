---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "stackql exec コマンドは、出力形式、認証、プロバイダーレジストリのフラグを使って、1 つの StackQL 文または複数の文からなるスクリプトを実行します。"
image: "/img/stackql-featured-image.png"
---

StackQL クエリを、単一の文として、または StackQL スクリプト（IQL ファイル）から文のバッチとして実行するためのコマンドです。  

* * * 

### 構文 {#syntax}

`stackql exec [query] [flags]`

* * *

### 引数 {#arguments}

| 引数 | 説明 | 例 |
|--|--|--|
|`query`|実行する StackQL クエリ<br/>または実行するコマンド | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_なお、クエリは単一引用符または二重引用符で囲む必要があります_ |    


### フラグ {#flags}

| フラグ | 説明 |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|出力モード。有効な値は `table`（デフォルト）、`json`、`jsonl`（改行区切りの JSON。`ndjson` もエイリアスとして受け付けます）、`csv`、`text`、`otel`（OpenTelemetry のログレコード。[出力モード](/getting-started/output-modes#otel-output)を参照）です。例：`--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|必要に応じて、指定したファイルからクエリを実行します。指定した場合、引数として渡したクエリは無視されます。例：`--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|`json` または `jsonnet` ファイルを使用した、コマンドのソースデータ。例：`--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|ドライランフラグ。プリプロセッサーだけが実行され、その出力が返されます |
|<span class="nowrap">`--var`</span>|外部変数（環境変数またはシークレットを指定できます）。`--iqldata` フラグで指定した jsonnet ファイルによって読み込まれます。例：`--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|必要に応じて結果を出力するファイル（デフォルトでは、結果はコンソールにのみ出力されます）。例：`--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|ヘルプ情報を表示します|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|詳細モードでクエリを実行し、追加の出力を stdout に送ります。`-f` オプションを選択した場合、この追加のログ情報はクエリ結果とともに出力ファイルに書き込まれます|
<br/>
<br/>
> そのほかのオプションについては、[グローバルフラグ](/command-line-usage/global-flags)を参照してください。


* * *

### 例 {#examples}

クエリ結果をテーブル形式でコンソールに出力します：
```shell
stackql exec "SHOW services IN google"
```

クエリ結果を JSON 形式でコンソールに出力します：
```shell
stackql exec "SHOW services IN google" -o json
```

クエリ結果を JSON 形式でファイルに出力します：
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

入力ファイル（IQL ファイル）からクエリを実行し、結果を JSON 形式でファイルに出力します：
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### 終了ステータス {#exit-status}

`exec` は、入力を処理し終えるとステータス `0` で終了します。個々の文のエラーはコンソールに書き込まれるため、スクリプトでは出力を確認する必要があります。`v1.0` 以降では、[`SET`](/language-spec/set) または [`UNSET`](/language-spec/unset) の設定文が失敗した場合、プレビュー版の IaC 実行が失敗したリソースを報告した場合、または[アクセス管理](/command-line-usage/access-management)が有効で、いずれかの文が失敗するか拒否された場合に、ステータス `1` で終了します。
