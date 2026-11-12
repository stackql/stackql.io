---
title: StackQL dans AWS Cloud Shell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
  - cspm
  - aws
  - amazon web services
description: "Exécutez StackQL dans AWS CloudShell en tant qu'utilisateur connecté : téléchargement du paquet Linux, shell stackql, rôle IAM assumé et requêtes par lots avec exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Utiliser StackQL dans AWS Cloud Shell

Dans ce guide, nous montrons comment utiliser StackQL, un outil de développement puissant qui permet d'interroger et de déployer l'infrastructure et les ressources cloud à l'aide de la syntaxe SQL, depuis AWS Cloud Shell, avec le contexte utilisateur de la session de connexion à la console AWS ou en assumant un autre rôle IAM.    

Testé avec <span class="tutorial_tested_on">backend SQL embarqué</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>Qu'est-ce qu'<b>AWS Cloud Shell</b> ?</summary>

> AWS Cloud Shell est un service de shell préauthentifié, accessible depuis le navigateur, fourni par Amazon Web Services (AWS).  Il permet aux développeurs et aux professionnels de l'informatique de gérer leurs ressources cloud directement depuis leur navigateur web.  Avec AWS Cloud Shell, les utilisateurs peuvent exécuter rapidement des commandes liées aux services AWS sans installer ni configurer l'AWS CLI (Command Line Interface) sur leur machine locale.  Ce service fournit un environnement prêt à l'emploi dans lequel les outils courants de développement et de gestion sont préinstallés, comme l'AWS CLI, Python, Node.js et d'autres, ce qui facilite l'interaction avec les services AWS et l'automatisation des tâches.  AWS Cloud Shell est directement intégré à l'AWS Management Console et offre une expérience fluide pour exécuter des tâches d'administration, explorer les services AWS et gérer l'infrastructure.  Comme AWS Cloud Shell authentifie automatiquement les utilisateurs par leur connexion à la console AWS, il simplifie l'exécution de commandes et de scripts dans le contexte de leur compte AWS, ce qui améliore la productivité et l'efficacité des opérations cloud.

</details>

## Fonctionnement {#how-it-works}

Le paquet Linux de StackQL comprend des scripts permettant d'utiliser `stackql` directement dans les environnements cloud shell intégrés des principaux fournisseurs cloud.  Pour AWS, le script est __`stackql-aws-cloud-shell.sh`__.  L'utilisation du script __`stackql-aws-cloud-shell.sh`__ est présentée ici :  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

Le script `stackql-aws-cloud-shell.sh` télécharge automatiquement la dernière version du provider AWS pour StackQL.  Le code source des scripts cloud shell inclus se trouve sur [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Télécharger le paquet Linux `stackql` depuis AWS Cloud Shell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Pour commencer, depuis AWS Cloud Shell, téléchargez le dernier binaire Linux de StackQL à l'aide des commandes suivantes :  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Ces commandes téléchargent et extraient le binaire `stackql` ainsi que le script `stackql-aws-cloud-shell.sh` dans le répertoire personnel de votre environnement AWS Cloud Shell.  Tout est maintenant prêt !  

## Utiliser `stackql shell` depuis AWS Cloud Shell {#using-the-stackql-shell-from-aws-cloud-shell}

Pour lancer un shell de commandes StackQL authentifié depuis AWS Cloud Shell, il suffit d'exécuter :  

```shell
sh stackql-aws-cloud-shell.sh
```

Vous pouvez maintenant exécuter des requêtes AWS interactives en SQL à l'aide du provider StackQL, comme illustré ici :  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Votre navigateur ne prend pas en charge la balise video.
</video>

## Assumer un rôle IAM avec StackQL {#assuming-an-iam-role-using-stackql}

Assumer un rôle IAM dans AWS vous permet d'effectuer des actions et d'accéder à des ressources auxquelles les permissions par défaut de votre utilisateur ne donnent pas forcément accès.  C'est particulièrement utile lorsque vous avez besoin d'un accès temporaire à des ressources ou que vous devez effectuer des actions avec des permissions différentes de celles de votre compte principal.  Avec StackQL, vous pouvez assumer un rôle IAM de façon transparente pour interroger et gérer des ressources AWS avec les permissions accordées par ce rôle.

<details>
<summary>Comprendre ce que signifie assumer un rôle IAM</summary>

> Assumer un rôle IAM est un moyen sûr d'accorder un accès temporaire à des ressources AWS.  Lorsque vous assumez un rôle, AWS STS vous fournit des identifiants de sécurité temporaires qui vous permettent d'accéder aux services et aux ressources AWS que le rôle est autorisé à utiliser.  Ce mécanisme est idéal pour l'accès intercompte, l'accès temporaire d'utilisateurs ou le respect du principe du moindre privilège.

> Les rôles peuvent être assumés avec l'AWS Management Console ou l'AWS CLI, et directement dans StackQL. Les utilisateurs et les applications ne disposent ainsi que des permissions nécessaires pendant la durée requise, ce qui renforce la posture de sécurité de votre environnement AWS.

</details>

Pour assumer un rôle IAM avec StackQL dans AWS Cloud Shell, vous pouvez utiliser l'option `--role-arn` avec le script `stackql-aws-cloud-shell.sh`.  Cette option requiert l'Amazon Resource Name (ARN) du rôle que vous souhaitez assumer.  Lorsqu'elle est fournie, StackQL utilise AWS Security Token Service (STS) pour assumer le rôle indiqué avant d'exécuter toute requête ou commande.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

En assumant un rôle, vous pouvez utiliser StackQL pour interagir avec des ressources AWS auxquelles votre utilisateur par défaut n'a pas forcément accès, ce qui permet de gérer votre environnement cloud de manière plus souple et plus sûre.  

Lorsque vous exécutez des commandes ou des scripts StackQL avec un rôle IAM assumé, assurez-vous que le rôle dispose des permissions nécessaires pour effectuer les actions voulues et accéder aux ressources indiquées.  Cette approche facilite un contrôle d'accès granulaire et s'aligne sur les bonnes pratiques AWS en matière de sécurité et de gouvernance.  

## Exécuter `stackql exec` (opérations par lots) depuis AWS Cloud Shell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Les requêtes StackQL peuvent aussi être exécutées avec la commande `exec` ; c'est une option utile pour :

- indiquer d'autres formats de sortie comme `json` ou `csv`
- produire des fichiers de sortie pour les résultats des requêtes StackQL
- utiliser des requêtes StackQL fournies dans des fichiers d'entrée (fichiers `.iql` par convention)
- effectuer des opérations complexes, notamment charger des variables ou prétraiter des requêtes (avec `json` ou `jsonnet`)

Des exemples d'utilisation de `exec` dans AWS Cloud Shell sont présentés ici :  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

L'exemple ci-dessus exécute la requête StackQL indiquée et produit un fichier de sortie `CSV`, qui peut être téléchargé depuis AWS Cloud Shell.  Vous pouvez en outre fournir un rôle IAM avec l'argument `--role-arn` afin d'assumer un autre rôle pour votre requête ou votre opération de mutation ; un exemple est présenté ici (dans ce cas, la requête provient d'un fichier d'entrée) :  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Vous trouverez plus d'informations sur la commande `exec` [__ici__](/command-line-usage/exec)
