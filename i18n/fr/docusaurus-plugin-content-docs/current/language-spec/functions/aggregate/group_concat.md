---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction d'agrégation GROUP_CONCAT dans StackQL : concaténer les valeurs non NULL d'une colonne ou d'un regroupement en une seule chaîne, avec un séparateur facultatif."
image: "/img/stackql-featured-image.png"
---
Renvoie une chaîne de caractères qui est la concaténation de toutes les valeurs non `NULL` d'une colonne d'entrée ou d'un regroupement.  

> L'ordre des éléments concaténés est arbitraire.

Voir aussi :  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments {#arguments}

__*columnExpression*__  
Une colonne ou une expression.  Si __*__ est spécifié, tous les enregistrements de la ressource ou du regroupement sont comptés.  

> Au moins une valeur de *columnExpression* doit être non `NULL`, sinon `NULL` est renvoyé.

__*separator*__  
Séparateur utilisé pour le résultat concaténé ; la valeur par défaut `,` est utilisée si *separator* n'est pas spécifié.

__*groupByColumn*__  
Une ou plusieurs colonnes sur lesquelles effectuer des opérations de synthèse ou d'agrégation.  La clause `GROUP BY` renvoie une ligne pour chaque regroupement de colonnes.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne sous forme de liste de valeurs séparées par *separator* ou par `,`.

* * *

## Exemples {#examples}

### Renvoyer une liste des valeurs d'une colonne sur l'ensemble d'une ressource {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer une liste des valeurs d'une expression de colonne regroupée selon une autre expression de colonne {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
