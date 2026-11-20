---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - kotlin
  - jvm
  - gradle
description: ضمّن خادم StackQL MCP في تطبيق Kotlin/JVM كمكتبة أو كمكون Gradle إضافي
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

ضمّن خادم StackQL MCP في تطبيق Kotlin أو JVM، كمكتبة أو كمكون Gradle إضافي. العميل هو Kotlin MCP SDK الرسمي.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

أضف المكتبة `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

يتطلب JDK 17 وKotlin 2.x. يأتي العميل من Kotlin MCP SDK الرسمي.

* * *

## البدء السريع {#quick-start}

```kotlin
import io.stackql.mcp.LaunchArgs
import io.stackql.mcp.Mode
import io.stackql.mcp.StackqlMcp

suspend fun main() {
    val server = StackqlMcp.builder()
        .mode(Mode.ReadOnly)
        .auth(LaunchArgs.authFor("github", "null_auth"))
        .start()

    server.use {
        val tools = server.client.listTools().tools
        println("${tools.size} tools available")
    }
}
```

واجهة API هي `StackqlMcp.builder()` مع `.mode()` و`.auth()` و`.start()`، والتعداد `Mode`، و`LaunchArgs.authFor()`.

* * *

## الأوضاع {#modes}

يستخدم المنشئ `Mode.ReadOnly` افتراضيًا. استدع `.mode()` مع `Mode.Safe` أو `Mode.DeleteSafe` أو `Mode.FullAccess` للتصعيد إلى وضع أعلى. التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

تقوم المكتبة بتنزيل الملف الثنائي الموقع رقميًا والتحقق منه وتخزينه مؤقتًا في ذاكرة التخزين المؤقت المشتركة في `~/.stackql/mcp-server-bin/`، بشكل مستقل عن دليل العمل. ويربط مكون Gradle الإضافي آلية التشغيل نفسها بالبناء الخاص بك، بحيث يمكن لمهام CI تشغيل استعلامات StackQL دون خطوة تثبيت منفصلة.

* * *

## تطبيق توضيحي {#demo-app}

**costgate** أداة تقيد عمليات نشر البنية التحتية بميزانيات التكاليف. وهي متوفرة كأداة CLI وكمكون Gradle إضافي.

كأداة CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

كمكون Gradle إضافي:

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
