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
  - جرد الموارد السحابية
description: "يشغل الأمر stackql mcp StackQL كخادم Model Context Protocol لوكلاء الذكاء الاصطناعي: أوضاع النشر وأوضاع الأمان وإعادة تحميل بيانات الاعتماد وسجل التدقيق."
image: "/img/stackql-featured-image.png"
---

الأمر المستخدم لتشغيل StackQL كخادم Model Context Protocol (MCP)، مما يتيح لوكلاء ومساعدي الذكاء الاصطناعي التفاعل مع البنية التحتية السحابية باستخدام قدرات الاستعلام في StackQL.

:::info[ما هو MCP؟]

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/) بروتوكول مفتوح يوحد كيفية تفاعل تطبيقات الذكاء الاصطناعي مع مصادر البيانات والأدوات الخارجية. عند تشغيل StackQL كخادم MCP، يمكنك تمكين وكلاء الذكاء الاصطناعي (Claude وCursor وContinue والوكلاء المخصصين وغيرها) من الاستعلام عن البنية التحتية السحابية وإدارتها عبر موفرين متعددين باستخدام اللغة الطبيعية.

:::

* * *

### بناء الجملة {#syntax}

`stackql mcp [flags]` لخادم MCP مستقل.

`stackql srv [flags]` لتشغيل خادم MCP إلى جانب خادم بروتوكول الاتصال الخاص بـ PostgreSQL في العملية نفسها.

* * *

### أوضاع النشر {#deployment-modes}

