---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Herramienta MCP que ejecuta una operación de ciclo de vida EXEC de StackQL
image: "/img/stackql-featured-image.png"
---

Ejecuta una operación de ciclo de vida `EXEC` de StackQL - las acciones del provider que no son CRUD (iniciar/detener una VM, rotar una clave, desencadenar un workflow, etc.).  **Efectos secundarios reales.**

Controlada por el [modo](/command-line-usage/mcp#server-modes) del servidor:

| Modo | `EXEC` de ciclo de vida |
|--|--|
|`read_only`|rechazado|
|`safe` (valor predeterminado)|requiere aprobación (elicitación)|
|`delete_safe`|requiere aprobación|
|`full_access`|permitido|

<br/>

:::info

Una carga útil que contiene varias instrucciones queda controlada por su instrucción con más privilegios

:::

Cuando se requiere aprobación y el cliente no anunció la elicitación, la llamada se rechaza.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC en la especificación del lenguaje ]](/language-spec/exec)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`sql`|Sí|Una instrucción `EXEC` de StackQL dirigida a un método de ciclo de vida.|
|`source`|No|ID de la entrada de la Query Library cuando el SQL procede de [`query_library_get`](/mcp/query_library_get). Solo para atribución; no cambia la ejecución ni las aprobaciones.|
|`format`|No|Renderizado del texto: `markdown` o `json`. Cuando se omite, usa la configuración de renderizado del servidor.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

El cliente MCP mostrará una solicitud de aprobación antes de enviar la operación, a menos que el servidor se ejecute en el modo `full_access`.

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
