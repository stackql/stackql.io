---
slug: windows-arm64-build-available
title: "StackQL now runs natively on Windows ARM64"
hide_table_of_contents: true
authors:
  - jeffreyaven
description: "Native Windows ARM64 builds are now available for the StackQL CLI and MCP server, with MSI, ZIP and MCPB downloads."
keywords: [stackql, windows, arm64, cli, mcp, mcpb]
tags: [stackql, windows, arm64, mcp]
---

StackQL now runs natively on Windows ARM64. [v0.12.778](https://github.com/stackql/stackql/releases/tag/v0.12.778) adds signed ARM64 builds for the CLI (`stackql.exe`) and MCP server.

- **CLI:** download the [ARM64 MSI installer](https://releases.stackql.io/stackql/latest/stackql_windows_arm64.msi) or [ZIP archive](https://releases.stackql.io/stackql/latest/stackql_windows_arm64.zip).
- **Claude Desktop:** install the [ARM64 MCPB](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb), with no separate CLI installation required.

<!-- truncate -->

The [installation page](/installing-stackql) now offers AMD64 and ARM64 dropdown options for Windows downloads. `amd64` remains the default and continues to work on `arm64` through emulation.  Releases prior to [v0.12.778](https://github.com/stackql/stackql/releases/tag/v0.12.778) used `x64` emulation for `arm64` support.
