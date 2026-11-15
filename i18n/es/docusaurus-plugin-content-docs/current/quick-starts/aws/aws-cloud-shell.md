---
title: StackQL en AWS Cloud Shell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
  - cspm
  - aws
  - amazon web services
description: "Ejecute StackQL en AWS CloudShell como usuario conectado: descargue el paquete de Linux, use el shell de stackql, asuma un rol de IAM y ejecute consultas por lotes con exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Usar StackQL en AWS Cloud Shell

En esta guía mostraremos cómo usar StackQL, una potente herramienta de desarrollo que permite consultar y desplegar infraestructura y recursos en la nube con sintaxis SQL, desde AWS Cloud Shell, con el contexto de usuario de la sesión iniciada en la consola de AWS o asumiendo otro rol de IAM.    

Probado con <span class="tutorial_tested_on">backend SQL integrado</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>¿Qué es <b>AWS Cloud Shell</b>?</summary>

> AWS Cloud Shell es un servicio de shell preautenticado y basado en el navegador que ofrece Amazon Web Services (AWS).  Permite a los desarrolladores y a los profesionales de TI gestionar sus recursos en la nube directamente desde el navegador web.  Con AWS Cloud Shell, los usuarios pueden ejecutar rápidamente comandos relacionados con los servicios de AWS sin instalar ni configurar la AWS CLI (Command Line Interface) en su equipo local.  Este servicio proporciona un entorno listo para usar con las herramientas habituales de desarrollo y gestión ya instaladas, como la AWS CLI, Python, Node.js y otras, lo que facilita la interacción con los servicios de AWS y la automatización de tareas.  AWS Cloud Shell está integrado directamente en la AWS Management Console y ofrece una experiencia fluida para ejecutar tareas administrativas, explorar los servicios de AWS y gestionar la infraestructura.  Como AWS Cloud Shell autentica automáticamente a los usuarios mediante su inicio de sesión en la consola de AWS, simplifica la ejecución de comandos y scripts en el contexto de su cuenta de AWS, lo que mejora la productividad y la eficiencia de las operaciones en la nube.

</details>

## Funcionamiento {#how-it-works}

El paquete de StackQL para Linux incluye scripts para usar `stackql` directamente en los entornos de cloud shell integrados de los principales proveedores de nube.  Para AWS, el script es __`stackql-aws-cloud-shell.sh`__.  El uso del script __`stackql-aws-cloud-shell.sh`__ se muestra aquí:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

El script `stackql-aws-cloud-shell.sh` descarga automáticamente la versión más reciente del provider de AWS para StackQL.  El código fuente de los scripts de cloud shell incluidos se encuentra en [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Descargar el paquete de `stackql` para Linux desde AWS Cloud Shell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Para empezar, desde AWS Cloud Shell, descargue el binario de StackQL para Linux más reciente con los siguientes comandos:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Esto descarga y extrae el binario `stackql` junto con el script `stackql-aws-cloud-shell.sh` en el directorio de inicio de su entorno de AWS Cloud Shell.  ¡Ya está todo listo!  

## Usar `stackql shell` desde AWS Cloud Shell {#using-the-stackql-shell-from-aws-cloud-shell}

Iniciar un shell de comandos de StackQL autenticado desde AWS Cloud Shell es tan sencillo como:  

```shell
sh stackql-aws-cloud-shell.sh
```

Ahora puede empezar a ejecutar consultas interactivas de AWS basadas en SQL con el provider de StackQL, como se muestra aquí:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Su navegador no admite la etiqueta video.
</video>

## Asumir un rol de IAM con StackQL {#assuming-an-iam-role-using-stackql}

Asumir un rol de IAM en AWS le permite realizar acciones y acceder a recursos que quizá no estén a su alcance con los permisos predeterminados de su usuario.  Esto resulta especialmente útil cuando necesita acceso temporal a recursos o realizar acciones con permisos distintos de los de su cuenta principal.  Con StackQL, puede asumir de forma transparente un rol de IAM para consultar y gestionar recursos de AWS con los permisos que concede ese rol.

<details>
<summary>Entender la asunción de roles de IAM</summary>

> Asumir un rol de IAM es una forma segura de conceder acceso temporal a recursos de AWS.  Cuando asume un rol, AWS STS le proporciona credenciales de seguridad temporales que le permiten acceder a los servicios y recursos de AWS que el rol tiene permiso para usar.  Este mecanismo es ideal para el acceso entre cuentas, el acceso temporal de usuarios o el cumplimiento del principio de privilegio mínimo.

> Los roles se pueden asumir con la AWS Management Console o la AWS CLI, y directamente en StackQL. Así, los usuarios y las aplicaciones solo tienen los permisos necesarios durante el tiempo requerido, lo que refuerza la postura de seguridad de su entorno de AWS.

</details>

Para asumir un rol de IAM con StackQL en AWS Cloud Shell, puede usar la opción `--role-arn` con el script `stackql-aws-cloud-shell.sh`.  Esta opción requiere el Amazon Resource Name (ARN) del rol que desea asumir.  Cuando se proporciona, StackQL usa AWS Security Token Service (STS) para asumir el rol especificado antes de ejecutar cualquier consulta o comando.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Al asumir un rol, puede usar StackQL para interactuar con recursos de AWS a los que su usuario predeterminado quizá no tenga acceso, lo que permite gestionar su entorno en la nube de una forma más flexible y segura.  

Al ejecutar comandos o scripts de StackQL con un rol de IAM asumido, asegúrese de que el rol tenga los permisos necesarios para realizar las acciones deseadas y acceder a los recursos especificados.  Este enfoque facilita un control de acceso granular y se ajusta a las prácticas recomendadas de AWS en materia de seguridad y gobernanza.  

## Ejecutar `stackql exec` (operaciones por lotes) desde AWS Cloud Shell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Las consultas de StackQL también se pueden ejecutar con el comando `exec`; es una opción útil para:

- especificar otros formatos de salida, como `json` o `csv`
- generar archivos de salida con los resultados de las consultas de StackQL
- usar consultas de StackQL proporcionadas en archivos de entrada (archivos `.iql` por convención)
- realizar operaciones complejas, como cargar variables o preprocesar consultas (con `json` o `jsonnet`)

Aquí se muestran ejemplos de uso de `exec` en AWS Cloud Shell:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

El ejemplo anterior ejecuta la consulta de StackQL especificada y genera un archivo de salida `CSV`, que se puede descargar desde AWS Cloud Shell.  Además, puede indicar un rol de IAM con el argumento `--role-arn` para asumir otro rol en su consulta u operación de mutación; aquí se muestra un ejemplo (en este caso, hemos tomado la consulta de un archivo de entrada):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Puede encontrar más información sobre el comando `exec` [__aquí__](/command-line-usage/exec)
