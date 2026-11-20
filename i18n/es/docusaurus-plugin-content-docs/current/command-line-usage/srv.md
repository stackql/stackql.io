---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El comando stackql srv ejecuta StackQL como servidor del protocolo de red de PostgreSQL, para que psql, herramientas de BI y drivers consulten API en la nube y de SaaS con SQL."
image: "/img/stackql-featured-image.png"
---

Comando que se usa para iniciar StackQL como servicio, al que después pueden acceder clientes del [protocolo de red de Postgres](https://www.postgresql.org/docs/current/protocol.html) autenticados mediante mTLS. El comando `srv` también puede ejecutar un servidor [Model Context Protocol (MCP)](/command-line-usage/mcp) junto con el servidor del protocolo de red de PostgreSQL, lo que permite el acceso mediante ambos protocolos tanto a clientes de bases de datos tradicionales como a agentes de IA.

* * * 

### Sintaxis {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Aunque el servidor de StackQL usa el protocolo de red de Postgres, no es un servidor Postgres ni lo requiere.  El cliente usa el protocolo de red para comunicarse con el servidor, que usa StackQL para realizar consultas u operaciones DML en providers remotos en la nube y de SaaS y devolver los resultados al cliente.

:::

Los parámetros enviados mediante el protocolo de consulta extendido (`Parse` / `Bind` / `Execute`, como lo usan `psycopg` 3, `pgx` y JDBC) se vinculan en el servidor como literales de cadena.  A partir de `v0.12.742`, un marcador de posición `$n` dentro de un literal de cadena, un identificador entre comillas o un comentario se deja intacto, las barras invertidas de los valores de parámetro se escapan y un valor de texto `NULL` sigue siendo la cadena `NULL` (el `NULL` de SQL se transmite por separado).

### Opciones del servidor {#server-options}

| Opción | Descripción | Valor predeterminado |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Dirección en la que escucha el servidor.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|Puerto TCP en el que escucha el servidor.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|Objeto de configuración de TLS en JSON. Consulte el [ejemplo de mTLS](#examples) siguiente.|ninguno|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|Habilita la salida de depuración de consultas para los clientes de PostgreSQL, por ejemplo, con `stackql srv --pgsrv.debug.enable=true`. Déjela deshabilitada a menos que los clientes deban recibir información de depuración.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|Asignación JSON o YAML avanzada que se pasa al servidor del protocolo de red de PostgreSQL solo cuando no se establece `--pgsrv.tls`.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Se acepta, pero no establece el nivel de log en el motor actual. Use `--loglevel` en su lugar.|`WARN`|

<br/>

> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales

### Opciones del servidor MCP {#mcp-server-options}

Cuando se ejecuta el comando `srv` con compatibilidad con MCP, están disponibles las siguientes opciones adicionales:

| Opción | Descripción |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Tipo de servidor MCP: `http` (en memoria) o `reverse_proxy` (basado en TCP)|
|<span class="nowrap">`--mcp.config`</span>|Objeto de configuración JSON para el servidor MCP (consulte la [documentación de MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Codificación del log de auditoría de MCP: `jsonl` (valor predeterminado) u `otel` (registros de log OTLP/JSON); tiene prioridad sobre `server.audit.format` en el objeto de configuración|
|<span class="nowrap">`--mcp.protocol.version`</span>|Revisión más reciente del protocolo MCP que se anuncia: `auto` (valor predeterminado), `2026-07-28` (solo sin sesión) o una revisión anterior como `2025-11-25`; tiene prioridad sobre `server.protocol_version` en el objeto de configuración|

<br/>

:::info[Cambio incompatible en v0.12.742]

A partir de `v0.12.742`, el transporte HTTP de MCP (`--mcp.server.type=http` o `reverse_proxy`) se niega a iniciar a menos que el objeto de configuración indique un token bearer con `server.auth_token_env_var` o renuncie explícitamente a la autenticación con `"allow_unauthenticated": true`.  Consulte [Autenticación de clientes HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Consulte la [documentación del comando MCP](/command-line-usage/mcp) para obtener información detallada sobre la configuración y el uso del servidor MCP, incluidos los modos de despliegue, las opciones de configuración y la integración con asistentes de IA.

:::

:::info

Debe establecer las variables de entorno necesarias para la autenticación de los providers antes de iniciar el servidor; para obtener más información, consulte [Usar un provider](/getting-started/using-a-provider).

:::

Para los servidores de larga duración, la opción [`--env.file`](/command-line-usage/global-flags) le permite colocar o rotar las credenciales en un archivo de estilo dotenv en disco antes del inicio, en lugar de exportar variables en el shell:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Para `stackql srv`, el archivo se lee una sola vez al iniciar; la vía para volver a cargarlo en mitad de la sesión es la herramienta MCP [`reload_credentials`](/mcp/reload_credentials).

### Opciones {#flags}

| Opción | Descripción |
|--|--|
|`-h,--help`|Muestra la información de ayuda|
|`-v,--verbose`|Ejecuta las consultas en modo detallado con salida adicional enviada a stdout; si se establece la opción global `-f` (`--outfile`),<br/>esta información de log adicional se escribe en el archivo de salida junto con los resultados de la consulta|

<br/>

> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales

* * *

### Ejemplos {#examples}

Iniciar un proceso de servidor de StackQL y conectarse a él con un cliente de línea de comandos `psql`.  

Primero, descargue el archivo de configuración `openssl.cnf` de [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

A continuación, genere los archivos de certificado y de clave que usarán el servidor y el cliente para la autenticación TLS mutua (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Ahora, establezca las variables de entorno; si el cliente y el servidor están en el mismo host, se pueden compartir.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Establezca las credenciales de los providers que necesita el servidor.  Para GitHub, el provider lee un nombre de usuario y un token de acceso personal de estas dos variables:  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Ahora, inicie el servidor en un terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

En otro terminal, conéctese al servidor con el cliente de línea de comandos `psql`.  

```bash
psql -h 127.0.0.1 -d stackql
```

Ahora, ejecute una consulta de StackQL en un provider autenticado.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
