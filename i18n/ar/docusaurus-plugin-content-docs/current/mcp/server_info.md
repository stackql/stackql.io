---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: أداة MCP تعيد هوية خادم StackQL MCP ومعلومات وقت التشغيل الخاصة به
image: "/img/stackql-featured-image.png"
---

تعيد هوية خادم StackQL MCP ومعلومات وقت التشغيل الخاصة به: الإصدار، ومحرك SQL الأساسي، وموقع سجل الموفرين، ووضع الخادم، وعلامة القراءة فقط القديمة.  استدعها عند الحاجة، على سبيل المثال لتشخيص عملية مرفوضة أو مقيدة أو للتحقق من سلوك خاص بإصدار معين؛ فلا حاجة إليها عند بدء الجلسة أو قبل الاستعلامات.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## الإدخالات {#inputs}

لا يوجد.

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV (كتلة مفاتيح وقيم بتنسيق markdown لنموذج اللغة الكبير) | حمولة منظمة تحتوي على الحقول أدناه |

<br/><br/>

| الحقل | الوصف |
|--|--|
|`version`|إصدار بناء StackQL.|
|`commit`|التجزئة المختصرة لإيداع Git.|
|`build_date`|الطابع الزمني للبناء بتنسيق RFC3339.|
|`platform`|`os/arch` (على سبيل المثال `linux/amd64`).|
|`transport`|`http` أو `stdio`.|
|`sql_backend`|محرك SQL الأساسي (على سبيل المثال `sqlite3`).|
|`provider_registry`|عنوان URL لسجل الموفرين.|
|`mode`|وضع الخادم النشط - `read_only` أو `safe` أو `delete_safe` أو `full_access`.|
|`is_read_only`|علامة منطقية قديمة (`true` إذا وفقط إذا كان `mode == read_only`).|

* * *

## التقييد {#gating}

مسموح دائمًا في جميع أوضاع الخادم.  لا تخضع للتدقيق بوصفها عملية تعديل للبيانات.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP يدعم elicitation ومسجل فيه خادم StackQL MCP:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
