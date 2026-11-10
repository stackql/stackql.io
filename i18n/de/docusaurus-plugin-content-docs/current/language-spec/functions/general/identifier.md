---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Sitzungsvariablen
  - identifier
description: "IDENTIFIER-Funktion in StackQL: löst eine Sitzungsvariable mit Spaltennamen in eine Spaltenreferenz auf, sodass Variablen Spalten in Projektionen und ORDER BY benennen."
image: "/img/stackql-featured-image.png"
---
Löst einen String-Wert, in der Regel eine Sitzungsvariable, in einen Spaltennamen auf.  Verwenden Sie die Funktion dort, wo ein bloßes `$variable` sonst als skalarer Wert gelesen würde.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` SET `]](/language-spec/set) [[ Sitzungsvariablen ]](/getting-started/session-variables)

* * * 

## Syntax {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Argumente {#arguments}

__*variable*__  
Eine Sitzungsvariable oder ein String-Literal, die bzw. das einen Spaltennamen enthält.  Ein qualifizierter Name wie `t.name` wird akzeptiert.  Es wird genau ein Argument akzeptiert, und es muss ein einzelner String-Wert sein; eine Verkettung wie `'t.' || $column_name`, ein zweites Argument oder ein numerischer Wert wird mit `IDENTIFIER requires one string value` abgelehnt.  Ein leerer Wert oder ein Wert, der eine Anweisung enthält, wird mit `invalid identifier value` abgelehnt, und eine nicht definierte Variable schlägt als nicht definiert fehl.  Alle diese Prüfungen erfolgen, bevor eine Anfrage an den Provider gesendet wird.

## Rückgabewert(e) {#return-values}

Die Funktion wird in der geparsten Anweisung durch eine Referenz auf die benannte Spalte ersetzt und gibt daher den Wert dieser Spalte zurück.  Namen werden in Bezeichner umgewandelt und niemals in SQL-Text eingefügt.  Die resultierende Spaltenreferenz kann überall dort verwendet werden, wo eine Spalte zulässig ist: in der SELECT-Liste, in den Klauseln `WHERE`, `GROUP BY` und `ORDER BY`, in Funktionsargumenten sowie innerhalb von CTEs und Unterabfragen.

* * *

## Beispiele {#examples}

### Eine in einer Variablen benannte Spalte projizieren und danach sortieren {#project-and-order-by-a-column-named-in-a-variable}

Die zurückzugebende Spalte und die Spalte, nach der sortiert wird, stammen beide aus `column_name`, und die Ausgabespalte wird anhand von `output_name` umbenannt.  `IDENTIFIER` ist in der SELECT-Liste und in `ORDER BY` erforderlich, weil ein bloßes `$column_name` an diesen Stellen der String `'name'` wäre, nicht die Spalte.  Der Alias verwendet stattdessen die Form in doppelten Anführungszeichen.  Sobald die Variablen aufgelöst sind, entspricht die Anweisung `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Nach einer in einer Variablen benannten Spalte filtern {#filter-on-a-column-named-in-a-variable}

Die aufgelöste Referenz funktioniert sowohl in einem Prädikat als auch in einer Projektion.  Hier stammen das Feld, nach dem gefiltert wird, und der Vergleichswert beide aus Variablen: `IDENTIFIER($filter_column)` wird zur Spalte `status`, während `$filter_value` als String-Literal `'RUNNING'` übergeben wird.  Die beiden Formen sind bewusst unterschiedlich, weil die erste eine Spalte benennt und die zweite einen Wert liefert.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Die Spalte mit einem Tabellenalias qualifizieren {#qualify-the-column-with-a-table-alias}

Ein Wert mit Punkt wird zu einer qualifizierten Spaltenreferenz aufgelöst.  Die Tabelle erhält den Alias `w`, wobei der Alias selbst aus einer Variablen stammt, und `IDENTIFIER($column_name)` wird zu `w.name`.  Diese Form ist zu verwenden, wenn derselbe Spaltenname auf beiden Seiten eines Joins vorkommt und die Referenz sonst mehrdeutig wäre.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Abgelehnte Werte {#rejected-values}

Da der Wert zu einem Namen und nicht zu Abfragetext wird, wird alles, was kein verwendbarer Name ist, zurückgewiesen, bevor die Anweisung geplant wird, und keine Anfrage erreicht den Provider.  Die ersten beiden Anweisungen unten schlagen mit `invalid identifier value` fehl, die dritte mit `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Ein `$variable` in einer `FROM`-Klausel wird ohne `IDENTIFIER` zu einer Relation aufgelöst, und ein `"$variable"` in doppelten Anführungszeichen wird für Aliase, DDL-Spaltennamen, `INSERT`-Spaltenlisten und `UPDATE`-Ziele verwendet.
