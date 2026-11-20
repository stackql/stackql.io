---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco cosseno hiperbólico (cosseno hiperbólico inverso) de um número com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco cosseno hiperbólico (cosseno hiperbólico inverso) de um número, que é o valor não negativo cujo cosseno hiperbólico é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica maior ou igual a 1, cujo arco cosseno hiperbólico será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco cosseno hiperbólico do valor especificado.

* * *

## Exemplos {#examples}

### Calcular o arco cosseno hiperbólico de 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Esta consulta retorna `0`, pois o arco cosseno hiperbólico de 1 é 0.

### Calcular o arco cosseno hiperbólico de 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Esta consulta calcula o arco cosseno hiperbólico de 10, o que resulta em aproximadamente `2.9932`, pois esse é o valor cujo cosseno hiperbólico é 10.
