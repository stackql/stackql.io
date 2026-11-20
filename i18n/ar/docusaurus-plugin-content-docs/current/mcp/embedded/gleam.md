---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - gleam
  - erlang
  - beam
description: ضمّن خادم StackQL MCP في تطبيق Gleam على الهدف Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

ضمّن خادم StackQL MCP في تطبيق Gleam على الهدف Erlang/BEAM. تتكامل المكتبة مع إشراف OTP، وتقوم بتنزيل الملف الثنائي الموقع رقميًا والتحقق منه، ثم تشغيله عبر stdio.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

```
gleam add stackql_mcp
```

تستهدف المكتبة Erlang/BEAM فقط (وليس هدف JavaScript). يتم توفير الخادم كعملية فرعية في OTP عبر `child_spec()` بحيث يمكن وضعه داخل شجرة الإشراف الخاصة بك.

* * *

## البدء السريع {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

واجهة API هي `stackql_mcp.start()` و`stop()` و`child_spec()` لدورة الحياة؛ و`list_tools()` و`call_tool()` للعمليات (مع الدالتين `supervised_list_tools()` و`supervised_call_tool()` المخصصتين للاستخدام تحت مشرف)؛ و`Config` و`default_config()` و`auth_for()` للتكوين.

* * *

## الأوضاع {#modes}

يتم تعيين الوضع في `Config.mode`، وقيمته الافتراضية `read_only`. غيّره إلى `safe` أو `delete_safe` أو `full_access` للتصعيد إلى وضع أعلى:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

عند البدء، تقوم المكتبة بتنزيل bundle الخاص بـ StackQL للمنصة، والتحقق منه مقابل قيم sha256 المقفلة المضمنة فيها، وتخزينه مؤقتًا في ذاكرة التخزين المؤقت المشتركة لعائلة المكتبات. أولوية الحل:

1. القيمة الصريحة لـ `Config.binary`.
2. متغير البيئة `STACKQL_MCP_BIN`.
3. ذاكرة التخزين المؤقت المشتركة في `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

لعمليات النشر دون اتصال أو المعزولة عن الشبكة، اجعل `STACKQL_MCP_BUNDLE` يشير إلى bundle محلي.

* * *

## تطبيق توضيحي {#demo-app}

**pipewatch** هو التطبيق التوضيحي المخطط له لهذه المكتبة. وهو مدرج في خارطة الطريق ولم يتم تنفيذه بعد.

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
