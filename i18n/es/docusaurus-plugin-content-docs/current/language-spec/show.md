---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "SHOW en StackQL: enumerar providers, servicios, recursos y métodos, generar plantillas de INSERT y examinar la versión, colaboradores y dependencias del binario en ejecución."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Se usa para enumerar los elementos de una colección, por ejemplo, para mostrar todos los servicios disponibles en un provider o todos los recursos disponibles en un servicio determinado. También expone la versión, los colaboradores y las dependencias de software del binario de StackQL en ejecución.

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxis {#syntax}

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

`VERSION`, `CONTRIBUTORS` y `DEPENDENCIES` no requieren configuración de providers ni de autenticación. `DEPENDENCIES` admite únicamente una cláusula `LIKE` opcional; `IN`, `FROM`, `WHERE` y `LIMIT` no se admiten.

* * *

## Ejemplos {#examples}

### Enumerar todos los providers {#list-all-providers}
Enumere los providers disponibles en el entorno local de StackQL: los descargados del registro de providers, además de los providers integrados en StackQL.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

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

### Enumerar todos los servicios de un provider {#list-all-services-in-a-cloud-provider}
Enumere todos los servicios disponibles en un provider determinado; use la palabra clave opcional EXTENDED para devolver información adicional.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

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


### Enumerar todos los recursos de un servicio de un provider {#list-all-resources-within-a-cloud-provider-service}
Enumere todos los recursos disponibles en un servicio determinado de un provider.  La palabra clave opcional `EXTENDED` devuelve la descripción del recurso además de su nombre.
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

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

### Mostrar los métodos disponibles de un recurso de un servicio de un provider {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Enumere todos los métodos disponibles para un recurso determinado.
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

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

### Generar una plantilla de `INSERT` con el comando `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

El comando `SHOW` se puede usar para generar plantillas de instrucciones `INSERT`, como se muestra aquí:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

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

> Los campos devueltos se pueden limitar a los que requiere el provider; consulte [__Crear plantillas de infraestructura__](/getting-started/templating) para ver ejemplos de ello.

> Esta plantilla se puede usar después junto con un archivo de datos `json` o `jsonnet` para proporcionar los valores en tiempo de ejecución; para obtener más información, consulte [__Usar variables__](/getting-started/variables).

### Mostrar la versión de StackQL {#show-the-stackql-version}

Devuelve la versión del proceso de StackQL en ejecución. Resulta útil en los modos shell, exec y servidor con protocolo de red de PostgreSQL cuando necesita confirmar qué build responde a las consultas. La misma información también está disponible a través de la herramienta MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

```
|---------|
| version |
|---------|
| 1.0.888 |
|---------|
```

</details>

Use la palabra clave opcional `EXTENDED` para devolver también el commit de git, la marca de tiempo del build y la plataforma del build:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Devuelve:</strong></em></summary>

```
|---------|---------|----------------------|----------|
| version | commit  |      build_date      | platform |
|---------|---------|----------------------|----------|
| 1.0.888 | f7b4116 | 2026-11-05T00:26:03Z | Windows  |
|---------|---------|----------------------|----------|
```

</details>

> `SHOW VERSION` no requiere configuración de providers, de registro ni de autenticación y se puede ejecutar con seguridad en cualquier sesión.

### Enumerar los colaboradores de StackQL {#list-stackql-contributors}

Devuelve la clasificación de colaboradores integrada en el binario de StackQL en ejecución. La instantánea reconoce a los colaboradores humanos de GitHub que han marcado con una estrella el repositorio de StackQL, ordenados por número de contribuciones, de mayor a menor. Es fija para cada build y no consulta GitHub en tiempo de ejecución.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Columna | Descripción |
| --- | --- |
| `contributor` | Nombre de usuario de GitHub |
| `contributions` | Número de contribuciones registrado en la instantánea; se incluye con `EXTENDED` |

### Enumerar las dependencias de software del binario {#list-the-binarys-software-dependencies}

Devuelve un inventario sin conexión de los módulos de Go incluidos en el ejecutable de StackQL en ejecución. Esto proporciona una lista de materiales de software (SBOM) ligera para el binario, que los usuarios y los agentes pueden consultar en tiempo de ejecución. Incluye las dependencias de Go directas y transitivas; las definiciones de providers instaladas, los wrappers independientes, las herramientas de CI, los módulos solo de prueba, las imágenes de contenedor y los paquetes del sistema operativo quedan fuera de su ámbito.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

Las columnas predeterminadas son `name`, `version`, `license` y `description`. `EXTENDED` agrega las demás columnas indicadas a continuación sin cambiar las filas que se devuelven.

| Columna | Descripción |
| --- | --- |
| `name` | Ruta efectiva del módulo de Go; se usa la ruta de reemplazo cuando se aplica un reemplazo de módulo de Go |
| `version` | Versión exacta del módulo, incluidas las versiones preliminares y las pseudoversiones |
| `license` | Identificador de licencia detectado; `UNKNOWN` cuando no se identifica o es ambiguo |
| `description` | Descripción del componente, cuando está disponible |
| `source_url` | URL del repositorio de código fuente; se incluye con `EXTENDED` |
| `license_url` | URL del archivo de licencia fijada al tag del módulo o a la revisión del código fuente; se incluye con `EXTENDED` |
| `purl` | Package URL que identifica el módulo y la versión; se incluye con `EXTENDED` |
| `checksum` | Suma de comprobación del módulo de Go con su prefijo `h1:`, no un hash SHA-256 de un ejecutable o de un archivo comprimido; se incluye con `EXTENDED` |
| `original_name` | Ruta del módulo solicitada originalmente cuando se aplica un reemplazo; se incluye con `EXTENDED` |
| `original_version` | Versión solicitada originalmente cuando se aplica un reemplazo; se incluye con `EXTENDED` |

Las filas se ordenan por el nombre efectivo del módulo y después por la versión. Los reemplazos usan la versión, la suma de comprobación y la atribución del reemplazo. `original_name` y `original_version` son `NULL` en los módulos sin reemplazos. Los demás metadatos no disponibles también son `NULL`, salvo las licencias no identificadas, que son `UNKNOWN`. Los reemplazos del sistema de archivos local no tienen versión, URL ni suma de comprobación inventadas.

Use `LIKE` para buscar en el nombre efectivo del módulo, el nombre original del módulo o la descripción:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

La coincidencia no distingue entre mayúsculas y minúsculas. `%` coincide con cero o más caracteres y `_` coincide exactamente con un carácter. No se busca en las columnas de licencia y de versión. Duplique las comillas simples dentro de un literal de cadena de SQL; tras la decodificación de la cadena de SQL, las barras invertidas son literales y no escapan los comodines de `LIKE`.

Cuando ningún módulo coincide, el resultado tiene cero filas. CSV conserva los encabezados de columna, los clientes del protocolo de red de PostgreSQL conservan los metadatos de las columnas y la salida JSON es `null`.

El inventario combina los metadatos integrados durante el build con la información de build de Go del ejecutable. Las consultas no necesitan acceso a la red, una instalación de Go, una copia del código fuente ni una caché de módulos. Los builds sin metadatos generados siguen informando de sus módulos enlazados, y la atribución que falta se muestra como `UNKNOWN` o `NULL`.

La salida abarca únicamente las dependencias de Go del binario. No incluye las relaciones entre dependencias ni exporta un documento SPDX o CycloneDX, y no constituye un SBOM completo del despliegue ni una determinación de cumplimiento de licencias.

* * *
