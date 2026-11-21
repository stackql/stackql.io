---
slug: /ai-agents
title: Agen AI
hide_title: false
keywords:
  - stackql
  - agen AI
  - mcp
  - model context protocol
  - agentik
description: Hubungkan agen AI ke layanan cloud dan SaaS dengan StackQL melalui server MCP, alat, integrasi tersemat, dan kueri SQL.
image: "/img/stackql-featured-image.png"
---

StackQL memberi agen AI antarmuka SQL yang sama dengan yang digunakan orang untuk mengkueri, menyediakan, dan mengoperasikan layanan cloud dan SaaS. Agen menemukan apa yang diekspos oleh sebuah provider, memvalidasi kueri sebelum kueri dijalankan, membaca status terkini dengan `SELECT` dan mengubahnya dengan `INSERT`, `UPDATE`, dan `DELETE`, tanpa file status dan tanpa SDK per cloud.

## Cara agen terhubung {#how-agents-connect}

- **MCP melalui stdio** untuk klien editor dan desktop seperti Claude Desktop, Cursor, dan Continue. Klien sendiri yang meluncurkan perintah `stackql mcp --mcp.server.type=stdio`. Lihat [Server MCP](/command-line-usage/mcp).
- **MCP melalui HTTP** untuk agen mandiri dan otomatisasi yang terhubung ke proses `stackql mcp` atau `stackql srv` yang berjalan lama, dengan autentikasi token bearer dan TLS opsional.
- **MCP tersemat** untuk aplikasi terkompilasi: server berjalan di dalam proses aplikasi di Go, Rust, Swift, Kotlin, .NET, dan Gleam. Lihat [MCP tersemat](/mcp/embedded).
- **Protokol komunikasi PostgreSQL** melalui [`stackql srv`](/command-line-usage/srv) untuk framework agen, notebook, dan alat BI yang sudah mendukung Postgres.

## Mulai cepat dengan Claude Desktop {#quick-start-with-claude-desktop}

Instal dari Anthropic Connector Directory (direkomendasikan) atau dari MCP Bundle yang dapat diunduh, atau daftarkan server secara manual di file `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

Sertakan hanya kredensial untuk provider yang akan Anda gunakan. Instruksi lengkap, termasuk unduhan bundle dan penggunaan file kredensial, ada di [Menggunakan StackQL dengan Claude Desktop](/getting-started/claude-desktop).

## Apa yang dapat dilakukan agen {#what-an-agent-can-do}

Server MCP mengekspos sekumpulan kecil alat bertipe. Setiap alat mengembalikan tampilan hasil render untuk model dan payload terstruktur untuk klien yang mengaksesnya secara terprogram.

- **Menemukan**: `list_providers`, `list_services`, `list_resources`, `list_methods`, dan `describe_method` menelusuri hierarki provider dan mengembalikan input serta field output setiap operasi, sehingga parameter `WHERE` yang wajib sudah diketahui sebelum kueri ditulis.
- **Memvalidasi dan membaca**: `validate_select_query` mengurai dan merencanakan pernyataan `SELECT` tanpa menjalankannya; `run_select_query` menjalankannya.
- **Mengubah**: `run_mutation_query` menjalankan `INSERT`, `UPDATE`, `REPLACE`, dan `DELETE`; `run_lifecycle_operation` menjalankan operasi siklus hidup `EXEC`. Keduanya dikendalikan oleh mode server yang dijelaskan di bawah.
- **Pemeliharaan**: `server_info`, `list_registry`, `pull_provider`, dan `reload_credentials`.

Referensi lengkap, dengan input, perilaku pengendalian, dan contoh prompt untuk setiap alat, ada di [Alat MCP](/mcp).

## Batasan pengaman {#guardrails}

Mode server menetapkan kontrak keamanan untuk seluruh sesi:

| Mode | `SELECT` / metadata | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | izinkan | tolak | tolak |
| `safe` (default) | izinkan | perlu persetujuan | perlu persetujuan |
| `delete_safe` | izinkan | izinkan | perlu persetujuan |
| `full_access` | izinkan | izinkan | izinkan |

"Perlu persetujuan" menggunakan elisitasi MCP: klien yang mendukungnya (Claude Desktop, Cursor, Continue) menampilkan nama alat, kelas kueri, dan SQL kepada pengguna lalu menunggu keputusan sebelum apa pun dijalankan; klien yang tidak menyatakan kemampuan tersebut ditolak. Setiap panggilan alat ditulis ke log audit, listener HTTP memerlukan token bearer kecuali jika dibuka secara eksplisit dengan `allow_unauthenticated`, dan kredensial diambil dari lingkungan atau file dotenv tanpa ada nilai rahasia yang pernah dikembalikan ke klien. Detailnya ada di [Mode server](/command-line-usage/mcp#server-modes), [Autentikasi klien HTTP](/command-line-usage/mcp#http-client-authentication), dan [Log audit](/command-line-usage/mcp#audit-log).

## Langkah berikutnya {#next-steps}

- [Server MCP](/command-line-usage/mcp) - mode deployment, konfigurasi, mode server, log audit, dan flag.
- [Alat MCP](/mcp) - referensi lengkap alat dengan input, pengendalian, dan contoh prompt.
- [MCP tersemat](/mcp/embedded) - jalankan server di dalam aplikasi Go, Rust, Swift, Kotlin, .NET, atau Gleam milik Anda sendiri.
- [Query Library](pathname:///docs/query-library/) - SQL siap jalan untuk pertanyaan umum seputar operasi cloud, per provider.
- [Referensi AI](/ai) - konsep, panduan praktis, perbandingan, dan pemecahan masalah yang ditulis untuk agen dan orang yang membangunnya.
