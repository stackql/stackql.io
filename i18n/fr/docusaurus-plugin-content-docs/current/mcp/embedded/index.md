---
title: Serveurs MCP embarqués
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - go
  - rust
  - swift
  - kotlin
description: Embarquez le serveur MCP StackQL directement dans des applications compilées en Go, Rust, Swift et Kotlin
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

Le serveur MCP de StackQL peut être embarqué directement dans des applications compilées - pas de `npx`, pas d'installation séparée, aucune dépendance à l'exécution. Le binaire signé est téléchargé et vérifié à la première exécution (mode sidecar) ou vendored dans le binaire de votre application pour obtenir un exécutable unique et autonome.

Les quatre bibliothèques partagent un même contrat : le mode `read_only` par défaut, un lancement indépendant du répertoire de travail, un cache de binaires partagé dans `~/.stackql/mcp-server-bin/` et les quatre mêmes plateformes cibles (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Des [téléchargements de la CLI et du MCPB](/installing-stackql) natifs pour Windows ARM64 sont également disponibles. Les résolveurs de plateforme Go, Rust et .NET livrés dans la v0.12.778 ne sélectionnent toujours que `windows-x64` sous Windows ; le nouveau bundle n'ajoute pas de sélection automatique d'ARM64 à ces bibliothèques.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: "Embarqué via go:embed ou en sidecar ; SDK MCP officiel pour Go. Démo : sandboxctl, un concierge d'infrastructure à la demande."
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar ou vendoring avec include_bytes! ; client rmcp. Démo : auditron, un copilote de conformité pour le terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Binaire notarisé inclus dans une application macOS signée ; SDK MCP officiel pour Swift. Démo : CloudLens, une sentinelle cloud pour la barre de menus.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Bibliothèque + plugin Gradle ; SDK MCP officiel pour Kotlin. Démo : costgate, un contrôle des coûts pour CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar ou bundle vendored ; SDK MCP officiel pour C#. Démo : driftwatch, un worker planifié de vérification de dérive.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Supervisé par OTP sur la cible Erlang/BEAM ; télécharge et vérifie le binaire signé. Démo : pipewatch (prévue).'
  }
];

<DocCardList
  items={EMBEDDED_LIBRARIES.map(lib => ({
    type: 'link',
    label: lib.name,
    href: lib.href,
    description: lib.description,
    customProps: { iconComponent: <FaRobot size={18} className="homeCardIcon" /> }
  }))}
/>

## Contrat commun {#common-contract}

- Mode `read_only` par défaut, avec activation explicite de `safe`, `delete_safe` ou `full_access`.
- Le binaire est notarisé par Apple sous macOS et signé Authenticode sous Windows.
- Les empreintes sha256 épinglées sont publiées pour chaque version.
- mcp-name `io.github.stackql/stackql-mcp` sur l'Official MCP Registry.

Consultez la [référence des outils MCP](/mcp) et les [méthodes d'installation](/installing-stackql).
