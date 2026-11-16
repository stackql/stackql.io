---
title: Modos de saída
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Modos de saída do StackQL: table, json, jsonl, csv e otel, como selecioná-los e o que cada um emite no uso com shell, exec e servidor."
image: "/img/stackql-featured-image.png"
---
Veja também:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Os resultados retornados pelas consultas do StackQL podem ser formatados em formato tabular (table), bem como nos formatos json, jsonl (JSON delimitado por quebras de linha), csv, text ou otel (registros de log do OpenTelemetry).  Os formatos JSON, JSONL e CSV podem ser usados para trocar dados com outros programas; o formato `otel` envia um conjunto de resultados a um backend do OpenTelemetry como um snapshot com carimbo de data/hora.  O formato de saída desejado é configurado com a opção `--output` do StackQL.

### Formato de saída `table` {#table-output-format}
Os resultados podem ser formatados como tabela com o valor `table` do parâmetro `--output`, como mostrado abaixo (este é o valor padrão do shell interativo do StackQL):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output table

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

### Saída `json` {#json-output}
Os resultados podem ser retornados em formato JSON com o valor `json` do parâmetro `--output`, como mostrado abaixo; este formato de saída é útil quando os resultados de uma consulta precisam ser passados para um processo ou script externo:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Saída `jsonl` {#jsonl-output}
Os resultados podem ser retornados em formato JSON delimitado por quebras de linha (JSON Lines) com o valor `jsonl` do parâmetro `--output`, como mostrado abaixo (`ndjson` é aceito como alias).  Cada linha do resultado é gravada como um único objeto JSON em uma linha própria, e as linhas são liberadas (flush) à medida que são gravadas, o que torna este formato de saída útil para transmitir resultados em fluxo contínuo a ferramentas orientadas a linhas ou para encaminhar grandes conjuntos de resultados a outros programas por pipe:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Saída `csv` {#csv-output}
Os resultados podem ser retornados em formato CSV com o valor `csv` do parâmetro `--output`, como mostrado abaixo; este formato de saída é útil para analisar resultados no Excel ou fornecer uma interface de dados a outro sistema:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### Exemplo de saída csv (com delimitador alternativo) {#csv-output-example-with-alternative-delimiter}
Um delimitador alternativo pode ser especificado para o formato de saída `csv` com a opção `-d`, como mostrado abaixo:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```

### Saída `text` {#text-output}
Os resultados podem ser retornados como texto simples com o valor `text` do parâmetro `--output`.  Cada linha do resultado é gravada como os seus valores unidos por vírgulas, precedida de uma linha de cabeçalho, a menos que a opção `-H` (`--hideheaders`) esteja definida.  Diferentemente do `csv`, os valores não são colocados entre aspas nem escapados e o delimitador `-d` é ignorado; por isso, um valor que contém uma vírgula ou aspas duplas é gravado como está:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

Com `--output csv`, a mesma consulta retorna `"a,b","say ""hi"""` como linha de dados.

### Saída `otel` {#otel-output}
Os resultados podem ser emitidos como registros de log do OpenTelemetry com o valor `otel` do parâmetro `--output`.  Cada linha do resultado é gravada como um objeto OTLP/JSON `LogsData` em uma linha própria, liberada (flush) assim que é gravada, como em `jsonl`, seguida de um registro de conclusão da instrução.  Este formato de saída transforma uma consulta em um snapshot da configuração na nuvem na forma de telemetria: execute uma consulta de inventário de forma agendada, envie os registros a um backend compatível com OTLP (ClickHouse, Datadog ou um OpenTelemetry Collector na frente de qualquer outro) e responda ali a "como está o parque agora", "o que mudou desde a última execução" e "quem teve qual direito de acesso, e desde quando".  Disponível a partir do StackQL `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

Um registro de linha, formatado para leitura (o arquivo contém cada registro em uma única linha):

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

Uma instrução é um snapshot.  Todos os registros da instrução têm o mesmo `timeUnixNano` (o horário de início da instrução), o mesmo `stackql.snapshot.id` e o mesmo `spanId`, de modo que consultas do estado em um momento dado, comparações e consultas do estado mais recente funcionam mesmo que as linhas cheguem em várias páginas ao longo de vários segundos; `observedTimeUnixNano` registra quando cada registro foi efetivamente gravado.  Todas as instruções de uma invocação compartilham um `traceId`, obtido da variável de ambiente `TRACEPARENT` quando ela está definida (a convenção para pipelines de CI) e gerado nos demais casos.

O último registro de uma instrução é o registro de conclusão: corpo `snapshot complete <provider.service.resource>`, com `stackql.snapshot.complete=true`, `stackql.rows_returned` e `stackql.duration_ms`.  Ele informa ao backend que o snapshot está completo, e uma instrução que não retorna linhas também o emite, de modo que um recurso que desapareceu aparece como desvio, e não como silêncio.  Uma instrução com falha produz um registro `ERROR` com `error.type` e `error.message` quando a apresentação de erros é `record`; por padrão, os erros vão para stderr, como em todos os outros formatos de saída.

#### Formato do registro {#record-shape}

O corpo é a linha completa em JSON compacto, com os objetos aninhados intactos, e é a fonte da verdade do documento completo.  Os atributos contêm cada coluna com o próprio nome, tipados quando o valor é escalar (`stringValue`, `intValue`, `doubleValue`, `boolValue`); as colunas aninhadas são transportadas como strings JSON, o que é portável entre backends cujo esquema de atributos padrão usa valores string, e as colunas nulas são omitidas.  Os atributos reservados `stackql.*` são:

| Atributo | Registro | Valor |
|--|--|--|
|`stackql.snapshot.id`|todos|UUID gerado a cada execução da instrução|
|`stackql.query`|todos|O texto da consulta enviada, sem alterações|
|`stackql.query.hash`|todos|SHA-256 do texto da consulta; uma chave de série mais barata que o SQL para um job recorrente|
|`stackql.provider`, `stackql.service`, `stackql.resource`|todos|A tabela principal da instrução; omitidos em joins e views que abrangem várias tabelas|
|`stackql.row.index`|linha|Posição no conjunto de resultados, a partir de zero|
|`stackql.row.fingerprint`|linha|`sha256:` do JSON canônico da linha, de modo que detectar desvio exige uma comparação por identidade em vez de uma comparação de todos os atributos|
|`stackql.snapshot.complete`|conclusão|`true`|
|`stackql.rows_returned`|conclusão|Número de linhas da instrução|
|`stackql.duration_ms`|conclusão|Duração real (wall-clock) desde o início da instrução|

<br />

O conjunto de atributos emitido é uma interface versionada: o escopo de instrumentação é `github.com/stackql/stackql/internal/stackql/output` na versão `1.0.0`, e `schemaUrl` fixa as convenções estáveis em `https://opentelemetry.io/schemas/1.44.0`.

Nenhuma identidade de recurso é inferida: a impressão digital é o valor, e o backend escolhe a chave (`id`, `arn`, `selfLink` ou uma tupla de colunas).  Para acompanhar direitos de acesso como entidades, expanda as vinculações em SQL para uma linha por principal e role; a telemetria segue a granularidade do resultado.

#### Atributos do recurso {#resource-attributes}

O recurso contém `service.name` (`stackql` ou o valor de `OTEL_SERVICE_NAME`) e `service.version`, as convenções de nuvem do OpenTelemetry quando podem ser derivadas de um `SELECT` de uma única tabela (`cloud.provider` a partir do provider; `cloud.account.id` a partir de uma igualdade em `project` ou `subscriptionId` na cláusula `WHERE`; `cloud.region` a partir de `region`; `cloud.availability_zone` a partir de `zone`) e cada par da variável de ambiente padronizada `OTEL_RESOURCE_ATTRIBUTES`, de modo que uma etapa do pipeline pode marcar `deployment.environment`, um nome de job ou tags de conta sem opções adicionais:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### Enviar os registros {#shipping-the-records}

O arquivo tem um `LogsData` por linha, o formato que o receptor `otlp_json_file` do OpenTelemetry Collector ingere sem nenhum processador de transformação:

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

Cada linha também é, byte a byte, o corpo de uma solicitação OTLP/HTTP `POST /v1/logs`, que é o que o exportador push integrado envia.

#### Enviar para um endpoint OTLP {#pushing-to-an-otlp-endpoint}

Os registros podem ser enviados diretamente a um endpoint de logs OTLP/HTTP, além de serem gravados no arquivo ou em stdout, com a configuração de um exportador na opção `--otel.config`, uma string JSON ou YAML como as outras opções de configuração (disponível a partir do StackQL `v0.12.732`):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| Chave | Finalidade |
|--|--|
|`exporter.endpoint`|URL completa do endpoint de logs, por exemplo `http://localhost:4318/v1/logs`; obrigatória para habilitar o exportador|
|`exporter.headers`|Cabeçalhos de solicitação enviados em cada exportação, normalmente um token de ingestão ou uma chave de API|
|`exporter.timeout_ms`|Tempo limite da solicitação em milissegundos, valor padrão `10000`|
|`exporter.batch_size`|Registros por solicitação, valor padrão `512`|

<br />

Cada instrução é um lote: os seus registros de linha e o registro de conclusão são enviados como um único `LogsData` OTLP/JSON (um envelope de recurso e escopo, uma entrada em `logRecords` por registro), e um conjunto de resultados grande é dividido em solicitações de no máximo `batch_size` registros à medida que as linhas chegam, com a solicitação final enviada depois do registro de conclusão.  Respostas `429` e `5xx` e falhas de conexão são repetidas em até quatro tentativas, com um intervalo de espera que dobra a partir de 500ms, respeitando um cabeçalho `Retry-After` em segundos (limitado a 30 segundos) quando o servidor envia um; os outros erros não são repetidos.  O arquivo ou o fluxo é gravado de qualquer forma, então um envio com falha deixa uma cópia local e uma linha de erro em stderr.  O exportador usa OTLP/JSON sobre HTTP; um endpoint que só aceita protobuf precisa de um Collector na frente dele.

Nenhuma variável de ambiente `OTEL_EXPORTER_OTLP_*` é lida; todas as configurações do exportador vêm de `--otel.config`.  Um valor malformado em `--otel.config`, ou um objeto `exporter` sem `endpoint`, faz a inicialização falhar.

Isso atende à ingestão OTLP do ClickStack (`http://<host>:4318/v1/logs`, cabeçalho `authorization: <ingestion API key>`), ao receptor OTLP de um Datadog Agent ou à ingestão OTLP do Datadog (`dd-api-key: <key>`) e a qualquer receptor `otlp` do OpenTelemetry Collector.  Consulte [Receitas de backends OTel](/getting-started/otel-recipes) para ver as consultas de inventário, desvio e direitos de acesso do lado receptor.
