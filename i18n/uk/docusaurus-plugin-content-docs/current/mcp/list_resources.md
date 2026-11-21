---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Інструмент MCP, що виводить список ресурсів у provider.service StackQL
image: "/img/stackql-featured-image.png"
---

Повертає ресурси в `provider.service` - третій рівень ієрархії StackQL, описаної на сторінці [Ієрархія ресурсів](/getting-started/resource-hierarchy).  Ресурси - це цілі, які вказують у реченні `FROM` запиту StackQL.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Так|Назва провайдера.|
|`service`|Так|Сервіс у цьому провайдері.|

<br/>

:::info

Ідентифікатори не можуть містити пробільних символів, крапок з комою або зворотних скісних рисок; такі вхідні дані відхиляються ще до побудови будь-якої інструкції.

:::

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця | Один рядок на кожен ресурс у `provider.service` |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Лише читання.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
List the resources in the google.storage service
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
