---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "SELECT dans StackQL : interroger des ressources cloud et SaaS en SQL, avec filtres WHERE traduits en paramètres d'API, jointures, regroupement, tri et accès aux champs JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Renvoie une ou plusieurs instances d'une ressource.  

Voir aussi :
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Fonctions de fenêtrage `]](/language-spec/windowing_functions)

* * * 

## Syntaxe {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

Un `JOIN` sans clause `ON` est traité comme un `CROSS JOIN` ; la condition de jointure peut donc être indiquée dans la clause `WHERE`. Les versions de StackQL antérieures à `v0.12.732` ne parviennent pas à planifier un `JOIN` sans condition.

* * *

## Exemples {#examples}

### Instruction `SELECT` de base {#basic-select-statement}
Exécutez une instruction `SELECT` de base dans une session authentifiée.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Renvoyer des champs précis avec une instruction `SELECT` {#returning-specified-fields-using-a-select-statement}
Exécutez une instruction `SELECT` avec une liste de colonnes pour renvoyer des champs précis d'une instance Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Renvoyer un jeu de résultats trié par une colonne dans l'ordre croissant avec la clause `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Exécutez une instruction `SELECT` pour renvoyer des champs de la ressource buckets de Cloud Storage, en triant les résultats dans l'ordre croissant (du plus ancien au plus récent).

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Renvoyer un jeu de résultats trié par une colonne dans l'ordre décroissant avec la clause `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Exécutez une instruction `SELECT` pour renvoyer des champs de la ressource buckets de Cloud Storage, en triant les résultats dans l'ordre décroissant (du plus récent au plus ancien).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Obtenir le premier résultat d'une colonne avec les clauses `ORDER BY` et `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Exécutez une instruction `SELECT` pour renvoyer le bucket Cloud Storage créé le plus récemment.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Utiliser la fonction `COUNT` avec les clauses `GROUP BY` et `HAVING` pour compter des ressources selon un regroupement et un filtre {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Exécutez une instruction `SELECT` avec une fonction `COUNT`, en utilisant les clauses `GROUP BY` et `HAVING` pour renvoyer le nombre de buckets dans chaque emplacement autre que l'Asie.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Pour plus d'informations sur la fonction `COUNT` et les autres fonctions d'agrégation prises en charge par StackQL, consultez [Fonctions d'agrégation](/language-spec/functions/aggregate/count).

### Obtenir la liste des VM à la demande et de leur interface réseau à partir d'un VM Scale Set Azure {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Exécutez une instruction `SELECT` utilisant les fonctions [`SPLIT_PART`](/language-spec/functions/string/split_part) et [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) pour obtenir la liste des VM à la demande et de leurs cartes réseau. Notez que, sans qualificateur, subscription_id et resource_group_name s'appliquent aux trois ressources.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

Pour plus d'informations sur les fonctions de fenêtrage, consultez [Fonctions de fenêtrage](/language-spec/windowing_functions).

### Utiliser des expressions de table communes (CTE) avec `WITH` {#using-common-table-expressions-ctes-with-with}

Les CTE permettent de définir des jeux de résultats temporaires nommés qui peuvent être référencés dans la requête principale.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Pour plus d'informations sur les CTE, consultez [WITH (CTE)](/language-spec/with).
