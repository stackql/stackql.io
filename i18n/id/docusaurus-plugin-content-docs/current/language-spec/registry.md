---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "REGISTRY di StackQL: mencantumkan provider dan versi di registri provider serta mengunduh versi terbaru atau versi tertentu ke instalasi Anda."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Mencantumkan provider yang tersedia di registri provider yang dikonfigurasi, atau mengunduh provider ke instalasi lokal.  

Lihat juga:  
[[ Hierarki sumber daya StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> Mulai `v1.0`, dokumen provider dapat diwajibkan memiliki tanda tangan ML-DSA-65 yang valid saat dibaca.  Verifikasi nonaktif secara default dan diaktifkan dengan kunci `verifyConfig` dari flag [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Sintaksis {#syntax}

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## Contoh {#examples}

### Mencantumkan provider yang tersedia {#list-available-providers}
Mengambil daftar versi terbaru dari setiap provider yang tersedia.  
```sql
REGISTRY LIST;
```
### Mencantumkan semua versi untuk provider tertentu {#list-all-versions-for-a-specific-provider}
Mengambil daftar semua versi yang tersedia dari provider tertentu.    
```sql
REGISTRY LIST google;
```

### Menginstal versi terbaru provider {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Menginstal versi tertentu provider {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v24.10.00267;
```
