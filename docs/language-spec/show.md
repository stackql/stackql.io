---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Used to list items in a collection, for instance show all of the services available in a provider, or show all of the resources available in a given service.

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS | AUTH | INSERT INTO <resource> }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];
```
* * *

## Examples

### List all providers
List all of the available cloud providers in the installed version of StackQL.
```sql
-- Returns all of the available cloud providers
SHOW PROVIDERS;
```
*__Returns :__*

    +------------+
    |    NAME    |
    +------------+
    | google     |
    | aws        |
    | azure      |
    | kubernetes |
    +------------+

### List all services in a cloud provider
List all of the services available within a given cloud provider, use the optional EXTENDED keyword to return additional information.
```sql
-- Returns all services available in a cloud provider
SHOW EXTENDED SERVICES IN google;
```
*__Returns :__*  

    +---------------+-----------+------------------------+---------+----------------------------------------------------------------------------------------------------+
    |      ID       |   NAME    |         TITLE          | VERSION |                                            DESCRIPTION                                             |
    +---------------+-----------+------------------------+---------+----------------------------------------------------------------------------------------------------+
    | compute__v1   | compute   | Compute Engine API     | v1      | Creates and runs virtual machines on Google Cloud Platform.                                        |
    | storage__v1   | storage   | Cloud Storage JSON API | v1      | Stores and retrieves potentially large, immutable data objects.                                    |
    | container__v1 | container | Kubernetes Engine API  | v1      | Builds and manages container-based applications, powered by the open source Kubernetes technology. |
    | bigquery__v2  | bigquery  | BigQuery API           | v2      | A data platform for customers to create, manage, share and query data.                             |
    | ...           | ...       | ...                    | ...     | ...                                                                                                |
    +---------------+-----------+------------------------+---------+----------------------------------------------------------------------------------------------------+



### List all resources within a cloud provider service
List all of the resources available within a given service of a cloud provider.  The USE statement can be used to specify a cloud provider for an StackQL session.  The optional EXTENDED keyword returns the resource description in addition to the resource name.
```sql
-- Returns all resources available in a cloud provider service
USE google;
SHOW EXTENDED RESOURCES IN compute;
```
*__Returns :__*  

    +-----------+------------------------------------------------------------------------------------------------------------+
    |   NAME    |                                                DESCRIPTION                                                 |
    +-----------+------------------------------------------------------------------------------------------------------------+
    | instances | Represents an Instance resource. An instance is a virtual machine that is hosted on Google Cloud Platform. |
    | networks  | Represents a VPC Network resource. Networks connect resources to each other and to the internet.           |
    | images    | Represents an Image resource.  You can use images to create boot disks for your VM instances.              |
    | ...       | ...                                                                                                        |
    +-----------+------------------------------------------------------------------------------------------------------------+

### Show available methods for a resource within a cloud provider service
List all of the methods available for a given resource.
```sql
-- Returns all methods available for a cloud provider resource
USE google;
SHOW METHODS IN compute.instances;
```
*__Returns :__*  

    |------------------------------------|--------------------------------|
    |             MethodName             |         RequiredParams         |
    |------------------------------------|--------------------------------|
    | start                              | zone, instance, project        |
    |------------------------------------|--------------------------------|
    | setServiceAccount                  | instance, project, zone        |
    |------------------------------------|--------------------------------|
    | setDiskAutoDelete                  | project, zone, autoDelete,     |
    |                                    | deviceName, instance           |
    |------------------------------------|--------------------------------|
    | getScreenshot                      | instance, project, zone        |
    |------------------------------------|--------------------------------|
    | updateNetworkInterface             | instance, networkInterface,    |
    |                                    | project, zone                  |
    |------------------------------------|--------------------------------|
    | testIamPermissions                 | project, resource, zone        |
    |------------------------------------|--------------------------------|
    | stop                               | instance, project, zone        |
    |------------------------------------|--------------------------------|
    ..
    |------------------------------------|--------------------------------|

### Generating an `INSERT` template using the `SHOW INSERT` command

The `SHOW` command can be used to generate templates for `INSERT` statements as shown here:

```sql
SHOW INSERT INTO google.compute.addresses;
```
*__Returns :__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> The fields returned can be limited to only those required by the provider, see [__Creating Infrastructure Templates__](/docs/getting-started/templating) for examples of this.

> This template can then be used along with a `json` or `jsonnet` data file to supply values at run time,for more information see [__Using Variables__](/docs/getting-started/variables).

* * * 