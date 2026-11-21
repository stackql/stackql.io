---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Використання регулярних виразів для перевірки рядків на відповідність шаблонам за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Перевіряє, чи відповідає вихідний рядок вказаному шаблону регулярного виразу.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*source_string*__  
Рядок, у якому потрібно шукати шаблон.

__*pattern_expression*__  
Шаблон регулярного виразу, на відповідність якому перевіряється вихідний рядок.

## Значення, що повертається {#return-values}
Повертає `1`, якщо вихідний рядок відповідає шаблону регулярного виразу, інакше `0`.

* * *

## Приклади {#examples}

### Перевірка наявності чисел у рядку {#check-if-a-string-contains-numbers}

Цей приклад показує, як перевірити, чи містить рядок 'the year is 2021' один або більше цифрових символів:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Цей запит повертає `1`, оскільки рядок містить підрядок '2021', який відповідає шаблону `[0-9]+`, що означає одну або більше цифр.

### Перевірка формату адреси електронної пошти {#validate-email-format}

Щоб перевірити, чи має рядок допустимий формат адреси електронної пошти:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Цей запит повертає `1`: рядок 'user@example.com' має формат типової адреси електронної пошти й відповідає вказаному шаблону регулярного виразу.
