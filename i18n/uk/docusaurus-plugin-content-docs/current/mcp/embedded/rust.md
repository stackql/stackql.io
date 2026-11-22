---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - rust
description: Вбудуйте сервер MCP StackQL у застосунок Rust як sidecar або через вендоринг за допомогою include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Вбудуйте сервер MCP StackQL у застосунок Rust: або як завантажений sidecar, або включивши його у ваш двійковий файл через вендоринг за допомогою `include_bytes!`. Клієнт - `rmcp`.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [Спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

Додайте crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Швидкий старт {#quick-start}

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

API складається з `StackqlMcp::builder()` з методами `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` і `.shutdown()`, а також `.list_all_tools()`. MSRV - Rust 1.88.

* * *

## Режими {#modes}

За замовчуванням будівельник використовує `Mode::ReadOnly`. Щоб підвищити рівень доступу, викличте `.mode()` зі значенням `Mode::Safe`, `Mode::DeleteSafe` або `Mode::FullAccess`. Підвищення рівня доступу завжди явне - сервер не вмикає інструменти змінення даних і життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Джерело двійкового файлу визначають дві можливості Cargo (features):

- `sidecar` (за замовчуванням) - завантаження, перевірка й кешування підписаного двійкового файлу під час першого запуску.
- `vendored` - вбудовування двійкового файлу за допомогою `include_bytes!`, щоб отримати один автономний виконуваний файл.

Двійковий файл, включений через вендоринг, збільшує збірку приблизно на 80 MB.

* * *

## Демонстраційний застосунок {#demo-app}

**auditron** - термінальний помічник для перевірки відповідності вимогам: TUI, що в потоковому режимі виводить результати перевірок (пройдено/не пройдено) разом із відповідним SQL і поясненнями від Claude. Він постачається як один двійковий файл обсягом ~80 MB, включений через вендоринг.

* * *

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
