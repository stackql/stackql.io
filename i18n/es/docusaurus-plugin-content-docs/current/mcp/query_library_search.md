---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: Herramienta MCP que busca plantillas de consulta seleccionadas de StackQL por intención en lenguaje natural, provider, servicio y tags
image: "/img/stackql-featured-image.png"
---

Busca en la [Query Library de StackQL](pathname:///docs/query-library/) plantillas que coincidan con una intención expresada en lenguaje natural. Consúltela antes de componer SQL desde cero; después pase el `id` de un resultado a [`query_library_get`](/mcp/query_library_get) para inspeccionar la plantilla y validar sus parámetros.

La herramienta solo recupera contenido de la Query Library. No ejecuta SQL, no llama a las API en la nube ni requiere credenciales de provider.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`intent`|Sí|Descripción de la tarea en lenguaje natural.|
|`provider`|No|Filtra por provider, por ejemplo `aws`.|
|`service`|No|Filtra por servicio, por ejemplo `s3`.|
|`tags`|No|Array de tags. Una entrada debe coincidir con todos los tags proporcionados.|
|`include_mutations`|No|Incluye las plantillas de mutación. Valor predeterminado: `false`; se ignora en el modo `read_only`, donde las entradas de mutación siempre se excluyen.|
|`limit`|No|Número máximo de coincidencias. Valor predeterminado: `5` cuando se omite o no es positivo; limitado a `20`.|
|`format`|No|Renderizado del texto: `markdown` o `json`. Cuando se omite, usa la configuración de renderizado del servidor (`markdown` de forma predeterminada).|

* * *

## Salida {#output}

La vista de texto predeterminada es una tabla ordenada por relevancia con `id`, `title`, `mutation`, `required_params` y `score`, seguida de la cita de la fuente y, en su caso, de la orientación para cuando no hay coincidencias. La carga útil estructurada contiene:

| Campo | Descripción |
|--|--|
|`hits`|Entradas ordenadas por relevancia con `id`, `title`, `description` opcional, `providers`, `services` y `required_params`, además de `mutation` y `score`. Continúe con `query_library_get` para obtener las declaraciones de parámetros completas.|
|`miss`|`true` cuando no hay coincidencias o la puntuación más alta está por debajo del umbral de coincidencia. Aun así, se pueden devolver las coincidencias más cercanas con puntuación baja.|
|`guidance`|Cuando no hay coincidencias, dirige al agente a las instrucciones del servidor, las herramientas de descubrimiento y `validate_select_query` para escribir una consulta.|
|`source_tier`|Fuente del contenido: `primary`, `fallback` o `snapshot`.|
|`source_url`|URL o ruta integrada del catálogo utilizado, para citarlo.|
|`stale`|Cuando es `true`, el contenido se sirvió desde la caché o desde la instantánea integrada tras un error al obtenerlo.|

Las entradas obsoletas se excluyen. El servidor almacena el catálogo en caché y puede recurrir, en lugar de la Query Library alojada, a su origen en GitHub y a una instantánea integrada.

* * *

## Control {#gating}

Permitida en todos los modos del servidor, sujeta a la lista de permitidos de herramientas configurada. No se necesitan aprobación ni credenciales de provider. Buscar plantillas de mutación no autoriza su ejecución.

* * *

## Ejemplo {#example}

Argumentos de la herramienta:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Use un `id` de `hits`, como `aws/s3/buckets-list`, con [`query_library_get`](/mcp/query_library_get). Si `miss` es `true`, inspeccione las plantillas más cercanas como orientación o use [`list_methods`](/mcp/list_methods) y [`describe_method`](/mcp/describe_method) antes de escribir el SQL.
