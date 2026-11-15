---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestión de accesos
  - groups
  - directorio de principales
description: "Función GROUPS en StackQL: devolver los grupos del directorio del llamador autenticado como un array JSON cuando la gestión de accesos está habilitada."
image: "/img/stackql-featured-image.png"
---
Devuelve las pertenencias a grupos del directorio del llamador autenticado como un array JSON.  Requiere una sesión iniciada con la [gestión de accesos](/command-line-usage/access-management) habilitada.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Gestión de accesos ]](/command-line-usage/access-management)

* * * 

## Sintaxis {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Argumentos {#arguments}

Ninguno.

## Valor(es) devuelto(s) {#return-values}

Devuelve un array JSON de ID de grupo de la instantánea del directorio de principales tomada para la instrucción actual, por ejemplo `["readers"]`.  La pertenencia se actualiza con cada instrucción, de modo que la siguiente instrucción ya refleja una revocación.  La función falla en una sesión iniciada sin `--access.config`.

* * *

## Ejemplos {#examples}

### Inspeccionar la identidad establecida {#inspect-the-established-identity}

Las dos funciones juntas muestran el ID del principal del llamador y sus grupos actuales del directorio.  Como la pertenencia se lee de nuevo con cada instrucción, ejecutar esta consulta después de un cambio en el directorio confirma que el cambio es visible para la sesión.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
