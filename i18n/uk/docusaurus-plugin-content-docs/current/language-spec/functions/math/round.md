---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція ROUND у StackQL: округлює числовий аргумент до вказаної кількості знаків після коми, за замовчуванням до нуля."
image: "/img/stackql-featured-image.png"
---
Округлює числовий аргумент.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз із категорії точних або наближених числових типів даних.

__*precision*__  
Кількість цифр праворуч від десяткової коми, до якої потрібно округлити *numeric_expression*.

> Якщо аргумент *precision* не вказано, вважається, що він дорівнює 0.

## Значення, що повертається {#return-values}
Повертає числове значення.

* * *

## Приклади {#examples}

### Округлення числа з рухомою комою до двох знаків після коми {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Докладніше див. [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
