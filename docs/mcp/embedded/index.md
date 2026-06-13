---
title: Embedded MCP Servers
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - go
  - rust
  - swift
  - kotlin
description: Embed the StackQL MCP server directly in compiled applications across Go, Rust, Swift, and Kotlin
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

StackQL's MCP server can be embedded directly in compiled applications - no `npx`, no separate install, no runtime dependency. The signed binary is downloaded and verified on first run (sidecar mode) or vendored into your app binary for a single self-contained executable.

All four libraries share one contract: `read_only` mode by default, a cwd-independent launch, a shared binary cache at `~/.stackql/mcp-server-bin/`, and the same four platform targets (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/docs/mcp/embedded/go',
    description: 'Embed via go:embed or sidecar; official Go MCP SDK. Demo: sandboxctl, an on-demand infrastructure concierge.'
  },
  {
    name: 'Rust',
    href: '/docs/mcp/embedded/rust',
    description: 'Sidecar or include_bytes! vendoring; rmcp client. Demo: auditron, a terminal compliance copilot.'
  },
  {
    name: 'Swift',
    href: '/docs/mcp/embedded/swift',
    description: 'Bundle the notarised binary in a signed macOS app; official Swift MCP SDK. Demo: CloudLens, a menu-bar cloud sentinel.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/docs/mcp/embedded/kotlin',
    description: 'Library + Gradle plugin; official Kotlin MCP SDK. Demo: costgate, a cost gate for CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/docs/mcp/embedded/dotnet',
    description: 'Sidecar or vendored bundle; official C# MCP SDK. Demo: driftwatch, a scheduled drift-check worker.'
  },
  {
    name: 'Gleam',
    href: '/docs/mcp/embedded/gleam',
    description: 'OTP-supervised on the Erlang/BEAM target; downloads and verifies the signed binary. Demo: pipewatch (planned).'
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

## Common contract

- Default `read_only` mode, with explicit opt-in to `safe`, `delete_safe`, or `full_access`.
- The binary is Apple-notarised on macOS and Authenticode-signed on Windows.
- sha256 pins are published per release.
- mcp-name `io.github.stackql/stackql-mcp` on the Official MCP Registry.

See the [MCP tools reference](/docs/mcp) and the [install vectors](/docs/installing-stackql).
