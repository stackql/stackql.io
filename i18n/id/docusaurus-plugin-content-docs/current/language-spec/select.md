---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "SELECT di StackQL: mengkueri sumber daya cloud dan SaaS dengan SQL, dengan filter WHERE yang dipetakan ke parameter API, join, pengelompokan, pengurutan, dan akses field JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Mengembalikan satu atau beberapa instance dari suatu sumber daya.  

Lihat juga:
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Fungsi jendela `]](/language-spec/windowing_functions)

* * * 

## Sintaksis {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

`JOIN` tanpa klausa `ON` diperlakukan sebagai `CROSS JOIN`, sehingga kondisi join dapat diberikan di klausa `WHERE` sebagai gantinya.  Rilis StackQL sebelum `v0.12.732` gagal merencanakan `JOIN` tanpa kondisi.

* * *

## Contoh {#examples}

### Pernyataan `SELECT` dasar {#basic-select-statement}
Jalankan pernyataan `SELECT` dasar dalam sesi yang telah diautentikasi.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Mengembalikan field tertentu menggunakan pernyataan `SELECT` {#returning-specified-fields-using-a-select-statement}
Jalankan pernyataan `SELECT` dengan daftar kolom untuk mengembalikan field tertentu dari instance Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan kumpulan hasil yang diurutkan berdasarkan kolom secara menaik menggunakan klausa `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Jalankan pernyataan `SELECT` untuk mengembalikan field dari sumber daya bucket Cloud Storage, dengan mengurutkan hasil secara menaik (terlama lebih dulu). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Mengembalikan kumpulan hasil yang diurutkan berdasarkan kolom secara menurun menggunakan klausa `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Jalankan pernyataan `SELECT` untuk mengembalikan field dari sumber daya bucket Cloud Storage, dengan mengurutkan hasil secara menurun (terbaru lebih dulu).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Mendapatkan hasil teratas dari kolom menggunakan klausa `ORDER BY` dan `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Jalankan pernyataan `SELECT` untuk mengembalikan bucket Cloud Storage yang paling baru dibuat.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Menggunakan fungsi `COUNT` dengan klausa `GROUP BY` dan `HAVING` untuk menghitung sumber daya berdasarkan pengelompokan dan filter {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Jalankan pernyataan `SELECT` dengan fungsi `COUNT`, menggunakan klausa `GROUP BY` dan `HAVING` untuk mengembalikan jumlah bucket di setiap lokasi selain Asia.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Untuk informasi selengkapnya tentang fungsi `COUNT` dan fungsi agregat lain yang didukung oleh StackQL, lihat [Fungsi agregat](/language-spec/functions/aggregate/count).

### Mendapatkan daftar VM On Demand dan antarmuka jaringan terkait dari Azure VM Scale Set {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Jalankan pernyataan `SELECT` menggunakan fungsi [`SPLIT_PART`](/language-spec/functions/string/split_part) dan [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) untuk mendapatkan daftar VM On Demand (OD) beserta NIC masing-masing. Perhatikan bahwa tanpa kualifikasi, `subscription_id` dan `resource_group_name` diterapkan ke ketiga sumber daya tersebut.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

### Menggunakan ekspresi tabel umum (CTE) dengan `WITH` {#using-common-table-expressions-ctes-with-with}

CTE memungkinkan Anda mendefinisikan kumpulan hasil sementara yang bernama dan dapat dirujuk dalam kueri utama.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Untuk informasi selengkapnya tentang CTE, lihat [WITH (CTE)](/language-spec/with).

Untuk informasi selengkapnya tentang fungsi jendela, lihat [Fungsi jendela](/language-spec/windowing_functions).
