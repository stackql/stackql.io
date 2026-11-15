---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular a tangente de um ângulo, informado em radianos, com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula a tangente de um ângulo especificado em radianos.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Uma expressão numérica que especifica o ângulo, em radianos, cuja tangente será calculada.

## Valor(es) retornado(s) {#return-values}
Retorna a tangente do ângulo especificado.

* * *

## Exemplos {#examples}

### Calcular a tangente de π/4 radianos {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Esta consulta retorna `1`, pois a tangente de π/4 radianos é 1.

### Calcular a tangente de π radianos {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Esta consulta calcula a tangente de π radianos, com resultado de aproximadamente `0`, já que a tangente de π é muito próxima de 0.