يمكن نشر خادم MCP في StackQL بثلاثة تكوينات مختلفة لتلبية متطلبات معمارية متنوعة. تستخدم الأمثلة أدناه التفعيل الاختياري للخدمة بدون مصادقة عبر `"allow_unauthenticated": true`، وهو ما يناسب التطوير المحلي؛ أما لأي خادم يمكن لعملية أو جهاز آخر الوصول إليه، فاستخدم المصادقة برمز مميز من نوع bearer بدلًا من ذلك (راجع [مصادقة عملاء HTTP](#http-client-authentication)).

#### 1. خادم MCP مستقل {#1-standalone-mcp-server}

شغّل StackQL كخادم MCP مخصص على منفذ محدد.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**حالة الاستخدام:** عندما تحتاج فقط إلى الوصول عبر بروتوكول MCP ولا تحتاج إلى التوافق مع بروتوكول الاتصال الخاص بـ PostgreSQL.

#### 2. خادم MCP + PostgreSQL (في الذاكرة) {#2-mcp--postgresql-server-in-memory}

شغّل خادمي MCP وPostgreSQL في وقت واحد مع اتصال داخل الذاكرة بينهما.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**حالة الاستخدام:** عندما تحتاج إلى الوصول عبر بروتوكول MCP لوكلاء الذكاء الاصطناعي وإلى بروتوكول الاتصال الخاص بـ PostgreSQL لعملاء قواعد البيانات التقليدية معًا، مع أقصى أداء بفضل الاتصال داخل الذاكرة.

#### 3. خادم MCP + PostgreSQL (وكيل عكسي) {#3-mcp--postgresql-server-reverse-proxy}

شغّل الخادمين مع اتصال قائم على TCP، بما يدعم عمليات النشر الموزعة وتشفير TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**مع تشفير TLS:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

يشفر TLS وسيلة النقل لكنه لا يوفر مصادقة العميل، لذلك يتطلب هذا المثال أيضًا الرمز المميز من نوع bearer المخزن في متغير البيئة `STACKQL_MCP_TOKEN`.

**حالة الاستخدام:** عندما تحتاج إلى فصل أحمال عمل MCP وPostgreSQL عبر عمليات أو أجهزة مختلفة، أو عندما تحتاج إلى تشفير TLS لنقطة نهاية MCP.

* * *

### خيارات التكوين {#configuration-options}

#### نوع خادم MCP {#mcp-server-type}

| النوع | الوصف |
|--|--|
|`http`|وضع خادم HTTP المباشر - يعالج محرك StackQL طلبات MCP داخل العملية نفسها.|
|`reverse_proxy`|وضع الوكيل العكسي - يرسل الخادم طلبات MCP إلى خادم PostgreSQL أساسي عبر DSN المحدد في التكوين. للاستخدام مع `stackql srv`.|
|`stdio`|وسيلة نقل الإدخال/الإخراج القياسي - يقرأ الخادم الطلبات من `stdin` ويكتب الاستجابات إلى `stdout`. يستخدمها عملاء MCP المضمنون في المحررات (Claude Desktop وCursor وغيرها).|

#### تنسيق سجل MCP {#mcp-log-format}

تحدد العلامة `--mcp.log.format` ترميز سجل التدقيق: `jsonl` (افتراضي) أو `otel` (إدخالات سجل OpenTelemetry بتنسيق OTLP/JSON). تتجاوز هذه العلامة قيمة `server.audit.format` في كائن التكوين. راجع [تنسيق السجل](#log-format).

#### إصدار بروتوكول MCP {#mcp-protocol-version}

تحدد العلامة `--mcp.protocol.version` أحدث مراجعة للبروتوكول يعلن عنها الخادم: `auto` (افتراضي، كل المراجعات المدعومة) أو `2026-07-28` (بدون جلسات فقط) أو مراجعة أقدم مثل `2025-11-25` (تلك المراجعة وما قبلها). تتجاوز هذه العلامة قيمة `server.protocol_version` في كائن التكوين. راجع [قفل المراجعة](#pinning-the-revision).

#### كائن تكوين MCP {#mcp-configuration-object}

تقبل العلامة `--mcp.config` كائن JSON بالبنية التالية.

##### تكوين الخادم {#server-configuration}

| الحقل | الوصف | مطلوب |
|--|--|--|
|`server.transport`|بروتوكول النقل - `http` أو `stdio`.|نعم|
|`server.address`|العنوان والمنفذ اللذان يرتبط بهما خادم MCP (على سبيل المثال، `127.0.0.1:9912`). لوسيلة النقل HTTP فقط.|لـ `http`|
|`server.tls_cert_file`|مسار ملف شهادة TLS لبروتوكول HTTPS.|لا|
|`server.tls_key_file`|مسار ملف مفتاح TLS الخاص لبروتوكول HTTPS.|لا|
|`server.auth_token_env_var`|اسم متغير البيئة الذي يحتوي على رمز مميز من نوع bearer مشترك مسبقًا. عند تعيينه، يجب أن يحمل كل طلب HTTP الرأس `Authorization: Bearer <token>`؛ ويرفض الخادم البدء إذا كان المتغير غير معين أو كانت قيمته فارغة. راجع [مصادقة عملاء HTTP](#http-client-authentication).|أحد الحقلين: هذا أو `allow_unauthenticated`، لـ `http`|
|`server.allow_unauthenticated`|تفعيل صريح لتقديم وسيلة النقل HTTP بدون مصادقة العميل. القيمة الافتراضية: `false`.|أحد الحقلين: هذا أو `auth_token_env_var`، لـ `http`|
|`server.mode`|عقد الأمان الذي يقيد عمليات تعديل البيانات وعمليات دورة الحياة. إحدى القيم: `read_only` أو `safe` (افتراضي) أو `delete_safe` أو `full_access`. راجع [أوضاع الخادم](#server-modes).|لا|
|`server.read_only`|اسم مستعار منطقي **قديم** لـ `mode: read_only`. عند تعيين الحقلين معًا، تكون الأولوية لـ `mode`.|لا|
|`server.stateless`|تقديم Streamable HTTP بدون جلسات (بدون `Mcp-Session-Id`)، وهي كيفية تقديم مراجعة البروتوكول `2026-07-28` عبر HTTP. القيمة الافتراضية: `false`. يتم تجاهله مع `stdio`. راجع [دعم مراجعات البروتوكول](#protocol-revision-support).|لا|
|`server.protocol_version`|أحدث مراجعة للبروتوكول يعلن عنها الخادم: `auto` (افتراضي) أو `2026-07-28` أو مراجعة أقدم مثل `2025-11-25`. تستلزم القيمة `2026-07-28` تمكين `stateless` لـ Streamable HTTP. راجع [قفل المراجعة](#pinning-the-revision).|لا|
|`server.audit`|تكوين نظام التدقيق الفرعي، بما في ذلك `audit.format` (`jsonl` أو `otel`). راجع [سجل التدقيق](#audit-log).|لا|

##### تكوين الواجهة الخلفية (وضع الوكيل العكسي فقط) {#backend-configuration-reverse-proxy-mode-only}

| الحقل | الوصف | مطلوب |
|--|--|--|
|`backend.dsn`|سلسلة اتصال PostgreSQL لخادم StackQL الخلفي.|نعم (لـ `reverse_proxy`)|

<br />

:::info

ينبغي أن يتضمن DSN الخاص بالواجهة الخلفية المعلمة `default_query_exec_mode=simple_protocol` لتحقيق أفضل توافق.

:::

##### قوائم السماح {#allowlists}

| الحقل | الوصف |
|--|--|
|`enabled_tools`|مصفوفة بأسماء الأدوات التي يحق للخادم نشرها. عند حذفها أو تركها فارغة، يتم تسجيل كل الأدوات المدمجة. تفيد في إتاحة مجموعة فرعية محدودة (على سبيل المثال، خادم جرد للقراءة فقط).|
|`enabled_prompts`|مصفوفة بأسماء الموجهات. لها دلالة `enabled_tools` نفسها، لكن لواجهة الموجهات.|

<br/>

مثال - خادم ينشر الأداتين `server_info` و`list_providers` فقط:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### دعم مراجعات البروتوكول {#protocol-revision-support}

مراجعة البروتوكول `2026-07-28` والحقل `server.stateless` وتنسيق سجل التدقيق `otel` متاحة في StackQL من الإصدار `v0.11.660` فصاعدًا. الحقل `server.protocol_version` والعلامة `--mcp.protocol.version` متاحان في StackQL من الإصدار `v0.12.732` فصاعدًا.

يتحدث الخادم بكل مراجعات Model Context Protocol التي تدعمها حزمة [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) ويتفاوض مع كل عميل على حدة، لذا تعمل مجموعة متنوعة من العملاء مع خادم واحد.

| المراجعة | دورة الحياة | طلب الموافقة (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (المعلن عنها)|بدون مصافحة: `server/discover`، ثم يحمل كل طلب `io.modelcontextprotocol/protocolVersion` و`io.modelcontextprotocol/clientCapabilities` في `_meta` (بالإضافة إلى الرؤوس `Mcp-Protocol-Version` و`Mcp-Method` و`Mcp-Name` عبر HTTP).|نتيجة `input_required` يعيد العميل بعدها الاستدعاء مع `inputResponses` (SEP-2322).|
|`2025-11-25`، `2025-06-18`|مصافحة `initialize` / `notifications/initialized`.|طلب `elicitation/create` يبدؤه الخادم.|
|`2025-03-26`، `2024-11-05`|كما سبق.|كما سبق.|

<br />

تقدم وسيلة النقل **stdio** كل المراجعات في عملية واحدة: تتم خدمة الطلب الأول من عميل `2026-07-28` بدون مصافحة، ويظل `initialize` من عميل أقدم يعمل. لا يلزم أي تكوين لـ Claude Desktop أو أدوات التشغيل من npm / PyPI أو صورة Docker.

يستخدم **Streamable HTTP** افتراضيًا الآلية ذات الحالة، بجلسة لكل عميل (`Mcp-Session-Id`)، التي تقدمها حزمة SDK للمراجعات حتى `2025-11-25`. يستجيب الخادم لطلب `2026-07-28` بخطأ JSON-RPC `-32022` (إصدار بروتوكول غير مدعوم؛ ومجرد استجابة HTTP 400 في الإصدارات حتى `v0.12.718`) يسرد الحقل `data.supported` فيه مراجعات المصافحة، ويعلن `server/discover` عن المراجعات نفسها، فيتفاوض العميل على مراجعة أقدم وتحتفظ تكاملات HTTP الحالية بجلساتها وطلبات الموافقة الخاصة بها دون تغيير. عيّن `server.stateless` (أو اقفل المراجعة على `2026-07-28`، [أدناه](#pinning-the-revision)) لتقديم `2026-07-28` بشكل أصلي عبر HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

لا يصدر الخادم الذي يعمل بدون جلسات أي `Mcp-Session-Id`، ويستجيب لـ `GET` و`DELETE` برمز الحالة 405 (لا توجد جلسة لإنهائها)، ويبقي نتائج `tools/list` و`prompts/list` و`resources/list` ثابتة بغض النظر عن الاتصال، وينفذ جولة الموافقة عبر `input_required`. ولا يزال يقبل `initialize` من عميل أقدم ويقدم له عمليات القراءة، لكنه لا يستطيع الاحتفاظ بقدرة elicitation التي أعلنها ذلك العميل عند التهيئة (يحصل كل طلب على جلسة مؤقتة)، لذا لا يستطيع العملاء الأقدم الموافقة على عمليات الكتابة التي تتطلب موافقة على خادم بدون جلسات. اختر `stateless` لتطبيقات المضيف التي تستخدم المراجعة الحالية، والإعداد الافتراضي لمجموعة لا تزال تضم عملاء أقدم.

لا يحتفظ الخادم بأي حالة بين الاستدعاءات: الوضع والتدقيق وبيانات اعتماد الموفرين تكوين على مستوى العملية، لذا لا يلزم نقل أي شيء خلف مقابض صريحة.

#### قفل المراجعة {#pinning-the-revision}

افتراضيًا، يعلن الخادم عن كل المراجعات المدعومة ويتفاوض مع كل عميل على حدة. تحدد العلامة `--mcp.protocol.version`، أو الحقل `server.protocol_version` في كائن التكوين (الأولوية للعلامة)، أحدث مراجعة يعلن عنها الخادم:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| القيمة | المراجعات المعلن عنها | التأثير |
|--|--|--|
|`auto` (افتراضي) أو غير محدد|كل المراجعات المدعومة.|التفاوض كما سبق: أعلى مراجعة يدعمها الطرفان.|
|`2026-07-28`|`2026-07-28` فقط.|بدون جلسات فقط، ويستلزم ذلك تمكين `stateless` لـ Streamable HTTP. يستجيب الخادم لـ `initialize` من عميل أقدم بالمراجعة `2025-11-25`، وهي إشارة لذلك العميل لقطع الاتصال بدلًا من قراءة الإجابة على أنها دورة الحياة الجديدة.|
|`2025-11-25` (أو أي مراجعة أقدم)|تلك المراجعة وكل ما قبلها.|دورة حياة المصافحة فقط: يتلقى طلب `2026-07-28` خطأ JSON-RPC `-32022` مع الحقل `data.supported` الذي يسرد المراجعات المعلن عنها، فيستطيع العميل إعادة التفاوض. ويحتفظ عملاء المصافحة الأقدم بمراجعتهم الخاصة.|

<br />

تفشل أي قيمة أخرى في التحقق من صحة التكوين عند بدء التشغيل، ويذكر الخطأ القيم المسموح بها.

#### حدود وسيلة النقل {#transport-limits}

يقتصر حجم نص الطلب في Streamable HTTP على 4 MiB (مع استجابة HTTP 413 عند تجاوزه)، ويرفض الخادم JSON المتداخل بعمق يزيد على 1000 مستوى قبل تحليله، ويقتصر حجم إطار `stdio` الواحد على 8 MiB. لا يمكن تكوين أي من هذه الحدود؛ وكلها أعلى بكثير من أي جملة SQL يقبلها الخادم.

* * *

### (إعادة) تحميل بيانات الاعتماد (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

العلامة `--env.file` والأداة `reload_credentials` متاحتان في إصدارات StackQL بعد `v0.10.542`.

:::

تكون كتلة بيئة العملية ثابتة منذ تشغيلها. ويسبب ذلك مشكلتين لخوادم MCP:

- لا يمكن لخادم MCP بوسيلة النقل `stdio` يشغله عميل سطح مكتب (Claude Desktop وCursor وغيرها) بدون متغيرات البيئة الخاصة ببيانات الاعتماد أن يراها أبدًا - إذ يمرر العميل بيئته الخاصة إلى العملية الفرعية.
- بيانات الاعتماد قصيرة الأجل التي يتم تدويرها لا تصل أبدًا إلى جلسة خادم تعمل لفترة طويلة.

تسد العلامة `--env.file` الفجوتين بتحديد ملف بتنسيق dotenv يعمل كمخزن قابل للتعديل لبيانات الاعتماد:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### دلالات التحميل {#sourcing-semantics}

يتم تحميل الملف إلى بيئة العملية عند بدء التشغيل لكل نقطة دخول (`exec` و`shell` و`srv` و`registry` و`mcp`):

- عندما تكون العلامة فارغة أو غير معينة، لا يحدث شيء - تُحل بيانات الاعتماد عند الحاجة من بيئة العملية وقت الطلب، تمامًا كالسلوك المعتاد سابقًا.
- غياب الملف في المسار المحدد مقبول ولا يترتب عليه أي إجراء (قد ينشأ الملف لاحقًا).
- يتسبب الملف غير جيد التكوين أو غير القابل للقراءة في فشل بدء التشغيل.
- لا يتم تعيين سوى المفاتيح ذات القيم غير الفارغة؛ و**تتم** الكتابة فوق متغيرات البيئة الموجودة في العملية (الملف هو المصدر الموثوق)؛ ولا يتم إلغاء تعيين أي متغير أبدًا.
- العلامة متاحة في سطر الأوامر فقط - ولا يتم تحميلها من الملف `.stackqlrc`.

تنسيق الملف هو `KEY=VALUE` واحد في كل سطر. ويقبل الملف تعليقات `#` والأسطر الفارغة والبادئة الاختيارية `export ` وعلامات الاقتباس المفردة أو المزدوجة الاختيارية حول القيمة ونهايات الأسطر CRLF:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

تعيد الأداة [`reload_credentials`](/mcp/reload_credentials) تحميل ملف `--env.file` إلى بيئة العملية أثناء الجلسة، ثم تبلغ عن حالة حل بيانات الاعتماد لكل موفر. لا تعيد الأداة القيم السرية أبدًا ولا تكتبها في السجلات ولا في سجل التدقيق - الأسماء والحالات فقط. وبدون تكوين `--env.file` تقتصر الأداة على فحص الحالة فقط.

عندما يفشل استعلام في حل بيانات الاعتماد، يحتوي خطأ MCP على تلميح يوجه الوكيل إلى استدعاء `reload_credentials` وإعادة المحاولة، فيصلح العملاء القائمون على الوكلاء المشكلة ذاتيًا: اكتب بيانات الاعتماد في الملف، واطلب من الوكيل إعادة التحميل، فينجح الاستعلام الفاشل عند إعادة المحاولة - دون الحاجة إلى إعادة تشغيل الخادم.

الأداة `reload_credentials` غير مدعومة في وضع الواجهة الخلفية `reverse_proxy` - إذ يتم تنفيذ الاستعلامات في عملية خادم stackql البعيد، لذا تعيد الأداة خطأ يوجه المستخدم إلى إعادة التحميل على الخادم الأساسي.

#### Windows وClaude Desktop {#windows-and-claude-desktop}

يكتب الأمر `setx` في سجل Windows، لا في العمليات قيد التشغيل، ويمرر Claude Desktop بيئته الخاصة (غير المحدثة) إلى عمليات MCP الفرعية - لذا تتطلب تغييرات بيانات الاعتماد في العادة إعادة تشغيل كاملة لـ Claude Desktop. مع `--env.file` يمكن إنشاء ملف بيانات الاعتماد أو تدويره في أي وقت، وتلتقطه الأداة `reload_credentials` دون أي إعادة تشغيل. راجع [استخدام StackQL مع Claude Desktop](/getting-started/claude-desktop) للاطلاع على مثال تكوين كامل.

* * *

### مصادقة عملاء HTTP {#http-client-authentication}

:::note

الحقلان `server.auth_token_env_var` و`server.allow_unauthenticated` متاحان في StackQL من الإصدار `v0.12.742` فصاعدًا.

:::

تتطلب وسيلة النقل HTTP مصادقة العميل. يحدد الحقل `server.auth_token_env_var` اسم متغير البيئة الذي يحتوي على رمز مميز من نوع bearer مشترك مسبقًا؛ ويجب أن يحمل كل طلب الرأس `Authorization: Bearer <token>` وإلا يستجيب الخادم برمز الحالة `401`. يقرأ الخادم الرمز المميز من البيئة، لذا لا يظهر أبدًا في `--mcp.config` أو في وسيطات العملية.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[مصادقة الخادم مطلوبة]

لا تتم الخدمة بدون رمز مميز إلا بتفعيل صريح: عيّن `"allow_unauthenticated": true` في الكائن `server`. في غياب كلا المفتاحين، يتوقف الخادم عند بدء التشغيل برسالة تذكر المفتاحين؛ ويتوقف أيضًا إذا كان متغير الرمز المميز غير معين أو كانت قيمته فارغة. اعتبارًا من الإصدار `v0.12.742`، يجب أن تضيف `auth_token_env_var` أو `"allow_unauthenticated": true` إلى `server` عند استخدام وسيلة النقل `http`.

:::

يشفر TLS (`tls_cert_file` / `tls_key_file`) وسيلة النقل لكنه لا يوفر مصادقة العميل؛ لذا استخدمه مع رمز مميز لأي مستمع خارج عنوان الاسترجاع. يرفض الخادم طلبات المتصفح من أصل مختلف (رأس `Origin` غير مطابق، أو `Sec-Fetch-Site` يشير إلى موقع مختلف) برمز الحالة `403` بغض النظر عن المصادقة. لا يرسل العميل المرفق `stackql_mcp_client` أي رأس `Authorization`، لذا يجب أن يستخدم الخادم الذي يتعامل معه التفعيل الاختياري للخدمة بدون مصادقة.

بدءًا من `v1.0`، يؤدي تشغيل الخادم مع [`--access.config`](/command-line-usage/access-management) إلى استبدال التحقق بالرمز المميز المشترك بمصادقة كل جهة مستدعية على حدة: يتم التحقق من كل طلب HTTPS باستخدام آلية JWT أو رمز bearer الثابت أو TLS المتبادل المحددة في التكوين، ويتم تخويله وفق قواعد السياسات. TLS للخادم إلزامي في هذه الحالة، ويرفض الخادم مصادقة `local_os` عبر HTTP، ولا يستخدم الطلب الذي لا يحمل بيانات اعتماد الرمز المميز الموجود في بيئة الخادم كبديل احتياطي أبدًا. إدارة الوصول غير متاحة مع نوع الخادم `reverse_proxy`.

* * *

### أوضاع الخادم {#server-modes}

يحدد الحقل `server.mode` أحد عقود الأمان الأربعة. تسمح العقود الأربعة كلها بجمل `SELECT` وعمليات قراءة بيانات التعريف؛ وتختلف في كيفية تعاملها مع عمليات تعديل البيانات وعمليات دورة الحياة.

| الوضع | `SELECT` / بيانات التعريف | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (دورة الحياة) |
|--|--|--|--|--|
|`read_only`|سماح|رفض|رفض|رفض|
|`safe` (افتراضي)|سماح|يتطلب موافقة|يتطلب موافقة|يتطلب موافقة|
|`delete_safe`|سماح|سماح|يتطلب موافقة|يتطلب موافقة|
|`full_access`|سماح|سماح|سماح|سماح|

<br />

**رفض** يعيد خطأ إلى العميل فورًا.

**يتطلب موافقة** يستخدم تدفق elicitation في MCP، ويتحدد شكله حسب [مراجعة البروتوكول](#protocol-revision-support) التي تم التفاوض عليها:

- إذا أعلن العميل عن قدرة elicitation (عند التهيئة، أو في قدرات العميل ضمن `_meta` لكل طلب في `2026-07-28`)، يطلب الخادم من المستخدم الموافقة على الإجراء برسالة قصيرة (اسم الأداة وفئة الاستعلام وSQL). في `2026-07-28` يعيد الخادم طلب الموافقة كنتيجة `input_required` تحمل طلب إدخال `elicitation/create` تحت المعرف `stackql_approval`، ويعيد العميل محاولة الاستدعاء مع الإجابة في `inputResponses`؛ أما في المراجعات السابقة فيرسل الخادم طلب `elicitation/create` بنفسه أثناء الاستدعاء. يوافق المستخدم أو يرفض أو يلغي.
- إذا **لم** يعلن العميل عن elicitation، يرفض الخادم استدعاء الأداة برسالة توضح النقص وتوجه المشغل إلى الوضع `full_access`.

يتولى العميل الإجابة عن طلب الموافقة، لذا تحمي الموافقة من وكيل يتصرف دون علم مستخدمه، لا من جهة مستدعية غير موثوقة. استخدم وسيلة النقل HTTP مع [مصادقة العملاء](#http-client-authentication) حيث لا تكون الجهات المستدعية موثوقة.

الوضع عام على مستوى الخادم. لا يوجد تجاوز لكل أداة على حدة.

بدءًا من `v1.0`، ترفض الأداة [`run_select_query`](/mcp/run_select_query) جمل `INSERT` و`UPDATE` و`REPLACE` و`DELETE` و`EXEC` في كل الأوضاع، بما في ذلك `full_access`؛ ويذكر الخطأ اسم `run_mutation_query` أو `run_lifecycle_operation`. تحمل كل أداة أيضًا الحقل `title` وتلميحات السلوك الأربعة في MCP كقيم منطقية صريحة (`readOnlyHint` و`destructiveHint` و`idempotentHint` و`openWorldHint`)، وهي مشتقة من التصنيف نفسه الذي تفرضه آلية التقييد بالسياسات. قيمة `openWorldHint` هي `true` للأدوات `list_registry` و`pull_provider` و`run_select_query` و`run_mutation_query` و`run_lifecycle_operation`، التي تصل إلى نقاط نهاية السجل أو الموفرين، و`false` للأدوات التي تقرأ الحالة المحلية فقط. التلميحات استشارية لتجربة المستخدم في العميل؛ ويبقى الإنفاذ من مهام آلية التقييد بالسياسات.

:::info[تقييد الحمولة]

بدءًا من الإصدار `v0.12.742`، تخضع الحمولة التي تحتوي على عدة جمل للتقييد وفق الجملة صاحبة أعلى امتياز فيها في كل الأوضاع، لذا يعامل الخادم `SELECT 1; DELETE ...` كجملة `DELETE` ويحمل إدخال سجل التدقيق `query_class` بالقيمة `mutation_delete`. تقبل الأداة `validate_select_query` جملة واحدة بالضبط.

:::

#### تغيير سلوك الوضع الافتراضي {#default-mode-behaviour-change}

كانت الإصدارات السابقة تفرض الأمان فقط عبر علامة واحدة `read_only: true/false` قيمتها الافتراضية "بدون فرض". يجعل الوضع الافتراضي الحالي (`mode: safe`) عمليات تعديل البيانات تتطلب موافقة المستخدم دون أي إعداد إضافي. سيظهر للمشغلين الذين يستخدمون عميل MCP يدعم elicitation (Claude Desktop وCursor وContinue وغيرها) طلب موافقة واحد لكل عملية تعديل بيانات. أما المشغلون الذين يشغلون أدوات أتمتة، أو العميل المرفق `stackql_mcp_client` (الذي لا يعلن عن elicitation)، فيجب عليهم التفعيل الصريح لوضع أقل تقييدًا: `mode: delete_safe` لتشغيل `INSERT` و`UPDATE` و`REPLACE`، أو `mode: full_access` لتشغيل `DELETE` وعمليات دورة الحياة أيضًا.

لا يزال StackQL يقبل المفتاح القديم `read_only: true` في JSON / YAML للتوافق مع الإصدارات السابقة، ويعامله على أنه مكافئ لـ `mode: read_only`.

#### أمثلة {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE/REPLACE proceed; DELETE and EXEC need approval.
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

### سجل التدقيق {#audit-log}

ينتج عن كل استدعاء أداة إدخال واحد في وجهة التدقيق المحددة في التكوين، بتنسيق JSONL افتراضيًا أو كإدخالات سجل OpenTelemetry (راجع [تنسيق السجل](#log-format)). التدقيق **يعمل افتراضيًا**. يجيب التدقيق عن سؤال "ماذا فعل الوكيل"، لا "ماذا رأى الوكيل" - إذ لا يسجل التدقيق عمدًا صفوف النتائج الناتجة عن جمل `SELECT`، في أي من التنسيقين.

#### ما يتم تسجيله {#what-gets-recorded}

| الحقل | الوصف |
|--|--|
|`timestamp`|وقت بدء الاستدعاء بحسب الساعة الفعلية (RFC3339).|
|`tool`|اسم الأداة (على سبيل المثال، `run_select_query`).|
|`mode`|وضع الخادم الساري وقت الاستدعاء.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`. للحمولة التي تحتوي على عدة جمل، فئة الجملة صاحبة أعلى امتياز.|
|`sql`|سلسلة SQL لأدوات الاستعلام (`run_select_query` و`run_mutation_query` و`run_lifecycle_operation` و`validate_select_query`).|
|`args`|حقول التسلسل الهرمي لأدوات بيانات التعريف (`list_*` و`describe_method`)؛ وSQL + `row_limit` لأدوات الاستعلام.|
|`duration_ms`|المدة الفعلية لآلية التقييد + المعالج.|
|`error`|رسالة الخطأ إذا فشلت الأداة أو تم رفضها.|

<br />

يستبعد التدقيق عمدًا صفوف النتائج الناتجة عن جمل `SELECT` - إذ قد تكون كبيرة جدًا وقد تحتوي على بيانات حساسة.

#### وجهة الملف {#file-sink}

الوجهة الوحيدة المتوفرة حاليًا هي `file`. كائن JSON واحد في كل سطر، مع استدعاء fsync بعد كل إدخال، وتدوير مماثل لـ lumberjack حسب الحجم / العمر / عدد النسخ الاحتياطية.

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

يمكن تحديد الموقع بإحدى وسيلتين:

- `audit.file.path` - مسار ملف كامل (مطلق، أو نسبي إلى cwd).
- `audit.file.dir` - دليل؛ تختار الوجهة الاسم الأساسي `stackql_mcp_server_<RFC3339-utc-second>.log` داخله.

عندما لا يتم تعيين أي منهما في `mcp.config`، يستخدم خادم MCP الدليل cwd كقيمة افتراضية لـ `dir` كي يرى المشغلون الحاليون السلوك نفسه كما في السابق. وترفض حزمة الوجهة العامة الأساسية نفسها اختيار دليل بصمت - إذ تتخذ الجهة المستدعية دائمًا قرار "أين تنتهي السجلات".

يكتب الخادم المسار المطلق الذي تم حله في stderr عند بدء التشغيل بالشكل `sink file: /path/to/file.log`.

#### تنسيق السجل {#log-format}

لتدفق التدقيق ترميزان، ويكتب الخادم كليهما في وجهة الملف نفسها:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

تتجاوز العلامة قيمة `server.audit.format` في `mcp.config` (`"audit": {"format": "otel"}`). التنسيق `jsonl` متوافق على مستوى البايت مع الإدخالات الموضحة أعلاه. أما التنسيق `otel` فيكتب في كل سطر كائن OTLP/JSON `LogsData` واحد، وهو الشكل الذي يستوعبه receiver `otlp_json_file` في OpenTelemetry Collector كما هو (تم التحقق من ذلك مع otelcol-contrib 0.160.0)، لذا يصل التدفق إلى أي خط أنابيب OTLP دون أي معالج تحويل:

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

ينتج عن كل استدعاء أداة إدخال سجل واحد؛ وينتج عن الاستدعاء الذي مر بآلية الموافقة إدخال ثان لقرار elicitation. تشترك إدخالات الاستدعاء الواحد في `gen_ai.tool.call.id` وفي معرف تتبع. يحترم الخادم قيمة `traceparent` من W3C التي توفرها الجهة المستدعية في `params._meta`؛ وإلا فإن الخادم ينشئ معرف التتبع على مستوى جلسة MCP، فيكون لكل جلسة معرف واحد كي ترتبط إدخالات جلسة الوكيل ببعضها.

مجموعة السمات الصادرة واجهة ذات إصدارات: نطاق القياس هو `github.com/stackql/stackql/pkg/mcp_server/audit` بالإصدار `1.0.0`، ويقفل الحقل `schemaUrl` الاصطلاحات المستقرة على `https://opentelemetry.io/schemas/1.44.0`. تتبع السمات `gen_ai.*` و`mcp.*` الاصطلاحات الدلالية لـ [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) و[MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md)، وحالتها Development؛ ويتم رفع إصدار النطاق عندما تتغير المجموعة.

| السمة | الإدخال | القيمة |
|--|--|--|
|`service.name`، `service.version`|المورد|`stackql`، إصدار StackQL|
|`gen_ai.operation.name`|استدعاء الأداة|`execute_tool`|
|`mcp.method.name`|كلاهما|`tools/call` في إدخال استدعاء الأداة، و`elicitation/create` في إدخال القرار|
|`gen_ai.tool.name`|كلاهما|اسم الأداة|
|`gen_ai.tool.call.id`|كلاهما|ينشأ لكل استدعاء، ويشترك فيه الإدخالان|
|`mcp.protocol.version`|كلاهما|المراجعة التي تم التفاوض عليها|
|`mcp.session.id`|كلاهما|معرف الجلسة (غير موجود في طلبات HTTP بدون جلسات)|
|`stackql.mode`|كلاهما|وضع الخادم|
|`stackql.decision`|كلاهما|قيمة `decision` من الجدول أعلاه|
|`stackql.query`|استدعاء الأداة|نص SQL كما هو، لأدوات الاستعلام|
|`stackql.query_class`|استدعاء الأداة|قيمة `query_class`|
|`stackql.provider`|استدعاء الأداة|الموفر، لأدوات بيانات التعريف|
|`stackql.query.source`|استدعاء الأداة|معرف إدخال Query Library، عند توفيره|
|`stackql.duration_ms`|استدعاء الأداة|المدة الفعلية|
|`stackql.rows_returned`|استدعاء الأداة|عدد الصفوف، للأدوات التي تعيد الصفوف|
|`error.type`، `error.message`|استدعاء الأداة|عند الفشل: قرار الرفض، أو `tool_error` بعد اجتياز آلية التقييد|

<br />

نص الإدخال هو `execute_tool <tool>` / `elicitation <tool>`؛ ومستوى الخطورة `INFO`، أو `ERROR` عندما يفشل الاستدعاء أو يتم رفضه. عقد الحجب متطابق في التنسيقين: لا تكتب الإدخالات قيم النتائج أبدًا، بل الجملة التي أنتجتها فقط.

#### أوضاع الفشل {#failure-modes}

عندما تعيد الوجهة خطأ، يعتمد سلوك الاستجابة على `audit.failure_mode`.

| failure_mode | التأثير |
|--|--|
|`strict` (افتراضي)|يعيد استدعاء الأداة خطأ التدقيق إلى العميل حتى إذا نجحت الأداة الأساسية. وهذا مقصود: استجابة ملتبسة للعميل أفضل من عملية `DELETE` غير مكتشفة.|
|`strict_mutations`|تستمر عمليات قراءة `SELECT` وبيانات التعريف مع رسالة في stderr؛ وتظهر عمليات تعديل البيانات وعمليات دورة الحياة خطأ التدقيق.|
|`best_effort`|التسجيل دائمًا في stderr والمتابعة.|

#### ترتيب العمليات {#sequencing}

تحدث عملية كتابة إدخال التدقيق **بعد** تنفيذ الأداة (أو رفضها بالتقييد) لكن **قبل** عودة الاستجابة إلى العميل. في الوضع strict، يعني فشل عملية كتابة التدقيق في عملية DELETE ناجحة أن حذف الصف قد تم لكن العميل يتلقى خطأ - وهذا مقصود في التصميم، كي لا تمر أي عملية تعديل بيانات دون تدقيق.

#### تعطيل التدقيق {#disabling-audit}

لتعطيل التدقيق بالكامل، كما في الإصدارات السابقة لـ `v0.10.474` التي لم يكن فيها تدقيق:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### أدوات MCP المتاحة {#available-mcp-tools}

عند التشغيل كخادم MCP، يتيح StackQL الأدوات التالية وفق قائمة السماح `enabled_tools` المحددة في التكوين. تعيد كل أداة نوعين من الإخراج: عرض نصي منسق (لنموذج LLM) وحمولة منظمة محددة الأنواع (للعملاء البرمجيين). العرض النصي الافتراضي جدول markdown للنتائج المتجانسة متعددة الصفوف أو كتلة KV بتنسيق markdown للنتائج المتفرقة / ذات الإدخال الواحد. تقبل الأدوات التي لها وسيطات الوسيطة الاختيارية `format` (`markdown` أو `json`) لتجاوز الإعداد؛ وإلا فتستخدم الأداة إعداد العرض في الخادم. تسرد الإدخالات أدناه الوسيطات الخاصة بكل مهمة؛ ويشير الرمز `?` إلى الإدخالات الاختيارية.

انقر فوق اسم أي أداة لفتح صفحتها المرجعية الكاملة، وفيها الإدخالات وسلوك التقييد ومثال على موجه.

| الأداة | أداة العرض | الوصف | الإدخالات |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|هوية الخادم ووقت التشغيل: إصدار stackql ومحرك SQL الأساسي وموقع سجل الموفرين والوضع وعلامة القراءة فقط. استدعها عند الحاجة، على سبيل المثال لتشخيص عملية مرفوضة أو مقيدة أو للتحقق من سلوك خاص بإصدار معين.|لا يوجد|
|[`list_providers`](/mcp/list_providers)|جدول|الموفرون الذين تم سحبهم بالفعل إلى ذاكرة التخزين المؤقت المحلية - أعلى التسلسل الهرمي.|لا يوجد|
|[`list_services`](/mcp/list_services)|جدول|الخدمات ضمن موفر.|`provider`|
|[`list_resources`](/mcp/list_resources)|جدول|الموارد ضمن `provider`.`service`.|`provider`، `service`|
|[`list_methods`](/mcp/list_methods)|جدول|أساليب الوصول (عمليات HTTP) لمورد، مع فعل SQL الذي يقابله كل أسلوب ومعلماته المطلوبة. استدعها قبل كتابة أي استعلام - فمن خلالها يتم استنتاج معلمات `WHERE` المطلوبة.|`provider`، `service`، `resource`|
|[`describe_method`](/mcp/describe_method)|KV|عقد الإدخال والإخراج الكامل لأسلوب واحد (EXTENDED دائمًا): الإدخالات المطلوبة والاختيارية، وحقول الإخراج التي يمكن لجملة `SELECT` الإشارة إليها.|`provider`، `service`، `resource`، `method`|
|[`query_library_search`](/mcp/query_library_search)|جدول|البحث عن قوالب استعلامات منتقاة حسب الغرض المعبر عنه باللغة الطبيعية. للقراءة فقط؛ تتطلب إدخالات تعديل البيانات التفعيل الاختياري، ويتم استبعادها في الوضع `read_only`.|`intent`، `provider?`، `service?`، `tags?`، `include_mutations?`، `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|استرداد قالب وتعريفات معلماته. عندما لا تكون المعلمة `params` فارغة، التحقق من صحة القيم وإعادة SQL الناتج بالإضافة إلى `next_tool`، دون تنفيذ.|`id`، `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|تحليل جملة `SELECT` واحدة وتخطيطها دون تنفيذ. تعيد `{valid, errors}`. جمل `SELECT` فقط؛ جملة واحدة بالضبط.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|جدول|تنفيذ جملة `SELECT`. تعيد `{rows}`. للقراءة فقط.|`sql`، `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|تنفيذ `INSERT`/`UPDATE`/`REPLACE`/`DELETE` على الموفر. **آثار جانبية حقيقية.** تعيد `{messages, timestamp}`. تخضع لـ [وضع الخادم](#server-modes).|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|تنفيذ عملية دورة حياة `EXEC` في stackql. تعيد `{messages, timestamp}`. تخضع لـ [وضع الخادم](#server-modes).|`sql`|
|[`list_registry`](/mcp/list_registry)|جدول|الموفرون (وإصداراتهم) المتاحون في السجل المحدد في التكوين. تختلف عن الأداة `list_providers` التي تسرد الموفرين الذين تم سحبهم بالفعل فقط.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|تثبيت موفر واحد من السجل في ذاكرة التخزين المؤقت المحلية. تؤثر في حالة ذاكرة التخزين المؤقت المحلية فقط - دون أي تأثير في مستوى التحكم أو مستوى البيانات السحابي.|`provider`، `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|جدول|إعادة تحميل بيانات الاعتماد من ملف dotenv المحدد بـ `--env.file` إلى بيئة العملية والإبلاغ عن حالة الحل لكل موفر (`ok` و`unresolved` و`not_checked`). لا تعيد القيم السرية أبدًا. مسموح بها في جميع الأوضاع.|`provider?`|

راجع الأداة `query_library_search` قبل كتابة SQL من البداية، ثم استخدم `query_library_get` لفحص قالب مطابق وإنشاء SQL منه. تقبل أدوات الاستعلام أيضًا الوسيطة الاختيارية `source` التي تحتوي على معرف إدخال المكتبة لأغراض الإسناد فقط؛ ويخضع التنفيذ كالمعتاد لوضع الخادم وسياسة الموافقة.

:::note

تمت إزالة الأداة `describe_resource` في StackQL `v0.12.732`؛ وينبغي لعملاء MCP والموجهات وتعليمات الوكلاء التي كانت تستدعيها أن تستدعي [`list_methods`](/mcp/list_methods) ثم `describe_method` بدلًا منها. لا توجد للمورد قائمة حقول واحدة، لأن كل أسلوب وصول يعيد شكله الخاص، لذا تأتي أسماء الأعمدة من [`describe_method`](/mcp/describe_method) للأسلوب الذي يتم توجيه الاستعلام إليه. جملة [`DESCRIBE`](/language-spec/describe) في SQL لم تتغير.

:::

### موجهات MCP المتاحة {#available-mcp-prompts}

ينشر الخادم سبعة موجهات تخضع لقائمة السماح `enabled_prompts` المحددة في التكوين. كل موجه منها سير عمل إرشادي يشغله الوكيل باستخدام أدوات StackQL MCP، ولا يغير أي منها الموارد السحابية.

| الموجه | الوصف |
|--|--|
|`getting_started`|جولة إرشادية تفاعلية في StackQL وخادم StackQL MCP للمستخدمين الجدد: المفاهيم واكتشاف الموفرين والاستعلامات الأولى والخطوات التالية.|
|`cloud_audit`|تدقيق للأمان وFinOps عبر عدة سحابات (AWS وGoogle Cloud وAzure وEntra ID)؛ يعيد تقريرًا بالنتائج.|
|`cost_cleanup`|عملية تنظيف FinOps تعثر على الموارد الخاملة واليتيمة، وتقدّر الوفورات الشهرية، وتعيد قائمة مرتبة حسب الأولوية بالموارد التي يمكن حذفها بأمان.|
|`drift_report`|تقرير انحراف يقارن البنية التحتية المعلنة كتعليمات برمجية (حالة Terraform أو خطته، أو جرد يلصقه المستخدم) بالموارد السحابية الفعلية؛ يعيد جدولًا بالانحرافات.|
|`iam_access_review`|مراجعة للهوية والوصول عبر AWS IAM وEntra ID وOkta وGitHub؛ تعيد تقريرًا لمراجعة الوصول جاهزًا للمدققين (بأسلوب SOC 2 أو ISO).|
|`public_exposure_scan`|فحص للموارد التي يمكن الوصول إليها من الإنترنت (التخزين العام، ومجموعات الأمان المفتوحة، وعناوين IP العامة، ونقاط نهاية قواعد البيانات العامة، وخدمات Kubernetes المكشوفة)؛ يعيد جدولًا بحالات التعرض مرتبًا حسب الأولوية.|
|`create_deploy_stack`|يرشد المستخدم خلال إنشاء مكدس `stackql-deploy` (ملف البيان وملفات الموارد `.iql`) مع اختبار الاستعلامات مباشرة على الموارد الفعلية. يتم إنشاء عمليات تعديل البيانات لـ `stackql-deploy` ولا يتم تنفيذها أبدًا.|

* * *

### العلامات {#flags}

| العلامة | الوصف |
|--|--|
|`--mcp.server.type`|نوع خادم MCP: `http` أو `stdio` أو `reverse_proxy` (الأخير للاستخدام مع `stackql srv`).|
|`--mcp.config`|كائن تكوين JSON لخادم MCP. YAML مقبول أيضًا.|
|`--mcp.log.format`|ترميز سجل التدقيق: `jsonl` (افتراضي) أو `otel` (إدخالات سجل OpenTelemetry بتنسيق OTLP/JSON). تتجاوز قيمة `server.audit.format`. راجع [تنسيق السجل](#log-format).|
|`--mcp.protocol.version`|أحدث مراجعة للبروتوكول يعلن عنها الخادم: `auto` (افتراضي) أو `2026-07-28` أو مراجعة أقدم مثل `2025-11-25`. راجع [قفل المراجعة](#pinning-the-revision).|
|`--env.file`|ملف بيانات اعتماد بتنسيق dotenv يتم تحميله عند بدء التشغيل وتعيد الأداة `reload_credentials` تحميله عند الطلب. راجع [(إعادة) تحميل بيانات الاعتماد](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|منفذ TCP لخادم بروتوكول الاتصال الخاص بـ PostgreSQL (للاستخدام مع `stackql srv`).|
|`-h`، `--help`|طباعة معلومات المساعدة.|
|`-v`، `--verbose`|التشغيل في الوضع المفصل مع إخراج إضافي.|

&nbsp;
&nbsp;
> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية

:::info

يجب عليك تعيين متغيرات البيئة المطلوبة لمصادقة الموفر قبل بدء تشغيل خادم MCP، أو تحديد ملف بيانات اعتماد بتنسيق dotenv باستخدام [`--env.file`](#credential-resourcing---envfile--reload_credentials). لمزيد من المعلومات، راجع [استخدام موفر](/getting-started/using-a-provider).

:::

* * *

### أمثلة {#examples-1}

#### خادم MCP مستقل أساسي {#basic-standalone-mcp-server}

شغّل خادم MCP بشكل مستقل مع مصادقة الموفر ورمز مميز من نوع bearer لعملائه:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### خادم MCP مع خادم PostgreSQL (في الذاكرة) {#mcp-server-with-postgresql-server-in-memory}

شغّل خادمي MCP وPostgreSQL معًا للوصول عبر البروتوكولين:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### خادم جرد للقراءة فقط مع تدقيق إلى مسار معروف {#read-only-inventory-server-with-audit-to-a-known-path}

مفيد لوكيل يجري جرد الموارد ولا ينبغي أن يعدل البيانات أبدًا:

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
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### خادم أتمتة بوصول كامل {#full-access-automation-server}

لخطوط أنابيب الأتمتة الموثوقة التي تحتاج إلى عمليات تعديل البيانات ولا يمكنها الاستجابة لطلبات elicitation. يقدم خط الأنابيب الرمز المميز من نوع bearer المخزن في متغير البيئة `STACKQL_MCP_TOKEN`:

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
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### خادم MCP آمن مع TLS {#secure-mcp-server-with-tls}

شغّل خادم MCP مع تشفير TLS في وضع الوكيل العكسي:

أولًا، أنشئ شهادات TLS (موقعة ذاتيًا للتطوير):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

ثم شغّل الخادم:

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

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
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### اختبار خادم MCP الخاص بك {#testing-your-mcp-server}

يتضمن المستودع عميل MCP للتطوير (`stackql_mcp_client`) لكتابة البرامج النصية واختبارات الانحدار. لبنائه، شغّل:

```bash
python cicd/python/build.py --build-mcp-client
```

ينتج عن ذلك الملف `./build/stackql_mcp_client`.

لاحظ أن العميل `stackql_mcp_client` لا يرسل أي رأس `Authorization`، لذا يجب أن يعين الخادم الذي يستهدفه `"allow_unauthenticated": true` (كما تفعل الأمثلة أعلاه)؛ ويستجيب له الخادم المحمي برمز مميز برمز الحالة `401`. كما أنه **لا** يعلن عن قدرة elicitation في MCP - فهو مخصص للبرامج النصية غير التفاعلية. مع خادم `safe` سيتلقى الرفض "client does not support elicitation" عند استدعاءات تعديل البيانات ودورة الحياة؛ ومع خادم `delete_safe`، عند استدعاءات `DELETE` و`EXEC`. للاختبار التفاعلي مع طلبات elicitation، استخدم أحد عملاء MCP المضمنين في المحررات (Claude Desktop وCursor وContinue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode and registry
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

# Mutation (a DELETE) - succeeds only against a full_access server:
# stackql_mcp_client does not support elicitation, so safe and delete_safe
# servers refuse it.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### أمثلة على الاستجابات {#example-responses}

`server_info` (مع خادم في الوضع الافتراضي، StackQL `v1.0.888` على Windows عبر stdio):

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
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

`run_mutation_query` (مع خادم `full_access`):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

سطر نموذجي من سجل التدقيق لعملية تعديل البيانات نفسها:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### التكامل مع مساعدي الذكاء الاصطناعي {#integration-with-ai-assistants}

لدمج خادم MCP في StackQL مع مساعد ذكاء اصطناعي، سجّل `stackql` كخادم MCP في تكوين المساعد. يشغل معظم عملاء MCP المضمنين في المحررات الخادم عبر `stdio`؛ ففي هذه الحالة استخدم `--mcp.server.type=stdio` ويشغل المساعد العملية مباشرة. يمكن للوكلاء المستقلين الذين يتحدثون HTTP الاتصال بعملية `stackql mcp --mcp.server.type=http` طويلة التشغيل.

بالنسبة إلى Claude Desktop - بما في ذلك الإدراج في Anthropic Connector Directory (وسيلة التثبيت الموصى بها) وMCP Bundle القابل للتنزيل (`.mcpb`) والتكوين اليدوي - راجع [استخدام StackQL مع Claude Desktop](/getting-started/claude-desktop).

:::note

تختلف مواقع ملفات التكوين حسب نظام التشغيل ومساعد الذكاء الاصطناعي. راجع وثائق مساعد الذكاء الاصطناعي الذي تستخدمه لمعرفة المسار والتنسيق الصحيحين.

:::

* * *

### اعتبارات البنية المعمارية {#architecture-considerations}

عند اختيار وضع النشر، ضع في اعتبارك ما يلي:

1. **مستقل (`stackql mcp`)**: أبسط إعداد، ومثالي للتطوير وللعملاء المضمنين في المحررات عبر `stdio`.
2. **خادم مزدوج في الذاكرة (`stackql srv` مع `mcp.server.type=http`)**: تخدم عملية واحدة بروتوكولي الاتصال الخاصين بـ MCP وPostgreSQL معًا. أفضل أداء، ومناسب لمعظم عمليات النشر في بيئة الإنتاج.
3. **وكيل عكسي (`stackql srv` مع `mcp.server.type=reverse_proxy`)**:
   - يتيح فصل أحمال العمل عبر العمليات أو الأجهزة
   - يدعم تشفير TLS لنقاط نهاية MCP الآمنة
   - يسمح بتوسيع واجهتي MCP وPostgreSQL بشكل مستقل
   - يوفر مرونة للتكامل مع CA المؤسسية (ملاحظة: دعم CA المؤسسية تجريبي)

وبشكل منفصل، فيما يتعلق بالأمان:

- ابدأ بالوضع الافتراضي `mode: safe`. سيطلب عملاء MCP الداعمون لـ elicitation (Claude Desktop وCursor وContinue) موافقة المستخدم قبل أي عملية تعديل بيانات.
- اقفل الإعداد على `mode: read_only` لوكلاء الجرد أو التحليلات الذين لا ينبغي لهم إجراء أي عملية كتابة.
- اقفل الإعداد على `mode: delete_safe` عندما تريد حرية الإنشاء/التحديث لكنك تريد مشاركة بشرية في العمليات المدمرة.
- خصّص `mode: full_access` لخطوط أنابيب الأتمتة الموثوقة، وفقط مع سجل تدقيق يراجعه شخص ما.
- عيّن `server.auth_token_env_var` على أي مستمع HTTP يمكن لعمليات أو أجهزة أخرى الوصول إليه؛ و`allow_unauthenticated` مخصص للتطوير المحلي. لا يوفر TLS وحده مصادقة العملاء.

:::caution

عند استخدام TLS مع مراجع الشهادات (CA) في المؤسسات، قد يلزم تكوين إضافي. هذه الميزة تجريبية وقد تتطلب تعديلات بحسب تنفيذ CA المحدد لديك.

:::

* * *

### قراءات إضافية {#further-reading}

- مرجع مطوري الحزمة هو [ملف README في `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) في مستودع stackql.
- توجد الوثيقة التقنية الموجهة للمساهمين في [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) في مستودع stackql، وتتضمن أمثلة كاملة على الاستجابات لكل أداة.
