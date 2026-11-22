---
title: أوضاع الإخراج
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "أوضاع الإخراج في StackQL: table وjson وjsonl وcsv وtext وotel، وكيفية اختيارها، وما يخرجه كل وضع عند الاستخدام مع shell وexec والخادم."
image: "/img/stackql-featured-image.png"
---
راجع أيضًا:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

يمكن تنسيق النتائج التي تعيدها استعلامات StackQL بتنسيق جدولي (table)، وكذلك بتنسيق json أو jsonl (JSON مفصول بأسطر جديدة) أو csv أو text أو otel (إدخالات سجل OpenTelemetry).  يمكن استخدام تنسيقات JSON وJSONL وCSV لتبادل البيانات مع برامج أخرى؛ أما التنسيق `otel` فيرسل مجموعة النتائج إلى واجهة خلفية لـ OpenTelemetry كلقطة ذات طابع زمني.  يتم تكوين تنسيق الإخراج المطلوب باستخدام العلامة `--output` في StackQL.

### تنسيق الإخراج `table` {#table-output-format}
يمكن تنسيق النتائج في شكل جدول باستخدام القيمة `table` للمعلمة `--output` كما هو موضح أدناه (هذه هي القيمة الافتراضية لواجهة الأوامر التفاعلية في StackQL):

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

### الإخراج `json` {#json-output}
يمكن إرجاع النتائج بتنسيق JSON باستخدام القيمة `json` للمعلمة `--output` كما هو موضح أدناه، ويفيد تنسيق الإخراج هذا إذا كانت نتائج الاستعلام بحاجة إلى تمريرها إلى عملية خارجية أو برنامج نصي:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### الإخراج `jsonl` {#jsonl-output}
يمكن إرجاع النتائج بتنسيق JSON المفصول بأسطر جديدة (JSON Lines) باستخدام القيمة `jsonl` للمعلمة `--output` كما هو موضح أدناه (القيمة `ndjson` مقبولة كاسم مستعار).  يُكتب كل صف ككائن JSON واحد في سطر مستقل، ويتم تفريغ الصفوف فور كتابتها، لذا فإن تنسيق الإخراج هذا مفيد لبث النتائج إلى الأدوات التي تعالج الأسطر أو لتمرير مجموعات النتائج الكبيرة عبر أنبوب إلى برامج أخرى:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### الإخراج `csv` {#csv-output}
يمكن إرجاع النتائج بتنسيق CSV باستخدام القيمة `csv` للمعلمة `--output` كما هو موضح أدناه، ويفيد تنسيق الإخراج هذا في تحليل النتائج في Excel أو في توفير واجهة بيانات لنظام آخر:

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

#### مثال على الإخراج Csv (باستخدام فاصل بديل) {#csv-output-example-with-alternative-delimiter}
يمكن تحديد فاصل بديل لتنسيق الإخراج `csv` باستخدام العلامة `-d` كما هو موضح أدناه:

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

### الإخراج `text` {#text-output}
يمكن إرجاع النتائج كنص عادي باستخدام القيمة `text` للمعلمة `--output`.  يُكتب كل صف على شكل قيمه مفصولة بفواصل، ويسبقه صف رأس ما لم يتم تحديد العلامة `-H` (`--hideheaders`).  على عكس `csv`، لا توضع القيم بين علامات اقتباس ولا يتم إلغاء أحرفها، ويتم تجاهل الفاصل `-d`، لذا تُكتب القيمة التي تحتوي على فاصلة أو علامة اقتباس مزدوجة كما هي:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

باستخدام `--output csv`، يعيد الاستعلام نفسه `"a,b","say ""hi"""` في صف البيانات.

### الإخراج `otel` {#otel-output}
يمكن إخراج النتائج كإدخالات سجل OpenTelemetry باستخدام القيمة `otel` للمعلمة `--output`.  يُكتب كل صف ككائن OTLP/JSON واحد من النوع `LogsData` في سطر مستقل، ويتم تفريغه فور كتابته كما في `jsonl`، ويتبع ذلك سجل إكمال واحد للجملة.  يحول تنسيق الإخراج هذا الاستعلام إلى لقطة للتكوين السحابي في صورة بيانات قياس عن بعد (telemetry): شغّل استعلام جرد وفق جدول زمني، وأرسل السجلات إلى واجهة خلفية تدعم OTLP (ClickHouse أو Datadog أو OpenTelemetry Collector يعمل أمام أي نظام آخر)، وأجب هناك عن الأسئلة "كيف تبدو البيئة الآن" و"ما الذي تغير منذ التشغيل الأخير" و"من كان لديه أي استحقاق، ومنذ متى".  متاح في StackQL من الإصدار `v0.11.660` فصاعدًا.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

