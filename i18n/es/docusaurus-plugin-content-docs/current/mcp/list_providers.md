---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Herramienta MCP que enumera los providers en la nube y de SaaS disponibles para el servidor MCP de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve los providers ya descargados en la caché local de StackQL: el nivel superior de la [jerarquía de recursos](/getting-started/resource-hierarchy) de StackQL.  Use esta herramienta como punto de entrada cuando un agente necesite descubrir qué puede consultar.

Para ver los providers disponibles en el *registro* (incluidos los que aún no se han descargado), consulte [`list_registry`](/mcp/list_registry).

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Entradas {#inputs}

Ninguna.

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla (tabla markdown para el LLM) | Una fila por provider |

<br />

Cada fila lleva el nombre del provider y los metadatos de versión tal como los publica el registro.

* * *

## Control {#gating}

Permitida en todos los modos del servidor (`read_only`, `safe`, `delete_safe`, `full_access`).  De solo lectura.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
Which cloud providers are available for you to query right now?
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
