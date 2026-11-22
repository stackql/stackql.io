---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - rust
description: Встройте MCP-сервер StackQL в приложение Rust в режиме sidecar или через вендоринг с include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Встройте MCP-сервер StackQL в приложение Rust: либо как скачиваемый sidecar, либо включив его в ваш двоичный файл через вендоринг с помощью `include_bytes!`. Клиент - `rmcp`.

:::info
Встроенный двоичный файл скачивается и проверяется по sha256 при первом запуске либо включается в ваше приложение через вендоринг. См. [Общий контракт](/mcp/embedded#common-contract).
:::

* * *

## Установка {#install}

Добавьте crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Быстрый старт {#quick-start}

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

API включает `StackqlMcp::builder()` с методами `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` и `.shutdown()`, а также `.list_all_tools()`. Минимальная поддерживаемая версия Rust (MSRV) - 1.88.

* * *

## Режимы {#modes}

По умолчанию построитель использует `Mode::ReadOnly`. Чтобы повысить уровень доступа, вызовите `.mode()` со значением `Mode::Safe`, `Mode::DeleteSafe` или `Mode::FullAccess`. Повышение уровня доступа выполняется явно - сервер не включит инструменты изменения данных и жизненного цикла, пока вы сами их не включите.

* * *

## Вендоринг / встраивание {#vendoring--embedding}

Способ получения двоичного файла определяют две возможности Cargo (features):

- `sidecar` (по умолчанию) - скачивание, проверка и кэширование подписанного двоичного файла при первом запуске.
- `vendored` - встраивание двоичного файла с помощью `include_bytes!`, чтобы получился один автономный исполняемый файл.

Двоичный файл, включенный через вендоринг, увеличивает сборку примерно на 80 MB.

* * *

## Демонстрационное приложение {#demo-app}

**auditron** - терминальный помощник для проверки соответствия требованиям: TUI, который в потоковом режиме выводит результаты проверок (пройдено/не пройдено) вместе с SQL и пояснениями Claude; поставляется как единый двоичный файл размером около 80 MB, включенный через вендоринг.

* * *

## Ссылка на репозиторий {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
