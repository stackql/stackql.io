---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Arkustangens (Umkehrfunktion des Tangens) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Arkustangens (Umkehrfunktion des Tangens) einer Zahl, also den Winkel im Bogenmaß, dessen Tangens die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck, dessen Arkustangens berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Arkustangens des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Arkustangens von 1 berechnen {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Diese Abfrage gibt π/4 oder ungefähr `0.785` zurück, da der Arkustangens von 1 gleich π/4 Radiant ist.

### Den Arkustangens von 0 berechnen {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Diese Abfrage berechnet den Arkustangens von 0 und ergibt `0`, da der Tangens von 0 Radiant 0 ist.
