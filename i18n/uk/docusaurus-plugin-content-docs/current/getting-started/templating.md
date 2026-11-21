---
title: Створення шаблонів інфраструктури
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Шаблони INSERT для будь-якого ресурсу з SHOW INSERT у StackQL, повні або лише з обов'язковими параметрами, як відправна точка для визначень інфраструктури."
image: "/img/stackql-featured-image.png"
---
Див. також:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Використання змінних ]](/getting-started/variables)

У StackQL хмарні ресурси розгортають за допомогою інструкцій `INSERT`.  Шаблони `INSERT` для різних хмарних ресурсів можна згенерувати командою `SHOW INSERT`.

### Генерування шаблону `INSERT` за допомогою команди `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Ось приклад генерування шаблону:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Цю команду можна виконати в оболонці `shell` або за допомогою команди `exec`, як показано тут:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Повертає:__*  

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

> Потім цей шаблон можна використати разом із файлом даних `json` або `jsonnet`, щоб передати значення під час виконання; докладніше див. [Використання змінних](/getting-started/variables).

### Генерування шаблону `INSERT` лише з обов'язковими параметрами {#generating-an-insert-template-with-only-required-parameters}

Щоб згенерувати шаблон `INSERT` для StackQL лише з обов'язковими атрибутами, використовуйте підказку для запиту `/*+ REQUIRED */`, як показано тут:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Цю команду можна виконати в оболонці `shell` або за допомогою команди `exec`, як показано тут:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Повертає:__*  

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
