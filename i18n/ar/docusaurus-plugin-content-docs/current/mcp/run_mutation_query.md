---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: أداة MCP تنفذ INSERT أو UPDATE أو REPLACE أو DELETE على موفر عبر StackQL
image: "/img/stackql-featured-image.png"
---

تنفذ جمل `INSERT` أو `UPDATE` أو `REPLACE` أو `DELETE` على الموفر.  **آثار جانبية فعلية** - يغير الاستدعاء الناجح الحالة السحابية.

يخضع [لوضع الخادم](/command-line-usage/mcp#server-modes):

| الوضع | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|مرفوض|مرفوض|
|`safe` (افتراضي)|يتطلب موافقة (elicitation)|يتطلب موافقة|
|`delete_safe`|مسموح|يتطلب موافقة|
|`full_access`|مسموح|مسموح|

<br/>

:::info

تخضع الحمولة التي تحتوي على عدة جمل للجملة ذات الامتيازات الأعلى فيها

:::

عندما تتطلب عملية تعديل البيانات موافقة ولم يعلن العميل عن قدرة elicitation في MCP، يتم رفض الاستدعاء.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT في مواصفات اللغة ]](/language-spec/insert) [[ DELETE في مواصفات اللغة ]](/language-spec/delete)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`sql`|نعم|جملة `INSERT` أو `UPDATE` أو `REPLACE` أو `DELETE`.|
|`source`|لا|معرف إدخال Query Library عندما يأتي SQL من [`query_library_get`](/mcp/query_library_get). للإشارة إلى المصدر فقط؛ لا يغير التنفيذ ولا الموافقات.|
|`format`|لا|تنسيق عرض النص: `markdown` أو `json`. عند حذفها، تستخدم الأداة إعداد العرض الخاص بالخادم.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV | `{messages: [...], timestamp}` - تأكيد الإرسال، وليس نص استجابة واجهة API |

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP يدعم elicitation ومسجل فيه خادم StackQL MCP:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

سيعرض عميل MCP طلب موافقة قبل إرسال عملية الحذف، ما لم يكن الخادم يعمل في الوضع `full_access`.

يوضح المقطع التالي تدفق elicitation عندما يعمل خادم StackQL MCP في الوضع الافتراضي `safe` - إذ يُطلب من العميل الموافقة على جملة `DELETE` قبل إرسال أي تغيير على جانب السحابة.  يمكنك إعادة إنتاج ذلك على خادم StackQL MCP الخاص بك باستخدام [MCP Inspector](https://github.com/modelcontextprotocol/inspector) الرسمي عبر `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
