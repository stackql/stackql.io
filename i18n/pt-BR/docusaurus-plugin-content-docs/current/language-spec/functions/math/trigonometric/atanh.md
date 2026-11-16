---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco tangente hiperbólico (tangente hiperbólica inversa) de um número e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco tangente hiperbólico (tangente hiperbólica inversa) de um número, que é o valor em radianos cuja tangente hiperbólica é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica no intervalo de -1 a 1 (exclusive), cujo arco tangente hiperbólico será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco tangente hiperbólico do valor especificado em radianos.

* * *

## Exemplos {#examples}

### Calcular o arco tangente hiperbólico de 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Esta consulta retorna aproximadamente `0.5493`, pois o arco tangente hiperbólico de 0.5 é cerca de 0.5493 radianos.

### Calcular o arco tangente hiperbólico de -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Esta consulta calcula o arco tangente hiperbólico de -0.5, o que resulta em aproximadamente `-0.5493`, pois esse é o valor em radianos cuja tangente hiperbólica é -0.5.
