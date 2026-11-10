---
title: Datentypen
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "StackQL-Datentypen: die Typen für Ressourceneigenschaften und Parameter in Abfragen und Ergebnissen und ihre Zuordnung zu den API-Schemas der Provider."
image: "/img/stackql-featured-image.png"
---

StackQL-Datentypen stellen Werte für Ein- oder Ausgabefelder (Eigenschaften oder Parameter) dar, die in Cloud-Ressourcen verwendet werden.  Die StackQL-Datentypen sind in der folgenden Tabelle aufgeführt:

| StackQL-Typ | Beschreibung | Beispiel |
|--|--|--|
| `string` | Folge von Unicode-Zeichen in einfachen Anführungszeichen | `'mybucket'` |
| `array` | geordnete Sammlung von Werten, eingeschlossen in eckige Klammern `[ ]` und durch Komma getrennt | `[0,1,2]` |
| `object` | ungeordnete Menge von null oder mehr Name/Wert-Paaren, eingeschlossen in `{ }` und durch Komma getrennt | `{"name": "mydb"}` |
| `boolean` | `true` oder `false`, boolesche Werte stehen nicht in Anführungszeichen und werden als String-Werte behandelt | `true` |
| `integer` | eine ganze Zahl, die positiv, negativ oder null sein kann | `2` |
| `double` | Gleitkommazahl mit doppelter Genauigkeit, kann positiv oder negativ sein | `0.95` |
<br/>
<br/>
> StackQL-Datentypen entsprechen den Datentypen, die die APIs aller großen Cloud-Anbieter bereitstellen.

## Typkonvertierung {#type-conversion}

`CAST(expression AS type)` und `CONVERT(expression, type)` konvertieren einen Wert in einen anderen Typ.  Zusätzlich zu den aus der MySQL-Grammatik übernommenen Konvertierungstypen (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`) akzeptiert StackQL ab `v1.0` auch `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` und `BOOL`.  Die Konvertierung wird vom SQL-Backend ausgeführt, ihre Semantik ist daher die von SQLite oder PostgreSQL.  Konvertierungen werden häufig verwendet, um eine typisierte [Sitzungsvariable](/getting-started/session-variables) mit einer String-Spalte zu vergleichen:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Boolesche Felder von XML-Providern wie AWS werden als `true` und `false` ausgegeben; StackQL-Releases vor `v1.0` gaben sie als `0` und `1` aus.
