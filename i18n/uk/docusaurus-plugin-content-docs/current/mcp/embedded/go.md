---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - go
description: Вбудуйте сервер MCP StackQL у застосунок Go через go:embed або як sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Вбудуйте сервер MCP StackQL у скомпільований застосунок Go: або як завантажений sidecar, або включивши його у ваш двійковий файл через вендоринг за допомогою `go:embed`. Клієнт - офіційний SDK MCP для Go.

:::info
Вбудований двійковий файл завантажується й перевіряється за sha256 під час першого запуску або включається у ваш застосунок через вендоринг. Див. [Спільний контракт](/mcp/embedded#common-contract).
:::

* * *

## Встановлення {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Швидкий старт {#quick-start}

Цей приклад передбачає, що двійковий файл включено через вендоринг: функцію `StackqlMCPBinary()` генерує крок отримання в `go:generate`, описаний у розділі [Вендоринг / вбудовування](#vendoring--embedding).

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

API - це функція `StartServer(ctx, Options{Binary, Mode})`; вона повертає клієнт з офіційного SDK MCP для Go.

* * *

## Режими {#modes}

За замовчуванням сервер запускається в режимі `read_only`. Щоб підвищити рівень доступу до `safe`, `delete_safe` або `full_access`, встановіть відповідне значення `Options.Mode`. Підвищення рівня доступу завжди явне - сервер не вмикає інструменти змінення даних і життєвого циклу, якщо ви явно їх не ввімкнете.

* * *

## Вендоринг / вбудовування {#vendoring--embedding}

Включіть двійковий файл через вендоринг за допомогою `go:embed` і кроку отримання в `go:generate`, щоб отримати один автономний виконуваний файл:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Крок отримання завантажує підписаний двійковий файл для цільової платформи й перевіряє його за sha256, а потім записує туди, звідки `go:embed` може включити його у вашу збірку. Під час виконання `StackqlMCPBinary()` повертає вбудований двійковий файл, тож завантаження під час першого запуску не потрібне.

* * *

## Демонстраційний застосунок {#demo-app}

**sandboxctl** - інфраструктурний консьєрж: вбудований сервер і цикл агента Claude в одному двійковому файлі, що планує, перевіряє вартість і створює тимчасові хмарні пісочниці лише після схвалення.

* * *

## Посилання на репозиторій {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
