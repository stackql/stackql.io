---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El comando stackql exec ejecuta una instrucción de StackQL o un script de instrucciones, con opciones para el formato de salida, la autenticación y el registro de providers."
image: "/img/stackql-featured-image.png"
---

Comando que se usa para ejecutar una consulta de StackQL, ya sea como una instrucción o como un lote de instrucciones de un script de StackQL (archivo IQL).  

* * * 

### Sintaxis {#syntax}

`stackql exec [query] [flags]`

* * *

### Argumentos {#arguments}

| Argumento | Descripción | Ejemplo |
|--|--|--|
|`query`|Consulta de StackQL que se va a realizar<br/>o comando que se va a ejecutar | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-b'"`<br/>_Tenga en cuenta que la consulta debe ir entre comillas simples o dobles_ |    


### Opciones {#flags}

| Opción | Descripción |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Modo de salida, los valores válidos incluyen: `table` (valor predeterminado), `json`, `jsonl` (JSON delimitado por saltos de línea, `ndjson` se acepta como alias), `csv`, `text` y `otel` (registros de log de OpenTelemetry, consulte [Modos de salida](/getting-started/output-modes#otel-output)), ejemplo: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Ejecuta la consulta desde el archivo especificado si es necesario; si se especifica, se ignora cualquier consulta proporcionada como argumento, ejemplo: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Datos de origen para el comando mediante un archivo `json` o `jsonnet`, ejemplo: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Opción de ejecución de prueba; solo se ejecuta el preprocesador y se devuelve la salida |
|<span class="nowrap">`--var`</span>|Variables externas (pueden ser variables de entorno o secretos) que usan los archivos jsonnet especificados con la opción `--iqldata`, ejemplo `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Archivo en el que se escriben los resultados si es necesario (el comportamiento predeterminado es mostrar los resultados solo en la consola), ejemplo `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Muestra la información de ayuda|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Ejecuta las consultas en modo detallado con salida adicional enviada a stdout; si se selecciona la opción `-f`, esta información de log adicional se escribe en el archivo de salida junto con los resultados de la consulta|
<br/>
<br/>
> consulte [Opciones globales](/command-line-usage/global-flags) para ver opciones adicionales


* * *

### Ejemplos {#examples}

Mostrar los resultados de la consulta en la consola en formato de tabla:
```shell
stackql exec "SHOW services IN google"
```

Mostrar los resultados de la consulta en la consola en formato json:
```shell
stackql exec "SHOW services IN google" -o json
```

Escribir los resultados de la consulta en un archivo en formato json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Ejecutar una consulta desde un archivo de entrada (archivo IQL) y escribir los resultados en un archivo en formato json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Estado de salida {#exit-status}

`exec` termina con el estado `0` una vez procesada la entrada; los errores de las instrucciones individuales se escriben en la consola, por lo que los scripts deben inspeccionar la salida.  A partir de `v1.0` termina con el estado `1` cuando falla una instrucción de configuración [`SET`](/language-spec/set) o [`UNSET`](/language-spec/unset), cuando una ejecución de IaC en versión preliminar informa de un recurso que ha fallado o, con la [gestión de accesos](/command-line-usage/access-management) habilitada, cuando cualquier instrucción falla o se rechaza.
