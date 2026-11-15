---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - gleam
  - erlang
  - beam
description: StackQL MCP サーバーを Erlang/BEAM ターゲットの Gleam アプリケーションに組み込みます
image: "/img/stackql-featured-image.png"
---

# Gleam

StackQL MCP サーバーを Erlang/BEAM ターゲットの Gleam アプリケーションに組み込みます。ライブラリは OTP のスーパービジョンと統合され、署名済みバイナリをダウンロードして検証し、stdio 経由で起動します。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

```
gleam add stackql_mcp
```

対象は Erlang/BEAM のみです（JavaScript ターゲットは対象外）。サーバーは `child_spec()` を通じて OTP の子プロセスとして公開されるため、独自のスーパービジョンツリーに組み込むことができます。

* * *

## クイックスタート {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API は次のとおりです。ライフサイクルには `stackql_mcp.start()`、`stop()`、`child_spec()` を使用します。操作には `list_tools()` と `call_tool()` を使用します（スーパーバイザー配下で使用する `supervised_list_tools()` と `supervised_call_tool()` のバリアントもあります）。設定には `Config`、`default_config()`、`auth_for()` を使用します。

* * *

## モード {#modes}

モードは `Config.mode` で設定し、デフォルトは `read_only` です。昇格するには、`safe`、`delete_safe`、`full_access` のいずれかに設定します：

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

起動時に、ライブラリはプラットフォーム用の StackQL バンドルをダウンロードし、組み込み済みの固定 sha256 値と照合して検証し、ファミリー共通の共有キャッシュに保存します。解決の優先順位は次のとおりです：

1. 明示的に指定した `Config.binary`。
2. `STACKQL_MCP_BIN` 環境変数。
3. `~/.stackql/mcp-server-bin/<version>/<platform-key>/` の共有キャッシュ。

オフラインまたはエアギャップ環境へのデプロイでは、`STACKQL_MCP_BUNDLE` にローカルのバンドルを指定します。

* * *

## デモアプリ {#demo-app}

**pipewatch** は、このライブラリ用に予定されているデモです。ロードマップに含まれていますが、まだ実装されていません。

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
