---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - go
description: ضمّن خادم StackQL MCP في تطبيق Go عبر go:embed أو sidecar
image: "/img/stackql-featured-image.png"
---

# Go

ضمّن خادم StackQL MCP في تطبيق Go مجمع، إما كـ sidecar يتم تنزيله، أو كنسخة محلية مضمنة في الملف الثنائي الخاص بك باستخدام `go:embed`. العميل هو Go MCP SDK الرسمي.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## البدء السريع {#quick-start}

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

واجهة API هي `StartServer(ctx, Options{Binary, Mode})`؛ والعميل الذي تعيده يأتي من Go MCP SDK الرسمي.

* * *

## الأوضاع {#modes}

يبدأ الخادم في الوضع `read_only` افتراضيًا. غيّر `Options.Mode` للتصعيد إلى `safe` أو `delete_safe` أو `full_access`. التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

ضمّن نسخة محلية من الملف الثنائي باستخدام `go:embed` عبر خطوة تنزيل في `go:generate`، لإنتاج ملف تنفيذي واحد مستقل:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

تقوم خطوة التنزيل بتنزيل الملف الثنائي الموقع رقميًا للمنصة المستهدفة والتحقق منه باستخدام sha256، ثم تكتبه في موقع يستطيع `go:embed` منه إدراجه في البناء الخاص بك. في وقت التشغيل، تعيد الدالة `StackqlMCPBinary()` الملف الثنائي المضمن، فلا حاجة إلى التنزيل عند التشغيل الأول.

* * *

## تطبيق توضيحي {#demo-app}

**sandboxctl** هو مساعد للبنية التحتية: خادم مضمن مع حلقة وكيل Claude في ملف ثنائي واحد، يخطط لبيئات معزولة سحابية مؤقتة ويفحص تكاليفها ويوفرها بشرط الحصول على الموافقة.

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
