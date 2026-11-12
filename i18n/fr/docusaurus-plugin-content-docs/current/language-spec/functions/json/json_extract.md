---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction JSON_EXTRACT dans StackQL : extraire par chemin une ou plusieurs valeurs d'un objet ou tableau JSON, l'outil principal pour aplatir les champs de réponse d'API imbriqués."
image: "/img/stackql-featured-image.png"
---
Renvoie une ou plusieurs valeurs à partir d'un objet ou d'un tableau JSON bien formé.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

Utilisez la fonction [**DESCRIBE**](/language-spec/describe) pour repérer les types de données `array` ou `object` utilisables avec les fonctions JSON de StackQL. Utilisez la fonction [**JSON_TYPE**](/language-spec/functions/json/json_type) pour déterminer les types de données des objets imbriqués ou des éléments de tableau.

:::

:::note 

La fonction `json_extract` est implémentée par la fonction `json_extract_path_text` si vous utilisez stackql avec un backend SQL `postgres`

:::


## Syntaxe {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_array_or_object*__  
Un tableau ou un objet JSON.

__*json_path*__  
Le chemin JSON vers un tableau ou un objet contenu dans *json_array_or_object*.  Consultez [jsonpath.com](https://jsonpath.com/).

> Si un seul *json_path* est fourni, un résultat scalaire est renvoyé, dont le type de données correspond à celui du chemin résolu.

> Si plusieurs arguments *json_path* sont fournis, le résultat est un tableau composé des objets résolus par les chemins respectifs.

> Spécifier `'$'` pour l'argument *json_path* renvoie l'objet JSON entier correspondant à la colonne représentée par l'argument *json_array_or_object*.

> REMARQUE : vous pouvez utiliser des outils comme [`json-viewer`](https://aarrange.com/json-viewer) pour développer et tester des expressions JSON Path

## Valeur(s) renvoyée(s) {#return-values}
Une chaîne de caractères, un entier, un objet, un tableau, un booléen ou null, selon les arguments *json_array_or_object* et *json_path* fournis en entrée.

* * *

## Exemples {#examples}

### Renvoyer les objets disque associés à chaque instance Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer le nom de périphérique et la taille du premier disque attaché à chaque instance Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer le niveau de service réseau de la première interface réseau attachée à chaque instance Google Compute Engine {#return-the-network-tier-of-first-nic-attached-to-each-google-compute-engine-instances}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer la liste des scopes associés au premier compte de service associé à chaque instance Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Pour plus d'informations, consultez [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
