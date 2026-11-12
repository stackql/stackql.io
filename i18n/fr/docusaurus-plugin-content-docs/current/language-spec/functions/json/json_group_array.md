---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Agréger des objets JSON en un tableau JSON avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Agrège des valeurs sous la forme d'un tableau JSON. `JSON_GROUP_ARRAY()` collecte toutes les valeurs de chaque groupe dans une colonne spécifiée et les combine en un tableau JSON.

Voir aussi :  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Arguments {#arguments}

__*column_name*__  
La colonne dont les valeurs sont agrégées en un tableau JSON.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un tableau JSON contenant toutes les valeurs agrégées de la colonne spécifiée.

* * *

## Exemples {#examples}

### Agréger les noms de groupes de ressources en un tableau JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Cette requête renvoie un tableau JSON de noms de groupes de ressources, par exemple `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Agréger les groupes de ressources par emplacement {#aggregate-resource-groups-by-location}

```sql
-- Collects resource group names into a JSON array grouped by location
SELECT
  location,
  JSON_GROUP_ARRAY(name) AS resource_groups
FROM
  azure.resource.resource_groups
WHERE
  subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
GROUP BY
  location;
```

Cette requête organise les groupes de ressources en tableaux JSON selon leur emplacement et renvoie plusieurs tableaux, un par emplacement, contenant les noms des groupes de ressources de cet emplacement.
