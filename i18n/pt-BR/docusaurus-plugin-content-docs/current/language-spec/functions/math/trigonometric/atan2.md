---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Calcular o arco tangente de dois números (y/x) e retornar um ângulo em radianos com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula o arco tangente de dois números, especificamente o ângulo \( \theta \) em radianos cuja tangente é a razão entre dois números especificados (y dividido por x), tratando corretamente os sinais das entradas para determinar o quadrante correto.

Veja também:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*y*__  
A coordenada y (numerador) do ponto.

__*x*__  
A coordenada x (denominador) do ponto.

## Valor(es) retornado(s) {#return-values}
Retorna o arco tangente de y/x em radianos, levando em conta o quadrante correto do ângulo.

* * *

## Exemplos {#examples}

### Calcular o arco tangente do ponto (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Esta consulta retorna π/4, ou aproximadamente `0.785`, pois o arco tangente do ponto (1,1) corresponde a um ângulo de π/4 radianos no primeiro quadrante.

### Calcular o arco tangente do ponto (-10, 10) {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Esta consulta calcula o ângulo do ponto (-10, 10), com y = 10 e x = -10, o que resulta em 3π/4, ou aproximadamente `2.356`: a tangente desse ângulo é -1 e o ponto está no segundo quadrante.
