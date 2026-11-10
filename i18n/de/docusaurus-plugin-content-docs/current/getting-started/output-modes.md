---
title: Ausgabemodi
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud-inventar
description: "StackQL-Ausgabemodi: table, json, jsonl, csv und otel, wie Sie sie auswählen und was jeder für Shell, exec und Server ausgibt."
image: "/img/stackql-featured-image.png"
---
Siehe auch:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Die von StackQL-Abfragen zurückgegebenen Ergebnisse können tabellarisch (table) sowie im Format json, jsonl (newline delimited JSON), csv, text oder otel (OpenTelemetry-Log-Records) formatiert werden. Die Formate JSON, JSONL und CSV eignen sich zum Datenaustausch mit anderen Programmen; das Format `otel` liefert eine Ergebnismenge als Snapshot mit Zeitstempel an ein OpenTelemetry-Backend. Das gewünschte Ausgabeformat wird mit dem StackQL-Flag `--output` konfiguriert.

### Ausgabeformat `table` {#table-output-format}
Ergebnisse können mit dem Wert `table` für den Parameter `--output` wie unten gezeigt als Tabelle formatiert werden (dies ist der Standardwert für die interaktive StackQL-Shell):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output table

|---------------------|-----------------|
|         ID          |      NAME       |
|---------------------|-----------------|
| 1257085253691867467 | demo-instance-1 |
|---------------------|-----------------|
| 2586731219281477960 | demo-instance-2 |
|---------------------|-----------------|
| 5584456226809282885 | demo-instance-3 |
|---------------------|-----------------|
```

### Ausgabe `json` {#json-output}
Ergebnisse können mit dem Wert `json` für den Parameter `--output` wie unten gezeigt im JSON-Format zurückgegeben werden; dieses Ausgabeformat ist nützlich, wenn die Ergebnisse einer Abfrage an einen externen Prozess oder ein Skript übergeben werden sollen:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Ausgabe `jsonl` {#jsonl-output}
Ergebnisse können mit dem Wert `jsonl` für den Parameter `--output` wie unten gezeigt im Format newline delimited JSON (JSON Lines) zurückgegeben werden (`ndjson` wird als Alias akzeptiert). Jede Zeile wird als einzelnes JSON-Objekt in eine eigene Zeile geschrieben, und die Zeilen werden beim Schreiben sofort geflusht, was dieses Ausgabeformat nützlich macht, um Ergebnisse an zeilenorientierte Tools zu streamen oder große Ergebnismengen per Pipe an andere Programme zu übergeben:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Ausgabe `csv` {#csv-output}
Ergebnisse können mit dem Wert `csv` für den Parameter `--output` wie unten gezeigt im CSV-Format zurückgegeben werden; dieses Ausgabeformat ist nützlich, um Ergebnisse in Excel auszuwerten oder eine Datenschnittstelle zu einem anderen System bereitzustellen:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### Beispiel für die CSV-Ausgabe (mit alternativem Trennzeichen) {#csv-output-example-with-alternative-delimiter}
Für das Ausgabeformat `csv` kann mit dem Schalter `-d` wie unten gezeigt ein alternatives Trennzeichen angegeben werden:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```

