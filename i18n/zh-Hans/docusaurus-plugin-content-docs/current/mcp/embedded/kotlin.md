---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - kotlin
  - jvm
  - gradle
description: 以库或 Gradle 插件的形式将 StackQL MCP 服务器嵌入 Kotlin/JVM 应用
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

将 StackQL MCP 服务器嵌入 Kotlin 或 JVM 应用，可作为库或 Gradle 插件使用。客户端是官方的 Kotlin MCP SDK。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

添加 `io.stackql:stackql-mcp` 库：

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

需要 JDK 17 和 Kotlin 2.x。客户端来自官方的 Kotlin MCP SDK。

* * *

## 快速入门 {#quick-start}

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

API 为 `StackqlMcp.builder()`，搭配 `.mode()`、`.auth()` 和 `.start()`，以及 `Mode` 枚举和 `LaunchArgs.authFor()`。

* * *

## 模式 {#modes}

构建器默认使用 `Mode.ReadOnly`。调用 `.mode()` 并传入 `Mode.Safe`、`Mode.DeleteSafe` 或 `Mode.FullAccess` 即可提升权限。权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

该库会下载、校验已签名的二进制文件，并将其缓存到位于 `~/.stackql/mcp-server-bin/` 的共享缓存中，与工作目录无关。Gradle 插件将相同的启动方式接入你的构建，因此 CI 任务无需单独的安装步骤即可运行 StackQL 查询。

* * *

## 演示应用 {#demo-app}

**costgate** 根据成本预算对基础设施部署进行门控。它同时以 CLI 和 Gradle 插件两种形式发布。

作为 CLI：

```bash
costgate check --intent examples/costgate.yaml --explain
```

作为 Gradle 插件：

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

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
