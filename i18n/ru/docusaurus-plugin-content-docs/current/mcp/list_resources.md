---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: MCP-инструмент, выводящий список ресурсов в provider.service StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает ресурсы в `provider.service` - третий уровень иерархии StackQL, описанной на странице [Иерархия ресурсов](/getting-started/resource-hierarchy).  Ресурсы - это объекты, которые указываются в предложении `FROM` запроса StackQL.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Да|Имя провайдера.|
|`service`|Да|Сервис этого провайдера.|

<br/>

:::info

Идентификаторы не могут содержать пробельные символы, точки с запятой или символы обратной косой черты; такие входные данные отклоняются до построения какой-либо инструкции.

:::

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица | Одна строка на каждый ресурс в `provider.service` |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Только чтение.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
List the resources in the google.storage service
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
