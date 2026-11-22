---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: MCP-инструмент, который выполняет INSERT, UPDATE, REPLACE или DELETE для провайдера с помощью StackQL
image: "/img/stackql-featured-image.png"
---

Выполняет инструкции `INSERT`, `UPDATE`, `REPLACE` или `DELETE` для провайдера.  **Реальные побочные эффекты** - успешный вызов изменяет состояние в облаке.

Контролируется [режимом сервера](/command-line-usage/mcp#server-modes):

| Режим | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|отклонено|отклонено|
|`safe` (по умолчанию)|требуется подтверждение (elicitation)|требуется подтверждение|
|`delete_safe`|разрешено|требуется подтверждение|
|`full_access`|разрешено|разрешено|

<br/>

:::info

Полезная нагрузка из нескольких инструкций контролируется по ее инструкции с наибольшими привилегиями

:::

Если изменение данных требует подтверждения, а клиент не объявил возможность MCP elicitation, вызов отклоняется.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT в спецификации языка ]](/language-spec/insert) [[ DELETE в спецификации языка ]](/language-spec/delete)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`sql`|Да|Инструкция `INSERT`, `UPDATE`, `REPLACE` или `DELETE`.|
|`source`|Нет|Идентификатор записи Query Library, если SQL получен из [`query_library_get`](/mcp/query_library_get). Только для указания источника; не влияет на выполнение и подтверждения.|
|`format`|Нет|Текстовое отображение: `markdown` или `json`. Если не указан, используется настройка отображения сервера.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV | `{messages: [...], timestamp}` - подтверждение отправки, а не тело ответа API |

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте с поддержкой elicitation, в котором зарегистрирован MCP-сервер StackQL:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

MCP-клиент покажет запрос подтверждения до отправки операции удаления, если только сервер не работает в режиме `full_access`.

В следующем видео показан процесс elicitation, когда MCP-сервер StackQL работает в режиме `safe`, который используется по умолчанию - клиенту предлагается подтвердить инструкцию `DELETE` до отправки каких-либо изменений на стороне облака.  Вы можете воспроизвести это на собственном MCP-сервере StackQL с помощью официального [MCP Inspector](https://github.com/modelcontextprotocol/inspector), запущенного командой `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
