---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Інструмент MCP, що повертає повний контракт вхідних і вихідних даних для одного методу доступу StackQL
image: "/img/stackql-featured-image.png"
---

Повертає повний контракт вхідних і вихідних даних для одного методу доступу - завжди в режимі EXTENDED, тобто вхідні дані, схему тіла запиту й повну форму вихідних даних.  Саме звідси агент отримує імена стовпців: кожен рядок має `param_type` зі значенням `input_required` (обов'язковий предикат `WHERE` з точним збігом), `input_optional` або `output` (поле, на яке може посилатися `SELECT` чи `RETURNING`).  Ресурс не має єдиного списку полів, оскільки кожен метод повертає власну форму, тому викликайте цей інструмент для методу, вибраного з [`list_methods`](/mcp/list_methods), перед першим запитом до нього.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Так|Ім'я провайдера.|
|`service`|Так|Сервіс цього провайдера.|
|`resource`|Так|Ресурс у `provider.service`.|
|`method`|Так|Ім'я методу, яке повертає [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Ідентифікатори не можуть містити пробільних символів, крапок з комою чи зворотних скісних рисок; такі вхідні дані відхиляються ще до побудови будь-якої інструкції.

:::

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV | Один рядок на кожне вхідне та вихідне поле: ім'я, тип, `param_type` (`input_required`, `input_optional` або `output`), форма й опис |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Лише читання.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
