---
title: Servidores MCP integrados
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Integre el servidor MCP de StackQL directamente en aplicaciones compiladas en Go, Rust, Swift, Kotlin, .NET y Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

El servidor MCP de StackQL se puede integrar directamente en aplicaciones compiladas - sin `npx`, sin instalación por separado, sin dependencia de runtime. El binario firmado se descarga y se verifica en la primera ejecución (modo sidecar) o se incorpora mediante vendoring al binario de su aplicación para obtener un único ejecutable autónomo.

Las seis bibliotecas comparten un mismo contrato: el modo `read_only` de forma predeterminada, un inicio independiente del directorio de trabajo, una caché de binarios compartida en `~/.stackql/mcp-server-bin/` y las mismas cuatro plataformas de destino (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

También están disponibles [descargas de la CLI y del MCPB](/installing-stackql) nativas para Windows ARM64. Los resolvedores de plataforma de Go, Rust y .NET publicados en la v0.12.778 siguen seleccionando solo `windows-x64` en Windows; el nuevo bundle no añade la selección automática de ARM64 a esas bibliotecas.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Integración mediante go:embed o sidecar; SDK oficial de MCP para Go. Demostración: sandboxctl, un asistente de infraestructura bajo demanda.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar o vendoring con include_bytes!; cliente rmcp. Demostración: auditron, un copiloto de cumplimiento para terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Binario notarizado incluido en una aplicación de macOS firmada; SDK oficial de MCP para Swift. Demostración: CloudLens, un centinela de la nube para la barra de menús.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Biblioteca + plugin de Gradle; SDK oficial de MCP para Kotlin. Demostración: costgate, un control de costos para CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar o bundle vendored; SDK oficial de MCP para C#. Demostración: driftwatch, un worker programado de comprobación de desviaciones.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Supervisado por OTP en el destino Erlang/BEAM; descarga y verifica el binario firmado. Demostración: pipewatch (prevista).'
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

## Contrato común {#common-contract}

- Modo `read_only` de forma predeterminada, con activación explícita de `safe`, `delete_safe` o `full_access`.
- El binario está notarizado por Apple en macOS y firmado con Authenticode en Windows.
- Se publican los hashes sha256 fijados de cada versión.
- mcp-name `io.github.stackql/stackql-mcp` en el Official MCP Registry.

Consulte la [referencia de herramientas MCP](/mcp) y los [métodos de instalación](/installing-stackql).
