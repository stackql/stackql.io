---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Arkuskosinus (Umkehrfunktion des Kosinus) einer Zahl mit SQL in StackQL berechnen und einen Winkel im Bogenmaß zurückgeben.
image: "/img/stackql-featured-image.png"
---
Berechnet den Arkuskosinus (Umkehrfunktion des Kosinus) einer Zahl, also den Winkel im Bogenmaß, dessen Kosinus die angegebene Zahl ist.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*value*__  
Ein numerischer Ausdruck im Bereich von -1 bis 1 (einschließlich), dessen Arkuskosinus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Arkuskosinus des angegebenen Werts im Bogenmaß zurück.

* * *

## Beispiele {#examples}

### Den Arkuskosinus von 0.5 berechnen {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Diese Abfrage gibt ungefähr `1.047` zurück, da der Arkuskosinus von 0.5 Radiant π/3 ist, also etwa 1.047 Radiant.

### Den Arkuskosinus von 1 berechnen {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Diese Abfrage berechnet den Arkuskosinus von 1 und ergibt `0`, da der Kosinus von 0 Radiant 1 ist.
