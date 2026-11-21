---
title: Manajemen akses
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - manajemen akses
  - autentikasi
  - otorisasi
  - jwt
  - mtls
  - token bearer
description: "Manajemen akses pemanggil dengan aktivasi eksplisit di StackQL: autentikasi dengan JWT, token bearer, TLS mutual, atau identitas OS lokal, pemetaan ke prinsipal dan grup direktori, serta otorisasi pernyataan dengan aturan kebijakan."
image: "/img/stackql-featured-image.png"
---

Manajemen akses pemanggil mengautentikasi pemanggil sesi StackQL dan mengotorisasi setiap pernyataan berdasarkan kebijakan.  Fitur ini tidak bergantung pada kredensial provider, yang tetap diberikan melalui flag `--auth`, variabel lingkungan, atau [sesi provider](/getting-started/provider-sessions).  Fitur ini dinonaktifkan secara default, dan deployment yang tidak mengaktifkannya berperilaku seperti sebelumnya.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[ Flag global ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Mengaktifkan manajemen akses {#enabling-access-management}

Manajemen akses diaktifkan dengan memulai StackQL menggunakan flag global `--access.config` yang menunjuk ke file konfigurasi JSON.  Contoh ini memulai shell interaktif dalam mode terlindungi; perintah `exec` dan `mcp` diaktifkan dengan cara yang sama.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

Flag `--access.config` menerima objek pembungkus JSON dengan satu kunci `path`; jalur file biasa juga diterima.  Konfigurasi dimuat satu kali saat startup, jadi rotasikan kunci atau kebijakan dengan memulai ulang StackQL secara terkendali.

Selama manajemen akses diaktifkan, hanya `exec`, `shell`, `mcp`, dan `version` yang berjalan; perintah lain, termasuk `registry`, menolak dijalankan alih-alih melewati jalur terlindungi.  Perintah `srv` dan tipe server MCP `reverse_proxy` juga menolak dijalankan, karena dependensi protokol komunikasi PostgreSQL belum menyediakan bukti autentikasi per koneksi.  Pernyataan SQL `AUTH` dan `AUTH REVOKE` ditolak dalam mode terlindungi; sebagai gantinya, sediakan kredensial provider melalui konfigurasi host.  Perintah `exec` terlindungi yang gagal keluar dengan status `1` setelah menampilkan error.

## Mode autentikasi {#authentication-modes}

File konfigurasi memilih satu nilai `type`: `jwt` (default jika tidak ditentukan), `bearer`, `mtls`, atau `local_os`.  Konfigurasi berupa JSON ketat: field yang tidak dikenal, dan field milik mode lain, ditolak.  Semua mode menggunakan `rules` yang sama dan [direktori prinsipal](#principal-directory) yang sama.

### JWT {#jwt}

Pemanggil memberikan token yang ditandatangani, yang diverifikasi secara offline terhadap kunci yang dikunci di konfigurasi.  Konfigurasi minimal menyebutkan penerbit dan audiens yang harus dimuat token, mencantumkan satu kunci publik, dan mengizinkan anggota grup `analysts` menjalankan pernyataan baca (anggota grup `readers` dapat melakukannya tanpa aturan, lihat [Aturan kebijakan](#policy-rules)):

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

Penerbit, audiens, dan kunci publik dikunci di konfigurasi; tidak ada penemuan OIDC.  Algoritma yang diterima adalah `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384`, dan `ES512`, dan kunci RSA harus berukuran minimal 2048 bit.  Setiap kunci terikat pada algoritma yang dikonfigurasi untuknya.  Token dengan ID kunci yang tidak dikenal, tanda tangan yang tidak valid, waktu kedaluwarsa yang tidak ada atau sudah lewat, `nbf` atau `iat` di masa depan, atau penerbit atau audiens yang salah akan ditolak.  Klaim grup dan URL yang dibawa token tidak digunakan untuk kepercayaan atau keanggotaan direktori.

### Token bearer statis {#static-bearer-token}

Pemanggil memberikan token opak, dan konfigurasi mencantumkan hash SHA-256 dari setiap token yang diterima beserta subjek yang diidentifikasinya.  Mode ini cocok untuk sekumpulan kecil identitas layanan yang tetap, ketika menjalankan penerbit token tidak sepadan:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Hanya hash SHA-256 dari setiap token yang dikonfigurasi, dan hash harus unik.  Mengubah hash yang dikonfigurasi memerlukan mulai ulang.

### TLS mutual {#mutual-tls}

Pemanggil diidentifikasi melalui sertifikat klien yang diberikannya pada transport MCP HTTPS.  Konfigurasi memuat CA yang harus menjadi penerbit sertifikat tersebut dan menentukan nama alternatif subjek mana yang memuat identitas:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Rantai sertifikat klien diverifikasi terhadap `client_ca_pem` dan subjek identitas diambil dari tepat satu SAN dengan jenis yang dikonfigurasi, `uri_san` atau `dns_san`.  Tidak ada fallback ke Common Name.  TLS mutual hanya tersedia melalui transport MCP HTTPS.

### Identitas OS lokal {#local-os-identity}

Tidak ada kredensial yang dipertukarkan: subjeknya adalah identitas sistem operasi yang menjalankan proses StackQL.  Konfigurasi hanya memerlukan label `issuer` dan aturan:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Subjek identitas adalah UID Unix atau SID Windows efektif dari proses StackQL, termasuk UID container numerik yang tidak memiliki entri passwd.  Tidak ada kredensial yang diberikan dan variabel lingkungan `STACKQL_CALLER_TOKEN` diabaikan.  Mode ini hanya untuk penggunaan lokal: mode ini ditolak untuk pemanggil HTTP.  Keanggotaan grup sistem operasi tidak memberikan akses; hanya keanggotaan direktori yang memberikannya.

### Batas waktu sesi {#session-deadline}

Secara default, kueri tidak memiliki batas waktu yang berasal dari autentikasi.  Kredensial diverifikasi dan keanggotaan direktori diperbarui untuk setiap pernyataan baru, sehingga kredensial yang kedaluwarsa tidak dapat memulai pekerjaan baru, tetapi kedaluwarsa tidak menghentikan pernyataan yang sudah diotorisasi.  Tetapkan `"enforce_session_deadline": true` untuk membatalkan pada `max_session_seconds` (default `300`, maksimum `86400`), yang dipersingkat oleh waktu kedaluwarsa JWT atau sertifikat.  Pengaturan ini tidak memperbarui token.

## Direktori prinsipal {#principal-directory}

Identitas dipetakan ke prinsipal, dan prinsipal ke grup, dalam empat tabel di backend SQLite atau PostgreSQL yang dikonfigurasi:

| Tabel | Isi |
|--|--|
| `stackql_access_principals` | ID prinsipal dan objek JSON berisi atribut string.  Atribut `tenant` memberi namespace pada status IaC pratinjau. |
| `stackql_access_identities` | Pasangan `(issuer, subject)` terverifikasi ke ID prinsipal. |
| `stackql_access_groups` | ID grup. |
| `stackql_access_memberships` | Pengikatan prinsipal ke grup. |

Sediakan tabel, identitas, dan keanggotaan melalui koneksi administrasi database tepercaya sebelum melayani kueri terlindungi:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Gunakan penerbit dan subjek yang sebenarnya dari mode tersebut: untuk identitas OS lokal, nilainya misalnya `('this-host', '1000')`, dan untuk TLS mutual `('client-certificates', 'spiffe://example/alice')`.  Pilih backend secara eksplisit:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

Direktori menghasilkan snapshot yang tidak dapat diubah untuk setiap pernyataan.  Pencabutan dilakukan dengan pernyataan `DELETE FROM stackql_access_memberships` pada koneksi administrasi; pernyataan berikutnya melihat snapshot yang baru.  Identitas yang tidak dikenal, skema yang tidak ada, data yang formatnya salah, dan gangguan direktori semuanya menyebabkan akses ditolak.  SQL terlindungi biasa tidak dapat mengakses tabel direktori.  Batasi operasi tulis keanggotaan pada koneksi administrasi, lindungi file database, dan gunakan hak istimewa database yang terpisah untuk administrasi dan runtime jika backend mendukungnya.

## Aturan kebijakan {#policy-rules}

Setiap aturan memberikan satu `action` pada satu `resource` kepada `subject` berbentuk `group:<group id>`.  Gunakan pengidentifikasi metode dan operasi terkompilasi dari dokumen provider tepercaya; karakter pengganti pada operasi memberikan izin untuk metode mutasi sekaligus operasi baca.

Grup `readers` secara implisit memiliki kemampuan SQL baca aman milik host.  Operasi baca oleh prinsipal di luar grup tersebut memerlukan `host/sql/read`, dan pernyataan lain memerlukan `execute` pada `host/sql/<statement kind>`.  Operasi provider dan penggunaan kredensial selalu memerlukan aturan eksplisit, dan izin pada sebuah koleksi tidak memberikan izin pada metode yang mendasarinya.  Pemeriksaan host tambahan:

| Sumber daya | Tindakan | Cakupan |
|--|--|--|
| `host/sql/read` | `execute` | Pernyataan baca di luar grup pembaca implisit |
| `host/sql/exec` | `execute` | `EXEC` native, termasuk `EXEC` bersarang |
| `host/iac/run`, `host/iac/destroy` | `execute` | Relasi IaC pratinjau, meskipun menggunakan sintaksis `SELECT` |
| `host/function/<name>` | `execute` | Fungsi di luar daftar izin kecil berisi fungsi yang diketahui murni |
| `host/backend` | `read` | Relasi backend tersimpan dan eksternal yang opak |
| `host/credentials` | `reload` | Alat MCP `reload_credentials` |
| `session/variables` | `read` | Mendereferensi [variabel sesi](/getting-started/session-variables) dan rahasia |
| `session/config` | `write` | `SET` dan `UNSET` |

Ekspansi kueri tersimpan diperiksa sebelum eksekusi, dan penggunaan ulang cache rencana dan cache baris dinonaktifkan pada jalur terlindungi.

## Titik masuk {#entry-points}

| Autentikasi | `exec`, `shell`, MCP stdio | MCP HTTPS |
|--|--|--|
| JWT | Variabel lingkungan `STACKQL_CALLER_TOKEN` | Header permintaan `Authorization: Bearer` |
| Bearer statis | Variabel lingkungan `STACKQL_CALLER_TOKEN` | Header permintaan `Authorization: Bearer` |
| OS lokal | UID atau SID efektif proses; tanpa kredensial | Ditolak |
| TLS mutual | Ditolak: tidak ada koneksi TLS terverifikasi | Rantai klien terverifikasi dan satu SAN yang dikonfigurasi |

Berikan kredensial token lokal melalui lingkungan proses, jangan pernah sebagai literal SQL atau argumen baris perintah.  Melalui HTTPS, verifikator pemanggil menggantikan gerbang bearer bersama `auth_token_env_var` yang dijelaskan di [Autentikasi klien HTTP](/command-line-usage/mcp#http-client-authentication); konfigurasi sertifikat dan kunci TLS server wajib ada, dan permintaan tanpa kredensial tidak pernah menggunakan token lingkungan milik server sebagai fallback.

## Memeriksa pemanggil {#inspecting-the-caller}

Dalam sesi yang terautentikasi, fungsi `whoami()` mengembalikan ID prinsipal dan fungsi `groups()` mengembalikan grup direktori sebagai array JSON:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Kedua fungsi memerlukan sesi yang dimulai dengan flag `--access.config`.

## Panduan langkah demi langkah {#walkthroughs}

Repositori StackQL menyertakan [empat panduan langkah demi langkah yang dapat dijalankan](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), satu untuk setiap mode autentikasi, yang digerakkan oleh skrip Python yang menyediakan direktori demonstrasi dan memverifikasi sebuah kueri.  Panduan ini tidak memerlukan akun cloud dan dijalankan oleh suite pengujian proyek.
