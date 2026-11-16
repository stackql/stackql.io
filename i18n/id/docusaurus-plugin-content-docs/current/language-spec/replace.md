---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "REPLACE di StackQL: mengganti setiap field instance sumber daya, setara dengan HTTP PUT, berbeda dengan pembaruan parsial yang dilakukan oleh UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Memperbarui semua field dalam sumber daya (sinonim dengan operasi `put`).

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Sintaksis {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Contoh {#examples}

### Pernyataan `REPLACE` dasar {#basic-replace-statement}
Jalankan pernyataan `REPLACE` untuk mengganti semua topik pada repositori GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Pernyataan `REPLACE` (memblokir) {#replace-statement-blocking}
Jalankan pernyataan `REPLACE` untuk mengganti aturan firewall Compute Engine. Ini adalah pemanggilan yang memblokir (sinkron).

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Pernyataan `REPLACE` dengan fungsi JSON {#replace-statement-with-json-functions}
Jalankan pernyataan `REPLACE` yang nilainya disusun dengan fungsi JSON. Fungsi yang tidak membaca kolom apa pun dievaluasi sebelum permintaan dikirim. Nilai `SET` tidak dapat membaca baris yang sedang diganti, jadi untuk mengubah sebagian nilai yang ada, baca nilai tersebut dengan `SELECT` terlebih dahulu, lalu tetapkan nilai yang telah diubah.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
