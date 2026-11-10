---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Logarithmus zur Basis 10 einer Zahl mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Logarithmus zur Basis 10 einer gegebenen Zahl und bestimmt damit die Potenz, mit der die Zahl 10 potenziert werden muss, um den Wert zu erhalten.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein positiver numerischer Ausdruck, dessen Logarithmus zur Basis 10 berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Logarithmus zur Basis 10 des angegebenen numerischen Werts zurück.

* * *

## Beispiele {#examples}

### Den Logarithmus zur Basis 10 von 100 berechnen {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Diese Abfrage gibt `2` zurück, da \(10^2 = 100\).

### Den Logarithmus zur Basis 10 von 1000 berechnen {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Diese Abfrage berechnet den Logarithmus zur Basis 10 von 1000 und ergibt `3`, da \(10^3 = 1000\).
