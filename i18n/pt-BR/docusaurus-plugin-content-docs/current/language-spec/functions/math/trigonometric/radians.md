---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Converter um ângulo de graus para radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Converte um ângulo especificado de graus para radianos.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*angle_in_degrees*__  
Uma expressão numérica que representa um ângulo em graus.

## Valor(es) retornado(s) {#return-values}
Retorna o equivalente do ângulo em radianos.

* * *

## Exemplos {#examples}

### Converter 180 graus para radianos {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Esta consulta retorna π (aproximadamente `3.14159`), pois a conversão de 180 graus para radianos resulta em π radianos.

### Converter 90 graus para radianos {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Esta consulta calcula a conversão de 90 graus para radianos, com resultado de π/2, ou aproximadamente `1.5708`, já que 90 graus são a metade de 180 graus.
