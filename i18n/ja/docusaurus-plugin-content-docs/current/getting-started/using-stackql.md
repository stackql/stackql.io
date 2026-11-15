---
title: StackQL の使用
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の使用：シェルで対話的に、exec で非対話的に、またはサーバーとしてクエリを実行し、その過程でプロバイダー、サービス、リソースを調べます。"
image: "/img/stackql-featured-image.png"
---
関連項目：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL には、StackQL 対話型シェル、StackQL 文を使用する StackQL コマンドライン、入力ファイル（IQL ファイル）での 1 つ以上の StackQL 文の実行という、いくつかの使用モードがあります。各モードについては、以下で詳しく説明します。

- StackQL 対話型シェルの使用（[`stackql shell`](/command-line-usage/shell)）
- StackQL コマンドラインユーティリティの使用（[`stackql exec`](/command-line-usage/exec)）
- 入力ファイル（IQL ファイル）の使用（[`stackql exec -i`](/command-line-usage/exec)）

### StackQL 対話型シェルの使用 {#using-the-stackql-interactive-shell}
StackQL シェルは、エンジニアがクラウドリソースをデプロイして操作するための対話型プログラミング環境を提供します。また、（[`SELECT`](/language-spec/select) 文を使用した）クラウド環境の対話的な分析も可能にします。シェルは、機能的には MySQL シェルやその他の REPL（Read Evaluate Print Loop）シェルに似ています。シェルは有効な StackQL 文を受け付け、結果をユーザーに返します。文は複数行にわたって記述でき、終端のセミコロンが入力されると実行されます。

StackQL シェルは、次のように `shell` コマンドで起動できます（プロバイダーの認証については、[StackQL Provider Registry](/providers) を参照してください）。

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
クラウドベンダーに認証済みのシェルに入ると、StackQL クエリ（`SELECT` クエリ、または Infrastructure as Code の DDL 操作）の実行を開始できます。

StackQL シェルはデフォルトで表形式の結果を返しますが、`--output` フラグを使用すると `json`、`jsonl`、`csv`、`text` の出力形式も利用できます。[出力モード](/getting-started/output-modes)を参照してください。

次のように、`--colorscheme` フラグを使用して StackQL シェルの配色を変更できます。

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### `.stackqlrc` 初期化ファイル {#the-stackqlrc-initialization-file}

前述の出力形式や配色など、StackQL のコマンドオプションのデフォルトは、`stackqlrc` 初期化ファイルで設定できます。このファイルが StackQL 実行ファイルの使用するカレントディレクトリに存在する場合は、自動的に読み込まれます。または、次のように `--configfile` フラグを使用して参照することもできます。

```shell
# Specifing a Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
コマンドオプションの詳細については、グローバルフラグのトピックを参照してください。

### StackQL コマンドラインユーティリティの使用 {#using-the-stackql-command-line-utility}
StackQL のコマンドは、StackQL 実行ファイルの exec コマンドを使用して非対話的に実行できます。個々のコマンドをセミコロン（`;`）で終端すれば、複数のコマンドをバッチで実行できます。`--output` フラグなどのコマンドフラグは、コマンドラインで指定することも、前述の `.stackqlrc` 初期化ファイルで指定することもできます。

StackQL 実行ファイルの出力は、使用しているシステムの stdout に送られます。この出力は、BASH または PowerShell スクリプト内で解析できます。

StackQL コマンドを含む Windows PowerShell スクリプトの例を次に示します。

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$keyfilepath = "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--keyfilepath ${keyfilepath} --output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
上記の PowerShell コマンドは、Compute Engine インスタンスを選択し、その出力を `Out-GridView` で表示する例です。

### 入力ファイル（IQL ファイル）の使用 {#using-an-input-file-iql-file}
StackQL の入力ファイルを使用すると、セミコロンで終端された複数のコマンドを実行できます。これは、非対話型またはバッチ型のインフラストラクチャプログラミングのもう 1 つの選択肢です。入力ファイルは、次のように `-i` または `--infile` オプションで指定します。

```shell
# create an iql file
echo "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--keyfilepath /mnt/c/tmp/stackql-demo.json \
--output json | jq '.[] | .name'
```
