---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o seno de um ângulo, informado em radianos, com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o seno de um ângulo especificado em radianos.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Uma expressão numérica que especifica o ângulo, em radianos, cujo seno será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o seno do ângulo especificado.

* * *

## Exemplos {#examples}

### Calcular o seno de π/2 radianos {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Esta consulta retorna `1`, pois o seno de π/2 radianos é 1.

### Calcular o seno de π radianos {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Esta consulta calcula o seno de π radianos, com resultado de aproximadamente `0`, pois o seno de π é 0.
