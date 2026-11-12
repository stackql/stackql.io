---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Agréger des paires clé-valeur en un objet JSON avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Agrège des paires clé-valeur en un objet JSON, en regroupant selon une colonne spécifiée. `JSON_GROUP_OBJECT()` collecte toutes les paires clé-valeur de chaque groupe dans les colonnes spécifiées et les combine en un seul objet JSON par groupe.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Arguments {#arguments}

__*key_column*__  
La colonne qui fournit les clés des objets JSON.

__*value_column*__  
La colonne qui fournit les valeurs des objets JSON.

__*grouping_column*__  
La colonne selon laquelle les résultats sont regroupés.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie un objet JSON contenant toutes les paires clé-valeur agrégées des colonnes spécifiées, regroupées selon `grouping_column`.

* * *

## Exemples {#examples}

### Agréger les tailles de dépôts par langage de programmation {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Cette requête renvoie un objet JSON pour chaque langage de programmation, où chaque objet contient les noms des dépôts et leurs tailles correspondantes sous forme de paires clé-valeur. Voici un extrait de la sortie attendue :

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Cette sortie est utile pour visualiser la répartition des tailles de dépôts entre les différents langages de programmation au sein d'une organisation, en fournissant des données claires et organisées, regroupées par langage.
