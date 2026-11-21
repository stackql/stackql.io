---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o cosseno de um ângulo, informado em radianos, com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o cosseno de um ângulo especificado em radianos.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Uma expressão numérica que especifica o ângulo em radianos cujo cosseno será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o cosseno do ângulo especificado.

* * *

## Exemplos {#examples}

### Calcular o cosseno de π/3 radianos {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Esta consulta retorna `0.5000000000000001`, a aproximação em ponto flutuante do cosseno de π/3 radianos, que é 0.5.

### Calcular o cosseno de π radianos {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Esta consulta calcula o cosseno de π radianos, o que resulta em `-1`, pois o cosseno de π é -1.
