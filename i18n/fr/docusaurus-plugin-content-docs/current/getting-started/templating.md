---
title: Créer des modèles d'infrastructure
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Générer des modèles INSERT pour toute ressource avec SHOW INSERT dans StackQL, complets ou limités aux paramètres obligatoires, comme base des définitions d'infrastructure."
image: "/img/stackql-featured-image.png"
---
Voir aussi :  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Utiliser des variables ]](/getting-started/variables)

Les ressources cloud sont déployées avec StackQL au moyen d'instructions `INSERT`. Les modèles `INSERT` des différentes ressources cloud sont générés avec la commande `SHOW INSERT`.

### Générer un modèle `INSERT` avec la commande `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Voici un exemple de génération d'un modèle :

```sql
SHOW INSERT INTO google.compute.addresses;
```
Cette instruction peut être exécutée dans le `shell` ou avec une commande `exec`, comme illustré ici :

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Renvoie :__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> Ce modèle peut ensuite être utilisé avec un fichier de données `json` ou `jsonnet` pour fournir les valeurs à l'exécution. Pour plus d'informations, consultez [Utiliser des variables](/getting-started/variables).

### Générer un modèle `INSERT` avec uniquement les paramètres obligatoires {#generating-an-insert-template-with-only-required-parameters}

Pour générer un modèle `INSERT` StackQL ne contenant que les attributs obligatoires, utilisez l'indication de requête `/*+ REQUIRED */`, comme illustré ici :

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Cette instruction peut être exécutée dans le `shell` ou avec une commande `exec`, comme illustré ici :

```shell
./stackql exec 'show insert /*+ REQUIRED */ into compute.instances' --output text -H
```

*__Renvoie :__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
