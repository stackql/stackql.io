---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Tangens eines im Bogenmaß angegebenen Winkels mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Tangens eines angegebenen Winkels im Bogenmaß.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*radian_value*__  
Ein numerischer Ausdruck, der den Winkel im Bogenmaß angibt, dessen Tangens berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Tangens des angegebenen Winkels zurück.

* * *

## Beispiele {#examples}

### Den Tangens von π/4 Radiant berechnen {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Diese Abfrage gibt `1` zurück, da der Tangens von π/4 Radiant 1 ist.

### Den Tangens von π Radiant berechnen {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Diese Abfrage berechnet den Tangens von π Radiant und ergibt ungefähr `0`, da der Tangens von π sehr nahe bei 0 liegt.
