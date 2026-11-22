---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "SHOW di StackQL: mencantumkan provider, layanan, sumber daya, dan metode, membuat template INSERT, serta memeriksa versi, kontributor, dan dependensi biner yang berjalan."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Digunakan untuk mencantumkan item dalam suatu koleksi, misalnya menampilkan semua layanan yang tersedia di suatu provider, atau menampilkan semua sumber daya yang tersedia di layanan tertentu. Juga menampilkan versi, kontributor, dan dependensi perangkat lunak dari biner StackQL yang sedang berjalan.

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaksis {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE '<pattern>' | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS`, dan `DEPENDENCIES` tidak memerlukan konfigurasi provider atau autentikasi. `DEPENDENCIES` hanya mendukung klausa `LIKE` opsional; `IN`, `FROM`, `WHERE`, dan `LIMIT` tidak didukung.

* * *

## Contoh {#examples}

### Mencantumkan semua provider {#list-all-providers}
Cantumkan provider yang tersedia di lingkungan StackQL lokal: provider yang diunduh dari registri provider, ditambah provider bawaan StackQL.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### Mencantumkan semua layanan di provider {#list-all-services-in-a-cloud-provider}
Cantumkan semua layanan yang tersedia di provider tertentu; gunakan kata kunci EXTENDED opsional untuk mengembalikan informasi tambahan.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### Mencantumkan semua sumber daya di layanan provider {#list-all-resources-within-a-cloud-provider-service}
Cantumkan semua sumber daya yang tersedia di layanan tertentu dari suatu provider.  Kata kunci `EXTENDED` opsional mengembalikan deskripsi sumber daya selain nama sumber daya.
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Menampilkan metode yang tersedia untuk sumber daya di layanan provider {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Cantumkan semua metode yang tersedia untuk sumber daya tertentu.
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Membuat template `INSERT` menggunakan perintah `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Perintah `SHOW` dapat digunakan untuk membuat template pernyataan `INSERT` seperti yang ditunjukkan di sini:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> Field yang dikembalikan dapat dibatasi hanya pada field yang diwajibkan oleh provider; lihat [__Membuat template infrastruktur__](/getting-started/templating) untuk contohnya.

> Template ini kemudian dapat digunakan bersama file data `json` atau `jsonnet` untuk menyediakan nilai saat runtime; untuk informasi selengkapnya, lihat [__Menggunakan variabel__](/getting-started/variables).

### Menampilkan versi StackQL {#show-the-stackql-version}

Mengembalikan versi proses StackQL yang sedang berjalan. Berguna dalam mode shell, exec, dan server protokol komunikasi Postgres saat Anda perlu memastikan build mana yang menjawab kueri. Informasi yang sama juga tersedia melalui alat MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|---------|
| version |
|---------|
| 1.0.888 |
|---------|
```

</details>

Gunakan kata kunci `EXTENDED` opsional untuk juga mengembalikan commit git, stempel waktu build, dan platform build:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Mengembalikan:</strong></em></summary>

```
|---------|---------|----------------------|----------|
| version | commit  |      build_date      | platform |
|---------|---------|----------------------|----------|
| 1.0.888 | f7b4116 | 2026-11-05T00:26:03Z | Windows  |
|---------|---------|----------------------|----------|
```

</details>

> `SHOW VERSION` tidak memerlukan konfigurasi provider, registri, atau autentikasi dan aman dijalankan di sesi mana pun.

### Mencantumkan kontributor StackQL {#list-stackql-contributors}

Mengembalikan papan peringkat kontributor yang tersemat di biner StackQL yang sedang berjalan. Snapshot ini memberikan kredit kepada kontributor GitHub manusia yang telah memberi bintang pada repositori StackQL, diurutkan berdasarkan jumlah kontribusi, dari yang tertinggi. Snapshot ini tetap untuk setiap build dan tidak mengkueri GitHub saat runtime.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Kolom | Deskripsi |
| --- | --- |
| `contributor` | Nama pengguna GitHub |
| `contributions` | Jumlah kontribusi yang tercatat dalam snapshot; disertakan dengan `EXTENDED` |

### Mencantumkan dependensi perangkat lunak biner {#list-the-binarys-software-dependencies}

Mengembalikan inventaris offline modul Go yang disertakan dalam file executable StackQL yang sedang berjalan. Inventaris ini menyediakan software bill of materials (SBOM) yang ringan untuk biner tersebut, yang dapat dikueri oleh pengguna dan agen saat runtime. Inventaris ini mencakup dependensi Go langsung dan transitif; definisi provider yang terinstal, wrapper terpisah, alat CI, modul khusus pengujian, image container, dan paket sistem operasi berada di luar cakupannya.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

Kolom default adalah `name`, `version`, `license`, dan `description`. `EXTENDED` menambahkan kolom lainnya di bawah ini tanpa mengubah baris yang dikembalikan.

| Kolom | Deskripsi |
| --- | --- |
| `name` | Jalur modul Go efektif, menggunakan jalur pengganti jika penggantian modul Go berlaku |
| `version` | Versi modul yang persis, termasuk versi prarilis dan pseudo-version |
| `license` | Pengidentifikasi lisensi yang terdeteksi; `UNKNOWN` jika tidak teridentifikasi atau ambigu |
| `description` | Deskripsi komponen, jika tersedia |
| `source_url` | URL repositori sumber; disertakan dengan `EXTENDED` |
| `license_url` | URL file lisensi yang dikunci ke tag modul atau revisi sumber; disertakan dengan `EXTENDED` |
| `purl` | Package URL yang mengidentifikasi modul dan versi; disertakan dengan `EXTENDED` |
| `checksum` | Checksum modul Go dengan prefiks `h1:`, bukan hash SHA-256 dari file executable atau arsip; disertakan dengan `EXTENDED` |
| `original_name` | Jalur modul yang semula diminta jika penggantian berlaku; disertakan dengan `EXTENDED` |
| `original_version` | Versi yang semula diminta jika penggantian berlaku; disertakan dengan `EXTENDED` |

Baris diurutkan berdasarkan nama modul efektif, lalu versi. Penggantian menggunakan versi, checksum, dan atribusi dari modul pengganti. `original_name` dan `original_version` bernilai `NULL` untuk modul tanpa penggantian. Metadata lain yang tidak tersedia juga bernilai `NULL`, kecuali lisensi yang tidak teridentifikasi, yang bernilai `UNKNOWN`. Penggantian dengan sistem file lokal tidak memiliki versi, URL, atau checksum rekaan.

Gunakan `LIKE` untuk mencari nama modul efektif, nama modul asli, atau deskripsi:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

Pencocokan tidak peka huruf besar/kecil. `%` cocok dengan nol karakter atau lebih dan `_` cocok dengan tepat satu karakter. Kolom lisensi dan versi tidak ikut dicari. Gandakan tanda kutip tunggal di dalam literal string SQL; setelah string SQL didekode, garis miring terbalik bersifat literal dan tidak berfungsi sebagai karakter escape untuk karakter pengganti `LIKE`.

Jika tidak ada modul yang cocok, hasilnya berisi nol baris. Output CSV tetap menyertakan header kolom, klien protokol komunikasi PostgreSQL tetap menerima metadata kolom, dan output JSON bernilai `null`.

Inventaris ini menggabungkan metadata yang disematkan saat build dengan informasi build Go dari file executable. Kueri tidak memerlukan akses jaringan, instalasi Go, checkout kode sumber, atau cache modul. Build tanpa metadata yang dihasilkan tetap melaporkan modul yang ditautkan, dengan atribusi yang tidak ada ditampilkan sebagai `UNKNOWN` atau `NULL`.

Output ini hanya mencakup dependensi Go dari biner. Output ini tidak menyertakan hubungan antardependensi, tidak mengekspor dokumen SPDX atau CycloneDX, dan bukan SBOM deployment yang lengkap ataupun penentuan kepatuhan lisensi.

* * *