سجل صف واحد بتنسيق مقروء (يحفظ الملف كل سجل في سطر واحد):

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

#### اللقطات {#snapshots}

الجملة الواحدة لقطة واحدة.  يحمل كل سجل من سجلات الجملة القيمة نفسها في `timeUnixNano` (وقت بدء الجملة)، والقيمة نفسها في `stackql.snapshot.id`، والقيمة نفسها في `spanId`، لذا تعمل استعلامات الحالة في لحظة محددة واستعلامات الفروق واستعلامات أحدث حالة حتى عندما تصل الصفوف عبر عدة صفحات على مدى عدة ثوان؛ ويسجل الحقل `observedTimeUnixNano` وقت كتابة كل سجل فعليًا.  تشترك جميع جمل الاستدعاء الواحد في قيمة `traceId` واحدة، تؤخذ من متغير البيئة `TRACEPARENT` عند تعيينه (وهو العرف المتبع في خطوط أنابيب CI) ويتم إنشاؤها خلاف ذلك.

آخر سجل في الجملة هو سجل الإكمال: محتواه (body) هو `snapshot complete <provider.service.resource>`، ويحمل `stackql.snapshot.complete=true` و`stackql.rows_returned` و`stackql.duration_ms`.  يخبر هذا السجل الواجهة الخلفية بأن اللقطة مكتملة، وتخرجه الجملة حتى عندما لا تعيد أي صفوف، لذا يظهر المورد الذي اختفى كانحراف بدلًا من أن يمر بصمت.  تنتج الجملة الفاشلة سجل `ERROR` يحمل `error.type` و`error.message` عندما يعمل StackQL باستخدام العلامة `--errorpresentation record`؛ وافتراضيًا تذهب الأخطاء إلى stderr كما في جميع تنسيقات الإخراج الأخرى.

#### شكل السجل {#record-shape}

محتوى السجل (body) هو الصف الكامل بتنسيق JSON مضغوط، مع الإبقاء على الكائنات المتداخلة كما هي، وهو المصدر الموثوق للمستند الكامل.  تحمل السمات كل عمود باسمه، مع نوعه عندما تكون القيمة مفردة (`stringValue` و`intValue` و`doubleValue` و`boolValue`)؛ أما الأعمدة المتداخلة فتحمل كسلاسل JSON نصية، وهو ما يضمن قابلية النقل بين الواجهات الخلفية التي يكون مخطط السمات الافتراضي فيها قائما على السلاسل النصية، وتحذف الأعمدة التي قيمتها null.  السمات المحجوزة `stackql.*` هي:

| السمة | السجل | القيمة |
|--|--|--|
|`stackql.snapshot.id`|الكل|UUID يتم إنشاؤه لكل تنفيذ للجملة|
|`stackql.query`|الكل|نص الاستعلام المرسل، حرفيًا|
|`stackql.query.hash`|الكل|تجزئة SHA-256 لنص الاستعلام؛ مفتاح سلسلة أقل تكلفة من نص SQL لمهمة متكررة|
|`stackql.provider`، `stackql.service`، `stackql.resource`|الكل|الجدول الأساسي للجملة؛ يحذف في عمليات الربط وطرق العرض التي تمتد عبر عدة جداول|
|`stackql.row.index`|صف|الموضع في مجموعة النتائج، مع بدء الترقيم من الصفر|
|`stackql.row.fingerprint`|صف|`sha256:` لتمثيل JSON المعياري للصف، بحيث يكون اكتشاف الانحراف مقارنة واحدة لكل هوية بدلًا من مقارنة على مستوى كل سمة|
|`stackql.snapshot.complete`|إكمال|`true`|
|`stackql.rows_returned`|إكمال|عدد صفوف الجملة|
|`stackql.duration_ms`|إكمال|المدة الزمنية الفعلية منذ بدء الجملة|

<br />

مجموعة السمات الناتجة واجهة ذات إصدارات: نطاق القياس (instrumentation scope) هو `github.com/stackql/stackql/internal/stackql/output` بالإصدار `1.0.0`، ويقفل الحقل `schemaUrl` الاصطلاحات المستقرة على `https://opentelemetry.io/schemas/1.44.0`.

لا يتم استنتاج هوية المورد: البصمة هي القيمة، والواجهة الخلفية هي التي تختار المفتاح (`id` أو `arn` أو `selfLink` أو مجموعة من الأعمدة).  لتتبع الاستحقاقات ككيانات، فكك الروابط في SQL إلى صف واحد لكل كيان أمان ودور؛ إذ تتبع بيانات القياس عن بعد مستوى تفصيل النتيجة.

#### سمات المورد {#resource-attributes}

