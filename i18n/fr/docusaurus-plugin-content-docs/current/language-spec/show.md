---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "SHOW dans StackQL : lister les providers, services, ressources et méthodes, générer des modèles INSERT, inspecter la version, les contributeurs et les dépendances du binaire."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Sert à lister les éléments d'une collection, par exemple afficher tous les services disponibles dans un provider ou toutes les ressources disponibles dans un service donné. Expose également la version, les contributeurs et les dépendances logicielles du binaire StackQL en cours d'exécution.

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Syntaxe {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS` et `DEPENDENCIES` ne nécessitent aucune configuration de provider ni d'authentification. `DEPENDENCIES` n'accepte qu'une clause `LIKE` facultative ; `IN`, `FROM`, `WHERE` et `LIMIT` ne sont pas pris en charge.

* * *

## Exemples {#examples}

### Lister tous les providers {#list-all-providers}
Liste tous les fournisseurs cloud disponibles dans la version installée de StackQL.
```sql
-- Returns all of the available cloud providers
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### Lister tous les services d'un fournisseur cloud {#list-all-services-in-a-cloud-provider}
Liste tous les services disponibles chez un fournisseur cloud donné ; utilisez le mot-clé facultatif EXTENDED pour renvoyer des informations supplémentaires.
```sql
-- Returns all services available in a cloud provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### Lister toutes les ressources d'un service de fournisseur cloud {#list-all-resources-within-a-cloud-provider-service}
Liste toutes les ressources disponibles dans un service donné d'un fournisseur cloud. Le mot-clé facultatif `EXTENDED` renvoie la description de la ressource en plus de son nom.
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Afficher les méthodes disponibles pour une ressource d'un service de fournisseur cloud {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Liste toutes les méthodes disponibles pour une ressource donnée.
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Générer un modèle `INSERT` avec la commande `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

La commande `SHOW` permet de générer des modèles d'instructions `INSERT`, comme illustré ici :

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

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

</details>

> Les champs renvoyés peuvent être limités à ceux que le provider exige ; consultez [__Créer des modèles d'infrastructure__](/getting-started/templating) pour des exemples.

> Ce modèle peut ensuite être utilisé avec un fichier de données `json` ou `jsonnet` pour fournir les valeurs à l'exécution ; pour plus d'informations, consultez [__Utiliser des variables__](/getting-started/variables).

### Afficher la version de StackQL {#show-the-stackql-version}

Renvoie la version du processus StackQL en cours d'exécution. Utile dans les modes shell, exec et serveur à protocole filaire PostgreSQL lorsque vous devez confirmer quel build répond aux requêtes. La même information est également exposée par l'outil MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

Utilisez le mot-clé facultatif `EXTENDED` pour renvoyer également le commit git, l'horodatage du build et la plateforme de build :

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Renvoie :</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` ne nécessite aucune configuration de provider, de registre ni d'authentification et peut être exécutée sans risque dans n'importe quelle session.

### Lister les contributeurs de StackQL {#list-stackql-contributors}

Renvoie le classement des contributeurs embarqué dans le binaire StackQL en cours d'exécution. L'instantané crédite les contributeurs GitHub humains qui ont ajouté une étoile au dépôt StackQL, triés par nombre de contributions, du plus élevé au plus faible. Il est figé pour chaque build et n'interroge pas GitHub à l'exécution.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Colonne | Description |
| --- | --- |
| `contributor` | Nom d'utilisateur GitHub |
| `contributions` | Nombre de contributions enregistré dans l'instantané ; inclus avec `EXTENDED` |

### Lister les dépendances logicielles du binaire {#list-the-binarys-software-dependencies}

Renvoie un inventaire hors ligne des modules Go inclus dans l'exécutable StackQL en cours d'exécution. Il constitue une nomenclature logicielle (SBOM) légère du binaire, que les utilisateurs et les agents peuvent interroger à l'exécution. Il comprend les dépendances Go directes et transitives ; les définitions de providers installées, les wrappers distincts, les outils de CI, les modules réservés aux tests, les images de conteneur et les paquets du système d'exploitation sont hors de son périmètre.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

Les colonnes par défaut sont `name`, `version`, `license` et `description`. `EXTENDED` ajoute les autres colonnes ci-dessous sans modifier les lignes renvoyées.

| Colonne | Description |
| --- | --- |
| `name` | Chemin effectif du module Go, avec le chemin de remplacement lorsqu'un remplacement de module Go s'applique |
| `version` | Version exacte du module, y compris les préversions et les pseudo-versions |
| `license` | Identifiant de licence détecté ; `UNKNOWN` lorsqu'il est non identifié ou ambigu |
| `description` | Description du composant, lorsqu'elle est disponible |
| `source_url` | URL du dépôt source ; incluse avec `EXTENDED` |
| `license_url` | URL du fichier de licence, épinglée sur le tag du module ou la révision source ; incluse avec `EXTENDED` |
| `purl` | Package URL identifiant le module et la version ; incluse avec `EXTENDED` |
| `checksum` | Somme de contrôle du module Go avec son préfixe `h1:`, et non une empreinte SHA-256 d'exécutable ou d'archive ; incluse avec `EXTENDED` |
| `original_name` | Chemin du module demandé à l'origine lorsqu'un remplacement s'applique ; inclus avec `EXTENDED` |
| `original_version` | Version demandée à l'origine lorsqu'un remplacement s'applique ; incluse avec `EXTENDED` |

Les lignes sont triées par nom effectif de module, puis par version. Les remplacements utilisent la version, la somme de contrôle et l'attribution du module de remplacement. `original_name` et `original_version` valent `NULL` pour les modules sans remplacement. Les autres métadonnées indisponibles valent également `NULL`, à l'exception des licences non identifiées, qui valent `UNKNOWN`. Pour les remplacements par un chemin du système de fichiers local, aucune version, URL ou somme de contrôle n'est fabriquée.

Utilisez `LIKE` pour rechercher dans le nom effectif du module, le nom d'origine du module ou la description :

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

La correspondance ne tient pas compte de la casse. `%` correspond à zéro, un ou plusieurs caractères et `_` correspond à exactement un caractère. Les colonnes de licence et de version ne sont pas incluses dans la recherche. Doublez un guillemet simple à l'intérieur d'un littéral de chaîne SQL ; après le décodage de la chaîne SQL, les barres obliques inverses sont littérales et n'échappent pas les caractères génériques de `LIKE`.

Lorsqu'aucun module ne correspond, le résultat ne contient aucune ligne. Le format CSV conserve les en-têtes de colonnes, les clients du protocole filaire PostgreSQL conservent les métadonnées de colonnes et la sortie JSON vaut `null`.

L'inventaire combine les métadonnées embarquées au moment du build avec les informations de build Go de l'exécutable. Les requêtes ne nécessitent ni accès réseau, ni installation de Go, ni copie locale des sources, ni cache de modules. Les builds sans métadonnées générées signalent tout de même leurs modules liés, l'attribution manquante étant indiquée par `UNKNOWN` ou `NULL`.

La sortie couvre uniquement les dépendances Go du binaire. Elle n'inclut pas les relations entre dépendances, n'exporte pas de document SPDX ou CycloneDX, et ne constitue ni un SBOM complet du déploiement ni une évaluation de conformité des licences.

* * *
