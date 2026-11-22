---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Інструмент MCP, що виконує операцію життєвого циклу EXEC у StackQL
image: "/img/stackql-featured-image.png"
---

Виконує операцію життєвого циклу `EXEC` у StackQL - дії провайдера, що не належать до CRUD (запуск або зупинка віртуальної машини, ротація ключа, запуск робочого процесу тощо).  **Реальні побічні ефекти.**

Контролюється [режимом](/command-line-usage/mcp#server-modes) сервера:

| Режим | `EXEC` життєвого циклу |
|--|--|
|`read_only`|відхилено|
|`safe` (за замовчуванням)|потрібне схвалення (elicitation)|
|`delete_safe`|потрібне схвалення|
|`full_access`|дозволено|

<br/>

:::info

Корисне навантаження з кількома інструкціями контролюється за інструкцією з найвищими привілеями

:::

Якщо потрібне схвалення, а клієнт не оголосив можливість elicitation, сервер відхиляє виклик.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC у специфікації мови ]](/language-spec/exec)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`sql`|Так|Інструкція `EXEC` у StackQL, яка звертається до методу життєвого циклу.|
|`source`|Ні|Ідентифікатор запису Query Library, якщо SQL отримано за допомогою [`query_library_get`](/mcp/query_library_get). Лише для атрибуції; не впливає на виконання та схвалення.|
|`format`|Ні|Формат текстового відображення: `markdown` або `json`. Якщо аргумент не вказано, сервер використовує власне налаштування відображення.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

Клієнт MCP покаже запит на схвалення перед надсиланням операції, якщо сервер не працює в режимі `full_access`.

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
