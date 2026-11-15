---
title: Crear plantillas de infraestructura
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Generar plantillas INSERT para cualquier recurso con SHOW INSERT en StackQL, completas o solo con parámetros obligatorios, como punto de partida para definir infraestructura."
image: "/img/stackql-featured-image.png"
---
Consulte también:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Usar variables ]](/getting-started/variables)

Los recursos en la nube se despliegan con StackQL mediante instrucciones `INSERT`. Las plantillas `INSERT` para los distintos recursos en la nube se pueden generar con el comando `SHOW INSERT`.

### Generar una plantilla `INSERT` con el comando `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

A continuación se muestra un ejemplo de generación de una plantilla:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Esto se puede ejecutar desde el `shell` o con un comando `exec`, como se muestra aquí:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Devuelve:__*  

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

> Esta plantilla se puede usar después junto con un archivo de datos `json` o `jsonnet` para suministrar valores en tiempo de ejecución; para obtener más información, consulte [Usar variables](/getting-started/variables).

### Generar una plantilla `INSERT` solo con los parámetros obligatorios {#generating-an-insert-template-with-only-required-parameters}

Para generar una plantilla `INSERT` de StackQL solo con los atributos obligatorios, use la indicación de consulta `/*+ REQUIRED */`, como se muestra aquí:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Esto se puede ejecutar desde el `shell` o con un comando `exec`, como se muestra aquí:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Devuelve:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
