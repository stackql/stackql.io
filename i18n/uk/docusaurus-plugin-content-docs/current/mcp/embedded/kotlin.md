---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - kotlin
  - jvm
  - gradle
description: Вбудуйте сервер MCP StackQL у застосунок Kotlin/JVM як бібліотеку або плагін Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Вбудуйте сервер MCP StackQL у застосунок Kotlin або JVM як бібліотеку чи плагін Gradle. Клієнт - офіційний SDK MCP для Kotlin.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [Спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

Додайте бібліотеку `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Потрібні JDK 17 і Kotlin 2.x. Клієнт надає офіційний SDK MCP для Kotlin.

* * *

## Швидкий старт {#quick-start}

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

API складається з `StackqlMcp.builder()` з методами `.mode()`, `.auth()` і `.start()`, переліку `Mode` і `LaunchArgs.authFor()`.

* * *

## Режими {#modes}

За замовчуванням будівельник використовує `Mode.ReadOnly`. Щоб підвищити рівень доступу, викличте `.mode()` зі значенням `Mode.Safe`, `Mode.DeleteSafe` або `Mode.FullAccess`. Підвищення рівня доступу завжди явне - сервер не вмикає інструменти змінення даних і життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Бібліотека завантажує, перевіряє й кешує підписаний двійковий файл у спільному кеші в каталозі `~/.stackql/mcp-server-bin/` незалежно від робочого каталогу. Плагін Gradle підключає такий самий запуск до вашої збірки, тож завдання CI можуть виконувати запити StackQL без окремого кроку встановлення.

* * *

## Демонстраційний застосунок {#demo-app}

**costgate** контролює розгортання інфраструктури на основі бюджетів витрат. Застосунок доступний і як CLI, і як плагін Gradle.

Як CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Як плагін Gradle:

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

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
