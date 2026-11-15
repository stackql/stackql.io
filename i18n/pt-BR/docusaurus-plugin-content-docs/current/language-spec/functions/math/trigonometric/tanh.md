---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular a tangente hiperbólica de um número e retornar o resultado com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula a tangente hiperbólica de um valor especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica cuja tangente hiperbólica será calculada.

## Valor(es) retornado(s) {#return-values}
Retorna a tangente hiperbólica do valor especificado.

* * *

## Exemplos {#examples}

### Calcular a tangente hiperbólica de 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Esta consulta retorna aproximadamente `0.7616`, pois a tangente hiperbólica de 1 é cerca de 0.7616.

### Calcular a tangente hiperbólica de 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Esta consulta calcula a tangente hiperbólica de 0, com resultado `0`, já que a tangente hiperbólica de 0 é exatamente 0.
