---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: MCP-инструмент, который выполняет инструкцию SELECT в StackQL для провайдера
image: "/img/stackql-featured-image.png"
---

Выполняет инструкцию `SELECT` для настроенных провайдеров и возвращает строки результата.  Только чтение - изменения данных и операции жизненного цикла не принимаются; для них используйте [`run_mutation_query`](/mcp/run_mutation_query) или [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Строки результата возвращаются клиенту, но намеренно не включаются в журнал аудита.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT в спецификации языка ]](/language-spec/select)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`sql`|Да|Инструкция `SELECT`.  Инструкции метаданных (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) механизм контроля политик относит к инструкциям только для чтения и тоже принимает; `DESC` распознается в StackQL `v0.12.732` и более поздних версиях.|
|`row_limit`|Нет|Ограничение числа строк, возвращаемых клиенту.  `0` (по умолчанию) означает отсутствие ограничения на стороне клиента.|
|`source`|Нет|Идентификатор записи Query Library, если SQL получен из [`query_library_get`](/mcp/query_library_get). Только для указания источника; не влияет на выполнение и подтверждения.|
|`format`|Нет|Текстовое отображение: `markdown` или `json`. Если не указан, используется настройка отображения сервера.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица | `{rows: [...]}` - по одному объекту на строку, с ключами по именам столбцов |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера (`read_only`, `safe`, `delete_safe`, `full_access`), если каждая инструкция в полезной нагрузке предназначена только для чтения.  Начиная с `v1.0`, этот инструмент отклоняет `INSERT`, `UPDATE`, `REPLACE`, `DELETE` и `EXEC` во всех режимах, включая `full_access`, с ошибкой, в которой указан инструмент [`run_mutation_query`](/mcp/run_mutation_query) или [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Полезная нагрузка из нескольких инструкций классифицируется по ее инструкции с наибольшими привилегиями, поэтому изменение данных после начальной инструкции `SELECT` отклоняется так же.  Инструмент объявляет `destructiveHint: false` и `openWorldHint: true`.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
