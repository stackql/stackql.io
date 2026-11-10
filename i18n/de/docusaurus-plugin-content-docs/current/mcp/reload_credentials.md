---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: MCP-Tool, das Anmeldedaten aus der dotenv-Datei --env.file erneut einliest und den Auflösungsstatus pro Provider meldet
image: "/img/stackql-featured-image.png"
---

Liest die mit dem Flag [`--env.file`](/command-line-usage/global-flags) angegebene Datei im dotenv-Format erneut in die Prozessumgebung des Servers ein und meldet anschließend den Status der Anmeldedaten-Auflösung für jeden Provider.  Das löst das Problem der beim Start festgelegten Umgebung: Anmeldedaten, die mitten in der Sitzung auf die Festplatte geschrieben oder rotiert werden, erreichen den laufenden Server ohne Neustart.

Die Env-Datei überlagert die Prozessumgebung, statt sie zu ersetzen: Die Anmeldedaten-Auflösung liest zum Zeitpunkt der Anfrage immer die aktuelle Prozessumgebung, sodass beim Start bereits vorhandene Variablen (aus der Shell oder dem `env`-Block des Clients) mit oder ohne `--env.file` weiterhin funktionieren.  Beim Neuladen werden nur in der Datei vorhandene Schlüssel mit nicht leeren Werten geschrieben, und nichts wird jemals entfernt, sodass ein fehlgeschlagenes oder teilweises Neuladen zuvor funktionierende Anmeldedaten erhält.

Geheime Werte werden niemals zurückgegeben, protokolliert oder auditiert - das Tool meldet nur Variablennamen und Status.  Ohne konfiguriertes `--env.file` reduziert sich das Tool auf eine reine Statusprüfung.  Wenn eine Abfrage an der Anmeldedaten-Auflösung scheitert, enthält der MCP-Fehler einen Hinweis, der den Agenten anweist, `reload_credentials` aufzurufen und es erneut zu versuchen, sodass sich agentische Clients selbst heilen.

:::note

Verfügbar in StackQL-Releases nach `v0.10.542`.

:::

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ (Erneutes) Einlesen von Anmeldedaten ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Nein|Beschränkt den Statusbericht auf einen Provider.  Wenn nicht angegeben, wird jeder Provider gemeldet.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | Ergebnis des Einlesens der Env-Datei plus eine Zeile pro Provider mit dem Status der Anmeldedaten-Auflösung |

<br/>

Die strukturierte Payload enthält:

| Feld | Beschreibung |
|--|--|
|`env_file`|Der konfigurierte `--env.file`-Pfad (leer, wenn keiner konfiguriert ist).|
|`env_file_sourced`|`true`, wenn die Datei bei diesem Aufruf gefunden und eingelesen wurde.|
|`sourced_vars`|Namen der aus der Datei gesetzten Umgebungsvariablen - nur Namen, niemals Werte.|
|`providers`|Eine Zeile pro Provider (siehe unten).|

<br/>

Jede `providers`-Zeile enthält:

| Spalte | Beschreibung |
|--|--|
|`provider`|Name des Providers.|
|`auth_type`|Für den Provider konfigurierter Authentifizierungstyp.|
|`sourced_from`|Woher die Anmeldedaten aufgelöst wurden, z. B. `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` oder `none`.|
|`status`|`ok`, `unresolved` oder `not_checked`.|
|`detail`|Fehlertext bei `unresolved`.|

<br/>

Auth-Typen, die außerhalb des Pfads der Anmeldedaten-Bytes authentifizieren (interaktives gcloud, azure CLI, oauth2 client credentials, assume-role-Ketten, OCI-Signierung, null), melden `not_checked`.

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt, einschließlich `read_only` - das Tool ändert nichts außer der eigenen Prozessumgebung des Servers.

Im Backend-Modus `reverse_proxy` nicht unterstützt: Abfragen werden im entfernten stackql-Server-Prozess ausgeführt, daher gibt das Tool einen Fehler zurück, der den Benutzer anweist, die Anmeldedaten auf dem dahinterliegenden Server neu zu laden.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist (gestartet mit `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
