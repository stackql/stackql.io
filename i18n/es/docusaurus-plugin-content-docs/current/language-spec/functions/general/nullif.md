---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función NULLIF en StackQL: devolver NULL cuando dos argumentos son iguales y, en caso contrario, el primer argumento."
image: "/img/stackql-featured-image.png"
---
Acepta dos argumentos y devuelve un valor `NULL` si son iguales; en caso contrario, se devuelve el primer argumento.  

> La función `NULLIF` equivale a `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` es útil con funciones de agregación como [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) y [`COUNT`](/language-spec/functions/aggregate/count), cuando el conjunto de resultados contiene valores especiales que no son `NULL`, pero que se quieren tratar como `NULL` a efectos de la agregación.

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Sintaxis {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Dos expresiones de cualquier tipo de datos que se resuelven en un valor `NULL` o no `NULL`.

> La función `NULLIF` busca en sus argumentos, de izquierda a derecha, un argumento que defina una función de intercalación y usa esa función de intercalación para todas las comparaciones de cadenas.  Si ninguno de los argumentos define una función de intercalación, se usa la intercalación `BINARY`.

## Valor(es) devuelto(s) {#return-values}

Devuelve `NULL` si los dos argumentos *expression* se resuelven en el mismo valor; en caso contrario, se devuelve el primer valor. 

* * *

## Ejemplos {#examples}

### Contar las filas en las que un campo no es igual a un valor dado {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
