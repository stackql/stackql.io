---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - gleam
  - erlang
  - beam
description: Sematkan server MCP StackQL di aplikasi Gleam pada target Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Sematkan server MCP StackQL di aplikasi Gleam pada target Erlang/BEAM. Library ini terintegrasi dengan supervisi OTP, mengunduh dan memverifikasi biner yang ditandatangani, lalu menjalankannya melalui stdio.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

```
gleam add stackql_mcp
```

Hanya menargetkan Erlang/BEAM (bukan target JavaScript). Server diekspos sebagai proses anak OTP melalui `child_spec()` sehingga dapat ditempatkan di dalam pohon supervisi Anda sendiri.

* * *

## Mulai cepat {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API library ini terdiri atas `stackql_mcp.start()`, `stop()`, dan `child_spec()` untuk siklus hidup; `list_tools()` dan `call_tool()` untuk operasi (dengan varian `supervised_list_tools()` dan `supervised_call_tool()` untuk digunakan di bawah supervisor); serta `Config`, `default_config()`, dan `auth_for()` untuk konfigurasi.

* * *

## Mode {#modes}

Mode diatur di `Config.mode` dengan nilai default `read_only`. Atur ke `safe`, `delete_safe`, atau `full_access` untuk melakukan eskalasi:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Saat dimulai, library ini mengunduh bundle StackQL untuk platform tersebut, memverifikasinya terhadap nilai sha256 terkunci yang disertakan di dalam library, lalu menyimpannya di cache bersama keluarga library ini. Urutan prioritas resolusi:

1. `Config.binary` yang ditetapkan secara eksplisit.
2. Variabel lingkungan `STACKQL_MCP_BIN`.
3. Cache bersama di `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Untuk deployment offline atau air-gapped, atur `STACKQL_MCP_BUNDLE` agar mengarah ke bundle lokal.

* * *

## Aplikasi demo {#demo-app}

**pipewatch** adalah demo yang direncanakan untuk library ini. Demo ini ada dalam peta jalan dan belum diimplementasikan.

* * *

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
