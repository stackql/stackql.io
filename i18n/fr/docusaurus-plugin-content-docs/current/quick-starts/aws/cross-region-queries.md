---
title: Requêtes multirégions
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
description: "Interrogez et agrégez des ressources AWS de plusieurs régions en une instruction StackQL, avec listes de régions et exécution asynchrone au lieu de boucles par région."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Requêtes multirégions asynchrones avec StackQL

Dans ce guide, nous présentons une capacité puissante de StackQL : interroger et agréger des ressources dans plusieurs régions AWS.  Cela implique de nombreux appels d'API vers différents points de terminaison, effectués de manière asynchrone.  L'ensemble est fluide et transparent pour l'utilisateur.  

> StackQL est un outil de développement puissant qui permet d'interroger et de déployer l'infrastructure et les ressources cloud à l'aide de la syntaxe SQL.  

Testé avec <span class="tutorial_tested_on">backend SQL embarqué</span> <span class="tutorial_tested_on">backend SQL postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Comprendre les régions et les ressources AWS {#understanding-aws-regions-and-resources}

Les ressources AWS sont hébergées dans de nombreux emplacements à travers le monde.  Ces emplacements se composent de régions et de zones de disponibilité, conçues pour permettre aux utilisateurs de placer des instances et de stocker des données à différents endroits.

## Le problème des régions AWS... {#the-problem-with-aws-regions}

Lorsque vous travaillez avec AWS, il est essentiel de comprendre que l'AWS Management Console fournit une vue des ressources limitée à une région, qui permet aux utilisateurs d'interagir avec les ressources et de les gérer dans une seule région à la fois.  De même, les commandes de l'AWS Command Line Interface (CLI) et les kits de développement logiciel (SDK) utilisent la région comme point d'entrée des opérations, ce qui impose d'indiquer une région pour exécuter des commandes ou des programmes.  

> Le cadre régional des services cloud introduit de la complexité lorsque les ressources s'étendent sur plusieurs régions.  En outre, les atteintes à la sécurité, notamment les attaques de minage de cryptomonnaie, utilisent souvent des ressources situées dans différentes zones, au-delà de la région principale habituellement utilisée par les organisations.

## Indiquer des régions dans les requêtes StackQL {#specifying-regions-in-stackql-queries}

Avec StackQL, vous pouvez indiquer des régions pour cibler des ressources situées dans différents emplacements géographiques.  C'est particulièrement utile pour gérer des ressources réparties dans le monde entier.

### Exemple : lister les instances EC2 de plusieurs régions {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Cette requête renvoie la liste des instances EC2 des régions `us-east-1` (Virginie du Nord) et `eu-west-1` (Irlande), ce qui montre comment StackQL peut récupérer des données de plusieurs régions en une seule requête.  Cela fonctionne aussi avec des requêtes d'agrégation, par exemple :  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## La puissance des requêtes asynchrones {#the-power-of-asynchronous-queries}

StackQL exécute ces requêtes multirégions de manière asynchrone, en envoyant plusieurs appels d'API en parallèle à AWS.  La récupération des données est ainsi efficace, sans attendre la fin de l'appel pour une région avant de passer à la suivante.

## Conclusion {#conclusion}

Les requêtes multirégions asynchrones de StackQL offrent une méthode robuste et efficace pour gérer des ressources AWS dans différents emplacements géographiques.  En s'appuyant sur une syntaxe de type SQL pour ces requêtes, StackQL simplifie les tâches complexes de gestion du cloud, ce qui en fait un outil précieux pour les développeurs et les administrateurs cloud qui cherchent à optimiser leur infrastructure AWS.
