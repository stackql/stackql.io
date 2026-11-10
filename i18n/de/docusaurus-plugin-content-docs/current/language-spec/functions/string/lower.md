---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "LOWER-Funktion in StackQL: einen String in Kleinbuchstaben umwandeln."
image: "/img/stackql-featured-image.png"
---
Gibt einen Eingabestring in Kleinbuchstaben zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Datentypen ]](/language-spec/data-types)

* * * 

## Syntax {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> Die Funktion `lower()` funktioniert nur für ASCII-Zeichen.

## Argumente {#arguments}

__*character_expression*__  
Ein ASCII-String.

## Rückgabewert(e) {#return-values}
Gibt einen String-Wert zurück.

* * *

## Beispiele {#examples}

### Einen String in Kleinbuchstaben zurückgeben {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
