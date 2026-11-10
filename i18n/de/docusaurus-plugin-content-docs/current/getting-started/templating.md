---
title: Infrastrukturvorlagen erstellen
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "INSERT-Vorlagen für jede Ressource mit SHOW INSERT in StackQL erzeugen, vollständig oder nur mit erforderlichen Parametern, als Ausgangspunkt für Infrastrukturdefinitionen."
image: "/img/stackql-featured-image.png"
---
Siehe auch:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Variablen verwenden ]](/getting-started/variables)

Cloud-Ressourcen werden mit StackQL über `INSERT`-Anweisungen bereitgestellt.  `INSERT`-Vorlagen für verschiedene Cloud-Ressourcen erzeugen Sie mit dem Befehl `SHOW INSERT`.

### Eine `INSERT`-Vorlage mit dem Befehl `SHOW INSERT` erzeugen {#generating-an-insert-template-using-the-show-insert-command}

Ein Beispiel für das Erzeugen einer Vorlage sehen Sie hier:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Dies kann über die `shell` oder mit einem `exec`-Befehl ausgeführt werden, wie hier gezeigt:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Rückgabe:__*  

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

> Diese Vorlage kann dann zusammen mit einer `json`- oder `jsonnet`-Datendatei verwendet werden, um Werte zur Laufzeit zu liefern; weitere Informationen finden Sie unter [Variablen verwenden](/getting-started/variables).

### Eine `INSERT`-Vorlage nur mit erforderlichen Parametern erzeugen {#generating-an-insert-template-with-only-required-parameters}

Um eine StackQL-`INSERT`-Vorlage nur mit Pflichtattributen zu erzeugen, verwenden Sie den Abfragehinweis `/*+ REQUIRED */`, wie hier gezeigt:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Dies kann über die `shell` oder mit einem `exec`-Befehl ausgeführt werden, wie hier gezeigt:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into compute.instances' --output text -H
```

*__Rückgabe:__*  

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
