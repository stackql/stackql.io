---
title: 如何在 Kotlin 或 JVM 应用中嵌入 StackQL MCP 服务器
description: 以库或 Gradle 插件的形式将 StackQL MCP 服务器嵌入 Kotlin/JVM 应用，使 JVM 服务或 CI/CD 流水线无需运行时依赖即可通过 SQL 执行云查询和成本门控。
keywords: [stackql, mcp, kotlin, jvm, gradle 插件, 嵌入式 MCP 服务器, model context protocol, kotlin mcp sdk, ci cd, 智能体基础设施, costgate]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 Kotlin 应用添加嵌入式 StackQL MCP 服务器？
    answer: 将库 `io.stackql:stackql-mcp:0.1.0` 添加到你的 Gradle 依赖中，然后调用 `StackqlMcp.builder().mode(Mode.ReadOnly).start()`。客户端是官方的 Kotlin MCP SDK。已签名的二进制文件在首次运行时下载，经 sha256 校验后存入位于 ~/.stackql/mcp-server-bin/ 的共享缓存。需要 JDK 17 和 Kotlin 2.x。
  - question: 能否在 Gradle 构建或 CI 流水线中运行 StackQL 查询？
    answer: 可以。Gradle 插件（id "io.stackql.costgate"）将嵌入式服务器接入你的构建，因此 CI 任务无需单独的安装步骤即可运行 StackQL 查询。costgate 演示应用根据成本预算对基础设施部署进行门控，同时提供 CLI 和 Gradle 插件两种形式。
  - question: 如何在 Kotlin 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: 在构建器上调用 .mode(Mode.ReadOnly)，这是默认值。ReadOnly 拒绝 INSERT、UPDATE、DELETE 和 EXEC。仅当构建或服务需要预配资源时，才提升到 Mode.Safe、Mode.DeleteSafe 或 Mode.FullAccess，权限提升始终需要显式进行。
  - question: stackql-mcp-kotlin 使用哪个 MCP 客户端？
    answer: 官方的 Kotlin MCP SDK。启动后的服务器提供 server.client，因此可以直接调用 server.client.listTools().tools 和其他 SDK 方法。
---

# 如何在 Kotlin 或 JVM 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在 Kotlin 或 JVM 应用内运行，无需 `npx`，无需单独安装，也没有运行时依赖。`io.stackql:stackql-mcp` 库在官方 Kotlin MCP SDK 客户端的背后通过 stdio 启动已签名的 StackQL 二进制文件，配套的 Gradle 插件将相同的启动方式接入构建。这就是 **costgate** 所采用的模式，它是面向 CI/CD 的成本门控，以 CLI 和 Gradle 插件两种形式发布。

## 步骤 {#steps}

1. **添加库**到你的 Gradle 依赖中：

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

需要 JDK 17 和 Kotlin 2.x。客户端来自官方的 Kotlin MCP SDK。

2. **启动服务器并列出工具。** 构建器默认使用 `Mode.ReadOnly`：

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

3. **选择模式。** `.mode()` 默认为 `Mode.ReadOnly`。仅当应用需要预配资源时，才设置 `Mode.Safe`、`Mode.DeleteSafe` 或 `Mode.FullAccess`。权限提升必须显式进行。

## 在 Gradle 构建中运行 StackQL 查询 {#run-stackql-queries-from-a-gradle-build}

Gradle 插件将嵌入式服务器接入你的构建，因此 CI 任务无需单独的安装步骤即可运行 StackQL 查询。**costgate** 演示应用根据成本预算对基础设施部署进行门控，可作为 CLI 使用：

```bash
costgate check --intent examples/costgate.yaml --explain
```

也可作为 Gradle 插件使用：

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

## 相关概念 {#related-concepts}

- [嵌入式 MCP：Kotlin / JVM 参考](/mcp/embedded/kotlin)：安装、API 和 Gradle 插件
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
