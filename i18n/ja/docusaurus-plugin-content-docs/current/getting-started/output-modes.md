---
title: 出力モード
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の出力モード（table、json、jsonl、csv、otel）、その選択方法、shell、exec、サーバーでの使用時にそれぞれが出力する内容。"
image: "/img/stackql-featured-image.png"
---
関連項目：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL のクエリから返される結果は、表形式（table）のほか、json、jsonl（改行区切りの JSON）、csv、text、otel（OpenTelemetry ログレコード）の形式で出力できます。JSON、JSONL、CSV 形式は、ほかのプログラムとのデータ交換に使用できます。`otel` 形式は、結果セットをタイムスタンプ付きのスナップショットとして OpenTelemetry バックエンドに送信します。出力形式は、StackQL の `--output` フラグで設定します。

### `table` 出力形式 {#table-output-format}
次のように `--output` パラメーターに `table` 値を指定すると、結果を表形式で出力できます（これは StackQL の対話型シェルのデフォルト値です）：

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

### `json` 出力 {#json-output}
次のように `--output` パラメーターに `json` 値を指定すると、結果を JSON 形式で返すことができます。この出力形式は、クエリの結果を外部のプロセスやスクリプトに渡す必要がある場合に便利です：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### `jsonl` 出力 {#jsonl-output}
次のように `--output` パラメーターに `jsonl` 値を指定すると、結果を改行区切りの JSON（JSON Lines）形式で返すことができます（エイリアスとして `ndjson` も使用できます）。各行は 1 つの JSON オブジェクトとしてそれぞれ独立した行に書き込まれ、書き込まれるたびにフラッシュされます。そのため、この出力形式は、行指向のツールへ結果をストリーミングする場合や、大きな結果セットをほかのプログラムにパイプで渡す場合に便利です：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### `csv` 出力 {#csv-output}
次のように `--output` パラメーターに `csv` 値を指定すると、結果を JSON 形式で返すことができます。この出力形式は、Excel で結果を解析する場合や、ほかのシステムにデータインターフェイスを提供する場合に便利です：

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

#### CSV 出力の例（別の区切り文字を使用） {#csv-output-example-with-alternative-delimiter}
次のように `-d` スイッチを使用すると、`csv` 出力形式に別の区切り文字を指定できます：

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

### `otel` 出力 {#otel-output}
`--output` パラメーターに `otel` 値を指定すると、結果を OpenTelemetry ログレコードとして出力できます。各行は 1 つの OTLP/JSON `LogsData` オブジェクトとしてそれぞれ独立した行に書き込まれ、`jsonl` と同様に書き込まれるたびにフラッシュされます。その後に、文の完了レコードが 1 つ続きます。この出力形式は、クエリをテレメトリとしてのクラウド構成のスナップショットに変換します。インベントリクエリをスケジュールに従って実行し、レコードを OTLP 対応のバックエンド（ClickHouse、Datadog、またはほかの任意のバックエンドの前段に置いた OpenTelemetry Collector）に送信すれば、そこで「現在のリソース全体はどうなっているか」「前回の実行から何が変わったか」「誰がどの権限付与を、いつから保持しているか」に答えられます。StackQL `v0.11.660` 以降で利用できます。

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

1 つの行レコードを整形して表示したものです（ファイルでは各レコードが 1 行に格納されます）：

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

#### スナップショット {#snapshots}

1 つの文が 1 つのスナップショットです。文のすべてのレコードは、同じ `timeUnixNano`（文の開始時刻）、同じ `stackql.snapshot.id`、同じ `spanId` を持ちます。そのため、行が複数のページにわたって数秒かけて届いても、時点指定のクエリ、差分、最新状態のクエリが機能します。`observedTimeUnixNano` には、各レコードが実際に書き込まれた時刻が記録されます。1 回の呼び出しのすべての文は、1 つの `traceId` を共有します。この値は、`TRACEPARENT` 環境変数が設定されていればそこから取得され（CI パイプラインでの慣例）、設定されていなければ生成されます。

文の最後のレコードは完了レコードです。ボディは `snapshot complete <provider.service.resource>` で、`stackql.snapshot.complete=true`、`stackql.rows_returned`、`stackql.duration_ms` を持ちます。完了レコードは、スナップショットが完全であることをバックエンドに伝えます。行を返さない文も完了レコードを出力するため、消えたリソースは何も届かない状態ではなく、ドリフトとして現れます。エラーの表示方法が `record` の場合、失敗した文は `error.type` と `error.message` を持つ `ERROR` レコードを生成します。デフォルトでは、ほかのすべての出力形式と同様に、エラーは stderr に出力されます。

#### レコードの形状 {#record-shape}

ボディは、ネストされたオブジェクトをそのまま含むコンパクトな JSON 形式の完全な行であり、ドキュメント全体の信頼できる唯一の情報源です。属性は各列をその列名で保持し、値がスカラーの場合は型付けされます（`stringValue`、`intValue`、`doubleValue`、`boolValue`）。ネストされた列は JSON 文字列として保持されます。この形式は、デフォルトの属性スキーマが文字列値であるバックエンドの間で移植できます。null の列は省略されます。予約済みの `stackql.*` 属性は次のとおりです：

