---
title: Jerarquía de recursos
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "La jerarquía de recursos de StackQL: cómo los providers, servicios, recursos y métodos asignan las API en la nube y de SaaS a tablas SQL consultables."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Consulte también:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Los objetos de StackQL se representan en la siguiente jerarquía, que se muestra con un ejemplo basado en el provider de Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Jerarquía de recursos de StackQL"/>

### Usar la notación de objetos de StackQL {#using-the-stackql-object-notation}

Esta jerarquía se expresa con la notación de objetos `<provider>.<service>.<resource>` en el contexto de un provider determinado; a continuación se muestra un ejemplo de esta notación de objetos en una instrucción [`SELECT`](/language-spec/select):  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Si el nombre de un recurso o de un servicio contiene un punto (`.`), use el símbolo de acento grave (__`__) para encerrar el nombre del recurso, por ejemplo:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Mostrar los servicios de un provider {#showing-services-in-a-provider}

Los servicios de un provider se pueden enumerar con el comando [`SHOW SERVICES`](/language-spec/show), como se muestra aquí:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Mostrar los recursos de un servicio {#showing-resources-within-a-service}

Del mismo modo, los recursos de un servicio se pueden enumerar con [`SHOW RESOURCES`](/language-spec/show), como se muestra aquí:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Mostrar los campos de un recurso {#showing-fields-within-a-resource}

Los campos de un recurso se pueden mostrar con una instrucción [`DESCRIBE`](/language-spec/describe), como se muestra aquí:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Mostrar los métodos ejecutables de un recurso {#showing-executable-methods-for-a-resource}

Los métodos disponibles para un recurso se pueden mostrar con un comando [`SHOW METHODS`](/language-spec/show), como se muestra aquí:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
