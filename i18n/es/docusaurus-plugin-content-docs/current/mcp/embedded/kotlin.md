---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - kotlin
  - jvm
  - gradle
description: Integre el servidor MCP de StackQL en una aplicación Kotlin/JVM como biblioteca o como plugin de Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Integre el servidor MCP de StackQL en una aplicación Kotlin o JVM, como biblioteca o como plugin de Gradle. El cliente es el SDK oficial de MCP para Kotlin.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

Agregue la biblioteca `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Requiere JDK 17 y Kotlin 2.x. El cliente procede del SDK oficial de MCP para Kotlin.

* * *

## Inicio rápido {#quick-start}

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

La API es `StackqlMcp.builder()` con `.mode()`, `.auth()` y `.start()`, la enumeración `Mode` y `LaunchArgs.authFor()`.

* * *

## Modos {#modes}

El builder usa `Mode.ReadOnly` de forma predeterminada. Llame a `.mode()` con `Mode.Safe`, `Mode.DeleteSafe` o `Mode.FullAccess` para elevar el modo. La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

La biblioteca descarga, verifica y almacena en caché el binario firmado en la caché compartida de `~/.stackql/mcp-server-bin/`, independientemente del directorio de trabajo. El plugin de Gradle conecta ese mismo inicio a su build, de modo que las tareas de CI pueden ejecutar consultas de StackQL sin un paso de instalación por separado.

* * *

## Aplicación de demostración {#demo-app}

**costgate** controla los despliegues de infraestructura en función de presupuestos de costos. Se distribuye como CLI y como plugin de Gradle.

Como CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Como plugin de Gradle:

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

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
