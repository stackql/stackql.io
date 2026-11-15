---
title: Claude Desktop で StackQL を使用する
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - マルチクラウド
description: Claude Desktop で StackQL を使用し、自然言語でクラウドリソースのクエリとプロビジョニングを行う方法
image: "/img/stackql-featured-image.png"
---

StackQL は Model Context Protocol（MCP）を介して Claude Desktop と統合され、自然言語を使用して複数のクラウドベンダーにまたがるクラウドリソースをクエリ、プロビジョニングできます。この強力な組み合わせにより、内部では StackQL の SQL ベースのインターフェイスを活用しながら、クラウドインフラストラクチャと対話形式でやり取りできます。

インストール方法は 3 つあります。推奨順に、Anthropic Connector Directory からインストールする方法、ダウンロードしたバンドルからインストールする方法、ローカルにインストールした StackQL バイナリを手動で登録する方法です。

## Connector Directory からのインストール（推奨） {#install-from-the-connector-directory-recommended}

Claude Desktop で StackQL MCP サーバーを使用する最も簡単な方法は、Anthropic Connector Directory からインストールすることです。Claude Desktop で **Settings -> Connectors -> Add -> Browse Connectors** を開き、**StackQL** を検索して **Install** をクリックします。ダウンロードするものはなく、StackQL を別途インストールする必要もありません。

![Anthropic Connector Directory の StackQL](/img/stackql-anthropic-directory-listing.png)

掲載ページは [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) で直接確認することもできます。

