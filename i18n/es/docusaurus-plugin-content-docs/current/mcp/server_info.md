---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Herramienta MCP que devuelve la identidad y el runtime del servidor MCP de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve la identidad y el runtime del servidor MCP de StackQL: versión, motor SQL subyacente, ubicación del registro de providers, modo del servidor y la opción heredada de solo lectura.  Llame a esta herramienta una vez al inicio de la sesión para que el agente sepa a qué está conectado.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Entradas {#inputs}

Ninguna.

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV (bloque clave/valor markdown para el LLM) | Carga útil estructurada con los campos siguientes |

<br/><br/>

| Campo | Descripción |
|--|--|
|`version`|Versión del build de StackQL.|
|`commit`|Hash corto del commit de Git.|
|`build_date`|Marca de tiempo del build en formato RFC3339.|
|`platform`|`os/arch` (por ejemplo, `linux/amd64`).|
|`transport`|`http` o `stdio`.|
|`sql_backend`|Motor SQL subyacente (por ejemplo, `sqlite3`).|
|`provider_registry`|URL del registro de providers.|
|`mode`|Modo activo del servidor - `read_only`, `safe`, `delete_safe` o `full_access`.|
|`is_read_only`|Opción booleana heredada (`true` si y solo si `mode == read_only`).|

* * *

## Control {#gating}

Siempre permitida en todos los modos del servidor.  No se audita como mutación.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
