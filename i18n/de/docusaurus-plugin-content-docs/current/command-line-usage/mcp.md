---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Der Befehl stackql mcp startet StackQL als Model-Context-Protocol-Server für KI-Agenten: Deployment-Modi, Sicherheitsmodi, Neuladen der Anmeldedaten und Audit-Log."
image: "/img/stackql-featured-image.png"
---

Befehl zum Starten von StackQL als Model-Context-Protocol-Server (MCP-Server), mit dem KI-Agenten und Assistenten über die Abfragefunktionen von StackQL mit Cloud-Infrastruktur interagieren können.

:::info[Was ist MCP?]

Das [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) ist ein offenes Protokoll, das standardisiert, wie KI-Anwendungen mit externen Datenquellen und Tools interagieren.  Wenn Sie StackQL als MCP-Server ausführen, können KI-Agenten (Claude, Cursor, Continue, eigene Agenten usw.) Cloud-Infrastruktur über mehrere Provider hinweg in natürlicher Sprache abfragen und verwalten.

:::

* * *

### Syntax {#syntax}

`stackql mcp [flags]` für einen eigenständigen MCP-Server.

`stackql srv [flags]`, um den MCP-Server zusammen mit dem PostgreSQL-Wire-Protokoll-Server im selben Prozess auszuführen.

* * *

### Deployment-Modi {#deployment-modes}

