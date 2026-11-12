---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Outil MCP qui installe un provider StackQL du registre dans le cache local
image: "/img/stackql-featured-image.png"
---

Installe un seul provider du registre StackQL dans le cache local de l'approot.  Lorsque `version` est omis, la dernière version publiée est téléchargée.

N'écrit que l'état du cache local - il n'y a aucun effet sur le plan de contrôle ni sur le plan de données du cloud, cet outil est donc autorisé dans tous les modes du serveur (y compris `read_only`).  Utilisez [`list_registry`](/mcp/list_registry) pour découvrir le nom du provider et les versions disponibles avant le téléchargement.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Commande registry ]](/command-line-usage/registry)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Oui|Nom du provider tel que publié dans le registre (par exemple `aws`, `google`, `cloudflare`).|
|`version`|Non|Version publiée à télécharger.  Lorsqu'elle est omise, la dernière version publiée est installée.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV | Confirmation que le provider (et la version) a été installé dans le cache local |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  Aucun effet côté cloud ; seul l'état du cache local change.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
