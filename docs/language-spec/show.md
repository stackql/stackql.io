---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "SHOW in StackQL: list providers, services, resources and methods, generate INSERT templates, and inspect the running binary's version, contributors and dependencies."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Used to list items in a collection, for instance show all of the services available in a provider, or show all of the resources available in a given service. Also exposes the running StackQL binary's version, contributors and software dependencies.

See also:  
[[ StackQL Resource Hierarchy ]](/getting-started/resource-hierarchy)

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
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS` and `DEPENDENCIES` require no provider or authentication configuration. `DEPENDENCIES` supports an optional `LIKE` clause only; `IN`, `FROM`, `WHERE` and `LIMIT` are not supported.

* * *

## Examples

### List all providers
List all of the available cloud providers in the installed version of StackQL.
```sql
-- Returns all of the available cloud providers
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### List all services in a cloud provider
List all of the services available within a given cloud provider, use the optional EXTENDED keyword to return additional information.
```sql
-- Returns all services available in a cloud provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### List all resources within a cloud provider service
List all of the resources available within a given service of a cloud provider.  The optional `EXTENDED` keyword returns the resource description in addition to the resource name.
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Show available methods for a resource within a cloud provider service
List all of the methods available for a given resource.
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Generating an `INSERT` template using the `SHOW INSERT` command

The `SHOW` command can be used to generate templates for `INSERT` statements as shown here:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

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

</details>

> The fields returned can be limited to only those required by the provider, see [__Creating Infrastructure Templates__](/getting-started/templating) for examples of this.

> This template can then be used along with a `json` or `jsonnet` data file to supply values at run time,for more information see [__Using Variables__](/getting-started/variables).

### Show the StackQL version

Return the version of the running StackQL process. Useful in shell, exec, and Postgres-wire server modes when you need to confirm which build is answering queries. The same information is also surfaced through the MCP `server_info` tool.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

Use the optional `EXTENDED` keyword to also return the git commit, build timestamp, and build platform:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Returns :</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` requires no provider, registry, or authentication configuration and is safe to issue in any session.

### List StackQL contributors

Return the contributor leaderboard embedded in the running StackQL binary. The snapshot credits human GitHub contributors who have starred the StackQL repository, ordered by contribution count, highest first. It is fixed for each build and does not query GitHub at runtime.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Column | Description |
| --- | --- |
| `contributor` | GitHub username |
| `contributions` | Contribution count recorded in the snapshot; included with `EXTENDED` |

### List the binary's software dependencies

Return an offline inventory of the Go modules included in the running StackQL executable. This provides a lightweight software bill of materials (SBOM) for the binary, queryable by users and agents at runtime. It includes direct and transitive Go dependencies; installed provider definitions, separate wrappers, CI tools, test-only modules, container images and operating-system packages are outside its scope.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

The default columns are `name`, `version`, `license` and `description`. `EXTENDED` appends the remaining columns below without changing which rows are returned.

| Column | Description |
| --- | --- |
| `name` | Effective Go module path, using the replacement path when a Go module replacement applies |
| `version` | Exact module version, including prerelease and pseudo-versions |
| `license` | Detected licence identifier; `UNKNOWN` when unidentified or ambiguous |
| `description` | Component description, when available |
| `source_url` | Source repository URL; included with `EXTENDED` |
| `license_url` | Licence file URL pinned to the module tag or source revision; included with `EXTENDED` |
| `purl` | Package URL identifying the module and version; included with `EXTENDED` |
| `checksum` | Go module checksum with its `h1:` prefix, not an executable or archive SHA-256 digest; included with `EXTENDED` |
| `original_name` | Originally requested module path when a replacement applies; included with `EXTENDED` |
| `original_version` | Originally requested version when a replacement applies; included with `EXTENDED` |

Rows are sorted by effective module name, then version. Replacements use the replacement's version, checksum and attribution. `original_name` and `original_version` are `NULL` for modules without replacements. Other unavailable metadata is also `NULL`, except that unidentified licences are `UNKNOWN`. Local filesystem replacements have no fabricated version, URLs or checksum.

Use `LIKE` to search the effective module name, original module name or description:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

Matching is case-insensitive. `%` matches zero or more characters and `_` matches exactly one character. Licence and version columns are not searched. Double a single quote inside a SQL string literal; after SQL string decoding, backslashes are literal and do not escape `LIKE` wildcards.

When no modules match, the result has zero rows. CSV retains the column headers, PostgreSQL wire clients retain column metadata, and JSON output is `null`.

The inventory combines metadata embedded at build time with the executable's Go build information. Queries need no network access, Go installation, source checkout or module cache. Builds without generated metadata still report their linked modules, with missing attribution shown as `UNKNOWN` or `NULL`.

The output covers the binary's Go dependencies only. It does not include dependency relationships or export an SPDX or CycloneDX document, and is not a complete deployment SBOM or a licence-compliance determination.

* * *
