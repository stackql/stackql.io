---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - kotlin
  - jvm
  - gradle
description: Sematkan server MCP StackQL di aplikasi Kotlin/JVM sebagai library atau plugin Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Sematkan server MCP StackQL di aplikasi Kotlin atau JVM, sebagai library atau plugin Gradle. Kliennya adalah SDK MCP resmi untuk Kotlin.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

Tambahkan library `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Memerlukan JDK 17 dan Kotlin 2.x. Klien berasal dari SDK MCP resmi untuk Kotlin.

* * *

## Mulai cepat {#quick-start}

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

API library ini terdiri atas `StackqlMcp.builder()` dengan `.mode()`, `.auth()`, dan `.start()`, enum `Mode`, dan `LaunchArgs.authFor()`.

* * *

## Mode {#modes}

Builder menggunakan `Mode.ReadOnly` secara default. Panggil `.mode()` dengan `Mode.Safe`, `Mode.DeleteSafe`, atau `Mode.FullAccess` untuk melakukan eskalasi. Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Library ini mengunduh, memverifikasi, dan menyimpan biner yang ditandatangani di cache bersama di `~/.stackql/mcp-server-bin/`, terlepas dari direktori kerja. Plugin Gradle menghubungkan peluncuran yang sama ke build Anda, sehingga task CI dapat menjalankan kueri StackQL tanpa langkah instalasi terpisah.

* * *

## Aplikasi demo {#demo-app}

**costgate** mengendalikan deployment infrastruktur berdasarkan anggaran biaya. Aplikasi ini tersedia baik sebagai CLI maupun sebagai plugin Gradle.

Sebagai CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Sebagai plugin Gradle:

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

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
