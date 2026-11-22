---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Herramienta MCP que instala un provider de StackQL del registro en la caché local
image: "/img/stackql-featured-image.png"
---

Instala un único provider del registro de StackQL en la caché local del approot.  Cuando se omite `version`, se descarga la última versión publicada.

Solo escribe el estado de la caché local: no hay ningún efecto en el plano de control ni en el plano de datos de la nube, por lo que esta herramienta está permitida en todos los modos del servidor (incluido `read_only`).  Use [`list_registry`](/mcp/list_registry) para descubrir el nombre del provider y las versiones disponibles antes de descargarlo.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Comando registry ]](/command-line-usage/registry)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|Sí|Nombre del provider tal como está publicado en el registro (por ejemplo, `aws`, `google`, `cloudflare`).|
|`version`|No|Versión publicada que se va a descargar.  Cuando se omite, se instala la última versión publicada.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV | Confirmación de que el provider (y la versión) se ha instalado en la caché local |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  Sin efectos en la nube; solo cambia el estado de la caché local.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que tenga registrado el servidor MCP de StackQL:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
