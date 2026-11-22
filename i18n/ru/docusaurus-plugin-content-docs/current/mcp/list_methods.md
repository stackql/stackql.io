---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: MCP-инструмент, выводящий список методов доступа к ресурсу StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает методы доступа (HTTP-операции), привязанные к ресурсу, - доступные методы чтения, методы изменения данных и операции жизненного цикла.  Этот инструмент агент должен вызывать **перед написанием любого запроса**, так как обязательные параметры `WHERE` определяются по выбранному методу чтения.  Выберите метод, глагол SQL которого соответствует намерению и обязательные параметры которого можно передать, а затем вызовите для него [`describe_method`](/mcp/describe_method), чтобы получить полный контракт, включая выходные поля.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Да|Имя провайдера.|
|`service`|Да|Сервис этого провайдера.|
|`resource`|Да|Ресурс в `provider.service`.|

<br/>

:::info

Идентификаторы не могут содержать пробельные символы, точки с запятой или символы обратной косой черты; такие входные данные отклоняются до построения какой-либо инструкции.

:::

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица | Одна строка на каждый метод доступа, включая глагол SQL, которому он соответствует, и обязательные параметры |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Только чтение.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
