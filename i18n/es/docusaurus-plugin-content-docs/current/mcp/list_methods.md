---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Herramienta MCP que enumera los métodos de acceso de un recurso de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve los métodos de acceso (operaciones HTTP) vinculados a un recurso: los métodos de lectura, las mutaciones y las operaciones de ciclo de vida disponibles.  Esta es la herramienta que un agente debe llamar **antes de escribir cualquier consulta**, porque los parámetros `WHERE` obligatorios se infieren del método de lectura elegido.  Elija el método cuyo verbo SQL corresponda a la intención y cuyos parámetros obligatorios se puedan proporcionar; después llame a [`describe_method`](/mcp/describe_method) con ese método para obtener el contrato completo, incluidos los campos de salida.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|Sí|Nombre del provider.|
|`service`|Sí|Servicio de ese provider.|
|`resource`|Sí|Recurso de `provider.service`.|

<br/>

:::info

Los identificadores no pueden contener espacios en blanco, signos de punto y coma ni barras invertidas; una entrada de ese tipo se rechaza antes de construir ninguna instrucción.

:::

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | Una fila por método de acceso, incluidos el verbo SQL al que corresponde y los parámetros obligatorios |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  De solo lectura.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que admita la elicitación y tenga registrado el servidor MCP de StackQL:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
