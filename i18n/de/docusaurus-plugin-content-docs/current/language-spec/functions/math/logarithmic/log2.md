---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Logarithmus zur Basis 2 einer Zahl mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Logarithmus zur Basis 2 einer gegebenen Zahl und gibt die Potenz zurück, mit der die Zahl 2 potenziert werden muss, um den Wert zu erhalten.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein positiver numerischer Ausdruck, dessen Logarithmus zur Basis 2 berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Logarithmus zur Basis 2 des angegebenen numerischen Werts zurück.

* * *

## Beispiele {#examples}

### Den Logarithmus zur Basis 2 von 16 berechnen {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Diese Abfrage gibt `4` zurück, da \(2^4 = 16\).

### Den Logarithmus zur Basis 2 von 32 berechnen {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Diese Abfrage berechnet den Logarithmus zur Basis 2 von 32 und ergibt `5`, da \(2^5 = 32\).
