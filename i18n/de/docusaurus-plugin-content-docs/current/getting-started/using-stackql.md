---
title: StackQL verwenden
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "StackQL verwenden: Abfragen interaktiv in der Shell, nicht-interaktiv mit exec oder als Server ausführen und dabei Provider, Services und Ressourcen entdecken."
image: "/img/stackql-featured-image.png"
---
Siehe auch:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL hat mehrere Nutzungsmodi, darunter: die interaktive StackQL-Shell, die StackQL-Kommandozeile mit StackQL-Anweisungen oder das Ausführen einer oder mehrerer StackQL-Anweisungen aus einer Eingabedatei (IQL-Datei).  Jeder dieser Modi wird im Folgenden näher beschrieben:

- Die interaktive StackQL-Shell verwenden ([`stackql shell`](/command-line-usage/shell))
- Das StackQL-Kommandozeilenwerkzeug verwenden ([`stackql exec`](/command-line-usage/exec))
- Eine Eingabedatei (IQL-Datei) verwenden ([`stackql exec -i`](/command-line-usage/exec))

### Die interaktive StackQL-Shell verwenden {#using-the-stackql-interactive-shell}
Die StackQL-Shell bietet Engineers eine interaktive Programmierumgebung, um Cloud-Ressourcen bereitzustellen und mit ihnen zu interagieren, und ermöglicht außerdem die interaktive Analyse ihrer Cloud-Umgebung (mit [`SELECT`](/language-spec/select)-Anweisungen).  Die Shell ähnelt funktional der MySQL-Shell oder anderen REPL-Shells (Read Evaluate Print Loop).  Die Shell akzeptiert gültige StackQL-Anweisungen und gibt Ergebnisse an den Benutzer zurück.  Anweisungen können sich über mehrere Zeilen erstrecken und werden ausgeführt, sobald ein abschließendes Semikolon eingegeben wird.

Die StackQL-Shell kann mit dem Befehl `shell` aufgerufen werden, wie unten gezeigt (Informationen zur Provider-Authentifizierung finden Sie unter [StackQL Provider Registry](/providers)):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Sobald Sie sich in einer bei einem Cloud-Anbieter authentifizierten Shell befinden, können Sie StackQL-Abfragen ausführen (`SELECT`-Abfragen oder DDL-Operationen - Infrastructure as Code).

Standardmäßig gibt die StackQL-Shell tabellarische Ergebnisse zurück; die Ausgabeformate `json`, `jsonl`, `csv` und `text` sind jedoch über das Flag `--output` verfügbar, siehe [Ausgabemodi](/getting-started/output-modes).  

Sie können das Farbschema der StackQL-Shell mit dem Flag `--colorscheme` ändern, wie unten gezeigt:  

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### Die Initialisierungsdatei `.stackqlrc` {#the-stackqlrc-initialization-file}

Standardwerte für StackQL-Befehlsoptionen, wie das zuvor besprochene Ausgabeformat und Farbschema, können in der Initialisierungsdatei `stackqlrc` festgelegt werden.  Wenn diese Datei im aktuellen Verzeichnis der ausführbaren StackQL-Datei vorhanden ist, wird sie automatisch geladen; alternativ kann sie mit dem Flag `--configfile` angegeben werden, wie unten gezeigt:

```shell
# Specifing a Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Weitere Informationen zu Befehlsoptionen finden Sie im Thema Globale Flags.

### Das StackQL-Kommandozeilenwerkzeug verwenden {#using-the-stackql-command-line-utility}
StackQL-Befehle können nicht-interaktiv mit dem Befehl exec der ausführbaren StackQL-Datei ausgeführt werden.  Mehrere Befehle können in einem Batch ausgeführt werden, sofern jeder einzelne Befehl mit einem Semikolon (`;`) abgeschlossen ist.  Befehlsflags, wie das Flag `--output`, können auf der Kommandozeile oder in der zuvor besprochenen Initialisierungsdatei `.stackqlrc` angegeben werden.  

Die Ausgabe der ausführbaren StackQL-Datei wird auf dem verwendeten System an stdout gesendet; diese Ausgabe kann in einem BASH- oder PowerShell-Skript verarbeitet werden.  

Ein Beispiel für ein Windows-PowerShell-Skript mit einem StackQL-Befehl sehen Sie hier:  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$keyfilepath = "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--keyfilepath ${keyfilepath} --output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
Der obige PowerShell-Befehl zeigt die Auswahl von Compute-Engine-Instanzen und die Anzeige der Ausgabe mit `Out-GridView`.  

### Eine Eingabedatei (IQL-Datei) verwenden {#using-an-input-file-iql-file}
StackQL-Eingabedateien können verwendet werden, um mehrere durch Semikolons abgeschlossene Befehle auszuführen.  Dies ist eine weitere Option für nicht-interaktive oder Batch-Infrastrukturprogrammierung.  Eine Eingabedatei wird mit der Option `-i` oder `--infile` angegeben, wie unten gezeigt:

```shell
# create an iql file
echo "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--keyfilepath /mnt/c/tmp/stackql-demo.json \
--output json | jq '.[] | .name'
```
