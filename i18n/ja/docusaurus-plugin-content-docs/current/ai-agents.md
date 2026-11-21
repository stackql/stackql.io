---
slug: /ai-agents
title: AI エージェント
hide_title: false
keywords:
  - stackql
  - AI エージェント
  - mcp
  - model context protocol
  - エージェント型
description: MCP サーバー、ツール、組み込みインテグレーション、SQL クエリを通じて、StackQL で AI エージェントをクラウドおよび SaaS サービスに接続します。
image: "/img/stackql-featured-image.png"
---

StackQL は、人がクラウドおよび SaaS サービスのクエリ、プロビジョニング、運用に使うのと同じ SQL インターフェイスを AI エージェントに提供します。エージェントは、プロバイダーが公開する内容を検出し、クエリを実行前に検証し、`SELECT` でライブ状態を読み取り、`INSERT`、`UPDATE`、`DELETE` で状態を変更します。ステートファイルもクラウドごとの SDK も必要ありません。

## エージェントの接続方法 {#how-agents-connect}

- **stdio 経由の MCP** は、Claude Desktop、Cursor、Continue などのエディターおよびデスクトップクライアント向けです。クライアントが `stackql mcp --mcp.server.type=stdio` を自身で起動します。[MCP サーバー](/command-line-usage/mcp)を参照してください。
- **HTTP 経由の MCP** は、長時間実行される `stackql mcp` または `stackql srv` プロセスに接続するスタンドアロンのエージェントと自動化向けです。Bearer トークン認証と、オプションの TLS を備えています。
- **組み込み MCP** は、コンパイル済みアプリケーション向けです。サーバーは Go、Rust、Swift、Kotlin、.NET、Gleam のプロセス内で実行されます。[組み込み MCP](/mcp/embedded) を参照してください。
- **PostgreSQL ワイヤプロトコル** は、[`stackql srv`](/command-line-usage/srv) を介して利用でき、すでに Postgres に対応しているエージェントフレームワーク、ノートブック、BI ツール向けです。

## Claude Desktop でのクイックスタート {#quick-start-with-claude-desktop}

Anthropic Connector Directory（推奨）またはダウンロード可能な MCP Bundle からインストールするか、`claude_desktop_config.json` にサーバーを手動で登録します：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

使用する予定のプロバイダーの認証情報だけを含めてください。バンドルのダウンロードや認証情報ファイルの使用を含む詳しい手順は、[Claude Desktop で StackQL を使用する](/getting-started/claude-desktop)にあります。

## エージェントにできること {#what-an-agent-can-do}

MCP サーバーは、型付けされた小さなツールセットを公開します。各ツールは、モデル向けのレンダリング済みビューと、プログラムから利用するクライアント向けの構造化ペイロードを返します。

- **検出**：`list_providers`、`list_services`、`list_resources`、`list_methods`、`describe_method` はプロバイダーの階層をたどり、各操作の入力と出力フィールドを返します。そのため、必須の `WHERE` パラメーターがクエリを書く前にわかります。
- **検証と読み取り**：`validate_select_query` は `SELECT` を実行せずに解析し、計画を作成します。`run_select_query` はそれを実行します。
- **変更**：`run_mutation_query` は `INSERT`、`UPDATE`、`REPLACE`、`DELETE` を実行します。`run_lifecycle_operation` は `EXEC` ライフサイクル操作を実行します。どちらも後述のサーバーモードによってゲート制御されます。
- **ハウスキーピング**：`server_info`、`list_registry`、`pull_provider`、`reload_credentials`。

ツールごとの入力、ゲート制御の動作、プロンプト例を含む完全なリファレンスは、[MCP ツール](/mcp)にあります。

## ガードレール {#guardrails}

サーバーモードは、セッション全体の安全コントラクトを設定します：

| モード | `SELECT` / メタデータ | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | 許可 | 拒否 | 拒否 |
| `safe`（デフォルト） | 許可 | 承認が必要 | 承認が必要 |
| `delete_safe` | 許可 | 許可 | 承認が必要 |
| `full_access` | 許可 | 許可 | 許可 |

「承認が必要」では、MCP のエリシテーション（elicitation）を使用します。これをサポートするクライアント（Claude Desktop、Cursor、Continue）は、ツール名、クエリの分類、SQL をユーザーに表示し、何かが実行される前に判断を待ちます。これを公開していないクライアントは拒否されます。すべてのツール呼び出しは監査ログに書き込まれ、HTTP リスナーには、`allow_unauthenticated` で明示的に開放されていない限り、Bearer トークンが必要です。認証情報は環境または dotenv ファイルから取得され、シークレットの値がクライアントに返されることはありません。詳細は[サーバーモード](/command-line-usage/mcp#server-modes)、[HTTP クライアント認証](/command-line-usage/mcp#http-client-authentication)、[監査ログ](/command-line-usage/mcp#audit-log)にあります。

## 次のステップ {#next-steps}

- [MCP サーバー](/command-line-usage/mcp)：デプロイモード、設定、サーバーモード、監査ログ、フラグ。
- [MCP ツール](/mcp)：入力、ゲート制御、プロンプト例を含む完全なツールリファレンス。
- [組み込み MCP](/mcp/embedded)：独自の Go、Rust、Swift、Kotlin、.NET または Gleam アプリケーション内でサーバーを実行する。
- [Query Library](pathname:///docs/query-library/)：クラウド運用でよくある質問に答える、すぐに実行できるプロバイダー別の SQL。
- [AI リファレンス](/ai)：エージェントと、エージェントを構築する人のために書かれた概念、ハウツー、比較、トラブルシューティング。
