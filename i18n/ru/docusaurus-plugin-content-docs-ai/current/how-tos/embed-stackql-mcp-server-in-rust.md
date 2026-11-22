---
title: Как встроить MCP-сервер StackQL в приложение Rust
description: Встройте MCP-сервер StackQL в двоичный файл Rust через sidecar или вендоринг с include_bytes!, чтобы выполнять облачные запросы и создавать ресурсы на SQL без зависимостей во время выполнения.
keywords: [stackql, mcp, rust, rmcp, include_bytes, встроенный MCP-сервер, model context protocol, tokio, агентная инфраструктура, auditron]
proficiencyLevel: Intermediate
faq:
  - question: Как добавить встроенный MCP-сервер StackQL в приложение Rust?
    answer: Добавьте crate `stackql-mcp` (stackql-mcp = "0.1"), затем создайте сервер с помощью `StackqlMcp::builder().mode(Mode::ReadOnly).start().await?`. Клиент - rmcp. По умолчанию возможность sidecar при первом запуске скачивает подписанный двоичный файл, проверяет его по sha256 и сохраняет в общий кэш в каталоге ~/.stackql/mcp-server-bin/.
  - question: Можно ли поставлять один автономный двоичный файл Rust со встроенным StackQL?
    answer: Да. Включите возможность `vendored`, которая встраивает двоичный файл StackQL с помощью include_bytes!, так что весь MCP-сервер находится внутри вашего исполняемого файла. Возможность `sidecar`, используемая по умолчанию, вместо этого скачивает, проверяет и кэширует двоичный файл во время выполнения. Двоичный файл, включенный через вендоринг, увеличивает сборку примерно на 80 MB.
  - question: Как ограничить встроенный MCP-сервер StackQL режимом только для чтения в Rust?
    answer: Вызовите .mode(Mode::ReadOnly) у построителя; этот режим также используется по умолчанию. ReadOnly отклоняет INSERT, UPDATE, DELETE и EXEC. Повышайте уровень доступа до Mode::Safe, Mode::DeleteSafe или Mode::FullAccess, только когда приложению нужно создавать ресурсы - повышение уровня доступа всегда выполняется явно.
  - question: Какова минимальная поддерживаемая версия Rust для stackql-mcp?
    answer: MSRV - Rust 1.88. Этот crate асинхронный и построен на tokio.
---

# Как встроить MCP-сервер StackQL в приложение Rust

MCP-сервер StackQL может работать внутри двоичного файла Rust без `npx`, без отдельной установки и без зависимостей во время выполнения. Crate `stackql-mcp` запускает подписанный двоичный файл StackQL и работает с ним через stdio с помощью клиента `rmcp`, поэтому ваше приложение Rust получает управляемый, самоописываемый SQL-интерфейс к каждому провайдеру облачных и SaaS-сервисов в реестре StackQL. На этом подходе построен **auditron** - терминальный помощник для проверки соответствия требованиям, поставляемый как один двоичный файл.

## Шаги {#steps}

1. **Добавьте crate** в файл `Cargo.toml`:

```toml
stackql-mcp = "0.1"
```

2. **Создайте сервер и выведите список инструментов.** По умолчанию построитель использует `Mode::ReadOnly`:

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

Построитель также предоставляет методы `.binary()`, `.bundle_path()`, `.approot()` и `.command()`. MSRV - Rust 1.88.

3. **Выберите режим.** Метод `.mode()` по умолчанию использует `Mode::ReadOnly`. Задавайте `Mode::Safe`, `Mode::DeleteSafe` или `Mode::FullAccess`, только когда приложению нужно создавать ресурсы. Повышение уровня доступа выполняется явно.

## Поставка одного двоичного файла с include_bytes! {#ship-a-single-binary-with-include_bytes}

Способ получения двоичного файла определяют две возможности (features):

- `sidecar` (по умолчанию) - скачивание, проверка и кэширование подписанного двоичного файла при первом запуске.
- `vendored` - встраивание двоичного файла с помощью `include_bytes!`, чтобы получился один автономный исполняемый файл.

Двоичный файл, включенный через вендоринг, увеличивает сборку примерно на 80 MB.

## Демонстрационное приложение {#demo-app}

**auditron** - терминальный помощник для проверки соответствия требованиям: TUI, который в потоковом режиме выводит результаты проверок (пройдено/не пройдено) вместе с SQL и пояснениями Claude; поставляется как единый двоичный файл размером около 80 MB, включенный через вендоринг.

## Связанные концепции {#related-concepts}

- [Встроенный MCP: справочник по Rust](/mcp/embedded/rust) - установка, API и возможности (features)
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - модель MCP и режимы безопасности
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - внутреннее устройство транспортов, режимов и аудита
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - подход, который это делает возможным
