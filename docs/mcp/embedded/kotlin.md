---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - kotlin
  - jvm
  - gradle
description: Embed the StackQL MCP server in a Kotlin/JVM application as a library or Gradle plugin
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Embed the StackQL MCP server in a Kotlin or JVM application, as a library or a Gradle plugin. The client is the official Kotlin MCP SDK.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded).
:::

* * *

## Install

Add the `io.stackql:stackql-mcp` library:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Requires JDK 17 and Kotlin 2.x. The client comes from the official Kotlin MCP SDK.

* * *

## Quick start

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

The API is `StackqlMcp.builder()` with `.mode()`, `.auth()`, and `.start()`, the `Mode` enum, and `LaunchArgs.authFor()`.

* * *

## Modes

The builder defaults to `Mode.ReadOnly`. Call `.mode()` with `Mode.Safe`, `Mode.DeleteSafe`, or `Mode.FullAccess` to escalate. Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

The library downloads, verifies, and caches the signed binary in the shared cache at `~/.stackql/mcp-server-bin/`, independent of the working directory. The Gradle plugin wires the same launch into your build, so CI tasks can run StackQL queries without a separate install step.

* * *

## Demo app

**costgate** gates infrastructure deploys on cost budgets. It ships both as a CLI and as a Gradle plugin.

As a CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

As a Gradle plugin:

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

## Repo link

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
