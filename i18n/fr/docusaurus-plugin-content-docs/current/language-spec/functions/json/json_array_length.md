---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction JSON_ARRAY_LENGTH dans StackQL : le nombre d'éléments d'un tableau JSON, au premier niveau ou à un chemin donné dans une valeur JSON."
image: "/img/stackql-featured-image.png"
---
Renvoie le nombre d'éléments d'un tableau JSON.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

Utilisez la fonction [**DESCRIBE**](/language-spec/describe) pour repérer les types de données `array` ou `object` utilisables avec les fonctions JSON de StackQL. Utilisez la fonction [**JSON_TYPE**](/language-spec/functions/json/json_type) pour déterminer les types de données des objets imbriqués ou des éléments de tableau.

:::

## Syntaxe {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*json_array_or_object*__  
Un tableau ou un objet JSON.

> Si *json_array_or_object* est un objet JSON (et non un tableau) et que *json_path* n'est pas spécifié, `0` est renvoyé.

__*json_path*__  
Le chemin JSON facultatif vers un tableau contenu dans un objet.  Consultez [jsonpath.com](https://jsonpath.com/).

> Si *json_path* est fourni, la fonction `json_array_length` localise le chemin dans *json_array_or_object* et renvoie la longueur du tableau, ou `0` si *json_path* désigne un objet (et non un tableau).  Si *json_path* est introuvable dans *json_array_or_object*, `NULL` est renvoyé.

> Si *json_array_or_object* ou *json_path* ne sont pas des objets JSON ou ne sont pas bien formés, une erreur est levée.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un entier.

* * *

## Exemples {#examples}

### Renvoyer le nombre de disques attachés aux instances Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer le nombre d'interfaces réseau attachées aux instances Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer le nombre d'ACL associées aux buckets Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Renvoyer le nombre de scopes associés à un compte de service {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