これにより、StackQL MCP サーバーがプラットフォームに合わせてインプロセスでインストールされます。掲載ページでは、macOS universal、Windows x64、Linux x64 および arm64 向けの署名済みバイナリを含む、統合されたマルチプラットフォームバンドルが配信されます。Claude Desktop は、マシンに合ったバイナリを自動的に選択します。ネイティブの Windows ARM64 の場合は、[下記のダウンロード](#install-from-a-downloaded-bundle)から個別の `windows-arm64` バンドルをインストールしてください。Windows バイナリは Authenticode で署名済み、macOS バイナリは Apple による公証済みです。これらは、スタンドアロンの StackQL リリースと同じバイナリです。

![StackQL コネクターのインストール](/img/stackql-anthropic-directory-install.png)

Connector Directory からインストールしたコネクターは、Claude Desktop の環境を継承します。そのため、Claude Desktop を起動する前に、オペレーティングシステムのレベルでプロバイダーの環境変数を設定してください（[プロバイダー固有の認証情報](#provider-specific-credentials)を参照）。Claude Desktop は起動時に環境を取得します。変数を変更した後は、Claude Desktop を完全に終了し（Windows ではトレイアイコンを含む）、再起動する必要があります。エージェントに [`reload_credentials`](/mcp/reload_credentials) の実行を依頼すると、プロバイダーごとの認証情報の状態が報告されます。これは、サーバーから何が見えて何が見えないかを確認する最も速い方法です。

Claude Desktop は MCP のエリシテーション（elicitation）機能を公開しています。そのため、デフォルトのサーバーモード（`safe`）では、ミューテーションのたびに承認リクエストが表示されます（[サーバーモード](/command-line-usage/mcp#server-modes)を参照）。

## ダウンロードしたバンドルからのインストール {#install-from-a-downloaded-bundle}

特定のバージョンに固定する場合、Connector Directory にアクセスせずにインストールする場合、またはインストール前にチェックサムを自分で検証する場合は、このインストール方法を使用します。

ビルド済みの MCP Bundle（`.mcpb`）は、署名済みの `stackql` バイナリとサーバー設定を含む、ワンクリックでインストールできるパッケージです。StackQL を別途インストールする必要はありません。

最新のリリースから、プラットフォームに対応するバンドルをダウンロードします。

| プラットフォーム | バンドル |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64（ネイティブ） | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

次に、Claude Desktop で **Settings -> Extensions -> Install Extension** を開き、ダウンロードしたファイルを選択します。組み込まれているバイナリは、Authenticode で署名済み（Windows）、Apple による公証済み（macOS）です。各バンドルには、[リリースページ](https://github.com/stackql/stackql/releases/latest)に `.sha256` チェックサムが用意されています。インストール前に検証するには、次のようにします。

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

サーバーは [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) にも `io.github.stackql/stackql-mcp` として登録されており、これらのチェックサムが固定されています。

## 手動設定 {#manual-configuration}

StackQL がすでにインストールされている場合は、Claude Desktop の設定ファイルに StackQL MCP サーバーを直接登録できます。

### 前提条件 {#prerequisites}

Claude Desktop で StackQL をセットアップする前に、次のものが揃っていることを確認してください。

1. **インストール済みの StackQL**：[stackql.io](https://stackql.io/downloads) から StackQL をダウンロードしてインストールするか、パッケージマネージャーを使用する
2. **Claude Desktop**：[Anthropic の Web サイト](https://claude.ai/desktop)からダウンロードする
3. **クラウドベンダーの認証情報**：使用するクラウドベンダーの API キーまたは認証情報

次のコマンドを実行して、StackQL がシステムの PATH に含まれていることを確認します。

```bash
stackql --version
```

### MCP サーバーのセットアップ {#setting-up-the-mcp-server}

Claude Desktop が StackQL と通信できるようにするには、Claude Desktop の設定ファイルで MCP サーバーを構成する必要があります。

#### Windows {#windows}

次の場所にある設定ファイルを編集します。
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

次の場所にある設定ファイルを編集します。
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

次の場所にある設定ファイルを編集します。
```
~/.config/Claude/claude_desktop_config.json
```

### 設定例 {#configuration-example}

`claude_desktop_config.json` ファイルに次の設定を追加します。

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
含める必要があるのは、使用する予定のクラウドベンダーの環境変数だけです。使用しないプロバイダーの認証情報は、設定から削除してください。
:::

:::warning
実際の認証情報を含む `claude_desktop_config.json` ファイルは、絶対にバージョン管理システムにコミットしないでください。安全な認証情報管理の手法を使用してください。
:::

Claude Desktop は MCP のエリシテーション機能を公開しています。そのため、デフォルトのサーバーモード（`safe`）では、ミューテーションのたびに承認リクエストが表示されます。インベントリの参照だけに限定するには `mode: read_only` に、承認リクエストを省略するには `mode: full_access` に切り替えます（[サーバーモード](/command-line-usage/mcp#server-modes)を参照）。

### 認証情報ファイルの使用（`--env.file`） {#using-a-credentials-file---envfile}

`env` ブロックの代わりに、認証情報を dotenv 形式のファイルに保存し、[`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) フラグで渡すこともできます（Windows ではこちらを推奨します）。

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

最小限の認証情報ファイルは次のとおりです。

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

`env` ブロックより優れている理由は次のとおりです。

- `env` ブロックの変更を MCP サブプロセスに反映するには、Claude Desktop を完全に再起動する必要があります（トレイアイコンの終了を含む）。また、Windows の `setx` はレジストリに書き込むだけで、実行中の Desktop プロセスには反映されません。
- `--env.file` のファイルはいつでも作成やローテーションができます（サーバーの起動時に存在している必要すらありません）。エージェントに [`reload_credentials`](/mcp/reload_credentials) の実行を依頼すれば、再起動せずに変更が反映されます。

:::note

`--env.file` と `reload_credentials` は、StackQL `v0.10.542` より後のリリースで利用できます。

:::

### プロバイダー固有の認証情報 {#provider-specific-credentials}

クラウドベンダーによって、必要な認証方式は異なります。目的のプロバイダーに必要な環境変数については、プロバイダーのドキュメント（[https://stackql.io/providers](/providers)）を参照してください。


### Claude Desktop の再起動 {#restarting-claude-desktop}

設定ファイルを更新した後、Claude Desktop を再起動して新しい MCP サーバーの設定を読み込みます。StackQL MCP サーバーは、新しい会話を開始すると自動的に起動します。

## できること {#what-you-can-do}

設定が完了すると、Claude Desktop で自然言語を使用して次のことができます。

### クラウドリソースのクエリ {#query-cloud-resources}

クラウドインフラストラクチャに関する情報の取得を Claude に依頼します。

- 「us-central1 リージョンにあるすべての GCP コンピュートインスタンスを表示して」
- 「AWS アカウント内のすべての S3 バケットを一覧表示して」
- 「サブスクリプションで実行中の Azure 仮想マシンはどれ？」
- 「default 名前空間にあるすべての Kubernetes Pod を表示して」

### リソースのプロビジョニング {#provision-resources}

会話形式の指示でクラウドリソースを作成、デプロイします。

- 「us-west-2 に『my-data-bucket』という名前の S3 バケットを作成して」
- 「バージョニングを有効にした GCP Cloud Storage バケットをデプロイして」
- 「us-east-1 で小さな EC2 インスタンスを起動して」
- 「westus2 に Azure リソースグループを作成して」

### 分析と監査 {#analyze-and-audit}

セキュリティ監査とコンプライアンスチェックを実行します。

- 「パブリックにアクセス可能なすべての S3 バケットを表示して」
- 「90 日間ログインしていない IAM ユーザーを一覧表示して」
- 「適切なラベルがないすべての GCP インスタンスを見つけて」
- 「バックアップが有効になっていない Azure VM を特定して」

### クロスクラウド操作 {#cross-cloud-operations}

複数のクラウドベンダーにまたがるリソースをクエリ、比較します。

- 「AWS、GCP、Azure のすべてのコンピュートインスタンスを表示して」
- 「AWS S3 と GCP Cloud Storage のストレージコストを比較して」
- 「すべてのクラウドベンダーのすべてのデータベースを一覧表示して」

### Infrastructure as Code（IaC） {#infrastructure-as-code}

インフラストラクチャをデプロイするクエリを生成、実行します。

- 「VPC、サブネット、EC2 インスタンスを含むリソースのスタックを作成して」
- 「ノードプールを含む完全な GKE クラスターをデプロイして」
- 「AWS でマルチリージョンのデプロイをセットアップして」

### リソースの変更 {#modify-resources}

既存のクラウドリソースを更新します。

- 「タグのないすべての EC2 インスタンスにタグを追加して」
- 「S3 バケット『my-bucket』の暗号化を有効にして」
- 「GCP インスタンス『web-server-1』のマシンタイプを更新して」
- 「Cloud Storage バケットの ACL をプライベートに変更して」

### リソースの削除 {#delete-resources}

不要になったクラウドリソースを削除します。

- 「S3 バケット『temp-bucket』を削除して」
- 「停止しているすべての EC2 インスタンスを削除して」
- 「使用されていない GCP ディスクをクリーンアップして」

## 会話の例 {#example-conversation}

StackQL を使用して Claude Desktop とやり取りする例を次に示します。

**ユーザー**：「すべての GCP コンピュートインスタンスを表示してくれる？」

**Claude**：*StackQL クエリを実行し、インスタンス名、ゾーン、マシンタイプ、ステータスを示す整形された表で結果を返します*

**ユーザー**：「均一なバケットレベルのアクセスを有効にして、us-central1 リージョンに『analytics-data-2025』という名前の新しい Cloud Storage バケットを作成して」

**Claude**：*StackQL の INSERT 文を実行してバケットを作成し、作成されたことを確認します*

**ユーザー**：「次に、すべての Cloud Storage バケットを一覧表示して」

**Claude**：*SELECT クエリを実行し、新しく作成されたバケットを含むすべてのバケットを表示します*

## ヒントとベストプラクティス {#tips-and-best-practices}

1. **具体的に指示する**：リクエストが具体的であるほど、よい結果が得られます。可能な場合は、リソース名、リージョン、具体的な属性を含めてください。

2. **自然言語を使用する**：SQL の構文を知る必要はありません。Claude が自然言語のリクエストを StackQL クエリに変換します。

3. **実行前に確認する**：破壊的な操作（DELETE、UPDATE）の場合、Claude は通常、処理を進める前に実行内容を表示します。

4. **複数ステップの操作**：1 つの会話の中で操作を連続して行えます。Claude はセッション全体を通じてコンテキストを保持します。

5. **エラー処理**：クエリが失敗した場合、Claude はトラブルシューティングを支援し、エラーメッセージに基づいて修正を提案できます。

6. **機能を探る**：Claude に「StackQL で何ができる？」と尋ねると、設定済みのプロバイダーに応じた提案が得られます。

## トラブルシューティング {#troubleshooting}

### MCP サーバーが起動しない {#mcp-server-not-starting}

Claude Desktop が StackQL MCP サーバーに接続できない場合は、次の手順を実行します。

1. StackQL が PATH に含まれていることを確認する
2. `claude_desktop_config.json` ファイルが有効な JSON であることを確認する
3. 設定内のファイルパスが、使用しているオペレーティングシステムに対して正しいことを確認する
4. Claude Desktop のログでエラーメッセージを確認する

### 認証エラー {#authentication-errors}

認証エラーが発生した場合は、次の手順を実行します。

1. クラウドベンダーの認証情報が正しいことを確認する
2. 指定したパスに認証情報ファイルが存在することを確認する
3. 設定内の環境変数が正しい形式であることを確認する（エージェントに [`reload_credentials`](/mcp/reload_credentials) の実行を依頼すると、プロバイダーごとの認証情報の状態が報告されるため、不足している変数を最も速く確認できる）
4. 認証情報に必要な権限があることを確認する

### 権限エラー {#permission-errors}

権限不足により操作が失敗する場合は、次の手順を実行します。

1. サービスアカウントまたは IAM ユーザーに必要な権限があることを確認する
2. プロバイダー固有の IAM ポリシーを確認する
3. クラウドプロジェクトで API サービスが有効になっていることを確認する

## 詳細情報 {#learn-more}

- [StackQL MCP コマンドリファレンス](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [言語仕様](/language-spec/select)
- [Model Context Protocol のドキュメント](https://modelcontextprotocol.io)

## セキュリティに関する考慮事項 {#security-considerations}

:::caution
- 認証情報は安全に保管し、バージョン管理システムには絶対にコミットしない
- サービスアカウントには最小権限の原則を適用する
- 認証情報と API キーを定期的にローテーションする
- MCP サーバーのログを監視して、異常なアクティビティがないか確認する
- クラウドベンダーの認証情報管理ツール（Secret Manager、Parameter Store など）の使用を検討する
:::
