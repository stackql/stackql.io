---
title: AWS Cloud Shell での StackQL
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - cspm
  - aws
  - amazon web services
description: "ログイン中のユーザーとして AWS CloudShell で StackQL を実行する方法：Linux パッケージのダウンロード、stackql shell の使用、IAM ロールの引き受け、exec によるバッチクエリの実行。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# AWS Cloud Shell で StackQL を使用する

このガイドでは、AWS Cloud Shell 内から StackQL を使用する方法を示します。AWS Console へのログインセッションのユーザーコンテキストを使用するか、別の IAM ロールを引き受けて実行します。StackQL は、SQL 構文を使ってクラウドインフラストラクチャとリソースのクエリおよびデプロイを可能にする強力な開発ツールです。    

動作確認済み <span class="tutorial_tested_on">組み込み SQL バックエンド</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary><b>AWS Cloud Shell</b> とは？</summary>

> AWS Cloud Shell は、Amazon Web Services（AWS）が提供する、ブラウザーベースで事前認証済みのシェルサービスです。開発者や IT 担当者は、Web ブラウザーから直接クラウドリソースを管理できます。AWS Cloud Shell を使用すると、ローカルマシンに AWS CLI（Command Line Interface）をインストールしたり設定したりすることなく、AWS サービスに関連するコマンドをすばやく実行できます。このサービスは、AWS CLI、Python、Node.js などの一般的な開発ツールと管理ツールがプリインストールされた、すぐに使える環境を提供します。これにより、AWS サービスの操作やタスクの自動化が容易になります。AWS Cloud Shell は AWS Management Console と直接統合されており、管理タスクの実行、AWS サービスの調査、インフラストラクチャの管理をシームレスに行えます。AWS Cloud Shell は AWS Console へのログインを通じてユーザーを自動的に認証するため、AWS アカウントのコンテキストでコマンドやスクリプトを実行するプロセスが簡単になり、クラウド運用の生産性と効率が向上します。

</details>

## 仕組み {#how-it-works}

StackQL の Linux パッケージには、主要なクラウドベンダーに組み込まれたクラウドシェル環境内で `stackql` を直接使用するためのスクリプトが含まれています。AWS 用のスクリプトは __`stackql-aws-cloud-shell.sh`__ です。__`stackql-aws-cloud-shell.sh`__ スクリプトの使用方法を次に示します：  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

`stackql-aws-cloud-shell.sh` スクリプトは、StackQL 用の最新の AWS プロバイダーを自動的にダウンロードします。同梱のクラウドシェルスクリプトのソースコードは、[__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts) にあります。  

## AWS Cloud Shell から `stackql` Linux パッケージをダウンロードする {#download-the-stackql-linux-package-from-aws-cloud-shell}

まず、AWS Cloud Shell から次のコマンドを使用して、StackQL の最新の Linux バイナリをダウンロードします：  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

これにより、`stackql` バイナリと `stackql-aws-cloud-shell.sh` スクリプトが AWS Cloud Shell 環境のホームディレクトリにダウンロードされ、展開されます。これで準備は完了です！  

## AWS Cloud Shell から `stackql shell` を使用する {#using-the-stackql-shell-from-aws-cloud-shell}

AWS Cloud Shell 内から認証済みの StackQL コマンドシェルを起動するのは、次のように簡単です：  

```shell
sh stackql-aws-cloud-shell.sh
```

次に示すように、StackQL プロバイダーを使用して、SQL ベースの AWS クエリを対話的に実行できるようになります：  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  このブラウザーは video タグをサポートしていません。
</video>

## StackQL を使用して IAM ロールを引き受ける {#assuming-an-iam-role-using-stackql}

AWS で IAM ロールを引き受けると、デフォルトのユーザー権限ではできない可能性のある操作の実行やリソースへのアクセスが可能になります。これは、リソースへの一時的なアクセスが必要な場合や、プライマリアカウントとは異なる権限で操作を実行する必要がある場合に特に便利です。StackQL を使用すると、IAM ロールをシームレスに引き受け、そのロールに付与された権限の下で AWS リソースをクエリおよび管理できます。

<details>
<summary>IAM ロールの引き受けについて</summary>

> IAM ロールの引き受けは、AWS リソースへの一時的なアクセスを付与する安全な方法です。ロールを引き受けると、AWS STS から一時的なセキュリティ認証情報が提供されます。この認証情報を使うと、そのロールに使用権限がある AWS サービスとリソースにアクセスできます。この仕組みは、クロスアカウントアクセス、ユーザーへの一時的なアクセスの付与、最小権限の原則の順守に最適です。

> ロールは、AWS Management Console、AWS CLI または AWS CLI を使用して、また StackQL 内で直接引き受けることができます。これにより、ユーザーとアプリケーションは必要な期間だけ必要な権限を持つことになり、AWS 環境のセキュリティ体制が強化されます。

</details>

AWS Cloud Shell で StackQL を使用して IAM ロールを引き受けるには、`stackql-aws-cloud-shell.sh` スクリプトで `--role-arn` オプションを使用します。このオプションには、引き受けるロールの Amazon Resource Name（ARN）が必要です。指定した場合、StackQL はクエリやコマンドを実行する前に、AWS Security Token Service（STS）を使用して指定されたロールを引き受けます。

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

ロールを引き受けることで、デフォルトのユーザーではアクセスできない可能性のある AWS リソースを StackQL で操作できます。これにより、より柔軟かつ安全にクラウド環境を管理できます。  

引き受けた IAM ロールで StackQL のコマンドやスクリプトを実行する場合は、目的の操作の実行と指定したリソースへのアクセスに必要な権限がそのロールにあることを確認してください。このアプローチにより、きめ細かなアクセス制御が容易になり、セキュリティとガバナンスに関する AWS のベストプラクティスにも沿います。  

## AWS Cloud Shell から `stackql exec`（バッチ操作）を実行する {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

StackQL のクエリは `exec` コマンドを使用して実行することもできます。これは次の用途に役立つオプションです：

- `json` や `csv` などの別の出力形式の指定
- StackQL クエリの結果を格納する出力ファイルの生成
- 入力ファイル（慣例では `.iql` ファイル）で提供される StackQL クエリの使用
- 変数の読み込みやクエリの前処理（`json` または `jsonnet` を使用）を含む複雑な操作の実行

AWS Cloud Shell 内で `exec` を使用する例を次に示します：  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

上の例では、指定した StackQL クエリを実行し、`CSV` の出力ファイルを生成します。このファイルは AWS Cloud Shell からダウンロードできます。さらに、`--role-arn` 引数で IAM ロールを指定すると、クエリやミューテーション操作に別のロールを引き受けることができます。次に例を示します（この例では、クエリを入力ファイルから読み込んでいます）：  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

`exec` コマンドの詳細については、[__こちら__](/command-line-usage/exec)を参照してください
