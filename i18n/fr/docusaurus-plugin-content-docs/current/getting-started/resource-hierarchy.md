---
title: Hiérarchie des ressources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "La hiérarchie des ressources StackQL : comment providers, services, ressources et méthodes font correspondre les API cloud et SaaS à des tables SQL interrogeables."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Voir aussi :  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Les objets dans StackQL sont représentés selon la hiérarchie suivante, illustrée par un exemple utilisant le provider Google Cloud :

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Hiérarchie des ressources StackQL"/>

### Utiliser la notation d'objet StackQL {#using-the-stackql-object-notation}

Cette hiérarchie s'exprime avec la notation d'objet `<provider>.<service>.<resource>` dans le contexte d'un provider donné ; un exemple de cette notation d'objet dans une instruction [`SELECT`](/language-spec/select) est présenté ici :  

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

Si le nom d'une ressource ou d'un service contient un point (`.`), utilisez l'accent grave (__`__) pour encadrer le nom de la ressource, par exemple :

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Afficher les services d'un provider {#showing-services-in-a-provider}

Les services d'un provider peuvent être listés avec la commande [`SHOW SERVICES`](/language-spec/show), comme indiqué ici :  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Afficher les ressources d'un service {#showing-resources-within-a-service}

De même, les ressources d'un service peuvent être listées avec la commande [`SHOW RESOURCES`](/language-spec/show), comme indiqué ici :

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Afficher les champs d'une ressource {#showing-fields-within-a-resource}

Les champs d'une ressource peuvent être affichés avec une instruction [`DESCRIBE`](/language-spec/describe), comme indiqué ici :

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Afficher les méthodes exécutables d'une ressource {#showing-executable-methods-for-a-resource}

Les méthodes disponibles pour une ressource peuvent être affichées avec une commande [`SHOW METHODS`](/language-spec/show), comme indiqué ici :

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
