---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - swift
  - macos
description: 公証済みの StackQL MCP サーバーバイナリを署名済み macOS アプリにバンドルします
image: "/img/stackql-featured-image.png"
---

# Swift

StackQL MCP サーバーを macOS アプリケーションに組み込みます。署名済みかつ公証済みのバイナリを `.app` 内にバンドルでき、クライアントは公式の Swift MCP SDK です。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

`Package.swift` にパッケージを追加します：

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

macOS 13 以降と Swift 6.1（Xcode 16.3 以降）が必要です。依存関係は公式の Swift MCP SDK のみです。

* * *

## クイックスタート {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

API は `StackQLServer.start(Options)`、`.listToolNames()`、`.call(_:_:)`、`.stop()` です。`Options` には `mode`、`auth`、`binaryOverride`、`allowDownload` があります。外部ハーネス向けに、`StackQLServer.resolveCommand(_:)` は解決済みの起動コマンドを返します。

* * *

## モード {#modes}

`Options.mode` のデフォルトは `.readOnly` です。昇格するには、`.safe`、`.deleteSafe`、`.fullAccess` のいずれかに設定します。昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

`darwin-universal` バイナリは Developer ID で署名され、Apple の公証を受けています。そのため、アプリ自体の公証を有効に保ったまま、署名済みの macOS `.app` 内にバンドルできます。これは Swift ライブラリ独自の機能です。初回実行時のダウンロードが不要な、自己完結型で公証済みのアプリケーションを 1 つ配布できます。

バイナリの解決順序：

1. 明示的に指定した `binaryOverride`。
2. バンドルされた `.app` リソース。
3. `~/.stackql/mcp-server-bin/` の共有キャッシュ。
4. sha256 による検証付きのダウンロード（`allowDownload` が設定されている場合）。

* * *

## デモアプリ {#demo-app}

**CloudLens** は、公証済みバイナリを組み込み、`read_only` のチェックスイートを実行するメニューバー常駐のクラウドセンチネルです。

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
