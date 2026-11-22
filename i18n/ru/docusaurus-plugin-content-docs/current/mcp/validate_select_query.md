---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: MCP-инструмент, который выполняет синтаксический анализ и планирование инструкции SELECT в StackQL без ее выполнения
image: "/img/stackql-featured-image.png"
---

Выполняет синтаксический анализ и планирование одной инструкции `SELECT` без ее выполнения.  Возвращает `{valid, errors}`.  Полезен, когда агенту нужно убедиться, что его запрос компилируется и привязывается к обнаруженному методу, прежде чем выполнять реальный вызов API.

:::info

Только `SELECT` и ровно одна инструкция: полезная нагрузка, содержащая более одной инструкции, отклоняется с ошибкой `validate_select_query accepts exactly one statement`.

:::

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`sql`|Да|Одна инструкция `SELECT`.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV | `valid` (bool) и `errors` (массив строковых значений, пустой, если запрос допустим) |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Вызовы API не выполняются.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
