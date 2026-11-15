---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - kotlin
  - jvm
  - gradle
description: Incorpore o servidor MCP do StackQL em uma aplicação Kotlin/JVM como biblioteca ou plugin do Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Incorpore o servidor MCP do StackQL em uma aplicação Kotlin ou JVM, como biblioteca ou como plugin do Gradle. O cliente é o SDK oficial do MCP para Kotlin.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído na sua aplicação por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

Adicione a biblioteca `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Requer o JDK 17 e o Kotlin 2.x. O cliente vem do SDK oficial do MCP para Kotlin.

* * *

## Início rápido {#quick-start}

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

A API é composta por `StackqlMcp.builder()` com `.mode()`, `.auth()` e `.start()`, o enum `Mode` e `LaunchArgs.authFor()`.

* * *

## Modos {#modes}

O builder usa `Mode.ReadOnly` por padrão. Chame `.mode()` com `Mode.Safe`, `Mode.DeleteSafe` ou `Mode.FullAccess` para elevar o modo. A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

A biblioteca baixa, verifica e armazena o binário assinado no cache compartilhado em `~/.stackql/mcp-server-bin/`, independentemente do diretório de trabalho. O plugin do Gradle conecta a mesma inicialização ao seu build, para que os jobs de CI possam executar consultas do StackQL sem uma etapa de instalação separada.

* * *

## Aplicativo de demonstração {#demo-app}

**costgate** controla as implantações de infraestrutura com base em orçamentos de custo. Ele é distribuído como CLI e como plugin do Gradle.

Como CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Como plugin do Gradle:

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

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
