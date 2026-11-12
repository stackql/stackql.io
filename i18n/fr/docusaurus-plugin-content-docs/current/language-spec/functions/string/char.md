---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction CHAR dans StackQL : les caractères d'un ou plusieurs codes, pour insérer sauts de ligne, guillemets, tabulations et autres caractères de contrôle dans des chaînes."
image: "/img/stackql-featured-image.png"
---

La fonction `CHAR()` renvoie le caractère correspondant au code ASCII indiqué.

Voir aussi :  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Lorsque StackQL est utilisé avec un backend PostgreSQL, la fonction équivalente est `CHR()`.
:::

## Syntaxe {#syntax}

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> La fonction `CHAR()` renvoie le caractère associé au code ASCII donné.

## Arguments {#arguments}

**_ascii_code_**  
Un entier représentant le code ASCII du caractère souhaité.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie une chaîne de caractères composée d'un seul caractère, correspondant au code ASCII indiqué.

* * *

## Codes ASCII courants {#common-ascii-codes}

| Caractère     | Description            | Code ASCII | Exemple d'utilisation           |
|---------------|------------------------|------------|---------------------------------|
| `\n`          | Saut de ligne          | 10         | `CHAR(10)`                      |
| `'`           | Guillemet simple       | 39         | `CHAR(39)`                      |
| `"`           | Guillemet double       | 34         | `CHAR(34)`                      |
| (espace)      | Espace                 | 32         | `CHAR(32)`                      |
| `:`           | Deux-points            | 58         | `CHAR(58)`                      |
| `;`           | Point-virgule          | 59         | `CHAR(59)`                      |
| `\t`          | Tabulation horizontale | 9          | `CHAR(9)`                       |
| `\r`          | Retour chariot         | 13         | `CHAR(13)`                      |
| `\b`          | Retour arrière         | 8          | `CHAR(8)`                       |
| `\f`          | Saut de page           | 12         | `CHAR(12)`                      |

* * *

## Exemples {#examples}

### Concaténer des chaînes avec des caractères spéciaux {#concatenate-strings-with-special-characters}

Pour concaténer des chaînes en les séparant par un caractère de saut de ligne :

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Résultat :**

```
He said "Hello" and left.
```

* * *
