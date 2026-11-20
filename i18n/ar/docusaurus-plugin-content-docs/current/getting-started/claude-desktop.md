---
title: استخدام StackQL مع Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - الذكاء الاصطناعي
  - infrastructure-as-code
  - السحابة المتعددة
description: استخدم StackQL مع Claude Desktop للاستعلام عن الموارد السحابية وتوفيرها باستخدام اللغة الطبيعية
image: "/img/stackql-featured-image.png"
---

يتكامل StackQL مع Claude Desktop عبر Model Context Protocol (MCP)، مما يتيح لك الاستعلام عن الموارد السحابية وتوفيرها لدى عدة مزودي خدمات سحابية باستخدام اللغة الطبيعية. يتيح لك هذا المزيج القوي التفاعل مع البنية التحتية السحابية الخاصة بك من خلال المحادثة، مع الاستفادة من واجهة StackQL القائمة على SQL في الخلفية.

تتوفر ثلاثة خيارات للتثبيت، مرتبة هنا حسب الترتيب الموصى به: التثبيت من Anthropic Connector Directory، أو التثبيت من ملف bundle تم تنزيله، أو تسجيل ملف StackQL الثنائي المثبت محليًا يدويًا.

## التثبيت من Connector Directory (موصى به) {#install-from-the-connector-directory-recommended}

أسهل خيار لاستخدام خادم StackQL MCP مع Claude Desktop هو تثبيته من Anthropic Connector Directory. في Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**، وابحث عن **StackQL**، ثم انقر على **Install**. لا يوجد ما تحتاج إلى تنزيله، ولا يلزم تثبيت StackQL بشكل منفصل.

