---
title: Sitzungsvariablen
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Sitzungsvariablen
  - set
  - unset
  - identifier
description: "Sitzungsvariablen in StackQL: typisierte skalare Werte mit SET setzen, sie mit $name in Prädikaten und Ausdrücken dereferenzieren und sie mit IDENTIFIER als Relations- und Spaltennamen verwenden."
image: "/img/stackql-featured-image.png"
---

Siehe auch:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Provider-Sitzungen ]](/getting-started/provider-sessions) [[ Variablen verwenden ]](/getting-started/variables)

Sitzungsvariablen sind skalare Werte, die von der aktuellen Sitzung gehalten und aus späteren Anweisungen referenziert werden.  Sie sind in StackQL `v1.0` und höher verfügbar und funktionieren in der interaktiven Shell, in `exec`-Skripten, über das PostgreSQL-Wire-Protokoll und über den MCP-Server.

Sitzungsvariablen unterscheiden sich von den Templating-Variablen, die unter [Variablen verwenden](/getting-started/variables) beschrieben sind.  Templating-Variablen werden `stackql exec` über `--iqldata` und `--var` übergeben und vor dem Parsen in den Skripttext gerendert.  Sitzungsvariablen werden innerhalb der geparsten Anweisung aufgelöst, behalten ihren Typ und können zwischen Anweisungen geändert werden.

## Variablen setzen und entfernen {#setting-and-removing-variables}

[`SET`](/language-spec/set) weist eine Zeichenkette, eine Zahl, einen booleschen Wert oder `null` zu.  [`UNSET`](/language-spec/unset) entfernt eine Variable; `SET name = DEFAULT` ist gleichbedeutend.  Ein explizites `null` ist ein gespeicherter Wert, kein Zurücksetzen.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Arrays und Objekte sind keine skalaren Werte.  Um ein JSON-Dokument zu halten, speichern Sie es als Zeichenkette:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Eine Variable dereferenzieren {#dereferencing-a-variable}

Verwenden Sie `$name`, ohne Anführungszeichen, an jeder Ausdrucksposition.  Zeichenketten werden als SQL-Literale maskiert; Zahlen, boolesche Werte und `null` behalten ihren Typ.  Ein in einfache Anführungszeichen gesetztes `'$name'` ist der literale Text `$name`, keine Dereferenzierung.  Ein Verweis auf eine nicht vorhandene Variable schlägt fehl, bevor eine Provider-Anfrage gestellt wird.

Aufgelöste Werte können in gewöhnlichen SQL-Ausdrücken verwendet werden, einschließlich `CAST`, `CONVERT`, Verkettung mit `||` und Funktionsaufrufen:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Zuweisungen akzeptieren nur skalare Literale oder direkte Verweise; wenden Sie Konvertierungen und andere Ausdrücke dort an, wo die Variable verwendet wird, nicht in der `SET`-Anweisung.

## Variablen in Prädikaten {#variables-in-predicates}

Eine Variable in einer `WHERE`-Klausel wird aufgelöst, bevor die Anweisung geplant wird, und daher genau wie ein Literal an die Provider-API weitergeleitet.  Erforderliche Parameter können deshalb aus Variablen geliefert werden:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Typisierte Werte werden mit ihrem Typ verglichen.  `WHERE active = $enabled` mit `SET enabled = false` filtert auf den booleschen Wert, und `WHERE id = CAST($some_id AS TEXT)` ist die Form, die Sie verwenden, wenn die Provider-Spalte eine Zeichenkette und die Variable eine Zahl ist.

## Variablen als Objektnamen {#variables-as-object-names}

Variablen können neben Werten auch Relationen und Spalten benennen.  Namen werden in der geparsten Anweisung in Bezeichner umgewandelt und nie in SQL-Text eingefügt, sodass ein Wert, der eine Anweisung enthält, leer ist oder keine Zeichenkette ist, vor jedem Provider-Zugriff abgelehnt wird.

| Position | Form |
|--|--|
| `FROM`-Klausel | `FROM $relation_name` oder komponentenweise `FROM $provider.$service.$resource` |
| Spalte in einem Ausdruck | `IDENTIFIER($column_name)`, siehe [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Spalten- oder Tabellenalias | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL-Spaltennamen, `INSERT`-Spaltenlisten, `UPDATE`-Ziele | die Form `"$name"` in doppelten Anführungszeichen |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Joins, CTEs und Unterabfragen lösen ihre Relationsvariablen auf dieselbe Weise auf.  Relationswerte mit Punkten haben bis zu vier Komponenten.  Variablen sind keine beliebigen SQL-Fragmente: Ein Schlüsselwort, eine kommagetrennte Spaltenliste oder eine ganze `WHERE`-Klausel kann nicht eingesetzt werden.

## Gültigkeitsbereich und Isolation {#scope-and-isolation}

Variablen gehören zu der Sitzung, die sie gesetzt hat.  Getrennte Verbindungen zu einem `stackql srv`-Server haben getrennte Variablen, ebenso getrennte `exec`-Aufrufe.  Jede MCP-Anfrage ist eine eigene Sitzung, daher müssen ein `SET` und die Abfrage, die es verwendet, im selben Anweisungs-Batch gesendet werden.  Neue Sitzungen beginnen ohne Variablen.

Die Sitzung ist der einzige Gültigkeitsbereich.  `SET SESSION` und `UNSET SESSION` werden akzeptiert und bedeuten genau dasselbe wie `SET` und `UNSET`; das Schlüsselwort ist für Skripte gedacht, die sich genauso lesen sollen wie auf Datenbanken, bei denen sich Sitzungs- und globale Einstellungen unterscheiden.  `GLOBAL` wird abgelehnt, weil es keinen sitzungsübergreifend geteilten Zustand gibt.

Prepared Statements lösen Variablen bei der Ausführung auf, nicht bei der Vorbereitung, und ein Plan, der gebundene Sitzungswerte enthält, wird nicht über Ausführungen hinweg wiederverwendet.

Bei aktivierter [Zugriffsverwaltung](/command-line-usage/access-management) erfordert das Lesen einer Variable `session/variables:read` und das Setzen oder Zurücksetzen `session/config:write`.  Das Auflösen einer Variable zu einem Objektnamen gewährt keine Berechtigung für dieses Objekt; die aufgelöste Relation wird wie üblich vor der Ausführung autorisiert.
