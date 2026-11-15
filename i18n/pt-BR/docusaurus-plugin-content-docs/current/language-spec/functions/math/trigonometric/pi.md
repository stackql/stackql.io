---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Obter a constante matemática π (pi) com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Retorna a constante matemática π (pi), que é a razão entre a circunferência de um círculo e o seu diâmetro.

Veja também:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Argumentos {#arguments}

Esta função não exige argumentos.

## Valor(es) retornado(s) {#return-values}
Retorna a constante π, aproximadamente igual a 3.14159.

* * *

## Exemplos {#examples}

### Obter o valor de π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Esta consulta exibe o valor de π, que é aproximadamente `3.14159`, útil em diversos cálculos matemáticos que envolvem círculos e outras funções geométricas.
