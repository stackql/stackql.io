---
title: Hierarquia de recursos
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "A hierarquia de recursos do StackQL: como providers, serviços, recursos e métodos mapeiam APIs na nuvem e de SaaS para tabelas SQL consultáveis."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Veja também:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Os objetos do StackQL são representados na hierarquia a seguir, com um exemplo que usa o provider do Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Hierarquia de recursos do StackQL"/>

### Usar a notação de objetos do StackQL {#using-the-stackql-object-notation}

Essa hierarquia é expressa na notação de objetos `<provider>.<service>.<resource>` no contexto de um determinado provider; veja aqui um exemplo dessa notação de objetos usada em uma instrução [`SELECT`](/language-spec/select):  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Se um recurso ou serviço tiver um ponto (`.`) no nome, use o caractere de crase (__`__) para delimitar o nome do recurso, por exemplo:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Mostrar os serviços de um provider {#showing-services-in-a-provider}

Os serviços de um provider podem ser listados com o comando [`SHOW SERVICES`](/language-spec/show), como mostrado aqui:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Mostrar os recursos de um serviço {#showing-resources-within-a-service}

Da mesma forma, os recursos de um serviço podem ser listados com [`SHOW RESOURCES`](/language-spec/show), como mostrado aqui:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Mostrar os campos de um recurso {#showing-fields-within-a-resource}

Os campos de um recurso podem ser exibidos com uma instrução [`DESCRIBE`](/language-spec/describe), como mostrado aqui:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Mostrar os métodos executáveis de um recurso {#showing-executable-methods-for-a-resource}

Os métodos disponíveis para um recurso podem ser exibidos com um comando [`SHOW METHODS`](/language-spec/show), como mostrado aqui:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
