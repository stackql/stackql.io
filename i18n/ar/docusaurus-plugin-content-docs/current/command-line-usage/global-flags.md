---
title: العلامات العامة
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "العلامات العامة للملف الثنائي stackql: إعدادات المصادقة وتنسيق الإخراج وموقع سجل الموفرين والتسجيل وواجهة SQL الخلفية، المتاحة في كل أمر."
image: "/img/stackql-featured-image.png"
---

تقبل أوامر StackQL العلامات العامة، لكن بعضها ينطبق على وضع معين فقط. حدّدها في سطر الأوامر أو، حيثما كان ذلك مدعومًا، في ملف تهيئة StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). ترد العلامات `--pgsrv.*` في [خيارات خادم PostgreSQL](/command-line-usage/srv#server-options).

| العلامة | نوع البيانات | الوصف | الافتراضي |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | تكوين وصول الجهة المستدعية، وهو مغلف JSON `{"path": "/path/to/access.json"}` (ويُقبل أيضًا مسار ملف مجرد).  تؤدي إلى تمكين [إدارة الوصول](/command-line-usage/access-management): تتم مصادقة الجهات المستدعية باستخدام JWT أو رمز مميز ثابت من نوع bearer أو TLS المتبادل أو هوية نظام التشغيل المحلي، ويتم تخويل الجمل وفقًا لقواعد السياسة.  تتم قراءة بيانات اعتماد الرمز المميز المحلي من متغير البيئة `STACKQL_CALLER_TOKEN`.  متاح في StackQL من الإصدار `v1.0` فصاعدًا. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | السماح للجلسة باختيار مخزن أسرار دائم في نظام التشغيل (macOS Keychain أو Linux Secret Service أو Windows Credential Manager) باستخدام `SET "$.secret_store"`.  يتم رفض المخازن القائمة على الملفات المسطحة حتى مع هذه العلامة.  راجع [جلسات الموفرين والأسرار](/getting-started/provider-sessions#persistent-os-secret-stores).  متاح في StackQL من الإصدار `v1.0` فصاعدًا. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | السماح بإلغاء مرجعية قيم الأسرار وبيانات الاعتماد في تعبيرات SQL (`"$secrets.name"` و`"$auth.provider.field"`)، مما يعني أنها قد تظهر في النتائج والسجلات والملفات الناتجة المحفوظة للاستعلامات.  غير مطلوب لمصادقة الموفرين العادية.  راجع [قيم الأسرار في SQL](/getting-started/provider-sessions#secret-values-in-sql).  متاح في StackQL من الإصدار `v1.0` فصاعدًا. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | مهلة طلب API بالثواني، و0 لعدم وجود مهلة | `45` |
| <span class="nowrap">`--approot`</span> | string | المسار الجذر لتكوين التطبيق وذاكرة التخزين المؤقت | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | وضع ملفات تكوين التطبيق وذاكرة التخزين المؤقت | `493` |
| <span class="nowrap">`--auth`</span> | string | أزواج المفاتيح والقيم لسياق `auth` بتنسيق json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | العدد الأولي لمفاتيح ذاكرة التخزين المؤقت | `100` |
| <span class="nowrap">`--configfile`</span> | string | المسار الكامل لملف التكوين | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | ملف `cpuprofile`، لا يوجد ملف إذا كانت القيمة فارغة | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | integer | الحد الأقصى لعمق تبعيات تدفق البيانات لاستعلام معين | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | integer | الحد الأقصى لعدد المكونات ضعيفة الاتصال في تدفق البيانات لاستعلام معين | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | سلسلة نصية بتنسيق JSON / YAML لتكوين معالجة استعلامات الصيانة الداخلية في DBMS | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | الفاصل لإخراج csv. حرف واحد فقط.  يتم تجاهله لكل إخراج غير csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | علامة `dryrun`؛ تشغيل المعالج المسبق فقط، ويعاد الإخراج الناتج عن القوالب | `false` |
| <span class="nowrap">`--env.file`</span> | string | ملف اختياري بنمط dotenv يتم تحميله في بيئة العملية عند بدء التشغيل (جميع الأوامر)؛ وتعيد الأداة [`reload_credentials`](/mcp/reload_credentials) في MCP تحميله عند الطلب. لا يعد غياب الملف خطأ؛ ويتم تخطي المفاتيح ذات القيم الفارغة؛ وتتم الكتابة فوق المتغيرات الموجودة. لا يؤخذ من الملف `.stackqlrc`. متاح في إصدارات StackQL بعد `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | integer | حد التزامن لتنفيذ الاستعلامات | `1` |
| <span class="nowrap">`--export.alias`</span> | string | بادئة الاسم المستعار للتصدير (مساحة الاسم أو المخطط) | |
| <span class="nowrap">`--gc`</span> | string | سلسلة نصية بتنسيق JSON / YAML تمثل تكوين GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | يعطل رؤوس الأعمدة، وهو صالح فقط مع `--output csv` أو `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | عرض معلومات طلبات http في الوحدة الطرفية | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | مضيف الخادم الوكيل (اتركه فارغًا لعدم استخدام خادم وكيل) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | كلمة مرور الخادم الوكيل | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | منفذ الخادم الوكيل، وأي رقم أقل من 0 أو يساويه يؤدي إلى استخدام المنفذ الافتراضي للبروتوكول المحدد (على سبيل المثال `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | بروتوكول الخادم الوكيل، `http` أو `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | مستخدم الخادم الوكيل | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | الحد الأقصى للنتائج لكل طلب http، وأي رقم أقل من 0 أو يساويه يعني عدم وجود حد | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | الحد الأقصى لعدد صفحات النتائج التي تعاد لكل مورد، وأي رقم أقل من 0 أو يساويه يعني عدم وجود حد | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | الحد الأقصى لعمق الاستعلامات غير المباشرة: طرق العرض والاستعلامات الفرعية | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | ملف الإدخال (ملف IQL) الذي تقرأ منه الاستعلامات | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | ملف السياق (البيانات) لاستخدام القوالب (ملف `json` أو `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | مستوى السجل، ويجب أن يكون أحد \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (ويُقبل `warning` كاسم مستعار لـ `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | كائن تكوين [خادم MCP](/command-line-usage/mcp#mcp-configuration-object) بتنسيق JSON / YAML (`stackql mcp` و`stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | ترميز سجل تدقيق MCP، `jsonl` أو `otel` (إدخالات سجل OTLP/JSON)؛ ويتجاوز `server.audit.format` في `--mcp.config`.  راجع [تنسيق السجل](/command-line-usage/mcp#log-format).  متاح في StackQL من الإصدار `v0.11.660` فصاعدًا. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | أحدث مراجعة لبروتوكول MCP يتم الإعلان عنها: `auto` أو `2026-07-28` (في وضع بدون جلسات فقط) أو مراجعة أقدم مثل `2025-11-25`؛ ويتجاوز `server.protocol_version` في `--mcp.config`.  راجع [قفل المراجعة](/command-line-usage/mcp#pinning-the-revision).  متاح في StackQL من الإصدار `v0.12.732` فصاعدًا. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | نوع خادم MCP، أحد \{`http`, `stdio`, `reverse_proxy`\}.  راجع [نوع خادم MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | مدة صلاحية (TTL) مستندات بيانات التعريف المخزنة مؤقتًا، بالثواني | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | سلسلة نصية بتنسيق JSON / YAML تمثل مساحات الأسماء للتخزين المؤقت وطرق العرض وغيرها | `{}` |
| <span class="nowrap">`--offline`</span> | flag | العمل دون اتصال باستخدام البيانات المخزنة مؤقتًا | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | ملف الإخراج الذي تكتب فيه النتائج | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | تنسيق الإخراج، ويجب أن يكون أحد \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (ويُقبل `ndjson` كاسم مستعار لـ `jsonl`).  يصدر التنسيق `otel` إدخالات سجل OpenTelemetry، راجع [أوضاع الإخراج](/getting-started/output-modes#otel-output)؛ وهو متاح في StackQL من الإصدار `v0.11.660` فصاعدًا.  عند تكوين exporter في `--otel.config`، يتم أيضًا دفع إدخالات `otel` إلى نقطة النهاية تلك | `table` |
| <span class="nowrap">`--otel.config`</span> | string | سلسلة نصية بتنسيق JSON / YAML لتكوين تنسيق الإخراج `otel`؛ تدفع المفاتيح `exporter.endpoint` و`exporter.headers` و`exporter.timeout_ms` و`exporter.batch_size` الإدخالات إلى نقطة نهاية سجلات OTLP/HTTP، راجع [الدفع إلى نقطة نهاية OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  متاح في StackQL من الإصدار `v0.12.732` فصاعدًا | `{}` |
| <span class="nowrap">`--preview`</span> | string | سلسلة نصية بتنسيق JSON لتمكين ميزات المعاينة.  يعرض `{"unstable": true}` العلاقات `stackql_unstable_<provider>.<service>.<resource>` المبنية على مستندات، والتي يحلها `omnisdk` ويشغلها مباشرة من مستندات الموفرين مع بث الصفوف فور إنتاجها؛ وتضبط المفاتيح المتبقية (`batchSize` و`flushInterval` و`endpoint` و`insecureSkipTLSVerify`) تلك الواجهة الخلفية.  ميزات المعاينة غير مستقرة: قد يتغير السلوك وبناء الجملة والإخراج أو يتعطل بين الإصدارات دون سابق إنذار، لذا لا تعتمد عليها في بيئة الإنتاج.  من الإصدار `v1.0`، يعرض التفعيل الاختياري نفسه العلاقتين `stackql_preview.iac.run` و`stackql_preview.iac.destroy`، اللتين تطبقان مجموعة موارد معلنة وتزيلانها عبر `omnisdk` مع الاحتفاظ بالحالة تحت المسار الجذر للتطبيق.  راجع [ميزات المعاينة](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | الموفر الافتراضي الأولي. على سبيل المثال، مع `--provider github`، يُحل `repos.repos` إلى `github.repos.repos` دون جملة `USE` سابقة. لا يجري المصادقة مع الموفر. | لا يوجد |
| <span class="nowrap">`--querycachesize`</span> | integer | حجم ذاكرة التخزين المؤقت LRU لخطط الاستعلامات بعدد الإدخالات | `10000` |
| <span class="nowrap">`--registry`</span> | string | سلسلة نصية بتنسيق JSON / YAML لتكوين موقع سجل الموفرين والتحقق من توقيعات مستندات الموفرين | راجع [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | سلسلة نصية بتنسيق JSON / YAML تمثل تكوين الجلسة | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | سلسلة نصية بتنسيق JSON / YAML تمثل تكوين نظام واجهة SQL الخلفية | راجع [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | سلسلة نصية بتنسيق JSON / YAML تمثل تكوين مخزن Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | مسار حزمة شهادات CA، وإذا لم يتم تحديده تستخدم الإعدادات الافتراضية للنظام |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | السماح بالثقة في الشهادات غير الآمنة (غير موصى به) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | سياسة بتنسيق JSON / YAML للتسامح الصريح مع حالات استجابة HTTP عند القراءة من الموفرين. راجع [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | متغيرات خارجية تقدم كأزواج مفاتيح وقيم لتحميلها في استعلامات StackQL من خلال ملفات بيانات `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | إخراج مفصل | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | تعليمات خاصة بالسياق لـ stackql | |
| <span class="nowrap">`--version`</span> | | يعرض إصدار برنامج StackQL | |

العلامة `--colorscheme` مهملة ولم تعد تغير ألوان واجهة الأوامر. ويتم قبول العلامة `--usenonpreferredapis` لكنها لا تغير اختيار واجهة API في المحرك الحالي.

## `--registry` {#--registry}

تستخدم العلامة `--registry` لتكوين المكان الذي تقرأ منه مستندات الموفرين، وما إذا كان يتم التحقق من توقيعاتها.  القيمة سلسلة نصية بتنسيق JSON تحتوي على المفاتيح التالية:

| المفتاح | الوصف | الافتراضي |
| -- | -- | -- |
| `url` | عنوان URL للسجل.  يتم استخدام سجل الموفرين العام الخاص بـ StackQL عند حذفه. | السجل العام |
| `localDocRoot` | دليل محلي يحتوي على مستندات الموفرين، تتم قراءتها بدلًا من تنزيلها من السجل.  يستخدم عند [تطوير موفر](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | بادئة المسار تحت جذر السجل حيث توجد مستندات المصدر للموفرين. | `src` |
| `verifyConfig.nopVerify` | تؤدي القيمة `true` إلى تخطي التحقق من التوقيع.  عيّن القيمة إلى `false` لاشتراط توقيع ML-DSA-65 (FIPS 204) مضمن في كل مستند موفر وخدمة تتم قراءته، بما في ذلك علاقات المعاينة المبنية على مستندات. | `true` |
| `verifyConfig.publicKeys` | كائن JSON يربط معرفات المفاتيح بمفاتيح ML-DSA-65 عامة مرمزة بالنظام الست عشري، للسجلات الخاصة.  يحل محل مفاتيح الإنتاج المضمنة؛ أما تحديد كائن فارغ صراحة فهو خطأ. | المفاتيح المضمنة |

### مثال: اشتراط مستندات موفرين موقعة رقميًا {#example-require-signed-provider-documents}

تمكين التحقق لكل مستند موفر وخدمة تتم قراءته من السجل العام، مع التحقق منه مقابل مفاتيح الإنتاج المضمنة:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### مثال: قراءة مستندات غير موقعة من دليل محلي {#example-read-unsigned-documents-from-a-local-directory}

يوجه هذا الأمر السجل إلى دليل يحتوي على مستندات موفرين قيد التطوير.  وهي غير موقعة، لذا لا يتم تمكين التحقق:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

التحقق من التوقيع متاح في StackQL من الإصدار `v1.0` فصاعدًا، وهو معطل افتراضيًا.  تغطي التوقيعات بايتات YAML الدقيقة للمستند، لذا فإن أي تعديل، بما في ذلك تغيير في التنسيق أو في الأسطر الجديدة، يبطلها؛ أعد التوقيع باستخدام الأداة `pqsig sign-doc` الخاصة بالسجل.  يجب أن تحتفظ مستندات التطوير المحلية غير الموقعة بالقيمة `nopVerify: true`.  لم تعد خيارات الشهادات السابقة وملفات sidecar ذات الامتداد `.sig` مستخدمة، ويجب إعادة نشر الملفات المضغوطة التي نشرت مع ملفات sidecar فقط قبل استخدامها مع التحقق.  يجري التحقق مصادقة كل مستند عند قراءته؛ ولا يشمل تنزيل الملفات المضغوطة واستخراجها.

:::

## `--sqlBackend` {#--sqlbackend}

تستخدم العلامة `--sqlBackend` لتكوين واجهة SQL خلفية خارجية لـ StackQL، مما يتيح لك استخدام محرك قاعدة بيانات خارجي (مثل PostgreSQL) بدلًا من محرك SQL المضمن الافتراضي. يفيد هذا في الاستفادة من قاعدة بيانات خارجية لمعالجة الاستعلامات أو للتخزين الدائم أو للتكامل مع البنية التحتية الحالية لقواعد البيانات.

القيمة سلسلة نصية بتنسيق JSON تحتوي على المفاتيح التالية:

| المفتاح | الوصف | أمثلة على القيم |
| -- | -- | -- |
| `dbEngine` | نوع محرك قاعدة البيانات | `postgres_tcp` |
| `sqlDialect` | لهجة SQL المراد استخدامها | `postgres` |
| `dsn` | اسم مصدر البيانات (سلسلة الاتصال) لقاعدة البيانات | راجع الأمثلة أدناه |

### مثال: استخدام PostgreSQL كواجهة SQL خلفية {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### مثال: استخدام واجهة PostgreSQL في Databricks {#example-using-databricks-postgresql-interface}

يعرض Databricks واجهة متوافقة مع PostgreSQL يمكن استخدامها كواجهة SQL خلفية لـ StackQL. تستخدم قيمة DSN التنسيق القياسي لسلسلة اتصال PostgreSQL مع بيانات اعتماد Databricks الخاصة بك:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
يجب ترميز الأحرف الخاصة في اسم المستخدم أو كلمة المرور (مثل `@`) بتنسيق URL في قيمة DSN (على سبيل المثال، يصبح `@` هو `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

استخدم المفتاح `http.ignoreStatusCodes` لسرد حالات استجابة HTTP التي يجب أن تتسامح معها عملية القراءة من الموفر صراحة. الإدخالات سلاسل نصية: استخدم رمزًا دقيقًا مثل `"503"` أو فئة مثل `"5xx"`. لا تضيف القيمة الافتراضية `{}` أي استثناءات إلى المعالجة العادية التي يجريها المحرك لاستجابات المصدر. لا يعيد هذا الإعداد محاولة الطلبات الفاشلة، ولا ينطبق على عمليات تعديل البيانات أو عمليات تبادل المصادقة.

لا تساهم الاستجابة التي يتم تجاهلها بأي صفوف. يمكن لعمليات القراءة الأخرى في الاستعلام أن تعيد صفوفًا مع ذلك، لذا قد يعيد الاستعلام الناجح نتائج غير مكتملة. يبلغ StackQL عن حالة HTTP التي تم تجاهلها كرسالة تشخيصية.

على سبيل المثال، يستخدم [اختبار ربط GitHub باستخدام المحاكاة](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104) هذا الخيار:

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

في الاختبار، يعيد طلب المتعاونين لأحد المستودعات HTTP `500`. ومع ذلك يعيد الاستعلام صفوف المتعاونين للمستودعات الأخرى، ويتضمن stderr السطر `ignoring upstream response: 500 INTERNAL SERVER ERROR`. يفضل استخدام رمز حالة دقيق عندما لا تتوقع سوى فشل واحد.
