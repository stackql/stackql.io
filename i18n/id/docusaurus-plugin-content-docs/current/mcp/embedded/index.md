---
title: Server MCP tersemat
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Sematkan server MCP StackQL langsung di aplikasi terkompilasi dalam Go, Rust, Swift, Kotlin, .NET, dan Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

Server MCP StackQL dapat disematkan langsung di aplikasi terkompilasi - tanpa `npx`, tanpa instalasi terpisah, tanpa dependensi runtime. Biner yang ditandatangani diunduh dan diverifikasi saat pertama kali dijalankan (mode sidecar) atau disertakan dalam biner aplikasi Anda melalui vendoring sehingga menghasilkan satu file executable mandiri.

Keenam library menggunakan kontrak yang sama: mode `read_only` secara default, peluncuran yang tidak bergantung pada direktori kerja, cache biner bersama di `~/.stackql/mcp-server-bin/`, dan empat target platform yang sama (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

[Unduhan CLI dan MCPB native untuk Windows ARM64](/installing-stackql) juga tersedia. Resolver platform Go, Rust, dan .NET yang dirilis di v0.12.778 masih hanya memilih `windows-x64` di Windows; bundle baru ini tidak menambahkan pemilihan ARM64 otomatis ke library tersebut.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Penyematan melalui go:embed atau sidecar; SDK MCP resmi untuk Go. Demo: sandboxctl, concierge infrastruktur sesuai permintaan.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar atau vendoring dengan include_bytes!; klien rmcp. Demo: auditron, copilot kepatuhan di terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Sertakan biner yang telah dinotarisasi dalam aplikasi macOS yang ditandatangani; SDK MCP resmi untuk Swift. Demo: CloudLens, penjaga cloud di bar menu.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Library + plugin Gradle; SDK MCP resmi untuk Kotlin. Demo: costgate, gerbang biaya untuk CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar atau bundle vendored; SDK MCP resmi untuk C#. Demo: driftwatch, worker terjadwal untuk pemeriksaan penyimpangan.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Diawasi OTP pada target Erlang/BEAM; mengunduh dan memverifikasi biner yang ditandatangani. Demo: pipewatch (direncanakan).'
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

## Kontrak umum {#common-contract}

- Mode `read_only` secara default, dengan aktivasi eksplisit untuk `safe`, `delete_safe`, atau `full_access`.
- Biner dinotarisasi oleh Apple di macOS dan ditandatangani dengan Authenticode di Windows.
- Nilai sha256 yang dikunci dipublikasikan untuk setiap rilis.
- mcp-name `io.github.stackql/stackql-mcp` di Official MCP Registry.

Lihat [referensi alat MCP](/mcp) dan [cara instalasi](/installing-stackql).
