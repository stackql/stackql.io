---
title: Menggunakan StackQL dengan Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - multi-cloud
description: Gunakan StackQL dengan Claude Desktop untuk mengkueri dan menyediakan sumber daya cloud menggunakan bahasa alami
image: "/img/stackql-featured-image.png"
---

StackQL terintegrasi dengan Claude Desktop melalui Model Context Protocol (MCP), sehingga Anda dapat mengkueri dan menyediakan sumber daya cloud di beberapa penyedia cloud menggunakan bahasa alami. Kombinasi yang andal ini memungkinkan Anda berinteraksi dengan infrastruktur cloud Anda melalui percakapan sambil memanfaatkan antarmuka StackQL yang berbasis SQL di balik layar.

Ada tiga opsi instalasi, yang disajikan di sini sesuai urutan yang direkomendasikan: menginstal dari Anthropic Connector Directory, menginstal dari bundle yang diunduh, atau mendaftarkan biner StackQL yang terinstal secara lokal secara manual.

## Menginstal dari Connector Directory (direkomendasikan) {#install-from-the-connector-directory-recommended}

Cara termudah untuk menggunakan server MCP StackQL dengan Claude Desktop adalah menginstalnya dari Anthropic Connector Directory. Di Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**, cari **StackQL**, lalu klik **Install**. Tidak ada yang perlu diunduh dan tidak diperlukan instalasi StackQL terpisah.

