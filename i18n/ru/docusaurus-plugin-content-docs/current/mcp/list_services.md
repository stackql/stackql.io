---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: MCP-инструмент, выводящий список сервисов провайдера StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает сервисы, которые предоставляет один провайдер, - второй уровень иерархии StackQL, описанной на странице [Иерархия ресурсов](/getting-started/resource-hierarchy).  Вызывайте этот инструмент после [`list_providers`](/mcp/list_providers), чтобы сузить область поиска, прежде чем переходить к ресурсам.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Да|Имя провайдера (например, `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Идентификаторы не могут содержать пробельные символы, точки с запятой или символы обратной косой черты; такие входные данные отклоняются до построения какой-либо инструкции.

:::

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица | Одна строка на каждый сервис указанного провайдера |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Только чтение.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
List the services available in Google Cloud
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
