---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El comando stackql registry enumera, descarga y gestiona providers del registro de providers de StackQL desde la línea de comandos."
image: "/img/stackql-featured-image.png"
---

Comando que se usa para interactuar con el StackQL Provider Registry.  La ubicación del registro, y si se verifican las firmas de los documentos de provider, se configuran con la opción global [`--registry`](/command-line-usage/global-flags#--registry).  El comando `registry` no está disponible mientras la [gestión de accesos](/command-line-usage/access-management) está habilitada; en su lugar, use `REGISTRY PULL` en una sesión `exec` o `shell` protegida.

* * * 

### Sintaxis {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Argumentos {#arguments}

| Argumento | Descripción | Ejemplo |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Operación de registro que se va a realizar (`list` o `pull`) | `list` o `pull` |    
|<span class="nowrap">`provider`</span>|Provider que se va a instalar con el subcomando `pull`| `okta` |
|<span class="nowrap">`version`</span>|Versión opcional del provider que se va a instalar con el subcomando `pull` *(versión del documento de provider de StackQL)* | `v23.03.00121` |


### Opciones {#flags}

| Opción | Descripción |
|--|--|
|<span class="nowrap">`-h, --help`</span>|Muestra la información de ayuda|
<br/>
<br/>
> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales

* * *

### Ejemplos {#examples}

Enumerar los providers disponibles en el registro público:
```shell
stackql registry list
```

Descargar la versión más reciente de un provider del registro público:
```shell
stackql registry pull okta
```
