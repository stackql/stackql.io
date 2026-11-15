---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Obtener la constante matemática π (pi) con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Devuelve la constante matemática π (pi), que es la relación entre la circunferencia de un círculo y su diámetro.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Argumentos {#arguments}

Esta función no requiere ningún argumento.

## Valor(es) devuelto(s) {#return-values}
Devuelve la constante π, aproximadamente igual a 3.14159.

* * *

## Ejemplos {#examples}

### Obtener el valor de π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Esta consulta devuelve el valor de π, que es aproximadamente `3.14159`, útil en diversos cálculos matemáticos relacionados con círculos y otras funciones geométricas.
