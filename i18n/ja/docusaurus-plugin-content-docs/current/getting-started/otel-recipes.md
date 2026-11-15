---
title: OTel バックエンドのレシピ
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - クラウドインベントリ
  - ドリフト検出
description: StackQL の otel 出力を使った ClickHouse と Datadog でのクラウドインベントリ、ドリフト、権限付与の履歴と、スケジュール実行の CI の例
image: "/img/stackql-featured-image.png"
---
関連項目：  
[[` 出力モード `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` は、クエリをタイムスタンプ付きのスナップショットに変換します。行ごとに 1 つの OpenTelemetry ログレコード、文ごとに 1 つの完了レコードを出力し、すべてのレコードに相関キー `stackql.snapshot.id`、`stackql.query.hash`、`stackql.row.fingerprint` を付与します（[`otel` 出力](/getting-started/output-modes#otel-output)を参照）。このページでは、この形式が想定している 2 つのバックエンドで、これらのレコードをエンドツーエンドで扱います。ClickHouse（ClickStack を含む）の型付きインベントリテーブルと、Datadog のログクエリおよびモニターです。最後のセクションでは、各実行にトレース ID とリソース属性を付与するスケジュール実行の CI ジョブを取り上げます。

レシピでは、定期的に実行する 2 つのクエリを使用します。Compute Engine インスタンスのインベントリと、プリンシパルとロールの組み合わせごとに 1 行に展開したプロジェクトの IAM バインディングです。どちらのクエリも `identity` 列を射影します。これはバックエンドがリソースを追跡する際のキー（`id`、`arn`、`selfLink`、または複合キーの場合は連結した値）であり、値はフィンガープリントです。

```sql
-- inventory: one row per instance
select id as identity, name, status, machineType, zone
from google.compute.instances
where project = 'stackql-demo' and zone = 'australia-southeast1-a';

-- entitlements: one row per principal and role
select iam.role || '/' || split_part(json_each.value, ':', 2) as identity,
iam.role, split_part(json_each.value, ':', 2) as principal
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members)
where projectsId = 'stackql-demo';
```

各クエリには安定した `stackql.query.hash` があります。レシピではこの値でフィルタリングし、1 つの系列が 1 つの定期実行クエリだけで構成されるようにします。ClickHouse では `{query_hash:String}` をクエリパラメーターとして使用するか、完了レコードからハッシュを貼り付けます。

## ClickHouse {#clickhouse}

ClickStack に同梱されている OpenTelemetry Collector の ClickHouse エクスポーターは、ログレコードを `otel_logs` に格納します。属性は文字列マップ（`ResourceAttributes`、`LogAttributes`）として、`Timestamp`、`Body`、`TraceId`、`ScopeName` と並んで格納されます。エクスポーターの送信先には、ClickStack の OTLP インテークまたは Collector を指定します：

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### 型付きインベントリテーブル {#typed-inventory-table}

`otel_logs` を直接クエリすることもできますが、その場合は毎回マップの参照にコストがかかります。マテリアライズドビューを使うと、行レコードを `(query_hash, identity, snapshot_time)` をキーとする型付きテーブルに取り出せます：

```sql
CREATE TABLE stackql_inventory
(
    snapshot_time DateTime64(9),
    snapshot_id   String,
    query_hash    String,
    provider      LowCardinality(String),
    service       LowCardinality(String),
    resource      LowCardinality(String),
    identity      String,
    fingerprint   String,
    attributes    Map(LowCardinality(String), String),
    document      String,
    environment   LowCardinality(String),
    trace_id      String
)
ENGINE = MergeTree
ORDER BY (query_hash, identity, snapshot_time);

CREATE MATERIALIZED VIEW stackql_inventory_mv TO stackql_inventory AS
SELECT
    Timestamp                                    AS snapshot_time,
    LogAttributes['stackql.snapshot.id']         AS snapshot_id,
    LogAttributes['stackql.query.hash']          AS query_hash,
    LogAttributes['stackql.provider']            AS provider,
    LogAttributes['stackql.service']             AS service,
    LogAttributes['stackql.resource']            AS resource,
    LogAttributes['identity']                    AS identity,
    LogAttributes['stackql.row.fingerprint']     AS fingerprint,
    LogAttributes                                AS attributes,
    Body                                         AS document,
    ResourceAttributes['deployment.environment'] AS environment,
    TraceId                                      AS trace_id
FROM otel_logs
WHERE ScopeName = 'github.com/stackql/stackql/internal/stackql/output'
  AND mapContains(LogAttributes, 'stackql.row.index');
```

完了レコードは専用の小さなテーブルに格納します。このテーブルが、以降のすべてのレシピにおける「最新のスナップショット」の意味を定義します。スナップショットは完了レコードが届いた時点で有効とみなされるため、途中で失敗した実行を、リソースが減った状態と取り違えることはありません。

```sql
CREATE TABLE stackql_snapshots
(
    snapshot_time DateTime64(9),
    snapshot_id   String,
    query_hash    String,
    query         String,
    rows_returned UInt32,
    duration_ms   UInt32,
    environment   LowCardinality(String)
)
ENGINE = MergeTree
ORDER BY (query_hash, snapshot_time);

CREATE MATERIALIZED VIEW stackql_snapshots_mv TO stackql_snapshots AS
SELECT
    Timestamp                                              AS snapshot_time,
    LogAttributes['stackql.snapshot.id']                   AS snapshot_id,
    LogAttributes['stackql.query.hash']                    AS query_hash,
    LogAttributes['stackql.query']                         AS query,
    toUInt32OrZero(LogAttributes['stackql.rows_returned']) AS rows_returned,
    toUInt32OrZero(LogAttributes['stackql.duration_ms'])   AS duration_ms,
    ResourceAttributes['deployment.environment']           AS environment
FROM otel_logs
WHERE ScopeName = 'github.com/stackql/stackql/internal/stackql/output'
  AND LogAttributes['stackql.snapshot.complete'] = 'true';
```

マテリアライズドビューが認識するのは、作成後に挿入された行だけです。それ以前に格納されたレコードは、ビューと同じ select を使った `INSERT INTO ... SELECT` でバックフィルします。

### リソースごとの最新の状態 {#latest-state-per-resource}

現在のリソース全体は、最新の完全なスナップショットの行セットです：

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

これまでに確認されたすべてのリソース（その後消えたリソースを含む）について、最後に判明している状態を得るには、`argMax` で集約します：

```sql
SELECT
    identity,
    argMax(document, snapshot_time)    AS document,
    argMax(fingerprint, snapshot_time) AS fingerprint,
    max(snapshot_time)                 AS last_seen
FROM stackql_inventory
WHERE query_hash = {query_hash:String}
GROUP BY identity
ORDER BY last_seen DESC;
```

### 2 つのスナップショット間のドリフト {#drift-between-two-snapshots}

2 つのスナップショットを identity で結合します。フィンガープリントが一致しなければ変更であり、片側が欠けていればリソースが出現したか消えたことを示します：

```sql
WITH
    (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
     ORDER BY snapshot_time DESC LIMIT 1) AS latest,
    (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
     ORDER BY snapshot_time DESC LIMIT 1 OFFSET 1) AS previous
SELECT
    if(n.identity != '', n.identity, p.identity) AS identity,
    multiIf(p.identity = '', 'appeared', n.identity = '', 'disappeared', 'changed') AS change,
    p.document AS before,
    n.document AS after
FROM (SELECT identity, fingerprint, document FROM stackql_inventory WHERE snapshot_id = latest) AS n
FULL OUTER JOIN
     (SELECT identity, fingerprint, document FROM stackql_inventory WHERE snapshot_id = previous) AS p
ON n.identity = p.identity
WHERE n.fingerprint != p.fingerprint
ORDER BY change, identity;
```

任意の 2 つのスナップショット ID に置き換えると、任意の時点間の差分を取得できます。片側だけが必要な場合は、アンチ結合で同じことができます：

```sql
-- appeared since the previous snapshot
SELECT n.identity, n.document
FROM (SELECT identity, document FROM stackql_inventory WHERE snapshot_id = latest) AS n
LEFT ANTI JOIN (SELECT identity FROM stackql_inventory WHERE snapshot_id = previous) AS p
ON n.identity = p.identity;

-- disappeared since the previous snapshot
SELECT p.identity, p.document
FROM (SELECT identity, document FROM stackql_inventory WHERE snapshot_id = previous) AS p
LEFT ANTI JOIN (SELECT identity FROM stackql_inventory WHERE snapshot_id = latest) AS n
ON p.identity = n.identity;
```

`change` 列が検出するドリフトの範囲は、射影によって決まります。`SELECT` に含めなかった列は変更の原因にならないため、`name` と `status` だけを射影するクエリは、状態の変化だけを報告します。

### 権限付与の履歴 {#entitlement-history}

権限付与のクエリでは、identity はプリンシパルとロールのペアです。そのため、インベントリテーブル内のペアのタイムラインが、その権限付与の存続期間になります：

```sql
WITH (SELECT max(snapshot_time) FROM stackql_snapshots WHERE query_hash = {query_hash:String}) AS latest_time
SELECT
    attributes['principal']           AS principal,
    attributes['role']                AS role,
    min(snapshot_time)                AS first_seen,
    max(snapshot_time)                AS last_seen,
    max(snapshot_time) < latest_time  AS revoked
FROM stackql_inventory
WHERE query_hash = {query_hash:String}
GROUP BY principal, role
ORDER BY last_seen DESC, principal, role;
```

`first_seen` と `last_seen` は、取り消された後に再付与された権限を 1 つの期間にまとめてしまいます。空白期間が重要な場合は、同じグループ化で `groupArray(snapshot_time)` を使うと、そのペアが存在したすべてのスナップショットを一覧表示できます。このページのほかの内容はすべてそのまま適用できます。権限付与のクエリにドリフトのレシピを適用すると、2 回の実行間の付与と取り消しが一覧表示されます。

## Datadog {#datadog}

レコードは、Datadog Agent の OTLP レシーバー（Agent の `otlp_config` で HTTP レシーバーを有効にします。ポートは `4318` です）、または Datadog エクスポーターを実行している OpenTelemetry Collector の OTLP レシーバーに送信します。Datadog 自体の OTLP インテークも、API キーをヘッダーとして指定すればレコードを直接受け付けます。リージョンごとの URL は、Datadog の OTLP 取り込みに関するドキュメントで確認してください。

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://datadog-agent.example:4318/v1/logs" } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

```shell
stackql exec --output otel ... \
--otel.config '{ "exporter": { "endpoint": "<regional OTLP logs intake URL>", "headers": { "dd-api-key": "'"$DD_API_KEY"'" } } }'
```

### 属性のマッピング {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|`service.name` リソース属性|`service` タグ（`stackql` または `OTEL_SERVICE_NAME`）|
|`deployment.environment` リソース属性|`env` タグ|
|`cloud.provider`、`cloud.account.id`、`cloud.region`、`cloud.availability_zone` などのリソース属性|ログ属性（たとえば `@cloud.account.id`）|
|`stackql.*` ログ属性|ログ属性（たとえば `@stackql.snapshot.id`）。フィルタリングに使う属性にはファセットを追加します|
|行の列（`identity`、`name`、`status` など）|列名のログ属性（たとえば `@status`）|
|レコードのボディ|`message`（JSON 形式の行）|
|`traceId`、`spanId`|`trace_id`、`span_id`（1 回の実行のすべての文を関連付ける）|

<br />

### ログクエリ {#log-queries}

Datadog は、存在と履歴に関する問いに答えます。スナップショット間の結合は ClickHouse で行うか、Collector を介して同じストリームを両方にファンアウトして行います。

```
# completion records of one recurring query, newest first: the latest snapshot id
service:stackql @stackql.snapshot.complete:true @stackql.query.hash:<hash>

# the rows of one snapshot
service:stackql @stackql.query.hash:<hash> @stackql.snapshot.id:<snapshot id>

# the timeline of one resource; a new fingerprint value is a change
service:stackql @identity:<instance id>

# every principal that has held a role, grouped by @principal in Log Analytics;
# the first and last log in a group are first seen and last seen
service:stackql @stackql.query.hash:<entitlements hash> @role:roles/owner
```

完了レコードに対するモニターは、パイプライン自体を監視します。`service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` に対するログモニターは、空で返ってきたインベントリを検出します。`service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` に対する「no data」条件のモニターは、実行されなかったスケジュール実行を検出します。

## スケジュール実行の CI {#scheduled-ci}

系列は、スケジュールされたジョブによって作られます。この例では、GitHub Actions から両方のクエリを 1 時間ごとに実行します。エクスポーターは `OTEL_CONFIG` で一度だけ設定して `--otel.config` に渡します。`TRACEPARENT` は 1 回の実行のすべての文に同じトレース ID を与え、`OTEL_RESOURCE_ATTRIBUTES` はすべてのレコードに環境、ジョブ名、実行 ID を付与します。`.otlp.jsonl` ファイルはアーティファクトとして保存されるため、プッシュに失敗してもコピーが残ります。

```yaml
name: cloud-inventory
on:
  schedule:
    - cron: '0 * * * *'
  workflow_dispatch:
jobs:
  snapshot:
    runs-on: ubuntu-latest
    env:
      GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }}
      OTEL_CONFIG: '{ "exporter": { "endpoint": "${{ vars.OTLP_LOGS_ENDPOINT }}", "headers": { "authorization": "${{ secrets.OTLP_INGESTION_KEY }}" } } }'
      OTEL_RESOURCE_ATTRIBUTES: deployment.environment=prod,stackql.job=cloud-inventory,stackql.run=${{ github.run_id }}
    steps:
      - uses: stackql/setup-stackql@v2
      - name: snapshot
        run: |
          export TRACEPARENT="00-$(openssl rand -hex 16)-$(openssl rand -hex 8)-01"
          stackql exec --output otel -f instances.otlp.jsonl --otel.config "$OTEL_CONFIG" \
            "select id as identity, name, status, machineType, zone from google.compute.instances \
             where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
          stackql exec --output otel -f entitlements.otlp.jsonl --otel.config "$OTEL_CONFIG" \
            "select iam.role || '/' || split_part(json_each.value, ':', 2) as identity, \
             iam.role, split_part(json_each.value, ':', 2) as principal \
             from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) \
             where projectsId = 'stackql-demo'"
      - uses: actions/upload-artifact@v4
        with:
          name: inventory-${{ github.run_id }}
          path: "*.otlp.jsonl"
```

ほかのスケジューラーで同じジョブを実行する場合も、必要なのはこのフラグと 2 つの環境変数だけです。`TRACEPARENT` は省略可能です。指定がない場合は `stackql exec` のプロセスごとに独自のトレース ID が生成され、そのトレース ID を共有するのはそのプロセスが実行した文だけです。そのため、上の実行の 2 つの文には別々のトレース ID が付きます。
