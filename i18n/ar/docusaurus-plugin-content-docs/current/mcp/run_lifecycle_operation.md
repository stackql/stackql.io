---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: أداة MCP تنفذ عملية دورة حياة باستخدام جملة EXEC في StackQL
image: "/img/stackql-featured-image.png"
---

تنفذ عملية دورة حياة باستخدام جملة `EXEC` في StackQL - أي إجراءات الموفر التي لا تندرج ضمن عمليات CRUD (بدء جهاز ظاهري أو إيقافه، وتدوير مفتاح، وبدء سير عمل، وما إلى ذلك).  **آثار جانبية فعلية.**

يخضع [لوضع الخادم](/command-line-usage/mcp#server-modes):

| الوضع | جملة `EXEC` لدورة الحياة |
|--|--|
|`read_only`|مرفوض|
|`safe` (افتراضي)|يتطلب موافقة (elicitation)|
|`delete_safe`|يتطلب موافقة|
|`full_access`|مسموح|

<br/>

:::info

تخضع الحمولة التي تحتوي على عدة جمل للجملة ذات الامتيازات الأعلى فيها

:::

عندما تكون الموافقة مطلوبة ولم يعلن العميل عن قدرة elicitation، يتم رفض الاستدعاء.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC في مواصفات اللغة ]](/language-spec/exec)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`sql`|نعم|جملة `EXEC` في StackQL تستهدف أسلوب دورة حياة.|
|`source`|لا|معرف إدخال Query Library عندما يأتي SQL من [`query_library_get`](/mcp/query_library_get). للإشارة إلى المصدر فقط؛ لا يغير التنفيذ ولا الموافقات.|
|`format`|لا|تنسيق عرض النص: `markdown` أو `json`. عند حذفها، تستخدم الأداة إعداد العرض الخاص بالخادم.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP يدعم elicitation ومسجل فيه خادم StackQL MCP:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

سيعرض عميل MCP طلب موافقة قبل إرسال العملية، ما لم يكن الخادم يعمل في الوضع `full_access`.

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
