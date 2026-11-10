---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Befehl stackql shell startet die interaktive StackQL-Shell zum Ausführen von Abfragen und Provider-Befehlen gegen Ihre Cloud- und SaaS-Konten."
image: "/img/stackql-featured-image.png"
---

Befehl zum Starten der interaktiven StackQL-Shell.  Weitere Informationen finden Sie unter [Die interaktive StackQL-Shell verwenden](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Syntax {#syntax}

`stackql shell [flags]`

* * *

### Flags {#flags}

| Flag | Beschreibung |
|--|--|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|Hilfeinformationen ausgeben|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Führt Abfragen im ausführlichen Modus mit zusätzlicher Ausgabe auf stdout aus; ist die Option `-f` gewählt, werden diese zusätzlichen Protokollinformationen zusammen mit den Abfrageergebnissen in die Ausgabedatei geschrieben|
<br/>
<br/>
> weitere Optionen finden Sie unter [Globale Flags](/command-line-usage/global-flags)

* * *

### Beispiele {#examples}

Die StackQL-Shell starten und eine Dienstkonto-Schlüsseldatei zur Authentifizierung verwenden.
```shell
stackql shell --keyfilepath /tmp/stackql-demo.json
```
