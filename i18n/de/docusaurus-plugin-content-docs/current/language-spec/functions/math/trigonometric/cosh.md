---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Kosinus hyperbolicus einer Zahl mit SQL in StackQL berechnen und das Ergebnis zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Kosinus hyperbolicus eines angegebenen Werts.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck, dessen Kosinus hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Kosinus hyperbolicus des angegebenen Werts zurück.

* * *

## Beispiele {#examples}

### Den Kosinus hyperbolicus von 1 berechnen {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Diese Abfrage gibt ungefähr `1.5431` zurück, da der Kosinus hyperbolicus von 1 etwa 1.5431 beträgt.

### Den Kosinus hyperbolicus von 0 berechnen {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Diese Abfrage berechnet den Kosinus hyperbolicus von 0 und ergibt `1`, da der Kosinus hyperbolicus von 0 genau 1 ist.
