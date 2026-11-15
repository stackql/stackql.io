---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función de ventana NTILE en StackQL: dividir una partición en N grupos de la forma más uniforme posible y numerar el grupo de cada fila de 1 a N."
image: "/img/stackql-featured-image.png"
---
Divide la partición en N grupos de la forma más uniforme posible y asigna a cada grupo un entero entre 1 y N, en el orden definido por la cláusula `ORDER BY`, o en un orden arbitrario en caso contrario. Si es necesario, los grupos más grandes van primero.

Consulte también:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaxis {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*N*__
Un entero positivo que especifica el número de grupos (cuantiles) en que se divide la partición.

__*PARTITION BY column*__
Opcional. Divide el conjunto de resultados en particiones. La función `NTILE` se aplica a cada partición por separado.

__*ORDER BY column*__
Especifica el orden en que las filas se asignan a los cuantiles.

## Valor(es) devuelto(s) {#return-values}

Devuelve un entero entre 1 y N que indica a qué cuantil (grupo) pertenece la fila actual.

* * *

## Ejemplos {#examples}

### Agrupar colaboradores en cuartiles por nivel de contribución {#group-contributors-into-quartiles-by-contribution-level}

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

Use `NTILE` para crear segmentos o niveles en sus datos. Los casos de uso habituales incluyen dividir los datos en cuartiles (N=4), deciles (N=10) o percentiles (N=100).

:::

Para obtener más información, consulte [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
