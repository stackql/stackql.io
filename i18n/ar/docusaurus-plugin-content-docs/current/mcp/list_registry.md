---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: أداة MCP تسرد الموفرين والإصدارات المتاحة في سجل الموفرين الخاص بـ StackQL
image: "/img/stackql-featured-image.png"
---

تعيد الموفرين (وإصداراتهم) المتاحين في سجل الموفرين الذي تم تكوينه لـ StackQL. وهي تختلف عن [`list_providers`](/mcp/list_providers)، التي لا تبلغ إلا عن الموفرين الذين تم سحبهم بالفعل إلى ذاكرة التخزين المؤقت المحلية.

حدّد الوسيطة الاختيارية `provider` لسرد الإصدارات المنشورة لموفر واحد. استخدمها مع [`pull_provider`](/mcp/pull_provider) عندما يحتاج الوكيل إلى تثبيت شيء غير موجود محليًا بعد.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ الأمر registry ]](/command-line-usage/registry)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|لا|اسم الموفر. عند تحديده، تقتصر النتيجة على الإصدارات المنشورة لذلك الموفر؛ وعند حذفه، تعاد جميع إدخالات السجل.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| جدول | صف واحد لكل إدخال في السجل (الموفر مع بيانات تعريف الإصدار) |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. لا يوجد أي تأثير على مستوى التحكم أو مستوى البيانات في السحابة، إذ تتم قراءة السجل فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
