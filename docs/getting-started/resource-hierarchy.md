---
title: Resource Heirarchy
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

See also:  
[[` SHOW `]](/docs/language-spec/show) [[` DESCRIBE `]](/docs/language-spec/describe)

* * * 

Objects in StackQL are represented in the following hierarchy, with an example using the Google Cloud provider shown:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="StackQL Resource Heirarchy"/>

### Using the StackQL Object Notation

This hierarchy is expressed in the object notation `<provider>.<service>.<resource>` within the context of a given provider, an example of this object notation used in a [`SELECT`](/docs/language-spec/select) statement is shown here:  

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

If a resource or service contains a period (`.`) in the name, use the backtick symbol (__`__) to encapsulate the resource name, for example:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Showing Services in a Provider

Services within a provider can be listed using the [`SHOW SERVICES`](/docs/language-spec/show) command as shown here:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Showing Resources within a Service

Similarly resources within a service can be listed using the [`SHOW RESOURCES`](/docs/language-spec/show) as shown here:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Showing Fields within a Resource

Fields within a resource can be displayed using a [`DESCRIBE`](/docs/language-spec/describe) statement as shown here:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Showing Executable Methods for a Resource

Methods available for a resource can be displayed using a [`SHOW METHODS`](/docs/language-spec/show) command as shown here:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```