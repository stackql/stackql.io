---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: أداة MCP تثبّت موفر StackQL من السجل في ذاكرة التخزين المؤقت المحلية
image: "/img/stackql-featured-image.png"
---

تثبّت موفرًا واحدًا من سجل StackQL في ذاكرة التخزين المؤقت المحلية في approot. عند حذف `version`، يتم سحب أحدث إصدار منشور.

لا تكتب هذه الأداة إلا حالة ذاكرة التخزين المؤقت المحلية، ولا يوجد أي تأثير على مستوى التحكم أو مستوى البيانات في السحابة، لذا فهي مسموحة في جميع أوضاع الخادم (بما في ذلك `read_only`). استخدم [`list_registry`](/mcp/list_registry) لاكتشاف اسم الموفر والإصدارات المتاحة قبل السحب.

راجع أيضًا:
[[ نظرة عامة على MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ الأمر registry ]](/command-line-usage/registry)

* * *

## الإدخالات {#inputs}

| الوسيطة | مطلوب | الوصف |
|--|--|--|
|`provider`|نعم|اسم الموفر كما هو منشور في السجل (على سبيل المثال، `aws` و`google` و`cloudflare`).|
|`version`|لا|الإصدار المنشور المراد سحبه. عند حذفه، يتم تثبيت أحدث إصدار منشور.|

* * *

## الإخراج {#output}

| أداة العرض | الشكل |
|--|--|
| KV | تأكيد بأن الموفر (والإصدار) قد تم تثبيته في ذاكرة التخزين المؤقت المحلية |

* * *

## التقييد {#gating}

مسموح في جميع أوضاع الخادم. لا توجد تأثيرات على جانب السحابة؛ تتغير حالة ذاكرة التخزين المؤقت المحلية فقط.

* * *

## مثال {#example}

جرّب هذا الموجه مع أي عميل MCP مسجل فيه خادم StackQL MCP:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

إليك مثالًا في Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
