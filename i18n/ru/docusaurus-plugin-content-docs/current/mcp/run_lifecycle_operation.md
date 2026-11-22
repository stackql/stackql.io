---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: MCP-инструмент, который выполняет операцию жизненного цикла EXEC в StackQL
image: "/img/stackql-featured-image.png"
---

Выполняет операцию жизненного цикла `EXEC` в StackQL - действия провайдера, не относящиеся к CRUD (запуск или остановка ВМ, смена ключа, запуск рабочего процесса и т. д.).  **Реальные побочные эффекты.**

Контролируется [режимом сервера](/command-line-usage/mcp#server-modes):

| Режим | `EXEC` жизненного цикла |
|--|--|
|`read_only`|отклонено|
|`safe` (по умолчанию)|требуется подтверждение (elicitation)|
|`delete_safe`|требуется подтверждение|
|`full_access`|разрешено|

<br/>

:::info

Полезная нагрузка из нескольких инструкций контролируется по ее инструкции с наибольшими привилегиями

:::

Если требуется подтверждение, а клиент не объявил поддержку elicitation, вызов отклоняется.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC в спецификации языка ]](/language-spec/exec)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`sql`|Да|Инструкция `EXEC` в StackQL, обращающаяся к методу жизненного цикла.|
|`source`|Нет|Идентификатор записи Query Library, если SQL получен из [`query_library_get`](/mcp/query_library_get). Только для указания источника; не влияет на выполнение и подтверждения.|
|`format`|Нет|Текстовое отображение: `markdown` или `json`. Если не указан, используется настройка отображения сервера.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте с поддержкой elicitation, в котором зарегистрирован MCP-сервер StackQL:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

MCP-клиент покажет запрос подтверждения до отправки операции, если только сервер не работает в режиме `full_access`.

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
