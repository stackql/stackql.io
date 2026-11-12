---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction NULLIF dans StackQL : renvoyer NULL lorsque deux arguments sont égaux, sinon renvoyer le premier argument."
image: "/img/stackql-featured-image.png"
---
Accepte deux arguments et renvoie une valeur `NULL` s'ils sont égaux ; sinon, le premier argument est renvoyé.  

> La fonction `NULLIF` est équivalente à `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` est utile avec des fonctions d'agrégation telles que [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) et [`COUNT`](/language-spec/functions/aggregate/count), lorsque le jeu de résultats contient des valeurs spéciales qui ne sont pas `NULL`, mais que vous souhaitez traiter comme `NULL` pour les besoins de l'agrégation.

Voir aussi :  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Syntaxe {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
Deux expressions de n'importe quel type de données, dont l'évaluation donne une valeur `NULL` ou non `NULL`.

> La fonction `NULLIF` parcourt ses arguments de gauche à droite à la recherche d'un argument qui définit une fonction de collation et utilise cette fonction de collation pour toutes les comparaisons de chaînes de caractères.  Si aucun des deux arguments ne définit de fonction de collation, la collation `BINARY` est utilisée.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie `NULL` si l'évaluation des deux arguments *expression* donne la même valeur ; sinon, la première valeur est renvoyée.

* * *

## Exemples {#examples}

### Compter les enregistrements dont un champ est différent d'une valeur donnée {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

Pour plus d'informations, consultez [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
