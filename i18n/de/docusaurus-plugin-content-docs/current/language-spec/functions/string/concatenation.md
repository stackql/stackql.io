---
title: Verkettungsoperator
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Verkettungsoperator || in StackQL: zwei oder mehr String-Werte zu einem String zusammenfügen."
image: "/img/stackql-featured-image.png"
---
Gibt den String zurück, der aus der Verkettung, also dem Zusammenfügen, von zwei oder mehr String-Werten entsteht.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*character_expression*__  
Ein String-Literal oder eine String-Spalte.

## Rückgabewert(e) {#return-values}

Gibt einen String zurück.

:::note

Ab StackQL `v1.0` behandelt der Parser `||` als Verkettungsoperator, der stärker bindet als Vergleichsoperatoren, sodass `WHERE id = prefix || suffix` den Wert `id` mit dem zusammengefügten String vergleicht.  Frühere Releases haben `||` als boolesches `OR` tokenisiert.  Verwenden Sie `OR` für die boolesche Disjunktion.  [Sitzungsvariablen](/getting-started/session-variables) können ebenfalls verkettet werden: `$prefix || '-' || $suffix`.

:::

* * *

## Beispiele {#examples}

### Zwei Felder miteinander verketten {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Weitere Informationen finden Sie unter [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
