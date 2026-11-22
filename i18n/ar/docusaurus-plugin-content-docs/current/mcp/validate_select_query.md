---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: أداة MCP تحلل جملة SELECT في StackQL وتخطط لها دون تنفيذها
image: "/img/stackql-featured-image.png"
---

تحلل جملة `SELECT` واحدة وتخطط لها دون تنفيذها.  تعيد `{valid, errors}`.  تفيد عندما يريد وكيل التحقق من أن استعلامه قابل للترجمة البرمجية والربط بالأسلوب المكتشف قبل الالتزام باستدعاء API فعلي.

:::info

جمل `SELECT` فقط، وجملة واحدة بالضبط: يتم رفض الحمولة التي تحتوي على أكثر من جملة واحدة مع الخطأ `validate_select_query accepts exactly one statement`.

:::

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`sql`|نعم|جملة `SELECT` واحدة.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV | `valid` (قيمة منطقية) و`errors` (مصفوفة من السلاسل النصية، فارغة للاستعلام الصالح) |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم.  لا تجري الأداة أي استدعاءات لواجهات API.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
