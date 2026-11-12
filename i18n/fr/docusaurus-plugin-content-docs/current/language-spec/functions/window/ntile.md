---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "Fonction de fenêtrage NTILE dans StackQL : diviser une partition en N groupes aussi équitablement que possible et numéroter le groupe de chaque ligne de 1 à N."
image: "/img/stackql-featured-image.png"
---
Divise la partition en N groupes aussi équitablement que possible et attribue à chaque groupe un entier compris entre 1 et N, dans l'ordre défini par la clause `ORDER BY`, ou dans un ordre arbitraire sinon. Si nécessaire, les groupes les plus grands viennent en premier.

Voir aussi :
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntaxe {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*N*__
Un entier strictement positif indiquant le nombre de groupes (tranches) en lesquels diviser la partition.

__*PARTITION BY column*__
Facultatif. Divise le jeu de résultats en partitions. La fonction `NTILE` est appliquée à chaque partition séparément.

__*ORDER BY column*__
Spécifie l'ordre dans lequel les lignes sont affectées aux tranches.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un entier compris entre 1 et N indiquant à quelle tranche (groupe) appartient la ligne courante.

* * *

## Exemples {#examples}

### Regrouper les contributeurs en quartiles par niveau de contribution {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Utilisez `NTILE` pour créer des segments ou des niveaux dans vos données. Les cas d'utilisation courants incluent la division des données en quartiles (N=4), en déciles (N=10) ou en centiles (N=100).

:::

Pour plus d'informations, consultez [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
