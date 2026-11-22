---
title: Usar StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Usar StackQL: ejecutar consultas en el shell interactivo, configurarlo con un archivo .stackqlrc o ejecutarlas de forma no interactiva con exec, en línea o desde un archivo IQL."
image: "/img/stackql-featured-image.png"
---
Consulte también:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL tiene varios modos de uso, entre ellos: el shell interactivo de StackQL, la línea de comandos de StackQL con instrucciones de StackQL o la ejecución de una o varias instrucciones de StackQL desde un archivo de entrada (archivo IQL). Cada uno de los modos se describe con más detalle a continuación:

- Usar el shell interactivo de StackQL ([`stackql shell`](/command-line-usage/shell))
- Usar la utilidad de línea de comandos de StackQL ([`stackql exec`](/command-line-usage/exec))
- Usar un archivo de entrada (archivo IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Usar el shell interactivo de StackQL {#using-the-stackql-interactive-shell}
El shell de StackQL ofrece a los ingenieros un entorno de programación interactivo para desplegar recursos en la nube e interactuar con ellos, y permite además el análisis interactivo (con instrucciones [`SELECT`](/language-spec/select)) de su entorno en la nube. El shell es funcionalmente similar al shell de MySQL o a otros shells REPL (Read Evaluate Print Loop). El shell acepta instrucciones de StackQL válidas y devuelve los resultados al usuario. Las instrucciones pueden ocupar varias líneas y se ejecutan cuando se introduce un punto y coma como terminador.

El shell de StackQL se puede invocar con el comando `shell`, como se muestra a continuación (consulte [StackQL Provider Registry](/providers) para obtener información sobre la autenticación de los providers):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Una vez en un shell autenticado en un proveedor de nube, puede empezar a ejecutar consultas de StackQL (consultas `SELECT` u operaciones de DDL, de infraestructura como código).

De forma predeterminada, el shell de StackQL devuelve resultados tabulares; sin embargo, los formatos de salida `json`, `jsonl`, `csv`, `text` y `otel` están disponibles con la opción `--output`, consulte [Modos de salida](/getting-started/output-modes).  

### El archivo de inicialización `.stackqlrc` {#the-stackqlrc-initialization-file}

Los valores predeterminados de las opciones de comando de StackQL, como el formato de salida, se pueden establecer con el archivo de inicialización `.stackqlrc`. Si este archivo existe en el directorio actual que usa el ejecutable de StackQL, se carga automáticamente; como alternativa, se puede indicar con la opción `--configfile`, como se muestra a continuación:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Encontrará más información sobre las opciones de comando en [Opciones globales](/command-line-usage/global-flags).

### Usar la utilidad de línea de comandos de StackQL {#using-the-stackql-command-line-utility}
Los comandos de StackQL se pueden ejecutar de forma no interactiva con el comando exec del ejecutable de StackQL. Se puede ejecutar más de un comando en un lote siempre que cada comando termine con un punto y coma (`;`). Las opciones de comando, como la opción `--output`, se pueden especificar en la línea de comandos o con el archivo de inicialización `.stackqlrc` comentado anteriormente.  

La salida del ejecutable de StackQL se envía a stdout en el sistema que se esté usando; esta salida se puede analizar dentro de un script de BASH o de PowerShell.  

A continuación se muestra un ejemplo de un script de Windows PowerShell que incluye un comando de StackQL:  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
El comando de PowerShell anterior muestra cómo seleccionar instancias de Compute Engine y mostrar la salida con `Out-GridView`.  

### Usar un archivo de entrada (archivo IQL) {#using-an-input-file-iql-file}
Los archivos de entrada de StackQL se pueden usar para ejecutar varios comandos terminados en punto y coma. Es otra opción para la programación de infraestructura no interactiva o por lotes. Un archivo de entrada se especifica con la opción `-i` o `--infile`, como se muestra a continuación:

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
