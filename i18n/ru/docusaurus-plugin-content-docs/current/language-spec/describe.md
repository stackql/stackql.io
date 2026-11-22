---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "DESCRIBE в StackQL: вывод полей ресурса или параметров и типа возвращаемого значения конкретного метода перед запросом к нему или изменением данных."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Описывает ресурс или конкретный метод ресурса.

См. также:
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy)

* * *

## Синтаксис {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

`DESC` принимается как псевдоним `DESCRIBE` при описании ресурса; для `DESCRIBE METHOD` требуется полное ключевое слово.

## Ответ {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Столбец | Описание |
| --- | --- |
| `name` | Имя поля |
| `type` | Тип данных (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Описание поля (если указано `EXTENDED`) |

</TabItem>
<TabItem value="describe-method">

| Столбец | Описание |
| --- | --- |
| `name` | Имя поля |
| `type` | Тип данных (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` или `output` |
| `shape` | Подмножество JSON Schema для полей-объектов и полей-массивов; для скалярных полей пусто. Включает вложенные `properties`, `items`, `required`, `enum`, `default`, `description`, а также логические значения, если провайдер их предоставляет (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Описание поля (если указано `EXTENDED`) |

</TabItem>
</Tabs>

* * *

## Примеры {#examples}

### Простая инструкция `DESCRIBE` {#basic-describe-statement}
Выполните простую инструкцию `DESCRIBE`, чтобы вывести список полей ресурса в аутентифицированном сеансе.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Расширенная инструкция `DESCRIBE` {#extended-describe-statement}
Выполните расширенную инструкцию `DESCRIBE`, чтобы вывести список полей ресурса и их описания в аутентифицированном сеансе.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Псевдоним `DESC` {#desc-alias}
`DESC` - сокращенная форма `DESCRIBE` для ресурса, с `EXTENDED` или без него.  Эта форма считается инструкцией только для чтения везде, где ею считается `DESCRIBE`, в том числе в инструменте `run_select_query` MCP-сервера в режимах `read_only` и `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Простая инструкция `DESCRIBE METHOD` {#basic-describe-method-statement}
Получите сведения об отдельном методе ресурса. Результат содержит входные параметры и поля ответа, а вложенная структура отображается в столбце `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Расширенная инструкция `DESCRIBE METHOD` {#extended-describe-method-statement}
Выполните расширенную инструкцию `DESCRIBE METHOD`, чтобы наряду с формой получить описание каждого поля.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Определение полезной нагрузки для инструкции `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` - канонический способ узнать, какие поля принимает инструкция `INSERT` (или любой метод, изменяющий данные). Столбец `shape` содержит вложенную структуру, по которой агенты и люди могут составить полезную нагрузку за один запрос.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
