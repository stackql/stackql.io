---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "DESCRIBE у StackQL: виведення полів ресурсу або параметрів і типу значення, що повертається, певного методу перед запитом до нього чи зміненням даних."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Описує ресурс або певний метод ресурсу.

Див. також:
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy)

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

Під час опису ресурсу `DESC` можна використовувати як псевдонім `DESCRIBE`; `DESCRIBE METHOD` потребує повного ключового слова.

## Відповідь {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Стовпець | Опис |
| --- | --- |
| `name` | Ім'я поля |
| `type` | Тип даних (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Опис поля (якщо вказано `EXTENDED`) |

</TabItem>
<TabItem value="describe-method">

| Стовпець | Опис |
| --- | --- |
| `name` | Ім'я поля |
| `type` | Тип даних (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` або `output` |
| `shape` | Підмножина JSON Schema для полів з об'єктами й масивами; порожня для скалярних полів. Містить вкладені `properties`, `items`, `required`, `enum`, `default`, `description`, а також, якщо провайдер їх надає, логічні значення (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Опис поля (якщо вказано `EXTENDED`) |

</TabItem>
</Tabs>

* * *

## Приклади {#examples}

### Базова інструкція `DESCRIBE` {#basic-describe-statement}
Виконайте базову інструкцію `DESCRIBE`, щоб вивести список полів ресурсу з автентифікованого сеансу.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Розширена інструкція `DESCRIBE` {#extended-describe-statement}
Виконайте розширену інструкцію `DESCRIBE`, щоб вивести список полів ресурсу та їхні описи з автентифікованого сеансу.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Псевдонім `DESC` {#desc-alias}
`DESC` - скорочений запис `DESCRIBE` для ресурсу, з `EXTENDED` або без нього.  Його вважають інструкцією лише для читання скрізь, де такою вважають `DESCRIBE`, зокрема в інструменті `run_select_query` сервера MCP у режимах `read_only` і `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Базова інструкція `DESCRIBE METHOD` {#basic-describe-method-statement}
Отримайте опис окремого методу ресурсу. Результат містить вхідні параметри й поля відповіді, а вкладену структуру відображено в стовпці `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Розширена інструкція `DESCRIBE METHOD` {#extended-describe-method-statement}
Виконайте розширену інструкцію `DESCRIBE METHOD`, щоб разом із формою отримати опис кожного поля.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Визначення корисного навантаження для `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` - канонічний спосіб дізнатися, які поля приймає `INSERT` (або будь-який метод, що змінює дані). Стовпець `shape` містить вкладену структуру, за якою агенти й люди можуть сформувати корисне навантаження за один запит.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
