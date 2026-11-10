---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "CHAR-Funktion in StackQL: Zeichen zu einem oder mehreren Zeichencodes, um Zeilenumbrüche, Anführungszeichen, Tabs und andere Steuerzeichen in Strings einzufügen."
image: "/img/stackql-featured-image.png"
---

Die Funktion `CHAR()` gibt das Zeichen zurück, das dem angegebenen ASCII-Code entspricht.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Bei Verwendung von StackQL mit einem PostgreSQL-Backend lautet die entsprechende Funktion `CHR()`.
:::

## Syntax {#syntax}

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> Die Funktion `CHAR()` gibt das Zeichen zurück, das dem angegebenen ASCII-Code zugeordnet ist.

## Argumente {#arguments}

**_ascii_code_**  
Eine Ganzzahl, die den ASCII-Code des gewünschten Zeichens darstellt.

## Rückgabewert(e) {#return-values}

Gibt einen String aus einem einzelnen Zeichen zurück, das dem angegebenen ASCII-Code entspricht.

* * *

## Häufig verwendete ASCII-Codes {#common-ascii-codes}

| Zeichen       | Beschreibung                | ASCII-Code | Anwendungsbeispiel              |
|---------------|-----------------------------|------------|---------------------------------|
| `\n`          | Zeilenumbruch               | 10         | `CHAR(10)`                      |
| `'`           | Einfaches Anführungszeichen | 39         | `CHAR(39)`                      |
| `"`           | Doppeltes Anführungszeichen | 34         | `CHAR(34)`                      |
| (Leerzeichen) | Leerzeichen                 | 32         | `CHAR(32)`                      |
| `:`           | Doppelpunkt                 | 58         | `CHAR(58)`                      |
| `;`           | Semikolon                   | 59         | `CHAR(59)`                      |
| `\t`          | Horizontaler Tabulator      | 9          | `CHAR(9)`                       |
| `\r`          | Wagenrücklauf               | 13         | `CHAR(13)`                      |
| `\b`          | Rückschritt                 | 8          | `CHAR(8)`                       |
| `\f`          | Seitenvorschub              | 12         | `CHAR(12)`                      |

* * *

## Beispiele {#examples}

### Strings mit Sonderzeichen verketten {#concatenate-strings-with-special-characters}

So verketten Sie Strings mit einem Zeilenumbruchzeichen dazwischen:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Ergebnis:**

```
He said "Hello" and left.
```

* * *
