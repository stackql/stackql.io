---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "يشغل الأمر stackql srv نظام StackQL كخادم لبروتوكول الاتصال الخاص بـ PostgreSQL، ليستعلم psql وأدوات BI وبرامج تشغيل اللغات عن واجهات API السحابية وSaaS باستخدام SQL."
image: "/img/stackql-featured-image.png"
---

الأمر المستخدم لتشغيل StackQL كخدمة يمكن بعد ذلك الوصول إليها من خلال عملاء [بروتوكول الاتصال الخاص بـ Postgres](https://www.postgresql.org/docs/current/protocol.html) الذين تتم مصادقتهم باستخدام mTLS. يمكن للأمر `srv` أيضًا تشغيل خادم [Model Context Protocol (MCP)](/command-line-usage/mcp) إلى جانب خادم بروتوكول الاتصال الخاص بـ PostgreSQL، مما يتيح الوصول عبر بروتوكولين لكل من عملاء قواعد البيانات التقليدية ووكلاء الذكاء الاصطناعي.

* * * 

### بناء الجملة {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

على الرغم من أن خادم StackQL يستخدم بروتوكول الاتصال الخاص بـ Postgres، فإنه ليس خادم Postgres ولا يتطلب وجود خادم منه. يستخدم العميل بروتوكول الاتصال للتواصل مع الخادم، الذي يستخدم StackQL لتنفيذ الاستعلامات أو عمليات DML على مزودي الخدمات السحابية ومزودي خدمات SaaS البعيدين وإعادة النتائج إلى العميل.

:::

يتم ربط المعلمات المرسلة عبر بروتوكول الاستعلام الموسع (`Parse` / `Bind` / `Execute`، كما يستخدمه `psycopg` 3 و`pgx` وJDBC) على جانب الخادم كقيم حرفية نصية. بدءًا من `v0.12.742`، يبقى العنصر النائب `$n` الموجود داخل قيمة حرفية نصية أو معرف بين علامتي اقتباس أو تعليق دون تغيير، ويتم إلغاء الشرطات المائلة العكسية في قيم المعلمات، وتبقى القيمة النصية `NULL` هي السلسلة النصية `NULL` (يتم نقل قيمة SQL `NULL` بشكل منفصل).

### خيارات الخادم {#server-options}

| الخيار | الوصف | القيمة الافتراضية |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|العنوان الذي يستمع عليه الخادم.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|منفذ TCP الذي يستمع عليه الخادم.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|كائن تكوين TLS بتنسيق JSON. راجع [مثال mTLS](#examples) أدناه.|لا يوجد|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|تمكين إخراج تصحيح أخطاء الاستعلامات لعملاء PostgreSQL، على سبيل المثال باستخدام `stackql srv --pgsrv.debug.enable=true`. اترك هذا الخيار دون تمكين ما لم يكن من المفترض أن يتلقى العملاء معلومات تصحيح الأخطاء.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|تعيين متقدم بتنسيق JSON أو YAML يتم تمريره إلى خادم بروتوكول الاتصال الخاص بـ PostgreSQL فقط عندما لا يتم تحديد الخيار `--pgsrv.tls`.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|مقبول لكنه لا يعين مستوى أداة التسجيل في المحرك الحالي. استخدم `--loglevel` بدلًا من ذلك.|`WARN`|

<br/>

> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية

### خيارات خادم MCP {#mcp-server-options}

عند تشغيل الأمر `srv` مع دعم MCP، تتوفر الخيارات الإضافية التالية:

| الخيار | الوصف |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|نوع خادم MCP: `http` (في الذاكرة) أو `reverse_proxy` (يعتمد على TCP)|
|<span class="nowrap">`--mcp.config`</span>|كائن تكوين بتنسيق JSON لخادم MCP (راجع [وثائق MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|ترميز سجل تدقيق MCP: `jsonl` (افتراضي) أو `otel` (إدخالات سجل OTLP/JSON)؛ يتجاوز `server.audit.format` في كائن التكوين|
|<span class="nowrap">`--mcp.protocol.version`</span>|أحدث مراجعة لبروتوكول MCP يتم الإعلان عنها: `auto` (افتراضي)، أو `2026-07-28` (بدون جلسات فقط)، أو مراجعة أقدم مثل `2025-11-25`؛ يتجاوز `server.protocol_version` في كائن التكوين|

<br/>

:::info[تغيير جذري في v0.12.742]

بدءًا من `v0.12.742`، ترفض وسيلة النقل HTTP في MCP (`--mcp.server.type=http` أو `reverse_proxy`) بدء التشغيل إلا إذا حدد كائن التكوين الرمز المميز من نوع bearer باستخدام `server.auth_token_env_var` أو اختار بشكل صريح الاستغناء عن المصادقة باستخدام `"allow_unauthenticated": true`. راجع [مصادقة عملاء HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

راجع [وثائق أمر MCP](/command-line-usage/mcp) للحصول على معلومات مفصلة حول تكوين خادم MCP واستخدامه، بما في ذلك أوضاع النشر وخيارات التكوين والتكامل مع مساعدي الذكاء الاصطناعي.

:::

:::info

يجب عليك تعيين متغيرات البيئة المطلوبة لمصادقة الموفرين قبل بدء تشغيل الخادم، وراجع [استخدام موفر](/getting-started/using-a-provider) لمزيد من المعلومات.

:::

بالنسبة إلى الخوادم التي تعمل لفترات طويلة، تتيح لك العلامة [`--env.file`](/command-line-usage/global-flags) وضع بيانات الاعتماد أو تدويرها في ملف بنمط dotenv على القرص قبل بدء التشغيل بدلًا من تصدير المتغيرات إلى واجهة الأوامر:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

بالنسبة إلى الأمر `stackql srv`، تتم قراءة الملف مرة واحدة عند بدء التشغيل؛ ووسيلة إعادة التحميل أثناء الجلسة هي أداة MCP [`reload_credentials`](/mcp/reload_credentials).

### العلامات {#flags}

| العلامة | الوصف |
|--|--|
|`-h,--help`|طباعة معلومات المساعدة|
|`-v,--verbose`|تشغيل الاستعلامات في الوضع المفصل مع إرسال إخراج إضافي إلى stdout، وإذا تم تعيين العلامة العامة `-f` (`--outfile`)<br/>فستتم كتابة معلومات التسجيل الإضافية هذه في ملف الإخراج مع نتائج الاستعلام|

<br/>

> راجع [العلامات العامة](/command-line-usage/global-flags) للاطلاع على خيارات إضافية

* * *

### أمثلة {#examples}

تشغيل عملية خادم StackQL والاتصال باستخدام عميل سطر الأوامر `psql`.

أولًا، نزّل ملف التكوين `openssl.cnf` من [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).

بعد ذلك، أنشئ ملفات الشهادة والمفتاح التي سيستخدمها الخادم والعميل لمصادقة TLS المتبادل (mTLS).

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

الآن عيّن متغيرات البيئة، وإذا كان العميل والخادم على الجهاز نفسه، فيمكن مشاركتها.

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

عيّن بيانات الاعتماد للموفرين الذين يحتاجهم الخادم. بالنسبة إلى GitHub، يقرأ الموفر اسم المستخدم والرمز المميز للوصول الشخصي من هذين المتغيرين:

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

الآن ابدأ تشغيل الخادم في إحدى الوحدات الطرفية.

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

في وحدة طرفية أخرى، اتصل بالخادم باستخدام عميل سطر الأوامر `psql`.

```bash
psql -h 127.0.0.1 -d stackql
```

الآن شغّل استعلام StackQL على موفر تمت مصادقته.

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
