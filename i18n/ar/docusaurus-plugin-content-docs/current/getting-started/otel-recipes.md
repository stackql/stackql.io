---
title: وصفات واجهات OTel الخلفية
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - جرد الموارد السحابية
  - اكتشاف الانحراف
description: جرد الموارد السحابية والانحراف وتاريخ الاستحقاقات من إخراج otel في StackQL ضمن ClickHouse وDatadog، مع مثال على مهمة CI مجدولة
image: "/img/stackql-featured-image.png"
---
راجع أيضًا:  
[[` أوضاع الإخراج `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

يحول الأمر `stackql exec --output otel` الاستعلام إلى لقطة ذات طابع زمني: إدخال سجل OpenTelemetry واحد لكل صف، وسجل إكمال لكل جملة، ومفاتيح الارتباط `stackql.snapshot.id` و`stackql.query.hash` و`stackql.row.fingerprint` في كل سجل (راجع [الإخراج `otel`](/getting-started/output-modes#otel-output)).  تتابع هذه الصفحة تلك السجلات من البداية إلى النهاية في الواجهتين الخلفيتين اللتين تم تصميم التنسيق من أجلهما: جدول جرد محدد الأنواع في ClickHouse (بما في ذلك ClickStack)، واستعلامات السجلات وأدوات المراقبة في Datadog.  القسم الأخير مهمة CI مجدولة تضيف إلى كل تشغيل معرف تتبع وسمات موارد.

تستخدم الوصفات استعلامين متكررين: جرد لمثيلات Compute Engine، وروابط IAM لمشروع بعد فك تداخلها إلى صف واحد لكل كيان أمان ودور.  يتضمن إسقاط كل منهما العمود `identity`، وهو المفتاح الذي تتتبع به الواجهة الخلفية المورد (`id` أو `arn` أو `selfLink`، أو دمج للحصول على مفتاح مركب)؛ والبصمة هي القيمة.

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

لكل استعلام قيمة `stackql.query.hash` ثابتة، وتصفي الوصفات حسبها بحيث تقتصر السلسلة على استعلام متكرر واحد دون سواه.  استخدم `{query_hash:String}` كمعلمة استعلام في ClickHouse، أو الصق قيمة التجزئة من سجل إكمال.

## ClickHouse {#clickhouse}

يكتب exporter ClickHouse في OpenTelemetry Collector، الذي يتضمنه ClickStack، إدخالات السجل في `otel_logs` مع السمات على هيئة خرائط سلاسل نصية (`ResourceAttributes`، `LogAttributes`) إلى جانب `Timestamp` و`Body` و`TraceId` و`ScopeName`.  وجّه exporter إلى نقطة استقبال OTLP في ClickStack أو إلى Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### جدول جرد محدد الأنواع {#typed-inventory-table}

يعمل الاستعلام عن `otel_logs` مباشرة، لكن كل سؤال يتحمل حينها تكلفة عمليات البحث في الخرائط.  تنقل طريقة عرض مجسدة سجلات الصفوف إلى جدول محدد الأنواع مفتاحه `(query_hash, identity, snapshot_time)`:

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

تذهب سجلات الإكمال إلى جدول صغير خاص بها.  يحدد هذا الجدول معنى "أحدث لقطة" في كل الوصفات أدناه: لا تؤخذ اللقطة في الحسبان إلا بعد وصول سجل الإكمال الخاص بها، لذا لا يختلط أبدًا تشغيل فشل في منتصف الطريق ببيئة أصغر.

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

لا ترى طريقة العرض المجسدة إلا الصفوف المدرجة بعد إنشائها.  املأ بأثر رجعي السجلات التي وصلت قبل ذلك بجملة `INSERT INTO ... SELECT` تستخدم استعلام select نفسه الذي تستخدمه طريقة العرض.

### أحدث حالة لكل مورد {#latest-state-per-resource}

البيئة الحالية هي مجموعة صفوف أحدث لقطة مكتملة:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

للحصول على آخر حالة معروفة لكل مورد ظهر في أي وقت، بما في ذلك الموارد التي اختفت منذ ذلك الحين، نفّذ التجميع باستخدام الدالة `argMax`:

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

### الانحراف بين لقطتين {#drift-between-two-snapshots}

اربط بين اللقطتين على أساس الهوية.  عدم تطابق البصمتين يعني حدوث تغيير؛ والجانب المفقود يعني وجود مورد ظهر أو اختفى:

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

ضع معرفي أي لقطتين مكانهما لمقارنة أي نقطتين زمنيتين.  عندما تحتاج إلى جانب واحد فقط، يصبح السؤال نفسه عملية ربط عكسي (anti-join):

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

العمود `change` هو نطاق الانحراف الخاص بالإسقاط: لا يمكن أن ينتج تغيير عن الأعمدة المستبعدة من جملة `SELECT`، لذا فإن الاستعلام الذي يقتصر إسقاطه على `name` و`status` يبلغ عن تغييرات الحالة ولا شيء غيرها.

### تاريخ الاستحقاقات {#entitlement-history}

في استعلام الاستحقاقات، الهوية هي زوج كيان الأمان والدور، لذا فإن الخط الزمني للزوج في جدول الجرد هو عمر ذلك الاستحقاق:

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

يدمج العمودان `first_seen` و`last_seen` المنح الذي تم إلغاؤه ثم أعيد منحه لاحقًا في فترة واحدة؛ وعندما تكون الفجوات مهمة، تسرد الدالة `groupArray(snapshot_time)` على التجميع نفسه كل لقطة ظهر فيها الزوج.  كل ما تبقى في هذه الصفحة ينطبق دون تغيير: وصفة الانحراف على استعلام الاستحقاقات تسرد عمليات المنح والإلغاء بين تشغيلين.

## Datadog {#datadog}

أرسل السجلات إلى receiver OTLP في Datadog Agent (مكّن receiver HTTP في القسم `otlp_config` من تكوين Agent، المنفذ `4318`) أو في OpenTelemetry Collector يستخدم exporter Datadog.  تستقبل نقطة استقبال OTLP الخاصة بـ Datadog نفسها السجلات مباشرة مع تمرير مفتاح API في رأس؛ احصل على عنوان URL الخاص بمنطقتك من وثائق استيعاب OTLP في Datadog.

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

### تعيين السمات {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|سمة المورد `service.name`|الوسم `service` (`stackql`، أو `OTEL_SERVICE_NAME`)|
|سمة المورد `deployment.environment`|الوسم `env`|
|`cloud.provider` و`cloud.account.id` و`cloud.region` و`cloud.availability_zone` وسمات الموارد الأخرى|سمات السجل، على سبيل المثال `@cloud.account.id`|
|سمات السجل `stackql.*`|سمات السجل، على سبيل المثال `@stackql.snapshot.id`؛ أضف facets للسمات التي تصفي حسبها|
|أعمدة الصف (`identity`، `name`، `status`، ...)|سمات السجل باسم العمود، على سبيل المثال `@status`|
|نص السجل|`message`، الصف بتنسيق JSON|
|`traceId`، `spanId`|`trace_id`، `span_id`، تربط بين جميع جمل التشغيل الواحد|

<br />

### استعلامات السجلات {#log-queries}

يجيب Datadog عن أسئلة الوجود والتاريخ؛ أما عمليات الربط بين اللقطات فتتم في ClickHouse، أو بالتوزيع المتوازي للتدفق نفسه إلى كلتا الواجهتين عبر Collector.

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

تراقب أدوات المراقبة على سجلات الإكمال خط الأنابيب نفسه: أداة مراقبة سجلات على `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` تكتشف الجرد الذي جاءت نتيجته فارغة، وأداة مراقبة بشرط "no data" على `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` تكتشف التشغيل المجدول الذي لم يحدث.

## مهام CI المجدولة {#scheduled-ci}

المهمة المجدولة هي ما ينشئ السلسلة.  يشغّل المثال كلا الاستعلامين كل ساعة من GitHub Actions: يتم تكوين exporter مرة واحدة في متغير البيئة `OTEL_CONFIG` وتمريره إلى العلامة `--otel.config`، ويمنح المتغير `TRACEPARENT` جميع جمل التشغيل الواحد معرف التتبع نفسه، ويضيف المتغير `OTEL_RESOURCE_ATTRIBUTES` البيئة واسم المهمة ومعرف التشغيل إلى كل سجل، ويتم الاحتفاظ بملفات `.otlp.jsonl` كملفات ناتجة بحيث تبقى نسخة منها حتى إذا فشل الإرسال.

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

لا تحتاج المهمة نفسها على أي برنامج جدولة آخر إلا إلى العلامة ومتغيري البيئة؛ والمتغير `TRACEPARENT` اختياري: بدونه تنشئ كل عملية `stackql exec` معرف التتبع الخاص بها، ولا تشترك فيه إلا الجمل التي تنفذها تلك العملية، لذا ستحمل جملتا التشغيل أعلاه معرفي تتبع مختلفين.
