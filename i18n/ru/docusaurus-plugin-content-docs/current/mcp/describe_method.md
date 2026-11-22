---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: MCP-инструмент, возвращающий полный контракт входных и выходных данных для одного метода доступа StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает полный контракт входных и выходных данных для одного метода доступа - всегда в варианте EXTENDED, то есть со входными данными, схемой тела запроса и полной формой выходных данных.  Именно отсюда агент получает имена столбцов: каждая строка содержит `param_type` со значением `input_required` (обязательный предикат `WHERE` с точным совпадением), `input_optional` или `output` (поле, на которое может ссылаться `SELECT` или `RETURNING`).  У ресурса нет единого списка полей, поскольку каждый метод возвращает собственную форму, поэтому вызывайте этот инструмент для метода, выбранного с помощью [`list_methods`](/mcp/list_methods), перед первым запросом к нему.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Да|Имя провайдера.|
|`service`|Да|Сервис этого провайдера.|
|`resource`|Да|Ресурс в `provider.service`.|
|`method`|Да|Имя метода в том виде, в каком его возвращает [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Идентификаторы не могут содержать пробельные символы, точки с запятой или обратные косые черты; такие входные данные отклоняются до того, как будет построена какая-либо инструкция.

:::

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV | Одна строка на каждое поле входных и выходных данных: имя, тип, `param_type` (`input_required`, `input_optional` или `output`), форма и описание |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Только чтение.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
