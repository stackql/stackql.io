---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den natürlichen Logarithmus einer Zahl mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den natürlichen Logarithmus (Logarithmus zur Basis \(e\)) einer gegebenen Zahl.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein positiver numerischer Ausdruck, dessen natürlicher Logarithmus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den natürlichen Logarithmus des angegebenen positiven numerischen Werts zurück.

* * *

## Beispiele {#examples}

### Den natürlichen Logarithmus von 2.71828 (ungefähr \(e\)) berechnen {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Diese Abfrage gibt einen Wert nahe `1` zurück, da der natürliche Logarithmus von \(e\) (ungefähr 2.71828) 1 ist.

### Den natürlichen Logarithmus einer größeren Zahl berechnen {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Diese Abfrage berechnet den natürlichen Logarithmus von 10 und liefert den logarithmischen Wert von 10 zur Basis \(e\).
