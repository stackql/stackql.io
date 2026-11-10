---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Befehl stackql srv betreibt StackQL als PostgreSQL-Wire-Protocol-Server, sodass psql, BI-Tools und Sprachtreiber Cloud- und SaaS-APIs mit SQL abfragen können."
image: "/img/stackql-featured-image.png"
---

Befehl zum Starten von StackQL als Dienst, auf den Clients anschließend über [Postgres-Wire-Protocol](https://www.postgresql.org/docs/current/protocol.html)-Clients mit mTLS-Authentifizierung zugreifen können. Der Befehl `srv` kann zusätzlich zum PostgreSQL-Wire-Protocol-Server einen [Model Context Protocol (MCP)](/command-line-usage/mcp)-Server betreiben und so den Zugriff über beide Protokolle für klassische Datenbank-Clients und KI-Agenten ermöglichen.

* * * 

### Syntax {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Obwohl der StackQL-Server das Postgres-Wire-Protocol verwendet, ist er kein Postgres-Server und benötigt auch keinen.  Das Wire-Protocol dient dem Client zur Kommunikation mit dem Server, der mit StackQL Abfragen oder DML-Operationen gegen entfernte Cloud- und SaaS-Provider ausführt und die Ergebnisse an den Client zurückgibt.

:::

Parameter, die über das Extended Query Protocol gesendet werden (`Parse` / `Bind` / `Execute`, wie von `psycopg` 3, `pgx` und JDBC verwendet), werden serverseitig als String-Literale gebunden.  Ab `v0.12.742` bleibt ein `$n`-Platzhalter innerhalb eines String-Literals, eines quotierten Bezeichners oder eines Kommentars unangetastet, Backslashes in Parameterwerten werden escaped, und ein Textwert `NULL` bleibt der String `NULL` (SQL-`NULL` wird getrennt übertragen).

### Serveroptionen {#server-options}

| Option | Beschreibung |
|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Adresse, auf der der Server lauscht (typischerweise `0.0.0.0`)|
|<span class="nowrap">`--pgsrv.port`</span>|Port, auf dem der Server lauscht (z. B. `5444`)|
|<span class="nowrap">`--pgsrv.tls`</span>|mTLS-Konfigurationsobjekt, siehe das [Beispiel](#examples) unten|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Log-Level (Standard `WARN`)|

<br/>

> weitere Optionen finden Sie unter [Globale Flags](/command-line-usage/global-flags)

### MCP-Serveroptionen {#mcp-server-options}

Beim Ausführen des Befehls `srv` mit MCP-Unterstützung stehen die folgenden zusätzlichen Optionen zur Verfügung:

| Option | Beschreibung |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|MCP-Servertyp: `http` (In-Memory) oder `reverse_proxy` (TCP-basiert)|
|<span class="nowrap">`--mcp.config`</span>|JSON-Konfigurationsobjekt für den MCP-Server (siehe [MCP-Dokumentation](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Kodierung des MCP-Audit-Logs: `jsonl` (Standard) oder `otel` (OTLP/JSON-Log-Records); überschreibt `server.audit.format` im Konfigurationsobjekt|
|<span class="nowrap">`--mcp.protocol.version`</span>|Neueste angekündigte MCP-Protokollrevision: `auto` (Standard), `2026-07-28` (nur sitzungslos) oder eine ältere Revision wie `2025-11-25`; überschreibt `server.protocol_version` im Konfigurationsobjekt|

<br/>

:::info[Inkompatible Änderung in v0.12.742]

Ab `v0.12.742` verweigert der MCP-HTTP-Transport (`--mcp.server.type=http` oder `reverse_proxy`) den Start, sofern das Konfigurationsobjekt nicht mit `server.auth_token_env_var` ein Bearer-Token benennt oder sich mit `"allow_unauthenticated": true` explizit abmeldet.  Siehe [HTTP-Client-Authentifizierung](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Ausführliche Informationen zur Konfiguration und Verwendung des MCP-Servers, einschließlich Deployment-Modi, Konfigurationsoptionen und Integration mit KI-Assistenten, finden Sie in der [Dokumentation des MCP-Befehls](/command-line-usage/mcp).

:::

:::info

Sie müssen die für die Provider-Authentifizierung erforderlichen Umgebungsvariablen setzen, bevor Sie den Server starten; weitere Informationen finden Sie unter [Einen Provider verwenden](/getting-started/using-a-provider).

:::

Bei lang laufenden Servern können Sie mit dem Flag [`--env.file`](/command-line-usage/global-flags) Anmeldedaten vor dem Start in einer Datei im dotenv-Stil auf der Festplatte ablegen oder rotieren, statt Variablen in die Shell zu exportieren:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Bei `stackql srv` wird die Datei einmal beim Start gelesen; der Weg zum erneuten Einlesen während der Sitzung ist das MCP-Tool [`reload_credentials`](/mcp/reload_credentials).

### Flags {#flags}

| Flag | Beschreibung |
|--|--|
|`-H,--help`|Hilfeinformationen ausgeben|
|`-v,--verbose`|Führt Abfragen im ausführlichen Modus mit zusätzlicher Ausgabe auf stdout aus; ist die Option `-f` gewählt,<br/>werden diese zusätzlichen Protokollinformationen zusammen mit den Abfrageergebnissen in die Ausgabedatei geschrieben|

<br/>

> weitere Optionen finden Sie unter [Globale Flags](/command-line-usage/global-flags)

* * *

### Beispiele {#examples}

Einen StackQL-Serverprozess starten und mit einem `psql`-Kommandozeilen-Client verbinden.  

Laden Sie zuerst die Konfigurationsdatei `openssl.cnf` von [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf) herunter.  

Erzeugen Sie anschließend die Zertifikats- und Schlüsseldateien, die Server und Client für die gegenseitige TLS-Authentifizierung (mTLS) verwenden.  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Setzen Sie nun die Umgebungsvariablen; befinden sich Client und Server auf demselben Host, können diese gemeinsam genutzt werden.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Richten Sie ein Authentifizierungsobjekt für die Provider ein, auf die der Server zugreifen muss, zum Beispiel für GitHub.  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Starten Sie nun den Server in einem Terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

Verbinden Sie sich in einem anderen Terminal mit dem `psql`-Kommandozeilen-Client mit dem Server.  

```bash
psql 127.0.0.1
```

Führen Sie nun eine StackQL-Abfrage gegen einen authentifizierten Provider aus.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```
