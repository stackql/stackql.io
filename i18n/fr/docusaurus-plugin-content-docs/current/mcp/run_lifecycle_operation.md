---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Outil MCP qui exécute une opération de cycle de vie EXEC de StackQL
image: "/img/stackql-featured-image.png"
---

Exécute une opération de cycle de vie `EXEC` de StackQL - les actions de provider hors CRUD (démarrer ou arrêter une VM, effectuer la rotation d'une clé, déclencher un workflow, etc.). **Effets de bord réels.**

Contrôlé par le [mode](/command-line-usage/mcp#server-modes) du serveur :

| Mode | `EXEC` de cycle de vie |
|--|--|
|`read_only`|refusé|
|`safe` (valeur par défaut)|approbation requise (élicitation)|
|`delete_safe`|approbation requise|
|`full_access`|autorisé|

<br/>

:::info

Une charge utile contenant plusieurs instructions est contrôlée par son instruction la plus privilégiée

:::

Lorsqu'une approbation est nécessaire et que le client n'a pas annoncé l'élicitation, l'appel est refusé.

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC dans la spécification du langage ]](/language-spec/exec)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`sql`|Oui|Une instruction `EXEC` StackQL ciblant une méthode de cycle de vie.|
|`source`|Non|Identifiant de l'entrée de la Query Library lorsque le SQL provient de [`query_library_get`](/mcp/query_library_get). Attribution uniquement ; ne modifie ni l'exécution ni les approbations.|
|`format`|Non|Rendu du texte : `markdown` ou `json`. S'il est omis, le paramètre de rendu du serveur est utilisé.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP prenant en charge l'élicitation et dans lequel le serveur MCP StackQL est enregistré :

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

Le client MCP affiche une demande d'approbation avant l'envoi de l'opération, sauf si le serveur s'exécute en mode `full_access`.

Voici un exemple dans Claude Desktop :

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
