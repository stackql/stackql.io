---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - dotnet
  - csharp
description: StackQL MCP サーバーをサイドカーまたはベンダリングされたバンドルとして .NET アプリケーションに組み込みます
image: "/img/stackql-featured-image.png"
---

# .NET / C#

StackQL MCP サーバーを .NET アプリケーションに組み込みます。ダウンロードしたサイドカーとして使用するか、ビルドにベンダリングして自己完結型の実行可能ファイルにします。クライアントは公式の C# MCP SDK です。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

```
dotnet add package StackQL.Mcp
```

オプションの `StackQL.Mcp.AgentFramework` パッケージは、Agent Framework との統合を追加します。.NET 8 以降が必要です。

* * *

## クイックスタート {#quick-start}

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

API は、`.WithMode()`、`.WithAuth()`、`.WithBinary()`、`.WithBundlePath()`、`.WithApproot()`、`.WithCommand()` を備えた `StackqlMcp.CreateBuilder()` と、その後に呼び出す `.StartAsync()`、`.ListToolsAsync()`、`.CallToolAsync()` で構成されます。

* * *

## モード {#modes}

ビルダーのデフォルトは `StackqlMode.ReadOnly` です。昇格するには、`StackqlMode.Safe`、`StackqlMode.DeleteSafe`、`StackqlMode.FullAccess` のいずれかを指定して `.WithMode()` を呼び出します。昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

バイナリの入手方法は 2 つあります：

- **サイドカー（デフォルト）**：プラットフォームバンドルを初回実行時にダウンロードし、sha256 で検証して `~/.stackql/mcp-server-bin/<version>/<platform-key>/` にキャッシュし、stdio 経由で起動します。
- **ベンダリング**：バンドルをビルドリソースとして組み込み、自己完結型の実行可能ファイルを生成します。

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

実行時に `STACKQL_MCP_BIN` または `STACKQL_MCP_BUNDLE` 環境変数でバイナリやバンドルを上書きすることもできます。

* * *

## デモアプリ {#demo-app}

**driftwatch** は、読み取り専用の StackQL サーバーを組み込んだ .NET Worker Service です。スケジュールに従って一連の SQL ドリフトチェック（パブリックへの公開、タグやライセンスの欠落、ベースラインからのドリフト）を実行し、結果を Teams の Adaptive Card に投稿します。

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
