---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Інструмент MCP, що виконує інструкцію SELECT у StackQL для провайдера
image: "/img/stackql-featured-image.png"
---

Виконує інструкцію `SELECT` для налаштованих провайдерів і повертає рядки результату.  Лише читання - інструмент не приймає змінення даних та операції життєвого циклу; для них використовуйте [`run_mutation_query`](/mcp/run_mutation_query) або [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Інструмент повертає рядки результату клієнтові, але навмисно не записує їх у журнал аудиту.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT у специфікації мови ]](/language-spec/select)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`sql`|Так|Інструкція `SELECT`.  Механізм контролю політик класифікує інструкції метаданих (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) як інструкції лише для читання, тож інструмент приймає і їх; `DESC` розпізнається в StackQL `v0.12.732` і новіших версіях.|
|`row_limit`|Ні|Обмеження кількості рядків, які повертаються клієнтові.  `0` (за замовчуванням) означає відсутність обмеження на боці клієнта.|
|`source`|Ні|Ідентифікатор запису Query Library, якщо SQL отримано за допомогою [`query_library_get`](/mcp/query_library_get). Лише для атрибуції; не впливає на виконання та схвалення.|
|`format`|Ні|Формат текстового відображення: `markdown` або `json`. Якщо аргумент не вказано, сервер використовує власне налаштування відображення.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця | `{rows: [...]}` - один об'єкт на кожен рядок з іменами стовпців як ключами |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера (`read_only`, `safe`, `delete_safe`, `full_access`), якщо кожна інструкція в корисному навантаженні призначена лише для читання.  Починаючи з `v1.0`, цей інструмент відхиляє `INSERT`, `UPDATE`, `REPLACE`, `DELETE` та `EXEC` в усіх режимах, зокрема в режимі `full_access`, з помилкою, яка називає [`run_mutation_query`](/mcp/run_mutation_query) або [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Корисне навантаження з кількома інструкціями класифікується за інструкцією з найвищими привілеями, тому змінення даних після початкової інструкції `SELECT` відхиляється так само.  Інструмент оголошує `destructiveHint: false` і `openWorldHint: true`.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP, у якому зареєстровано сервер MCP StackQL:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
