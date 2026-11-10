---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "UPPER-Funktion in StackQL: einen String in Großbuchstaben umwandeln."
image: "/img/stackql-featured-image.png"
---
Gibt einen Eingabestring in Großbuchstaben zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Datentypen ]](/language-spec/data-types)

* * * 

## Syntax {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> Die Funktion `upper()` funktioniert nur für ASCII-Zeichen.

## Argumente {#arguments}

__*character_expression*__  
Ein ASCII-String.

## Rückgabewert(e) {#return-values}
Gibt einen String-Wert zurück.

* * *

## Beispiele {#examples}

### Einen String in Großbuchstaben zurückgeben {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
