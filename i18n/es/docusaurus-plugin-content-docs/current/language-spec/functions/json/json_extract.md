---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función JSON_EXTRACT en StackQL: extraer uno o varios valores de un objeto o array JSON por ruta, la herramienta principal para aplanar campos anidados de respuestas de API."
image: "/img/stackql-featured-image.png"
---
Devuelve uno o varios valores de un objeto o array JSON bien formado.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipos de datos ]](/language-spec/data-types)

* * * 

:::tip 

Use la función [**DESCRIBE**](/language-spec/describe) para localizar los tipos de datos `array` u `object` que se pueden usar con las funciones JSON de StackQL. Use la función [**JSON_TYPE**](/language-spec/functions/json/json_type) para determinar los tipos de datos de los objetos anidados o de los elementos de un array.

:::

:::note 

La función `json_extract` se implementa como la función `json_extract_path_text` si usa stackql con un backend SQL `postgres`

:::


## Sintaxis {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*json_array_or_object*__  
Un array u objeto JSON.

__*json_path*__  
La ruta JSON a un array u objeto contenido en *json_array_or_object*.  Consulte [jsonpath.com](https://jsonpath.com/).

> Si se proporciona un único *json_path*, se devuelve un resultado escalar que corresponde al tipo de datos de la ruta resuelta.

> Si se proporcionan varios argumentos *json_path*, el resultado es un array compuesto por los objetos resueltos por las rutas respectivas.

> Si se especifica `'$'` en el argumento *json_path*, se devuelve el objeto JSON completo correspondiente a la columna representada por el argumento *json_array_or_object*.

> NOTA: puede usar herramientas como [`json-viewer`](https://aarrange.com/json-viewer) para desarrollar y probar expresiones JSON Path

## Valor(es) devuelto(s) {#return-values}
Una cadena, un entero, un objeto, un array, un booleano o null, según el *json_array_or_object* y el *json_path* proporcionados como entrada.

* * *

## Ejemplos {#examples}

### Devolver los objetos de disco asociados a cada instancia de Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el nombre de dispositivo y el tamaño del primer disco conectado a cada instancia de Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver el nivel de red de la primera NIC conectada a cada instancia de Google Compute Engine {#return-the-network-tier-of-first-nic-attached-to-each-google-compute-engine-instances}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Devolver una lista de los scopes asociados a la primera cuenta de servicio asociada a cada instancia de Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Para obtener más información, consulte [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
