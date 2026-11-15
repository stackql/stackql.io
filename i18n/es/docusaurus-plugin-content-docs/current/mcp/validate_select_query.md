---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Herramienta MCP que analiza y planifica un SELECT de StackQL sin ejecutarlo
image: "/img/stackql-featured-image.png"
---

Analiza y planifica una única instrucción `SELECT` sin ejecutarla.  Devuelve `{valid, errors}`.  Resulta útil cuando un agente quiere comprobar que su consulta se compila y se vincula con el método descubierto antes de comprometerse con una llamada real a la API.

:::info

Solo `SELECT`, y exactamente una instrucción; una carga útil que contiene más de una instrucción se rechaza con `validate_select_query accepts exactly one statement`.

:::

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`sql`|Sí|Una única instrucción `SELECT`.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV | `valid` (bool) y `errors` (array de cadenas, vacío cuando la consulta es válida) |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  No se realiza ninguna llamada a la API.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
