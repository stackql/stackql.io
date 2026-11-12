---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de session
  - identifier
description: "Fonction IDENTIFIER dans StackQL : résoudre une variable de session contenant un nom de colonne en référence de colonne, utilisable dans les projections et ORDER BY."
image: "/img/stackql-featured-image.png"
---
Résout une chaîne de caractères, en général une variable de session, en nom de colonne.  Utilisez cette fonction là où `$variable` seul serait sinon lu comme une valeur scalaire.  Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` SET `]](/language-spec/set) [[ Variables de session ]](/getting-started/session-variables)

* * * 

## Syntaxe {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Arguments {#arguments}

__*variable*__  
Une variable de session, ou un littéral de chaîne, contenant un nom de colonne.  Un nom qualifié tel que `t.name` est accepté.  Un seul argument est accepté et il doit s'agir d'une seule valeur de type chaîne ; une concaténation telle que `'t.' || $column_name`, un second argument ou une valeur numérique est rejeté avec `IDENTIFIER requires one string value`.  Une valeur vide, ou une valeur contenant une instruction, est rejetée avec `invalid identifier value`, et une variable non définie échoue comme non définie.  Toutes ces vérifications ont lieu avant tout appel d'API au provider.

## Valeur(s) renvoyée(s) {#return-values}

La fonction est remplacée, dans l'instruction analysée, par une référence à la colonne nommée ; elle renvoie donc la valeur de cette colonne.  Les noms sont convertis en identifiants et ne sont jamais insérés dans le texte SQL.  La référence de colonne obtenue peut être utilisée partout où une colonne peut l'être : la liste de sélection, les clauses `WHERE`, `GROUP BY` et `ORDER BY`, les arguments de fonction, ainsi que dans les CTE et les sous-requêtes.

* * *

## Exemples {#examples}

### Projeter et trier selon une colonne nommée dans une variable {#project-and-order-by-a-column-named-in-a-variable}

La colonne à renvoyer et la colonne de tri proviennent toutes deux de `column_name`, et la colonne de sortie est renommée à partir de `output_name`.  `IDENTIFIER` est nécessaire dans la liste de sélection et dans `ORDER BY`, car `$column_name` seul à ces positions serait la chaîne `'name'`, et non la colonne.  L'alias utilise à la place la forme entre guillemets doubles.  Une fois les variables résolues, l'instruction est identique à `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Filtrer sur une colonne nommée dans une variable {#filter-on-a-column-named-in-a-variable}

La référence résolue fonctionne aussi bien dans un prédicat que dans une projection.  Ici, le champ sur lequel filtrer et la valeur à rechercher proviennent tous deux de variables : `IDENTIFIER($filter_column)` devient la colonne `status`, tandis que `$filter_value` est transmis comme le littéral de chaîne `'RUNNING'`.  Les deux formes sont délibérément différentes, car la première nomme une colonne et la seconde fournit une valeur.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Qualifier la colonne avec un alias de table {#qualify-the-column-with-a-table-alias}

Une valeur contenant un point est résolue en référence de colonne qualifiée.  La table reçoit l'alias `w`, l'alias lui-même étant tiré d'une variable, et `IDENTIFIER($column_name)` devient `w.name`.  C'est la forme à utiliser lorsque le même nom de colonne existe des deux côtés d'une jointure et que la référence serait sinon ambiguë.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Valeurs rejetées {#rejected-values}

Comme la valeur devient un nom et non du texte de requête, tout ce qui n'est pas un nom utilisable est refusé avant que l'instruction soit planifiée, et aucun appel d'API n'atteint le provider.  Les deux premières instructions ci-dessous échouent avec `invalid identifier value` et la troisième avec `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Dans une clause `FROM`, `$variable` est résolu en relation sans `IDENTIFIER`, et la forme entre guillemets doubles `"$variable"` est utilisée pour les alias, les noms de colonnes DDL, les listes de colonnes `INSERT` et les cibles `UPDATE`.
