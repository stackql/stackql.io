---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação COUNT no StackQL: o número de linhas de um recurso ou de valores não NULL de uma coluna em um agrupamento."
image: "/img/stackql-featured-image.png"
---
Retorna o número total de registros de um recurso ou a contagem das instâncias não `NULL` de uma coluna em um agrupamento.

Veja também:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  ou __*__  
Uma coluna ou expressão. Se __*__ for especificado, a função conta todos os registros do recurso ou do agrupamento.

> Pelo menos um valor de *columnExpression* deve ser não `NULL`; caso contrário, a função retorna `NULL`.

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação. A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

## Valor(es) retornado(s) {#return-values}

Retorna um inteiro que representa a contagem.

* * *

## Exemplos {#examples}

### Retornar o número de linhas de todo um recurso {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar o número de linhas agrupadas por uma expressão de coluna {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Usar `COUNT` como função de janela para calcular contagens cumulativas {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
