---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Befehl stackql exec führt eine StackQL-Anweisung oder ein Skript aus Anweisungen aus, mit Flags für Ausgabeformat, Authentifizierung und die Provider-Registry."
image: "/img/stackql-featured-image.png"
---

Befehl zum Ausführen einer StackQL-Abfrage, entweder als einzelne Anweisung oder als Stapel von Anweisungen aus einem StackQL-Skript (IQL-Datei).  

* * * 

### Syntax {#syntax}

`stackql exec [query] [flags]`

* * *

### Argumente {#arguments}

| Argument | Beschreibung | Beispiel |
|--|--|--|
|`query`|Auszuführende StackQL-Abfrage<br/>oder auszuführender Befehl | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Beachten Sie, dass die Abfrage in einfache oder doppelte Anführungszeichen eingeschlossen sein muss_ |    


### Flags {#flags}

| Flag | Beschreibung |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Ausgabemodus, gültige Werte sind: `table` (Standard), `json`, `jsonl` (zeilengetrenntes JSON, `ndjson` wird als Alias akzeptiert), `csv`, `text` und `otel` (OpenTelemetry-Log-Records, siehe [Ausgabemodi](/getting-started/output-modes#otel-output)), Beispiel: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Führt die Abfrage bei Bedarf aus der angegebenen Datei aus; ist das Flag gesetzt, wird eine als Argument übergebene Abfrage ignoriert, Beispiel: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Quelldaten für den Befehl aus einer `json`- oder `jsonnet`-Datei, Beispiel: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Dry-Run-Flag; nur der Präprozessor wird ausgeführt und die Ausgabe wird zurückgegeben |
|<span class="nowrap">`--var`</span>|Externe Variablen (können Umgebungsvariablen oder Secrets sein), die von den mit dem Flag `--iqldata` angegebenen jsonnet-Dateien eingelesen werden, Beispiel `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Datei, in die die Ergebnisse bei Bedarf geschrieben werden (standardmäßig werden die Ergebnisse nur auf der Konsole ausgegeben), Beispiel `--outfile myoutput.csv`|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|Hilfeinformationen ausgeben|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Führt Abfragen im ausführlichen Modus mit zusätzlicher Ausgabe auf stdout aus; ist die Option `-f` gewählt, werden diese zusätzlichen Protokollinformationen zusammen mit den Abfrageergebnissen in die Ausgabedatei geschrieben|
<br/>
<br/>
> weitere Optionen finden Sie unter [Globale Flags](/command-line-usage/global-flags)


* * *

### Beispiele {#examples}

Abfrageergebnisse im Tabellenformat auf der Konsole ausgeben:
```shell
stackql exec "SHOW services IN google"
```

Abfrageergebnisse im json-Format auf der Konsole ausgeben:
```shell
stackql exec "SHOW services IN google" -o json
```

Abfrageergebnisse im json-Format in eine Datei ausgeben:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Abfrage aus einer Eingabedatei (IQL-Datei) ausführen und die Ergebnisse im json-Format in eine Datei ausgeben:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Exit-Status {#exit-status}

`exec` beendet sich mit Status `0`, sobald die Eingabe verarbeitet wurde; Fehler einzelner Anweisungen werden auf die Konsole geschrieben, Skripte sollten daher die Ausgabe prüfen.  Ab `v1.0` beendet es sich mit Status `1`, wenn eine [`SET`](/language-spec/set)- oder [`UNSET`](/language-spec/unset)-Konfigurationsanweisung fehlschlägt, wenn die [Zugriffsverwaltung](/command-line-usage/access-management) aktiviert ist und eine Anweisung verweigert wird, oder wenn ein Preview-IaC-Lauf eine fehlgeschlagene Ressource meldet.
