---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - змінні сеансу
  - identifier
description: "Функція IDENTIFIER у StackQL: перетворює змінну сеансу з ім'ям стовпця на посилання на стовпець, щоб змінні могли вказувати стовпці в проєкціях і ORDER BY."
image: "/img/stackql-featured-image.png"
---
Перетворює рядкове значення, зазвичай змінну сеансу, на ім'я стовпця.  Використовуйте її там, де окрему змінну `$variable` інакше було б прочитано як скалярне значення.  Доступно в StackQL `v1.0` і новіших версіях.

Див. також:  
[[` SET `]](/language-spec/set) [[ Змінні сеансу ]](/getting-started/session-variables)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Аргументи {#arguments}

__*variable*__  
Змінна сеансу або рядковий літерал, що містить ім'я стовпця.  Допускається повне ім'я, наприклад `t.name`.  Функція приймає рівно один аргумент, і це має бути одне рядкове значення; конкатенацію, як-от `'t.' || $column_name`, другий аргумент або числове значення StackQL відхиляє з помилкою `IDENTIFIER requires one string value`.  Порожнє значення або значення, що містить інструкцію, StackQL відхиляє з помилкою `invalid identifier value`, а невизначена змінна призводить до помилки про невизначену змінну.  Усі ці перевірки виконуються до надсилання будь-якого запиту до провайдера.

## Значення, що повертається {#return-values}

Після синтаксичного аналізу інструкції функцію замінено посиланням на вказаний стовпець, тому вона повертає значення цього стовпця.  Імена перетворюються на ідентифікатори й ніколи не вставляються в текст SQL.  Отримане посилання на стовпець можна використовувати всюди, де можна використовувати стовпець: у списку вибірки, у реченнях `WHERE`, `GROUP BY` і `ORDER BY`, в аргументах функцій, а також усередині CTE і підзапитів.

* * *

## Приклади {#examples}

### Проєкція та сортування за стовпцем, ім'я якого вказано в змінній {#project-and-order-by-a-column-named-in-a-variable}

І стовпець, який потрібно повернути, і стовпець для сортування беруться з `column_name`, а вихідний стовпець отримує нове ім'я з `output_name`.  Функція `IDENTIFIER` потрібна в списку вибірки та в `ORDER BY`, бо окрема змінна `$column_name` у цих позиціях була б рядковим значенням `'name'`, а не стовпцем.  Для псевдоніма натомість використано форму в подвійних лапках.  Після визначення змінних інструкція збігається з `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Фільтрування за стовпцем, ім'я якого вказано в змінній {#filter-on-a-column-named-in-a-variable}

Визначене посилання працює і в предикаті, і в проєкції.  Тут і поле для фільтрування, і значення для порівняння беруться зі змінних: `IDENTIFIER($filter_column)` стає стовпцем `status`, а `$filter_value` передається як рядковий літерал `'RUNNING'`.  Ці дві форми навмисно різні, бо перша називає стовпець, а друга надає значення.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Уточнення стовпця псевдонімом таблиці {#qualify-the-column-with-a-table-alias}

Значення з крапкою відповідає посиланню на стовпець із повним ім'ям.  Таблиця має псевдонім `w`, сам псевдонім береться зі змінної, а `IDENTIFIER($column_name)` стає `w.name`.  Цю форму слід використовувати, коли стовпець із тим самим ім'ям є з обох боків з'єднання і посилання інакше було б неоднозначним.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Відхилені значення {#rejected-values}

Оскільки значення стає ім'ям, а не текстом запиту, усе, що не є придатним ім'ям, StackQL відхиляє ще до планування інструкції, і жоден запит не доходить до провайдера.  Перші дві інструкції нижче завершуються помилкою `invalid identifier value`, а третя - помилкою `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Змінна `$variable` у реченні `FROM` відповідає відношенню й без `IDENTIFIER`, а `"$variable"` у подвійних лапках використовується для псевдонімів, імен стовпців DDL, списків стовпців `INSERT` і цілей `UPDATE`.
