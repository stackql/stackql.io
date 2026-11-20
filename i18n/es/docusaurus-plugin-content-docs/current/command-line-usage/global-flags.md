---
title: Opciones globales
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Opciones globales del binario stackql: autenticación, formato de salida, ubicación del registro, logs y configuración del backend SQL, disponibles en todos los comandos."
image: "/img/stackql-featured-image.png"
---

Los comandos de StackQL aceptan las opciones globales, pero algunas solo se aplican a un modo determinado. Establézcalas en la línea de comandos o, donde se admita, en el archivo de inicialización de StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Las opciones `--pgsrv.*` se enumeran en [Opciones del servidor PostgreSQL](/command-line-usage/srv#server-options).

| Opción | Tipo de datos | Descripción | Valor predeterminado |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Configuración de acceso de los llamadores, un sobre JSON `{"path": "/path/to/access.json"}` (también se acepta solo la ruta del archivo).  Habilita la [gestión de accesos](/command-line-usage/access-management): los llamadores se autentican mediante JWT, token bearer estático, TLS mutuo o identidad local del SO, y las instrucciones se autorizan según reglas de política.  Las credenciales de token locales se leen de `STACKQL_CALLER_TOKEN`.  Disponible a partir de StackQL `v1.0`. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Permite que una sesión seleccione un almacén de secretos persistente del sistema operativo (macOS Keychain, Linux Secret Service o Windows Credential Manager) con `SET "$.secret_store"`.  Los almacenes de archivo plano se rechazan incluso con la opción.  Consulte [Sesiones de provider y secretos](/getting-started/provider-sessions#persistent-os-secret-stores).  Disponible a partir de StackQL `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Permite desreferenciar los valores de secretos y credenciales en expresiones SQL (`"$secrets.name"`, `"$auth.provider.field"`), con la consecuencia de que pueden acabar en los resultados, los logs y los artefactos de consulta persistidos.  No es necesaria para la autenticación normal de los providers.  Consulte [Valores secretos en SQL](/getting-started/provider-sessions#secret-values-in-sql).  Disponible a partir de StackQL `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Tiempo de espera de las solicitudes a la API en segundos, 0 para no aplicar tiempo de espera | `45` |
| <span class="nowrap">`--approot`</span> | string | Ruta raíz de la configuración y la caché de la aplicación | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Modo de archivo de la configuración y la caché de la aplicación | `493` |
| <span class="nowrap">`--auth`</span> | string | Pares clave-valor del contexto `auth` en formato json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Número inicial de claves de la caché | `100` |
| <span class="nowrap">`--configfile`</span> | string | Ruta completa del archivo de configuración | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Archivo `cpuprofile`, ninguno si está vacío | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Profundidad máxima de dependencias del flujo de datos para una consulta determinada | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | Número máximo de componentes débilmente conexos del flujo de datos para una consulta determinada | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | Cadena JSON / YAML para configurar el tratamiento de las consultas de mantenimiento interno del SGBD | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Delimitador para la salida csv. Un solo carácter.  Se ignora para toda salida que no sea csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Opción `dryrun`; solo se ejecuta el preprocesador y se devuelve la salida generada a partir de la plantilla | `false` |
| <span class="nowrap">`--env.file`</span> | string | Archivo opcional de estilo dotenv que se carga en el entorno del proceso al iniciar (todos los comandos); la herramienta MCP [`reload_credentials`](/mcp/reload_credentials) lo vuelve a cargar bajo demanda. Se tolera que el archivo no exista; las claves con valores vacíos se omiten; las variables existentes se sobrescriben. No se carga desde `.stackqlrc`. Disponible en las versiones de StackQL posteriores a `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Límite de concurrencia para la ejecución de consultas | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Prefijo de alias de exportación (espacio de nombres o esquema) | |
| <span class="nowrap">`--gc`</span> | string | Cadena JSON / YAML que representa la configuración de GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Deshabilita los encabezados de columna, válido solo con `--output csv` o `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Muestra la información de las solicitudes http en el terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Host del proxy (déjelo en blanco para no usar proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Contraseña del proxy | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Puerto del proxy; cualquier número menor o igual que 0 da como resultado el puerto predeterminado del esquema correspondiente (p. ej., `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Esquema del proxy, `http` o `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Usuario del proxy | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Número máximo de resultados por solicitud http; cualquier número menor o igual que 0 implica que no hay límite | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Número máximo de páginas de resultados que se devuelven por recurso; cualquier número menor o igual que 0 implica que no hay límite | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Profundidad máxima de las consultas indirectas: vistas y subconsultas | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Archivo de entrada (archivo IQL) del que se leen las consultas | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Archivo de contexto (datos) para el uso de plantillas (archivo `json` o `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Nivel de log, debe ser uno de {`info`, `warn`, `debug`, `fatal`} | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Objeto de configuración JSON / YAML para el [servidor MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` y `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Codificación del log de auditoría de MCP, `jsonl` u `otel` (registros de log OTLP/JSON); tiene prioridad sobre `server.audit.format` en `--mcp.config`.  Consulte [Formato del log](/command-line-usage/mcp#log-format).  Disponible a partir de StackQL `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Revisión más reciente del protocolo MCP que se anuncia, `auto`, `2026-07-28` (solo sin sesión) o una revisión anterior como `2025-11-25`; tiene prioridad sobre `server.protocol_version` en `--mcp.config`.  Consulte [Fijar la revisión](/command-line-usage/mcp#pinning-the-revision).  Disponible a partir de StackQL `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Tipo de servidor MCP, uno de {`http`, `stdio`, `reverse_proxy`}.  Consulte [Tipo de servidor MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL de los documentos de metadatos en caché, en segundos | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | Cadena JSON / YAML que representa los espacios de nombres para la caché, las vistas, etc. | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Trabaja sin conexión, con datos en caché | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Archivo de salida en el que se escriben los resultados | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Formato de salida, debe ser uno de {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} (`ndjson` se acepta como alias de `jsonl`).  `otel` emite registros de log de OpenTelemetry, consulte [Modos de salida](/getting-started/output-modes#otel-output); disponible a partir de StackQL `v0.11.660`.  Con un exportador configurado en `--otel.config`, los registros `otel` también se envían a ese endpoint | `table` |
| <span class="nowrap">`--otel.config`</span> | string | Cadena JSON / YAML que configura el formato de salida `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` y `exporter.batch_size` envían los registros a un endpoint de logs OTLP/HTTP, consulte [Enviar a un endpoint OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Disponible a partir de StackQL `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | Cadena JSON que habilita la funcionalidad en versión preliminar.  `{"unstable": true}` expone las relaciones `stackql_unstable_<provider>.<service>.<resource>` basadas en documentos, que `omnisdk` resuelve y ejecuta directamente a partir de los documentos de provider, con las filas transmitidas a medida que se producen; las claves restantes (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) ajustan ese backend.  La funcionalidad en versión preliminar no es estable: el comportamiento, la sintaxis y la salida pueden cambiar o dejar de funcionar entre versiones sin previo aviso, así que no dependa de ella en producción.  A partir de `v1.0`, la misma activación explícita expone las relaciones `stackql_preview.iac.run` y `stackql_preview.iac.destroy`, que aplican y eliminan un conjunto de recursos declarado mediante `omnisdk`, con el estado guardado en la raíz de la aplicación.  Consulte [Funcionalidad en versión preliminar](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Provider predeterminado inicial. Por ejemplo, con `--provider github`, `repos.repos` se resuelve como `github.repos.repos` sin una instrucción `USE` previa. No realiza la autenticación en el provider. | ninguno |
| <span class="nowrap">`--querycachesize`</span> | integer | Tamaño, en número de entradas, de la caché LRU para los planes de consulta | `10000` |
| <span class="nowrap">`--registry`</span> | string | Cadena JSON / YAML que configura la ubicación del registro de providers y la verificación de firmas de los documentos de provider | consulte [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | Cadena JSON / YAML que representa la configuración de la sesión | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | Cadena JSON / YAML que representa la configuración del sistema de backend SQL | consulte [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | Cadena JSON / YAML que representa la configuración del almacén de transacciones | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Ruta del bundle de CA; si no se especifica, se usan los valores predeterminados del sistema |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Permite confiar en certificados no seguros (no recomendado) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Política JSON / YAML para tolerar explícitamente estados de respuesta HTTP en las lecturas de los providers. Consulte [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Variables externas proporcionadas como pares clave/valor para cargarlas en las consultas de StackQL mediante archivos de datos `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Salida detallada | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Ayuda contextual de stackql | |
| <span class="nowrap">`--version`</span> | | Muestra la versión del programa StackQL | |

La opción `--colorscheme` está en desuso y ya no cambia los colores del shell. La opción `--usenonpreferredapis` se acepta, pero no cambia la selección de API en el motor actual.

## `--registry` {#--registry}

La opción `--registry` configura de dónde se leen los documentos de provider y si se verifican sus firmas.  El valor es una cadena JSON con las siguientes claves:

| Clave | Descripción | Valor predeterminado |
| -- | -- | -- |
| `url` | URL del registro.  Si se omite, se usa el registro de providers público de StackQL. | registro público |
| `localDocRoot` | Un directorio local que contiene documentos de provider, que se leen en lugar de descargarlos del registro.  Se usa al [desarrollar un provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Prefijo de ruta bajo la raíz del registro donde se encuentran los documentos de origen de los providers. | `src` |
| `verifyConfig.nopVerify` | `true` omite la verificación de firmas.  Establézcalo en `false` para exigir una firma ML-DSA-65 (FIPS 204) integrada en cada documento de provider y de servicio que se lea, incluidas las relaciones en versión preliminar basadas en documentos. | `true` |
| `verifyConfig.publicKeys` | Un objeto JSON que asigna ID de clave a claves públicas ML-DSA-65 codificadas en hexadecimal, para registros privados.  Sustituye a las claves de producción integradas; un objeto vacío explícito es un error. | claves integradas |

### Ejemplo: exigir documentos de provider firmados {#example-require-signed-provider-documents}

Activa la verificación para cada documento de provider y de servicio que se lee del registro público, que se comprueba con las claves de producción integradas:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Ejemplo: leer documentos sin firmar desde un directorio local {#example-read-unsigned-documents-from-a-local-directory}

Apunta el registro a un directorio de documentos de provider en desarrollo.  Esos documentos no están firmados, por lo que la verificación permanece desactivada:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

La verificación de firmas está disponible a partir de StackQL `v1.0` y está desactivada de forma predeterminada.  Las firmas cubren los bytes YAML exactos de un documento, por lo que cualquier modificación, incluido un cambio de formato o de salto de línea, las invalida; vuelva a firmar con la herramienta `pqsig sign-doc` del registro.  Los documentos locales de desarrollo sin firmar deben mantener `nopVerify: true`.  Las antiguas opciones de certificado y los archivos sidecar `.sig` ya no se usan, y los archivos comprimidos publicados solo con sidecars deben volver a publicarse antes de usarlos con verificación.  La verificación autentica los documentos cuando se leen; la descarga y la extracción de los archivos comprimidos no están cubiertas.

:::

## `--sqlBackend` {#--sqlbackend}

La opción `--sqlBackend` configura un backend SQL externo para StackQL, lo que le permite usar un motor de base de datos externo (como PostgreSQL) en lugar del motor SQL integrado predeterminado. Esto resulta útil para aprovechar una base de datos externa para el procesamiento de consultas, el almacenamiento persistente o la integración con una infraestructura de bases de datos existente.

El valor es una cadena JSON con las siguientes claves:

| Clave | Descripción | Valores de ejemplo |
| -- | -- | -- |
| `dbEngine` | El tipo de motor de base de datos | `postgres_tcp` |
| `sqlDialect` | El dialecto SQL que se va a usar | `postgres` |
| `dsn` | El nombre del origen de datos (cadena de conexión) de la base de datos | Consulte los ejemplos siguientes |

### Ejemplo: usar un backend PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Ejemplo: usar la interfaz PostgreSQL de Databricks {#example-using-databricks-postgresql-interface}

Databricks expone una interfaz compatible con PostgreSQL que se puede usar como backend SQL de StackQL. El DSN usa el formato estándar de cadena de conexión de PostgreSQL con sus credenciales de Databricks:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Los caracteres especiales del nombre de usuario o de la contraseña (como `@`) deben codificarse como URL en el DSN (p. ej., `@` se convierte en `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

Use `http.ignoreStatusCodes` para enumerar los estados de respuesta HTTP que una lectura de provider debe tolerar explícitamente. Las entradas son cadenas: use un código exacto como `"503"` o una clase como `"5xx"`. El valor predeterminado `{}` no añade excepciones al tratamiento normal que el motor da a las respuestas de origen. Esta configuración no reintenta las solicitudes fallidas ni se aplica a las mutaciones ni a los intercambios de autenticación.

Una respuesta ignorada no aporta filas. Otras lecturas de la consulta pueden seguir devolviendo filas, por lo que una consulta correcta puede devolver resultados incompletos. StackQL informa del estado HTTP ignorado como un diagnóstico.

Por ejemplo, la [prueba simulada de join de GitHub](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104) usa esta opción:

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

En la prueba, la solicitud de colaboradores de un repositorio devuelve HTTP `500`. La consulta sigue devolviendo filas de colaboradores para los demás repositorios, y stderr incluye `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Prefiera un código de estado exacto cuando solo se espere un fallo.
