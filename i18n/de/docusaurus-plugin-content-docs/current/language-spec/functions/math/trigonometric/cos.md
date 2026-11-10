---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Kosinus eines im Bogenmaß angegebenen Winkels mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Kosinus eines angegebenen Winkels im Bogenmaß.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*radian_value*__  
Ein numerischer Ausdruck, der den Winkel im Bogenmaß angibt, dessen Kosinus berechnet werden soll.

## Rückgabewert(e) {#return-values}
Gibt den Kosinus des angegebenen Winkels zurück.

* * *

## Beispiele {#examples}

### Den Kosinus von π/3 Radiant berechnen {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Diese Abfrage gibt `0.5` zurück, da der Kosinus von π/3 Radiant 0.5 ist.

### Den Kosinus von π Radiant berechnen {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Diese Abfrage berechnet den Kosinus von π Radiant und ergibt `-1`, da der Kosinus von π gleich -1 ist.