### Ausgabe `otel` {#otel-output}
Ergebnisse können mit dem Wert `otel` für den Parameter `--output` als OpenTelemetry-Log-Records ausgegeben werden. Jede Zeile wird als ein OTLP/JSON-`LogsData`-Objekt in eine eigene Zeile geschrieben und wie bei `jsonl` beim Schreiben sofort geflusht, gefolgt von einem Abschluss-Record für die Anweisung. Dieses Ausgabeformat macht aus einer Abfrage einen Snapshot der Cloud-Konfiguration als Telemetrie: Führen Sie eine Inventarabfrage nach Zeitplan aus, senden Sie die Records an ein OTLP-fähiges Backend (ClickHouse, Datadog oder ein OpenTelemetry Collector vor beliebigen anderen Systemen) und beantworten Sie dort die Fragen "Wie sieht der Bestand jetzt aus", "Was hat sich seit dem letzten Lauf geändert" und "Wer hatte welche Berechtigung, und seit wann". Verfügbar in StackQL-Releases ab `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

Ein Zeilen-Record, formatiert ausgegeben (die Datei enthält jeden Record in einer Zeile):

```json
{"resourceLogs":[{"resource":{"attributes":[
  {"key":"service.name","value":{"stringValue":"stackql"}},
  {"key":"service.version","value":{"stringValue":"0.11.660"}},
  {"key":"cloud.provider","value":{"stringValue":"gcp"}},
  {"key":"cloud.account.id","value":{"stringValue":"stackql-demo"}},
  {"key":"cloud.availability_zone","value":{"stringValue":"australia-southeast1-a"}}
]},"scopeLogs":[{"scope":{"name":"github.com/stackql/stackql/internal/stackql/output","version":"1.0.0"},
"logRecords":[{
  "timeUnixNano":"1788739200000000000","observedTimeUnixNano":"1788739203412000000",
  "severityNumber":9,"severityText":"INFO",
  "body":{"stringValue":"{\"id\":\"1257085253691867467\",\"name\":\"demo-instance-1\"}"},
  "attributes":[
    {"key":"id","value":{"stringValue":"1257085253691867467"}},
    {"key":"name","value":{"stringValue":"demo-instance-1"}},
    {"key":"stackql.snapshot.id","value":{"stringValue":"1e0d4a6c-4b0f-4b4e-9f7d-2a6c9a1f3b21"}},
    {"key":"stackql.query","value":{"stringValue":"select id, name from google.compute.instances where project = 'stackql-demo' and zone = 'australia-southeast1-a'"}},
    {"key":"stackql.query.hash","value":{"stringValue":"9c1d2e..."}},
    {"key":"stackql.provider","value":{"stringValue":"google"}},
    {"key":"stackql.service","value":{"stringValue":"compute"}},
    {"key":"stackql.resource","value":{"stringValue":"instances"}},
    {"key":"stackql.row.index","value":{"intValue":"0"}},
    {"key":"stackql.row.fingerprint","value":{"stringValue":"sha256:7f3a..."}}
  ],
  "traceId":"4bf92f3577b34da6a3ce929d0e0e4736","spanId":"a1b2c3d4e5f60718"
}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}]}
```

#### Snapshots {#snapshots}

Eine Anweisung ist ein Snapshot. Jeder Record der Anweisung trägt dasselbe `timeUnixNano` (die Startzeit der Anweisung), dieselbe `stackql.snapshot.id` und dieselbe `spanId`, sodass As-of-Abfragen, Diffs und Abfragen des letzten Zustands funktionieren, obwohl die Zeilen über mehrere Seiten hinweg über mehrere Sekunden eintreffen; `observedTimeUnixNano` hält fest, wann jeder Record tatsächlich geschrieben wurde. Alle Anweisungen eines Aufrufs teilen sich eine `traceId`, die aus einer Umgebungsvariablen `TRACEPARENT` übernommen wird, wenn eine gesetzt ist (die Konvention für CI-Pipelines), und andernfalls generiert wird.

Der letzte Record einer Anweisung ist der Abschluss-Record: Body `snapshot complete <provider.service.resource>`, mit `stackql.snapshot.complete=true`, `stackql.rows_returned` und `stackql.duration_ms`. Er teilt dem Backend mit, dass der Snapshot vollständig ist, und auch eine Anweisung, die keine Zeilen zurückgibt, gibt ihn aus, sodass eine verschwundene Ressource als Drift erscheint und nicht als Stille. Eine fehlgeschlagene Anweisung erzeugt einen `ERROR`-Record mit `error.type` und `error.message`, wenn die Fehlerdarstellung `record` ist; standardmäßig gehen Fehler wie bei jedem anderen Ausgabeformat nach stderr.

#### Aufbau der Records {#record-shape}

Der Body ist die vollständige Zeile als kompaktes JSON mit intakten verschachtelten Objekten und ist die maßgebliche Quelle für das vollständige Dokument. Die Attribute führen jede Spalte unter ihrem eigenen Namen, typisiert, wo der Wert ein Skalar ist (`stringValue`, `intValue`, `doubleValue`, `boolValue`); verschachtelte Spalten werden als JSON-Strings übertragen, was über Backends hinweg portabel ist, deren Standard-Attributschema stringbasiert ist, und Null-Spalten werden weggelassen. Die reservierten `stackql.*`-Attribute sind:

| Attribut | Record | Wert |
|--|--|--|
|`stackql.snapshot.id`|alle|UUID, die pro Anweisungsausführung generiert wird|
|`stackql.query`|alle|Der übermittelte Abfragetext, wörtlich|
|`stackql.query.hash`|alle|SHA-256 des Abfragetexts; ein günstigerer Serienschlüssel als das SQL für einen wiederkehrenden Job|
|`stackql.provider`, `stackql.service`, `stackql.resource`|alle|Die primäre Tabelle der Anweisung; entfällt bei Joins und Views, die mehrere Tabellen umfassen|
|`stackql.row.index`|Zeile|Nullbasierte Position in der Ergebnismenge|
|`stackql.row.fingerprint`|Zeile|`sha256:` des kanonischen Zeilen-JSON, sodass Drift ein Vergleich pro Identität ist statt eines Vergleichs über jedes Attribut|
|`stackql.snapshot.complete`|Abschluss|`true`|
|`stackql.rows_returned`|Abschluss|Zeilenanzahl der Anweisung|
|`stackql.duration_ms`|Abschluss|Tatsächlich verstrichene Zeit seit Anweisungsstart|

<br />

Die ausgegebene Attributmenge ist eine versionierte Schnittstelle: Der Instrumentation Scope ist `github.com/stackql/stackql/internal/stackql/output` in Version `1.0.0`, und `schemaUrl` legt die stabilen Konventionen auf `https://opentelemetry.io/schemas/1.44.0` fest.

Es wird keine Ressourcenidentität abgeleitet: Der Fingerprint ist der Wert, und das Backend wählt den Schlüssel (`id`, `arn`, `selfLink` oder ein Tupel von Spalten). Um Berechtigungen als Entitäten zu verfolgen, lösen Sie Bindings in SQL in eine Zeile pro Principal und Rolle auf; die Telemetrie folgt der Granularität des Ergebnisses.

#### Ressourcenattribute {#resource-attributes}

Die Ressource trägt `service.name` (`stackql` oder den Wert von `OTEL_SERVICE_NAME`) und `service.version`, die Cloud-Konventionen von OpenTelemetry, soweit sie aus einem `SELECT` auf eine einzelne Tabelle abgeleitet werden können (`cloud.provider` aus dem Provider; `cloud.account.id` aus einer Gleichheitsbedingung auf `project` oder `subscriptionId` in der `WHERE`-Klausel; `cloud.region` aus `region`; `cloud.availability_zone` aus `zone`), sowie jedes Paar aus der Standard-Umgebungsvariablen `OTEL_RESOURCE_ATTRIBUTES`, sodass ein Pipeline-Schritt `deployment.environment`, einen Jobnamen oder Konto-Tags ohne zusätzliche Flags setzen kann:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### Versand der Records {#shipping-the-records}

Die Datei enthält ein `LogsData` pro Zeile, die Form, die der `otlp_json_file`-Receiver des OpenTelemetry Collectors ohne Transform-Prozessor einliest:

```yaml
receivers:
  otlp_json_file:
    include: [/var/lib/stackql/inventory/*.otlp.jsonl]
exporters:
  otlphttp:
    endpoint: https://otlp.example.com
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [otlphttp]
```

Jede Zeile ist außerdem Byte für Byte der Body einer OTLP/HTTP-Anfrage `POST /v1/logs`, also das, was der eingebaute Push-Exporter sendet.

#### Push an einen OTLP-Endpunkt {#pushing-to-an-otlp-endpoint}

Die Records können zusätzlich zum Schreiben in die Datei oder nach stdout direkt an einen OTLP/HTTP-Logs-Endpunkt gepusht werden, indem im Flag `--otel.config` ein Exporter konfiguriert wird, ein JSON- oder YAML-String wie bei den anderen Konfigurations-Flags (verfügbar in StackQL-Releases ab `v0.12.732`):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\'
```

| Schlüssel | Zweck |
|--|--|
|`exporter.endpoint`|Vollständige URL des Logs-Endpunkts, zum Beispiel `http://localhost:4318/v1/logs`; erforderlich, um den Exporter zu aktivieren|
|`exporter.headers`|Request-Header, die mit jedem Export gesendet werden, typischerweise ein Ingestion-Token oder API-Schlüssel|
|`exporter.timeout_ms`|Request-Timeout in Millisekunden, Standard `10000`|
|`exporter.batch_size`|Records pro Request, Standard `512`|

<br />

Jede Anweisung ist ein Batch: Ihre Zeilen-Records und ihr Abschluss-Record werden als ein einziges OTLP/JSON-`LogsData` gesendet (eine Ressourcen- und Scope-Hülle, ein Eintrag in `logRecords` pro Record), und eine große Ergebnismenge wird beim Streamen der Zeilen in Requests mit höchstens `batch_size` Records aufgeteilt, wobei der letzte Request auf den Abschluss-Record folgt. Bei `429`- und `5xx`-Antworten sowie Verbindungsfehlern wird bis zu viermal mit einem sich verdoppelnden Backoff ab 500ms erneut versucht, wobei ein `Retry-After`-Header in Sekunden (begrenzt auf 30 Sekunden) berücksichtigt wird, wenn der Server einen sendet; andere Fehler werden nicht wiederholt. Die Datei oder der Stream wird in jedem Fall geschrieben, sodass ein fehlgeschlagener Push eine lokale Kopie und eine Fehlerzeile auf stderr hinterlässt. Der Exporter spricht OTLP/JSON über HTTP; ein Endpunkt, der nur Protobuf akzeptiert, benötigt einen vorgeschalteten Collector.

Es werden keine `OTEL_EXPORTER_OTLP_*`-Umgebungsvariablen gelesen; jede Exporter-Einstellung stammt aus `--otel.config`. Ein fehlerhafter `--otel.config`-Wert oder ein `exporter`-Objekt ohne `endpoint` lässt den Start fehlschlagen.

Das zielt auf den OTLP-Intake von ClickStack (`http://<host>:4318/v1/logs`, Header `authorization: <ingestion API key>`), den OTLP-Receiver eines Datadog Agents oder den Datadog-OTLP-Intake (`dd-api-key: <key>`) sowie jeden `otlp`-Receiver eines OpenTelemetry Collectors. Siehe [OTel-Backend-Rezepte](/getting-started/otel-recipes) für die Inventar-, Drift- und Berechtigungsabfragen auf der Empfängerseite.
