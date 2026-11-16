---
title: Sesi provider dan rahasia
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - sesi provider
  - multiakun
  - kredensial
  - rahasia
  - keychain
description: "Sesi provider di StackQL: mengikat alias dengan kredensial, tenancy, dan nilai default parameter sendiri, mengganti field kredensial, dan menyimpan rahasia di memori sesi atau penyimpanan OS."
image: "/img/stackql-featured-image.png"
---

Lihat juga:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Variabel sesi ]](/getting-started/session-variables) [[ Flag global ]](/command-line-usage/global-flags)

Sebuah sesi dapat mengikat provider dengan sebuah alias yang memiliki kredensial, label tenancy, nilai default parameter, dan batas konkurensi permintaan sendiri, sehingga beberapa akun atau tenant dari provider yang sama dapat dikueri dalam satu sesi, dan juga dalam satu pernyataan.  Field kredensial juga dapat diganti untuk sesi tersebut, dan rahasia dapat disimpan di memori sesi atau, dengan aktivasi eksplisit oleh operator, di penyimpanan rahasia sistem operasi.  Semua ini dikonfigurasi dengan [`SET`](/language-spec/set) dan [`UNSET`](/language-spec/unset) dan tersedia mulai StackQL `v1.0`.

Semua yang dijelaskan di sini berlaku dalam cakupan sesi saat ini.  Koneksi independen ke server `stackql srv` tidak berbagi pengikatan atau rahasia, dan setiap permintaan MCP adalah sesi tersendiri; karena itu, lakukan konfigurasi dan kueri dalam satu batch pernyataan saat menggunakan server MCP.

## Alias provider {#provider-aliases}

Pernyataan `SET "$.providers.<alias>"` mengikat sebuah alias ke provider.  Nilainya berupa dokumen JSON yang disimpan dalam string:

