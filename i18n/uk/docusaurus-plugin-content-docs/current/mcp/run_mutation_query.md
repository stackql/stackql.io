---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Інструмент MCP, що виконує інструкції INSERT, UPDATE, REPLACE або DELETE для провайдера через StackQL
image: "/img/stackql-featured-image.png"
---

Виконує інструкції `INSERT`, `UPDATE`, `REPLACE` або `DELETE` для провайдера.  **Реальні побічні ефекти** - успішний виклик змінює стан хмари.

Контролюється [режимом](/command-line-usage/mcp#server-modes) сервера:

| Режим | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|відхилено|відхилено|
|`safe` (за замовчуванням)|потрібне схвалення (elicitation)|потрібне схвалення|
|`delete_safe`|дозволено|потрібне схвалення|
|`full_access`|дозволено|дозволено|

<br/>

:::info

Корисне навантаження з кількома інструкціями контролюється за інструкцією з найвищими привілеями

:::

Якщо змінення даних потребує схвалення, а клієнт не оголосив можливість elicitation протоколу MCP, сервер відхиляє виклик.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT у специфікації мови ]](/language-spec/insert) [[ DELETE у специфікації мови ]](/language-spec/delete)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`sql`|Так|Інструкція `INSERT`, `UPDATE`, `REPLACE` або `DELETE`.|
|`source`|Ні|Ідентифікатор запису Query Library, якщо SQL отримано за допомогою [`query_library_get`](/mcp/query_library_get). Лише для атрибуції; не впливає на виконання та схвалення.|
|`format`|Ні|Формат текстового відображення: `markdown` або `json`. Якщо аргумент не вказано, сервер використовує власне налаштування відображення.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV | `{messages: [...], timestamp}` - підтвердження надсилання, а не тіло відповіді API |

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

Клієнт MCP покаже запит на схвалення перед надсиланням операції видалення, якщо сервер не працює в режимі `full_access`.

У наведеному нижче відео показано процес elicitation, коли сервер MCP StackQL працює в режимі за замовчуванням `safe` - клієнт отримує запит на схвалення інструкції `DELETE` до надсилання будь-якої зміни на боці хмари.  Ви можете відтворити це на власному сервері MCP StackQL за допомогою офіційного інструмента [MCP Inspector](https://github.com/modelcontextprotocol/inspector), запущеного командою `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
