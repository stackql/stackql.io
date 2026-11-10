---
title: Eingebettete MCP-Server
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - go
  - rust
  - swift
  - kotlin
description: Betten Sie den StackQL-MCP-Server direkt in kompilierte Anwendungen in Go, Rust, Swift und Kotlin ein
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

Der MCP-Server von StackQL kann direkt in kompilierte Anwendungen eingebettet werden - kein `npx`, keine separate Installation, keine Laufzeitabhängigkeit. Die signierte Binärdatei wird beim ersten Start heruntergeladen und verifiziert (Sidecar-Modus) oder per Vendoring in die Binärdatei Ihrer App eingebunden, sodass eine einzelne eigenständige ausführbare Datei entsteht.

Alle vier Bibliotheken teilen sich einen Vertrag: standardmäßig der `read_only`-Modus, ein vom Arbeitsverzeichnis unabhängiger Start, ein gemeinsamer Binärdatei-Cache unter `~/.stackql/mcp-server-bin/` und dieselben vier Plattformziele (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Für Windows ARM64 sind auch native [CLI- und MCPB-Downloads](/installing-stackql) verfügbar. Die mit v0.12.778 ausgelieferten Plattform-Resolver für Go, Rust und .NET wählen unter Windows weiterhin nur `windows-x64` aus; das neue Bundle fügt diesen Bibliotheken keine automatische ARM64-Auswahl hinzu.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Einbetten über go:embed oder als Sidecar; offizielles Go MCP SDK. Demo: sandboxctl, ein On-Demand-Infrastruktur-Concierge.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar oder include_bytes!-Vendoring; rmcp-Client. Demo: auditron, ein Compliance-Copilot für das Terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Bündeln der notarisierten Binärdatei in einer signierten macOS-App; offizielles Swift MCP SDK. Demo: CloudLens, ein Cloud-Wächter für die Menüleiste.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Bibliothek + Gradle-Plugin; offizielles Kotlin MCP SDK. Demo: costgate, ein Kosten-Gate für CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar oder per Vendoring eingebundenes Bundle; offizielles C# MCP SDK. Demo: driftwatch, ein zeitgesteuerter Worker für Drift-Prüfungen.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'OTP-überwacht auf dem Erlang/BEAM-Target; lädt die signierte Binärdatei herunter und verifiziert sie. Demo: pipewatch (geplant).'
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

## Gemeinsamer Vertrag {#common-contract}

- Standardmäßig `read_only`-Modus, mit explizitem Opt-in für `safe`, `delete_safe` oder `full_access`.
- Die Binärdatei ist unter macOS von Apple notarisiert und unter Windows Authenticode-signiert.
- sha256-Pins werden pro Release veröffentlicht.
- mcp-name `io.github.stackql/stackql-mcp` in der Official MCP Registry.

Siehe die [MCP-Tools-Referenz](/mcp) und die [Installationswege](/installing-stackql).
