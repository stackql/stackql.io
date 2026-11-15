---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Herramienta MCP que enumera los recursos de un provider.service de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve los recursos de un `provider.service`: el tercer nivel de la [jerarquía de recursos](/getting-started/resource-hierarchy) de StackQL.  Los recursos son los destinos que aparecen en la cláusula `FROM` de una consulta de StackQL.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|Sí|Nombre del provider.|
|`service`|Sí|Servicio de ese provider.|

<br/>

:::info

Los identificadores no pueden contener espacios en blanco, signos de punto y coma ni barras invertidas; una entrada de ese tipo se rechaza antes de construir ninguna instrucción.

:::

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | Una fila por recurso de `provider.service` |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  De solo lectura.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
List the resources in the google.storage service
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
