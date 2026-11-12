---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: Outil MCP qui recharge les identifiants depuis le fichier dotenv --env.file et indique l'état de résolution par provider
image: "/img/stackql-featured-image.png"
---

Recharge dans l'environnement du processus du serveur le fichier de type dotenv désigné par l'option [`--env.file`](/command-line-usage/global-flags), puis indique l'état de résolution des identifiants pour chaque provider. Cela résout le problème de l'environnement figé au lancement : les identifiants écrits ou renouvelés sur disque en cours de session parviennent au serveur en cours d'exécution sans redémarrage.

Le fichier d'environnement se superpose à l'environnement du processus au lieu de le remplacer : la résolution des identifiants lit toujours l'environnement courant du processus au moment de la requête, de sorte que les variables déjà présentes au lancement (issues du shell ou du bloc `env` du client) continuent de fonctionner avec ou sans `--env.file`. Lors du rechargement, seules les clés présentes dans le fichier avec une valeur non vide sont écrites et rien n'est jamais supprimé, de sorte qu'un rechargement échoué ou partiel préserve les identifiants qui fonctionnaient auparavant.

Les valeurs des secrets ne sont jamais renvoyées, journalisées ni auditées - l'outil n'indique que les noms de variables et les états. Sans `--env.file` configuré, l'outil se réduit à une simple sonde d'état. Lorsqu'une requête échoue à la résolution des identifiants, l'erreur MCP contient une indication invitant l'agent à appeler `reload_credentials` et à réessayer, de sorte que les clients agentiques se rétablissent d'eux-mêmes.

:::note

Disponible dans les versions de StackQL postérieures à `v0.10.542`.

:::

Voir aussi :
[[ Présentation de MCP ]](/command-line-usage/mcp) [[ (Re)chargement des identifiants ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Entrées {#inputs}

| Argument | Obligatoire | Description |
|--|--|--|
|`provider`|Non|Limite le rapport d'état à un seul provider. S'il est omis, le rapport couvre tous les providers.|

* * *

## Sortie {#output}

| Rendu | Forme |
|--|--|
| Tableau | Résultat du chargement du fichier d'environnement, plus une ligne par provider avec l'état de résolution des identifiants |

<br/>

La charge utile structurée contient :

| Champ | Description |
|--|--|
|`env_file`|Le chemin `--env.file` configuré (vide lorsqu'aucun n'est configuré).|
|`env_file_sourced`|`true` lorsque le fichier a été trouvé et chargé lors de cet appel.|
|`sourced_vars`|Noms des variables d'environnement définies à partir du fichier - les noms uniquement, jamais les valeurs.|
|`providers`|Une ligne par provider (voir ci-dessous).|

<br/>

Chaque ligne de `providers` contient :

| Colonne | Description |
|--|--|
|`provider`|Nom du provider.|
|`auth_type`|Type d'authentification configuré pour le provider.|
|`sourced_from`|Origine de la résolution de l'identifiant, par exemple `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` ou `none`.|
|`status`|`ok`, `unresolved` ou `not_checked`.|
|`detail`|Texte de l'erreur lorsque l'état est `unresolved`.|

<br/>

Les types d'authentification qui s'authentifient en dehors du chemin des octets d'identifiants (gcloud interactif, azure CLI, oauth2 client credentials, chaînes assume-role, signature OCI, null) indiquent `not_checked`.

* * *

## Contrôle {#gating}

Autorisé dans tous les modes du serveur, y compris `read_only` - l'outil ne modifie rien en dehors de l'environnement du processus du serveur lui-même.

Non pris en charge en mode backend `reverse_proxy` : les requêtes s'exécutent dans le processus du serveur stackql distant, l'outil renvoie donc une erreur invitant l'utilisateur à recharger les identifiants sur le serveur sous-jacent.

* * *

## Exemple {#example}

Essayez ce prompt avec n'importe quel client MCP dans lequel le serveur MCP StackQL est enregistré (démarré avec `--env.file`) :

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