| 属性 | レコード | 値 |
|--|--|--|
|`stackql.snapshot.id`|すべて|文の実行ごとに生成される UUID|
|`stackql.query`|すべて|送信されたクエリのテキストそのもの|
|`stackql.query.hash`|すべて|クエリのテキストの SHA-256。定期実行ジョブでは SQL より軽量な系列キー|
|`stackql.provider`、`stackql.service`、`stackql.resource`|すべて|文の主テーブル。複数のテーブルにまたがる結合とビューでは省略|
|`stackql.row.index`|行|結果セット内の 0 から始まる位置|
|`stackql.row.fingerprint`|行|正規化された行の JSON の `sha256:`。これにより、ドリフトの検出がすべての属性の比較ではなく identity ごとに 1 回の比較で済む|
|`stackql.snapshot.complete`|完了|`true`|
|`stackql.rows_returned`|完了|文の行数|
|`stackql.duration_ms`|完了|文の開始からの実経過時間|

<br />

出力される属性セットは、バージョン管理されたインターフェイスです。インストルメンテーションスコープはバージョン `1.0.0` の `github.com/stackql/stackql/internal/stackql/output` で、`schemaUrl` は安定版の規約を `https://opentelemetry.io/schemas/1.44.0` に固定します。

リソースの identity は推測されません。フィンガープリントが値であり、キー（`id`、`arn`、`selfLink`、または列の組）はバックエンドが選択します。権限付与をエンティティとして追跡するには、SQL でバインディングをプリンシパルとロールの組み合わせごとに 1 行に展開します。テレメトリは結果の粒度に従います。

#### リソース属性 {#resource-attributes}

リソースは次の属性を持ちます。`service.name`（`stackql`、または `OTEL_SERVICE_NAME` の値）と `service.version`、単一テーブルの `SELECT` から導出できる場合は OpenTelemetry のクラウド規約（`cloud.provider` はプロバイダーから、`cloud.account.id` は `WHERE` 句の `project` または `subscriptionId` の等価条件から、`cloud.region` は `region` から、`cloud.availability_zone` は `zone` から）、そして標準の `OTEL_RESOURCE_ATTRIBUTES` 環境変数に含まれるすべてのペアです。そのため、パイプラインのステップでは、フラグを追加せずに `deployment.environment`、ジョブ名、アカウントのタグを付与できます：

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### レコードの送信 {#shipping-the-records}

ファイルには 1 行に 1 つの `LogsData` が格納されます。これは、OpenTelemetry Collector の `otlp_json_file` レシーバーが transform プロセッサーなしで取り込める形状です：

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

各行は、OTLP/HTTP の `POST /v1/logs` リクエストのボディともバイト単位で同一です。これは、組み込みのプッシュエクスポーターが送信する内容です。

#### OTLP エンドポイントへのプッシュ {#pushing-to-an-otlp-endpoint}

`--otel.config` フラグでエクスポーターを設定すると、レコードをファイルや stdout に書き込むだけでなく、OTLP/HTTP のログエンドポイントに直接プッシュできます。このフラグの値は、ほかの設定フラグと同様に JSON または YAML の文字列です（StackQL `v0.12.732` 以降で利用できます）：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\'
```

| キー | 用途 |
|--|--|
|`exporter.endpoint`|ログエンドポイントの完全な URL（たとえば `http://localhost:4318/v1/logs`）。エクスポーターを有効にするには必須|
|`exporter.headers`|エクスポートのたびに送信されるリクエストヘッダー。通常は取り込み用のトークンまたは API キー|
|`exporter.timeout_ms`|リクエストのタイムアウト（ミリ秒）。デフォルトは `10000`|
|`exporter.batch_size`|リクエストあたりのレコード数。デフォルトは `512`|

<br />

1 つの文が 1 つのバッチです。文の行レコードと完了レコードは、1 つの OTLP/JSON `LogsData`（リソースとスコープのエンベロープが 1 つ、`logRecords` にはレコードごとに 1 エントリ）として送信されます。大きな結果セットは、行のストリーミングに合わせて最大 `batch_size` 件のレコードを含むリクエストに分割され、完了レコードの後に最後のリクエストが送信されます。`429` と `5xx` のレスポンス、および接続の失敗は、500ms から倍増するバックオフで最大 4 回の試行まで再試行されます。サーバーが秒数指定の `Retry-After` ヘッダーを送信した場合は、その値に従います（上限は 30 秒）。そのほかのエラーは再試行されません。ファイルまたはストリームへの書き込みはプッシュの結果にかかわらず行われるため、プッシュに失敗してもローカルのコピーと stderr のエラー行が残ります。エクスポーターは HTTP 上の OTLP/JSON を使用します。protobuf しか受け付けないエンドポイントには、前段に Collector が必要です。

`OTEL_EXPORTER_OTLP_*` 環境変数は読み取られません。エクスポーターの設定はすべて `--otel.config` から取得されます。`--otel.config` の値が不正な場合、または `exporter` オブジェクトに `endpoint` がない場合は、起動に失敗します。

この機能の送信先は、ClickStack の OTLP インテーク（`http://<host>:4318/v1/logs`、ヘッダー `authorization: <ingestion API key>`）、Datadog Agent の OTLP レシーバーまたは Datadog の OTLP インテーク（`dd-api-key: <key>`）、および任意の OpenTelemetry Collector の `otlp` レシーバーです。受信側でのインベントリ、ドリフト、権限付与のクエリについては、[OTel バックエンドのレシピ](/getting-started/otel-recipes)を参照してください。
