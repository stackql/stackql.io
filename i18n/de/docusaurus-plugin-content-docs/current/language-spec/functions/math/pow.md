---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Eine Zahl hoch eine andere Zahl mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet *x* hoch *y*.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*x*__  
Die Basis, ein numerischer Ausdruck.

__*y*__  
Der Exponent, mit dem die Basis *x* potenziert wird, ebenfalls ein numerischer Ausdruck.

## Rückgabewert(e) {#return-values}
Gibt das Ergebnis von *x* hoch *y* zurück.

* * *

## Beispiele {#examples}

### x hoch y berechnen {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
