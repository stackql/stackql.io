---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El comando stackql shell inicia el shell de comandos interactivo de StackQL para ejecutar consultas y comandos de provider en sus cuentas en la nube y de SaaS."
image: "/img/stackql-featured-image.png"
---

Comando que se usa para iniciar el shell de comandos interactivo de StackQL.  Para obtener más información, consulte [Usar el shell interactivo de StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Sintaxis {#syntax}

`stackql shell [flags]`

* * *

### Opciones {#flags}

| Opción | Descripción |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Muestra la información de ayuda|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Ejecuta las consultas en modo detallado con salida adicional enviada a stdout; si se establece la opción global `-f` (`--outfile`), esta información de log adicional se escribe en el archivo de salida junto con los resultados de la consulta|
<br/>
<br/>
> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales

* * *

### Ejemplos {#examples}

Iniciar el shell de StackQL, usando un archivo de clave de cuenta de servicio para la autenticación.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
