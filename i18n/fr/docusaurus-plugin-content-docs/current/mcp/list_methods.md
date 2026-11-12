---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Outil MCP qui liste les méthodes d'accès d'une ressource StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie les méthodes d'accès (opérations HTTP) liées à une ressource - les méthodes de lecture, les mutations et les opérations de cycle de vie disponibles.  C'est l'outil qu'un agent doit appeler **avant d'écrire la moindre requête**, car les paramètres `WHERE` obligatoires sont déduits de la méthode de lecture choisie.  Choisissez la méthode dont le verbe SQL correspond à l'intention et dont les paramètres obligatoires peuvent être fournis, puis appelez [`describe_method`](/mcp/describe_method) sur celle-ci pour obtenir le contrat complet, y compris les champs de sortie.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Oui|Nom du provider.|
|`service`|Oui|Service de ce provider.|
|`resource`|Oui|Ressource de `provider.service`.|

<br/>

:::info

Les identifiants ne peuvent pas contenir d'espaces blancs, de points-virgules ni de barres obliques inverses ; une telle entrée est refusée avant la construction de toute instruction.

:::

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | Une ligne par méthode d'accès, avec le verbe SQL auquel elle correspond et les paramètres obligatoires |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  En lecture seule.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
