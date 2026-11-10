---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Sitzungsvariablen
  - unset
  - Provider-Sitzungen
  - Secrets
description: "UNSET in StackQL: Sitzungsvariablen entfernen oder mit SET konfigurierte Provider-Aliase, Anmeldedatenfelder und Secrets für die aktuelle Sitzung löschen."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Entfernt eine Sitzungsvariable oder löscht die mit [`SET`](/language-spec/set) gesetzte Laufzeitkonfiguration für die aktuelle Sitzung.  Verfügbar in StackQL `v1.0` und höher.

Siehe auch:  
[[` SET `]](/language-spec/set) [[ Sitzungsvariablen ]](/getting-started/session-variables) [[ Provider-Sitzungen ]](/getting-started/provider-sessions)

* * * 

## Syntax {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Jedes von `SET` akzeptierte Ziel: ein einfacher Variablenname oder ein Variablenname in doppelten Anführungszeichen, oder ein Konfigurationspfad mit Dollar-Präfix in doppelten Anführungszeichen.

__*SESSION*__  
Optional und der einzige Gültigkeitsbereich, den es gibt: `UNSET SESSION name` und `UNSET name` sind dieselbe Anweisung, weil jede Variable und jede Konfigurationseinstellung zur aktuellen Sitzung gehört.  `UNSET GLOBAL` wird mit `configuration scope "global" is not supported; use SESSION` abgewiesen.

`UNSET name` ist gleichbedeutend mit `SET name = DEFAULT`.  Ziele in einer Liste werden der Reihe nach entfernt, und das Entfernen eines nicht gesetzten Namens ist kein Fehler.  Das Dereferenzieren einer Variable, nachdem sie entfernt wurde, schlägt fehl, bevor eine Provider-Anfrage gestellt wird.

| Ziel | Wirkung |
|--|--|
| `name` | Entfernt die Sitzungsvariable. |
| `"$.auth.<provider>.<field>"` | Löscht dieses Feld aus dem Sitzungs-Authentifizierungskontext des Providers.  Der vor der Überschreibung geltende Wert wird nicht wiederhergestellt. |
| `"$.providers.<alias>"` | Entfernt die Provider-Alias-Bindung. |
| `"$.secrets.<name>"` | Löscht das Secret aus dem Store der Sitzung.  Bei einem persistenten Store des Betriebssystems wird der gespeicherte Wert gelöscht. |
| `"$.secret_store"` | Verwirft die aktuelle Store-Bindung und beginnt mit leerem Sitzungsspeicher.  Bereits in einem Store des Betriebssystems persistierte Werte werden nicht gelöscht. |

* * *

## Beispiele {#examples}

### Eine Variable entfernen {#remove-a-variable}

Die Variable wird aus der Sitzung entfernt, sodass die auskommentierte Abfrage mit `variable "$region" is not defined` fehlschlägt, statt mit einem veralteten oder leeren Wert zu laufen.  Setzen Sie den Namen erneut, um ihn wiederzuverwenden.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Mehrere Ziele entfernen {#remove-several-targets}

Eine Anweisung kann Variablen und Konfigurationspfade gemeinsam entfernen; die Ziele werden der Reihe nach verarbeitet.  Hier werden eine einfache Variable, eine Variable mit einem Namen mit Punkten und eine Anmeldedaten-Überschreibung in einer Liste entfernt.

```sql
UNSET region, "providers.saved", "$.auth.github.credentialsenvvar";
```

### Den Sitzungsbereich explizit angeben {#make-the-session-scope-explicit}

Diese beiden Anweisungen tun dasselbe: `SESSION` benennt den einzigen Gültigkeitsbereich, den es gibt.  Die explizite Form eignet sich für Skripte, die auch auf Datenbanken laufen, die zwischen Sitzungs- und globalem Zustand unterscheiden.  Die auskommentierte Anweisung schlägt fehl, weil kein geteilter Zustand existiert.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Eine Anmeldedaten-Überschreibung aufheben {#clear-a-credential-override}

Das zuvor mit `SET "$.auth.github.credentialsenvvar"` gesetzte Feld wird aus dem Sitzungs-Authentifizierungskontext des GitHub-Providers gelöscht; die anderen Felder dieses Kontexts bleiben unberührt.  Der vor der Überschreibung geltende Wert wird nicht wiederhergestellt; setzen Sie das Feld daher erneut, wenn der Provider es weiterhin benötigt.

```sql
UNSET "$.auth.github.credentialsenvvar";
```

### Einen Provider-Alias und sein Secret entfernen {#remove-a-provider-alias-and-its-secret}

Der Alias `production` wurde mit `SET "$.providers.production"` gebunden und verweist über `auth_secret` auf ein unter `$.secrets.production` gespeichertes Secret.  Das Entfernen beider in einer Anweisung hebt die Bindung auf, sodass `production.s3.buckets` keine gültige Relation mehr ist, und löscht das Secret aus dem Sitzungs-Store.  `SESSION` ist hier wie überall optional.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
