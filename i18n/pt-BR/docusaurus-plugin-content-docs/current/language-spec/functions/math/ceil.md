---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função CEIL no StackQL: arredondar um argumento numérico para cima até o inteiro mais próximo."
image: "/img/stackql-featured-image.png"
---
Arredonda um argumento numérico para cima até o inteiro mais próximo.  

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

## Valor(es) retornado(s) {#return-values}
Retorna o menor inteiro maior ou igual à expressão numérica especificada.

* * *

## Exemplos {#examples}

### Arredondar um número de ponto flutuante para cima até o inteiro mais próximo (maior) {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
