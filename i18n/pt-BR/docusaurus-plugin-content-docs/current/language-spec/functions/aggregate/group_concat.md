---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função de agregação GROUP_CONCAT no StackQL: concatenar os valores não NULL de uma coluna ou de um agrupamento em uma única string com um separador opcional."
image: "/img/stackql-featured-image.png"
---
Retorna uma string que é a concatenação de todos os valores não `NULL` de uma coluna de entrada ou de um agrupamento.

> A ordem dos elementos concatenados é arbitrária.

Veja também:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumentos {#arguments}

__*columnExpression*__  
Uma coluna ou expressão.  

> Pelo menos um valor de *columnExpression* deve ser não `NULL`; caso contrário, a função retorna `NULL`.

__*separator*__  
Separador usado no resultado concatenado; se *separator* não for especificado, a função usa o valor padrão `,`.

__*groupByColumn*__  
Uma ou mais colunas usadas para realizar operações de resumo ou de agregação. A cláusula `GROUP BY` retorna uma linha para cada agrupamento de colunas.

## Valor(es) retornado(s) {#return-values}

Retorna uma string com uma lista de valores separados por *separator* ou por `,`.

* * *

## Exemplos {#examples}

### Retornar uma lista de valores de uma coluna em todo um recurso {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar uma lista de valores de uma expressão de coluna agrupada por outra expressão de coluna {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
