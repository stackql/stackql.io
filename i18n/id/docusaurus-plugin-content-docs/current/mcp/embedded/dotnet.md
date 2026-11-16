---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - dotnet
  - csharp
description: Sematkan server MCP StackQL di aplikasi .NET sebagai sidecar atau bundle vendored
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Sematkan server MCP StackQL di aplikasi .NET, baik sebagai sidecar yang diunduh maupun disertakan dalam build Anda melalui vendoring sebagai file executable mandiri. Kliennya adalah SDK MCP resmi untuk C#.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

```
dotnet add package StackQL.Mcp
```

Paket opsional `StackQL.Mcp.AgentFramework` menambahkan integrasi Agent Framework. Memerlukan .NET 8 atau yang lebih baru.

* * *

## Mulai cepat {#quick-start}

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

API library ini terdiri atas `StackqlMcp.CreateBuilder()` dengan `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()`, dan `.WithCommand()`, lalu `.StartAsync()`, `.ListToolsAsync()`, dan `.CallToolAsync()`.

* * *

## Mode {#modes}

Builder menggunakan `StackqlMode.ReadOnly` secara default. Panggil `.WithMode()` dengan `StackqlMode.Safe`, `StackqlMode.DeleteSafe`, atau `StackqlMode.FullAccess` untuk melakukan eskalasi. Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Ada dua cara untuk memperoleh biner:

- **Sidecar (default)** - bundle platform diunduh saat pertama kali dijalankan, diverifikasi dengan sha256, disimpan di cache di `~/.stackql/mcp-server-bin/<version>/<platform-key>/`, lalu dijalankan melalui stdio.
- **Vendored** - sematkan bundle sebagai sumber daya build untuk menghasilkan file executable mandiri:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Anda juga dapat mengganti biner atau bundle saat runtime dengan variabel lingkungan `STACKQL_MCP_BIN` atau `STACKQL_MCP_BUNDLE`.

* * *

## Aplikasi demo {#demo-app}

**driftwatch** adalah .NET Worker Service yang menyematkan server StackQL dalam mode hanya baca dan, sesuai jadwal, menjalankan serangkaian pemeriksaan penyimpangan dengan SQL (eksposur publik, tag atau lisensi yang hilang, penyimpangan dari baseline) lalu mengirimkan temuannya ke Teams Adaptive Card.

* * *

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
