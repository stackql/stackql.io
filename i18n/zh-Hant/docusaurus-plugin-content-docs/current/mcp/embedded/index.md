---
title: 內嵌式 MCP 伺服器
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: 在 Go、Rust、Swift、Kotlin、.NET 與 Gleam 的編譯式應用程式中直接內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

StackQL 的 MCP 伺服器可以直接內嵌在編譯式應用程式中：不需要 `npx`、不需要另外安裝，也沒有執行階段相依性。已簽署的二進位檔會在首次執行時下載並驗證（sidecar 模式），或透過 vendoring 納入您的應用程式二進位檔，成為單一的獨立可執行檔。

六個程式庫都遵循同一份契約：預設為 `read_only` 模式、與 cwd 無關的啟動方式、位於 `~/.stackql/mcp-server-bin/` 的共用二進位檔快取，以及相同的四個平台目標（`linux-x64`、`linux-arm64`、`windows-x64`、`darwin-universal`）。

另外也提供原生 Windows ARM64 的 [CLI 與 MCPB 下載](/installing-stackql)。v0.12.778 中隨附的 Go、Rust 與 .NET 平台解析邏輯在 Windows 上仍只會選取 `windows-x64`；新的安裝套件並不會為這些程式庫加入自動選取 ARM64 的功能。

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: '透過 go:embed 或 sidecar 內嵌；官方 Go MCP SDK。示範：sandboxctl，隨選的基礎架構管家。'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar 或 include_bytes! vendoring；rmcp 用戶端。示範：auditron，終端機合規性 copilot。'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: '將經過公證的二進位檔打包到已簽署的 macOS 應用程式中；官方 Swift MCP SDK。示範：CloudLens，選單列雲端哨兵。'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: '程式庫 + Gradle 外掛程式；官方 Kotlin MCP SDK。示範：costgate，CI/CD 的成本關卡。'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar 或 vendored 安裝套件；官方 C# MCP SDK。示範：driftwatch，依排程執行漂移檢查的 worker。'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: '在 Erlang/BEAM 目標上受 OTP 監督；下載並驗證已簽署的二進位檔。示範：pipewatch（規劃中）。'
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

## 共同契約 {#common-contract}

- 預設為 `read_only` 模式，需明確選擇加入才能使用 `safe`、`delete_safe` 或 `full_access`。
- 二進位檔在 macOS 上經過 Apple 公證，在 Windows 上經過 Authenticode 簽署。
- 每個發行版本都會發佈 sha256 釘選值。
- 在 Official MCP Registry 上的 mcp-name 為 `io.github.stackql/stackql-mcp`。

請參閱 [MCP 工具參考](/mcp) 與[安裝方式](/installing-stackql)。
