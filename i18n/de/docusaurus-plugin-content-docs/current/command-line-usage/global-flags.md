---
title: Globale Flags
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Globale Flags der stackql-Binärdatei: Einstellungen für Authentifizierung, Ausgabeformat, Registry-Speicherort, Logging und SQL-Backend, verfügbar bei jedem Befehl."
image: "/img/stackql-featured-image.png"
---

Globale Flags legen das Laufzeitverhalten der StackQL-Anwendung fest; diese Flags werden auf der Kommandozeile beim Start der StackQL-Anwendung gesetzt (entweder im interaktiven Shell-Modus oder im nicht interaktiven exec-Modus) oder aus der StackQL-Initialisierungsdatei ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)) gelesen.  Die Flags umfassen:  

| Flag | Datentyp | Beschreibung | Standard |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Konfiguration des Aufruferzugriffs, ein JSON-Envelope `{"path": "/path/to/access.json"}` (ein bloßer Dateipfad wird ebenfalls akzeptiert).  Aktiviert die [Zugriffsverwaltung](/command-line-usage/access-management): Aufrufer werden per JWT, statischem Bearer-Token, Mutual TLS oder lokaler OS-Identität authentifiziert und Anweisungen anhand von Policy-Regeln autorisiert.  Lokale Token-Anmeldedaten werden aus `STACKQL_CALLER_TOKEN` gelesen.  Verfügbar in StackQL-Releases ab `v1.0`. | |
| <span class="nowrap">`--acid`</span> | string | JSON-/YAML-String mit der ACID-Konfiguration | `{}` |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Erlaubt einer Sitzung, mit `SET "$.secret_store"` einen persistenten OS-Secret-Store (macOS Keychain, Linux Secret Service oder Windows Credential Manager) auszuwählen.  Flat-File-Stores werden auch mit dem Flag verweigert.  Siehe [Provider-Sitzungen und Secrets](/getting-started/provider-sessions#persistent-os-secret-stores).  Verfügbar in StackQL-Releases ab `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Erlaubt das Dereferenzieren von Secret- und Anmeldedatenwerten in SQL-Ausdrücken (`"$secrets.name"`, `"$auth.provider.field"`), mit der Folge, dass sie in Ergebnisse, Logs und persistierte Abfrageartefakte gelangen können.  Für die normale Provider-Authentifizierung nicht erforderlich.  Siehe [Secret-Werte in SQL](/getting-started/provider-sessions#secret-values-in-sql).  Verfügbar in StackQL-Releases ab `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Timeout für API-Anfragen in Sekunden, 0 für kein Timeout | `45` |
| <span class="nowrap">`--approot`</span> | string | Wurzelpfad für Anwendungskonfiguration und Cache | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Dateimodus für Anwendungskonfiguration und Cache | `493` |
| <span class="nowrap">`--auth`</span> | string | `auth`-Kontext-Schlüssel-Wert-Paare in json-Form |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Anfängliche Schlüsselanzahl des Caches | `100` |
| <span class="nowrap">`--configfile`</span> | string | Vollständiger Pfad der Konfigurationsdatei | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile`-Datei, keine wenn leer | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Maximale Tiefe der Datenflussabhängigkeiten für eine Abfrage | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | Maximale Anzahl schwach zusammenhängender Datenflusskomponenten für eine Abfrage | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | JSON-/YAML-String zur Konfiguration der Behandlung von DBMS-Housekeeping-Abfragen | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Trennzeichen für die csv-Ausgabe. Nur ein einzelnes Zeichen.  Wird bei jeder Nicht-csv-Ausgabe ignoriert | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun`-Flag; nur den Präprozessor ausführen, die templatierte Ausgabe wird zurückgegeben | `false` |
| <span class="nowrap">`--env.file`</span> | string | Optionale Datei im dotenv-Stil, die beim Start in die Prozessumgebung eingelesen wird (alle Befehle); das MCP-Tool [`reload_credentials`](/mcp/reload_credentials) liest sie bei Bedarf erneut ein. Eine fehlende Datei wird toleriert; Schlüssel mit leeren Werten werden übersprungen; vorhandene Variablen werden überschrieben. Wird nicht aus `.stackqlrc` gelesen. Verfügbar in StackQL-Releases nach `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Parallelitätslimit für die Abfrageausführung | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Präfix für Export-Aliase (Namespace oder Schema) | |
| <span class="nowrap">`--gc`</span> | string | JSON-/YAML-String mit der GC-Konfiguration | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Deaktiviert Spaltenüberschriften, nur gültig mit `--output csv` oder `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Zeigt http-Anfrageinformationen im Terminal an | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Proxy-Host (leer lassen für keinen Proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Proxy-Passwort | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Proxy-Port, jede Zahl kleiner oder gleich 0 führt zum Standardport des jeweiligen Schemas (z. B. `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Proxy-Schema, `http` oder `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Proxy-Benutzer | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Maximale Ergebnisse pro http-Anfrage, jede Zahl kleiner oder gleich 0 bedeutet keine Begrenzung | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Maximale Anzahl von Ergebnisseiten, die pro Ressource zurückgegeben werden, jede Zahl kleiner oder gleich 0 bedeutet keine Begrenzung | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Maximale Tiefe für indirekte Abfragen: Views und Unterabfragen | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Eingabedatei (IQL-Datei), aus der Abfragen gelesen werden | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Kontextdatei (Daten) für das Templating (`json`- oder `jsonnet`-Datei)| |
| <span class="nowrap">`--loglevel`</span> | string | Log-Level, muss einer von {`info`, `warn`, `debug`, `fatal`} sein | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | JSON-/YAML-Konfigurationsobjekt für den [MCP-Server](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` und `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Kodierung des MCP-Audit-Logs, `jsonl` oder `otel` (OTLP/JSON-Log-Records); überschreibt `server.audit.format` in `--mcp.config`.  Siehe [Log-Format](/command-line-usage/mcp#log-format).  Verfügbar in StackQL-Releases ab `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Neueste angekündigte MCP-Protokollrevision, `auto`, `2026-07-28` (nur sitzungslos) oder eine ältere Revision wie `2025-11-25`; überschreibt `server.protocol_version` in `--mcp.config`.  Siehe [Revision festlegen](/command-line-usage/mcp#pinning-the-revision).  Verfügbar in StackQL-Releases ab `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP-Servertyp, einer von {`http`, `stdio`, `reverse_proxy`}.  Siehe [MCP-Servertyp](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL für zwischengespeicherte Metadatendokumente, in Sekunden | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | JSON-/YAML-String mit Namespaces für Caching, Views usw. | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Offline arbeiten, mit zwischengespeicherten Daten | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Ausgabedatei, in die Ergebnisse geschrieben werden | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Ausgabeformat, muss eines von {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} sein (`ndjson` wird als Alias für `jsonl` akzeptiert).  `otel` gibt OpenTelemetry-Log-Records aus, siehe [Ausgabemodi](/getting-started/output-modes#otel-output); verfügbar in StackQL-Releases ab `v0.11.660`.  Ist in `--otel.config` ein Exporter konfiguriert, werden `otel`-Records zusätzlich an diesen Endpunkt gesendet | `table` |
| <span class="nowrap">`--otel.config`</span> | string | JSON-/YAML-String zur Konfiguration des Ausgabeformats `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` und `exporter.batch_size` senden die Records an einen OTLP/HTTP-Logs-Endpunkt, siehe [An einen OTLP-Endpunkt senden](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Verfügbar in StackQL-Releases ab `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | JSON-String zur Aktivierung von Preview-Funktionen.  `{"unstable": true}` stellt die dokumentgetriebenen Relationen `stackql_unstable_<provider>.<service>.<resource>` bereit, die von `omnisdk` direkt aus den Provider-Dokumenten aufgelöst und ausgeführt werden, wobei Zeilen gestreamt werden, sobald sie entstehen; die übrigen Schlüssel (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) stimmen dieses Backend ab.  Preview-Funktionen sind nicht stabil: Verhalten, Syntax und Ausgabe können sich zwischen Releases ohne Ankündigung ändern oder brechen, verlassen Sie sich daher in der Produktion nicht darauf.  Ab `v1.0` stellt dasselbe Opt-in die Relationen `stackql_preview.iac.run` und `stackql_preview.iac.destroy` bereit, die eine deklarierte Ressourcenmenge über `omnisdk` anwenden und abbauen, wobei der Zustand unter dem Anwendungswurzelpfad gehalten wird.  Siehe [Preview-Funktionen](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--querycachesize`</span> | integer | Größe des LRU-Caches für Abfragepläne in Anzahl der Einträge | `10000` |
| <span class="nowrap">`--registry`</span> | string | JSON-/YAML-String zur Konfiguration des Speicherorts der Provider-Registry und der Signaturprüfung von Provider-Dokumenten | siehe [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | JSON-/YAML-String mit der Sitzungskonfiguration | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | JSON-/YAML-String mit der Systemkonfiguration des SQL-Backends | siehe [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | JSON-/YAML-String mit der Konfiguration des Txn-Stores | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Pfad zum CA-Bundle, ohne Angabe werden die Systemstandards verwendet |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Erlaubt das Vertrauen in unsichere Zertifikate (nicht empfohlen) |  |
| <span class="nowrap">`--var`</span> | string | Externe Variablen als Schlüssel/Wert-Paare, die über `jsonnet`-Datendateien in StackQL-Abfragen eingelesen werden |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Ausführliche Ausgabe | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Kontextspezifische Hilfe für stackql | |
| <span class="nowrap">`--version`</span> | | Zeigt die Version des StackQL-Programms an | |

## `--registry` {#--registry}

Das Flag `--registry` konfiguriert, woher Provider-Dokumente gelesen werden und ob ihre Signaturen geprüft werden.  Der Wert ist ein JSON-String mit den folgenden Schlüsseln:

| Schlüssel | Beschreibung | Standard |
| -- | -- | -- |
| `url` | Registry-URL.  Ohne Angabe wird die öffentliche StackQL-Provider-Registry verwendet. | öffentliche Registry |
| `localDocRoot` | Ein lokales Verzeichnis mit Provider-Dokumenten, das anstelle eines Downloads aus der Registry gelesen wird.  Wird beim [Entwickeln eines Providers](/developers/developing-stackql-providers#testing-a-provider-locally) verwendet. | |
| `srcPrefix` | Pfadpräfix unterhalb der Registry-Wurzel, in dem die Provider-Quelldokumente liegen. | `src` |
| `verifyConfig.nopVerify` | `true` überspringt die Signaturprüfung.  Auf `false` setzen, um für jedes gelesene Provider- und Service-Dokument eine eingebettete ML-DSA-65-Signatur (FIPS 204) zu verlangen, einschließlich der dokumentgestützten Preview-Relationen. | `true` |
| `verifyConfig.publicKeys` | Ein JSON-Objekt, das Schlüssel-IDs auf hex-kodierte öffentliche ML-DSA-65-Schlüssel abbildet, für private Registries.  Es ersetzt die eingebetteten Produktionsschlüssel; ein explizit leeres Objekt ist ein Fehler. | eingebettete Schlüssel |

### Beispiel: Signierte Provider-Dokumente verlangen {#example-require-signed-provider-documents}

Schaltet die Prüfung für jedes aus der öffentlichen Registry gelesene Provider- und Service-Dokument ein, geprüft gegen die eingebetteten Produktionsschlüssel:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Beispiel: Unsignierte Dokumente aus einem lokalen Verzeichnis lesen {#example-read-unsigned-documents-from-a-local-directory}

Verweist die Registry auf ein Verzeichnis mit Provider-Dokumenten in Entwicklung.  Diese Dokumente sind unsigniert, daher bleibt die Prüfung ausgeschaltet:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Die Signaturprüfung ist in StackQL-Releases ab `v1.0` verfügbar und standardmäßig ausgeschaltet.  Signaturen decken die exakten YAML-Bytes eines Dokuments ab, sodass jede Änderung, auch eine Formatierungs- oder Zeilenumbruchänderung, sie ungültig macht; signieren Sie mit dem Tool `pqsig sign-doc` der Registry neu.  Unsignierte lokale Entwicklungsdokumente müssen `nopVerify: true` beibehalten.  Die früheren Zertifikatsoptionen und `.sig`-Sidecar-Dateien werden nicht mehr verwendet, und Archive, die nur mit Sidecars veröffentlicht wurden, müssen vor einer geprüften Verwendung neu veröffentlicht werden.  Die Prüfung authentifiziert Dokumente beim Lesen; Download und Entpacken von Archiven sind nicht abgedeckt.

:::

## `--sqlBackend` {#--sqlbackend}

Das Flag `--sqlBackend` konfiguriert ein externes SQL-Backend für StackQL, sodass Sie anstelle der standardmäßig eingebetteten SQL-Engine eine externe Datenbank-Engine (etwa PostgreSQL) verwenden können. Das ist nützlich, um eine externe Datenbank für die Abfrageverarbeitung, persistente Speicherung oder die Integration in bestehende Datenbankinfrastruktur zu nutzen.

Der Wert ist ein JSON-String mit den folgenden Schlüsseln:

| Schlüssel | Beschreibung | Beispielwerte |
| -- | -- | -- |
| `dbEngine` | Der Typ der Datenbank-Engine | `postgres_tcp` |
| `sqlDialect` | Der zu verwendende SQL-Dialekt | `postgres` |
| `dsn` | Der Data Source Name (Verbindungszeichenfolge) der Datenbank | Siehe Beispiele unten |

### Beispiel: Ein PostgreSQL-Backend verwenden {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Beispiel: Die PostgreSQL-Schnittstelle von Databricks verwenden {#example-using-databricks-postgresql-interface}

Databricks stellt eine PostgreSQL-kompatible Schnittstelle bereit, die als StackQL-SQL-Backend verwendet werden kann. Der DSN verwendet das Standardformat der PostgreSQL-Verbindungszeichenfolge mit Ihren Databricks-Anmeldedaten:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Sonderzeichen im Benutzernamen oder Passwort (etwa `@`) müssen im DSN URL-kodiert sein (z. B. wird aus `@` `%40`).
:::
