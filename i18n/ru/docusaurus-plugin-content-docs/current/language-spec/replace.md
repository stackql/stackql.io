---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "REPLACE в StackQL: замена всех полей экземпляра ресурса, аналог HTTP PUT, в отличие от частичного обновления, которое выполняет UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Обновляет все поля ресурса (аналог операции `put`).

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Синтаксис {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Примеры {#examples}

### Базовая инструкция `REPLACE` {#basic-replace-statement}
Выполните инструкцию `REPLACE`, чтобы заменить все темы репозитория GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Инструкция `REPLACE` (блокирующая) {#replace-statement-blocking}
Выполните инструкцию `REPLACE`, чтобы заменить правило брандмауэра Compute Engine. Это блокирующий (синхронный) вызов.

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Инструкция `REPLACE` с JSON-функциями {#replace-statement-with-json-functions}
Выполните инструкцию `REPLACE`, значение в которой строится с помощью JSON-функций. Функция, которая не читает столбцы, вычисляется до отправки запроса к API. Значение в предложении `SET` не может читать заменяемую строку, поэтому, чтобы изменить часть существующего значения, сначала прочитайте его инструкцией `SELECT`, а затем присвойте измененное значение.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
