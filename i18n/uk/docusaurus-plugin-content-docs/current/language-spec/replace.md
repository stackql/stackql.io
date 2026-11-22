---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "REPLACE у StackQL: заміна всіх полів екземпляра ресурсу, аналог запиту HTTP PUT, на відміну від часткового оновлення, яке виконує UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Оновлює всі поля ресурсу (рівнозначно операції `put`).

Див. також:  
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

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

## Приклади {#examples}

### Базова інструкція `REPLACE` {#basic-replace-statement}
Виконайте інструкцію `REPLACE`, щоб замінити всі теми репозиторію GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Інструкція `REPLACE` (блокувальна) {#replace-statement-blocking}
Виконайте інструкцію `REPLACE`, щоб замінити правило брандмауера Compute Engine. Це блокувальний (синхронний) виклик.

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

### Інструкція `REPLACE` з функціями JSON {#replace-statement-with-json-functions}
Виконайте інструкцію `REPLACE`, значення якої сформовано за допомогою функцій JSON. Функцію, яка не читає жодного стовпця, обчислюють до надсилання запиту. Значення в `SET` не може читати рядок, який замінюється, тому, щоб змінити частину наявного значення, спочатку прочитайте його за допомогою `SELECT`, а потім установіть змінене значення.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
