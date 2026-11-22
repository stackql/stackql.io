---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція TRUNC у StackQL: ціла частина числа з округленням у бік нуля й відкиданням дробової частини."
image: "/img/stackql-featured-image.png"
---

Повертає цілу частину числа, округлюючи його в бік нуля й відкидаючи будь-яку дробову частину.

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз із категорії точних або наближених числових типів даних.

## Значення, що повертається {#return-values}
Повертає числове значення, усічене до цілої частини.

* * *

## Приклади {#examples}

### Усічення кількості днів від створення бакетів Google Cloud Storage до цілих днів {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
