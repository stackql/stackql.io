---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: أداة MCP تنفذ جملة SELECT في StackQL على موفر
image: "/img/stackql-featured-image.png"
---

تنفذ جملة `SELECT` على الموفرين الذين تم تكوينهم وتعيد صفوف النتائج.  للقراءة فقط - لا تقبل الأداة عمليات تعديل البيانات ولا عمليات دورة الحياة؛ استخدم [`run_mutation_query`](/mcp/run_mutation_query) أو [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) لها.

تعاد صفوف النتائج إلى العميل، لكن يتم استبعادها عمدًا من سجل التدقيق.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT في مواصفات اللغة ]](/language-spec/select)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`sql`|نعم|جملة `SELECT`.  تصنف آلية التقييد القائمة على السياسات جمل بيانات التعريف (`SHOW` و`DESCRIBE` و`DESC` و`EXPLAIN`) على أنها للقراءة فقط، وتقبلها الأداة أيضًا؛ ويتم التعرف على `DESC` في StackQL من الإصدار `v0.12.732` فصاعدًا.|
|`row_limit`|لا|الحد الأقصى لعدد الصفوف التي تعاد إلى العميل.  تعني القيمة `0` (الافتراضية) عدم وجود حد من جانب العميل.|
|`source`|لا|معرف إدخال Query Library عندما يأتي SQL من [`query_library_get`](/mcp/query_library_get). للإشارة إلى المصدر فقط؛ لا يغير التنفيذ ولا الموافقات.|
|`format`|لا|تنسيق عرض النص: `markdown` أو `json`. عند حذفها، تستخدم الأداة إعداد العرض الخاص بالخادم.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول | `{rows: [...]}` - كائن واحد لكل صف، مفاتيحه أسماء الأعمدة |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم (`read_only` و`safe` و`delete_safe` و`full_access`) عندما تكون كل جملة في الحمولة للقراءة فقط.  من الإصدار `v1.0` فصاعدًا، ترفض هذه الأداة جمل `INSERT` و`UPDATE` و`REPLACE` و`DELETE` و`EXEC` في جميع الأوضاع، بما في ذلك `full_access`، مع خطأ يذكر [`run_mutation_query`](/mcp/run_mutation_query) أو [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  يتم تصنيف الحمولة التي تحتوي على عدة جمل وفق الجملة ذات الامتيازات الأعلى فيها، لذا يتم رفض عملية تعديل البيانات التي تلي جملة `SELECT` في البداية بالمثل.  تعلن الأداة عن `destructiveHint: false` و`openWorldHint: true`.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
