---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestión de accesos
  - whoami
  - principal
description: "Función WHOAMI en StackQL: devolver el ID del principal del llamador autenticado cuando la gestión de accesos está habilitada."
image: "/img/stackql-featured-image.png"
---
Devuelve el ID del principal del llamador autenticado.  Requiere una sesión iniciada con la [gestión de accesos](/command-line-usage/access-management) habilitada.  Disponible a partir de StackQL `v1.0`.

Consulte también:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Gestión de accesos ]](/command-line-usage/access-management)

* * * 

## Sintaxis {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Argumentos {#arguments}

Ninguno.

## Valor(es) devuelto(s) {#return-values}

Devuelve la cadena del ID del principal establecido para la instrucción actual.  La función falla en una sesión iniciada sin `--access.config`.

* * *

## Ejemplos {#examples}

### Inspeccionar la identidad establecida {#inspect-the-established-identity}

Ninguna de las dos funciones recibe argumentos y ambas describen al llamador que el servidor autenticó para esta instrucción.  `whoami()` devuelve el ID del principal y `groups()` los grupos del directorio del llamador, por lo que ejecutarlas juntas es una comprobación rápida de que un token o un certificado se asigna al principal esperado antes de ejecutar cualquier otra cosa.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
