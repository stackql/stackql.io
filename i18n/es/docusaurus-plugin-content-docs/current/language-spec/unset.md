---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de sesión
  - unset
  - sesiones de provider
  - secretos
description: "UNSET en StackQL: eliminar variables de sesión o quitar alias de provider, campos de credenciales y secretos configurados con SET para la sesión actual."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Elimina una variable de sesión o quita la configuración de tiempo de ejecución establecida con [`SET`](/language-spec/set) para la sesión actual.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` SET `]](/language-spec/set) [[ Variables de sesión ]](/getting-started/session-variables) [[ Sesiones de provider ]](/getting-started/provider-sessions)

* * * 

## Sintaxis {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Cualquier destino aceptado por `SET`: un nombre de variable simple o entre comillas dobles, o una ruta de configuración entre comillas dobles y con prefijo de dólar.

__*SESSION*__  
Opcional, y el único ámbito que existe: `UNSET SESSION name` y `UNSET name` son la misma instrucción, porque cada variable y cada ajuste de configuración pertenece a la sesión actual.  `UNSET GLOBAL` se rechaza con `configuration scope "global" is not supported; use SESSION`.

`UNSET name` equivale a `SET name = DEFAULT`.  Los destinos de una lista se eliminan en orden, y eliminar un nombre que no está asignado no es un error.  Desreferenciar una variable después de eliminarla falla antes de que se haga ninguna solicitud al provider.

| Destino | Efecto |
|--|--|
| `name` | Elimina la variable de sesión. |
| `"$.auth.<provider>.<field>"` | Elimina ese campo del contexto de autenticación de sesión del provider.  El valor vigente antes de la sobrescritura no se restaura. |
| `"$.providers.<alias>"` | Elimina la vinculación del alias de provider. |
| `"$.secrets.<name>"` | Elimina el secreto del almacén de la sesión.  En un almacén persistente del sistema operativo, esto elimina el valor almacenado. |
| `"$.secret_store"` | Descarta la vinculación actual del almacén y comienza con la memoria de sesión vacía.  Los valores ya persistidos en un almacén del sistema operativo no se eliminan. |

* * *

## Ejemplos {#examples}

### Eliminar una variable {#remove-a-variable}

La variable se elimina de la sesión, de modo que la consulta comentada falla con `variable "$region" is not defined` en lugar de ejecutarse con un valor obsoleto o vacío.  Vuelva a asignar el nombre para reutilizarlo.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Eliminar varios destinos {#remove-several-targets}

Una sola instrucción puede eliminar variables y rutas de configuración a la vez, y los destinos se procesan en orden.  Aquí se eliminan en una sola lista una variable simple, una variable con un nombre con puntos y una sobrescritura de credenciales.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### Hacer explícito el ámbito de sesión {#make-the-session-scope-explicit}

Estas dos instrucciones hacen lo mismo: `SESSION` nombra el único ámbito que existe.  La forma explícita es adecuada para los scripts que también se ejecutan en bases de datos que distinguen entre el estado de sesión y el estado global.  La instrucción comentada falla porque no existe ningún estado compartido.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Quitar una sobrescritura de credenciales {#clear-a-credential-override}

El campo establecido antes con `SET "$.auth.github.password_var"` se elimina del contexto de autenticación de sesión del provider de GitHub, y los demás campos de ese contexto no se modifican.  El valor vigente antes de la sobrescritura no se restaura, así que vuelva a establecer el campo si el provider aún lo necesita.

```sql
UNSET "$.auth.github.password_var";
```

### Eliminar un alias de provider y su secreto {#remove-a-provider-alias-and-its-secret}

El alias `production` se vinculó con `SET "$.providers.production"` y hace referencia, mediante `auth_secret`, a un secreto almacenado en `$.secrets.production`.  Al eliminar ambos en una sola instrucción se descarta la vinculación, de modo que `production.s3.buckets` deja de ser una relación válida, y se elimina el secreto del almacén de la sesión.  `SESSION` es opcional aquí, como en todas partes.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
