---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: MCP-инструмент, который устанавливает провайдер StackQL из реестра в локальный кэш
image: "/img/stackql-featured-image.png"
---

Устанавливает один провайдер из реестра StackQL в локальный кэш approot.  Если аргумент `version` не указан, загружается последняя опубликованная версия.

Записывает только состояние локального кэша - влияния на плоскость управления или плоскость данных в облаке нет, поэтому этот инструмент разрешен во всех режимах сервера (включая `read_only`).  Перед загрузкой используйте [`list_registry`](/mcp/list_registry), чтобы узнать имя провайдера и доступные версии.

См. также:
[[ Обзор MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Команда registry ]](/command-line-usage/registry)

* * *

## Входные данные {#inputs}

| Аргумент | Обязательный | Описание |
|--|--|--|
|`provider`|Да|Имя провайдера в том виде, в каком оно опубликовано в реестре (например, `aws`, `google`, `cloudflare`).|
|`version`|Нет|Опубликованная версия для загрузки.  Если не указана, устанавливается последняя опубликованная версия.|

* * *

## Выходные данные {#output}

| Средство отображения | Форма |
|--|--|
| KV | Подтверждение того, что провайдер (и версия) установлен в локальный кэш |

* * *

## Контроль {#gating}

Разрешено во всех режимах сервера.  Нет побочных эффектов в облаке; меняется только состояние локального кэша.

* * *

## Пример {#example}

Попробуйте этот промпт в любом MCP-клиенте, в котором зарегистрирован MCP-сервер StackQL:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Вот пример в Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
