---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "DESCRIBE di StackQL: mencantumkan field suatu sumber daya, atau parameter dan tipe nilai kembalian suatu metode, sebelum Anda mengkueri atau mengubahnya."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Mendeskripsikan sumber daya atau metode tertentu pada sumber daya.

Lihat juga:
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy)

* * *

## Sintaksis {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

`DESC` diterima sebagai alias untuk `DESCRIBE` saat mendeskripsikan sumber daya; `DESCRIBE METHOD` memerlukan kata kunci lengkap.

## Respons {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Kolom | Deskripsi |
| --- | --- |
| `name` | Nama field |
| `type` | Tipe data (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Deskripsi field (jika `EXTENDED` diberikan) |

</TabItem>
<TabItem value="describe-method">

| Kolom | Deskripsi |
| --- | --- |
| `name` | Nama field |
| `type` | Tipe data (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional`, atau `output` |
| `shape` | Subset JSON Schema untuk field objek dan array; kosong untuk skalar. Mencakup `properties`, `items`, `required`, `enum`, `default`, dan `description` bersarang, serta secara opsional mencakup boolean jika disediakan oleh provider (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Deskripsi field (jika `EXTENDED` diberikan) |

</TabItem>
</Tabs>

* * *

## Contoh {#examples}

### Pernyataan `DESCRIBE` dasar {#basic-describe-statement}
Jalankan pernyataan `DESCRIBE` dasar untuk mencantumkan field dalam sumber daya dari sesi yang terautentikasi.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Pernyataan `DESCRIBE` yang diperluas {#extended-describe-statement}
Jalankan pernyataan `DESCRIBE` yang diperluas untuk mencantumkan field dalam sumber daya beserta deskripsinya dari sesi yang terautentikasi.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Alias `DESC` {#desc-alias}
`DESC` adalah bentuk singkat dari `DESCRIBE` pada sumber daya, dengan atau tanpa `EXTENDED`. Pernyataan ini diperlakukan sebagai pernyataan hanya baca di mana pun `DESCRIBE` diperlakukan demikian, termasuk alat `run_select_query` pada server MCP dalam mode `read_only` dan `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Pernyataan `DESCRIBE METHOD` dasar {#basic-describe-method-statement}
Lakukan introspeksi pada satu metode sumber daya. Hasilnya mencakup parameter input dan field respons, dengan struktur bersarang yang dirender dalam kolom `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Pernyataan `DESCRIBE METHOD` yang diperluas {#extended-describe-method-statement}
Jalankan pernyataan `DESCRIBE METHOD` yang diperluas untuk menyertakan deskripsi setiap field beserta bentuknya.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Menemukan payload `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` adalah cara baku untuk mengetahui field apa saja yang diterima oleh `INSERT` (atau metode mutasi apa pun). Kolom `shape` memuat struktur bersarang yang dapat digunakan oleh agen dan manusia untuk menyusun payload dalam satu kueri.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
