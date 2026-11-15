---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la raíz cuadrada de un número con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la raíz cuadrada de una expresión numérica no negativa.

Consulte también:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Una expresión numérica no negativa cuya raíz cuadrada se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve la raíz cuadrada del valor numérico no negativo especificado.

* * *

## Ejemplos {#examples}

### Calcular la raíz cuadrada de un número {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
