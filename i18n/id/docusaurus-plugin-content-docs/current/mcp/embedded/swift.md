---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - swift
  - macos
description: Sertakan biner server MCP StackQL yang telah dinotarisasi di dalam aplikasi macOS yang ditandatangani
image: "/img/stackql-featured-image.png"
---

# Swift

Sematkan server MCP StackQL di aplikasi macOS. Biner yang ditandatangani dan dinotarisasi dapat disertakan di dalam `.app` Anda, dan kliennya adalah SDK MCP resmi untuk Swift.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

Tambahkan paket ke `Package.swift` Anda:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Memerlukan macOS 13+ dan Swift 6.1 (Xcode 16.3+). Satu-satunya dependensi adalah SDK MCP resmi untuk Swift.

* * *

## Mulai cepat {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

API library ini terdiri atas `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)`, dan `.stop()`. `Options` memuat `mode`, `auth`, `binaryOverride`, dan `allowDownload`. Untuk harness eksternal, `StackQLServer.resolveCommand(_:)` mengembalikan perintah peluncuran hasil resolusi.

* * *

## Mode {#modes}

Nilai default `Options.mode` adalah `.readOnly`. Atur ke `.safe`, `.deleteSafe`, atau `.fullAccess` untuk melakukan eskalasi. Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Biner `darwin-universal` ditandatangani dengan Developer ID dan dinotarisasi oleh Apple, sehingga dapat disertakan di dalam `.app` macOS yang ditandatangani dengan notarisasi aplikasi itu sendiri tetap valid. Hal ini hanya ada di library Swift - Anda mendistribusikan satu aplikasi mandiri yang telah dinotarisasi tanpa unduhan saat pertama kali dijalankan.

Urutan resolusi biner:

1. `binaryOverride` yang ditetapkan secara eksplisit.
2. Sumber daya `.app` yang disertakan.
3. Cache bersama di `~/.stackql/mcp-server-bin/`.
4. Unduhan dengan verifikasi sha256 (jika `allowDownload` diatur).

* * *

## Aplikasi demo {#demo-app}

**CloudLens** adalah penjaga cloud di bar menu yang menyematkan biner yang telah dinotarisasi dan menjalankan serangkaian pemeriksaan `read_only`.

* * *

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
