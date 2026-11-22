---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de sesión
  - set
  - sesiones de provider
  - secretos
description: "SET en StackQL: asignar variables de sesión con tipo y configurar alias de provider, campos de credenciales y secretos de la sesión actual mediante destinos con prefijo de dólar."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Asigna una variable de sesión o configura ajustes de tiempo de ejecución para la sesión actual.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` UNSET `]](/language-spec/unset) [[ Variables de sesión ]](/getting-started/session-variables) [[ Sesiones de provider ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Sintaxis {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Un identificador simple (`region`), un nombre entre comillas dobles (`"providers.saved"`) o una ruta de configuración entre comillas dobles y con prefijo de dólar (`"$.auth.aws.password"`).

__*literal*__  
Una cadena, un número, un booleano o `null`.  Las cadenas van entre comillas simples.  Los arrays y los objetos no son valores escalares; guarde un documento JSON como una cadena.

__*DEFAULT*__  
Restablece el destino, equivalente a [`UNSET`](/language-spec/unset).  Un `null` explícito es un valor almacenado, no un restablecimiento.

__*reference*__  
Una referencia `"$name"` entre comillas dobles a una variable existente, por ejemplo `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Opcional, y el único ámbito que tiene StackQL.  `SET SESSION name = ...` y `SET name = ...` son la misma instrucción: cada asignación pertenece a la sesión actual y a nada más.  La palabra clave existe para que los scripts se lean igual que en las bases de datos donde `SET SESSION` y `SET GLOBAL` son distintos.  `SET GLOBAL` se analiza correctamente, pero se rechaza con `configuration scope "global" is not supported; use SESSION`, porque no hay estado compartido entre sesiones.

Las asignaciones de una lista de `SET` se aplican en orden y no constituyen una transacción: si la segunda asignación de una lista falla, la primera sigue en vigor.

## Destinos {#targets}

| Destino | Efecto |
|--|--|
| `name` o `"name"` | Almacena una variable de sesión escalar, que se desreferencia como `$name` en instrucciones posteriores.  Los puntos por sí solos no seleccionan configuración: `"providers.saved"` es una variable llamada `providers.saved`.  La forma anterior `"variables.name"` es un alias de `name`. |
| `"$.auth.<provider>.<field>"` | Establece un campo del contexto de autenticación del provider para esta sesión.  Los campos son las claves del objeto [`--auth`](/command-line-usage/global-flags) de ese provider, por ejemplo `credentialsenvvar`, `password` o `token_url`. |
| `"$.providers.<alias>"` | Vincula un alias de provider con sus propias credenciales, etiqueta de tenancy, valores predeterminados de parámetros y ajustes de rendimiento.  El valor es un documento JSON en una cadena; consulte [Sesiones de provider](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Almacena un secreto en el almacén de secretos de la sesión.  El valor es una cadena literal, normalmente un documento JSON `auth`, al que un alias de provider hace referencia como `auth_secret`. |
| `"$.secret_store"` | Selecciona un almacén de secretos persistente del sistema operativo, `{"backend":"os","namespace":"..."}`.  Requiere la opción de operador `--allow-persistent-secrets`. |

Todas las formas afectan únicamente a la sesión actual.  Las conexiones independientes a un servidor [`srv`](/command-line-usage/srv) no ven las asignaciones de las demás, y cada solicitud MCP es su propia sesión, así que configure y consulte en un mismo lote de instrucciones cuando use el servidor MCP.

* * *

## Ejemplos {#examples}

### Asignar variables de sesión {#set-session-variables}

Cada instrucción almacena un escalar con el nombre indicado, y el valor conserva su tipo: `region` es una cadena, `minimum_count` un entero y `enabled` un booleano.  `null` se almacena como un valor, de modo que `$optional_value` se resuelve después como `NULL` de SQL en lugar de fallar por no estar definida.  La última instrucción asigna dos variables en una sola lista.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Hacer explícito el ámbito de sesión {#make-the-session-scope-explicit}

Las dos primeras instrucciones son idénticas: `SESSION` nombra el único ámbito que existe, y ambas asignaciones duran hasta que se elimina la variable o termina la sesión.  Use la palabra clave cuando un script deba leerse igual que en una base de datos que distingue entre ajustes de sesión y ajustes globales.  La instrucción comentada se muestra como contraste: no asigna nada y falla con `configuration scope "global" is not supported; use SESSION`, porque no se comparte ningún estado entre sesiones.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Usar una variable en una consulta {#use-a-variable-in-a-query}

Desreferencie una variable con `$name`, sin comillas, en cualquier posición de expresión.  Los valores conservan su tipo: las cadenas se escapan como literales de SQL, y los números, los booleanos y `null` se pasan tal cual.  Las variables de una cláusula `WHERE` se resuelven antes de planificar la solicitud, por lo que se envían al provider igual que los literales.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Restablecer una variable {#reset-a-variable}

`DEFAULT` elimina la variable, exactamente como lo hace [`UNSET`](/language-spec/unset), de modo que un `$region` posterior falla con `variable "$region" is not defined` hasta que se vuelve a asignar; la consulta comentada muestra ese fallo.  Esto es distinto de asignar `null`, que deja la variable en su lugar con un valor `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Copiar un valor de otra variable {#copy-a-value-from-another-variable}

El lado derecho puede ser una referencia entre comillas dobles a una variable existente, que copia el valor que esta contiene en ese momento.  Aquí una vinculación de provider se almacena primero como una variable ordinaria llamada `providers.saved` (los puntos no la convierten en configuración) y después se activa copiándola a la ruta `$.providers.production`.  Eliminar después la copia guardada no afecta a la vinculación activa.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Sobrescribir un campo de credenciales para esta sesión {#override-a-credential-field-for-this-session}

El contexto de autenticación de un provider es el objeto que se le proporciona mediante [`--auth`](/command-line-usage/global-flags) al inicio.  Una ruta bajo `$.auth` reemplaza un campo de ese objeto durante el resto de la sesión y deja los demás campos como estaban.  Aquí se hace que el provider de GitHub apunte a otra variable de entorno, que contiene un token de solo lectura; la variable se lee del entorno del proceso de StackQL, y las consultas posteriores a `github.*` la usan hasta que se elimina el campo o termina la sesión.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Vincular un alias de provider {#bind-a-provider-alias}

Un alias de provider es una segunda instancia de un provider, configurada de forma independiente, que se usa en lugar del nombre del provider en las consultas.  El valor es un documento JSON en una cadena: `provider` nombra el provider subyacente, `tenancy` etiqueta la cuenta, `auth` es un bloque de credenciales en el formato de alias (para AWS, `access_key_id_env_var` y `secret_access_key_env_var`, no las claves `keyIDenvvar` y `credentialsenvvar` de `--auth`), y `params` proporciona valores predeterminados para parámetros de solicitud como `region`.  Las consultas a `production.s3.buckets` se ejecutan entonces con esas credenciales, mientras que `aws.s3.buckets` conserva la configuración de inicio.  La lista completa de claves está en [Sesiones de provider](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
