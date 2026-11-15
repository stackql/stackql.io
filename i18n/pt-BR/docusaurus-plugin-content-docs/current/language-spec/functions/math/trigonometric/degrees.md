---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Converter um ângulo de radianos para graus com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Converte um ângulo especificado de radianos para graus.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*angle_in_radians*__  
Uma expressão numérica que representa um ângulo em radianos.

## Valor(es) retornado(s) {#return-values}
Retorna o equivalente do ângulo em graus.

* * *

## Exemplos {#examples}

### Converter π radianos para graus {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Esta consulta retorna aproximadamente `180`, pois a conversão de π radianos (aproximadamente `3.14159`) resulta em cerca de 180 graus.

### Converter π/2 radianos para graus {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Esta consulta calcula a conversão de π/2 radianos (aproximadamente `1.5708`) para graus, com resultado de cerca de `90`, já que π/2 radianos equivalem a um ângulo de 90 graus.
