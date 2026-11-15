---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Calcular la tangente de un ángulo, expresado en radianes, con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Calcula la tangente de un ángulo especificado, expresado en radianes.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*radian_value*__  
Una expresión numérica que especifica el ángulo en radianes cuya tangente se va a calcular.

## Valor(es) devuelto(s) {#return-values}
Devuelve la tangente del ángulo especificado.

* * *

## Ejemplos {#examples}

### Calcular la tangente de π/4 radianes {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Esta consulta devuelve `1`, ya que la tangente de π/4 radianes es 1.

### Calcular la tangente de π radianes {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Esta consulta calcula la tangente de π radianes, cuyo resultado es aproximadamente `0`, puesto que la tangente de π es muy cercana a 0.
