---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - go
description: Embarquez le serveur MCP StackQL dans une application Go via go:embed ou en sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Embarquez le serveur MCP StackQL dans une application Go compilée, soit sous forme de sidecar téléchargé, soit vendored dans votre binaire avec `go:embed`. Le client est le SDK MCP officiel pour Go.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Démarrage rapide {#quick-start}

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

L'API est `StartServer(ctx, Options{Binary, Mode})` ; le client renvoyé provient du SDK MCP officiel pour Go.

* * *

## Modes {#modes}

Le serveur démarre en mode `read_only` par défaut. Définissez `Options.Mode` pour élever le niveau d'accès à `safe`, `delete_safe` ou `full_access`. L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

Effectuez le vendoring du binaire avec `go:embed` au moyen d'une étape de récupération `go:generate`, ce qui produit un exécutable unique et autonome :

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

L'étape de récupération télécharge le binaire signé de la plateforme cible et le vérifie par sha256, puis l'écrit là où `go:embed` peut l'inclure dans votre build. À l'exécution, `StackqlMCPBinary()` renvoie le binaire embarqué ; il n'y a donc aucun téléchargement à la première exécution.

* * *

## Application de démonstration {#demo-app}

**sandboxctl** est un concierge d'infrastructure : un serveur embarqué et une boucle d'agent Claude réunis dans un seul binaire, qui planifie, vérifie les coûts et provisionne des sandboxes cloud éphémères derrière un contrôle d'approbation.

* * *

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
