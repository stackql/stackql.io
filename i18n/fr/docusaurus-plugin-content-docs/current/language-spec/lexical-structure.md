---
title: Structure lexicale
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Structure lexicale de StackQL : comment l'analyseur syntaxique découpe instructions, identifiants, guillemets, littéraux, commentaires et espaces blancs en unités lexicales."
image: "/img/stackql-featured-image.png"
---

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[ Mots-clés ]](/language-spec/keywords) [[ Types de données ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


La structure lexicale et la grammaire de StackQL sont calquées sur l'implémentation du langage de MySQL (une adaptation de la norme ANSI SQL-92), avec quelques extensions. Les instructions StackQL servent à interroger des ressources cloud (avec la grammaire `SELECT`) ou à créer, modifier ou supprimer de l'infrastructure à l'aide d'instructions standard du langage de manipulation de données (DML) telles que `INSERT`, `UPDATE`, `REPLACE` et `DELETE`. De plus, les opérations hors DML, par exemple démarrer une instance, ou lire ou écrire un objet dans un bucket AWS S3, sont implémentées sous forme de procédures de provider intégrées (comparables aux procédures stockées d'un SGBD). Les procédures de provider intégrées sont appelées avec l'instruction `EXEC`.

Le langage dédié SQL de StackQL permet une programmation de l'infrastructure aussi bien déclarative que procédurale.

* * * 

## Syntaxe {#syntax}
Les opérations StackQL se composent d'une ou de plusieurs instructions terminées par un point-virgule (`;`). Les instructions comprennent des mots-clés, des identifiants, des paramètres et des valeurs ; voici un exemple d'instruction StackQL qui crée un disque de 10 GB dans GCP :

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

Dans l'exemple ci-dessus, _`INSERT`_, _`INTO`_ et _`SELECT`_ sont des mots-clés, _google.compute.disks_ est un identifiant, _project_, _zone_, _name_, _sizeGb_ sont des paramètres, et _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ sont des valeurs.

## Mots-clés {#keywords}
Les mots-clés ne sont pas sensibles à la casse : `INSERT` et `insert` sont traités de manière équivalente, mais par convention ils sont souvent écrits en majuscules. La liste complète des mots-clés se trouve dans la section Mots-clés.

## Identifiants {#identifiers}
Les identifiants sont des symboles qui représentent des objets dans la hiérarchie des ressources d'un fournisseur cloud. Les identifiants se composent de plusieurs parties ; pour plus d'informations sur les identifiants, consultez la rubrique Hiérarchie des ressources StackQL.

## Paramètres et valeurs {#parameters-and-values}
Les paramètres sont définis par la ressource ; une instruction `DESCRIBE` permet de les trouver pour une ressource donnée d'un fournisseur cloud. Les valeurs sont des objets typés, scalaires ou complexes, qui correspondent aux paramètres. Pour plus d'informations sur les paramètres et les valeurs, consultez les rubriques `DESCRIBE` et Types de données.

## Variables {#variables}
Un identifiant préfixé par un dollar, tel que `$region`, déréférence une variable de session affectée avec [`SET`](/language-spec/set) ; il est remplacé par la valeur typée de la variable avant que l'instruction ne soit planifiée. Un `'$region'` entre guillemets simples est un littéral de chaîne de caractères. Dans une clause `FROM`, une `$variable` se résout en nom de relation, et `IDENTIFIER($variable)` se résout en nom de colonne dans les positions d'expression. Un chemin entre guillemets doubles et préfixé par un dollar, tel que `"$.auth.aws.password"`, est une cible de configuration d'exécution acceptée par `SET` et `UNSET`. Les variables de session sont disponibles à partir de StackQL `v1.0`, consultez [Variables de session](/getting-started/session-variables).

## Opérateurs {#operators}
`||` est l'opérateur de concaténation de chaînes et a une priorité plus élevée que les opérateurs de comparaison. `OR` est la disjonction booléenne. Les versions de StackQL antérieures à `v1.0` reconnaissaient `||` comme l'unité lexicale `OR`.

## Commentaires {#comments}
StackQL prend en charge les commentaires sur plusieurs lignes et les commentaires en ligne dans les instructions StackQL ; des exemples de commentaires sont présentés ci-dessous :

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
