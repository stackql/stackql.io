---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция TRUNC в StackQL: целая часть числа с округлением в сторону нуля и отбрасыванием дробной части."
image: "/img/stackql-featured-image.png"
---

Возвращает целую часть числа, округляя его в сторону нуля и отбрасывая дробную часть.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*numeric_expression*__  
Выражение, относящееся к категории точных или приближенных числовых типов данных.

## Возвращаемое значение {#return-values}
Возвращает числовое значение, усеченное до целой части.

* * *

## Примеры {#examples}

### Усечение числа дней с момента создания бакетов Google Cloud Storage до целых дней {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
