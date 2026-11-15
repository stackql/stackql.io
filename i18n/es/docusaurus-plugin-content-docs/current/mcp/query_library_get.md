---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: Herramienta MCP que recupera una plantilla de consulta de StackQL y valida los parámetros proporcionados para devolver el SQL renderizado sin ejecutarlo
image: "/img/stackql-featured-image.png"
---

Recupera una entrada de la [Query Library](pathname:///docs/query-library/) a partir de un `id` devuelto por [`query_library_search`](/mcp/query_library_search).

Si `params` se omite o está vacío, devuelve la plantilla sin procesar, las declaraciones de parámetros y las notas. Con un objeto `params` no vacío, el servidor valida los valores y los sustituye en la plantilla. Solo devuelve SQL cuando el renderizado se realiza correctamente. El modelo no necesita sustituir los valores por sí mismo.

Esta herramienta no ejecuta SQL ni comprueba las credenciales en la nube. Su validación de parámetros es distinta de [`validate_select_query`](/mcp/validate_select_query), que analiza y planifica un `SELECT` con el provider instalado.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`id`|Sí|ID de la consulta tomado de los resultados de la búsqueda, por ejemplo `aws/s3/buckets-list`.|
|`params`|No|Objeto que asigna valores a los nombres de los parámetros declarados. Omítalo o pase `{}` para inspeccionar la plantilla sin renderizarla.|
|`format`|No|Renderizado del texto: `markdown` o `json`. Cuando se omite, usa la configuración de renderizado del servidor (`markdown` de forma predeterminada).|

Los parámetros desconocidos se rechazan. Los parámetros obligatorios sin valor proporcionado ni valor predeterminado se indican en `missing_params`. Los valores proporcionados se comprueban con los tipos y las restricciones declarados; los marcadores de posición sin resolver son errores.

* * *

## Salida {#output}

La vista de texto predeterminada es un bloque KV con la identidad, el estado de mutación, la herramienta siguiente, la validez, los enlaces a la fuente y, si los hay, la plantilla, el SQL renderizado o los errores. Use la carga útil estructurada o `format: "json"` para obtener los metadatos completos:

| Campo | Descripción |
|--|--|
|`id`, `title`, `description`|Identidad y resumen de la entrada.|
|`mutation`, `verb`|Indican si la entrada modifica recursos y, cuando se declara, su tipo de operación (`select`, `mutation` o `lifecycle`).|
|`next_tool`|Herramienta de ejecución de la entrada: `run_select_query`, `run_mutation_query` o `run_lifecycle_operation`. La recuperación no la llama.|
|`params`|Declaraciones de parámetros con nombres, tipos, obligatoriedad y, si los hay, valores predeterminados, descripciones, ejemplos, enumeraciones o patrones.|
|`template`, `notes`|Plantilla sin procesar y notas del autor, que se devuelven cuando `params` se omite o está vacío.|
|`rendered`, `valid`|Indican si el renderizado se intentó y si se realizó correctamente. Ambos son `false` cuando se inspecciona la plantilla sin procesar.|
|`sql`|SQL renderizado, presente solo después de un renderizado correcto.|
|`missing_params`, `unknown_params`, `errors`|Detalles de validación cuando el renderizado falla. No ejecute el SQL hasta que se resuelvan.|
|`auth`, `permissions`|Cuando se declaran, las variables de entorno de credenciales y los permisos nativos del provider necesarios para la ejecución. La recuperación en sí no requiere ninguno de los dos.|
|`cost`, `outputs`, `related`|Cuando se declaran, indicaciones sobre el fan-out y el costo, descripciones de las columnas del resultado e ID de entradas relacionadas.|
|`doc_url`|Página de la Query Library legible para personas.|
|`source_tier`, `source_url`, `stale`|Procedencia del contenido: `primary`, `fallback` o `snapshot`, su URL o ruta integrada, y si el contenido se sirvió desde la caché o desde la instantánea tras un error al obtenerlo.|

Un ID desconocido o mal formado devuelve un error. Use `query_library_search` para encontrar ID válidos.

* * *

## Control {#gating}

Permitida en todos los modos del servidor, sujeta a la lista de permitidos de herramientas configurada. Recuperar o renderizar una plantilla de mutación o de ciclo de vida no tiene ningún efecto en la nube y no necesita aprobación. Ejecutar el SQL devuelto es una llamada independiente que se rige por el [modo](/command-line-usage/mcp#server-modes) del servidor.

* * *

## Ejemplo {#example}

Primero inspeccione una entrada:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Después proporcione la región de enrutamiento que requiere:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Después de comprobar `rendered` y `valid`, valide el `SELECT` devuelto con [`validate_select_query`](/mcp/validate_select_query). Ejecútelo con la herramienta indicada en `next_tool`, pasando el `sql` devuelto y asignando a `source` el `id` de la entrada de la Query Library para la atribución. Las credenciales y los permisos del provider son necesarios en el momento de la ejecución.
