---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Інструмент MCP, що виводить список сервісів провайдера StackQL
image: "/img/stackql-featured-image.png"
---

Повертає сервіси, які надає один провайдер, - другий рівень ієрархії StackQL, описаної на сторінці [Ієрархія ресурсів](/getting-started/resource-hierarchy).  Викликайте цей інструмент після [`list_providers`](/mcp/list_providers), щоб звузити область пошуку, перш ніж переходити до ресурсів.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Так|Назва провайдера (наприклад, `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Ідентифікатори не можуть містити пробільних символів, крапок з комою або зворотних скісних рисок; такі вхідні дані відхиляються ще до побудови будь-якої інструкції.

:::

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця | Один рядок на кожен сервіс указаного провайдера |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Лише читання.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP, у якому зареєстровано сервер MCP StackQL:

```
List the services available in Google Cloud
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
