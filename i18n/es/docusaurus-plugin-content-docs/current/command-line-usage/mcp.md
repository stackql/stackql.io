---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El comando stackql mcp ejecuta StackQL como servidor Model Context Protocol para agentes de IA: modos de despliegue y de seguridad, recarga de credenciales y log de auditoría."
image: "/img/stackql-featured-image.png"
---

Comando que se usa para iniciar StackQL como servidor Model Context Protocol (MCP), lo que permite a los agentes y asistentes de IA interactuar con la infraestructura en la nube mediante las capacidades de consulta de StackQL.

:::info[¿Qué es MCP?]

El [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) es un protocolo abierto que estandariza la forma en que las aplicaciones de IA interactúan con fuentes de datos y herramientas externas.  Al ejecutar StackQL como servidor MCP, puede permitir que los agentes de IA (Claude, Cursor, Continue, agentes personalizados, etc.) consulten y gestionen infraestructura en la nube en varios providers mediante lenguaje natural.

:::

* * *

### Sintaxis {#syntax}

`stackql mcp [flags]` para un servidor MCP independiente.

`stackql srv [flags]` para ejecutar el servidor MCP junto con el servidor del protocolo de red de PostgreSQL en el mismo proceso.

* * *

### Modos de despliegue {#deployment-modes}

