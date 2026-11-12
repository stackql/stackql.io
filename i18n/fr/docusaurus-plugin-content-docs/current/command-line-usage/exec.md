---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La commande stackql exec exécute une instruction StackQL ou un script d'instructions, avec des options de format de sortie, d'authentification et de registre de providers."
image: "/img/stackql-featured-image.png"
---

Commande servant à exécuter une requête StackQL, soit sous forme d'instruction unique, soit sous forme de lot d'instructions issu d'un script StackQL (fichier IQL).  

* * * 

### Syntaxe {#syntax}

`stackql exec [query] [flags]`

* * *

### Arguments {#arguments}

| Argument | Description | Exemple |
|--|--|--|
|`query`|Requête StackQL à exécuter<br/>ou commande à exécuter | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Notez que la requête doit être placée entre guillemets simples ou doubles_ |    


### Options {#flags}

| Option | Description |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Mode de sortie, les valeurs valides sont : `table` (valeur par défaut), `json`, `jsonl` (JSON délimité par des sauts de ligne, `ndjson` est accepté comme alias), `csv`, `text` et `otel` (enregistrements de journal OpenTelemetry, consultez [Modes de sortie](/getting-started/output-modes#otel-output)), exemple : `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Exécute la requête depuis le fichier spécifié si nécessaire ; si cette option est spécifiée, toute requête fournie en argument est ignorée, exemple : `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Données source de la commande, issues d'un fichier `json` ou `jsonnet`, exemple : `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Option d'exécution à blanc ; seul le préprocesseur s'exécute et la sortie est renvoyée |
|<span class="nowrap">`--var`</span>|Variables externes (qui peuvent être des variables d'environnement ou des secrets) lues par les fichiers jsonnet spécifiés avec l'option `--iqldata`, exemple `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Fichier dans lequel écrire les résultats si nécessaire (par défaut, les résultats sont uniquement affichés dans la console), exemple `--outfile myoutput.csv`|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|Affiche les informations d'aide|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Exécute les requêtes en mode détaillé, avec une sortie supplémentaire envoyée vers stdout ; si l'option `-f` est sélectionnée, ces informations de journalisation supplémentaires sont écrites dans le fichier de sortie avec les résultats de la requête|
<br/>
<br/>
> consultez [Options globales](/command-line-usage/global-flags) pour des options supplémentaires


* * *

### Exemples {#examples}

Afficher les résultats de la requête dans la console au format tableau :
```shell
stackql exec "SHOW services IN google"
```

Afficher les résultats de la requête dans la console au format json :
```shell
stackql exec "SHOW services IN google" -o json
```

Écrire les résultats de la requête dans un fichier au format json :
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Exécuter la requête depuis un fichier d'entrée (fichier IQL) et écrire les résultats dans un fichier au format json :
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Code de sortie {#exit-status}

`exec` se termine avec le code `0` une fois l'entrée traitée ; les erreurs des instructions individuelles sont écrites dans la console, les scripts doivent donc inspecter la sortie.  À partir de `v1.0`, la commande se termine avec le code `1` lorsqu'une instruction de configuration [`SET`](/language-spec/set) ou [`UNSET`](/language-spec/unset) échoue, lorsque la [gestion des accès](/command-line-usage/access-management) est activée et qu'une instruction est refusée, ou lorsqu'une exécution IaC en préversion signale une ressource en échec.
