---
title: Создание шаблонов инфраструктуры
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Создание шаблонов INSERT для любого ресурса с помощью SHOW INSERT в StackQL, со всеми или только обязательными параметрами, как основы для определений инфраструктуры."
image: "/img/stackql-featured-image.png"
---
См. также:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Использование переменных ]](/getting-started/variables)

Облачные ресурсы развертываются с помощью StackQL инструкциями `INSERT`.  Шаблоны `INSERT` для разных облачных ресурсов можно сгенерировать командой `SHOW INSERT`.

### Генерация шаблона `INSERT` с помощью команды `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Здесь показан пример генерации шаблона:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Эту инструкцию можно выполнить в `shell` или с помощью команды `exec`, как показано здесь:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Возвращает:__*  

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

> Затем этот шаблон можно использовать вместе с файлом данных `json` или `jsonnet`, чтобы передавать значения во время выполнения; подробнее см. [Использование переменных](/getting-started/variables).

### Генерация шаблона `INSERT` только с обязательными параметрами {#generating-an-insert-template-with-only-required-parameters}

Чтобы сгенерировать шаблон StackQL `INSERT` только с обязательными атрибутами, используйте подсказку запроса `/*+ REQUIRED */`, как показано здесь:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Эту инструкцию можно выполнить в `shell` или с помощью команды `exec`, как показано здесь:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Возвращает:__*  

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
