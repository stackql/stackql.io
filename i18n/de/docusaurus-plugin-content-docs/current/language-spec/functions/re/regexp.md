---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Verwenden Sie reguläre Ausdrücke, um mit SQL in StackQL Muster in Strings abzugleichen.
image: "/img/stackql-featured-image.png"
---
Prüft, ob der Quellstring dem angegebenen Muster eines regulären Ausdrucks entspricht.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*source_string*__  
Der String, in dem nach dem Muster gesucht wird.

__*pattern_expression*__  
Das Muster des regulären Ausdrucks, das mit dem Quellstring abgeglichen wird.

## Rückgabewert(e) {#return-values}
Gibt einen booleschen Wert zurück: `TRUE`, wenn der Quellstring dem Muster des regulären Ausdrucks entspricht, andernfalls `FALSE`.

* * *

## Beispiele {#examples}

### Prüfen, ob ein String Zahlen enthält {#check-if-a-string-contains-numbers}

Dieses Beispiel zeigt, wie geprüft wird, ob der String 'the year is 2021' ein oder mehrere numerische Zeichen enthält:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Diese Abfrage gibt `TRUE` zurück, weil der String den Teilstring '2021' enthält, der dem Muster `[0-9]+` entspricht, das für eine oder mehrere Ziffern steht.

### E-Mail-Format validieren {#validate-email-format}

So prüfen Sie, ob ein String ein gültiges E-Mail-Format hat:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Diese Abfrage gibt `TRUE` zurück, was anzeigt, dass 'user@example.com' wie eine typische E-Mail-Adresse formatiert ist und dem angegebenen Muster des regulären Ausdrucks entspricht.
