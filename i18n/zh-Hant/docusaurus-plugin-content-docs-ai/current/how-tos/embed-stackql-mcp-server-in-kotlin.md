---
title: 如何在 Kotlin 或 JVM 應用程式中內嵌 StackQL MCP 伺服器
description: 以程式庫或 Gradle 外掛程式的形式將 StackQL MCP 伺服器內嵌至 Kotlin/JVM 應用程式，讓 JVM 服務或 CI/CD 管線無需執行階段相依性即可透過 SQL 執行雲端查詢與成本關卡。
keywords: [stackql, mcp, kotlin, jvm, gradle 外掛程式, 內嵌式 mcp 伺服器, model context protocol, kotlin mcp sdk, ci cd, 代理式基礎架構, costgate]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 Kotlin 應用程式？
    answer: 將程式庫 `io.stackql:stackql-mcp:0.1.0` 加入您的 Gradle 相依性，然後呼叫 `StackqlMcp.builder().mode(Mode.ReadOnly).start()`。用戶端是官方的 Kotlin MCP SDK。首次執行時，已簽署的二進位檔會下載到位於 ~/.stackql/mcp-server-bin/ 的共用快取並進行 sha256 驗證。需要 JDK 17 與 Kotlin 2.x。
  - question: 能否從 Gradle 建置或 CI 管線執行 StackQL 查詢？
    answer: 可以。Gradle 外掛程式（id "io.stackql.costgate"）會將內嵌伺服器接入您的建置，讓 CI 工作無需額外的安裝步驟即可執行 StackQL 查詢。costgate 示範同時以 CLI 和 Gradle 外掛程式的形式，依成本預算管控基礎架構部署。
  - question: 如何在 Kotlin 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: 在建立器上呼叫 .mode(Mode.ReadOnly)，這是預設值。ReadOnly 會拒絕 INSERT、UPDATE、DELETE 與 EXEC。只有在建置或服務需要佈建時，才提升至 Mode.Safe、Mode.DeleteSafe 或 Mode.FullAccess；權限提升一律需明確指定。
  - question: stackql-mcp-kotlin 使用哪個 MCP 用戶端？
    answer: 官方的 Kotlin MCP SDK。啟動後的伺服器會公開 server.client，因此您可以直接呼叫 server.client.listTools().tools 及其他 SDK 方法。
---

# 如何在 Kotlin 或 JVM 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在 Kotlin 或 JVM 應用程式內執行，不需要 `npx`、無需另外安裝，也沒有執行階段相依性。`io.stackql:stackql-mcp` 程式庫會在官方 Kotlin MCP SDK 用戶端背後，透過 stdio 啟動已簽署的 StackQL 二進位檔，而配套的 Gradle 外掛程式則將同樣的啟動流程接入建置。這正是 **costgate** 背後的模式，它是以 CLI 和 Gradle 外掛程式形式發行的 CI/CD 成本關卡。

## 步驟 {#steps}

1. **新增程式庫**至您的 Gradle 相依性：

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

需要 JDK 17 與 Kotlin 2.x。用戶端來自官方的 Kotlin MCP SDK。

2. **啟動伺服器並列出工具**。建立器預設為 `Mode.ReadOnly`：

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

3. **選擇模式**。`.mode()` 預設為 `Mode.ReadOnly`。只有在應用程式需要佈建時，才設定 `Mode.Safe`、`Mode.DeleteSafe` 或 `Mode.FullAccess`。權限提升需明確指定。

## 從 Gradle 建置執行 StackQL 查詢 {#run-stackql-queries-from-a-gradle-build}

Gradle 外掛程式會將內嵌伺服器接入您的建置，讓 CI 工作無需額外的安裝步驟即可執行 StackQL 查詢。**costgate** 示範會依成本預算管控基礎架構部署，可作為 CLI 使用：

```bash
costgate check --intent examples/costgate.yaml --explain
```

也可作為 Gradle 外掛程式使用：

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

## 相關概念 {#related-concepts}

- [內嵌式 MCP：Kotlin / JVM 參考](/mcp/embedded/kotlin)：安裝、API 與 Gradle 外掛程式
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
