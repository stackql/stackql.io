---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Інструмент MCP, що виводить список провайдерів для хмарних платформ і SaaS, доступних серверу MCP StackQL
image: "/img/stackql-featured-image.png"
---

Повертає провайдери, вже завантажені в локальний кеш StackQL, - верхній рівень ієрархії StackQL, описаної на сторінці [Ієрархія ресурсів](/getting-started/resource-hierarchy).  Використовуйте цей інструмент як точку входу, коли агентові потрібно з'ясувати, до чого він може виконувати запити.

Про провайдери, доступні в *реєстрі* (зокрема ще не завантажені), див. [`list_registry`](/mcp/list_registry).

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Вхідні дані {#inputs}

Немає.

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця (таблиця Markdown для LLM) | Один рядок на кожен провайдер |

<br />

Кожен рядок містить назву провайдера й метадані версії в тому вигляді, у якому їх опубліковано в реєстрі.

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера (`read_only`, `safe`, `delete_safe`, `full_access`).  Лише читання.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
Which cloud providers are available for you to query right now?
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
