---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - kotlin
  - jvm
  - gradle
description: Betten Sie den StackQL-MCP-Server als Bibliothek oder Gradle-Plugin in eine Kotlin/JVM-Anwendung ein
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Betten Sie den StackQL-MCP-Server in eine Kotlin- oder JVM-Anwendung ein, als Bibliothek oder als Gradle-Plugin. Der Client ist das offizielle Kotlin MCP SDK.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Fügen Sie die Bibliothek `io.stackql:stackql-mcp` hinzu:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Erfordert JDK 17 und Kotlin 2.x. Der Client stammt aus dem offiziellen Kotlin MCP SDK.

* * *

## Schnellstart {#quick-start}

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

Die API besteht aus `StackqlMcp.builder()` mit `.mode()`, `.auth()` und `.start()`, dem Enum `Mode` und `LaunchArgs.authFor()`.

* * *

## Modi {#modes}

Der Builder verwendet standardmäßig `Mode.ReadOnly`. Rufen Sie `.mode()` mit `Mode.Safe`, `Mode.DeleteSafe` oder `Mode.FullAccess` auf, um zu eskalieren. Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Die Bibliothek lädt die signierte Binärdatei herunter, verifiziert sie und legt sie im gemeinsamen Cache unter `~/.stackql/mcp-server-bin/` ab, unabhängig vom Arbeitsverzeichnis. Das Gradle-Plugin bindet denselben Start in Ihren Build ein, sodass CI-Tasks StackQL-Abfragen ohne separaten Installationsschritt ausführen können.

* * *

## Demo-App {#demo-app}

**costgate** macht Infrastruktur-Deployments von Kostenbudgets abhängig. Es wird sowohl als CLI als auch als Gradle-Plugin ausgeliefert.

Als CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Als Gradle-Plugin:

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

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
