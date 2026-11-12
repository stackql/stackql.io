---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Outil MCP qui liste les providers et les versions disponibles dans le registre de providers StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie les providers (et leurs versions) disponibles dans le registre de providers StackQL configuré.  À distinguer de [`list_providers`](/mcp/list_providers), qui ne signale que les providers déjà téléchargés dans le cache local.

Passez l'argument facultatif `provider` pour lister les versions publiées d'un seul provider.  Associez-le à [`pull_provider`](/mcp/pull_provider) lorsque l'agent doit installer quelque chose qui n'est pas encore disponible en local.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Commande registry ]](/command-line-usage/registry)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Non|Nom du provider.  Lorsqu'il est fourni, le résultat est restreint aux versions publiées de ce provider ; lorsqu'il est omis, toutes les entrées du registre sont renvoyées.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | Une ligne par entrée du registre (provider et métadonnées de version) |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  Aucun effet sur le plan de contrôle ni sur le plan de données du cloud - seul le registre est lu.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
