---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction à valeur de table JSON_EACH dans StackQL : développer un tableau ou objet JSON en une ligne par élément ou membre, pour joindre et filtrer les réponses d'API imbriquées."
image: "/img/stackql-featured-image.png"
---
Fonction à valeur de table qui renvoie une table composée d'une ligne pour chaque élément de tableau ou membre d'objet.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

Utilisez la fonction [**DESCRIBE**](/language-spec/describe) pour repérer les types de données `array` utilisables avec la fonction `json_each`.

:::

:::note 

La fonction `json_each` est implémentée par la fonction `json_array_elements_text` si vous utilisez stackql avec un backend SQL `postgres`

:::

## Syntaxe {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Arguments {#arguments}

__*json_array_or_object*__  
Un tableau ou un objet JSON.

__*json_path*__  
Le chemin JSON vers un tableau ou un objet contenu dans *json_array_or_object*.  Consultez [jsonpath.com](https://jsonpath.com/).

> Si *json_path* n'existe pas dans *json_array_or_object*, la fonction renvoie `NULL`.

> La fonction `json_type()` lève une erreur si l'un de ses arguments n'est pas bien formé.

## Valeur(s) renvoyée(s) {#return-values}
Une table composée d'une ligne pour chaque élément de tableau ou membre d'objet.

* * *

## Exemples {#examples}

### Désimbriquer un tableau de chaînes de caractères en une table jointe aux champs associés {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

à titre de contexte :  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

renvoie...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

La requête `stackql` suivante utilise la fonction `json_each` pour désimbriquer le tableau `members` en une table jointe aux champs `role` et, éventuellement, `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

renverrait...

```
|--------------------------------------|-------------------------------------------------------|                                                                                                     
|                 role                 |                     member                            |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com           |                                                                                                     
|--------------------------------------|---------------------------------------------- --------|                                                                                                     
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com    |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com        |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| ...                                  | ...                                                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|    
```

Pour plus d'informations, consultez [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
