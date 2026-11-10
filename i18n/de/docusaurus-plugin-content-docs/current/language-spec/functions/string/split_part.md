---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "SPLIT_PART-Funktion in StackQL: einen String am Trennzeichen teilen und den Teil an 1-basierter Position zurückgeben, etwa um Ressourcen-IDs und ARNs zu zerlegen."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Teilt einen Quellstring anhand eines Trennzeichens und gibt den angegebenen Teil zurück (ab eins gezählt).  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*source*__  
Ein String-Literal oder eine String-Spalte, die geteilt werden soll.

__*sep*__  
Ein Zeichen oder mehrere Zeichen, die als Trennzeichen verwendet werden sollen.  

> Wenn `sep` aus mehreren Zeichen besteht, wird jedes Zeichen als Trennzeichen behandelt.  

> Als Trennzeichen werden nur ASCII-Symbole (1 Byte) unterstützt.  

__*part*__  
Das Element (ab eins gezählt) des geteilten Strings, das zurückgegeben werden soll.  Negative Werte zählen rückwärts vom letzten Element aus.  

## Rückgabewert(e) {#return-values}
Gibt einen String zurück.

* * *

## Beispiele {#examples}

### Elemente aus einem durch Schrägstriche getrennten String extrahieren {#extract-elements-from-a-string-seperated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Ergebnisse', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### Das letzte Element aus einer URL extrahieren {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Ergebnisse', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

Weitere Informationen finden Sie unter [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
