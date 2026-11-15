---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o resto da divisão de um número por outro com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o resto de *x* dividido por *y*, retornando o módulo. Isso é semelhante a usar o operador '%', mas é compatível com argumentos não inteiros.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*x*__  
O dividendo, que é uma expressão numérica.

__*y*__  
O divisor, também uma expressão numérica.

## Valor(es) retornado(s) {#return-values}
Retorna o resto de *x* dividido por *y*.

* * *

## Exemplos {#examples}

### Calcular o módulo de 10 dividido por 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
