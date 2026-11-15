---
title: Consultas entre regiones
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
description: "Consulte y agregue recursos de AWS de varias regiones en una sola instrucción de StackQL, con listas de regiones y ejecución asíncrona en lugar de bucles por región."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Consultas asíncronas entre regiones con StackQL

En esta guía mostraremos una potente capacidad de StackQL: consultar y agregar recursos de varias regiones de AWS.  Esto implica numerosas solicitudes de API a distintos endpoints, que se realizan de forma asíncrona.  Todo ello ocurre de forma fluida y transparente para el usuario.  

> StackQL es una potente herramienta de desarrollo que permite consultar y desplegar infraestructura y recursos en la nube con sintaxis SQL.  

Probado con <span class="tutorial_tested_on">backend SQL integrado</span> <span class="tutorial_tested_on">backend SQL postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Entender las regiones y los recursos de AWS {#understanding-aws-regions-and-resources}

Los recursos de AWS se alojan en varias ubicaciones de todo el mundo.  Estas ubicaciones se componen de regiones y zonas de disponibilidad, diseñadas para que los usuarios puedan situar instancias y almacenar datos en distintas ubicaciones.

## El problema de las regiones de AWS... {#the-problem-with-aws-regions}

Al trabajar con AWS, es fundamental entender que la AWS Management Console ofrece una vista de los recursos limitada a una región, de modo que los usuarios interactúan con los recursos y los gestionan en una sola región específica a la vez.  Del mismo modo, los comandos de la AWS Command Line Interface (CLI) y los Software Development Kits (SDK) usan la región como punto de entrada de las operaciones, por lo que es necesario especificar una región para ejecutar comandos o programas.  

> El marco regional de los servicios en la nube introduce complejidad cuando los recursos abarcan varias regiones.  Además, las brechas de seguridad, incluidos los ataques de minería de criptomonedas, suelen utilizar recursos en diversas áreas, más allá de la región principal que las organizaciones usan habitualmente.

## Especificar regiones en las consultas de StackQL {#specifying-regions-in-stackql-queries}

Con StackQL, puede especificar regiones para dirigirse a recursos situados en distintas ubicaciones geográficas.  Esto resulta especialmente útil para gestionar recursos distribuidos por todo el mundo.

### Ejemplo: enumerar instancias de EC2 de varias regiones {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Esta consulta devuelve una lista de instancias de EC2 de las regiones `us-east-1` (Norte de Virginia) y `eu-west-1` (Irlanda), lo que muestra cómo StackQL puede obtener datos de varias regiones en una sola consulta.  También se puede usar con consultas de agregación, por ejemplo:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## La potencia de las consultas asíncronas {#the-power-of-asynchronous-queries}

StackQL ejecuta estas consultas entre regiones de forma asíncrona y realiza varias solicitudes de API en paralelo a AWS.  Así, los datos se obtienen de forma eficiente, sin esperar a que termine la solicitud de una región para pasar a la siguiente.

## Conclusión {#conclusion}

Las consultas asíncronas entre regiones de StackQL ofrecen un método sólido y eficiente para gestionar recursos de AWS en distintas ubicaciones geográficas.  Al usar una sintaxis similar a SQL para estas consultas, StackQL simplifica tareas complejas de gestión en la nube, lo que lo convierte en una herramienta muy valiosa para los desarrolladores y los administradores de infraestructura en la nube que buscan optimizar su infraestructura de AWS.
