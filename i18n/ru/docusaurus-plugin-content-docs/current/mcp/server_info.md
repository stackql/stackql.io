---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: MCP-инструмент, который возвращает идентификационные сведения MCP-сервера StackQL и данные о его среде выполнения
image: "/img/stackql-featured-image.png"
---

Возвращает идентификационные сведения MCP-сервера StackQL и данные о его среде выполнения: версию, базовый SQL-движок, расположение реестра провайдеров, режим сервера и прежний флаг режима только для чтения.  Вызывайте его по мере необходимости, например чтобы диагностировать отклоненную или контролируемую операцию либо проверить поведение, зависящее от версии; в начале сеанса или перед запросами он не нужен.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Входные данные {#inputs}

Нет.

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV (блок пар "ключ-значение" в формате markdown для LLM) | Структурированная полезная нагрузка с полями, перечисленными ниже |

<br/><br/>

| Поле | Описание |
|--|--|
|`version`|Версия сборки StackQL.|
|`commit`|Краткий хэш коммита Git.|
|`build_date`|Метка времени сборки в формате RFC3339.|
|`platform`|`os/arch` (например, `linux/amd64`).|
|`transport`|`http` или `stdio`.|
|`sql_backend`|Базовый SQL-движок (например, `sqlite3`).|
|`provider_registry`|URL-адрес реестра провайдеров.|
|`mode`|Активный режим сервера - `read_only`, `safe`, `delete_safe` или `full_access`.|
|`is_read_only`|Прежний логический флаг (`true` тогда и только тогда, когда `mode == read_only`).|

* * *

## Контроль {#gating}

Всегда разрешено во всех режимах сервера.  Не записывается в журнал аудита как изменение данных.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
