---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Zugriffsverwaltung
  - Gruppen
  - Principal-Verzeichnis
description: "GROUPS-Funktion in StackQL: gibt die Verzeichnisgruppen des authentifizierten Aufrufers als JSON-Array zurück, wenn die Zugriffsverwaltung aktiviert ist."
image: "/img/stackql-featured-image.png"
---
Gibt die Verzeichnisgruppenmitgliedschaften des authentifizierten Aufrufers als JSON-Array zurück.  Erfordert eine Sitzung, die mit aktivierter [Zugriffsverwaltung](/command-line-usage/access-management) gestartet wurde.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Zugriffsverwaltung ]](/command-line-usage/access-management)

* * * 

## Syntax {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Argumente {#arguments}

Keine.

## Rückgabewert(e) {#return-values}

Gibt ein JSON-Array von Gruppen-IDs aus dem Snapshot des Principal-Verzeichnisses zurück, der für die aktuelle Anweisung erstellt wurde, zum Beispiel `["readers"]`.  Die Mitgliedschaft wird für jede Anweisung neu geladen, sodass ein Entzug ab der nächsten Anweisung sichtbar ist.  In einer Sitzung, die ohne `--access.config` gestartet wurde, schlägt die Funktion fehl.

* * *

## Beispiele {#examples}

### Die festgestellte Identität prüfen {#inspect-the-established-identity}

Die beiden Funktionen zusammen zeigen die Principal-ID des Aufrufers und dessen aktuelle Verzeichnisgruppen.  Da die Mitgliedschaft für jede Anweisung neu gelesen wird, bestätigt die Ausführung nach einer Verzeichnisänderung, dass die Änderung für die Sitzung sichtbar ist.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
