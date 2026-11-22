---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: MCP-инструмент, выводящий список провайдеров и версий, доступных в реестре провайдеров StackQL
image: "/img/stackql-featured-image.png"
---

Возвращает провайдеры (и их версии), доступные в настроенном реестре провайдеров StackQL.  Отличается от инструмента [`list_providers`](/mcp/list_providers), который сообщает только о провайдерах, уже загруженных в локальный кэш.

Передайте необязательный аргумент `provider`, чтобы вывести список опубликованных версий одного провайдера.  Используйте вместе с [`pull_provider`](/mcp/pull_provider), когда агенту нужно установить то, чего еще нет локально.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Команда registry ]](/command-line-usage/registry)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Нет|Имя провайдера.  Если указано, результат ограничивается опубликованными версиями этого провайдера; если не указано, возвращаются все записи реестра.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| Таблица | Одна строка на каждую запись реестра (провайдер и метаданные версии) |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Не влияет на плоскость управления или плоскость данных в облаке - выполняется только чтение реестра.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
