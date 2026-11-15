---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função FLOOR no StackQL: arredondar um argumento numérico para baixo até o inteiro mais próximo."
image: "/img/stackql-featured-image.png"
---
Arredonda um argumento numérico para baixo até o inteiro mais próximo.  

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

## Valor(es) retornado(s) {#return-values}
Retorna o maior inteiro menor ou igual à expressão numérica especificada.

* * *

## Exemplos {#examples}

### Arredondar um número de ponto flutuante para baixo até o inteiro mais próximo (menor) {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
