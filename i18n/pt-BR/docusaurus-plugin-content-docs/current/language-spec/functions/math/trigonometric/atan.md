---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco tangente (tangente inversa) de um número e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco tangente (tangente inversa) de um número, que é o ângulo em radianos cuja tangente é o número especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica cujo arco tangente será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o arco tangente do valor especificado em radianos.

* * *

## Exemplos {#examples}

### Calcular o arco tangente de 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Esta consulta retorna π/4, ou aproximadamente `0.785`, pois o arco tangente de 1 é π/4 radianos.

### Calcular o arco tangente de 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Esta consulta calcula o arco tangente de 0, o que resulta em `0`, pois a tangente de 0 radianos é 0.
