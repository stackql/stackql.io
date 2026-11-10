---
title: Ressourcenhierarchie
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die StackQL-Ressourcenhierarchie: wie Provider, Services, Ressourcen und Methoden Cloud- und SaaS-APIs auf abfragbare SQL-Tabellen abbilden."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Siehe auch:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Objekte in StackQL werden in der folgenden Hierarchie dargestellt, hier am Beispiel des Google-Cloud-Providers:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL-Ressourcenhierarchie"/>

### Die StackQL-Objektnotation verwenden {#using-the-stackql-object-notation}

Diese Hierarchie wird in der Objektnotation `<provider>.<service>.<resource>` im Kontext eines bestimmten Providers ausgedrückt; ein Beispiel dieser Objektnotation in einer [`SELECT`](/language-spec/select)-Anweisung sehen Sie hier:  

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

Wenn eine Ressource oder ein Service einen Punkt (`.`) im Namen enthält, schließen Sie den Ressourcennamen in Backticks (__`__) ein, zum Beispiel:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Services in einem Provider anzeigen {#showing-services-in-a-provider}

Services innerhalb eines Providers können mit dem Befehl [`SHOW SERVICES`](/language-spec/show) aufgelistet werden, wie hier gezeigt:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Ressourcen innerhalb eines Services anzeigen {#showing-resources-within-a-service}

Ebenso können Ressourcen innerhalb eines Services mit [`SHOW RESOURCES`](/language-spec/show) aufgelistet werden, wie hier gezeigt:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Felder innerhalb einer Ressource anzeigen {#showing-fields-within-a-resource}

Felder innerhalb einer Ressource können mit einer [`DESCRIBE`](/language-spec/describe)-Anweisung angezeigt werden, wie hier gezeigt:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Ausführbare Methoden für eine Ressource anzeigen {#showing-executable-methods-for-a-resource}

Für eine Ressource verfügbare Methoden können mit dem Befehl [`SHOW METHODS`](/language-spec/show) angezeigt werden, wie hier gezeigt:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