El servidor MCP de StackQL se puede desplegar en tres configuraciones distintas para adaptarse a diferentes requisitos de arquitectura.  Los ejemplos siguientes activan explícitamente el servicio sin autenticación con `"allow_unauthenticated": true`, lo que resulta adecuado para el desarrollo local; para cualquier servidor al que pueda acceder otro proceso u otro host, configure en su lugar un token bearer (consulte [Autenticación de clientes HTTP](#http-client-authentication)).

#### 1. Servidor MCP independiente {#1-standalone-mcp-server}

Ejecute StackQL como servidor MCP dedicado en un puerto especificado.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Caso de uso:** cuando solo necesita acceso mediante el protocolo MCP y no requiere compatibilidad con el protocolo de red de PostgreSQL.

#### 2. Servidor MCP + PostgreSQL (en memoria) {#2-mcp--postgresql-server-in-memory}

Ejecute los servidores MCP y PostgreSQL simultáneamente, con comunicación en memoria entre ambos.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Caso de uso:** cuando necesita tanto acceso mediante el protocolo MCP para agentes de IA como el protocolo de red de PostgreSQL para clientes de bases de datos tradicionales, con el máximo rendimiento gracias a la comunicación en memoria.

#### 3. Servidor MCP + PostgreSQL (proxy inverso) {#3-mcp--postgresql-server-reverse-proxy}

Ejecute ambos servidores con comunicación basada en TCP, lo que admite despliegues distribuidos y cifrado TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Con cifrado TLS:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS cifra el transporte pero no autentica al cliente, por lo que este ejemplo también requiere el token bearer almacenado en `STACKQL_MCP_TOKEN`.

**Caso de uso:** cuando necesita separar las cargas de trabajo de MCP y PostgreSQL en procesos o hosts distintos, o cuando requiere cifrado TLS para el endpoint MCP.

* * *

### Opciones de configuración {#configuration-options}

#### Tipo de servidor MCP {#mcp-server-type}

| Tipo | Descripción |
|--|--|
|`http`|Modo de servidor HTTP directo - el motor de StackQL atiende las solicitudes MCP dentro del propio proceso.|
|`reverse_proxy`|Modo de proxy inverso - las solicitudes MCP se envían a un servidor PostgreSQL subyacente mediante el DSN configurado.  Se usa con `stackql srv`.|
|`stdio`|Transporte de entrada/salida estándar - el servidor lee las solicitudes de `stdin` y escribe las respuestas en `stdout`.  Lo usan los clientes MCP integrados en editores (Claude Desktop, Cursor, etc.).|

#### Formato del log de MCP {#mcp-log-format}

La opción `--mcp.log.format` selecciona la codificación del log de auditoría: `jsonl` (valor predeterminado) u `otel` (registros de log OTLP/JSON de OpenTelemetry).  Tiene prioridad sobre `server.audit.format` en el objeto de configuración.  Consulte [Formato del log](#log-format).

#### Versión del protocolo MCP {#mcp-protocol-version}

La opción `--mcp.protocol.version` establece la revisión del protocolo más reciente que anuncia el servidor: `auto` (valor predeterminado, todas las revisiones admitidas), `2026-07-28` (solo sin sesión) o una revisión anterior como `2025-11-25` (esa revisión y las anteriores).  Tiene prioridad sobre `server.protocol_version` en el objeto de configuración.  Consulte [Fijar la revisión](#pinning-the-revision).

#### Objeto de configuración de MCP {#mcp-configuration-object}

La opción `--mcp.config` acepta un objeto JSON con la siguiente estructura.

##### Configuración del servidor {#server-configuration}

| Campo | Descripción | Obligatorio |
|--|--|--|
|`server.transport`|Protocolo de transporte - `http` o `stdio`.|Sí|
|`server.address`|Dirección y puerto a los que se vincula el servidor MCP (p. ej., `127.0.0.1:9912`).  Solo para el transporte HTTP.|Para `http`|
|`server.tls_cert_file`|Ruta del archivo de certificado TLS para HTTPS.|No|
|`server.tls_key_file`|Ruta del archivo de clave privada TLS para HTTPS.|No|
|`server.auth_token_env_var`|Nombre de una variable de entorno que contiene un token bearer precompartido.  Cuando se establece, toda solicitud HTTP debe incluir `Authorization: Bearer <token>`; el servidor se niega a iniciar si la variable no está definida o está vacía.  Consulte [Autenticación de clientes HTTP](#http-client-authentication).|Este campo o `allow_unauthenticated`, para `http`|
|`server.allow_unauthenticated`|Activación explícita para servir el transporte HTTP sin autenticación de clientes.  Valor predeterminado: `false`.|Este campo o `auth_token_env_var`, para `http`|
|`server.mode`|Contrato de seguridad que controla las mutaciones y las operaciones de ciclo de vida.  Uno de `read_only`, `safe` (valor predeterminado), `delete_safe`, `full_access`.  Consulte [Modos del servidor](#server-modes).|No|
|`server.read_only`|Alias booleano **heredado** de `mode: read_only`.  Cuando se establecen ambos campos, prevalece `mode`.|No|
|`server.stateless`|Sirve Streamable HTTP sin sesiones (sin `Mcp-Session-Id`), que es como se sirve la revisión `2026-07-28` del protocolo por HTTP.  Valor predeterminado: `false`.  Se ignora para `stdio`.  Consulte [Compatibilidad con revisiones del protocolo](#protocol-revision-support).|No|
|`server.protocol_version`|Revisión del protocolo más reciente que se anuncia: `auto` (valor predeterminado), `2026-07-28` o una revisión anterior como `2025-11-25`.  `2026-07-28` implica `stateless` para Streamable HTTP.  Consulte [Fijar la revisión](#pinning-the-revision).|No|
|`server.audit`|Configuración del subsistema de auditoría, incluido `audit.format` (`jsonl` u `otel`).  Consulte [Log de auditoría](#audit-log).|No|

##### Configuración del backend (solo en modo de proxy inverso) {#backend-configuration-reverse-proxy-mode-only}

| Campo | Descripción | Obligatorio |
|--|--|--|
|`backend.dsn`|Cadena de conexión de PostgreSQL para el servidor StackQL de backend.|Sí (para `reverse_proxy`)|

<br />

:::info

El DSN del backend debe incluir el parámetro `default_query_exec_mode=simple_protocol` para una compatibilidad óptima.

:::

##### Listas de permitidos {#allowlists}

| Campo | Descripción |
|--|--|
|`enabled_tools`|Array de nombres de herramientas que el servidor tiene permitido publicar.  Cuando se omite o está vacío, se registran todas las herramientas incorporadas.  Se usa para exponer un subconjunto reducido (p. ej., un servidor de inventario de solo lectura).|
|`enabled_prompts`|Array de nombres de prompts.  La misma semántica que `enabled_tools`, pero para la superficie de prompts.|

<br/>

Ejemplo - un servidor que publica solo `server_info` y `list_providers`:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Compatibilidad con revisiones del protocolo {#protocol-revision-support}

La revisión `2026-07-28` del protocolo, `server.stateless` y el formato `otel` del log de auditoría están disponibles a partir de StackQL `v0.11.660`.  `server.protocol_version` y la opción `--mcp.protocol.version` están disponibles a partir de StackQL `v0.12.732`.

El servidor habla todas las revisiones del Model Context Protocol admitidas por el [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) y negocia con cada cliente, de modo que una flota de clientes heterogéneos funciona con un solo servidor.

| Revisión | Ciclo de vida | Solicitud de aprobación (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (anunciada)|Sin handshake: `server/discover` y, después, cada solicitud lleva `io.modelcontextprotocol/protocolVersion` e `io.modelcontextprotocol/clientCapabilities` en `_meta` (además de los encabezados `Mcp-Protocol-Version`, `Mcp-Method` y `Mcp-Name` en HTTP).|Resultado `input_required`, que el cliente reintenta con `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|Handshake `initialize` / `notifications/initialized`.|Solicitud `elicitation/create` iniciada por el servidor.|
|`2025-03-26`, `2024-11-05`|Igual que arriba.|Igual que arriba.|

<br />

**stdio** sirve todas las revisiones en un solo proceso: la primera solicitud de un cliente `2026-07-28` se sirve sin handshake y el `initialize` de un cliente anterior sigue funcionando.  No hay nada que configurar para Claude Desktop, los lanzadores de npm / PyPI ni la imagen de Docker.

**Streamable HTTP** usa de forma predeterminada el modelo con estado, de una sesión por cliente (`Mcp-Session-Id`), que el SDK sirve para las revisiones hasta `2025-11-25`.  Una solicitud `2026-07-28` se responde con el error JSON-RPC `-32022` (versión del protocolo no admitida; un HTTP 400 simple en las versiones hasta `v0.12.718`) cuyo `data.supported` enumera las revisiones con handshake, y `server/discover` anuncia lo mismo, de modo que el cliente negocia una revisión inferior y las integraciones HTTP existentes conservan sus sesiones y sus solicitudes de aprobación sin cambios.  Establezca `server.stateless` (o fije la revisión en `2026-07-28`, [más abajo](#pinning-the-revision)) para servir `2026-07-28` de forma nativa por HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Un servidor sin sesión no emite ningún `Mcp-Session-Id`, responde a `GET` y `DELETE` con 405 (no hay ninguna sesión que cerrar), mantiene `tools/list`, `prompts/list` y `resources/list` invariantes respecto a la conexión y realiza el ciclo de aprobación mediante `input_required`.  Sigue aceptando el `initialize` de un cliente anterior y le sirve lecturas, pero no puede conservar la capacidad de elicitación que ese cliente declaró en la inicialización (cada solicitud obtiene una sesión efímera), por lo que los clientes anteriores no pueden aprobar escrituras sujetas a aprobación en un servidor sin sesión.  Elija `stateless` para hosts de la revisión actual y el valor predeterminado para una flota que todavía incluya clientes anteriores.

El servidor no mantiene ningún estado entre llamadas: el modo, la auditoría y las credenciales de los providers son configuración a nivel de proceso, por lo que nada tiene que trasladarse a identificadores explícitos.

#### Fijar la revisión {#pinning-the-revision}

De forma predeterminada, el servidor anuncia todas las revisiones admitidas y negocia con cada cliente.  La opción `--mcp.protocol.version`, o `server.protocol_version` en el objeto de configuración (prevalece la opción), establece la revisión más reciente que se anuncia:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Valor | Anunciado | Efecto |
|--|--|--|
|`auto` (valor predeterminado) o ausente|Todas las revisiones admitidas.|Negociación como se describe arriba: la revisión más alta que hablan ambas partes.|
|`2026-07-28`|Solo `2026-07-28`.|Solo sin sesión, y `stateless` queda implícito para Streamable HTTP.  El `initialize` de un cliente anterior se responde con `2025-11-25`, la señal para que ese cliente se desconecte en lugar de interpretar la respuesta como el nuevo ciclo de vida.|
|`2025-11-25` (o cualquier revisión anterior)|Esa revisión y todas las anteriores.|Solo ciclo de vida con handshake: una solicitud `2026-07-28` recibe el error JSON-RPC `-32022` con `data.supported`, que enumera las revisiones anunciadas, para que el cliente pueda renegociar.  Los clientes anteriores con handshake conservan su propia revisión.|

<br />

Cualquier otro valor no supera la validación de la configuración al iniciar, y el error indica los valores válidos.

#### Límites del transporte {#transport-limits}

Los cuerpos de las solicitudes de Streamable HTTP están limitados a 4 MiB (HTTP 413 por encima de ese tamaño), el JSON anidado a más de 1000 niveles de profundidad se rechaza antes de analizarlo y una sola trama de `stdio` está limitada a 8 MiB.  Ninguno de estos límites es configurable; todos están muy por encima de cualquier instrucción SQL que acepte el servidor.

* * *

### (Re)carga de credenciales (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` y la herramienta `reload_credentials` están disponibles en las versiones de StackQL posteriores a `v0.10.542`.

:::

El bloque de entorno de un proceso queda determinado en el momento de su creación.  Esto plantea dos problemas para los servidores MCP:

- Un servidor MCP `stdio` iniciado por un cliente de escritorio (Claude Desktop, Cursor, etc.) sin las variables de entorno de credenciales nunca puede verlas - el cliente pasa su propio entorno al subproceso.
- Las credenciales de corta duración que se rotan nunca llegan a una sesión de servidor de larga duración.

La opción `--env.file` resuelve ambos problemas al designar un archivo de estilo dotenv que actúa como almacén de credenciales mutable:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Semántica de la carga {#sourcing-semantics}

El archivo se carga en el entorno del proceso al iniciar, para todos los puntos de entrada (`exec`, `shell`, `srv`, `registry`, `mcp`):

- Cuando la opción está vacía o no se establece, no ocurre nada - las credenciales se resuelven de forma diferida desde el entorno del proceso en el momento de la solicitud, exactamente el comportamiento histórico.
- Si no existe ningún archivo en la ruta indicada, se tolera y no ocurre nada (el archivo puede crearse más tarde).
- Un archivo mal formado o ilegible es un error fatal al iniciar.
- Solo se establecen las claves con valores no vacíos; las variables de entorno del proceso existentes **sí** se sobrescriben (el archivo es la fuente de verdad); nunca se elimina ninguna variable.
- La opción solo se admite en la línea de comandos - no se lee de `.stackqlrc`.

El formato del archivo es un `KEY=VALUE` por línea.  Se toleran los comentarios con `#`, las líneas en blanco, un prefijo `export ` opcional, comillas simples o dobles opcionales alrededor del valor y los finales de línea CRLF:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

La herramienta [`reload_credentials`](/mcp/reload_credentials) vuelve a cargar el archivo de `--env.file` en el entorno del proceso en mitad de la sesión y, a continuación, informa del estado de resolución de las credenciales por provider.  Los valores de los secretos nunca se devuelven, se escriben en logs ni se auditan - solo nombres y estados.  Sin `--env.file` configurado, la herramienta se reduce a un mero sondeo de estado.

Cuando una consulta falla en la resolución de credenciales, el error MCP incluye una indicación que dirige al agente a llamar a `reload_credentials` y reintentar, de modo que los clientes agénticos se recuperan por sí solos: escriba la credencial en el archivo, pida al agente que recargue y la consulta fallida se completa correctamente al reintentarla - sin necesidad de reiniciar el servidor.

`reload_credentials` no se admite en el modo de backend `reverse_proxy` - las consultas se ejecutan en el proceso del servidor stackql remoto, por lo que la herramienta devuelve un error que indica al usuario que recargue en el servidor subyacente.

#### Windows y Claude Desktop {#windows-and-claude-desktop}

`setx` escribe en el registro, no en los procesos en ejecución, y Claude Desktop pasa su propio entorno (obsoleto) a los subprocesos MCP - por eso los cambios de credenciales normalmente requieren reiniciar Claude Desktop por completo.  Con `--env.file`, el archivo de credenciales se puede crear o rotar en cualquier momento y `reload_credentials` lo recoge sin ningún reinicio.  Consulte [Usar StackQL con Claude Desktop](/getting-started/claude-desktop) para ver una configuración completa de ejemplo.

* * *

### Autenticación de clientes HTTP {#http-client-authentication}

:::note

`server.auth_token_env_var` y `server.allow_unauthenticated` están disponibles a partir de StackQL `v0.12.742`.

:::

El transporte HTTP requiere autenticación de clientes.  `server.auth_token_env_var` indica el nombre de una variable de entorno que contiene un token bearer precompartido; cada solicitud debe incluir `Authorization: Bearer <token>` o se responde con `401`.  El token se lee del entorno, por lo que nunca aparece en `--mcp.config` ni en los argumentos del proceso.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Autenticación del servidor obligatoria]

Servir sin token requiere una activación explícita: establezca `"allow_unauthenticated": true` en `server`.  Si no se indica ninguna de las dos claves, el servidor termina al iniciar con un mensaje que nombra ambas; también termina si la variable del token no está definida o está vacía.  A partir de la versión `v0.12.742`, debe agregar `auth_token_env_var` o `"allow_unauthenticated": true` a `server` cuando use el transporte `http`.

:::

TLS (`tls_cert_file` / `tls_key_file`) cifra el transporte pero no autentica al cliente; combínelo con un token para cualquier listener que no sea de loopback.  Las solicitudes de navegador entre orígenes (un `Origin` que no coincide o un `Sec-Fetch-Site` entre sitios) se rechazan con `403` independientemente de la autenticación.  El cliente `stackql_mcp_client` incluido no envía ningún encabezado `Authorization`, por lo que un servidor al que se conecte debe activar explícitamente el servicio sin autenticación.

A partir de `v1.0`, iniciar el servidor con [`--access.config`](/command-line-usage/access-management) sustituye el control por token compartido por la autenticación por llamador: cada solicitud HTTPS se verifica con el esquema configurado (JWT, bearer estático o TLS mutuo) y se autoriza según las reglas de la política.  En ese caso el TLS del servidor es obligatorio, la autenticación `local_os` se rechaza por HTTP y una solicitud sin credenciales nunca recurre al token del entorno del servidor.  La gestión de accesos no está disponible con el tipo de servidor `reverse_proxy`.

* * *

### Modos del servidor {#server-modes}

`server.mode` elige uno de cuatro contratos de seguridad.  Los cuatro permiten `SELECT` y las lecturas de metadatos; se diferencian en cómo tratan las mutaciones y las operaciones de ciclo de vida.

| Modo | `SELECT` / metadatos | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (ciclo de vida) |
|--|--|--|--|--|
|`read_only`|permitir|rechazar|rechazar|rechazar|
|`safe` (valor predeterminado)|permitir|requiere aprobación|requiere aprobación|requiere aprobación|
|`delete_safe`|permitir|permitir|requiere aprobación|requiere aprobación|
|`full_access`|permitir|permitir|permitir|permitir|

<br />

**Rechazar** devuelve un error al cliente de inmediato.

**Requiere aprobación** usa el flujo de elicitación de MCP, cuya forma depende de la [revisión del protocolo](#protocol-revision-support) negociada:

- Si el cliente anunció la capacidad de elicitación (en la inicialización, o en las capacidades del cliente de `_meta` de cada solicitud en `2026-07-28`), el servidor pide al usuario que apruebe la acción con un mensaje breve (nombre de la herramienta, clase de consulta, SQL).  En `2026-07-28` la solicitud de aprobación se devuelve como un resultado `input_required` que lleva una solicitud de entrada `elicitation/create` con el id `stackql_approval`, y el cliente reintenta la llamada con la respuesta en `inputResponses`; en las revisiones anteriores es el propio servidor quien envía la solicitud `elicitation/create` en mitad de la llamada.  El usuario acepta, rechaza o cancela.
- Si el cliente **no** anunció la elicitación, la herramienta se rechaza con un mensaje que explica la carencia y remite al operador al modo `full_access`.

La aprobación la responde el cliente, por lo que protege frente a un agente que actúe sin su usuario, no frente a un llamador que no sea de confianza.  Combine el transporte HTTP con la [autenticación de clientes](#http-client-authentication) cuando los llamadores no sean de confianza.

El modo es global para cada servidor.  No se puede anular por herramienta.

A partir de `v1.0`, [`run_select_query`](/mcp/run_select_query) rechaza `INSERT`, `UPDATE`, `REPLACE`, `DELETE` y `EXEC` en todos los modos, incluido `full_access`; el error indica `run_mutation_query` o `run_lifecycle_operation`.  Cada herramienta lleva además un `title` y las cuatro indicaciones de comportamiento de MCP como booleanos explícitos (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), derivadas de la misma clasificación que aplica el control de políticas.  `openWorldHint` es `true` para `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query` y `run_lifecycle_operation`, que acceden a endpoints del registro o de los providers, y `false` para las herramientas que solo leen el estado local.  Las indicaciones son orientativas para la experiencia de usuario del cliente; la aplicación sigue correspondiendo al control de políticas.

:::info[Control de la carga útil]

A partir de la versión `v0.12.742`, una carga útil que contiene varias instrucciones queda controlada por su instrucción con más privilegios en todos los modos, de modo que `SELECT 1; DELETE ...` se trata como un `DELETE` y el registro de auditoría lleva `query_class` `mutation_delete`.  `validate_select_query` acepta exactamente una instrucción.

:::

#### Cambio de comportamiento del modo predeterminado {#default-mode-behaviour-change}

Las versiones anteriores aplicaban la seguridad únicamente mediante una sola opción `read_only: true/false` cuyo valor predeterminado era "sin aplicación".  El valor predeterminado actual (`mode: safe`) hace que las mutaciones requieran la aprobación del usuario sin configuración adicional.  Los operadores que usen un cliente con capacidad de elicitación (Claude Desktop, Cursor, Continue, etc.) verán una solicitud de aprobación por mutación.  Los operadores que ejecuten automatización, o el cliente `stackql_mcp_client` incluido (que no anuncia la elicitación), deben activar explícitamente un modo menos restrictivo: `mode: delete_safe` para ejecutar `INSERT`, `UPDATE` y `REPLACE`, o `mode: full_access` para ejecutar además `DELETE` y operaciones de ciclo de vida.

La clave heredada `read_only: true` de JSON / YAML se sigue aceptando por compatibilidad con versiones anteriores y se trata como equivalente a `mode: read_only`.

#### Ejemplos {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE/REPLACE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Log de auditoría {#audit-log}

Cada llamada a una herramienta escribe un registro en el destino de auditoría configurado, como JSONL de forma predeterminada o como registros de log de OpenTelemetry (consulte [Formato del log](#log-format)).  La auditoría está **habilitada de forma predeterminada**.  La auditoría responde a "¿qué hizo el agente?", no a "¿qué vio el agente?" - las filas de resultados de las instrucciones `SELECT` no se registran de forma intencionada, en ninguno de los dos formatos.

#### Qué se registra {#what-gets-recorded}

| Campo | Descripción |
|--|--|
|`timestamp`|Hora de reloj al inicio de la llamada (RFC3339).|
|`tool`|Nombre de la herramienta (p. ej., `run_select_query`).|
|`mode`|Modo del servidor vigente en el momento de la llamada.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.  Para una carga útil que contiene varias instrucciones, la clase de la que tiene más privilegios.|
|`sql`|Cadena SQL para las herramientas de consulta (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Campos de jerarquía para las herramientas de metadatos (`list_*`, `describe_method`); SQL + `row_limit` para las herramientas de consulta.|
|`duration_ms`|Duración en tiempo de reloj del control + el manejador.|
|`error`|Mensaje de error si la herramienta produjo un error o fue rechazada.|

<br />

Las filas de resultados de las instrucciones `SELECT` se excluyen deliberadamente - pueden ser muy grandes y contener datos sensibles.

#### Destino de archivo {#file-sink}

El único destino que se incluye actualmente es `file`.  Un objeto JSON por línea, con fsync después de cada registro y rotación al estilo de lumberjack por tamaño / antigüedad / número de copias de seguridad.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Hay dos formas de especificar la ubicación:

- `audit.file.path` - una ruta de archivo completa (absoluta o relativa al cwd).
- `audit.file.dir` - un directorio; el destino elige dentro de él el nombre base `stackql_mcp_server_<RFC3339-utc-second>.log`.

Cuando no se establece ninguna de las dos en `mcp.config`, el servidor MCP usa el cwd como valor predeterminado de `dir`, de modo que los operadores existentes ven el mismo comportamiento que antes.  El propio paquete genérico de destinos subyacente se niega a elegir un directorio de forma silenciosa - la decisión de "dónde se escriben los logs" la toma siempre el llamador.

La ruta absoluta resuelta se escribe en stderr al iniciar como `sink file: /path/to/file.log`.

#### Formato del log {#log-format}

El flujo de auditoría tiene dos codificaciones, que se escriben en el mismo destino de archivo:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

La opción tiene prioridad sobre `server.audit.format` en `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` es compatible byte a byte con los registros descritos arriba.  `otel` escribe un objeto `LogsData` de OTLP/JSON por línea, la forma que el receptor `otlp_json_file` del OpenTelemetry Collector ingiere tal cual (verificado con otelcol-contrib 0.160.0), de modo que el flujo llega a cualquier pipeline de OTLP sin ningún procesador de transformación:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Cada llamada a una herramienta produce un registro de log; una llamada que pasó por el control de aprobación produce un segundo registro para la decisión de elicitación.  Los registros de una misma llamada comparten `gen_ai.tool.call.id` y un id de traza.  Se respeta un `traceparent` de W3C proporcionado por el llamador en `params._meta`; en caso contrario, se genera un id de traza por sesión MCP para que los registros de una sesión de agente se correlacionen.

El conjunto de atributos emitido es una interfaz versionada: el ámbito de instrumentación es `github.com/stackql/stackql/pkg/mcp_server/audit` en la versión `1.0.0`, y `schemaUrl` fija las convenciones estables en `https://opentelemetry.io/schemas/1.44.0`.  Los atributos `gen_ai.*` y `mcp.*` siguen las convenciones semánticas de [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) y [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), que están en estado Development; la versión del ámbito se incrementa cuando el conjunto cambia.

| Atributo | Registro | Valor |
|--|--|--|
|`service.name`, `service.version`|recurso|`stackql`, la versión de StackQL|
|`gen_ai.operation.name`|llamada a herramienta|`execute_tool`|
|`mcp.method.name`|ambos|`tools/call` en el registro de la llamada a la herramienta, `elicitation/create` en el registro de la decisión|
|`gen_ai.tool.name`|ambos|Nombre de la herramienta|
|`gen_ai.tool.call.id`|ambos|Se genera por llamada, compartido por ambos registros|
|`mcp.protocol.version`|ambos|Revisión negociada|
|`mcp.session.id`|ambos|Id de sesión (ausente en las solicitudes HTTP sin sesión)|
|`stackql.mode`|ambos|Modo del servidor|
|`stackql.decision`|ambos|El valor de `decision` de la tabla anterior|
|`stackql.query`|llamada a herramienta|SQL literal, para las herramientas de consulta|
|`stackql.query_class`|llamada a herramienta|El valor de `query_class`|
|`stackql.provider`|llamada a herramienta|Provider, para las herramientas de metadatos|
|`stackql.query.source`|llamada a herramienta|Id de la entrada de Query Library, cuando se proporciona|
|`stackql.duration_ms`|llamada a herramienta|Duración en tiempo de reloj|
|`stackql.rows_returned`|llamada a herramienta|Número de filas, para las herramientas que devuelven filas|
|`error.type`, `error.message`|llamada a herramienta|En caso de fallo: la decisión de rechazo, o `tool_error` una vez superado el control|

<br />

El cuerpo es `execute_tool <tool>` / `elicitation <tool>`; la gravedad es `INFO`, o `ERROR` cuando la llamada falló o fue rechazada.  El contrato de ocultación es idéntico en ambos formatos: los valores de los resultados nunca se serializan, solo la instrucción que los produjo.

#### Modos de fallo {#failure-modes}

Cuando el destino devuelve un error, el comportamiento de la respuesta depende de `audit.failure_mode`.

| failure_mode | Efecto |
|--|--|
|`strict` (valor predeterminado)|La llamada a la herramienta devuelve el error de auditoría al cliente aunque la herramienta subyacente se haya ejecutado correctamente.  Es intencionado: es mejor una respuesta ambigua para el cliente que un `DELETE` no detectado.|
|`strict_mutations`|Las lecturas de `SELECT` / metadatos continúan con una nota en stderr; las mutaciones y las operaciones de ciclo de vida muestran el error de auditoría.|
|`best_effort`|Siempre escribe en stderr y continúa.|

#### Secuencia {#sequencing}

La escritura de auditoría se produce **después** de que la herramienta se ejecute (o de que el control la rechace) pero **antes** de que la respuesta vuelva al cliente.  En modo estricto, un fallo de escritura de auditoría en un DELETE correcto significa que la fila ya no existe pero el cliente recibe un error - es así por diseño, para que ninguna mutación pase sin auditar.

#### Deshabilitar la auditoría {#disabling-audit}

Para deshabilitar la auditoría por completo, como en las versiones anteriores a `v0.10.474`, que no tenían auditoría:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Herramientas MCP disponibles {#available-mcp-tools}

Cuando se ejecuta como servidor MCP, StackQL expone las siguientes herramientas, sujetas a la lista de permitidos `enabled_tools` configurada. Cada una devuelve tanto una vista de texto renderizada (para el LLM) como una carga útil estructurada con tipos (para clientes programáticos). El renderizado de texto predeterminado es una tabla markdown para resultados uniformes de varias filas o un bloque KV de markdown para resultados dispersos / de un solo registro. Las herramientas con argumentos aceptan un `format` opcional (`markdown` o `json`) que anula ese valor; en caso contrario, se usa la configuración de renderizado del servidor. Las entradas siguientes enumeran los argumentos específicos de cada tarea; `?` marca las entradas opcionales.

Haga clic en el nombre de cualquier herramienta para ver una página de referencia completa, con las entradas, el comportamiento de control y un prompt de ejemplo.

| Herramienta | Renderizador | Descripción | Entradas |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Identidad y runtime del servidor: versión de stackql, motor SQL subyacente, ubicación del registro de providers, modo, indicador de solo lectura.  Llámela cuando la necesite, por ejemplo para diagnosticar una operación rechazada o sujeta a control o para comprobar un comportamiento que depende de la versión.|ninguna|
|[`list_providers`](/mcp/list_providers)|Tabla|Providers ya descargados en la caché local - la cima de la jerarquía.|ninguna|
|[`list_services`](/mcp/list_services)|Tabla|Servicios de un provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Tabla|Recursos de un `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Tabla|Métodos de acceso (operaciones HTTP) de un recurso, con el verbo SQL al que corresponde cada uno y sus parámetros obligatorios.  Llámela antes de escribir cualquier consulta - aquí es donde se deducen los parámetros `WHERE` obligatorios.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Contrato de E/S completo de un método (siempre EXTENDED): entradas obligatorias y opcionales, y los campos de salida a los que puede hacer referencia un `SELECT`.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Tabla|Busca plantillas de consulta seleccionadas a partir de una intención en lenguaje natural. De solo lectura; las entradas de mutación requieren activación explícita y se excluyen en el modo `read_only`.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Recupera una plantilla y sus declaraciones de parámetros. Con `params` no vacío, valida los valores y devuelve el SQL renderizado más `next_tool`, sin ejecutarlo.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Analiza y planifica un único `SELECT` sin ejecutarlo.  Devuelve `{valid, errors}`.  Solo `SELECT`; exactamente una instrucción.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Tabla|Ejecuta un `SELECT`.  Devuelve `{rows}`.  Solo lecturas.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|Ejecuta `INSERT`/`UPDATE`/`REPLACE`/`DELETE` en el provider.  **Efectos secundarios reales.** Devuelve `{messages, timestamp}`.  Controlada por el [modo](#server-modes) del servidor.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Ejecuta una operación de ciclo de vida `EXEC` de stackql.  Devuelve `{messages, timestamp}`.  Controlada por el [modo](#server-modes) del servidor.|`sql`|
|[`list_registry`](/mcp/list_registry)|Tabla|Providers (y sus versiones) disponibles en el registro configurado.  Distinta de `list_providers`, que enumera solo los providers ya descargados.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Instala un único provider del registro en la caché local.  Solo afecta al estado de la caché local - sin efecto en el plano de control ni en el plano de datos de la nube.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Tabla|Vuelve a cargar las credenciales del archivo dotenv de `--env.file` en el entorno del proceso e informa del estado de resolución por provider (`ok`, `unresolved`, `not_checked`).  Nunca devuelve valores de secretos.  Permitida en todos los modos.|`provider?`|

Consulte `query_library_search` antes de escribir SQL desde cero y, después, use `query_library_get` para inspeccionar y renderizar una plantilla que coincida. Las herramientas de consulta también aceptan un `source` opcional que contiene el ID de la entrada de la biblioteca solo con fines de atribución; la ejecución sigue sujeta al modo del servidor y a la política de aprobación habituales.

:::note

`describe_resource` se retiró en StackQL `v0.12.732`; los clientes MCP, los prompts o las instrucciones de agentes que la llamaban deben llamar en su lugar a [`list_methods`](/mcp/list_methods) seguida de `describe_method`.  Un recurso no tiene una única lista de campos, porque cada método de acceso devuelve su propia forma, de modo que los nombres de las columnas provienen de [`describe_method`](/mcp/describe_method) para el método al que se dirige una consulta.  La instrucción SQL [`DESCRIBE`](/language-spec/describe) no cambia.

:::

### Prompts MCP disponibles {#available-mcp-prompts}

Se publican siete prompts, sujetos a la lista de permitidos `enabled_prompts` configurada.  Cada uno es un workflow guiado que un agente ejecuta con las herramientas MCP de StackQL, y ninguno de ellos modifica recursos en la nube.

| Prompt | Descripción |
|--|--|
|`getting_started`|Recorrido guiado e interactivo por StackQL y el servidor MCP de StackQL para nuevos usuarios: conceptos, descubrimiento de providers, primeras consultas y por dónde continuar.|
|`cloud_audit`|Auditoría de seguridad y FinOps entre nubes (AWS, Google Cloud, Azure y Entra ID); devuelve un informe de hallazgos.|
|`cost_cleanup`|Revisión de limpieza de FinOps que encuentra recursos inactivos y huérfanos, estima el ahorro mensual y devuelve una lista priorizada de recursos que se pueden eliminar de forma segura.|
|`drift_report`|Informe de desviación que compara la infraestructura como código declarada (estado o plan de Terraform, o un inventario pegado) con los recursos en la nube en vivo; devuelve una tabla de desviaciones.|
|`iam_access_review`|Revisión de identidades y accesos en AWS IAM, Entra ID, Okta y GitHub; devuelve una revisión de accesos lista para auditores (al estilo de SOC 2 o ISO).|
|`public_exposure_scan`|Análisis de recursos accesibles desde Internet (almacenamiento público, grupos de seguridad abiertos, direcciones IP públicas, endpoints de bases de datos públicos, servicios de Kubernetes expuestos); devuelve una tabla de exposición priorizada.|
|`create_deploy_stack`|Guía al usuario en la generación de un stack de `stackql-deploy` (manifiesto y archivos de recursos `.iql`) con consultas probadas en vivo.  Las mutaciones se generan para `stackql-deploy`, nunca se ejecutan.|

* * *

### Opciones {#flags}

| Opción | Descripción |
|--|--|
|`--mcp.server.type`|Tipo de servidor MCP: `http`, `stdio` o `reverse_proxy` (este último se usa con `stackql srv`).|
|`--mcp.config`|Objeto de configuración JSON para el servidor MCP.  También se acepta YAML.|
|`--mcp.log.format`|Codificación del log de auditoría: `jsonl` (valor predeterminado) u `otel` (registros de log OTLP/JSON de OpenTelemetry).  Tiene prioridad sobre `server.audit.format`.  Consulte [Formato del log](#log-format).|
|`--mcp.protocol.version`|Revisión del protocolo más reciente que se anuncia: `auto` (valor predeterminado), `2026-07-28` o una revisión anterior como `2025-11-25`.  Consulte [Fijar la revisión](#pinning-the-revision).|
|`--env.file`|Archivo de credenciales de estilo dotenv que se carga al iniciar y que la herramienta `reload_credentials` vuelve a cargar bajo demanda.  Consulte [(Re)carga de credenciales](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|Puerto TCP del servidor del protocolo de red de PostgreSQL (se usa con `stackql srv`).|
|`-h`, `--help`|Muestra la información de ayuda.|
|`-v`, `--verbose`|Ejecuta en modo detallado con salida adicional.|

&nbsp;
&nbsp;
> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales

:::info

Debe establecer las variables de entorno necesarias para la autenticación de los providers antes de iniciar el servidor MCP, o designar un archivo de credenciales de estilo dotenv con [`--env.file`](#credential-resourcing---envfile--reload_credentials).  Para obtener más información, consulte [Usar un provider](/getting-started/using-a-provider).

:::

* * *

### Ejemplos {#examples-1}

#### Servidor MCP independiente básico {#basic-standalone-mcp-server}

Inicie un servidor MCP independiente con autenticación de providers y un token bearer para sus clientes:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor MCP con servidor PostgreSQL (en memoria) {#mcp-server-with-postgresql-server-in-memory}

Ejecute los servidores MCP y PostgreSQL para disponer de acceso con ambos protocolos:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor de inventario de solo lectura con auditoría en una ruta conocida {#read-only-inventory-server-with-audit-to-a-known-path}

Útil para un agente de exploración de inventario que nunca debe realizar mutaciones:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor de automatización con acceso completo {#full-access-automation-server}

Para pipelines de automatización de confianza que necesitan mutaciones y no pueden responder a solicitudes de elicitación.  El pipeline presenta el token bearer almacenado en `STACKQL_MCP_TOKEN`:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor MCP seguro con TLS {#secure-mcp-server-with-tls}

Inicie un servidor MCP con cifrado TLS en modo de proxy inverso:

Primero, genere los certificados TLS (autofirmados para desarrollo):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

A continuación, inicie el servidor:

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Probar el servidor MCP {#testing-your-mcp-server}

El repositorio incluye un cliente MCP de desarrollo (`stackql_mcp_client`) para scripts y pruebas de regresión.  Compílelo con:

```bash
python cicd/python/build.py --build-mcp-client
```

Esto genera `./build/stackql_mcp_client`.

Tenga en cuenta que `stackql_mcp_client` no envía ningún encabezado `Authorization`, por lo que el servidor al que se dirige debe establecer `"allow_unauthenticated": true` (como hacen los ejemplos anteriores); un servidor protegido con token le responde con `401`.  Además, **no** anuncia la capacidad de elicitación de MCP - está pensado para scripts no interactivos.  Con un servidor `safe` recibirá el rechazo "client does not support elicitation" en las llamadas de mutación y de ciclo de vida; con un servidor `delete_safe`, en las llamadas `DELETE` y `EXEC`.  Para pruebas interactivas con solicitudes de elicitación, use un cliente MCP integrado en un editor (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode and registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation (a DELETE) - succeeds only against a full_access server:
# stackql_mcp_client does not support elicitation, so safe and delete_safe
# servers refuse it.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Respuestas de ejemplo {#example-responses}

`server_info` (con un servidor en el modo predeterminado, StackQL `v1.0.888` en Windows sobre stdio):

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (con un servidor `full_access`):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Línea de ejemplo del log de auditoría para la misma mutación:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Integración con asistentes de IA {#integration-with-ai-assistants}

Para integrar el servidor MCP de StackQL con un asistente de IA, registre `stackql` como servidor MCP en la configuración del asistente.  La mayoría de los clientes MCP integrados en editores ejecutan el servidor por `stdio`; para ellos, use `--mcp.server.type=stdio` y el asistente inicia el proceso directamente.  Los agentes independientes que hablan HTTP pueden conectarse a un proceso `stackql mcp --mcp.server.type=http` de larga duración.

Para Claude Desktop - incluida la ficha del Anthropic Connector Directory (el método de instalación recomendado), el MCP Bundle descargable (`.mcpb`) y la configuración manual - consulte [Usar StackQL con Claude Desktop](/getting-started/claude-desktop).

:::note

Las ubicaciones de los archivos de configuración varían según el sistema operativo y el asistente de IA.  Consulte la documentación de su asistente de IA para conocer la ruta y el formato correctos.

:::

* * *

### Consideraciones de arquitectura {#architecture-considerations}

Al elegir un modo de despliegue, tenga en cuenta:

1. **Independiente (`stackql mcp`)**: la configuración más sencilla, ideal para el desarrollo y para clientes integrados en editores por `stdio`.
2. **Servidor dual en memoria (`stackql srv` con `mcp.server.type=http`)**: un solo proceso sirve tanto el protocolo MCP como el protocolo de red de PostgreSQL.  El mejor rendimiento, adecuado para la mayoría de los despliegues de producción.
3. **Proxy inverso (`stackql srv` con `mcp.server.type=reverse_proxy`)**:
   - permite separar las cargas de trabajo entre procesos o hosts
   - admite cifrado TLS para endpoints MCP seguros
   - permite escalar de forma independiente las interfaces MCP y PostgreSQL
   - ofrece flexibilidad para la integración con CA empresariales (nota: la compatibilidad con CA empresariales es experimental)

Y, por separado, en cuanto a la seguridad:

- Empiece con el valor predeterminado `mode: safe`.  Los clientes con capacidad de elicitación (Claude Desktop, Cursor, Continue) pedirán confirmación al usuario antes de cualquier mutación.
- Fije `mode: read_only` para agentes de inventario o de análisis que nunca deben escribir.
- Fije `mode: delete_safe` cuando quiera libertad para crear y actualizar pero desee la intervención de una persona en las operaciones destructivas.
- Reserve `mode: full_access` para pipelines de automatización de confianza, y solo junto con un log de auditoría que alguien revise.
- Establezca `server.auth_token_env_var` en cualquier listener HTTP al que puedan acceder otros procesos o hosts; `allow_unauthenticated` es para el desarrollo local.  TLS por sí solo no autentica a los clientes.

:::caution

Al usar TLS con autoridades de certificación (CA) empresariales, puede ser necesaria configuración adicional.  Esta funcionalidad es experimental y puede requerir ajustes en función de su implementación de CA específica.

:::

* * *

### Lecturas adicionales {#further-reading}

- La referencia para desarrolladores del paquete es el [README de `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) del repositorio de stackql.
- El documento técnico dirigido a colaboradores está en [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) en el repositorio de stackql e incluye respuestas de ejemplo completas para cada herramienta.
