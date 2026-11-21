---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Інструмент MCP, що встановлює провайдер StackQL з реєстру в локальний кеш
image: "/img/stackql-featured-image.png"
---

Встановлює один провайдер із реєстру StackQL у локальний кеш approot.  Якщо `version` не вказано, завантажується остання опублікована версія.

Записує лише стан локального кешу - не впливає на площину керування чи площину даних у хмарі, тому цей інструмент дозволено в усіх режимах сервера (зокрема в `read_only`).  Перед завантаженням скористайтеся [`list_registry`](/mcp/list_registry), щоб дізнатися назву провайдера й доступні версії.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Команда registry ]](/command-line-usage/registry)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Так|Назва провайдера в тому вигляді, у якому її опубліковано в реєстрі (наприклад, `aws`, `google`, `cloudflare`).|
|`version`|Ні|Опублікована версія для завантаження.  Якщо її не вказано, встановлюється остання опублікована версія.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| KV | Підтвердження того, що провайдер (і версію) встановлено в локальний кеш |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Жодних змін на боці хмари; змінюється лише стан локального кешу.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP, у якому зареєстровано сервер MCP StackQL:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
