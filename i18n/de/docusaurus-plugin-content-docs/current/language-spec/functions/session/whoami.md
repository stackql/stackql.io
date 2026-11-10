---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Zugriffsverwaltung
  - whoami
  - Principal
description: "WHOAMI-Funktion in StackQL: gibt die Principal-ID des authentifizierten Aufrufers zurück, wenn die Zugriffsverwaltung aktiviert ist."
image: "/img/stackql-featured-image.png"
---
Gibt die Principal-ID des authentifizierten Aufrufers zurück.  Erfordert eine Sitzung, die mit aktivierter [Zugriffsverwaltung](/command-line-usage/access-management) gestartet wurde.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Zugriffsverwaltung ]](/command-line-usage/access-management)

* * * 

## Syntax {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Argumente {#arguments}

Keine.

## Rückgabewert(e) {#return-values}

Gibt den für die aktuelle Anweisung festgestellten Principal-ID-String zurück.  In einer Sitzung, die ohne `--access.config` gestartet wurde, schlägt die Funktion fehl.

* * *

## Beispiele {#examples}

### Die festgestellte Identität prüfen {#inspect-the-established-identity}

Beide Funktionen nehmen keine Argumente entgegen und beschreiben den Aufrufer, den der Server für diese Anweisung authentifiziert hat.  `whoami()` gibt die Principal-ID zurück und `groups()` die Verzeichnisgruppen des Aufrufers; die gemeinsame Ausführung ist daher eine schnelle Prüfung, ob ein Token oder Zertifikat dem erwarteten Principal zugeordnet wird, bevor etwas anderes ausgeführt wird.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
