---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Інструмент MCP, що виводить список провайдерів і версій, доступних у реєстрі провайдерів StackQL
image: "/img/stackql-featured-image.png"
---

Повертає провайдери (і їхні версії), доступні в налаштованому реєстрі провайдерів StackQL.  Цей інструмент відрізняється від [`list_providers`](/mcp/list_providers), який повідомляє лише про провайдери, вже завантажені в локальний кеш.

Передайте необов'язковий аргумент `provider`, щоб вивести список опублікованих версій одного провайдера.  Використовуйте разом із [`pull_provider`](/mcp/pull_provider), коли агентові потрібно встановити щось, чого ще немає локально.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Команда registry ]](/command-line-usage/registry)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Ні|Назва провайдера.  Якщо її вказано, результат обмежується опублікованими версіями цього провайдера; якщо не вказано, повертаються всі записи реєстру.|

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця | Один рядок на кожен запис реєстру (провайдер і метадані версії) |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Не впливає на площину керування чи площину даних у хмарі - виконується лише читання реєстру.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP, у якому зареєстровано сервер MCP StackQL:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
