---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Інструмент MCP, що виконує синтаксичний аналіз і планування інструкції SELECT у StackQL без її виконання
image: "/img/stackql-featured-image.png"
---

Аналізує й планує одну інструкцію `SELECT` без її виконання.  Повертає `{valid, errors}`.  Корисний, коли агент хоче перевірити, що його запит компілюється та прив'язується до знайденого методу, перш ніж виконувати реальний виклик API.

:::info

Лише `SELECT` і рівно одна інструкція: інструмент відхиляє корисне навантаження, що містить більше однієї інструкції, і повертає помилку `validate_select_query accepts exactly one statement`.

:::

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`sql`|Так|Одна інструкція `SELECT`.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV | `valid` (bool) і `errors` (масив рядкових значень, порожній, якщо запит коректний) |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Інструмент не виконує жодних викликів API.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
