---
title: Variables de session
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de session
  - set
  - unset
  - identifier
description: "Variables de session dans StackQL : valeurs scalaires typées définies avec SET, déréférencées avec $name dans les prédicats et expressions, utilisables comme noms de relation et de colonne avec IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

Voir aussi :  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Sessions de provider ]](/getting-started/provider-sessions) [[ Utiliser des variables ]](/getting-started/variables)

Les variables de session sont des valeurs scalaires conservées par la session en cours et référencées dans les instructions ultérieures. Elles sont disponibles à partir de StackQL `v1.0` et fonctionnent dans le shell interactif, dans les scripts `exec`, via le protocole filaire PostgreSQL et via le serveur MCP.

Les variables de session sont distinctes des variables de templating décrites dans [Utiliser des variables](/getting-started/variables). Les variables de templating sont fournies à `stackql exec` par `--iqldata` et `--var` et sont rendues dans le texte du script avant son analyse. Les variables de session sont résolues à l'intérieur de l'instruction analysée, conservent leur type et peuvent être modifiées entre deux instructions.

## Définir et supprimer des variables {#setting-and-removing-variables}

[`SET`](/language-spec/set) affecte une chaîne de caractères, un nombre, un booléen ou `null`. [`UNSET`](/language-spec/unset) supprime une variable ; `SET name = DEFAULT` est équivalent. Un `null` explicite est une valeur stockée, pas une réinitialisation.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Les tableaux et les objets ne sont pas des valeurs scalaires. Pour conserver un document JSON, stockez-le sous forme de chaîne :

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Déréférencer une variable {#dereferencing-a-variable}

Utilisez `$name`, sans guillemets, à toute position d'expression. Les chaînes sont échappées comme des littéraux SQL ; les nombres, les booléens et `null` conservent leur type. Un `'$name'` entre guillemets simples est le texte littéral `$name`, pas un déréférencement. Une référence à une variable qui n'existe pas échoue avant tout appel d'API au provider.

Les valeurs résolues peuvent être utilisées dans des expressions SQL ordinaires, notamment `CAST`, `CONVERT`, la concaténation avec `||` et les appels de fonction :

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Les affectations n'acceptent que des littéraux scalaires ou des références directes ; appliquez les conversions et les autres expressions là où la variable est utilisée, pas dans l'instruction `SET`.

## Variables dans les prédicats {#variables-in-predicates}

Une variable dans une clause `WHERE` est résolue avant la planification de l'instruction ; elle est donc acheminée vers l'API du provider exactement comme le serait un littéral. Les paramètres obligatoires peuvent ainsi être fournis à partir de variables :

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Les valeurs typées sont comparées avec leur type. `WHERE active = $enabled` avec `SET enabled = false` filtre sur le booléen, et `WHERE id = CAST($some_id AS TEXT)` est la forme à utiliser lorsque la colonne du provider est une chaîne et que la variable est un nombre.

## Variables comme noms d'objet {#variables-as-object-names}

Les variables peuvent désigner des relations et des colonnes aussi bien que des valeurs. Les noms sont convertis en identifiants dans l'instruction analysée et ne sont jamais insérés dans le texte SQL, de sorte qu'une valeur qui contient une instruction, qui est vide ou qui n'est pas une chaîne est rejetée avant tout accès au provider.

| Position | Forme |
|--|--|
| Clause `FROM` | `FROM $relation_name`, ou composant par composant, `FROM $provider.$service.$resource` |
| Colonne dans une expression | `IDENTIFIER($column_name)`, voir [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Alias de colonne ou de table | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| Noms de colonne DDL, listes de colonnes `INSERT`, cibles `UPDATE` | la forme `"$name"` entre guillemets doubles |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Les jointures, les CTE et les sous-requêtes résolvent leurs variables de relation de la même façon. Les valeurs de relation en notation pointée comportent jusqu'à quatre composants. Les variables ne sont pas des fragments SQL arbitraires : un mot-clé, une liste de colonnes séparées par des virgules ou une clause `WHERE` entière ne peuvent pas être substitués.

## Portée et isolation {#scope-and-isolation}

Les variables appartiennent à la session qui les a définies. Des connexions distinctes à un serveur `stackql srv` ont des variables distinctes, tout comme des invocations distinctes de `exec`. Chaque requête MCP constitue sa propre session ; un `SET` et la requête SQL qui l'utilise doivent donc être envoyés dans le même lot d'instructions. Les nouvelles sessions démarrent sans aucune variable.

La session est la seule portée. `SET SESSION` et `UNSET SESSION` sont acceptés et ont exactement le même sens que `SET` et `UNSET` ; le mot-clé existe pour les scripts qui doivent se lire de la même façon que sur les bases de données où les réglages de session et les réglages globaux diffèrent. `GLOBAL` est rejeté, car aucun état n'est partagé entre les sessions.

Les instructions préparées résolvent les variables au moment de leur exécution, pas au moment de leur préparation, et un plan qui contient des valeurs de session liées n'est pas réutilisé d'une exécution à l'autre.

Lorsque la [gestion des accès](/command-line-usage/access-management) est activée, la lecture d'une variable nécessite `session/variables:read` et sa définition ou sa réinitialisation nécessite `session/config:write`. La résolution d'une variable en nom d'objet n'accorde aucune permission sur cet objet ; la relation résolue est autorisée avant l'exécution, comme d'habitude.
