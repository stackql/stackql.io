---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variabel sesi
  - unset
  - sesi provider
  - rahasia
description: "UNSET di StackQL: menghapus variabel sesi, atau mengosongkan alias provider, field kredensial, dan rahasia yang dikonfigurasi dengan SET untuk sesi saat ini."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Menghapus variabel sesi, atau mengosongkan konfigurasi runtime yang ditetapkan dengan [`SET`](/language-spec/set) untuk sesi saat ini.  Tersedia mulai StackQL `v1.0`.

Lihat juga:  
[[` SET `]](/language-spec/set) [[ Variabel sesi ]](/getting-started/session-variables) [[ Sesi provider ]](/getting-started/provider-sessions)

* * * 

## Sintaksis {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Target apa pun yang diterima oleh `SET`: nama variabel biasa atau yang diapit tanda kutip ganda, atau jalur konfigurasi yang diapit tanda kutip ganda dan diawali tanda dolar.

__*SESSION*__  
Opsional, dan merupakan satu-satunya cakupan yang ada: `UNSET SESSION name` dan `UNSET name` adalah pernyataan yang sama, karena setiap variabel dan pengaturan konfigurasi termasuk dalam sesi saat ini.  `UNSET GLOBAL` ditolak dengan `configuration scope "global" is not supported; use SESSION`.

`UNSET name` setara dengan `SET name = DEFAULT`.  Target dalam satu daftar dihapus secara berurutan, dan menghapus nama yang tidak ditetapkan bukan merupakan error.  Mendereferensi variabel setelah variabel tersebut dihapus akan gagal sebelum permintaan apa pun ke provider dibuat.

| Target | Efek |
|--|--|
| `name` | Menghapus variabel sesi. |
| `"$.auth.<provider>.<field>"` | Menghapus field tersebut dari konteks autentikasi sesi provider.  Nilai yang berlaku sebelum penggantian tidak dipulihkan. |
| `"$.providers.<alias>"` | Menghapus pengikatan alias provider. |
| `"$.secrets.<name>"` | Menghapus rahasia dari penyimpanan sesi.  Untuk penyimpanan OS yang persisten, tindakan ini menghapus nilai yang tersimpan. |
| `"$.secret_store"` | Membuang pengikatan penyimpanan saat ini dan memulai dengan memori sesi yang kosong.  Nilai yang sudah tersimpan secara persisten di penyimpanan OS tidak dihapus. |

* * *

## Contoh {#examples}

### Menghapus variabel {#remove-a-variable}

Variabel dihapus dari sesi, sehingga kueri yang dikomentari gagal dengan `variable "$region" is not defined` alih-alih berjalan dengan nilai yang usang atau kosong.  Tetapkan nama tersebut lagi untuk menggunakannya kembali.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Menghapus beberapa target {#remove-several-targets}

Satu pernyataan dapat menghapus variabel dan jalur konfigurasi sekaligus, dan target diproses secara berurutan.  Di sini, variabel biasa, variabel dengan nama bertitik, dan penggantian kredensial dihapus dalam satu daftar.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### Menyatakan cakupan sesi secara eksplisit {#make-the-session-scope-explicit}

Kedua pernyataan ini melakukan hal yang sama: `SESSION` menyebut satu-satunya cakupan yang ada.  Bentuk eksplisit cocok untuk skrip yang juga dijalankan di database yang membedakan status sesi dari status global.  Pernyataan yang dikomentari gagal karena tidak ada status bersama.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Menghapus penggantian kredensial {#clear-a-credential-override}

Field yang sebelumnya ditetapkan dengan `SET "$.auth.github.password_var"` dihapus dari konteks autentikasi sesi provider GitHub, dan field lain dalam konteks tersebut tidak berubah.  Nilai yang berlaku sebelum penggantian tidak dipulihkan, jadi tetapkan field tersebut lagi jika provider masih memerlukannya.

```sql
UNSET "$.auth.github.password_var";
```

### Menghapus alias provider beserta rahasianya {#remove-a-provider-alias-and-its-secret}

Alias `production` diikat dengan `SET "$.providers.production"` dan merujuk, melalui `auth_secret`, ke rahasia yang disimpan di `$.secrets.production`.  Menghapus keduanya dalam satu pernyataan akan melepas pengikatan tersebut, sehingga `production.s3.buckets` tidak lagi menjadi relasi yang valid, dan menghapus rahasia dari penyimpanan sesi.  `SESSION` bersifat opsional di sini, seperti di tempat lain.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
