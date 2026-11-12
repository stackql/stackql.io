---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La commande stackql shell lance le shell de commandes interactif de StackQL pour exécuter des requêtes et des commandes de provider sur vos comptes cloud et SaaS."
image: "/img/stackql-featured-image.png"
---

Commande servant à lancer le shell de commandes interactif de StackQL.  Pour plus d'informations, consultez [Utiliser le shell interactif StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Syntaxe {#syntax}

`stackql shell [flags]`

* * *

### Options {#flags}

| Option | Description |
|--|--|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|Affiche les informations d'aide|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Exécute les requêtes en mode détaillé, avec une sortie supplémentaire envoyée vers stdout ; si l'option `-f` est sélectionnée, ces informations de journalisation supplémentaires sont écrites dans le fichier de sortie avec les résultats de la requête|
<br/>
<br/>
> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires

* * *

### Exemples {#examples}

Lancer le shell StackQL en utilisant un fichier de clé de compte de service pour l'authentification.
```shell
stackql shell --keyfilepath /tmp/stackql-demo.json
```
