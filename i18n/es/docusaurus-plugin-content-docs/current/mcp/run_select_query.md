---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Herramienta MCP que ejecuta un SELECT de StackQL sobre un provider
image: "/img/stackql-featured-image.png"
---

Ejecuta una instrucción `SELECT` sobre los providers configurados y devuelve las filas del resultado.  De solo lectura - no se aceptan mutaciones ni operaciones de ciclo de vida; para ellas, use [`run_mutation_query`](/mcp/run_mutation_query) o [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Las filas del resultado se devuelven al cliente, pero se excluyen deliberadamente del log de auditoría.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT en la especificación del lenguaje ]](/language-spec/select)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`sql`|Sí|Una instrucción `SELECT`.  El control de políticas clasifica las instrucciones de metadatos (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) como de solo lectura, por lo que también se aceptan; `DESC` se reconoce a partir de StackQL `v0.12.732`.|
|`row_limit`|No|Límite del número de filas que se devuelven al cliente.  `0` (valor predeterminado) significa que no hay límite en el lado del cliente.|
|`source`|No|ID de la entrada de la Query Library cuando el SQL procede de [`query_library_get`](/mcp/query_library_get). Solo para atribución; no cambia la ejecución ni las aprobaciones.|
|`format`|No|Renderizado del texto: `markdown` o `json`. Cuando se omite, usa la configuración de renderizado del servidor.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | `{rows: [...]}` - un objeto por fila, con el nombre de la columna como clave |

* * *

## Control {#gating}

Permitida en todos los modos del servidor (`read_only`, `safe`, `delete_safe`, `full_access`) cuando todas las instrucciones de la carga útil son de solo lectura.  A partir de `v1.0`, esta herramienta rechaza `INSERT`, `UPDATE`, `REPLACE`, `DELETE` y `EXEC` en todos los modos, incluido `full_access`, con un error que indica [`run_mutation_query`](/mcp/run_mutation_query) o [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Una carga útil que contiene varias instrucciones se clasifica según su instrucción con más privilegios, de modo que una mutación detrás de un `SELECT` inicial se rechaza de la misma forma.  La herramienta declara `destructiveHint: false` y `openWorldHint: true`.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
