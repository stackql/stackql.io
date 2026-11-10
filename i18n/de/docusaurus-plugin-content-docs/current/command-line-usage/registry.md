---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Befehl stackql registry listet, lädt und verwaltet Provider aus der StackQL-Provider-Registry über die Kommandozeile."
image: "/img/stackql-featured-image.png"
---

Befehl zur Interaktion mit der StackQL Provider Registry.  Der Speicherort der Registry und ob Signaturen von Provider-Dokumenten geprüft werden, werden mit dem globalen Flag [`--registry`](/command-line-usage/global-flags#--registry) konfiguriert.  Der Befehl `registry` ist nicht verfügbar, solange die [Zugriffsverwaltung](/command-line-usage/access-management) aktiviert ist; verwenden Sie stattdessen `REGISTRY PULL` in einer geschützten `exec`- oder `shell`-Sitzung.

* * * 

### Syntax {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Argumente {#arguments}

| Argument | Beschreibung | Beispiel |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Auszuführende Registry-Operation (entweder `list` oder `pull`) | `list` oder `pull` |    
|<span class="nowrap">`provider`</span>|Mit dem Unterbefehl `pull` zu installierender Provider| `okta` |
|<span class="nowrap">`version`</span>|Optionale Provider-Version, die mit dem Unterbefehl `pull` installiert werden soll *(Version des StackQL-Provider-Dokuments)* | `v23.03.00121` |


### Flags {#flags}

| Flag | Beschreibung |
|--|--|
|<span class="nowrap">`-H, --help`</span>|Hilfeinformationen ausgeben|
<br/>
<br/>
> weitere Optionen finden Sie unter [Globale Flags](/command-line-usage/global-flags)

* * *

### Beispiele {#examples}

Verfügbare Provider in der öffentlichen Registry auflisten:
```shell
stackql registry list
```

Die neueste Version eines Providers aus der öffentlichen Registry laden:
```shell
stackql registry pull okta
```
