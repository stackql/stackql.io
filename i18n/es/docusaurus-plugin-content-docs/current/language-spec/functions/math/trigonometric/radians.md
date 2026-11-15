---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Convertir un ángulo de grados a radianes con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Convierte un ángulo especificado de grados a radianes.

Consulte también:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*angle_in_degrees*__  
Una expresión numérica que representa un ángulo en grados.

## Valor(es) devuelto(s) {#return-values}
Devuelve el equivalente del ángulo en radianes.

* * *

## Ejemplos {#examples}

### Convertir 180 grados a radianes {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Esta consulta devuelve π (aproximadamente `3.14159`), ya que convertir 180 grados a radianes da como resultado π radianes.

### Convertir 90 grados a radianes {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Esta consulta calcula la conversión de 90 grados a radianes, cuyo resultado es π/2 o aproximadamente `1.5708`, puesto que 90 grados es la mitad de 180 grados.
