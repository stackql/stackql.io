---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion GROUP_CONCAT in StackQL: verkettet die Nicht-NULL-Werte einer Spalte oder Gruppierung mit einem optionalen Trennzeichen zu einer Zeichenfolge."
image: "/img/stackql-featured-image.png"
---
Gibt eine Zeichenfolge zurück, die die Verkettung aller Nicht-`NULL`-Werte einer Eingabespalte oder Gruppierung ist.  

> Die Reihenfolge der verketteten Elemente ist beliebig.

Siehe auch:  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumente {#arguments}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck.  Wenn __*__ angegeben ist, werden alle Datensätze in der Ressource oder Gruppierung gezählt.  

> Mindestens ein Wert der *columnExpression* darf nicht `NULL` sein, andernfalls wird `NULL` zurückgegeben.

__*separator*__  
Trennzeichen für das verkettete Ergebnis; wenn *separator* nicht angegeben ist, wird der Standardwert `,` verwendet.

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

## Rückgabewert(e) {#return-values}

Gibt eine Zeichenfolge als Liste von Werten zurück, die durch den *separator* oder `,` getrennt sind.

* * *

## Beispiele {#examples}

### Eine Liste von Werten einer Spalte über eine gesamte Ressource zurückgeben {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Eine Liste von Werten eines Spaltenausdrucks gruppiert nach einem anderen Spaltenausdruck zurückgeben {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
