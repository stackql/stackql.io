---
slug: /ai-agents
title: وكلاء الذكاء الاصطناعي
hide_title: false
keywords:
  - stackql
  - وكلاء الذكاء الاصطناعي
  - mcp
  - model context protocol
  - قائم على الوكلاء
description: اربط وكلاء الذكاء الاصطناعي بالخدمات السحابية وخدمات SaaS باستخدام StackQL عبر خوادم MCP والأدوات والتكاملات المضمنة واستعلامات SQL.
image: "/img/stackql-featured-image.png"
---

يمنح StackQL وكلاء الذكاء الاصطناعي واجهة SQL نفسها التي يستخدمها الأشخاص للاستعلام عن الخدمات السحابية وخدمات SaaS وتوفيرها وتشغيلها. يكتشف الوكيل ما يعرضه الموفر، ويتحقق من صحة الاستعلام قبل تشغيله، ويقرأ الحالة الحية باستخدام `SELECT` ويغيرها باستخدام `INSERT` و`UPDATE` و`DELETE`، دون ملف حالة ودون SDK خاص بكل سحابة.

## كيفية اتصال الوكلاء {#how-agents-connect}

- **MCP عبر stdio** لعملاء المحررات وتطبيقات سطح المكتب مثل Claude Desktop وCursor وContinue. يشغل العميل الأمر `stackql mcp --mcp.server.type=stdio` بنفسه. راجع [خادم MCP](/command-line-usage/mcp).
- **MCP عبر HTTP** للوكلاء المستقلين وعمليات الأتمتة التي تتصل بعملية `stackql mcp` أو `stackql srv` طويلة التشغيل، مع المصادقة برمز مميز من نوع bearer وTLS اختياري.
- **MCP المضمن** للتطبيقات المجمعة: يعمل الخادم داخل العملية نفسها في Go وRust وSwift وKotlin و.NET وGleam. راجع [MCP المضمن](/mcp/embedded).
- **بروتوكول الاتصال الخاص بـ PostgreSQL** عبر [`stackql srv`](/command-line-usage/srv) لأطر عمل الوكلاء ودفاتر الملاحظات وأدوات BI التي تتعامل مع Postgres بالفعل.

## البدء السريع مع Claude Desktop {#quick-start-with-claude-desktop}

ثبّت الخادم من Anthropic Connector Directory (موصى به) أو من MCP Bundle القابل للتنزيل، أو سجّل الخادم يدويًا في الملف `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

أدرج بيانات الاعتماد الخاصة بالموفرين الذين تخطط لاستخدامهم فقط. تجد الإرشادات الكاملة، بما في ذلك تنزيلات bundle واستخدام ملف بيانات الاعتماد، في [استخدام StackQL مع Claude Desktop](/getting-started/claude-desktop).

## ما يمكن للوكيل فعله {#what-an-agent-can-do}

يعرض خادم MCP مجموعة صغيرة من الأدوات محددة الأنواع. تعيد كل أداة عرضًا منسقًا للنموذج وحمولة منظمة للعملاء البرمجيين.

- **الاكتشاف**: تتنقل الأدوات `list_providers` و`list_services` و`list_resources` و`list_methods` و`describe_method` في التسلسل الهرمي للموفر وتعيد الإدخالات وحقول الإخراج لكل عملية، بحيث تكون معلمات `WHERE` المطلوبة معروفة قبل كتابة الاستعلام.
- **التحقق من الصحة والقراءة**: تحلل الأداة `validate_select_query` جملة `SELECT` وتخطط لها دون تشغيلها؛ وتنفذها الأداة `run_select_query`.
- **التغيير**: تشغل الأداة `run_mutation_query` الجمل `INSERT` و`UPDATE` و`REPLACE` و`DELETE`؛ وتشغل الأداة `run_lifecycle_operation` عمليات دورة الحياة `EXEC`. تخضع الأداتان لوضع الخادم الموضح أدناه.
- **المهام الإدارية**: `server_info` و`list_registry` و`pull_provider` و`reload_credentials`.

يتوفر المرجع الكامل، مع الإدخالات وسلوك التقييد ومثال على موجه لكل أداة، في [أدوات MCP](/mcp).

## ضوابط الحماية {#guardrails}

يحدد وضع الخادم عقد الأمان للجلسة بأكملها:

| الوضع | `SELECT` / بيانات التعريف | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | سماح | رفض | رفض |
| `safe` (افتراضي) | سماح | يتطلب موافقة | يتطلب موافقة |
| `delete_safe` | سماح | سماح | يتطلب موافقة |
| `full_access` | سماح | سماح | سماح |

تستخدم حالة "يتطلب موافقة" ميزة elicitation في MCP: يعرض العميل الذي يدعمها (Claude Desktop وCursor وContinue) على المستخدم اسم الأداة وفئة الاستعلام وجملة SQL، وينتظر القرار قبل تشغيل أي شيء؛ أما العميل الذي لا يعلن عن دعمها فيُرفض طلبه. يتم تسجيل كل استدعاء أداة في سجل التدقيق، ويتطلب كل مستمع HTTP رمزًا مميزًا من نوع bearer ما لم يتم فتحه صراحة باستخدام الحقل `allow_unauthenticated`، ويتم تحميل بيانات الاعتماد من البيئة أو من ملف dotenv دون إعادة أي قيم سرية إلى العميل مطلقًا. التفاصيل في [أوضاع الخادم](/command-line-usage/mcp#server-modes) و[مصادقة عملاء HTTP](/command-line-usage/mcp#http-client-authentication) و[سجل التدقيق](/command-line-usage/mcp#audit-log).

## الخطوات التالية {#next-steps}

- [خادم MCP](/command-line-usage/mcp) - أوضاع النشر والتكوين وأوضاع الخادم وسجل التدقيق والعلامات.
- [أدوات MCP](/mcp) - المرجع الكامل للأدوات مع الإدخالات والتقييد وأمثلة على الموجهات.
- [MCP المضمن](/mcp/embedded) - شغّل الخادم داخل تطبيقك المكتوب بلغة Go أو Rust أو Swift أو Kotlin أو .NET أو Gleam.
- [Query Library](pathname:///docs/query-library/) - استعلامات SQL جاهزة للتشغيل للإجابة عن الأسئلة الشائعة في العمليات السحابية، مصنفة حسب الموفر.
- [مرجع الذكاء الاصطناعي](/ai) - مفاهيم وأدلة إرشادية ومقارنات واستكشاف الأخطاء وإصلاحها، مكتوبة للوكلاء وللأشخاص الذين يبنونها.
