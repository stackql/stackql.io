---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - rust
description: ضمّن خادم StackQL MCP في تطبيق Rust عبر sidecar أو تضمين نسخة محلية باستخدام include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

ضمّن خادم StackQL MCP في تطبيق Rust، إما كـ sidecar يتم تنزيله، أو كنسخة محلية مضمنة في الملف الثنائي الخاص بك باستخدام `include_bytes!`. العميل هو `rmcp`.

:::info
يتم تنزيل الملف الثنائي المضمن والتحقق منه باستخدام sha256 عند التشغيل الأول، أو تضمينه كنسخة محلية في تطبيقك. راجع [العقد المشترك](/mcp/embedded#common-contract).
:::

* * *

## التثبيت {#install}

أضف crate المسمى `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## البدء السريع {#quick-start}

```rust
use stackql_mcp::{Mode, StackqlMcp};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let server = StackqlMcp::builder()
        .mode(Mode::ReadOnly)
        .auth(serde_json::json!({"github": {"type": "null_auth"}}))
        .start()
        .await?;
    let tools = server.list_all_tools().await?;
    println!("{} tools available", tools.len());
    server.shutdown().await?;
    Ok(())
}
```

واجهة API هي `StackqlMcp::builder()` مع `.mode()` و`.auth()` و`.binary()` و`.bundle_path()` و`.approot()` و`.command()` و`.start()` و`.shutdown()`، بالإضافة إلى `.list_all_tools()`. إصدار MSRV هو Rust 1.88.

* * *

## الأوضاع {#modes}

يستخدم المنشئ `Mode::ReadOnly` افتراضيًا. استدع `.mode()` مع `Mode::Safe` أو `Mode::DeleteSafe` أو `Mode::FullAccess` للتصعيد إلى وضع أعلى. التصعيد صريح - لن يمكّن الخادم أدوات تعديل البيانات أو دورة الحياة ما لم تفعّلها اختياريًا.

* * *

## تضمين نسخة محلية / التضمين {#vendoring--embedding}

تتحكم ميزتان من ميزات Cargo في كيفية الحصول على الملف الثنائي:

- `sidecar` (افتراضي) - تنزيل الملف الثنائي الموقع رقميًا والتحقق منه وتخزينه مؤقتًا عند التشغيل الأول.
- `vendored` - تضمين الملف الثنائي باستخدام `include_bytes!` للحصول على ملف تنفيذي واحد مستقل.

يضيف الملف الثنائي المضمن كنسخة محلية نحو 80 MB إلى البناء.

* * *

## تطبيق توضيحي {#demo-app}

**auditron** هو مساعد امتثال في الوحدة الطرفية: واجهة TUI تبث نتائج النجاح/الفشل مع عرض SQL وشروح Claude، ويتم توفيره كملف ثنائي واحد مضمن كنسخة محلية بحجم يقارب 80 MB.

* * *

## رابط المستودع {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
