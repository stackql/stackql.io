---
title: How to embed the StackQL MCP server in a Kotlin or JVM application
description: Embed the StackQL MCP server in a Kotlin/JVM app as a library or Gradle plugin, so a JVM service or CI/CD pipeline can run cloud queries and cost gates over SQL with no runtime dependency.
keywords: [stackql, mcp, kotlin, jvm, gradle plugin, embedded mcp server, model context protocol, kotlin mcp sdk, ci cd, agentic infrastructure, costgate]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a Kotlin app?
    answer: Add the library `io.stackql:stackql-mcp:0.1.0` to your Gradle dependencies, then call `StackqlMcp.builder().mode(Mode.ReadOnly).start()`. The client is the official Kotlin MCP SDK. The signed binary is downloaded and sha256-verified into the shared cache at ~/.stackql/mcp-server-bin/ on first run. Requires JDK 17 and Kotlin 2.x.
  - question: Can I run StackQL queries from a Gradle build or CI pipeline?
    answer: Yes. The Gradle plugin (id "io.stackql.costgate") wires the embedded server into your build, so CI tasks run StackQL queries without a separate install step. The costgate demo gates infrastructure deploys on cost budgets as both a CLI and a Gradle plugin.
  - question: How do I keep an embedded StackQL MCP server read-only in Kotlin?
    answer: Call .mode(Mode.ReadOnly) on the builder, which is the default. ReadOnly refuses INSERT, UPDATE, DELETE, and EXEC. Escalate to Mode.Safe, Mode.DeleteSafe, or Mode.FullAccess only when the build or service needs to provision - escalation is always explicit.
  - question: Which MCP client does stackql-mcp-kotlin use?
    answer: The official Kotlin MCP SDK. The started server exposes server.client, so you call server.client.listTools().tools and other SDK methods directly.
---

# How to embed the StackQL MCP server in a Kotlin or JVM application

StackQL's MCP server can run inside a Kotlin or JVM application with no `npx`, no separate install, and no runtime dependency. The `io.stackql:stackql-mcp` library spawns the signed StackQL binary over stdio behind the official Kotlin MCP SDK client, and a companion Gradle plugin wires the same launch into a build. This is the pattern behind **costgate**, a cost gate for CI/CD shipped as a CLI and a Gradle plugin.

## Steps

1. **Add the library** to your Gradle dependencies:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Requires JDK 17 and Kotlin 2.x. The client comes from the official Kotlin MCP SDK.

2. **Start the server and list tools.** The builder defaults to `Mode.ReadOnly`:

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

3. **Choose a mode.** `.mode()` defaults to `Mode.ReadOnly`. Set `Mode.Safe`, `Mode.DeleteSafe`, or `Mode.FullAccess` only when the app needs to provision. Escalation is explicit.

## Run StackQL queries from a Gradle build

The Gradle plugin wires the embedded server into your build so CI tasks run StackQL queries without a separate install step. The **costgate** demo gates infrastructure deploys on cost budgets, as a CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

and as a Gradle plugin:

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

## Related concepts

- [Embedded MCP: Kotlin / JVM reference](/docs/mcp/embedded/kotlin) - install, API, and the Gradle plugin
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
