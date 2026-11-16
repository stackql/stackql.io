---
title: Menggunakan provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Menggunakan provider di StackQL: mengunduhnya dari registri, melakukan autentikasi dengan variabel lingkungan atau kredensial, dan mengkueri layanan serta sumber dayanya."
image: "/img/stackql-featured-image.png"
---

Lihat juga:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Kueri StackQL dapat dijalankan terhadap sejumlah penyedia cloud yang berbeda, termasuk Google Cloud Platform, Amazon Web Services, Microsoft Azure, Digital Ocean, dan lainnya. Spesifikasi provider StackQL diunduh dari [__StackQL Provider Registry__](/providers); hal ini dapat dilakukan menggunakan perintah StackQL [__`REGISTRY PULL`__](/language-spec/registry), atau menggunakan utilitas baris perintah [__`stackql registry`__](/command-line-usage/registry).


## Menginstal provider {#installing-a-provider}

Untuk melihat daftar provider yang tersedia, jalankan perintah berikut:  

```sql
REGISTRY LIST;
```

Untuk menginstal provider, jalankan perintah berikut:

```sql
REGISTRY PULL okta;
```

Untuk melihat provider yang terinstal di sistem saat ini, jalankan perintah berikut:

```sql
SHOW PROVIDERS;
```

## Melakukan autentikasi ke provider {#authenticating-to-a-provider}

Melakukan autentikasi ke penyedia cloud sering kali merupakan langkah pertama dalam rutinitas StackQL. Perintah meta seperti [`SHOW`](/language-spec/show) dan [`DESCRIBE`](/language-spec/describe) dapat dijalankan terhadap provider tanpa autentikasi, tetapi perintah [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert), dan [`EXEC`](/language-spec/exec) umumnya memerlukan sesi yang terautentikasi.  

Autentikasi dilakukan menggunakan variabel lingkungan yang ditetapkan di sistem (atau diambil sebagai rahasia di sistem CI); setiap provider memiliki variabel default unik sendiri yang digunakan untuk autentikasi, karena itu lihat dokumentasi provider Anda melalui [dokumentasi StackQL Provider Registry](/providers).  Variabel tersebut juga dapat dimuat dari file bergaya dotenv saat startup menggunakan flag global [`--env.file`](/command-line-usage/global-flags).  

:::tip

Anda dapat melakukan autentikasi ke beberapa provider dalam shell interaktif atau rutinitas batch noninteraktif yang sama dengan menetapkan variabel autentikasi setiap provider, misalnya:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Hal ini sangat berguna saat Anda melakukan join data di antara beberapa provider dalam satu kueri.

:::
