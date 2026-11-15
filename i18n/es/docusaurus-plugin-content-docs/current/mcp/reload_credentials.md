---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: Herramienta MCP que vuelve a cargar las credenciales del archivo dotenv de --env.file e informa del estado de resolución por provider
image: "/img/stackql-featured-image.png"
---

Vuelve a cargar el archivo de estilo dotenv designado por la opción [`--env.file`](/command-line-usage/global-flags) en el entorno del proceso del servidor y, a continuación, informa del estado de resolución de las credenciales de cada provider.  Esto resuelve el problema del entorno fijado al crear el proceso: las credenciales escritas o rotadas en el disco en mitad de la sesión llegan al servidor en ejecución sin necesidad de reiniciarlo.

El archivo de entorno se superpone al entorno del proceso en lugar de sustituirlo: la resolución de credenciales siempre lee el entorno actual del proceso en el momento de la solicitud, por lo que las variables ya presentes al crear el proceso (del shell o del bloque `env` del cliente) siguen funcionando con o sin `--env.file`.  Al recargar, solo se escriben las claves presentes en el archivo con valores no vacíos y nunca se elimina ninguna variable, de modo que una recarga fallida o parcial conserva las credenciales que funcionaban antes.

Los valores de los secretos nunca se devuelven, se escriben en logs ni se auditan - la herramienta solo informa de nombres de variables y estados.  Sin `--env.file` configurado, la herramienta se reduce a un mero sondeo de estado.  Cuando una consulta falla en la resolución de credenciales, el error MCP incluye una indicación que dirige al agente a llamar a `reload_credentials` y reintentar, de modo que los clientes agénticos se recuperan por sí solos.

:::note

Disponible en las versiones de StackQL posteriores a `v0.10.542`.

:::

Consulte también:
[[ Descripción general de MCP ]](/command-line-usage/mcp) [[ (Re)carga de credenciales ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Entradas {#inputs}

| Argumento | Obligatorio | Descripción |
|--|--|--|
|`provider`|No|Limita el informe de estado a un provider.  Cuando se omite, se informa de todos los providers.|

* * *

## Salida {#output}

| Renderizador | Forma |
|--|--|
| Tabla | Resultado de la carga del archivo de entorno y una fila por provider con el estado de resolución de las credenciales |

<br/>

La carga útil estructurada contiene:

| Campo | Descripción |
|--|--|
|`env_file`|Ruta de `--env.file` configurada (vacía cuando no hay ninguna configurada).|
|`env_file_sourced`|`true` cuando el archivo se encontró y se cargó en esta llamada.|
|`sourced_vars`|Nombres de las variables de entorno establecidas desde el archivo - solo nombres, nunca valores.|
|`providers`|Una fila por provider (consulte más abajo).|

<br/>

Cada fila de `providers` tiene:

| Columna | Descripción |
|--|--|
|`provider`|Nombre del provider.|
|`auth_type`|Tipo de autenticación configurado para el provider.|
|`sourced_from`|Origen desde el que se resolvió la credencial, por ejemplo, `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` o `none`.|
|`status`|`ok`, `unresolved` o `not_checked`.|
|`detail`|Texto del error cuando el estado es `unresolved`.|

<br/>

Los tipos de autenticación que se autentican fuera de la ruta de los bytes de credenciales (gcloud interactivo, azure CLI, credenciales de cliente de oauth2, cadenas de asunción de roles, firma de OCI, null) devuelven el estado `not_checked`.

* * *

## Control {#gating}

Permitida en todos los modos del servidor, incluido `read_only` - la herramienta no modifica nada más allá del propio entorno del proceso del servidor.

No se admite en el modo de backend `reverse_proxy`: las consultas se ejecutan en el proceso del servidor stackql remoto, por lo que la herramienta devuelve un error que indica al usuario que recargue las credenciales en el servidor subyacente.

* * *

## Ejemplo {#example}

Pruebe este prompt con cualquier cliente MCP que tenga registrado el servidor MCP de StackQL (iniciado con `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
