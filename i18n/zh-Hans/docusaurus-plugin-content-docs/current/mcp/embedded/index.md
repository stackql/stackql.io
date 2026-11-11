---
title: 嵌入式 MCP 服务器
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - go
  - rust
  - swift
  - kotlin
description: 将 StackQL MCP 服务器直接嵌入 Go、Rust、Swift 和 Kotlin 的已编译应用
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

StackQL 的 MCP 服务器可以直接嵌入已编译的应用：无需 `npx`，无需单独安装，也没有运行时依赖。已签名的二进制文件在首次运行时下载并校验（sidecar 模式），或以 vendoring 方式打包进你的应用二进制文件，生成单个自包含的可执行文件。

全部四个库遵循同一契约：默认使用 `read_only` 模式，启动方式不依赖当前工作目录，共享位于 `~/.stackql/mcp-server-bin/` 的二进制文件缓存，并支持相同的四个平台目标（`linux-x64`、`linux-arm64`、`windows-x64`、`darwin-universal`）。

另外还提供原生 Windows ARM64 的 [CLI 和 MCPB 下载](/installing-stackql)。随 v0.12.778 发布的 Go、Rust 和 .NET 平台解析器在 Windows 上仍然只选择 `windows-x64`；新的 bundle 不会为这些库添加自动的 ARM64 选择。

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: '通过 go:embed 或 sidecar 嵌入；官方 Go MCP SDK。演示：sandboxctl，一个按需基础设施管家。'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar 或 include_bytes! vendoring；rmcp 客户端。演示：auditron，一个终端合规助手。'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: '将经过公证的二进制文件打包到已签名的 macOS 应用中；官方 Swift MCP SDK。演示：CloudLens，一个菜单栏云哨兵。'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: '库 + Gradle 插件；官方 Kotlin MCP SDK。演示：costgate，面向 CI/CD 的成本门控。'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar 或 vendored bundle；官方 C# MCP SDK。演示：driftwatch，一个定时执行漂移检查的 worker。'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: '在 Erlang/BEAM 目标上由 OTP 监督；下载并校验已签名的二进制文件。演示：pipewatch（计划中）。'
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

## 通用契约 {#common-contract}

- 默认使用 `read_only` 模式，需显式选择启用 `safe`、`delete_safe` 或 `full_access`。
- 二进制文件在 macOS 上经 Apple 公证，在 Windows 上经 Authenticode 签名。
- 每个发布版本都会公布 sha256 固定哈希值。
- 在 Official MCP Registry 上的 mcp-name 为 `io.github.stackql/stackql-mcp`。

参阅 [MCP 工具参考](/mcp)和[安装方式](/installing-stackql)。
