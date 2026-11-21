---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Інструмент MCP, що повертає ідентичність і середовище виконання сервера MCP StackQL
image: "/img/stackql-featured-image.png"
---

Повертає ідентичність і середовище виконання сервера MCP StackQL: версію, базовий рушій SQL, розташування реєстру провайдерів, режим сервера й попередній прапорець режиму лише для читання.  Викликайте його на вимогу, наприклад щоб діагностувати відхилену або контрольовану операцію чи перевірити поведінку, що залежить від версії; він не потрібен на початку сеансу або перед запитами.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Вхідні дані {#inputs}

Немає.

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV (блок "ключ-значення" у форматі markdown для LLM) | Структуроване корисне навантаження з наведеними нижче полями |

<br/><br/>

| Поле | Опис |
|--|--|
|`version`|Версія збірки StackQL.|
|`commit`|Короткий хеш коміту Git.|
|`build_date`|Позначка часу збірки у форматі RFC3339.|
|`platform`|`os/arch` (наприклад, `linux/amd64`).|
|`transport`|`http` або `stdio`.|
|`sql_backend`|Базовий рушій SQL (наприклад, `sqlite3`).|
|`provider_registry`|URL реєстру провайдерів.|
|`mode`|Активний режим сервера - `read_only`, `safe`, `delete_safe` або `full_access`.|
|`is_read_only`|Попередній логічний прапорець (`true` тоді й лише тоді, коли `mode == read_only`).|

* * *

## Контроль {#gating}

Завжди дозволено в усіх режимах сервера.  Не потрапляє до журналу аудиту як змінення даних.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
