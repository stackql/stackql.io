---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Herramienta MCP que enumera los providers y las versiones disponibles en el registro de providers de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve los providers (y sus versiones) disponibles en el registro de providers de StackQL configurado.  Es distinta de [`list_providers`](/mcp/list_providers), que solo informa de los providers ya descargados en la caché local.

Pase el argumento opcional `provider` para enumerar las versiones publicadas de un único provider.  Combínela con [`pull_provider`](/mcp/pull_provider) cuando el agente necesite instalar algo que aún no esté en local.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Comando registry ]](/command-line-usage/registry)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|No|Nombre del provider.  Cuando se proporciona, el resultado se limita a las versiones publicadas de ese provider; cuando se omite, se devuelven todas las entradas del registro.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | Una fila por entrada del registro (provider y metadatos de versión) |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  Sin efecto en el plano de control ni en el plano de datos de la nube: solo se lee el registro.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que tenga registrado el servidor MCP de StackQL:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
