---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función IIF en StackQL: devolver uno de dos valores según si una condición es verdadera, una expresión if-then-else en línea."
image: "/img/stackql-featured-image.png"
---
Devuelve un valor en función de la evaluación de una expresión de entrada.  

Consulte también:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxis {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> La función `iif()` es lógicamente equivalente a la expresión `CASE` `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Argumentos {#arguments}

__*expression*__  
Una expresión booleana que se va a evaluar.

:::tip 

El argumento *expression* puede ser una función anidada como `JSON_EXTRACT`.

:::

__*result_if_true*__  
El resultado que se devuelve si *expression* se evalúa como `true`.

__*result_if_false*__  
El resultado que se devuelve si *expression* se evalúa como `false`.

## Valor(es) devuelto(s) {#return-values}
El tipo de datos de los argumentos *result_if_true* o *result_if_false*.

* * *

## Ejemplos {#examples}

### Categorizar los tamaños de disco de las instancias de Compute Engine {#catgorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
