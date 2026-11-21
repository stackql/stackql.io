---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Herramienta MCP que devuelve el contrato de E/S completo de un método de acceso de StackQL
image: "/img/stackql-featured-image.png"
---

Devuelve el contrato de E/S completo de un único método de acceso - siempre EXTENDED, es decir, las entradas, el esquema del cuerpo de la solicitud y la forma completa de la salida.  Aquí es donde un agente obtiene los nombres de las columnas: cada fila lleva un `param_type` con el valor `input_required` (un predicado `WHERE` obligatorio y de coincidencia exacta), `input_optional` u `output` (un campo al que un `SELECT` o un `RETURNING` puede hacer referencia).  Un recurso no tiene una lista única de campos, porque cada método devuelve su propia forma, así que llame a esta herramienta para el método elegido de [`list_methods`](/mcp/list_methods) antes de la primera consulta sobre él.

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|Sí|Nombre del provider.|
|`service`|Sí|Servicio de ese provider.|
|`resource`|Sí|Recurso de `provider.service`.|
|`method`|Sí|Nombre del método tal como lo devuelve [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Los identificadores no pueden contener espacios en blanco, signos de punto y coma ni barras invertidas; una entrada de ese tipo se rechaza antes de construir ninguna instrucción.

:::

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| KV | Una fila por campo de entrada y de salida: nombre, tipo, `param_type` (`input_required`, `input_optional` u `output`), forma y descripción |

* * *

## Control {#gating}

Permitida en todos los modos del servidor.  De solo lectura.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que tenga registrado el servidor MCP de StackQL:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Este es un ejemplo en Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
