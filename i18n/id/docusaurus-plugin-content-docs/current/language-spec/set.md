---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variabel sesi
  - set
  - sesi provider
  - rahasia
description: "SET di StackQL: menetapkan variabel sesi bertipe serta mengonfigurasi alias provider, field kredensial, dan rahasia untuk sesi saat ini dengan target berawalan tanda dolar."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Menetapkan variabel sesi, atau mengonfigurasi pengaturan runtime untuk sesi saat ini.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` UNSET `]](/language-spec/unset) [[ Variabel sesi ]](/getting-started/session-variables) [[ Sesi provider ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Sintaksis {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Pengidentifikasi biasa (`region`), nama yang diapit tanda kutip ganda (`"providers.saved"`), atau jalur konfigurasi berawalan tanda dolar yang diapit tanda kutip ganda (`"$.auth.aws.password"`).

__*literal*__  
String, angka, boolean, atau `null`.  String diapit tanda kutip tunggal.  Array dan objek bukan nilai skalar; simpan dokumen JSON sebagai string.

__*DEFAULT*__  
Mengatur ulang target, setara dengan [`UNSET`](/language-spec/unset).  `null` yang eksplisit adalah nilai yang disimpan, bukan pengaturan ulang.

__*reference*__  
Referensi `"$name"` yang diapit tanda kutip ganda ke variabel yang sudah ada, misalnya `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Opsional, dan merupakan satu-satunya cakupan yang dimiliki StackQL.  `SET SESSION name = ...` dan `SET name = ...` adalah pernyataan yang sama: setiap penetapan berlaku untuk sesi saat ini dan tidak untuk yang lain.  Kata kunci ini ada agar skrip terbaca sama seperti pada database yang membedakan `SET SESSION` dan `SET GLOBAL`.  `SET GLOBAL` dapat diurai, tetapi ditolak dengan `configuration scope "global" is not supported; use SESSION`, karena tidak ada status yang dibagikan antarsesi.

Penetapan dalam satu daftar `SET` diterapkan secara berurutan dan bukan merupakan transaksi: jika penetapan kedua dalam daftar gagal, penetapan pertama tetap berlaku.

## Target {#targets}

| Target | Efek |
|--|--|
| `name` atau `"name"` | Menyimpan variabel sesi skalar, yang didereferensi sebagai `$name` dalam pernyataan berikutnya.  Titik saja tidak memilih konfigurasi: `"providers.saved"` adalah variabel bernama `providers.saved`.  Ejaan lama `"variables.name"` adalah alias untuk `name`. |
| `"$.auth.<provider>.<field>"` | Menetapkan satu field dari konteks autentikasi provider untuk sesi ini.  Field yang tersedia adalah kunci objek [`--auth`](/command-line-usage/global-flags) untuk provider tersebut, misalnya `credentialsenvvar`, `password`, atau `token_url`. |
| `"$.providers.<alias>"` | Mengikat alias provider dengan kredensial, label tenancy, nilai default parameter, dan penyetelan miliknya sendiri.  Nilainya berupa dokumen JSON dalam string; lihat [Sesi provider](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Menyimpan rahasia di penyimpanan rahasia sesi.  Nilainya berupa string literal, biasanya dokumen JSON `auth`, yang dirujuk dari alias provider sebagai `auth_secret`. |
| `"$.secret_store"` | Memilih penyimpanan rahasia OS yang persisten, `{"backend":"os","namespace":"..."}`.  Memerlukan flag operator `--allow-persistent-secrets`. |

Setiap bentuk hanya memengaruhi sesi saat ini.  Koneksi independen ke server [`srv`](/command-line-usage/srv) tidak melihat penetapan satu sama lain, dan setiap permintaan MCP merupakan sesi tersendiri, jadi lakukan konfigurasi dan kueri dalam satu batch pernyataan saat menggunakan server MCP.

* * *

## Contoh {#examples}

### Menetapkan variabel sesi {#set-session-variables}

Setiap pernyataan menyimpan satu nilai skalar dengan nama yang diberikan, dan nilai tersebut mempertahankan tipenya: `region` adalah string, `minimum_count` bilangan bulat, dan `enabled` boolean.  `null` disimpan sebagai nilai, sehingga `$optional_value` kemudian dievaluasi menjadi `NULL` SQL, bukan gagal karena tidak terdefinisi.  Pernyataan terakhir menetapkan dua variabel dalam satu daftar.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Menyatakan cakupan sesi secara eksplisit {#make-the-session-scope-explicit}

Dua pernyataan pertama identik: `SESSION` menyebut satu-satunya cakupan yang ada, dan kedua penetapan berlaku hingga variabel dihapus atau sesi berakhir.  Gunakan kata kunci ini jika skrip perlu terbaca sama seperti pada database yang membedakan pengaturan sesi dari pengaturan global.  Pernyataan yang dijadikan komentar ditampilkan sebagai pembanding: pernyataan tersebut tidak menetapkan apa pun dan gagal dengan `configuration scope "global" is not supported; use SESSION`, karena tidak ada status yang dibagikan antarsesi.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Menggunakan variabel dalam kueri {#use-a-variable-in-a-query}

Gunakan `$name`, tanpa tanda kutip, untuk mendereferensi variabel di posisi ekspresi mana pun.  Nilai mempertahankan tipenya: string di-escape sebagai literal SQL, sedangkan angka, boolean, dan `null` diteruskan apa adanya.  Variabel dalam klausa `WHERE` dievaluasi sebelum permintaan direncanakan, sehingga variabel tersebut diteruskan ke provider seperti literal.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Mengatur ulang variabel {#reset-a-variable}

`DEFAULT` menghapus variabel, persis seperti [`UNSET`](/language-spec/unset), sehingga `$region` setelahnya gagal dengan `variable "$region" is not defined` hingga variabel itu ditetapkan lagi; kueri yang dijadikan komentar menunjukkan kegagalan tersebut.  Hal ini berbeda dengan menetapkan `null`, yang membiarkan variabel tetap ada dengan nilai `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Menyalin nilai dari variabel lain {#copy-a-value-from-another-variable}

Sisi kanan dapat berupa referensi yang diapit tanda kutip ganda ke variabel yang sudah ada, yang menyalin nilai yang dimiliki variabel tersebut pada saat itu.  Di sini, pengikatan provider pertama-tama disimpan sebagai variabel biasa bernama `providers.saved` (titik di dalamnya tidak menjadikannya konfigurasi), lalu diaktifkan dengan menyalinnya ke jalur `$.providers.production`.  Menghapus salinan tersimpan setelahnya tidak memengaruhi pengikatan yang aktif.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Mengganti field kredensial untuk sesi ini {#override-a-credential-field-for-this-session}

Konteks autentikasi provider adalah objek yang diberikan untuk provider tersebut melalui [`--auth`](/command-line-usage/global-flags) saat proses dimulai.  Jalur di bawah `$.auth` mengganti satu field dari objek tersebut selama sisa sesi dan membiarkan field lainnya tetap seperti semula.  Di sini, provider GitHub diarahkan ke variabel lingkungan lain yang berisi token hanya baca; variabel tersebut dibaca dari lingkungan proses StackQL, dan kueri berikutnya terhadap `github.*` menggunakannya hingga field tersebut dihapus atau sesi berakhir.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Mengikat alias provider {#bind-a-provider-alias}

Alias provider adalah instance kedua dari suatu provider yang dikonfigurasi secara independen, yang digunakan sebagai pengganti nama provider dalam kueri.  Nilainya berupa dokumen JSON dalam string: `provider` menyebutkan provider yang mendasarinya, `tenancy` memberi label pada akun, `auth` adalah blok kredensial dalam format alias (untuk AWS, `access_key_id_env_var` dan `secret_access_key_env_var`, bukan kunci `keyIDenvvar` dan `credentialsenvvar` dari `--auth`), dan `params` menyediakan nilai default untuk parameter permintaan seperti `region`.  Kueri terhadap `production.s3.buckets` kemudian dijalankan dengan kredensial tersebut, sedangkan `aws.s3.buckets` tetap menggunakan konfigurasi awal.  Daftar lengkap kunci terdapat di [Sesi provider](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
