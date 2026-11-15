---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - go
description: go:embed またはサイドカーで StackQL MCP サーバーを Go アプリケーションに組み込みます
image: "/img/stackql-featured-image.png"
---

# Go

StackQL MCP サーバーをコンパイル済みの Go アプリケーションに組み込みます。ダウンロードしたサイドカーとして使用するか、`go:embed` でバイナリにベンダリングします。クライアントは公式の Go MCP SDK です。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## クイックスタート {#quick-start}

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

API は `StartServer(ctx, Options{Binary, Mode})` です。返されるクライアントは公式の Go MCP SDK のものです。

* * *

## モード {#modes}

サーバーはデフォルトで `read_only` モードで起動します。`safe`、`delete_safe`、`full_access` に昇格するには、`Options.Mode` を設定します。昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

`go:generate` による取得ステップを使って `go:embed` でバイナリをベンダリングし、単一の自己完結型実行可能ファイルを生成します：

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

取得ステップは、対象プラットフォーム用の署名済みバイナリをダウンロードして sha256 で検証し、`go:embed` がビルドに取り込める場所に書き込みます。実行時には `StackqlMCPBinary()` が組み込まれたバイナリを返すため、初回実行時のダウンロードは発生しません。

* * *

## デモアプリ {#demo-app}

**sandboxctl** はインフラストラクチャのコンシェルジュです。組み込みサーバーと Claude のエージェントループを 1 つのバイナリにまとめたもので、承認ゲートの背後で一時的なクラウドサンドボックスの計画、コストチェック、プロビジョニングを行います。

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
