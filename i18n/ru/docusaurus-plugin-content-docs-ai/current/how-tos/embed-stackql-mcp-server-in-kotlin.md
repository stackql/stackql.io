---
title: Как встроить MCP-сервер StackQL в приложение Kotlin или JVM
description: Встройте MCP-сервер StackQL в приложение Kotlin/JVM в виде библиотеки или плагина Gradle, чтобы сервис на JVM или конвейер CI/CD выполнял запросы к облаку и проверки затрат на SQL без зависимости от среды выполнения.
keywords: [stackql, mcp, kotlin, jvm, плагин gradle, встроенный MCP-сервер, model context protocol, mcp sdk для kotlin, ci cd, агентная инфраструктура, costgate]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение Kotlin?
    answer: Добавьте библиотеку `io.stackql:stackql-mcp:0.1.0` в зависимости Gradle, затем вызовите `StackqlMcp.builder().mode(Mode.ReadOnly).start()`. Клиент - официальный MCP SDK для Kotlin. При первом запуске подписанный двоичный файл скачивается в общий кэш в ~/.stackql/mcp-server-bin/ и проверяется по sha256. Требуются JDK 17 и Kotlin 2.x.
  - question: Можно ли выполнять запросы StackQL из сборки Gradle или конвейера CI?
    answer: Да. Плагин Gradle (id "io.stackql.costgate") подключает встроенный сервер к вашей сборке, поэтому задачи CI выполняют запросы StackQL без отдельного шага установки. Демонстрационное приложение costgate пропускает развертывания инфраструктуры только в пределах бюджета затрат и поставляется как в виде CLI, так и в виде плагина Gradle.
  - question: Как оставить встроенный MCP-сервер StackQL в режиме только для чтения в Kotlin?
    answer: Вызовите .mode(Mode.ReadOnly) у построителя; это значение по умолчанию. ReadOnly отклоняет INSERT, UPDATE, DELETE и EXEC. Повышайте уровень доступа до Mode.Safe, Mode.DeleteSafe или Mode.FullAccess, только когда сборке или сервису нужно создавать ресурсы - повышение уровня доступа всегда выполняется явно.
  - question: Какой MCP-клиент использует stackql-mcp-kotlin?
    answer: Официальный MCP SDK для Kotlin. Запущенный сервер предоставляет server.client, поэтому вы напрямую вызываете server.client.listTools().tools и другие методы SDK.
---

# Как встроить MCP-сервер StackQL в приложение Kotlin или JVM

MCP-сервер StackQL может работать внутри приложения Kotlin или JVM без `npx`, без отдельной установки и без зависимости от среды выполнения. Библиотека `io.stackql:stackql-mcp` запускает подписанный двоичный файл StackQL через stdio и работает с ним через клиент официального MCP SDK для Kotlin, а сопутствующий плагин Gradle подключает тот же механизм запуска к сборке. На этой схеме основан **costgate** - шлюз контроля затрат для CI/CD, поставляемый в виде CLI и плагина Gradle.

## Шаги {#steps}

1. **Добавьте библиотеку** в зависимости Gradle:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Требуются JDK 17 и Kotlin 2.x. Клиент предоставляется официальным MCP SDK для Kotlin.

2. **Запустите сервер и выведите список инструментов.** По умолчанию построитель использует `Mode.ReadOnly`:

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

3. **Выберите режим.** Метод `.mode()` по умолчанию использует `Mode.ReadOnly`. Задавайте `Mode.Safe`, `Mode.DeleteSafe` или `Mode.FullAccess`, только если приложению нужно создавать ресурсы. Повышение уровня доступа выполняется явно.

## Выполнение запросов StackQL из сборки Gradle {#run-stackql-queries-from-a-gradle-build}

Плагин Gradle подключает встроенный сервер к вашей сборке, поэтому задачи CI выполняют запросы StackQL без отдельного шага установки. Демонстрационное приложение **costgate**, которое пропускает развертывания инфраструктуры только в пределах бюджета затрат, работает в виде CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

и в виде плагина Gradle:

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по Kotlin / JVM](/mcp/embedded/kotlin) - установка, API и плагин Gradle
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - транспорты, режимы и внутреннее устройство аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
