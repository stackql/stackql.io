---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Herramienta MCP que enumera los servicios de un provider de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve los servicios que expone un único provider: el segundo nivel de la [jerarquía de recursos](/getting-started/resource-hierarchy) de StackQL.  Llame a esta herramienta después de [`list_providers`](/mcp/list_providers) para acotar la superficie antes de profundizar en los recursos.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|Sí|Nombre del provider (por ejemplo, `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Los identificadores no pueden contener espacios en blanco, signos de punto y coma ni barras invertidas; una entrada de ese tipo se rechaza antes de construir ninguna instrucción.

:::

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | Una fila por servicio del provider indicado |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  De solo lectura.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que tenga registrado el servidor MCP de StackQL:

```
List the services available in Google Cloud
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
