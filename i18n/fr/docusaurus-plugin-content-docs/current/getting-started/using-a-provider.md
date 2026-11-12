---
title: Utiliser un provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Utiliser un provider dans StackQL : le télécharger depuis le registre, s'authentifier par variables d'environnement ou identifiants et interroger ses services et ressources."
image: "/img/stackql-featured-image.png"
---

Voir aussi :  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Les requêtes StackQL peuvent être exécutées sur de nombreux fournisseurs cloud, notamment Google Cloud Platform, Amazon Web Services, Microsoft Azure, Digital Ocean et d'autres. Les spécifications des providers StackQL sont téléchargées depuis le [__StackQL Provider Registry__](/providers) ; pour cela, utilisez la commande StackQL [__`REGISTRY PULL`__](/language-spec/registry) ou l'utilitaire en ligne de commande [__`stackql registry`__](/command-line-usage/registry).


## Installer un provider {#installing-a-provider}

Pour afficher la liste des providers disponibles, exécutez la commande suivante :  

```sql
REGISTRY LIST;
```

Pour installer un provider, exécutez la commande suivante :

```sql
REGISTRY PULL okta;
```

Pour afficher les providers installés sur le système actuel, exécutez la commande suivante :

```sql
SHOW PROVIDERS;
```

## S'authentifier auprès d'un provider {#authenticating-to-a-provider}

L'authentification auprès d'un fournisseur cloud est souvent la première étape d'une routine StackQL. Les métacommandes telles que [`SHOW`](/language-spec/show) et [`DESCRIBE`](/language-spec/describe) peuvent être exécutées sur un provider sans authentification, mais les commandes [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) et [`EXEC`](/language-spec/exec) nécessitent généralement une session authentifiée.  

L'authentification s'effectue au moyen de variables d'environnement définies sur le système (ou fournies comme secrets dans un système de CI). Chaque provider a ses propres variables par défaut pour l'authentification ; consultez donc la documentation de votre provider dans la [documentation du StackQL Provider Registry](/providers). Les variables peuvent aussi être chargées au démarrage depuis un fichier de type dotenv avec l'option globale [`--env.file`](/command-line-usage/global-flags).  

:::tip

Vous pouvez vous authentifier auprès de plusieurs providers dans le même shell interactif ou la même routine par lots non interactive en fournissant plusieurs objets d'authentification, par exemple :  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

C'est particulièrement utile lorsque vous effectuez une jointure entre les données de plusieurs providers dans une seule requête.

:::
