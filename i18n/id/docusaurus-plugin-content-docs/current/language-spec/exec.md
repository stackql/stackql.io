---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "EXEC di StackQL: memanggil langsung metode sumber daya provider, dengan petunjuk kueri untuk mengembalikan hasil atau menunggu selesai, seperti memulai atau menghentikan instance."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Menjalankan metode sumber daya provider (prosedur bawaan).

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaksis {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Petunjuk kueri {#query-hints}

Petunjuk kueri yang didukung untuk perintah `EXEC` dirangkum di bawah ini:

| Petunjuk | Deskripsi |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Mengembalikan kumpulan hasil, dirancang untuk metode yang mengembalikan data; namun, gunakan `SELECT` jika memungkinkan. |
| `/*+ AWAIT  */` | Membuat operasi bersifat memblokir (sinkron), menyediakan kontrol skrip untuk operasi yang berjalan lama (misalnya operasi mutasi). Catatan: ini tidak didukung oleh semua provider |

* * *

## Contoh {#examples}

### Menghentikan instance Google Compute Engine {#stopping-a-google-compute-engine-instance}
Jalankan pernyataan `EXEC` untuk menghentikan instance Compute Engine yang sedang berjalan dalam sesi yang terautentikasi.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Memulai instance Google Compute Engine (opsi sinkron) {#starting-a-google-compute-engine-instance-synchronous-option}
Jalankan pernyataan `EXEC` untuk memulai instance Compute Engine yang dihentikan dalam sesi yang terautentikasi, dengan petunjuk kueri `/*+ AWAIT  */` untuk membuat operasi bersifat memblokir (sinkron).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Memulai dan menghentikan instance AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Jalankan pernyataan `EXEC` untuk memulai dan menghentikan instance AWS EC2 dalam sesi yang terautentikasi.

```sql
-- Start an EC2 instance
EXEC aws.ec2.instances.start_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
-- Stop an EC2 instance
EXEC aws.ec2.instances.stop_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
```

### Memasang Disk ke instance Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Jalankan pernyataan `EXEC` untuk memasang Disk ke instance Compute Engine dalam sesi yang terautentikasi.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attach_disk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### Menyimulasikan pengusiran Azure Spot VM {#simulate-azure-spot-vm-eviction}
Jalankan pernyataan `EXEC` untuk menyimulasikan pengusiran instance Azure Spot VM.

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
