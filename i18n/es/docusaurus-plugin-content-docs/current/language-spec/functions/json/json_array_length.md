---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función JSON_ARRAY_LENGTH en StackQL: el número de elementos de un array JSON, en el nivel superior o en una ruta dentro de un valor JSON."
image: "/img/stackql-featured-image.png"
---
Devuelve el número de elementos de un array JSON.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

Use la función [**DESCRIBE**](/language-spec/describe) para localizar los tipos de datos `array` u `object` que se pueden usar con las funciones JSON de StackQL. Use la función [**JSON_TYPE**](/language-spec/functions/json/json_type) para determinar los tipos de datos de los objetos anidados o de los elementos de un array.

:::

## Sintaxis {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Un array u objeto JSON.

> Si *json_array_or_object* es un objeto JSON (no un array) y no se especifica *json_path*, se devuelve `0`.

__*json_path*__  
La ruta JSON opcional a un array contenido en un objeto.  Consulte [jsonpath.com](https://jsonpath.com/).

> Si se proporciona *json_path*, la función `json_array_length` localiza la ruta dentro de *json_array_or_object* y devuelve la longitud del array, o `0` si *json_path* es un objeto (no un array).  Si *json_path* no se puede localizar dentro de *json_array_or_object*, se devuelve `NULL`.

> Si *json_array_or_object* o *json_path* no son objetos JSON o no están bien formados, se genera un error.

## Valor(es) devuelto(s) {#return-values}
Devuelve un entero.

* * *

## Ejemplos {#examples}

### Devolver el número de discos conectados a las instancias de Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el número de interfaces de red conectadas a las instancias de Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el número de ACL asociadas a los buckets de Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Devolver el número de scopes asociados a una cuenta de servicio {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
