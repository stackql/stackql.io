---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Інструмент MCP, що виводить список методів доступу до ресурсу StackQL
image: "/img/stackql-featured-image.png"
---

Повертає методи доступу (операції HTTP), прив'язані до ресурсу, - доступні методи читання, методи змінення даних і операції життєвого циклу.  Це інструмент, який агент має викликати **перед написанням будь-якого запиту**, оскільки обов'язкові параметри `WHERE` випливають із вибраного методу читання.  Виберіть метод, чиє дієслово SQL відповідає наміру і чиї обов'язкові параметри можна надати, а потім викличте для нього [`describe_method`](/mcp/describe_method), щоб отримати повний контракт, зокрема поля виводу.

Див. також:
[[ Огляд MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Вхідні дані {#inputs}

| Аргумент | Обов'язковий | Опис |
|--|--|--|
|`provider`|Так|Назва провайдера.|
|`service`|Так|Сервіс у цьому провайдері.|
|`resource`|Так|Ресурс у `provider.service`.|

<br/>

:::info

Ідентифікатори не можуть містити пробільних символів, крапок з комою або зворотних скісних рисок; такі вхідні дані відхиляються ще до побудови будь-якої інструкції.

:::

* * *

## Вихідні дані {#output}

| Засіб відображення | Форма |
|--|--|
| Таблиця | Один рядок на кожен метод доступу, зокрема дієслово SQL, якому він відповідає, і обов'язкові параметри |

* * *

## Контроль {#gating}

Дозволено в усіх режимах сервера.  Лише читання.

* * *

## Приклад {#example}

Спробуйте цей промпт у будь-якому клієнті MCP з підтримкою elicitation, у якому зареєстровано сервер MCP StackQL:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Ось приклад у Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
