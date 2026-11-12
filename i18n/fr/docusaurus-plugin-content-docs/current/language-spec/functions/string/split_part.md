---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction SPLIT_PART dans StackQL : découper une chaîne selon un séparateur et renvoyer la partie de rang donné (à partir de 1), utile pour décomposer ID de ressources et ARN."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Découpe la chaîne de caractères source selon un séparateur et renvoie la partie indiquée (en comptant à partir de un).  

Voir aussi :  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*source*__  
Une chaîne littérale ou une colonne de type chaîne sur laquelle effectuer le découpage.

__*sep*__  
Un ou plusieurs caractères à utiliser comme séparateur.  

> Si `sep` est composé de plusieurs caractères, chaque caractère est traité comme un séparateur.  

> Seuls les symboles ASCII (1 octet) sont pris en charge comme séparateurs.  

__*part*__  
L'élément (numéroté à partir de un) de la chaîne découpée à renvoyer. Les valeurs négatives servent à compter à rebours depuis le dernier élément.  

## Valeur(s) renvoyée(s) {#return-values}
Renvoie une chaîne.

* * *

## Exemples {#examples}

### Extraire des éléments d'une chaîne séparés par une barre oblique {#extract-elements-from-a-string-seperated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Résultats', value: 'results', },
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

### Extraire le dernier élément d'une URL {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Résultats', value: 'results', },
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

Pour plus d'informations, consultez [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
