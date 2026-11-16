---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "UPDATE di StackQL: mengubah field tertentu dari instance sumber daya, setara dengan HTTP PATCH, dengan target yang ditentukan oleh klausa WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Memperbarui field tertentu dalam sumber daya (sinonim dengan operasi `patch`).

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Sintaksis {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Contoh {#examples}

### Pernyataan `UPDATE` dasar {#basic-update-statement}
Jalankan pernyataan `UPDATE` untuk menetapkan label pada bucket Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Pernyataan `UPDATE` (memblokir) {#update-statement-blocking}
Jalankan pernyataan `UPDATE` untuk mengubah aturan firewall Compute Engine. Ini adalah pemanggilan yang memblokir (sinkron).

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Pernyataan `UPDATE` dengan fungsi JSON {#update-statement-with-json-functions}
Jalankan pernyataan `UPDATE` yang nilainya disusun dengan fungsi JSON. Fungsi yang tidak membaca kolom apa pun dievaluasi sebelum permintaan dikirim. Nilai `SET` tidak dapat membaca baris yang sedang diperbarui, jadi untuk mengubah sebagian nilai yang sudah ada, baca nilai tersebut terlebih dahulu dengan `SELECT`, lalu tetapkan nilai yang telah diubah.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
