---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction IIF dans StackQL : renvoyer l'une de deux valeurs selon qu'une condition est vraie ou non, une expression if-then-else en ligne."
image: "/img/stackql-featured-image.png"
---
Renvoie une valeur en fonction de l'évaluation d'une expression d'entrée.  

Voir aussi :  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntaxe {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> La fonction `iif()` est logiquement équivalente à l'expression `CASE` suivante : `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Arguments {#arguments}

__*expression*__  
Une expression booléenne à évaluer.

:::tip 

L'argument *expression* peut être une fonction imbriquée telle que `JSON_EXTRACT`.

:::

__*result_if_true*__  
Le résultat à renvoyer si *expression* est évaluée à `true`.

__*result_if_false*__  
Le résultat à renvoyer si *expression* est évaluée à `false`.

## Valeur(s) renvoyée(s) {#return-values}
Le type de données de l'argument *result_if_true* ou *result_if_false*.

* * *

## Exemples {#examples}

### Catégoriser la taille des disques des instances Compute Engine {#catgorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
