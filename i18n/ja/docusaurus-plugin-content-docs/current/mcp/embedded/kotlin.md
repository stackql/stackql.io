---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - kotlin
  - jvm
  - gradle
description: StackQL MCP サーバーをライブラリまたは Gradle プラグインとして Kotlin/JVM アプリケーションに組み込みます
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

StackQL MCP サーバーを、ライブラリまたは Gradle プラグインとして Kotlin または JVM アプリケーションに組み込みます。クライアントは公式の Kotlin MCP SDK です。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

`io.stackql:stackql-mcp` ライブラリを追加します：

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

JDK 17 と Kotlin 2.x が必要です。クライアントは公式の Kotlin MCP SDK のものです。

* * *

## クイックスタート {#quick-start}

```kotlin
import io.stackql.mcp.LaunchArgs
import io.stackql.mcp.Mode
import io.stackql.mcp.StackqlMcp

suspend fun main() {
    val server = StackqlMcp.builder()
        .mode(Mode.ReadOnly)
        .auth(LaunchArgs.authFor("github", "null_auth"))
        .start()

    server.use {
        val tools = server.client.listTools().tools
        println("${tools.size} tools available")
    }
}
```

API は、`.mode()`、`.auth()`、`.start()` を備えた `StackqlMcp.builder()`、`Mode` 列挙型、`LaunchArgs.authFor()` で構成されます。

* * *

## モード {#modes}

ビルダーのデフォルトは `Mode.ReadOnly` です。昇格するには、`Mode.Safe`、`Mode.DeleteSafe`、`Mode.FullAccess` のいずれかを指定して `.mode()` を呼び出します。昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

ライブラリは署名済みバイナリをダウンロードして検証し、作業ディレクトリに関係なく `~/.stackql/mcp-server-bin/` の共有キャッシュに保存します。Gradle プラグインは同じ起動処理をビルドに組み込むため、CI タスクは個別のインストール手順なしで StackQL クエリを実行できます。

* * *

## デモアプリ {#demo-app}

**costgate** は、コスト予算に基づいてインフラストラクチャのデプロイをゲート制御します。CLI としても Gradle プラグインとしても提供されます。

CLI として使用する場合：

```bash
costgate check --intent examples/costgate.yaml --explain
```

Gradle プラグインとして使用する場合：

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
