---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction JSON_OBJECT dans StackQL : construire un objet JSON à partir d'arguments clé et valeur, par exemple pour composer les corps de requête d'INSERT et d'UPDATE."
image: "/img/stackql-featured-image.png"
---
Renvoie un objet construit à partir des arguments fournis.  

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Types de données ]](/language-spec/data-types)

* * * 

:::tip 

Utilisez l'instruction [**DESCRIBE**](/language-spec/describe) pour repérer les types de données `array` ou `object` utilisables avec les fonctions JSON de StackQL.

:::

:::note 

Si vous utilisez stackql avec un backend SQL `postgres`, utilisez `jsonb_build_object` à la place de `json_object` : StackQL transmet `json_object` tel quel à PostgreSQL, et la fonction PostgreSQL de ce nom prend ses clés et ses valeurs sous forme de tableaux de texte.

:::

## Syntaxe {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*key1, key2, ...*__  
Les libellés de l'objet JSON.

__*value1, value2, ...*__  
Les valeurs correspondant aux libellés de l'objet JSON.

## Valeur(s) renvoyée(s) {#return-values}
Une chaîne de caractères représentant l'objet JSON bien formé.

* * *

## Exemples {#examples}

### Construire un tableau d'objets JSON à partir d'une colonne {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Pour plus d'informations, consultez [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
