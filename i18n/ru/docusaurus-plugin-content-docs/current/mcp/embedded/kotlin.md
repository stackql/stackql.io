---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - kotlin
  - jvm
  - gradle
description: Встройте MCP-сервер StackQL в приложение Kotlin/JVM в виде библиотеки или плагина Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Встройте MCP-сервер StackQL в приложение Kotlin или JVM в виде библиотеки или плагина Gradle. Клиент - официальный MCP SDK для Kotlin.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

Добавьте библиотеку `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Требуются JDK 17 и Kotlin 2.x. Клиент предоставляется официальным MCP SDK для Kotlin.

* * *

## Быстрый старт {#quick-start}

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

API включает `StackqlMcp.builder()` с методами `.mode()`, `.auth()` и `.start()`, перечисление `Mode` и `LaunchArgs.authFor()`.

* * *

## Режимы {#modes}

По умолчанию построитель использует `Mode.ReadOnly`. Чтобы повысить уровень доступа, вызовите `.mode()` со значением `Mode.Safe`, `Mode.DeleteSafe` или `Mode.FullAccess`. Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных и жизненного цикла, пока вы сами их не включите.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

Библиотека скачивает, проверяет и кэширует подписанный двоичный файл в общем кэше в каталоге `~/.stackql/mcp-server-bin/` независимо от рабочего каталога. Плагин Gradle подключает тот же механизм запуска к вашей сборке, поэтому задачи CI могут выполнять запросы StackQL без отдельного шага установки.

* * *

## Демонстрационное приложение {#demo-app}

**costgate** пропускает развертывания инфраструктуры только в пределах бюджета затрат. Инструмент поставляется и в виде CLI, и в виде плагина Gradle.

В виде CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

В виде плагина Gradle:

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

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