![StackQL di Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

Anda juga dapat melihat entri direktori tersebut secara langsung di [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Opsi ini menginstal server MCP StackQL di dalam proses untuk platform Anda: entri tersebut menyajikan bundle multiplatform gabungan yang berisi biner yang ditandatangani untuk macOS universal, Windows x64, serta Linux x64 dan arm64, dan Claude Desktop memilih biner yang tepat untuk mesin Anda secara otomatis. Untuk Windows ARM64 native, instal bundle `windows-arm64` terpisah dari [unduhan di bawah](#install-from-a-downloaded-bundle). Biner Windows ditandatangani dengan Authenticode dan biner macOS dinotarisasi oleh Apple - biner yang sama dengan rilis StackQL mandiri.

![Menginstal konektor StackQL](/img/stackql-anthropic-directory-install.png)

Konektor yang diinstal dari direktori mewarisi lingkungan Claude Desktop, karena itu atur variabel lingkungan untuk provider Anda di tingkat sistem operasi sebelum meluncurkan Claude Desktop - lihat [Kredensial khusus provider](#provider-specific-credentials). Claude Desktop menangkap lingkungannya saat diluncurkan: setelah mengubah variabel, Anda harus keluar sepenuhnya dari Claude Desktop (termasuk ikon di baki sistem pada Windows) lalu meluncurkannya kembali. Meminta agen untuk menjalankan [`reload_credentials`](/mcp/reload_credentials) akan melaporkan status kredensial per provider dan merupakan cara tercepat untuk memastikan apa yang dapat dan tidak dapat dilihat oleh server.

Claude Desktop menyatakan kemampuan elisitasi MCP, sehingga mode server default (`safe`) akan meminta persetujuan Anda untuk setiap mutasi - lihat [Mode server](/command-line-usage/mcp#server-modes).

## Menginstal dari bundle yang diunduh {#install-from-a-downloaded-bundle}

Gunakan opsi instalasi ini untuk mengunci versi tertentu, menginstal tanpa akses ke Connector Directory, atau memverifikasi checksum sendiri sebelum menginstal.

MCP Bundle (`.mcpb`) siap pakai adalah paket yang dapat diinstal dengan satu klik dan berisi biner `stackql` yang ditandatangani serta konfigurasi server. Tidak diperlukan instalasi StackQL terpisah.

Unduh bundle untuk platform Anda dari rilis terbaru:

| Platform | Bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (native) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Lalu di Claude Desktop: **Settings -> Extensions -> Install Extension** dan pilih file yang telah diunduh. Biner yang tersemat ditandatangani dengan Authenticode (Windows) dan dinotarisasi oleh Apple (macOS); setiap bundle disertai checksum `.sha256` di [halaman rilis](https://github.com/stackql/stackql/releases/latest). Untuk memverifikasi sebelum menginstal:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Server ini juga tercantum di [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) sebagai `io.github.stackql/stackql-mcp`, yang mengunci checksum tersebut.

## Konfigurasi manual {#manual-configuration}

Jika StackQL sudah terinstal, Anda dapat mendaftarkan server MCP StackQL langsung di file konfigurasi Claude Desktop.

### Prasyarat {#prerequisites}

Sebelum menyiapkan StackQL dengan Claude Desktop, pastikan Anda memiliki:

1. **StackQL terinstal** - Unduh dan instal StackQL dari [stackql.io](/installing-stackql) atau gunakan pengelola paket
2. **Claude Desktop** - Unduh dari [situs web Anthropic](https://claude.ai/desktop)
3. **Kredensial penyedia cloud** - Kunci API atau kredensial untuk penyedia cloud yang ingin Anda gunakan

Verifikasi bahwa StackQL ada di PATH sistem Anda dengan menjalankan:

```bash
stackql --version
```

### Menyiapkan server MCP {#setting-up-the-mcp-server}

Agar Claude Desktop dapat berkomunikasi dengan StackQL, Anda perlu mengonfigurasi server MCP di file konfigurasi Claude Desktop Anda.

#### Windows {#windows}

Edit file konfigurasi yang terletak di:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Edit file konfigurasi yang terletak di:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Edit file konfigurasi yang terletak di:
```
~/.config/Claude/claude_desktop_config.json
```

### Contoh konfigurasi {#configuration-example}

Tambahkan konfigurasi berikut ke file `claude_desktop_config.json` Anda:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Anda hanya perlu menyertakan variabel lingkungan untuk penyedia cloud yang ingin Anda gunakan. Hapus kredensial provider yang tidak digunakan dari konfigurasi.
:::

:::warning
Jangan pernah meng-commit file `claude_desktop_config.json` yang berisi kredensial asli ke kontrol versi. Terapkan praktik pengelolaan kredensial yang aman.
:::

Claude Desktop menyatakan kemampuan elisitasi MCP, sehingga mode server default (`safe`) akan meminta persetujuan Anda untuk setiap mutasi. Beralih ke `mode: read_only` untuk akses yang hanya mencakup inventaris atau ke `mode: full_access` untuk melewati permintaan persetujuan - lihat [Mode server](/command-line-usage/mcp#server-modes).

### Menggunakan file kredensial (`--env.file`) {#using-a-credentials-file---envfile}

Alternatif untuk blok `env` - dan di Windows, alternatif yang direkomendasikan - adalah menyimpan kredensial dalam file bergaya dotenv dan meneruskannya dengan flag [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

Dengan file kredensial minimal:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Mengapa cara ini lebih baik daripada blok `env`:

- Perubahan pada blok `env` mengharuskan Claude Desktop dimulai ulang sepenuhnya (termasuk keluar dari ikon di baki sistem) sebelum subproses MCP melihatnya, dan di Windows `setx` menulis ke registri - perubahan itu tidak pernah sampai ke proses Desktop yang sedang berjalan.
- File `--env.file` dapat dibuat atau dirotasi kapan saja - file tersebut bahkan tidak perlu ada saat server dimulai - dan meminta agen untuk menjalankan [`reload_credentials`](/mcp/reload_credentials) akan memuat perubahan tersebut tanpa perlu memulai ulang apa pun.

:::note

`--env.file` dan `reload_credentials` tersedia di rilis StackQL setelah `v0.10.542`.

:::

### Kredensial khusus provider {#provider-specific-credentials}

Penyedia cloud yang berbeda memerlukan metode autentikasi yang berbeda, lihat dokumentasi provider ([https://stackql.io/providers](/providers)) untuk variabel lingkungan yang diperlukan oleh provider yang Anda inginkan.


### Memulai ulang Claude Desktop {#restarting-claude-desktop}

Setelah memperbarui file konfigurasi, mulai ulang Claude Desktop untuk memuat konfigurasi server MCP yang baru. Server MCP StackQL akan dimulai secara otomatis saat Anda memulai percakapan baru.

## Apa yang dapat Anda lakukan {#what-you-can-do}

Setelah dikonfigurasi, Anda dapat menggunakan bahasa alami di Claude Desktop untuk:

### Mengkueri sumber daya cloud {#query-cloud-resources}

Minta Claude untuk mengambil informasi tentang infrastruktur cloud Anda:

- "Tampilkan semua instance compute GCP saya di region us-central1"
- "Cantumkan semua bucket S3 di akun AWS saya"
- "Mesin virtual Azure apa saja yang berjalan di langganan saya?"
- "Tampilkan semua pod Kubernetes di namespace default"

### Menyediakan sumber daya {#provision-resources}

Gunakan perintah percakapan untuk membuat dan men-deploy sumber daya cloud:

- "Buat bucket S3 bernama 'my-data-bucket' di us-west-2"
- "Lakukan deployment bucket Cloud Storage GCP dengan pembuatan versi diaktifkan"
- "Luncurkan instance EC2 kecil di us-east-1"
- "Buat grup sumber daya Azure di westus2"

### Menganalisis dan mengaudit {#analyze-and-audit}

Lakukan audit keamanan dan pemeriksaan kepatuhan:

- "Tampilkan semua bucket S3 yang dapat diakses publik"
- "Cantumkan pengguna IAM yang belum login selama 90 hari"
- "Temukan semua instance GCP yang tidak memiliki label yang sesuai"
- "Identifikasi VM Azure yang pencadangannya belum diaktifkan"

### Operasi lintas cloud {#cross-cloud-operations}

Kueri dan bandingkan sumber daya di beberapa penyedia cloud:

- "Tampilkan semua instance compute di AWS, GCP, dan Azure"
- "Bandingkan biaya penyimpanan antara AWS S3 dan GCP Cloud Storage"
- "Cantumkan semua database di semua penyedia cloud"

### Infrastruktur sebagai kode {#infrastructure-as-code}

Hasilkan dan jalankan kueri deployment infrastruktur:

- "Buat stack sumber daya yang mencakup VPC, subnet, dan instance EC2"
- "Lakukan deployment cluster GKE lengkap dengan node pool"
- "Siapkan deployment multi-region di AWS"

### Mengubah sumber daya {#modify-resources}

Perbarui sumber daya cloud yang sudah ada:

- "Tambahkan tag ke semua instance EC2 yang belum diberi tag"
- "Aktifkan enkripsi untuk bucket S3 'my-bucket'"
- "Perbarui jenis mesin instance GCP 'web-server-1'"
- "Ubah ACL pada bucket Cloud Storage saya menjadi privat"

### Menghapus sumber daya {#delete-resources}

Hapus sumber daya cloud yang tidak lagi diperlukan:

- "Hapus bucket S3 'temp-bucket'"
- "Hapus semua instance EC2 yang dihentikan"
- "Bersihkan disk GCP yang tidak digunakan"

## Contoh percakapan {#example-conversation}

Berikut contoh cara Anda dapat berinteraksi dengan Claude Desktop menggunakan StackQL:

**Anda:** "Dapatkah Anda menampilkan semua instance compute GCP saya?"

**Claude:** *Menjalankan kueri StackQL dan mengembalikan hasil dalam tabel berformat yang menampilkan nama instance, zona, jenis mesin, dan status*

**Anda:** "Buat bucket Cloud Storage baru bernama 'analytics-data-2025' di region us-central1 dengan akses level bucket yang seragam"

**Claude:** *Menjalankan pernyataan INSERT StackQL untuk membuat bucket dan mengonfirmasi pembuatannya*

**Anda:** "Sekarang cantumkan semua bucket Cloud Storage saya"

**Claude:** *Menjalankan kueri SELECT dan menampilkan semua bucket, termasuk bucket yang baru dibuat*

## Tips dan praktik terbaik {#tips-and-best-practices}

1. **Buat permintaan yang spesifik**: makin spesifik permintaan Anda, makin baik hasil yang Anda dapatkan. Sertakan nama sumber daya, region, dan atribut tertentu jika memungkinkan.

2. **Gunakan bahasa alami**: Anda tidak perlu mengetahui sintaksis SQL - Claude menerjemahkan permintaan bahasa alami Anda menjadi kueri StackQL.

3. **Tinjau sebelum menjalankan**: untuk operasi destruktif (DELETE, UPDATE), Claude biasanya akan menunjukkan apa yang akan dijalankan sebelum melanjutkan.

4. **Operasi multilangkah**: Anda dapat merangkai beberapa operasi dalam satu percakapan. Claude mempertahankan konteks sepanjang sesi.

5. **Penanganan error**: jika kueri gagal, Claude dapat membantu memecahkan masalah dan menyarankan perbaikan berdasarkan pesan error.

6. **Jelajahi kemampuan**: tanyakan kepada Claude "Apa yang dapat saya lakukan dengan StackQL?" untuk mendapatkan saran yang spesifik untuk provider yang telah Anda konfigurasi.

## Pemecahan masalah {#troubleshooting}

### Server MCP tidak dapat dimulai {#mcp-server-not-starting}

Jika Claude Desktop tidak dapat terhubung ke server MCP StackQL:

1. Verifikasi bahwa StackQL ada di PATH Anda
2. Periksa apakah file `claude_desktop_config.json` Anda berisi JSON yang valid
3. Pastikan jalur file di konfigurasi sudah benar untuk sistem operasi Anda
4. Tinjau log Claude Desktop untuk menemukan pesan error

### Error autentikasi {#authentication-errors}

Jika Anda menerima error autentikasi:

1. Verifikasi bahwa kredensial penyedia cloud Anda sudah benar
2. Pastikan file kredensial ada di jalur yang ditentukan
3. Periksa apakah variabel lingkungan diformat dengan benar di konfigurasi - meminta agen untuk menjalankan [`reload_credentials`](/mcp/reload_credentials) akan melaporkan status kredensial per provider dan merupakan cara tercepat untuk melihat variabel mana yang tidak ada
4. Pastikan kredensial Anda memiliki izin yang diperlukan

### Error izin {#permission-errors}

Jika operasi gagal karena izin yang tidak memadai:

1. Verifikasi bahwa akun layanan atau pengguna IAM Anda memiliki izin yang diperlukan
2. Periksa kebijakan IAM khusus provider
3. Pastikan layanan API diaktifkan di proyek cloud Anda

## Pelajari lebih lanjut {#learn-more}

- [Referensi perintah MCP StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Spesifikasi bahasa](/language-spec/select)
- [Dokumentasi Model Context Protocol](https://modelcontextprotocol.io)

## Pertimbangan keamanan {#security-considerations}

:::caution
- Simpan kredensial dengan aman dan jangan pernah meng-commit-nya ke kontrol versi
- Terapkan prinsip akses dengan hak istimewa paling rendah untuk akun layanan
- Rotasikan kredensial dan kunci API secara berkala
- Pantau log server MCP untuk mendeteksi aktivitas yang tidak biasa
- Pertimbangkan untuk menggunakan alat pengelolaan kredensial dari penyedia cloud (misalnya, Secret Manager, Parameter Store)
:::
