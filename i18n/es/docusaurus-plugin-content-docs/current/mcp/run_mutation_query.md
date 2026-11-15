---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Herramienta MCP que ejecuta INSERT, UPDATE, REPLACE o DELETE sobre un provider mediante StackQL
image: "/img/stackql-featured-image.png"
---

Ejecuta `INSERT`, `UPDATE`, `REPLACE` o `DELETE` sobre el provider.  **Efectos secundarios reales** - una llamada correcta cambia el estado en la nube.

Controlada por el [modo](/command-line-usage/mcp#server-modes) del servidor:

| Modo | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|rechazado|rechazado|
|`safe` (valor predeterminado)|requiere aprobación (elicitación)|requiere aprobación|
|`delete_safe`|permitido|requiere aprobación|
|`full_access`|permitido|permitido|

<br/>

:::info

Una carga útil que contiene varias instrucciones queda controlada por su instrucción con más privilegios

:::

Cuando una mutación requiere aprobación y el cliente no anunció la capacidad de elicitación de MCP, la llamada se rechaza.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT en la especificación del lenguaje ]](/language-spec/insert) [[ DELETE en la especificación del lenguaje ]](/language-spec/delete)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`sql`|Sí|Una instrucción `INSERT`, `UPDATE`, `REPLACE` o `DELETE`.|
|`source`|No|ID de la entrada de la Query Library cuando el SQL procede de [`query_library_get`](/mcp/query_library_get). Solo para atribución; no cambia la ejecución ni las aprobaciones.|
|`format`|No|Renderizado del texto: `markdown` o `json`. Cuando se omite, usa la configuración de renderizado del servidor.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV | `{messages: [...], timestamp}` - confirmación del envío, no el cuerpo de la respuesta de la API |

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

El cliente MCP mostrará una solicitud de aprobación antes de enviar la eliminación, a menos que el servidor se ejecute en el modo `full_access`.

El siguiente clip muestra el flujo de elicitación cuando el servidor MCP de StackQL se ejecuta en el modo `safe` predeterminado - se pide al cliente que apruebe el `DELETE` antes de enviar cualquier cambio en la nube.  Puede reproducirlo con su propio servidor MCP de StackQL mediante el [MCP Inspector](https://github.com/modelcontextprotocol/inspector) oficial, con `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
