---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Areakosinus hyperbolicus (Umkehrfunktion des Kosinus hyperbolicus) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Areakosinus hyperbolicus (Umkehrfunktion des Kosinus hyperbolicus) einer Zahl, also den nicht negativen Wert \( \theta \) im Bogenmaß, dessen Kosinus hyperbolicus die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck größer oder gleich 1, dessen Areakosinus hyperbolicus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Areakosinus hyperbolicus des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Areakosinus hyperbolicus von 1 berechnen {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Diese Abfrage gibt `0` zurück, da der Areakosinus hyperbolicus von 1 gleich 0 Radiant ist.

### Den Areakosinus hyperbolicus von 10 berechnen {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Diese Abfrage berechnet den Areakosinus hyperbolicus von 10 und ergibt ungefähr `2.9932`, da dies der Wert im Bogenmaß ist, dessen Kosinus hyperbolicus 10 ist.
