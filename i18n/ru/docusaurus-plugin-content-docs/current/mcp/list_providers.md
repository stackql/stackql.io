---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: MCP-инструмент, выводящий список провайдеров облачных сервисов и SaaS, доступных MCP-серверу StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает провайдеры, уже загруженные в локальный кэш StackQL, - верхний уровень иерархии StackQL, описанной на странице [Иерархия ресурсов](/getting-started/resource-hierarchy).  Используйте этот инструмент как точку входа, когда агенту нужно узнать, к чему он может выполнять запросы.

О провайдерах, доступных в *реестре* (включая еще не загруженные), см. [`list_registry`](/mcp/list_registry).

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Входные данные {#inputs}

Нет.

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица (таблица markdown для LLM) | Одна строка на каждый провайдер |

<br />

Каждая строка содержит имя провайдера и метаданные версии в том виде, в каком они опубликованы в реестре.

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера (`read_only`, `safe`, `delete_safe`, `full_access`).  Только чтение.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
Which cloud providers are available for you to query right now?
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
