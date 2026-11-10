---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Den Rest der Division einer Zahl durch eine andere mit SQL in StackQL berechnen.
image: "/img/stackql-featured-image.png"
---
Berechnet den Rest der Division von *x* durch *y* und gibt den Modulus zurück. Dies ähnelt der Verwendung des Operators '%', ist jedoch mit nicht ganzzahligen Argumenten kompatibel.

Siehe auch:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*x*__  
Der Dividend, ein numerischer Ausdruck.

__*y*__  
Der Divisor, ebenfalls ein numerischer Ausdruck.

## Rückgabewert(e) {#return-values}
Gibt den Rest der Division von *x* durch *y* zurück.

* * *

## Beispiele {#examples}

### Den Modulus von 10 geteilt durch 3 berechnen {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
