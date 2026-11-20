---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - dotnet
  - csharp
description: ضمّن خادم StackQL MCP في تطبيق .NET كـ sidecar أو كـ bundle مضمن كنسخة محلية
image: "/img/stackql-featured-image.png"
---

# .NET / C#

ضمّن خادم StackQL MCP في تطبيق .NET، إما كـ sidecar يتم تنزيله، أو كنسخة محلية مضمنة في البناء الخاص بك كملف تنفيذي مستقل. العميل هو C# MCP SDK الرسمي.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

```
dotnet add package StackQL.Mcp
```

تضيف الحزمة الاختيارية `StackQL.Mcp.AgentFramework` التكامل مع Agent Framework. يتطلب .NET 8 أو أحدث.

* * *

## البدء السريع {#quick-start}

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

واجهة API هي `StackqlMcp.CreateBuilder()` مع `.WithMode()` و`.WithAuth()` و`.WithBinary()` و`.WithBundlePath()` و`.WithApproot()` و`.WithCommand()`، ثم `.StartAsync()` و`.ListToolsAsync()` و`.CallToolAsync()`.

* * *

## الأوضاع {#modes}

يستخدم المنشئ `StackqlMode.ReadOnly` افتراضيًا. استدع `.WithMode()` مع `StackqlMode.Safe` أو `StackqlMode.DeleteSafe` أو `StackqlMode.FullAccess` للتصعيد إلى وضع أعلى. التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

خياران للحصول على الملف الثنائي:

- **Sidecar (افتراضي)** - يتم تنزيل bundle الخاص بالمنصة عند التشغيل الأول، والتحقق منه باستخدام sha256، وتخزينه مؤقتًا في `~/.stackql/mcp-server-bin/<version>/<platform-key>/`، وتشغيله عبر stdio.
- **مضمن كنسخة محلية** - ضمّن bundle كمورد بناء للحصول على ملف تنفيذي مستقل:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

يمكنك أيضًا تجاوز الملف الثنائي أو bundle في وقت التشغيل باستخدام متغيري البيئة `STACKQL_MCP_BIN` أو `STACKQL_MCP_BUNDLE`.

* * *

## تطبيق توضيحي {#demo-app}

**driftwatch** هو Worker Service في .NET يضمّن خادم StackQL للقراءة فقط، ويشغل وفق جدول زمني مجموعة من فحوصات الانحراف بلغة SQL (التعرض العام، والوسوم أو التراخيص المفقودة، والانحراف عن خط الأساس)، ثم يرسل النتائج إلى Adaptive Card في Teams.

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
