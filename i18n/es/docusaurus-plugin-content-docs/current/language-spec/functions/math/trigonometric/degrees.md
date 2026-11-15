---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Convertir un ángulo de radianes a grados con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Convierte un ángulo especificado de radianes a grados.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*angle_in_radians*__  
Una expresión numérica que representa un ángulo en radianes.

## Valor(es) devuelto(s) {#return-values}
Devuelve el equivalente del ángulo en grados.

* * *

## Ejemplos {#examples}

### Convertir π radianes a grados {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Esta consulta devuelve aproximadamente `180`, ya que convertir π radianes (aproximadamente `3.14159`) da como resultado unos 180 grados.

### Convertir π/2 radianes a grados {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Esta consulta calcula la conversión de π/2 radianes (aproximadamente `1.5708`) a grados, cuyo resultado es de alrededor de `90`, puesto que π/2 radianes equivalen a un ángulo de 90 grados.
