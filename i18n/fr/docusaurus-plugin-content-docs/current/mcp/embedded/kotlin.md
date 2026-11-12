---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - kotlin
  - jvm
  - gradle
description: Embarquez le serveur MCP StackQL dans une application Kotlin/JVM sous forme de bibliothèque ou de plugin Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Embarquez le serveur MCP StackQL dans une application Kotlin ou JVM, sous forme de bibliothèque ou de plugin Gradle. Le client est le SDK MCP officiel pour Kotlin.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Ajoutez la bibliothèque `io.stackql:stackql-mcp` :

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Nécessite le JDK 17 et Kotlin 2.x. Le client provient du SDK MCP officiel pour Kotlin.

* * *

## Démarrage rapide {#quick-start}

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

L'API se compose de `StackqlMcp.builder()` avec `.mode()`, `.auth()` et `.start()`, de l'énumération `Mode` et de `LaunchArgs.authFor()`.

* * *

## Modes {#modes}

Le builder utilise `Mode.ReadOnly` par défaut. Appelez `.mode()` avec `Mode.Safe`, `Mode.DeleteSafe` ou `Mode.FullAccess` pour élever le niveau d'accès. L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

La bibliothèque télécharge et vérifie le binaire signé, puis le met en cache dans le cache partagé `~/.stackql/mcp-server-bin/`, indépendamment du répertoire de travail. Le plugin Gradle intègre le même lancement à votre build, de sorte que les tâches de CI peuvent exécuter des requêtes StackQL sans étape d'installation séparée.

* * *

## Application de démonstration {#demo-app}

**costgate** soumet les déploiements d'infrastructure à un contrôle des budgets de coûts. Il est disponible à la fois sous forme de CLI et de plugin Gradle.

En tant que CLI :

```bash
costgate check --intent examples/costgate.yaml --explain
```

En tant que plugin Gradle :

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

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