Der MCP-Server von StackQL kann in drei verschiedenen Konfigurationen bereitgestellt werden, um unterschiedlichen Architekturanforderungen gerecht zu werden.  Die folgenden Beispiele aktivieren mit `"allow_unauthenticated": true` den unauthentifizierten Betrieb, der sich für die lokale Entwicklung eignet; für alles, was ein anderer Prozess oder Host erreichen kann, konfigurieren Sie stattdessen ein Bearer-Token (siehe [HTTP-Client-Authentifizierung](#http-client-authentication)).

#### 1. Eigenständiger MCP-Server {#1-standalone-mcp-server}

Führen Sie StackQL als dedizierten MCP-Server auf einem angegebenen Port aus.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Anwendungsfall:** wenn Sie nur Zugriff über das MCP-Protokoll benötigen und keine Kompatibilität mit dem PostgreSQL-Wire-Protokoll brauchen.

#### 2. MCP + PostgreSQL-Server (In-Memory) {#2-mcp--postgresql-server-in-memory}

Führen Sie MCP- und PostgreSQL-Server gleichzeitig aus, mit In-Memory-Kommunikation zwischen beiden.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Anwendungsfall:** wenn Sie sowohl MCP-Protokollzugriff für KI-Agenten als auch das PostgreSQL-Wire-Protokoll für herkömmliche Datenbank-Clients benötigen, mit maximaler Leistung durch In-Memory-Kommunikation.

#### 3. MCP + PostgreSQL-Server (Reverse-Proxy) {#3-mcp--postgresql-server-reverse-proxy}

Führen Sie beide Server mit TCP-basierter Kommunikation aus, was verteilte Deployments und TLS-Verschlüsselung unterstützt.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Mit TLS-Verschlüsselung:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS verschlüsselt den Transport, authentifiziert aber nicht den Client; daher erfordert dieses Beispiel zusätzlich das in `STACKQL_MCP_TOKEN` hinterlegte Bearer-Token.

**Anwendungsfall:** wenn Sie MCP- und PostgreSQL-Workloads auf verschiedene Prozesse oder Hosts verteilen müssen oder TLS-Verschlüsselung für den MCP-Endpunkt benötigen.

* * *

### Konfigurationsoptionen {#configuration-options}

#### MCP-Servertyp {#mcp-server-type}

| Typ | Beschreibung |
|--|--|
|`http`|Direkter HTTP-Servermodus - MCP-Anfragen werden prozessintern von der StackQL-Engine verarbeitet.|
|`reverse_proxy`|Reverse-Proxy-Modus - MCP-Anfragen werden über den konfigurierten DSN an einen dahinterliegenden PostgreSQL-Server weitergeleitet.  Wird mit `stackql srv` verwendet.|
|`stdio`|Transport über Standardein-/-ausgabe - der Server liest Anfragen von `stdin` und schreibt Antworten nach `stdout`.  Wird von in Editoren eingebetteten MCP-Clients (Claude Desktop, Cursor usw.) verwendet.|

#### MCP-Log-Format {#mcp-log-format}

Das Flag `--mcp.log.format` wählt die Kodierung des Audit-Logs: `jsonl` (Standard) oder `otel` (OpenTelemetry-OTLP/JSON-Log-Datensätze).  Es überschreibt `server.audit.format` im Konfigurationsobjekt.  Siehe [Log-Format](#log-format).

#### MCP-Protokollversion {#mcp-protocol-version}

Das Flag `--mcp.protocol.version` legt die neueste Protokollrevision fest, die der Server bekannt gibt: `auto` (Standard, jede unterstützte Revision), `2026-07-28` (nur sitzungslos) oder eine ältere Revision wie `2025-11-25` (diese Revision und frühere).  Es überschreibt `server.protocol_version` im Konfigurationsobjekt.  Siehe [Festlegen der Revision](#pinning-the-revision).

#### MCP-Konfigurationsobjekt {#mcp-configuration-object}

Das Flag `--mcp.config` akzeptiert ein JSON-Objekt mit der folgenden Struktur.

##### Serverkonfiguration {#server-configuration}

| Feld | Beschreibung | Erforderlich |
|--|--|--|
|`server.transport`|Transportprotokoll - `http` oder `stdio`.|Ja|
|`server.address`|Adresse und Port, an die der MCP-Server gebunden wird (z. B. `127.0.0.1:9912`).  Nur für den HTTP-Transport.|Für `http`|
|`server.tls_cert_file`|Pfad zur TLS-Zertifikatsdatei für HTTPS.|Nein|
|`server.tls_key_file`|Pfad zur Datei mit dem privaten TLS-Schlüssel für HTTPS.|Nein|
|`server.auth_token_env_var`|Name einer Umgebungsvariablen, die ein vorab vereinbartes Bearer-Token enthält.  Wenn gesetzt, muss jede HTTP-Anfrage `Authorization: Bearer <token>` mitführen; der Server verweigert den Start, wenn die Variable nicht gesetzt oder leer ist.  Siehe [HTTP-Client-Authentifizierung](#http-client-authentication).|Entweder dieses Feld oder `allow_unauthenticated`, für `http`|
|`server.allow_unauthenticated`|Ausdrückliches Opt-in, den HTTP-Transport ohne Client-Authentifizierung bereitzustellen.  Standard `false`.|Entweder dieses Feld oder `auth_token_env_var`, für `http`|
|`server.mode`|Sicherheitsvertrag, der Mutationen und Lebenszyklusoperationen absichert.  Einer von `read_only`, `safe` (Standard), `delete_safe`, `full_access`.  Siehe [Servermodi](#server-modes).|Nein|
|`server.read_only`|**Veralteter** boolescher Alias für `mode: read_only`.  Sind beide Felder gesetzt, gewinnt `mode`.|Nein|
|`server.stateless`|Stellt Streamable HTTP ohne Sitzungen bereit (keine `Mcp-Session-Id`); so wird Protokollrevision `2026-07-28` über HTTP bereitgestellt.  Standard `false`.  Wird bei `stdio` ignoriert.  Siehe [Unterstützung von Protokollrevisionen](#protocol-revision-support).|Nein|
|`server.protocol_version`|Neueste bekannt gegebene Protokollrevision: `auto` (Standard), `2026-07-28` oder eine ältere Revision wie `2025-11-25`.  `2026-07-28` impliziert `stateless` für Streamable HTTP.  Siehe [Festlegen der Revision](#pinning-the-revision).|Nein|
|`server.audit`|Konfiguration des Audit-Subsystems, einschließlich `audit.format` (`jsonl` oder `otel`).  Siehe [Audit-Log](#audit-log).|Nein|

##### Backend-Konfiguration (nur im Reverse-Proxy-Modus) {#backend-configuration-reverse-proxy-mode-only}

| Feld | Beschreibung | Erforderlich |
|--|--|--|
|`backend.dsn`|PostgreSQL-Verbindungszeichenfolge für den Backend-StackQL-Server.|Ja (für `reverse_proxy`)|

<br />

:::info

Der Backend-DSN sollte für optimale Kompatibilität den Parameter `default_query_exec_mode=simple_protocol` enthalten.

:::

##### Allowlists {#allowlists}

| Feld | Beschreibung |
|--|--|
|`enabled_tools`|Array der Tool-Namen, die der Server veröffentlichen darf.  Wenn weggelassen oder leer, wird jedes eingebaute Tool registriert.  Dient dazu, eine schmale Teilmenge bereitzustellen (z. B. einen schreibgeschützten Inventar-Server).|
|`enabled_prompts`|Array von Prompt-Namen.  Gleiche Semantik wie `enabled_tools`, aber für die Prompt-Oberfläche.|

<br/>

Beispiel - ein Server, der nur `server_info` und `list_providers` veröffentlicht:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Unterstützung von Protokollrevisionen {#protocol-revision-support}

Protokollrevision `2026-07-28`, `server.stateless` und das Audit-Log-Format `otel` sind in StackQL-Releases ab `v0.11.660` verfügbar.  `server.protocol_version` und das Flag `--mcp.protocol.version` sind in StackQL-Releases ab `v0.12.732` verfügbar.

Der Server spricht jede Revision des Model Context Protocol, die das [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) unterstützt, und handelt sie pro Client aus, sodass eine Flotte gemischter Clients mit einem Server arbeiten kann.

| Revision | Lebenszyklus | Genehmigungsaufforderung (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (bekannt gegeben)|Kein Handshake: `server/discover`, danach führt jede Anfrage `io.modelcontextprotocol/protocolVersion` und `io.modelcontextprotocol/clientCapabilities` in `_meta` mit (plus die Header `Mcp-Protocol-Version`, `Mcp-Method` und `Mcp-Name` bei HTTP).|`input_required`-Ergebnis, vom Client mit `inputResponses` erneut gesendet (SEP-2322).|
|`2025-11-25`, `2025-06-18`|`initialize`-/`notifications/initialized`-Handshake.|Vom Server initiierte `elicitation/create`-Anfrage.|
|`2025-03-26`, `2024-11-05`|Wie oben.|Wie oben.|

<br />

**stdio** bedient jede Revision in einem Prozess: Die erste Anfrage eines `2026-07-28`-Clients wird ohne Handshake bedient, und das `initialize` eines älteren Clients funktioniert weiterhin.  Für Claude Desktop, die npm-/PyPI-Launcher oder das Docker-Image muss nichts konfiguriert werden.

**Streamable HTTP** verwendet standardmäßig das zustandsbehaftete Modell mit einer Sitzung pro Client (`Mcp-Session-Id`), das das SDK für Revisionen bis `2025-11-25` bedient.  Eine `2026-07-28`-Anfrage wird mit dem JSON-RPC-Fehler `-32022` beantwortet (nicht unterstützte Protokollversion; ein einfaches HTTP 400 in Releases bis `v0.12.718`), dessen `data.supported` die Handshake-Revisionen auflistet, und `server/discover` gibt dasselbe bekannt, sodass der Client herunterhandelt und bestehende HTTP-Integrationen ihre Sitzungen und ihre Genehmigungsaufforderungen unverändert behalten.  Setzen Sie `server.stateless` (oder legen Sie die Revision auf `2026-07-28` fest, [siehe unten](#pinning-the-revision)), um `2026-07-28` nativ über HTTP bereitzustellen:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Ein sitzungsloser Server gibt keine `Mcp-Session-Id` aus, beantwortet `GET` und `DELETE` mit 405 (es gibt keine Sitzung, die abgebaut werden könnte), hält `tools/list`, `prompts/list` und `resources/list` verbindungsunabhängig und wickelt die Genehmigungsrunde über `input_required` ab.  Er akzeptiert weiterhin das `initialize` eines älteren Clients und bedient dessen Lesezugriffe, kann aber die Elicitation-Capability, die dieser Client beim Initialisieren deklariert hat, nicht behalten (jede Anfrage erhält eine flüchtige Sitzung), sodass ältere Clients auf einem sitzungslosen Server keine abgesicherten Schreibvorgänge genehmigen können.  Wählen Sie `stateless` für Hosts mit der aktuellen Revision und die Standardeinstellung für eine Flotte, die noch ältere Clients enthält.

Der Server hält keinen aufrufübergreifenden Zustand: Modus, Audit und Provider-Anmeldedaten sind Konfiguration auf Prozessebene, sodass nichts hinter explizite Handles verschoben werden muss.

#### Festlegen der Revision {#pinning-the-revision}

Standardmäßig gibt der Server jede unterstützte Revision bekannt und handelt pro Client aus.  Das Flag `--mcp.protocol.version` oder `server.protocol_version` im Konfigurationsobjekt (das Flag gewinnt) legt die neueste bekannt gegebene Revision fest:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Wert | Bekannt gegeben | Wirkung |
|--|--|--|
|`auto` (Standard) oder nicht gesetzt|Jede unterstützte Revision.|Aushandlung wie oben: die höchste Revision, die beide Seiten sprechen.|
|`2026-07-28`|Nur `2026-07-28`.|Nur sitzungslos, und `stateless` ist für Streamable HTTP impliziert.  Das `initialize` eines älteren Clients wird mit `2025-11-25` beantwortet, das Signal für diesen Client, die Verbindung zu trennen, statt die Antwort als den neuen Lebenszyklus zu lesen.|
|`2025-11-25` (oder eine beliebige ältere Revision)|Diese Revision und alles davor.|Nur Handshake-Lebenszyklus: Eine `2026-07-28`-Anfrage erhält den JSON-RPC-Fehler `-32022`, dessen `data.supported` die bekannt gegebenen Revisionen auflistet, sodass der Client neu verhandeln kann.  Ältere Handshake-Clients behalten ihre eigene Revision.|

<br />

Jeder andere Wert scheitert beim Start an der Konfigurationsvalidierung, und der Fehler nennt die zulässigen Werte.

#### Transportgrenzen {#transport-limits}

Request-Bodys bei Streamable HTTP sind auf 4 MiB begrenzt (darüber HTTP 413), tiefer als 1000 Ebenen verschachteltes JSON wird vor dem Parsen abgewiesen, und ein einzelner `stdio`-Frame ist auf 8 MiB begrenzt.  Keine dieser Grenzen ist konfigurierbar; alle liegen weit über jeder SQL-Anweisung, die der Server akzeptiert.

* * *

### (Erneutes) Einlesen von Anmeldedaten (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` und das Tool `reload_credentials` sind in StackQL-Releases nach `v0.10.542` verfügbar.

:::

Der Umgebungsblock eines Prozesses wird beim Start festgelegt.  Das verursacht zwei Probleme für MCP-Server:

- Ein MCP-`stdio`-Server, den ein Desktop-Client (Claude Desktop, Cursor usw.) ohne die Umgebungsvariablen für die Anmeldedaten startet, kann diese nie sehen - der Client übergibt seine eigene Umgebung an den Unterprozess.
- Rotierte kurzlebige Anmeldedaten erreichen eine lang laufende Serversitzung nie.

Das Flag `--env.file` schließt beide Lücken, indem es eine Datei im dotenv-Stil benennt, die als veränderbarer Speicher für Anmeldedaten dient:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Semantik des Einlesens {#sourcing-semantics}

Die Datei wird beim Start für jeden Einstiegspunkt (`exec`, `shell`, `srv`, `registry`, `mcp`) in die Prozessumgebung eingelesen:

- Ist das Flag leer oder nicht gesetzt, passiert nichts - Anmeldedaten werden zur Anfragezeit verzögert aus der Prozessumgebung aufgelöst, genau wie bisher.
- Eine fehlende Datei unter dem angegebenen Pfad wird toleriert und hat keine Wirkung (die Datei kann später erstellt werden).
- Eine fehlerhafte oder nicht lesbare Datei führt beim Start zu einem fatalen Fehler.
- Nur Schlüssel mit nicht leeren Werten werden gesetzt; vorhandene Prozessumgebungsvariablen **werden** überschrieben (die Datei ist die maßgebliche Quelle); nichts wird jemals entfernt.
- Das Flag gilt nur auf der Kommandozeile - es wird nicht aus `.stackqlrc` gelesen.

Das Dateiformat ist ein `KEY=VALUE` pro Zeile.  `#`-Kommentare, Leerzeilen, ein optionales Präfix `export `, optionale einfache oder doppelte Anführungszeichen um den Wert und CRLF-Zeilenenden werden alle toleriert:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

Das Tool [`reload_credentials`](/mcp/reload_credentials) liest die `--env.file`-Datei mitten in der Sitzung erneut in die Prozessumgebung ein und meldet anschließend pro Provider den Status der Auflösung der Anmeldedaten.  Geheime Werte werden nie zurückgegeben, protokolliert oder auditiert - nur Namen und Status.  Ohne konfiguriertes `--env.file` reduziert sich das Tool auf eine reine Statusprüfung.

Scheitert eine Abfrage an der Auflösung der Anmeldedaten, enthält der MCP-Fehler einen Hinweis, der den Agenten anweist, `reload_credentials` aufzurufen und es erneut zu versuchen, sodass agentische Clients sich selbst helfen: Schreiben Sie die Anmeldedaten in die Datei, bitten Sie den Agenten um ein Neuladen, und die gescheiterte Abfrage gelingt beim erneuten Versuch - ohne Neustart des Servers.

`reload_credentials` wird im Backend-Modus `reverse_proxy` nicht unterstützt - Abfragen werden im entfernten stackql-Serverprozess ausgeführt, daher gibt das Tool einen Fehler zurück, der den Benutzer anweist, auf dem dahinterliegenden Server neu zu laden.

#### Windows und Claude Desktop {#windows-and-claude-desktop}

`setx` schreibt in die Windows-Registrierung, nicht in laufende Prozesse, und Claude Desktop übergibt seine eigene (veraltete) Umgebung an MCP-Unterprozesse - daher erfordern Änderungen an Anmeldedaten normalerweise einen vollständigen Neustart von Claude Desktop.  Mit `--env.file` kann die Datei mit den Anmeldedaten jederzeit erstellt oder rotiert werden, und `reload_credentials` übernimmt sie ohne Neustart.  Eine ausgearbeitete Konfiguration finden Sie unter [StackQL mit Claude Desktop verwenden](/getting-started/claude-desktop).

* * *

### HTTP-Client-Authentifizierung {#http-client-authentication}

:::note

`server.auth_token_env_var` und `server.allow_unauthenticated` sind in StackQL-Releases ab `v0.12.742` verfügbar.

:::

Der HTTP-Transport erfordert eine Client-Authentifizierung.  `server.auth_token_env_var` benennt eine Umgebungsvariable, die ein vorab vereinbartes Bearer-Token enthält; jede Anfrage muss `Authorization: Bearer <token>` mitführen, sonst wird sie mit `401` beantwortet.  Das Token wird aus der Umgebung gelesen, sodass es nie in `--mcp.config` oder den Prozessargumenten erscheint.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Serverauthentifizierung erforderlich]

Der Betrieb ohne Token ist ein ausdrückliches Opt-in: Setzen Sie `"allow_unauthenticated": true` in `server`.  Fehlen beide Schlüssel, beendet sich der Server beim Start mit einer Meldung, die beide nennt; er beendet sich ebenfalls, wenn die Token-Variable nicht gesetzt oder leer ist.  Ab dem Release `v0.12.742` müssen Sie bei Verwendung des `http`-Transports entweder `auth_token_env_var` oder `"allow_unauthenticated": true` zu `server` hinzufügen.

:::

TLS (`tls_cert_file` / `tls_key_file`) verschlüsselt den Transport, authentifiziert aber nicht den Client; kombinieren Sie es für jeden Listener jenseits von Loopback mit einem Token.  Cross-Origin-Anfragen von Browsern (ein nicht übereinstimmender `Origin` oder ein Cross-Site-`Sec-Fetch-Site`) werden unabhängig von der Authentifizierung mit `403` abgelehnt.  Der mitgelieferte `stackql_mcp_client` sendet keinen `Authorization`-Header, daher muss ein Server, den er ansteuert, dem unauthentifizierten Betrieb zugestimmt haben.

Ab `v1.0` ersetzt der Start des Servers mit [`--access.config`](/command-line-usage/access-management) das gemeinsame Token-Gate durch eine Authentifizierung pro Aufrufer: Jede HTTPS-Anfrage wird mit dem konfigurierten JWT-, statischen Bearer- oder Mutual-TLS-Schema verifiziert und gegen Richtlinienregeln autorisiert.  Server-TLS ist in diesem Fall zwingend, die `local_os`-Authentifizierung wird über HTTP abgelehnt, und eine Anfrage ohne Anmeldedaten fällt nie auf das Umgebungs-Token des Servers zurück.  Die Zugriffsverwaltung ist mit dem Servertyp `reverse_proxy` nicht verfügbar.

* * *

### Servermodi {#server-modes}

`server.mode` wählt einen von vier Sicherheitsverträgen.  Alle vier erlauben `SELECT` und Metadaten-Lesezugriffe; sie unterscheiden sich darin, wie sie Mutationen und Lebenszyklusoperationen behandeln.

| Modus | `SELECT` / Metadaten | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (Lebenszyklus) |
|--|--|--|--|--|
|`read_only`|erlaubt|abgelehnt|abgelehnt|abgelehnt|
|`safe` (Standard)|erlaubt|Genehmigung erforderlich|Genehmigung erforderlich|Genehmigung erforderlich|
|`delete_safe`|erlaubt|erlaubt|Genehmigung erforderlich|Genehmigung erforderlich|
|`full_access`|erlaubt|erlaubt|erlaubt|erlaubt|

<br />

**Abgelehnt** gibt sofort einen Fehler an den Client zurück.

**Genehmigung erforderlich** verwendet den MCP-Elicitation-Ablauf, dessen Form die ausgehandelte [Protokollrevision](#protocol-revision-support) bestimmt:

- Hat der Client die Elicitation-Capability bekannt gegeben (beim Initialisieren oder bei `2026-07-28` in den Client-Capabilities in `_meta` jeder Anfrage), bittet der Server den Benutzer mit einer kurzen Meldung (Tool-Name, Abfrageklasse, SQL), die Aktion zu genehmigen.  Bei `2026-07-28` wird die Aufforderung als `input_required`-Ergebnis zurückgegeben, das eine `elicitation/create`-Eingabeanfrage unter der ID `stackql_approval` trägt, und der Client wiederholt den Aufruf mit der Antwort in `inputResponses`; bei früheren Revisionen sendet der Server die `elicitation/create`-Anfrage selbst mitten im Aufruf.  Der Benutzer akzeptiert, lehnt ab oder bricht ab.
- Hat der Client Elicitation **nicht** bekannt gegeben, wird das Tool mit einer Meldung abgelehnt, die die Lücke erklärt und den Betreiber auf den Modus `full_access` verweist.

Die Genehmigung wird vom Client beantwortet; sie schützt also davor, dass ein Agent ohne seinen Benutzer handelt, nicht vor einem nicht vertrauenswürdigen Aufrufer.  Kombinieren Sie den HTTP-Transport mit [Client-Authentifizierung](#http-client-authentication), wo Aufrufer nicht vertrauenswürdig sind.

Der Modus gilt global pro Server.  Es gibt keine Überschreibung pro Tool.

Ab `v1.0` lehnt [`run_select_query`](/mcp/run_select_query) `INSERT`, `UPDATE`, `REPLACE`, `DELETE` und `EXEC` in jedem Modus ab, auch in `full_access`; der Fehler nennt `run_mutation_query` oder `run_lifecycle_operation`.  Jedes Tool trägt außerdem einen `title` und die vier MCP-Verhaltenshinweise als explizite boolesche Werte (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), abgeleitet aus derselben Klassifizierung, die das Policy-Gate durchsetzt.  `openWorldHint` ist `true` für `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query` und `run_lifecycle_operation`, die Registry- oder Provider-Endpunkte erreichen, und `false` für Tools, die nur lokalen Zustand lesen.  Die Hinweise sind beratend für die Client-UX; die Durchsetzung bleibt beim Policy-Gate.

:::info[Payload-Gating]

Ab Release `v0.12.742` wird eine Payload mit mehreren Anweisungen in jedem Modus nach ihrer privilegiertesten Anweisung abgesichert, sodass `SELECT 1; DELETE ...` wie ein `DELETE` behandelt wird und der Audit-Datensatz die `query_class` `mutation_delete` trägt.  `validate_select_query` akzeptiert genau eine Anweisung.

:::

#### Verhaltensänderung des Standardmodus {#default-mode-behaviour-change}

Frühere Releases setzten Sicherheit nur über ein einzelnes Flag `read_only: true/false` durch, dessen Standard "keine Durchsetzung" war.  Der aktuelle Standard (`mode: safe`) verlangt für Mutationen von Haus aus die Genehmigung des Benutzers.  Betreiber eines Elicitation-fähigen Clients (Claude Desktop, Cursor, Continue usw.) sehen eine Genehmigungsaufforderung pro Mutation.  Betreiber von Automatisierung oder des mitgelieferten `stackql_mcp_client` (der Elicitation nicht bekannt gibt) müssen sich ausdrücklich für `mode: full_access` entscheiden, um Mutationen und Lebenszyklusoperationen auszuführen.

Der veraltete JSON-/YAML-Schlüssel `read_only: true` wird aus Gründen der Abwärtskompatibilität weiterhin akzeptiert und als gleichwertig zu `mode: read_only` behandelt.

#### Beispiele {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Audit-Log {#audit-log}

Jeder Tool-Aufruf schreibt einen Datensatz in die konfigurierte Audit-Sink, standardmäßig als JSONL oder als OpenTelemetry-Log-Datensätze (siehe [Log-Format](#log-format)).  Audit ist **standardmäßig eingeschaltet**.  Das Audit beantwortet "was hat der Agent getan", nicht "was hat der Agent gesehen" - Ergebniszeilen von `SELECT`-Anweisungen werden in beiden Formaten absichtlich nicht aufgezeichnet.

#### Was aufgezeichnet wird {#what-gets-recorded}

| Feld | Beschreibung |
|--|--|
|`timestamp`|Uhrzeit zu Beginn des Aufrufs (RFC3339).|
|`tool`|Tool-Name (z. B. `run_select_query`).|
|`mode`|Zum Zeitpunkt des Aufrufs geltender Servermodus.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.  Bei einer Payload mit mehreren Anweisungen die Klasse der privilegiertesten.|
|`sql`|SQL-Zeichenfolge für Abfrage-Tools (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Hierarchiefelder für Metadaten-Tools (`list_*`, `describe_method`); SQL + `row_limit` für Abfrage-Tools.|
|`duration_ms`|Dauer von Gate + Handler in Echtzeit.|
|`error`|Fehlermeldung, wenn das Tool einen Fehler ausgelöst hat oder abgelehnt wurde.|

<br />

Ergebniszeilen von `SELECT`-Anweisungen sind bewusst ausgeschlossen - sie können sehr groß sein und sensible Daten enthalten.

#### Datei-Sink {#file-sink}

Die einzige derzeit ausgelieferte Sink ist `file`.  Ein JSON-Objekt pro Zeile, nach jedem Datensatz per fsync gesichert, Rotation im Lumberjack-Stil nach Größe / Alter / Anzahl der Backups.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Zwei Möglichkeiten, den Speicherort anzugeben:

- `audit.file.path` - ein vollständiger Dateipfad (absolut oder relativ zum aktuellen Arbeitsverzeichnis).
- `audit.file.dir` - ein Verzeichnis; die Sink wählt darin den Basisnamen `stackql_mcp_server_<RFC3339-utc-second>.log`.

Ist keines von beiden in `mcp.config` gesetzt, setzt der MCP-Server `dir` standardmäßig auf das aktuelle Arbeitsverzeichnis, sodass bestehende Betreiber dasselbe Verhalten wie zuvor sehen.  Das zugrunde liegende generische Sink-Paket selbst weigert sich, stillschweigend ein Verzeichnis zu wählen - die Entscheidung "wo landen die Logs" trifft immer der Aufrufer.

Der aufgelöste absolute Pfad wird beim Start als `sink file: /path/to/file.log` nach stderr protokolliert.

#### Log-Format {#log-format}

Der Audit-Stream hat zwei Kodierungen, die in dieselbe Datei-Sink geschrieben werden:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

Das Flag überschreibt `server.audit.format` in `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` ist bytekompatibel mit den oben beschriebenen Datensätzen.  `otel` schreibt ein OTLP/JSON-`LogsData`-Objekt pro Zeile, also die Form, die der `otlp_json_file`-Receiver des OpenTelemetry Collector unverändert einliest (geprüft gegen otelcol-contrib 0.160.0), sodass der Stream jede OTLP-Pipeline ohne Transform-Prozessor erreicht:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Jeder Tool-Aufruf erzeugt einen Log-Datensatz; ein Aufruf, der das Genehmigungs-Gate durchlaufen hat, erzeugt einen zweiten Datensatz für die Elicitation-Entscheidung.  Die Datensätze eines Aufrufs teilen sich `gen_ai.tool.call.id` und eine Trace-ID.  Ein vom Aufrufer in `params._meta` übergebener W3C-`traceparent` wird berücksichtigt; andernfalls wird pro MCP-Sitzung eine Trace-ID erzeugt, sodass die Datensätze einer Agentensitzung korrelieren.

Die ausgegebene Attributmenge ist eine versionierte Schnittstelle: Der Instrumentation Scope ist `github.com/stackql/stackql/pkg/mcp_server/audit` in Version `1.0.0`, und `schemaUrl` fixiert die stabilen Konventionen auf `https://opentelemetry.io/schemas/1.44.0`.  Die Attribute `gen_ai.*` und `mcp.*` folgen den semantischen Konventionen für [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) und [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), die den Status "Development" haben; die Scope-Version wird erhöht, wenn sich die Menge ändert.

| Attribut | Datensatz | Wert |
|--|--|--|
|`service.name`, `service.version`|Resource|`stackql`, die StackQL-Version|
|`gen_ai.operation.name`|Tool-Aufruf|`execute_tool`|
|`mcp.method.name`|beide|`tools/call` im Tool-Aufruf-Datensatz, `elicitation/create` im Entscheidungsdatensatz|
|`gen_ai.tool.name`|beide|Tool-Name|
|`gen_ai.tool.call.id`|beide|Pro Aufruf erzeugt, von beiden Datensätzen geteilt|
|`mcp.protocol.version`|beide|Ausgehandelte Revision|
|`mcp.session.id`|beide|Sitzungs-ID (fehlt bei sitzungslosen HTTP-Anfragen)|
|`stackql.mode`|beide|Servermodus|
|`stackql.decision`|beide|Der Wert `decision` aus der Tabelle oben|
|`stackql.query`|Tool-Aufruf|SQL im Wortlaut, für Abfrage-Tools|
|`stackql.query_class`|Tool-Aufruf|Der Wert `query_class`|
|`stackql.provider`|Tool-Aufruf|Provider, für Metadaten-Tools|
|`stackql.query.source`|Tool-Aufruf|ID des Query-Library-Eintrags, falls angegeben|
|`stackql.duration_ms`|Tool-Aufruf|Dauer in Echtzeit|
|`stackql.rows_returned`|Tool-Aufruf|Zeilenanzahl, für Tools, die Zeilen zurückgeben|
|`error.type`, `error.message`|Tool-Aufruf|Bei Fehlschlag: die ablehnende Entscheidung oder `tool_error` nach dem Gate|

<br />

Der Body ist `execute_tool <tool>` / `elicitation <tool>`; der Schweregrad ist `INFO` oder `ERROR`, wenn der Aufruf fehlgeschlagen ist oder abgelehnt wurde.  Der Schwärzungsvertrag ist in beiden Formaten identisch: Ergebniswerte werden nie serialisiert, nur die Anweisung, die sie erzeugt hat.

#### Fehlermodi {#failure-modes}

Gibt die Sink einen Fehler zurück, hängt das Antwortverhalten von `audit.failure_mode` ab.

| failure_mode | Wirkung |
|--|--|
|`strict` (Standard)|Der Tool-Aufruf gibt den Audit-Fehler an den Client zurück, selbst wenn das zugrunde liegende Tool erfolgreich war.  Absichtlich: besser eine mehrdeutige Client-Antwort als ein unentdecktes `DELETE`.|
|`strict_mutations`|`SELECT` / Metadaten-Lesezugriffe werden mit einem Hinweis auf stderr fortgesetzt; Mutationen und Lebenszyklusoperationen geben den Audit-Fehler zurück.|
|`best_effort`|Immer nach stderr protokollieren und fortfahren.|

#### Reihenfolge {#sequencing}

Der Audit-Schreibvorgang erfolgt **nach** der Ausführung des Tools (oder seiner Ablehnung durch das Gate), aber **bevor** die Antwort an den Client zurückgeht.  Im strikten Modus bedeutet ein fehlgeschlagener Audit-Schreibvorgang bei einem erfolgreichen DELETE, dass die Zeile weg ist, der Client aber einen Fehler erhält - absichtlich, damit keine Mutation unauditiert durchrutscht.

#### Audit deaktivieren {#disabling-audit}

Um Audit vollständig abzuschalten (das Verhalten vor PR2):

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Verfügbare MCP-Tools {#available-mcp-tools}

Beim Betrieb als MCP-Server stellt StackQL die folgenden Tools bereit, vorbehaltlich der konfigurierten `enabled_tools`-Allowlist. Jedes gibt sowohl eine gerenderte Textansicht (für das LLM) als auch eine typisierte, strukturierte Payload (für programmatische Clients) zurück. Die Standard-Textdarstellung ist eine Markdown-Tabelle für einheitliche mehrzeilige Ergebnisse oder ein Markdown-KV-Block für dünn besetzte Ergebnisse oder Ergebnisse mit einem einzelnen Datensatz. Tools mit Argumenten akzeptieren eine optionale Überschreibung per `format` (`markdown` oder `json`); andernfalls wird die Render-Einstellung des Servers verwendet. Die Eingaben unten listen aufgabenspezifische Argumente auf; `?` kennzeichnet optionale Eingaben.

Klicken Sie auf einen Tool-Namen, um die vollständige Referenzseite mit Eingaben, Gating-Verhalten und einem Beispiel-Prompt aufzurufen.

| Tool | Renderer | Beschreibung | Eingaben |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Serveridentität und Laufzeit: stackql-Version, zugrunde liegende SQL-Engine, Speicherort der Provider-Registry, Modus, Schreibschutz-Flag.  Einmal zu Sitzungsbeginn aufrufen.|keine|
|[`list_providers`](/mcp/list_providers)|Tabelle|Provider, die bereits in den lokalen Cache gezogen wurden - oberste Ebene der Hierarchie.|keine|
|[`list_services`](/mcp/list_services)|Tabelle|Services unter einem Provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Tabelle|Ressourcen unter einem `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Tabelle|Zugriffsmethoden (HTTP-Operationen) für eine Ressource, mit dem SQL-Verb, auf das jede abgebildet wird, und ihren erforderlichen Parametern.  Vor dem Schreiben einer Abfrage aufrufen - hier werden die erforderlichen `WHERE`-Parameter abgeleitet.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Vollständiger I/O-Vertrag für eine Methode (immer EXTENDED): erforderliche und optionale Eingaben sowie die Ausgabefelder, auf die ein `SELECT` verweisen kann.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Tabelle|Kuratierte Abfragevorlagen anhand einer Absicht in natürlicher Sprache finden. Nur lesend; Mutationseinträge erfordern ein Opt-in und sind im Modus `read_only` ausgeschlossen.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Eine Vorlage und ihre Parameterdeklarationen abrufen. Mit nicht leeren `params` werden die Werte validiert und das gerenderte SQL plus `next_tool` zurückgegeben, ohne Ausführung.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Ein einzelnes `SELECT` parsen und planen, ohne es auszuführen.  Gibt `{valid, errors}` zurück.  Nur `SELECT`; genau eine Anweisung.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Tabelle|Ein `SELECT` ausführen.  Gibt `{rows}` zurück.  Nur Lesezugriffe.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|`INSERT`/`UPDATE`/`REPLACE`/`DELETE` gegen den Provider ausführen.  **Echte Seiteneffekte.** Gibt `{messages, timestamp}` zurück.  Durch den [Modus](#server-modes) des Servers abgesichert.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Eine stackql-`EXEC`-Lebenszyklusoperation ausführen.  Gibt `{messages, timestamp}` zurück.  Durch den [Modus](#server-modes) des Servers abgesichert.|`sql`|
|[`list_registry`](/mcp/list_registry)|Tabelle|In der konfigurierten Registry verfügbare Provider (und ihre Versionen).  Zu unterscheiden von `list_providers`, das nur bereits gezogene Provider auflistet.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Einen einzelnen Provider aus der Registry in den lokalen Cache installieren.  Nur Zustand des lokalen Caches - keine Wirkung auf Control Plane oder Data Plane der Cloud.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Tabelle|Anmeldedaten aus der `--env.file`-dotenv-Datei erneut in die Prozessumgebung einlesen und pro Provider den Auflösungsstatus melden (`ok`, `unresolved`, `not_checked`).  Gibt nie geheime Werte zurück.  In jedem Modus erlaubt.|`provider?`|

Konsultieren Sie `query_library_search`, bevor Sie SQL von Grund auf schreiben, und verwenden Sie dann `query_library_get`, um eine passende Vorlage zu prüfen und zu rendern. Abfrage-Tools akzeptieren außerdem ein optionales `source` mit der ID des Library-Eintrags, ausschließlich zur Zuordnung; die Ausführung unterliegt weiterhin dem normalen Servermodus und der Genehmigungsrichtlinie.

:::note

`describe_resource` wurde in StackQL `v0.12.732` zurückgezogen; MCP-Clients, Prompts oder Agentenanweisungen, die es aufgerufen haben, sollten stattdessen [`list_methods`](/mcp/list_methods) gefolgt von `describe_method` aufrufen.  Eine Ressource hat keine einzelne Feldliste, weil jede Zugriffsmethode ihre eigene Form zurückgibt; Spaltennamen stammen daher aus [`describe_method`](/mcp/describe_method) für die Methode, an die eine Abfrage geleitet wird.  Die SQL-Anweisung [`DESCRIBE`](/language-spec/describe) ist unverändert.

:::

### Verfügbare MCP-Prompts {#available-mcp-prompts}

Ein statischer Prompt wird veröffentlicht.

| Prompt | Beschreibung |
|--|--|
|`write_safe_select`|Anleitung zum Schreiben sicherer `SELECT`-Abfragen gegen stackql-Ressourcen.  Erklärt, wie mit `SHOW METHODS IN <provider>.<service>.<resource>` die richtige Lesemethode und die erforderlichen `WHERE`-Parameter ermittelt werden.|

* * *

### Flags {#flags}

| Flag | Beschreibung |
|--|--|
|`--mcp.server.type`|MCP-Servertyp: `http`, `stdio` oder `reverse_proxy` (Letzterer wird mit `stackql srv` verwendet).|
|`--mcp.config`|JSON-Konfigurationsobjekt für den MCP-Server.  YAML wird ebenfalls akzeptiert.|
|`--mcp.protocol.version`|Neueste bekannt gegebene Protokollrevision: `auto` (Standard), `2026-07-28` oder eine ältere Revision wie `2025-11-25`.  Siehe [Festlegen der Revision](#pinning-the-revision).|
|`--env.file`|Datei mit Anmeldedaten im dotenv-Stil, die beim Start eingelesen und vom Tool `reload_credentials` bei Bedarf erneut eingelesen wird.  Siehe [(Erneutes) Einlesen von Anmeldedaten](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|TCP-Port für den PostgreSQL-Wire-Protokoll-Server (wird mit `stackql srv` verwendet).|
|`-H`, `--help`|Hilfeinformationen ausgeben.|
|`-v`, `--verbose`|Im ausführlichen Modus mit zusätzlicher Ausgabe ausführen.|

&nbsp;
&nbsp;
> siehe [Globale Flags](/command-line-usage/global-flags) für weitere Optionen

:::info

Sie müssen die für die Provider-Authentifizierung erforderlichen Umgebungsvariablen vor dem Start des MCP-Servers setzen oder mit [`--env.file`](#credential-resourcing---envfile--reload_credentials) eine Datei mit Anmeldedaten im dotenv-Stil benennen.  Weitere Informationen finden Sie unter [Einen Provider verwenden](/getting-started/using-a-provider).

:::

* * *

### Beispiele {#examples-1}

#### Einfacher eigenständiger MCP-Server {#basic-standalone-mcp-server}

Starten Sie einen eigenständigen MCP-Server mit Provider-Authentifizierung und einem Bearer-Token für seine Clients:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP-Server mit PostgreSQL-Server (In-Memory) {#mcp-server-with-postgresql-server-in-memory}

Führen Sie MCP- und PostgreSQL-Server für den Zugriff über beide Protokolle aus:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Schreibgeschützter Inventar-Server mit Audit in einen bekannten Pfad {#read-only-inventory-server-with-audit-to-a-known-path}

Nützlich für einen Agenten für Inventar-Scans, der niemals mutieren soll:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Automatisierungsserver mit Vollzugriff {#full-access-automation-server}

Für vertrauenswürdige Automatisierungs-Pipelines, die Mutationen benötigen und nicht auf Elicitation-Aufforderungen antworten können.  Die Pipeline legt das in `STACKQL_MCP_TOKEN` hinterlegte Bearer-Token vor:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Abgesicherter MCP-Server mit TLS {#secure-mcp-server-with-tls}

Starten Sie einen MCP-Server mit TLS-Verschlüsselung im Reverse-Proxy-Modus:

Erzeugen Sie zunächst TLS-Zertifikate (selbstsigniert für die Entwicklung):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Starten Sie dann den Server:

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Ihren MCP-Server testen {#testing-your-mcp-server}

Das Repository liefert einen Entwicklungs-MCP-Client (`stackql_mcp_client`) für Skripting und Regressionstests mit.  Bauen Sie ihn mit:

```bash
python cicd/python/build.py --build-mcp-client
```

Das erzeugt `./build/stackql_mcp_client`.

Beachten Sie, dass `stackql_mcp_client` keinen `Authorization`-Header sendet; der Server, den er anspricht, muss daher `"allow_unauthenticated": true` setzen (wie in den Beispielen oben); ein tokengeschützter Server antwortet ihm mit `401`.  Er gibt außerdem die MCP-Elicitation-Capability **nicht** bekannt - er ist für nicht interaktives Skripting gedacht.  Gegen einen `safe`- oder `delete_safe`-Server erhält er bei Mutations- und Lebenszyklusaufrufen die Ablehnung "client does not support elicitation".  Für interaktive Tests mit Elicitation-Aufforderungen verwenden Sie einen in den Editor eingebetteten MCP-Client (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Beispielantworten {#example-responses}

`server_info` (gegen einen Server im Standardmodus):

```json
{
  "version": "0.10.444",
  "commit": "abc1234",
  "build_date": "2026-05-16T10:47:33Z",
  "platform": "linux/amd64",
  "transport": "http",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.io/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (gegen einen `full_access`-Server):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Beispielhafte Audit-Log-Zeile für dieselbe Mutation:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Integration mit KI-Assistenten {#integration-with-ai-assistants}

Um den MCP-Server von StackQL in einen KI-Assistenten zu integrieren, registrieren Sie `stackql` als MCP-Server in der Konfiguration des Assistenten.  Die meisten in Editoren eingebetteten MCP-Clients betreiben den Server über `stdio`; verwenden Sie für diese `--mcp.server.type=stdio`, und der Assistent startet den Prozess direkt.  Eigenständige Agenten, die HTTP sprechen, können sich mit einem lang laufenden `stackql mcp --mcp.server.type=http`-Prozess verbinden.

Für Claude Desktop - einschließlich des Eintrags im Anthropic Connector Directory (die empfohlene Installationsmethode), des herunterladbaren MCP Bundle (`.mcpb`) und der manuellen Konfiguration - siehe [StackQL mit Claude Desktop verwenden](/getting-started/claude-desktop).

:::note

Die Speicherorte der Konfigurationsdateien unterscheiden sich je nach Betriebssystem und KI-Assistent.  Den richtigen Pfad und das richtige Format entnehmen Sie der Dokumentation Ihres KI-Assistenten.

:::

* * *

### Architekturüberlegungen {#architecture-considerations}

Berücksichtigen Sie bei der Wahl eines Deployment-Modus:

1. **Eigenständig (`stackql mcp`)**: einfachste Einrichtung, ideal für die Entwicklung und für in Editoren eingebettete Clients über `stdio`.
2. **In-Memory-Doppelserver (`stackql srv` mit `mcp.server.type=http`)**: Ein einzelner Prozess bedient sowohl das MCP- als auch das PostgreSQL-Wire-Protokoll.  Beste Leistung, geeignet für die meisten Produktions-Deployments.
3. **Reverse-Proxy (`stackql srv` mit `mcp.server.type=reverse_proxy`)**:
   - ermöglicht die Trennung von Workloads über Prozesse oder Hosts hinweg
   - unterstützt TLS-Verschlüsselung für abgesicherte MCP-Endpunkte
   - erlaubt die unabhängige Skalierung der MCP- und PostgreSQL-Schnittstellen
   - bietet Flexibilität für die Integration von Unternehmens-CAs (Hinweis: Die Unterstützung von Unternehmens-CAs ist experimentell)

Und getrennt davon, zur Sicherheit:

- Beginnen Sie mit dem Standard `mode: safe`.  Elicitation-fähige Clients (Claude Desktop, Cursor, Continue) fragen den Benutzer vor jeder Mutation.
- Legen Sie `mode: read_only` für Inventar- oder Analyse-Agenten fest, die niemals schreiben sollen.
- Legen Sie `mode: delete_safe` fest, wenn Sie beim Anlegen und Aktualisieren freie Hand lassen, bei destruktiven Operationen aber einen Menschen einbeziehen möchten.
- Reservieren Sie `mode: full_access` für vertrauenswürdige Automatisierungs-Pipelines, und nur in Verbindung mit einem Audit-Log, das jemand prüft.
- Setzen Sie `server.auth_token_env_var` auf jedem HTTP-Listener, den andere Prozesse oder Hosts erreichen können; `allow_unauthenticated` ist für die lokale Entwicklung gedacht.  TLS allein authentifiziert keine Clients.

:::caution

Bei der Verwendung von TLS mit Zertifizierungsstellen (CAs) von Unternehmen kann zusätzliche Konfiguration erforderlich sein.  Diese Funktionalität ist experimentell und erfordert möglicherweise Anpassungen je nach Ihrer konkreten CA-Implementierung.

:::

* * *

### Weiterführende Informationen {#further-reading}

- Die Referenz für Paketentwickler ist die [`pkg/mcp_server`-README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) im stackql-Repository.
- Das technische Dokument für Mitwirkende liegt unter [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) im stackql-Repository und enthält ausgearbeitete Beispielantworten für jedes Tool.
