---
title: Utiliser StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Utiliser StackQL : exécuter des requêtes dans le shell interactif, en non interactif avec exec ou en mode serveur, et découvrir providers, services et ressources au passage."
image: "/img/stackql-featured-image.png"
---
Voir aussi :  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL propose plusieurs modes d'utilisation, notamment : le shell interactif StackQL, la ligne de commande StackQL avec des instructions StackQL, ou l'exécution d'une ou de plusieurs instructions StackQL depuis un fichier d'entrée (fichier IQL). Chacun de ces modes est décrit plus en détail ci-dessous :

- Utiliser le shell interactif StackQL ([`stackql shell`](/command-line-usage/shell))
- Utiliser l'utilitaire en ligne de commande StackQL ([`stackql exec`](/command-line-usage/exec))
- Utiliser un fichier d'entrée (fichier IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Utiliser le shell interactif StackQL {#using-the-stackql-interactive-shell}
Le shell StackQL fournit aux ingénieurs un environnement de programmation interactif pour déployer des ressources cloud et interagir avec elles, et permet aussi l'analyse interactive de leur environnement cloud (avec des instructions [`SELECT`](/language-spec/select)). Le shell est fonctionnellement similaire au shell MySQL ou à d'autres shells REPL (Read Evaluate Print Loop). Le shell accepte les instructions StackQL valides et renvoie les résultats à l'utilisateur. Les instructions peuvent s'étendre sur plusieurs lignes et sont exécutées dès qu'un point-virgule final est saisi.

Le shell StackQL se lance avec la commande `shell`, comme illustré ci-dessous (consultez [StackQL Provider Registry](/providers) pour en savoir plus sur l'authentification des providers) :

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Une fois dans un shell authentifié auprès d'un fournisseur cloud, vous pouvez commencer à exécuter des requêtes StackQL (requêtes `SELECT` ou opérations DDL - infrastructure as code).

Par défaut, le shell StackQL renvoie des résultats tabulaires ; les formats de sortie `json`, `jsonl`, `csv` et `text` sont toutefois disponibles avec l'option `--output`, consultez [Modes de sortie](/getting-started/output-modes).  

Vous pouvez changer le jeu de couleurs du shell StackQL avec l'option `--colorscheme`, comme illustré ci-dessous :  

```shell
# Changing the Shell Color Scheme
./stackql shell --colorscheme light
```
### Le fichier d'initialisation `.stackqlrc` {#the-stackqlrc-initialization-file}

Les valeurs par défaut des options de commande StackQL, comme le format de sortie et le jeu de couleurs évoqués précédemment, peuvent être définies dans le fichier d'initialisation `stackqlrc`. Si ce fichier existe dans le répertoire courant utilisé par l'exécutable StackQL, il est chargé automatiquement ; il peut aussi être désigné avec l'option `--configfile`, comme illustré ci-dessous :

```shell
# Specifing a Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Pour plus d'informations sur les options de commande, consultez la rubrique Options globales.

### Utiliser l'utilitaire en ligne de commande StackQL {#using-the-stackql-command-line-utility}
Les commandes StackQL peuvent être exécutées de manière non interactive avec la commande exec de l'exécutable StackQL. Plusieurs commandes peuvent être exécutées dans un même lot, à condition que chaque commande se termine par un point-virgule (`;`). Les options de commande, comme l'option `--output`, peuvent être indiquées sur la ligne de commande ou dans le fichier d'initialisation `.stackqlrc` évoqué précédemment.  

La sortie de l'exécutable StackQL est envoyée sur stdout du système utilisé ; cette sortie peut être analysée dans un script BASH ou PowerShell.  

Voici un exemple de script Windows PowerShell contenant une commande StackQL :  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$keyfilepath = "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--keyfilepath ${keyfilepath} --output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
La commande PowerShell ci-dessus montre la sélection d'instances Compute Engine et l'affichage de la sortie avec `Out-GridView`.  

### Utiliser un fichier d'entrée (fichier IQL) {#using-an-input-file-iql-file}
Les fichiers d'entrée StackQL permettent d'exécuter plusieurs commandes terminées par des points-virgules. C'est une autre option pour la programmation d'infrastructure non interactive ou par lots. Un fichier d'entrée est indiqué avec l'option `-i` ou `--infile`, comme illustré ci-dessous :

```shell
# create an iql file
echo "select id, name from compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--keyfilepath /mnt/c/tmp/stackql-demo.json \
--output json | jq '.[] | .name'
```
