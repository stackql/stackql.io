---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Outil MCP qui renvoie l'identité et le runtime du serveur MCP StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie l'identité et le runtime du serveur MCP StackQL : version, moteur SQL sous-jacent, emplacement du registre de providers, mode du serveur et booléen de lecture seule hérité. Appelez-le une fois en début de session pour que l'agent sache à quoi il est connecté.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Entrées {#inputs}

Aucune.

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV (bloc markdown clé/valeur destiné au LLM) | Charge utile structurée avec les champs ci-dessous |

<br/><br/>

| Champ | Description |
|--|--|
|`version`|Version du build de StackQL.|
|`commit`|Hash court du commit Git.|
|`build_date`|Horodatage du build au format RFC3339.|
|`platform`|`os/arch` (par exemple `linux/amd64`).|
|`transport`|`http` ou `stdio`.|
|`sql_backend`|Moteur SQL sous-jacent (par exemple `sqlite3`).|
|`provider_registry`|URL du registre de providers.|
|`mode`|Mode actif du serveur - `read_only`, `safe`, `delete_safe` ou `full_access`.|
|`is_read_only`|Booléen hérité (`true` si et seulement si `mode == read_only`).|

* * *

## Contrôle {#gating}

Toujours autorisé dans tous les modes du serveur. Non audité en tant que mutation.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
