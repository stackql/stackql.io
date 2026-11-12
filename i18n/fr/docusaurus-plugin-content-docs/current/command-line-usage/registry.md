---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La commande stackql registry liste, télécharge et gère les providers du registre de providers StackQL depuis la ligne de commande."
image: "/img/stackql-featured-image.png"
---

Commande servant à interagir avec le registre de providers StackQL.  L'emplacement du registre et la vérification éventuelle des signatures des documents de provider se configurent avec l'option globale [`--registry`](/command-line-usage/global-flags#--registry).  La commande `registry` n'est pas disponible tant que la [gestion des accès](/command-line-usage/access-management) est activée ; utilisez plutôt `REGISTRY PULL` dans une session `exec` ou `shell` protégée.

* * * 

### Syntaxe {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Arguments {#arguments}

| Argument | Description | Exemple |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Opération de registre à effectuer (`list` ou `pull`) | `list` ou `pull` |    
|<span class="nowrap">`provider`</span>|Provider à installer avec la sous-commande `pull`| `okta` |
|<span class="nowrap">`version`</span>|Version facultative du provider à installer avec la sous-commande `pull` *(version du document de provider StackQL)* | `v23.03.00121` |


### Options {#flags}

| Option | Description |
|--|--|
|<span class="nowrap">`-H, --help`</span>|Affiche les informations d'aide|
<br/>
<br/>
> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires

* * *

### Exemples {#examples}

Lister les providers disponibles dans le registre public :
```shell
stackql registry list
```

Télécharger la dernière version d'un provider depuis le registre public :
```shell
stackql registry pull okta
```