![StackQL في Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

يمكنك أيضًا عرض الإدراج مباشرة على [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

يؤدي ذلك إلى تثبيت خادم StackQL MCP داخل العملية لمنصتك: يحتوي الإدراج على ملف bundle مجمع متعدد المنصات يضم ملفات ثنائية موقعة رقميًا لـ macOS universal وWindows x64 وLinux x64 وarm64، ويختار Claude Desktop الملف المناسب لجهازك تلقائيًا. بالنسبة إلى Windows ARM64 الأصلي، ثبّت ملف bundle المنفصل `windows-arm64` من [التنزيلات أدناه](#install-from-a-downloaded-bundle). الملفات الثنائية لـ Windows موقعة رقميًا باستخدام Authenticode، والملف الثنائي لـ macOS موثق من Apple - وهي الملفات الثنائية نفسها الموجودة في إصدار StackQL المستقل.

![تثبيت موصل StackQL](/img/stackql-anthropic-directory-install.png)

يرث الموصل المثبت من Connector Directory بيئة Claude Desktop، لذا عيّن متغيرات البيئة الخاصة بموفريك على مستوى نظام التشغيل قبل تشغيل Claude Desktop - راجع [بيانات الاعتماد الخاصة بكل موفر](#provider-specific-credentials). يلتقط Claude Desktop بيئته عند بدء التشغيل: بعد تغيير المتغيرات، يجب عليك إنهاء Claude Desktop بالكامل (بما في ذلك أيقونة علبة النظام على Windows) ثم إعادة تشغيله. إن طلب تشغيل [`reload_credentials`](/mcp/reload_credentials) من الوكيل يعرض حالة بيانات الاعتماد لكل موفر، وهو أسرع وسيلة للتأكد مما يمكن للخادم رؤيته وما لا يمكنه رؤيته.

يعلن Claude Desktop عن قدرة elicitation في MCP، لذا سيطلب منك وضع الخادم الافتراضي (`safe`) الموافقة على كل عملية تعديل للبيانات - راجع [أوضاع الخادم](/command-line-usage/mcp#server-modes).

## التثبيت من ملف bundle تم تنزيله {#install-from-a-downloaded-bundle}

استخدم خيار التثبيت هذا لقفل إصدار محدد، أو للتثبيت دون الوصول إلى Connector Directory، أو للتحقق من المجاميع الاختبارية بنفسك قبل التثبيت.

ملف MCP Bundle (`.mcpb`) المبني مسبقًا هو حزمة قابلة للتثبيت بنقرة واحدة، تحتوي على الملف الثنائي `stackql` الموقع رقميًا وتكوين الخادم. لا يلزم تثبيت StackQL بشكل منفصل.

نزّل ملف bundle الخاص بمنصتك من أحدث إصدار:

| المنصة | ملف bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (أصلي) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

ثم في Claude Desktop: **Settings -> Extensions -> Install Extension**، واختر الملف الذي تم تنزيله. الملف الثنائي المضمن موقع رقميًا باستخدام Authenticode (Windows) وموثق من Apple (macOS)؛ ولكل ملف bundle مجموع اختباري `.sha256` بجانبه في [صفحة الإصدار](https://github.com/stackql/stackql/releases/latest). للتحقق قبل التثبيت:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

الخادم مدرج أيضًا في [سجل MCP الرسمي](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) باسم `io.github.stackql/stackql-mcp`، وهو يقفل هذه المجاميع الاختبارية.

## التكوين اليدوي {#manual-configuration}

إذا سبق لك تثبيت StackQL، فيمكنك تسجيل خادم StackQL MCP مباشرة في ملف تكوين Claude Desktop.

### المتطلبات الأساسية {#prerequisites}

قبل إعداد StackQL مع Claude Desktop، تأكد من توفر ما يلي لديك:

1. **StackQL مثبت** - نزّل StackQL وثبّته من [stackql.io](/installing-stackql) أو استخدم مدير حزم
2. **Claude Desktop** - نزّله من [موقع Anthropic على الويب](https://claude.ai/desktop)
3. **بيانات اعتماد مزودي الخدمات السحابية** - مفاتيح API أو بيانات الاعتماد الخاصة بمزودي الخدمات السحابية الذين تريد العمل معهم

تحقق من وجود StackQL في متغير PATH الخاص بنظامك من خلال تشغيل ما يلي:

```bash
stackql --version
```

### إعداد خادم MCP {#setting-up-the-mcp-server}

لكي يتمكن Claude Desktop من الاتصال بـ StackQL، تحتاج إلى تكوين خادم MCP في ملف تكوين Claude Desktop الخاص بك.

#### Windows {#windows}

حرّر ملف التكوين الموجود في:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

حرّر ملف التكوين الموجود في:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

حرّر ملف التكوين الموجود في:
```
~/.config/Claude/claude_desktop_config.json
```

### مثال على التكوين {#configuration-example}

أضف التكوين التالي إلى الملف `claude_desktop_config.json` الخاص بك:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
تحتاج فقط إلى تضمين متغيرات البيئة الخاصة بمزودي الخدمات السحابية الذين تخطط لاستخدامهم. أزل من التكوين أي بيانات اعتماد لموفرين لا تستخدمهم.
:::

:::warning
لا تودع أبدًا الملف `claude_desktop_config.json` الذي يحتوي على بيانات اعتماد فعلية في نظام التحكم في الإصدارات. استخدم ممارسات آمنة لإدارة بيانات الاعتماد.
:::

يعلن Claude Desktop عن قدرة elicitation في MCP، لذا سيطلب منك وضع الخادم الافتراضي (`safe`) الموافقة على كل عملية تعديل للبيانات. بدّل إلى `mode: read_only` للحصول على وصول يقتصر على الجرد، أو إلى `mode: full_access` لتخطي طلبات الموافقة - راجع [أوضاع الخادم](/command-line-usage/mcp#server-modes).

### استخدام ملف بيانات اعتماد (`--env.file`) {#using-a-credentials-file---envfile}

البديل عن الكتلة `env` - وهو البديل الموصى به على Windows - هو الاحتفاظ ببيانات الاعتماد في ملف بنمط dotenv وتمريره باستخدام العلامة [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

مع ملف بيانات اعتماد بسيط:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

لماذا هذا الخيار أفضل من الكتلة `env`:

- تتطلب التغييرات على الكتلة `env` إعادة تشغيل Claude Desktop بالكامل (بما في ذلك إنهاؤه من أيقونة علبة النظام) قبل أن تراها عملية MCP الفرعية، وعلى Windows يكتب الأمر `setx` في سجل Windows - ولا يصل أبدًا إلى عملية Desktop قيد التشغيل.
- يمكن إنشاء ملف `--env.file` أو تدويره في أي وقت - بل لا يلزم وجوده عند بدء تشغيل الخادم - ويؤدي طلب تشغيل [`reload_credentials`](/mcp/reload_credentials) من الوكيل إلى التقاط التغييرات دون أي إعادة تشغيل.

:::note

العلامة `--env.file` والأداة `reload_credentials` متاحتان في إصدارات StackQL بعد `v0.10.542`.

:::

### بيانات الاعتماد الخاصة بكل موفر {#provider-specific-credentials}

يتطلب مزودو الخدمات السحابية المختلفون أساليب مصادقة مختلفة، راجع وثائق الموفرين ([https://stackql.io/providers](/providers)) لمعرفة متغيرات البيئة المطلوبة للموفر أو الموفرين الذين تريدهم.


### إعادة تشغيل Claude Desktop {#restarting-claude-desktop}

بعد تحديث ملف التكوين، أعد تشغيل Claude Desktop ليقرأ تكوين خادم MCP الجديد. سيبدأ تشغيل خادم StackQL MCP تلقائيًا عندما تبدأ محادثة جديدة.

## ما يمكنك فعله {#what-you-can-do}

بعد التكوين، يمكنك استخدام اللغة الطبيعية في Claude Desktop من أجل:

### الاستعلام عن الموارد السحابية {#query-cloud-resources}

اطلب من Claude استرداد معلومات حول البنية التحتية السحابية الخاصة بك:

- "اعرض لي جميع مثيلات الحوسبة الخاصة بي في GCP في المنطقة us-central1"
- "اسرد جميع حاويات S3 في حساب AWS الخاص بي"
- "ما الأجهزة الظاهرية في Azure التي تعمل في اشتراكي؟"
- "اعرض لي جميع وحدات pod في Kubernetes في مساحة الاسم الافتراضية"

### توفير الموارد {#provision-resources}

أنشئ الموارد السحابية وانشرها باستخدام أوامر حوارية:

- "أنشئ حاوية S3 باسم 'my-data-bucket' في us-west-2"
- "انشر حاوية Cloud Storage في GCP مع تمكين تعيين الإصدارات"
- "شغّل مثيل EC2 صغير الحجم في us-east-1"
- "أنشئ مجموعة موارد في Azure في westus2"

### التحليل والتدقيق {#analyze-and-audit}

نفّذ عمليات تدقيق أمني وفحوصات امتثال:

- "اعرض لي جميع حاويات S3 التي يمكن الوصول إليها بشكل عام"
- "اسرد مستخدمي IAM الذين لم يسجلوا الدخول منذ 90 يومًا"
- "ابحث عن جميع مثيلات GCP التي لا تحمل تصنيفات مناسبة"
- "حدّد الأجهزة الظاهرية في Azure التي لم يتم تمكين النسخ الاحتياطي لها"

### العمليات عبر السحابات {#cross-cloud-operations}

استعلم عن الموارد وقارن بينها عبر عدة مزودي خدمات سحابية:

- "اعرض لي جميع مثيلات الحوسبة عبر AWS وGCP وAzure"
- "قارن تكاليف التخزين بين AWS S3 وGCP Cloud Storage"
- "اسرد جميع قواعد البيانات لدى جميع مزودي الخدمات السحابية"

### البنية التحتية كتعليمات برمجية {#infrastructure-as-code}

أنشئ استعلامات نشر البنية التحتية ونفّذها:

- "أنشئ مكدس موارد يتضمن VPC وشبكة فرعية ومثيل EC2"
- "انشر نظام مجموعة GKE مكتمل الإعداد مع تجمعات العقد"
- "أعدّ عملية نشر متعددة المناطق في AWS"

### تعديل الموارد {#modify-resources}

حدّث الموارد السحابية الموجودة:

- "أضف الوسوم إلى جميع مثيلات EC2 غير الموسومة"
- "مكّن التشفير لحاوية S3 'my-bucket'"
- "حدّث نوع الجهاز لمثيل GCP 'web-server-1'"
- "غيّر قائمة ACL على حاوية Cloud Storage الخاصة بي لتصبح خاصة"

### حذف الموارد {#delete-resources}

أزل الموارد السحابية عندما لا تعود هناك حاجة إليها:

- "احذف حاوية S3 'temp-bucket'"
- "أزل جميع مثيلات EC2 المتوقفة"
- "نظّف أقراص GCP غير المستخدمة"

## مثال على محادثة {#example-conversation}

إليك مثالًا على كيفية تفاعلك مع Claude Desktop باستخدام StackQL:

**أنت:** "هل يمكنك أن تعرض لي جميع مثيلات الحوسبة الخاصة بي في GCP؟"

**Claude:** *ينفذ استعلام StackQL ويعيد النتائج في جدول منسق يعرض أسماء المثيلات ومناطق التوفر وأنواع الأجهزة والحالة*

**أنت:** "أنشئ حاوية Cloud Storage جديدة باسم 'analytics-data-2025' في المنطقة us-central1 مع وصول موحد على مستوى الحاوية"

**Claude:** *ينفذ جملة INSERT في StackQL لإنشاء الحاوية ويؤكد إنشاءها*

**أنت:** "الآن اسرد جميع حاويات Cloud Storage الخاصة بي"

**Claude:** *ينفذ استعلام SELECT ويعرض جميع الحاويات بما في ذلك الحاوية التي تم إنشاؤها حديثًا*

## تلميحات وأفضل الممارسات {#tips-and-best-practices}

1. **حدّد طلبك بدقة**: كلما زادت دقة طلبك، كانت النتائج التي تحصل عليها أفضل. ضمّن أسماء الموارد والمناطق والسمات المحددة متى أمكن ذلك.

2. **استخدم اللغة الطبيعية**: لا تحتاج إلى معرفة بناء جملة SQL - يترجم Claude طلباتك المكتوبة باللغة الطبيعية إلى استعلامات StackQL.

3. **راجع قبل التنفيذ**: في العمليات الهدامة (DELETE، UPDATE)، يعرض لك Claude عادة ما سيتم تنفيذه قبل المتابعة.

4. **العمليات متعددة الخطوات**: يمكنك ربط العمليات ببعضها في محادثة واحدة. يحتفظ Claude بالسياق طوال الجلسة.

5. **معالجة الأخطاء**: إذا فشل استعلام، يمكن لـ Claude المساعدة في استكشاف الأخطاء وإصلاحها واقتراح تصحيحات استنادًا إلى رسالة الخطأ.

6. **استكشف القدرات**: اسأل Claude "ماذا يمكنني أن أفعل باستخدام StackQL؟" للحصول على اقتراحات خاصة بالموفرين الذين قمت بتكوينهم.

## استكشاف الأخطاء وإصلاحها {#troubleshooting}

### عدم بدء تشغيل خادم MCP {#mcp-server-not-starting}

إذا تعذر على Claude Desktop الاتصال بخادم StackQL MCP:

1. تحقق من وجود StackQL في PATH الخاص بك
2. تحقق من أن الملف `claude_desktop_config.json` الخاص بك ملف JSON صالح
3. تأكد من أن مسار الملف في التكوين صحيح لنظام التشغيل الخاص بك
4. راجع سجلات Claude Desktop بحثًا عن رسائل الخطأ

### أخطاء المصادقة {#authentication-errors}

إذا تلقيت أخطاء مصادقة:

1. تحقق من صحة بيانات اعتماد مزود الخدمات السحابية الخاصة بك
2. تأكد من وجود ملفات بيانات الاعتماد في المسارات المحددة
3. تحقق من أن متغيرات البيئة منسقة بشكل صحيح في التكوين - إن طلب تشغيل [`reload_credentials`](/mcp/reload_credentials) من الوكيل يعرض حالة بيانات الاعتماد لكل موفر، وهو أسرع وسيلة لمعرفة المتغير المفقود
4. تأكد من أن بيانات الاعتماد الخاصة بك تملك الأذونات اللازمة

### أخطاء الأذونات {#permission-errors}

إذا فشلت العمليات بسبب عدم كفاية الأذونات:

1. تحقق من أن حساب الخدمة أو مستخدم IAM الخاص بك يملك الأذونات المطلوبة
2. تحقق من سياسات IAM الخاصة بكل مزود خدمات سحابية
3. تأكد من تمكين خدمات API في مشاريعك السحابية

## معرفة المزيد {#learn-more}

- [مرجع أمر MCP في StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [مواصفات اللغة](/language-spec/select)
- [وثائق Model Context Protocol](https://modelcontextprotocol.io)

## اعتبارات الأمان {#security-considerations}

:::caution
- خزّن بيانات الاعتماد بشكل آمن ولا تودعها أبدًا في نظام التحكم في الإصدارات
- اتبع مبادئ الوصول بالحد الأدنى من الامتيازات لحسابات الخدمة
- دوّر بيانات الاعتماد ومفاتيح API بانتظام
- راقب سجلات خادم MCP بحثًا عن أي نشاط غير معتاد
- فكّر في استخدام أدوات إدارة بيانات الاعتماد التي يوفرها مزودو الخدمات السحابية (على سبيل المثال، Secret Manager وParameter Store)
:::
