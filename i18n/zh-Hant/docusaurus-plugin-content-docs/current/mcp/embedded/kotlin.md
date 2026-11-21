---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - kotlin
  - jvm
  - gradle
description: 以程式庫或 Gradle 外掛程式的形式，在 Kotlin/JVM 應用程式中內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

以程式庫或 Gradle 外掛程式的形式，在 Kotlin 或 JVM 應用程式中內嵌 StackQL MCP 伺服器。用戶端是官方的 Kotlin MCP SDK。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

加入 `io.stackql:stackql-mcp` 程式庫：

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

需要 JDK 17 與 Kotlin 2.x。用戶端來自官方的 Kotlin MCP SDK。

* * *

## 快速入門 {#quick-start}

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

API 包括 `StackqlMcp.builder()` 及其 `.mode()`、`.auth()` 與 `.start()`、`Mode` 列舉，以及 `LaunchArgs.authFor()`。

* * *

## 模式 {#modes}

建立器預設為 `Mode.ReadOnly`。若要提升權限，請以 `Mode.Safe`、`Mode.DeleteSafe` 或 `Mode.FullAccess` 呼叫 `.mode()`。權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

程式庫會下載並驗證已簽署的二進位檔，再將其快取在 `~/.stackql/mcp-server-bin/` 的共用快取中，與工作目錄無關。Gradle 外掛程式會將相同的啟動方式接入您的建置，因此 CI 工作不需要另外的安裝步驟即可執行 StackQL 查詢。

* * *

## 示範應用程式 {#demo-app}

**costgate** 依據成本預算管控基礎架構部署。它同時以 CLI 與 Gradle 外掛程式的形式提供。

以 CLI 形式：

```bash
costgate check --intent examples/costgate.yaml --explain
```

以 Gradle 外掛程式形式：

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

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
