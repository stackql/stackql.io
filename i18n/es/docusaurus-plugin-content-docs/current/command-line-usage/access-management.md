---
title: Gestión de accesos
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestión de accesos
  - autenticación
  - autorización
  - jwt
  - mtls
  - token bearer
description: "Gestión de accesos de llamadores con activación explícita en StackQL: autenticar llamadores con JWT, tokens bearer, TLS mutuo o la identidad local del SO, asignarlos a principales y grupos de un directorio y autorizar instrucciones con reglas de política."
image: "/img/stackql-featured-image.png"
---

La gestión de accesos de llamadores autentica al llamador de una sesión de StackQL y autoriza cada instrucción conforme a una política.  Es independiente de las credenciales de los providers, que se siguen suministrando mediante `--auth`, variables de entorno o [sesiones de provider](/getting-started/provider-sessions).  Está deshabilitada de forma predeterminada, y los despliegues que no la habilitan se comportan como antes.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[ Opciones globales ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Habilitar la gestión de accesos {#enabling-access-management}

La gestión de accesos se activa al iniciar StackQL con la opción global `--access.config` apuntando a un archivo de configuración JSON.  El ejemplo inicia un shell interactivo en modo protegido; `exec` y `mcp` se habilitan de la misma manera.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` recibe un sobre JSON con una única clave `path`; también se acepta la ruta del archivo por sí sola.  La configuración se carga una sola vez al inicio, así que rote las claves o la política mediante un reinicio controlado.

Mientras la gestión de accesos está habilitada solo se ejecutan `exec`, `shell`, `mcp` y `version`; los demás comandos, incluido `registry`, se niegan a ejecutarse en lugar de eludir la ruta protegida.  `srv` y el tipo de servidor MCP `reverse_proxy` también se niegan a ejecutarse, porque la dependencia del protocolo de red de PostgreSQL aún no expone evidencia de autenticación por conexión.  Las instrucciones SQL `AUTH` y `AUTH REVOKE` se rechazan en modo protegido; en su lugar, aprovisione las credenciales de los providers mediante la configuración del host.  Un `exec` protegido que falla devuelve el estado de salida `1` después de presentar el error.

## Modos de autenticación {#authentication-modes}

El archivo de configuración selecciona un `type`: `jwt` (el valor predeterminado cuando se omite), `bearer`, `mtls` o `local_os`.  La configuración es JSON estricto: se rechazan los campos desconocidos y los campos que pertenecen a otro modo.  Todos los modos comparten las mismas `rules` y el mismo [directorio de principales](#principal-directory).

### JWT {#jwt}

El llamador presenta un token firmado, que se verifica sin conexión con las claves fijadas en la configuración.  Una configuración mínima indica el emisor y la audiencia que el token debe llevar, incluye una clave pública y concede al grupo `readers` acceso de lectura:

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

El emisor, la audiencia y las claves públicas se fijan en la configuración; no hay descubrimiento OIDC.  Los algoritmos aceptados son `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` y `ES512`, y las claves RSA deben tener al menos 2048 bits.  Cada clave está vinculada a su algoritmo configurado.  Se rechazan los tokens con un ID de clave desconocido, una firma no válida, una expiración ausente o pasada, un `nbf` o `iat` futuro, o un emisor o una audiencia incorrectos.  Los claims de grupo y las URL que lleva el token no se usan para la confianza ni para la pertenencia al directorio.

### Token bearer estático {#static-bearer-token}

El llamador presenta un token opaco, y la configuración enumera el hash SHA-256 de cada token aceptado junto con el sujeto que identifica.  Esto resulta adecuado para un conjunto pequeño y fijo de identidades de servicio en el que no se justifica operar un emisor de tokens:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Solo se configura el hash SHA-256 de cada token, y los hashes deben ser únicos.  Cambiar los hashes configurados requiere un reinicio.

### TLS mutuo {#mutual-tls}

El llamador se identifica por el certificado de cliente que presenta en el transporte MCP HTTPS.  La configuración contiene la CA que debe haber emitido el certificado e indica qué nombre alternativo del sujeto contiene la identidad:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

La cadena de certificados del cliente se verifica con `client_ca_pem` y el sujeto de la identidad se toma de exactamente un SAN del tipo configurado, `uri_san` o `dns_san`.  No se recurre al Common Name como alternativa.  El TLS mutuo solo está disponible en el transporte MCP HTTPS.

### Identidad local del SO {#local-os-identity}

No se intercambia ninguna credencial: el sujeto es la identidad del sistema operativo con la que se ejecuta el proceso de StackQL.  La configuración solo necesita una etiqueta `issuer` y las reglas:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

El sujeto de la identidad es el UID efectivo de Unix o el SID de Windows del proceso de StackQL, incluidos los UID numéricos de contenedor sin entrada en passwd.  No se presenta ninguna credencial y se ignora `STACKQL_CALLER_TOKEN`.  Este modo es solo local: se rechaza para los llamadores HTTP.  La pertenencia a grupos del sistema operativo no concede acceso; solo lo hace la pertenencia al directorio.

### Plazo de la sesión {#session-deadline}

De forma predeterminada, las consultas no tienen ningún plazo derivado de la autenticación.  Las credenciales se verifican y la pertenencia al directorio se actualiza con cada nueva instrucción, de modo que una credencial expirada no puede iniciar trabajo nuevo, pero la expiración no interrumpe una instrucción que ya está autorizada.  Establezca `"enforce_session_deadline": true` para cancelar al alcanzar `max_session_seconds` (valor predeterminado `300`, máximo `86400`), plazo que se acorta con la expiración del JWT o del certificado.  Esto no renueva los tokens.

## Directorio de principales {#principal-directory}

Las identidades se asignan a principales, y los principales a grupos, en cuatro tablas del backend SQLite o PostgreSQL configurado:

| Tabla | Contenido |
|--|--|
| `stackql_access_principals` | ID del principal y un objeto JSON de atributos de cadena.  Un atributo `tenant` sirve de espacio de nombres para el estado de IaC en versión preliminar. |
| `stackql_access_identities` | Del par `(issuer, subject)` verificado al ID del principal. |
| `stackql_access_groups` | ID de los grupos. |
| `stackql_access_memberships` | Vinculaciones de principal a grupo. |

Aprovisione las tablas, las identidades y las pertenencias mediante una conexión de administración de base de datos de confianza antes de atender consultas protegidas:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Use el emisor y el sujeto reales del modo: para la identidad local del SO podría ser `('this-host', '1000')`, y para TLS mutuo `('client-certificates', 'spiffe://example/alice')`.  Seleccione el backend de forma explícita:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

El directorio produce una instantánea inmutable para cada instrucción.  La revocación es un `DELETE FROM stackql_access_memberships` en la conexión de administración; la siguiente instrucción ve la nueva instantánea.  Las identidades desconocidas, un esquema ausente, los datos con formato incorrecto y las interrupciones del directorio deniegan el acceso en todos los casos.  El SQL protegido ordinario no puede acceder a las tablas del directorio.  Mantenga las escrituras de pertenencias en la conexión de administración, proteja el archivo de la base de datos y use privilegios de base de datos distintos para la administración y para el runtime cuando el backend los admita.

## Reglas de política {#policy-rules}

Cada regla concede una `action` sobre un `resource` a un `subject` con la forma `group:<group id>`.  Use los identificadores compilados de métodos y operaciones de documentos de provider de confianza; un comodín de operación concede los métodos de mutación además de las lecturas.

El grupo `readers` tiene de forma implícita la capacidad SQL de lectura segura del host.  Las lecturas de principales ajenos a ese grupo requieren `host/sql/read`, y las demás instrucciones requieren `execute` sobre `host/sql/<statement kind>`.  Las operaciones de provider y el uso de credenciales siempre requieren reglas explícitas, y una concesión sobre una colección no concede sus métodos subyacentes.  Comprobaciones adicionales del host:

| Recurso | Acción | Ámbito |
|--|--|--|
| `host/sql/read` | `execute` | Instrucciones de lectura fuera del grupo implícito de lectores |
| `host/sql/exec` | `execute` | `EXEC` nativo, incluido el `EXEC` anidado |
| `host/iac/run`, `host/iac/destroy` | `execute` | Las relaciones de IaC en versión preliminar, a pesar de su sintaxis `SELECT` |
| `host/function/<name>` | `execute` | Funciones fuera de la pequeña lista de permitidos de funciones que se sabe que son puras |
| `host/backend` | `read` | Relaciones opacas almacenadas y de backend externo |
| `host/credentials` | `reload` | La herramienta MCP `reload_credentials` |
| `session/variables` | `read` | Desreferenciar [variables de sesión](/getting-started/session-variables) y secretos |
| `session/config` | `write` | `SET` y `UNSET` |

La expansión de consultas almacenadas se comprueba antes de la ejecución, y la reutilización de la caché de planes y de filas está deshabilitada en las rutas protegidas.

## Puntos de entrada {#entry-points}

| Autenticación | `exec`, `shell`, MCP stdio | MCP HTTPS |
|--|--|--|
| JWT | Variable de entorno `STACKQL_CALLER_TOKEN` | Encabezado de solicitud `Authorization: Bearer` |
| Bearer estático | Variable de entorno `STACKQL_CALLER_TOKEN` | Encabezado de solicitud `Authorization: Bearer` |
| SO local | UID o SID efectivo del proceso; sin credencial | Rechazado |
| TLS mutuo | Rechazado: no hay conexión TLS verificada | Cadena de cliente verificada y un SAN configurado |

Suministre las credenciales de token locales mediante el entorno del proceso, nunca como un literal SQL ni como un argumento de la línea de comandos.  Sobre HTTPS, el verificador de llamadores sustituye al control compartido por token bearer `auth_token_env_var` descrito en [Autenticación de clientes HTTP](/command-line-usage/mcp#http-client-authentication); la configuración del certificado y la clave TLS del servidor es obligatoria, y una solicitud sin credenciales nunca recurre al token de entorno del servidor.

## Inspeccionar al llamador {#inspecting-the-caller}

En una sesión autenticada, `whoami()` devuelve el ID del principal y `groups()` devuelve los grupos del directorio como un array JSON:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Ambas funciones requieren una sesión iniciada con `--access.config`.

## Guías paso a paso {#walkthroughs}

El repositorio de StackQL incluye [cuatro guías paso a paso ejecutables](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), una por modo de autenticación, dirigidas por un script de Python que aprovisiona un directorio de demostración y verifica una consulta.  No necesitan ninguna cuenta en la nube y las ejecuta el conjunto de pruebas del proyecto.
