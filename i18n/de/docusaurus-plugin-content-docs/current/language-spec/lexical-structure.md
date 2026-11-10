---
title: Lexikalische Struktur
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die lexikalische Struktur von StackQL: wie Anweisungen, Bezeichner, Anführungszeichen, Literale, Kommentare und Whitespace vom Parser tokenisiert werden."
image: "/img/stackql-featured-image.png"
---

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[ Schlüsselwörter ]](/language-spec/keywords) [[ Datentypen ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


Die lexikalische Struktur und Grammatik von StackQL orientiert sich an der Sprachimplementierung von MySQL (einer Adaption des ANSI-SQL-92-Standards) mit einigen Erweiterungen.  StackQL-Anweisungen dienen dazu, Cloud-Ressourcen abzufragen (mit der `SELECT`-Grammatik) oder Infrastruktur mit standardmäßigen Data-Manipulation-Language-Anweisungen (DML) wie `INSERT`, `UPDATE`, `REPLACE` und `DELETE` zu erstellen, zu ändern oder zu löschen.  Darüber hinaus sind Nicht-DML-Operationen, etwa das Starten einer Instanz oder das Abrufen oder Ablegen eines Objekts in einem Bucket in AWS S3, als integrierte Provider-Prozeduren implementiert (vergleichbar mit Stored Procedures in einem DBMS).  Integrierte Provider-Prozeduren werden mit der `EXEC`-Anweisung aufgerufen.

Die domänenspezifische SQL-Sprache von StackQL ermöglicht sowohl deklarative als auch prozedurale Infrastrukturprogrammierung.

* * * 

## Syntax {#syntax}
StackQL-Operationen bestehen aus einer oder mehreren Anweisungen, die mit einem Semikolon (`;`) abgeschlossen werden.  Anweisungen enthalten Schlüsselwörter, Bezeichner, Parameter und Werte; eine beispielhafte StackQL-Anweisung zum Erstellen eines 10-GB-Laufwerks in GCP wäre:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

Im obigen Beispiel sind _`INSERT`_, _`INTO`_ und _`SELECT`_ Schlüsselwörter, _google.compute.disks_ ist ein Bezeichner, _project_, _zone_, _name_, _sizeGb_ sind Parameter und _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ sind Werte.

## Schlüsselwörter {#keywords}
Bei Schlüsselwörtern wird nicht zwischen Groß- und Kleinschreibung unterschieden, `INSERT` und `insert` werden also gleich behandelt; gemäß Konvention werden sie jedoch häufig großgeschrieben. Eine vollständige Liste der Schlüsselwörter finden Sie im Abschnitt Schlüsselwörter.

## Bezeichner {#identifiers}
Bezeichner sind Symbole, die Objekte in der Ressourcenhierarchie eines Cloud-Anbieters repräsentieren.  Bezeichner bestehen aus mehreren Teilen; weitere Informationen zu Bezeichnern finden Sie im Thema StackQL-Ressourcenhierarchie.

## Parameter und Werte {#parameters-and-values}
Parameter werden durch die Ressource festgelegt; sie lassen sich mit einer `DESCRIBE`-Anweisung für eine bestimmte Ressource eines Cloud-Anbieters ermitteln.  Werte sind typisierte skalare oder komplexe Objekte, die Parametern zugeordnet sind.  Weitere Informationen zu Parametern und Werten finden Sie in den Themen `DESCRIBE` und Datentypen.

## Variablen {#variables}
Ein Bezeichner mit Dollar-Präfix wie `$region` dereferenziert eine mit [`SET`](/language-spec/set) zugewiesene Sitzungsvariable; er wird durch den typisierten Wert der Variablen ersetzt, bevor die Anweisung geplant wird.  Ein in einfache Anführungszeichen gesetztes `'$region'` ist ein String-Literal.  In einer `FROM`-Klausel wird eine `$variable` zu einem Relationsnamen aufgelöst, und `IDENTIFIER($variable)` wird an Ausdruckspositionen zu einem Spaltennamen aufgelöst.  Ein in doppelte Anführungszeichen gesetzter Pfad mit Dollar-Präfix wie `"$.auth.aws.password"` ist ein Laufzeitkonfigurationsziel, das von `SET` und `UNSET` akzeptiert wird.  Sitzungsvariablen sind ab StackQL `v1.0` verfügbar, siehe [Sitzungsvariablen](/getting-started/session-variables).

## Operatoren {#operators}
`||` ist der Operator für die String-Verkettung und bindet stärker als Vergleichsoperatoren.  `OR` ist die boolesche Disjunktion.  StackQL-Releases vor `v1.0` haben `||` als `OR` tokenisiert.

## Kommentare {#comments}
StackQL unterstützt mehrzeilige Kommentare und Inline-Kommentare innerhalb von StackQL-Anweisungen; Beispiele für Kommentare sind nachfolgend dargestellt:

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
