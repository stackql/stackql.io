---
title: Hierarki sumber daya
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Hierarki sumber daya StackQL: cara provider, layanan, sumber daya, dan metode memetakan API cloud dan SaaS ke tabel SQL yang dapat dikueri."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Lihat juga:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Objek di StackQL direpresentasikan dalam hierarki berikut, dengan contoh yang menggunakan provider Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Hierarki sumber daya StackQL"/>

### Menggunakan notasi objek StackQL {#using-the-stackql-object-notation}

Hierarki ini dinyatakan dalam notasi objek `<provider>.<service>.<resource>` dalam konteks provider tertentu; contoh notasi objek ini yang digunakan dalam pernyataan [`SELECT`](/language-spec/select) ditunjukkan di sini:  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Jika nama sumber daya atau layanan mengandung titik (`.`), gunakan simbol backtick (__`__) untuk mengapit nama sumber daya, misalnya:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Menampilkan layanan dalam provider {#showing-services-in-a-provider}

Layanan dalam sebuah provider dapat dicantumkan menggunakan perintah [`SHOW SERVICES`](/language-spec/show) seperti yang ditunjukkan di sini:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Menampilkan sumber daya dalam layanan {#showing-resources-within-a-service}

Demikian pula, sumber daya dalam sebuah layanan dapat dicantumkan menggunakan [`SHOW RESOURCES`](/language-spec/show) seperti yang ditunjukkan di sini:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Menampilkan field dalam sumber daya {#showing-fields-within-a-resource}

Field dalam sebuah sumber daya dapat ditampilkan menggunakan pernyataan [`DESCRIBE`](/language-spec/describe) seperti yang ditunjukkan di sini:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Menampilkan metode yang dapat dijalankan untuk sumber daya {#showing-executable-methods-for-a-resource}

Metode yang tersedia untuk sebuah sumber daya dapat ditampilkan menggunakan perintah [`SHOW METHODS`](/language-spec/show) seperti yang ditunjukkan di sini:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
