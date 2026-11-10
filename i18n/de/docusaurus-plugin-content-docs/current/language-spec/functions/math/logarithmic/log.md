---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Logarithmus einer Zahl zu einer beliebigen Basis mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Logarithmus einer Zahl zu einer angegebenen Basis, eine allgemeinere Form der Logarithmusfunktion im Vergleich zu LN, die die Basis \(e\) verwendet.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*base*__  
Die Basis des Logarithmus, ein positiver numerischer Ausdruck ungleich 1.

__*numeric_expression*__  
Ein positiver numerischer Ausdruck, dessen Logarithmus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Logarithmus des angegebenen numerischen Werts zur angegebenen Basis zurück.

* * *

## Beispiele {#examples}

### Den Logarithmus von 1000 zur Basis 10 berechnen {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Diese Abfrage gibt `3` zurück, da \(10^3 = 1000\).

### Den Logarithmus von 16 zur Basis 2 berechnen {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Diese Abfrage berechnet den Logarithmus von 16 zur Basis 2 und ergibt `4`, da \(2^4 = 16\).
