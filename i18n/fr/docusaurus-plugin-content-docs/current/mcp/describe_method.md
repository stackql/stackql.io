---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Outil MCP qui renvoie le contrat d'E/S complet d'une méthode d'accès StackQL
image: "/img/stackql-featured-image.png"
---

Renvoie le contrat d'E/S complet d'une seule méthode d'accès - toujours EXTENDED, c'est-à-dire les entrées, le schéma du corps de la requête et la forme complète de la sortie.  C'est ici qu'un agent obtient les noms de colonnes : chaque ligne porte un `param_type` valant `input_required` (un prédicat `WHERE` obligatoire, en correspondance exacte), `input_optional` ou `output` (un champ qu'un `SELECT` ou un `RETURNING` peut référencer).  Une ressource n'a pas de liste de champs unique, car chaque méthode renvoie sa propre forme ; appelez donc cet outil pour la méthode choisie dans [`list_methods`](/mcp/list_methods) avant la première requête sur celle-ci.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Oui|Nom du provider.|
|`service`|Oui|Service de ce provider.|
|`resource`|Oui|Ressource de `provider.service`.|
|`method`|Oui|Nom de la méthode tel que renvoyé par [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Les identifiants ne peuvent pas contenir d'espaces blancs, de points-virgules ni de barres obliques inverses ; une telle entrée est refusée avant la construction de toute instruction.

:::

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV | Une ligne par champ d'entrée et de sortie : nom, type, `param_type` (`input_required`, `input_optional` ou `output`), forme et description |

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur.  En lecture seule.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
