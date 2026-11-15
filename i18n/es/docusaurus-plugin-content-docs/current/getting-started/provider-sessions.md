---
title: Sesiones de provider y secretos
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - sesiones de provider
  - multicuenta
  - credenciales
  - secretos
  - keychain
description: "Sesiones de provider en StackQL: vincular alias de provider con credenciales, tenancy y valores predeterminados propios, anular campos de credenciales por sesión y guardar secretos en memoria o en un almacén del SO."
image: "/img/stackql-featured-image.png"
---

Consulte también:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Variables de sesión ]](/getting-started/session-variables) [[ Opciones globales ]](/command-line-usage/global-flags)

Una sesión puede vincular un provider bajo un alias con sus propias credenciales, etiqueta de tenancy, valores predeterminados de parámetros y límites de concurrencia de solicitudes, de modo que se pueden consultar varias cuentas o tenants del mismo provider en una sesión, y en una instrucción.  Los campos de credenciales también se pueden anular para la sesión, y los secretos se pueden guardar en la memoria de la sesión o, con una activación explícita del operador, en el almacén de secretos del sistema operativo.  Todo esto se configura con [`SET`](/language-spec/set) y [`UNSET`](/language-spec/unset) y está disponible a partir de StackQL `v1.0`.

Todo lo descrito aquí tiene como ámbito la sesión actual.  Las conexiones independientes a un servidor `stackql srv` no comparten vinculaciones ni secretos, y cada solicitud MCP es su propia sesión, así que configure y consulte en un mismo lote de instrucciones cuando use el servidor MCP.

## Alias de provider {#provider-aliases}

`SET "$.providers.<alias>"` vincula un alias a un provider.  El valor es un documento JSON contenido en una cadena:

| Clave | Obligatorio | Descripción |
|--|--|--|
| `provider` | Sí | El provider canónico al que se resuelve el alias, por ejemplo `aws`. |
| `tenancy` | Sí | Una etiqueta de agrupación, como un nombre de cuenta o de tenant.  StackQL no la verifica con el provider. |
| `auth` | Uno de `auth` o `auth_secret` | Un documento de autenticación con la misma forma que la entrada del provider en el objeto [`--auth`](/command-line-usage/global-flags).  El alias no toma prestadas las credenciales del provider canónico. |
| `auth_secret` | Uno de `auth` o `auth_secret` | El nombre de un [secreto de sesión](#session-secrets) que contiene el documento de autenticación. |
| `params` | No | Valores predeterminados para los parámetros declarados del provider, incluidos los parámetros de servidor declarados.  Un predicado en la consulta anula un valor predeterminado. |
| `tuning` | No | Límites `max_concurrent` y `max_per_host` para las solicitudes del alias.  Los alias que comparten provider y tenancy comparten estos controles y deben declarar ajustes idénticos. |
<br/>
El siguiente ejemplo vincula dos cuentas de AWS con alias distintos, cada una de las cuales lee sus claves de variables de entorno con nombres diferentes, y consulta ambas en una instrucción:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Las referencias a variables de entorno se resuelven a partir del entorno del proceso de StackQL; no existe un entorno local de la sesión.  Las credenciales temporales de AWS también necesitan el campo del token de sesión o la referencia a su variable de entorno.

Un alias se comporta como su provider en las instrucciones de metadatos y de datos: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` y `EXEC` aceptan el alias en lugar del nombre del provider.  Los alias no se extienden a las relaciones nativas incorporadas ni a las relaciones de IaC en versión preliminar.  Las ramas de `UNION ALL` que usan alias distintos se ejecutan de forma secuencial.

Cada consulta toma una instantánea independiente de las credenciales del alias.  Cambiar una vinculación afecta a las consultas posteriores, no a una que ya esté en ejecución.  `UNSET "$.providers.<alias>"` elimina la vinculación.

## Anular campos de credenciales {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` cambia un campo del contexto de autenticación de un provider para la sesión; los nombres de campo son las claves de la entrada `--auth` del provider:

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

Como con los alias, el cambio se aplica a las consultas iniciadas después del `SET`.  Las consultas a un provider cuya autenticación se ha eliminado, o cuyo alias no tiene un `auth` utilizable, fallan antes de enviar ninguna solicitud.

## Secretos de sesión {#session-secrets}

De forma predeterminada, cada sesión tiene un almacén de secretos solo en memoria, sin necesidad de configuración.  `SET "$.secrets.<name>"` almacena una cadena literal, normalmente un documento JSON de autenticación, y un alias de provider hace referencia a ella con `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Rotar un secreto afecta a las consultas posteriores; eliminarlo hace que fallen los usos posteriores.  Una sesión nueva recibe un almacén nuevo y vacío, y al terminar la sesión este se libera.  `UNSET "$.secret_store"` descarta la vinculación del almacén actual y vuelve a empezar con la memoria de la sesión vacía.

El uso normal de las credenciales, y las instrucciones `SET` que almacenan variables o configuración, no escriben secretos en el backend SQL.  Guardar datos confidenciales en una variable o una tabla ordinaria es responsabilidad del llamador.

## Almacenes de secretos persistentes del SO {#persistent-os-secret-stores}

El almacenamiento persistente está deshabilitado de forma predeterminada y solo lo puede habilitar el operador iniciando StackQL con `--allow-persistent-secrets`; SQL no puede concederlo.  Con la opción definida, una sesión selecciona un almacén del SO con:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

El almacén es el Keychain de macOS, el Secret Service de Linux (`secret-tool` con un servicio de sesión operativo) o el Credential Manager de Windows.  Solo se acepta el backend `os`: los almacenes de archivo plano, las rutas de archivo y cualquier recurso implícito a un archivo se rechazan incluso con la opción definida, y un servicio del SO no disponible falla en lugar de recurrir a un archivo.

Los valores escritos en un almacén del SO sobreviven a la sesión SQL y se eliminan explícitamente con `UNSET "$.secrets.<name>"`.  Restablecer la vinculación del almacén no elimina los valores ya persistidos.  El acceso al almacén lo determina la identidad del sistema operativo del proceso de StackQL; un espacio de nombres no es un control de acceso por usuario, así que no trate esta activación explícita como un almacén de secretos multiusuario.

## Valores secretos en SQL {#secret-values-in-sql}

Los valores secretos nunca se desreferencian en SQL a menos que el operador inicie StackQL con `--allow-secret-dereference`.  Con esa opción, referencias como `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` y `"$providers.production.auth.credentials"` se resuelven en las expresiones:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Si se inicia sin la opción, la misma instrucción se rechaza antes de evaluar nada, como muestra la copia comentada.  La opción es un reconocimiento de que los valores desreferenciados explícitamente pueden llegar al almacenamiento intermedio de SQL, a los resultados, a los logs y a los artefactos de consulta persistidos.  La autenticación normal de los providers no la requiere.

## Permisos {#permissions}

Con la [gestión de accesos](/command-line-usage/access-management) habilitada, leer variables y secretos requiere `session/variables:read`, y `SET` o `UNSET` requiere `session/config:write`.  Un alias resuelto se autoriza como su provider canónico antes de la ejecución.  No se han implementado la propiedad ni las concesiones por variable y por secreto.
