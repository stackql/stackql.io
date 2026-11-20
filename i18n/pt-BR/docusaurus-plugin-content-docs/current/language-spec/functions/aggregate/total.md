---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação TOTAL no StackQL: o total dos valores não NULL de uma coluna ou agrupamento, sempre retornado como número de ponto flutuante."
image: "/img/stackql-featured-image.png"
---
Retorna a soma de todos os valores não `NULL` de uma coluna ou de um agrupamento de colunas.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Sintaxe {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Uma coluna ou expressão.

> Se todos os valores de *columnExpression* forem `NULL`, `TOTAL` retorna `0.0`.  

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação.  A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

## Valor(es) retornado(s) {#return-values}

Retorna um valor de ponto flutuante que representa a soma de *columnExpression*.

* * *

## Exemplos {#examples}

### Retornar a soma de uma expressão de coluna em todo um recurso {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar a soma de uma expressão de coluna agrupada por outra expressão de coluna {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