| Kunci | Wajib | Deskripsi |
|--|--|--|
| `provider` | Ya | Provider kanonis yang dirujuk oleh alias, misalnya `aws`. |
| `tenancy` | Ya | Label pengelompokan seperti nama akun atau tenant.  StackQL tidak memverifikasinya terhadap provider. |
| `auth` | Salah satu dari `auth` atau `auth_secret` | Dokumen autentikasi dengan bentuk yang sama seperti entri provider di objek [`--auth`](/command-line-usage/global-flags).  Alias tidak meminjam kredensial provider kanonis. |
| `auth_secret` | Salah satu dari `auth` atau `auth_secret` | Nama [rahasia sesi](#session-secrets) yang menyimpan dokumen autentikasi. |
| `params` | Tidak | Nilai default untuk parameter provider yang dideklarasikan, termasuk parameter server yang dideklarasikan.  Predikat dalam kueri mengganti nilai default. |
| `tuning` | Tidak | Batas `max_concurrent` dan `max_per_host` untuk permintaan alias tersebut.  Alias yang berbagi provider dan tenancy juga berbagi kontrol ini dan harus mendeklarasikan penyetelan yang identik. |
<br/>
Contoh berikut mengikat dua akun AWS dengan alias terpisah, masing-masing membaca kuncinya dari variabel lingkungan dengan nama yang berbeda, lalu mengkueri keduanya dalam satu pernyataan:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Referensi variabel lingkungan di-resolve dari lingkungan proses StackQL; tidak ada lingkungan lokal khusus sesi.  Kredensial AWS sementara juga memerlukan field token sesi atau referensi variabel lingkungannya.

Alias berperilaku seperti provider asalnya untuk pernyataan metadata dan pernyataan data: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE`, dan `EXEC` semuanya menerima alias sebagai pengganti nama provider.  Alias tidak berlaku untuk relasi native bawaan atau relasi IaC yang masih dalam pratinjau.  Cabang `UNION ALL` yang menggunakan alias berbeda dijalankan secara berurutan.

Setiap kueri mengambil snapshot terpisah dari kredensial alias.  Mengubah pengikatan memengaruhi kueri berikutnya, bukan kueri yang sedang berjalan.  Pernyataan `UNSET "$.providers.<alias>"` menghapus pengikatan tersebut.

## Mengganti field kredensial {#overriding-credential-fields}

Pernyataan `SET "$.auth.<provider>.<field>"` mengubah satu field dari konteks autentikasi provider untuk sesi tersebut, dengan nama field berupa kunci dari entri `--auth` provider:

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

Seperti halnya alias, perubahan berlaku untuk kueri yang dimulai setelah pernyataan `SET`.  Kueri terhadap provider yang autentikasinya telah dihapus, atau yang aliasnya tidak memiliki `auth` yang dapat digunakan, gagal sebelum permintaan apa pun dikirim.

## Rahasia sesi {#session-secrets}

Secara default, setiap sesi memiliki penyimpanan rahasia yang hanya berada di memori, tanpa perlu konfigurasi.  Pernyataan `SET "$.secrets.<name>"` menyimpan string literal, biasanya dokumen autentikasi JSON, dan alias provider merujuknya dengan `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Merotasi rahasia memengaruhi kueri berikutnya; menghapusnya membuat penggunaan berikutnya gagal.  Sesi baru menerima penyimpanan baru yang kosong, dan mengakhiri sesi akan melepaskannya.  Pernyataan `UNSET "$.secret_store"` membuang pengikatan penyimpanan saat ini dan memulai lagi dengan memori sesi yang kosong.

Penggunaan kredensial biasa, serta pernyataan `SET` yang menyimpan variabel atau konfigurasi, tidak menulis rahasia ke backend SQL.  Memasukkan data sensitif ke variabel atau tabel biasa merupakan tanggung jawab pemanggil.

## Penyimpanan rahasia OS persisten {#persistent-os-secret-stores}

Penyimpanan persisten dinonaktifkan secara default dan hanya dapat diaktifkan oleh operator yang menjalankan StackQL dengan `--allow-persistent-secrets`; SQL tidak dapat memberikannya.  Jika flag tersebut digunakan, sesi memilih penyimpanan OS dengan:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

Penyimpanan tersebut adalah macOS Keychain, Linux Secret Service (`secret-tool` dengan layanan sesi yang berfungsi), atau Windows Credential Manager.  Hanya backend `os` yang diterima: penyimpanan berbasis file datar, jalur file, dan fallback implisit apa pun ke file ditolak meskipun flag tersebut digunakan, dan fasilitas OS yang tidak tersedia akan gagal alih-alih beralih ke file.

Nilai yang ditulis ke penyimpanan OS bertahan lebih lama daripada sesi SQL dan dihapus secara eksplisit dengan `UNSET "$.secrets.<name>"`.  Mengatur ulang pengikatan penyimpanan tidak menghapus nilai yang sudah tersimpan secara persisten.  Akses ke penyimpanan diatur oleh identitas sistem operasi dari proses StackQL; namespace bukan kontrol akses per pengguna, karena itu jangan perlakukan aktivasi eksplisit ini sebagai penyimpanan rahasia multipengguna.

## Nilai rahasia di SQL {#secret-values-in-sql}

Nilai rahasia tidak pernah didereferensi ke dalam SQL kecuali operator menjalankan StackQL dengan `--allow-secret-dereference`.  Dengan flag tersebut, referensi seperti `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"`, dan `"$providers.production.auth.credentials"` di-resolve dalam ekspresi:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Jika StackQL dijalankan tanpa flag tersebut, pernyataan yang sama ditolak sebelum apa pun dievaluasi, seperti yang ditunjukkan oleh salinan yang dijadikan komentar.  Flag ini merupakan pengakuan bahwa nilai yang didereferensi secara eksplisit dapat masuk ke staging SQL, hasil, log, dan artefak kueri yang tersimpan secara persisten.  Autentikasi provider biasa tidak memerlukannya.

## Izin {#permissions}

Jika [manajemen akses](/command-line-usage/access-management) diaktifkan, membaca variabel dan rahasia memerlukan `session/variables:read`, dan `SET` atau `UNSET` memerlukan `session/config:write`.  Alias yang telah di-resolve diotorisasi sebagai provider kanonisnya sebelum eksekusi.  Kepemilikan dan pemberian izin per variabel dan per rahasia tidak diimplementasikan.