يحمل المورد `service.name` (`stackql`، أو قيمة `OTEL_SERVICE_NAME`) و`service.version`، واصطلاحات OpenTelemetry السحابية حيثما أمكن اشتقاقها من جملة `SELECT` على جدول واحد (`cloud.provider` من الموفر؛ و`cloud.account.id` من شرط مساواة على `project` أو `subscriptionId` في عبارة `WHERE`؛ و`cloud.region` من `region`؛ و`cloud.availability_zone` من `zone`)، وكل زوج في متغير البيئة القياسي `OTEL_RESOURCE_ATTRIBUTES`، بحيث يمكن لخطوة في خط الأنابيب إضافة `deployment.environment` أو اسم مهمة أو وسوم الحساب دون أي علامات إضافية:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### إرسال السجلات {#shipping-the-records}

يحتوي الملف على كائن `LogsData` واحد في كل سطر، وهو الشكل الذي يستوعبه receiver `otlp_json_file` في OpenTelemetry Collector دون أي معالج تحويل (transform processor):

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

كل سطر هو أيضًا كائن OTLP/JSON كامل من النوع `LogsData` تقبله نقطة نهاية OTLP/HTTP `POST /v1/logs` كنص طلب.  يرسل exporter الدفع المدمج الترميز نفسه، لكنه يجمع السجلات في دفعات، كما هو موضح أدناه.

#### الدفع إلى نقطة نهاية OTLP {#pushing-to-an-otlp-endpoint}

يمكن دفع السجلات مباشرة إلى نقطة نهاية سجلات OTLP/HTTP، إلى جانب كتابتها في الملف أو في stdout، عن طريق تكوين exporter في العلامة `--otel.config`، وهي سلسلة نصية بتنسيق JSON أو YAML مثل علامات التكوين الأخرى (متاح في StackQL من الإصدار `v0.12.732` فصاعدًا):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| المفتاح | الغرض |
|--|--|
|`exporter.endpoint`|عنوان URL الكامل لنقطة نهاية السجلات، على سبيل المثال `http://localhost:4318/v1/logs`؛ مطلوب لتمكين exporter|
|`exporter.headers`|رؤوس الطلب المرسلة مع كل عملية تصدير، وهي عادة رمز مميز للاستيعاب أو مفتاح API|
|`exporter.timeout_ms`|مهلة الطلب بالمللي ثانية، والقيمة الافتراضية `10000`|
|`exporter.batch_size`|عدد السجلات في كل طلب، والقيمة الافتراضية `512`|

<br />

كل جملة دفعة واحدة: ترسل سجلات صفوفها وسجل الإكمال الخاص بها ككائن OTLP/JSON واحد من النوع `LogsData` (غلاف واحد للمورد والنطاق، وإدخال واحد في `logRecords` لكل سجل)، وتقسم مجموعة النتائج الكبيرة إلى طلبات لا يتجاوز كل منها `batch_size` من السجلات أثناء تدفق الصفوف، ويأتي الطلب الأخير بعد سجل الإكمال.  تتم إعادة محاولة الاستجابات `429` و`5xx` وحالات فشل الاتصال حتى أربع محاولات مع مهلة انتظار تبدأ من 500ms وتتضاعف في كل مرة، مع احترام الرأس `Retry-After` الذي يحدد التأخير بالثواني (بحد أقصى 30 ثانية) عندما يرسله الخادم؛ ولا تتم إعادة المحاولة للأخطاء الأخرى.  تتم كتابة الملف أو الدفق في جميع الأحوال، لذا يترك الدفع الفاشل نسخة محلية وسطر خطأ في stderr.  يتعامل exporter مع OTLP/JSON عبر HTTP؛ ونقطة النهاية التي لا تقبل إلا protobuf تحتاج إلى Collector أمامها.

لا تتم قراءة أي من متغيرات البيئة `OTEL_EXPORTER_OTLP_*`؛ إذ يأتي كل إعداد من إعدادات exporter من `--otel.config`.  تؤدي قيمة `--otel.config` غير جيدة التكوين، أو الكائن `exporter` الذي لا يحتوي على `endpoint`، إلى فشل بدء التشغيل.

يستهدف هذا نقطة استيعاب OTLP في ClickStack (`http://<host>:4318/v1/logs`، مع الرأس `authorization: <ingestion API key>`)، وreceiver OTLP في Datadog Agent أو نقطة استيعاب OTLP في Datadog (`dd-api-key: <key>`)، وأي receiver من النوع `otlp` في OpenTelemetry Collector.  راجع [وصفات واجهات OTel الخلفية](/getting-started/otel-recipes) للاطلاع على استعلامات الجرد والانحراف والاستحقاقات على الجانب المستقبل.
