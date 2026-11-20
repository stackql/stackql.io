---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - swift
  - macos
description: ضمّن الملف الثنائي الموثق لخادم StackQL MCP داخل تطبيق macOS موقع رقميًا
image: "/img/stackql-featured-image.png"
---

# Swift

ضمّن خادم StackQL MCP في تطبيق macOS. يمكن تضمين الملف الثنائي الموقع رقميًا والموثق داخل تطبيق `.app` الخاص بك، والعميل هو Swift MCP SDK الرسمي.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

أضف الحزمة إلى الملف `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

يتطلب macOS 13+ وSwift 6.1 (Xcode 16.3+). التبعية الوحيدة هي Swift MCP SDK الرسمي.

* * *

## البدء السريع {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

واجهة API هي `StackQLServer.start(Options)` و`.listToolNames()` و`.call(_:_:)` و`.stop()`. يحمل النوع `Options` الحقول `mode` و`auth` و`binaryOverride` و`allowDownload`. ولأطر التشغيل الخارجية، تعيد الدالة `StackQLServer.resolveCommand(_:)` أمر التشغيل بعد حله.

* * *

## الأوضاع {#modes}

القيمة الافتراضية لـ `Options.mode` هي `.readOnly`. غيّرها إلى `.safe` أو `.deleteSafe` أو `.fullAccess` للتصعيد إلى وضع أعلى. التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

الملف الثنائي `darwin-universal` موقع رقميًا بشهادة Developer ID وموثق من Apple، لذا يمكن تضمينه داخل تطبيق macOS `.app` موقع رقميًا مع الحفاظ على سريان توثيق التطبيق نفسه. وهذا ما تنفرد به مكتبة Swift - فأنت توزع تطبيقك كتطبيق واحد مستقل وموثق لا يحتاج إلى تنزيل عند التشغيل الأول.

ترتيب حل الملف الثنائي:

1. القيمة الصريحة لـ `binaryOverride`.
2. الموارد المضمنة في `.app`.
3. ذاكرة التخزين المؤقت المشتركة في `~/.stackql/mcp-server-bin/`.
4. التنزيل مع التحقق باستخدام sha256 (عند تعيين `allowDownload`).

* * *

## تطبيق توضيحي {#demo-app}

**CloudLens** هو حارس سحابي في شريط القوائم، يضمّن الملف الثنائي الموثق ويشغل مجموعة فحوصات في الوضع `read_only`.

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
