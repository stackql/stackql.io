---
title: Modos de salida
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Modos de salida de StackQL: table, json, jsonl, csv y otel, cómo seleccionarlos y qué emite cada uno para el uso con shell, exec y servidor."
image: "/img/stackql-featured-image.png"
---
Consulte también:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Los resultados que devuelven las consultas de StackQL se pueden presentar en formato tabular (table), así como en formato json, jsonl (JSON delimitado por saltos de línea), csv, text u otel (registros de log de OpenTelemetry). Los formatos JSON, JSONL y CSV sirven para intercambiar datos con otros programas; el formato `otel` envía un conjunto de resultados a un backend de OpenTelemetry como una instantánea con marca de tiempo. El formato de salida deseado se configura con la opción `--output` de StackQL.

### Formato de salida `table` {#table-output-format}
Los resultados se pueden presentar en formato de tabla con el valor `table` del parámetro `--output`, como se muestra a continuación (es el valor predeterminado del shell interactivo de StackQL):

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

### Salida `json` {#json-output}
Los resultados se pueden devolver en formato JSON con el valor `json` del parámetro `--output`, como se muestra a continuación; este formato de salida es útil cuando los resultados de una consulta deben pasarse a un proceso o script externo:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Salida `jsonl` {#jsonl-output}
Los resultados se pueden devolver en formato JSON delimitado por saltos de línea (JSON Lines) con el valor `jsonl` del parámetro `--output`, como se muestra a continuación (`ndjson` se acepta como alias). Cada fila se escribe como un único objeto JSON en su propia línea y las filas se vuelcan a la salida a medida que se escriben, por lo que este formato de salida es útil para transmitir resultados de forma continua a herramientas orientadas a líneas o para redirigir conjuntos de resultados grandes a otros programas:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Salida `csv` {#csv-output}
Los resultados se pueden devolver en formato CSV con el valor `csv` del parámetro `--output`, como se muestra a continuación; este formato de salida es útil para analizar los resultados en Excel o para proporcionar una interfaz de datos a otro sistema:

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

#### Ejemplo de salida CSV (con delimitador alternativo) {#csv-output-example-with-alternative-delimiter}
Se puede especificar un delimitador alternativo para el formato de salida `csv` con la opción `-d`, como se muestra a continuación:

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

### Salida `otel` {#otel-output}
Los resultados se pueden emitir como registros de log de OpenTelemetry con el valor `otel` del parámetro `--output`. Cada fila se escribe como un objeto `LogsData` de OTLP/JSON en su propia línea, que se vuelca a la salida a medida que se escribe, igual que con `jsonl`, y va seguida de un registro de finalización de la instrucción. Este formato de salida convierte una consulta en una instantánea de la configuración en la nube en forma de telemetría: ejecute una consulta de inventario de forma programada, envíe los registros a un backend compatible con OTLP (ClickHouse, Datadog o un OpenTelemetry Collector delante de cualquier otro) y responda allí a "qué aspecto tiene el parque ahora", "qué ha cambiado desde la última ejecución" y "quién ha tenido qué derecho de acceso, y desde cuándo". Disponible a partir de StackQL `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

Un registro de fila, con formato legible (el archivo contiene cada registro en una sola línea):

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

#### Instantáneas {#snapshots}

Una instrucción es una instantánea. Todos los registros de la instrucción llevan el mismo `timeUnixNano` (la hora de inicio de la instrucción), el mismo `stackql.snapshot.id` y el mismo `spanId`, de modo que las consultas a un momento dado, las comparaciones y las consultas del estado más reciente funcionan aunque las filas lleguen repartidas en varias páginas a lo largo de varios segundos; `observedTimeUnixNano` registra cuándo se escribió realmente cada registro. Todas las instrucciones de una invocación comparten un `traceId`, que se toma de la variable de entorno `TRACEPARENT` cuando está definida (la convención en los pipelines de CI) y se genera en caso contrario.

El último registro de una instrucción es el registro de finalización: cuerpo `snapshot complete <provider.service.resource>`, con `stackql.snapshot.complete=true`, `stackql.rows_returned` y `stackql.duration_ms`. Indica al backend que la instantánea está completa, y una instrucción que no devuelve filas lo emite igualmente, de modo que un recurso que ha desaparecido aparece como desviación y no como silencio. Una instrucción fallida produce un registro `ERROR` con `error.type` y `error.message` cuando la presentación de errores es `record`; de forma predeterminada, los errores van a stderr, como en todos los demás formatos de salida.

#### Forma del registro {#record-shape}

El cuerpo es la fila completa en JSON compacto, con los objetos anidados intactos, y es la fuente de verdad del documento completo. Los atributos llevan cada columna con su propio nombre, con tipo cuando el valor es un escalar (`stringValue`, `intValue`, `doubleValue`, `boolValue`); las columnas anidadas se transportan como cadenas JSON, lo que es portable entre backends cuyo esquema de atributos predeterminado es de valores de cadena, y las columnas nulas se omiten. Los atributos reservados `stackql.*` son:

| Atributo | Registro | Valor |
|--|--|--|
|`stackql.snapshot.id`|todos|UUID generado en cada ejecución de una instrucción|
|`stackql.query`|todos|El texto de la consulta enviada, literal|
|`stackql.query.hash`|todos|SHA-256 del texto de la consulta; una clave de serie más económica que el SQL para un trabajo recurrente|
|`stackql.provider`, `stackql.service`, `stackql.resource`|todos|La tabla principal de la instrucción; se omite en los joins y las vistas que abarcan varias tablas|
|`stackql.row.index`|fila|Posición en el conjunto de resultados, con base cero|
|`stackql.row.fingerprint`|fila|`sha256:` del JSON canónico de la fila, de modo que la desviación es una comparación por identidad en lugar de una comparación de todos los atributos|
|`stackql.snapshot.complete`|finalización|`true`|
|`stackql.rows_returned`|finalización|Número de filas de la instrucción|
|`stackql.duration_ms`|finalización|Duración en tiempo de reloj desde el inicio de la instrucción|

<br />

El conjunto de atributos emitido es una interfaz versionada: el ámbito de instrumentación es `github.com/stackql/stackql/internal/stackql/output` en la versión `1.0.0`, y `schemaUrl` fija las convenciones estables en `https://opentelemetry.io/schemas/1.44.0`.

No se infiere ninguna identidad de recurso: la huella digital es el valor y el backend elige la clave (`id`, `arn`, `selfLink` o una tupla de columnas). Para hacer un seguimiento de los derechos de acceso como entidades, desglose las vinculaciones en una fila por principal y rol en SQL; la telemetría sigue la granularidad del resultado.

#### Atributos de recurso {#resource-attributes}

El recurso lleva `service.name` (`stackql`, o el valor de `OTEL_SERVICE_NAME`) y `service.version`, las convenciones de nube de OpenTelemetry cuando se pueden derivar de un `SELECT` sobre una sola tabla (`cloud.provider` a partir del provider; `cloud.account.id` a partir de una igualdad de `project` o `subscriptionId` en la cláusula `WHERE`; `cloud.region` a partir de `region`; `cloud.availability_zone` a partir de `zone`), y todos los pares de la variable de entorno estándar `OTEL_RESOURCE_ATTRIBUTES`, de modo que un paso de un pipeline puede añadir `deployment.environment`, un nombre de trabajo o tags de cuenta sin opciones adicionales:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### Enviar los registros {#shipping-the-records}

El archivo contiene un `LogsData` por línea, la forma que el receptor `otlp_json_file` del OpenTelemetry Collector ingiere sin ningún procesador de transformación:

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

Cada línea es además, byte por byte, el cuerpo de una solicitud OTLP/HTTP `POST /v1/logs`, que es lo que envía el exportador push integrado.

#### Enviar a un endpoint OTLP {#pushing-to-an-otlp-endpoint}

Los registros se pueden enviar directamente a un endpoint de logs OTLP/HTTP, además de escribirse en el archivo o en stdout, configurando un exportador en la opción `--otel.config`, una cadena JSON o YAML como las demás opciones de configuración (disponible a partir de StackQL `v0.12.732`):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\'
```

| Clave | Finalidad |
|--|--|
|`exporter.endpoint`|URL completa del endpoint de logs, por ejemplo `http://localhost:4318/v1/logs`; obligatoria para habilitar el exportador|
|`exporter.headers`|Encabezados de solicitud que se envían con cada exportación, normalmente un token de ingesta o una clave de API|
|`exporter.timeout_ms`|Tiempo de espera de la solicitud en milisegundos, valor predeterminado `10000`|
|`exporter.batch_size`|Registros por solicitud, valor predeterminado `512`|

<br />

Cada instrucción es un lote: sus registros de fila y su registro de finalización se envían como un único `LogsData` de OTLP/JSON (un solo envoltorio de recurso y ámbito, una entrada en `logRecords` por registro), y un conjunto de resultados grande se divide en solicitudes de `batch_size` registros como máximo a medida que llegan las filas, con la solicitud final después del registro de finalización. Las respuestas `429` y `5xx` y los fallos de conexión se reintentan hasta cuatro intentos con una espera que se duplica a partir de 500ms, respetando un encabezado `Retry-After` expresado en segundos (con un máximo de 30 segundos) cuando el servidor lo envía; los demás errores no se reintentan. El archivo o el flujo se escribe en cualquier caso, de modo que un envío fallido deja una copia local y una línea de error en stderr. El exportador usa OTLP/JSON sobre HTTP; un endpoint que solo acepta protobuf necesita un Collector delante.

No se lee ninguna variable de entorno `OTEL_EXPORTER_OTLP_*`; toda la configuración del exportador procede de `--otel.config`. Un valor de `--otel.config` mal formado, o un objeto `exporter` sin `endpoint`, hace que el inicio falle.

Esto está pensado para la entrada OTLP de ClickStack (`http://<host>:4318/v1/logs`, encabezado `authorization: <ingestion API key>`), el receptor OTLP de un Datadog Agent o la entrada OTLP de Datadog (`dd-api-key: <key>`), y cualquier receptor `otlp` de un OpenTelemetry Collector. Consulte [Recetas para backends de OTel](/getting-started/otel-recipes) para ver las consultas de inventario, desviación y derechos de acceso en el lado receptor.
