---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o cosseno hiperbólico de um número e retornar o resultado com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o cosseno hiperbólico de um valor especificado.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*value*__  
Uma expressão numérica cujo cosseno hiperbólico será calculado.

## Valor(es) retornado(s) {#return-values}
Retorna o cosseno hiperbólico do valor especificado.

* * *

## Exemplos {#examples}

### Calcular o cosseno hiperbólico de 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Esta consulta retorna aproximadamente `1.5431`, pois o cosseno hiperbólico de 1 é cerca de 1.5431.

### Calcular o cosseno hiperbólico de 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Esta consulta calcula o cosseno hiperbólico de 0, o que resulta em `1`, pois o cosseno hiperbólico de 0 é exatamente 1.
