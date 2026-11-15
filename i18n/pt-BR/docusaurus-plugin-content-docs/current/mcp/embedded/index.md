---
title: Servidores MCP incorporados
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Incorpore o servidor MCP do StackQL diretamente em aplicações compiladas em Go, Rust, Swift, Kotlin, .NET e Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

O servidor MCP do StackQL pode ser incorporado diretamente em aplicações compiladas - sem `npx`, sem instalação separada, sem dependência de runtime. O binário assinado é baixado e verificado na primeira execução (modo sidecar) ou incluído por vendoring no binário da sua aplicação, gerando um único executável autocontido.

As seis bibliotecas compartilham um mesmo contrato: o modo `read_only` por padrão, uma inicialização independente do diretório de trabalho, um cache de binários compartilhado em `~/.stackql/mcp-server-bin/` e as mesmas quatro plataformas de destino (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Também estão disponíveis [downloads nativos da CLI e do MCPB](/installing-stackql) para Windows ARM64. Os resolvedores de plataforma de Go, Rust e .NET publicados na v0.12.778 ainda selecionam apenas `windows-x64` no Windows; o novo bundle não adiciona a seleção automática de ARM64 a essas bibliotecas.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Incorporação via go:embed ou sidecar; SDK oficial do MCP para Go. Demonstração: sandboxctl, um concierge de infraestrutura sob demanda.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar ou vendoring com include_bytes!; cliente rmcp. Demonstração: auditron, um copiloto de conformidade para o terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Binário notarizado incluído em um aplicativo assinado para macOS; SDK oficial do MCP para Swift. Demonstração: CloudLens, uma sentinela da nuvem na barra de menus.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Biblioteca + plugin do Gradle; SDK oficial do MCP para Kotlin. Demonstração: costgate, um controle de custos para CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar ou bundle vendored; SDK oficial do MCP para C#. Demonstração: driftwatch, um worker agendado de verificação de desvios.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Supervisionado por OTP no destino Erlang/BEAM; baixa e verifica o binário assinado. Demonstração: pipewatch (planejada).'
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

## Contrato comum {#common-contract}

- Modo `read_only` por padrão, com ativação explícita de `safe`, `delete_safe` ou `full_access`.
- O binário é notarizado pela Apple no macOS e assinado com Authenticode no Windows.
- Os hashes sha256 fixados são publicados para cada versão.
- mcp-name `io.github.stackql/stackql-mcp` no Official MCP Registry.

Consulte a [referência das ferramentas MCP](/mcp) e os [métodos de instalação](/installing-stackql).
