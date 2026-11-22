---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - переменные сеанса
  - identifier
description: "Функция IDENTIFIER в StackQL: преобразует переменную сеанса с именем столбца в ссылку на столбец, чтобы переменные могли задавать столбцы в проекциях и ORDER BY."
image: "/img/stackql-featured-image.png"
---
Преобразует строковое значение, обычно переменную сеанса, в имя столбца.  Используйте ее там, где переменная `$variable` сама по себе была бы прочитана как скалярное значение.  Доступно в StackQL `v1.0` и более поздних версиях.

См. также:  
[[` SET `]](/language-spec/set) [[ Переменные сеанса ]](/getting-started/session-variables)

* * * 

## Синтаксис {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Аргументы {#arguments}

__*variable*__  
Переменная сеанса или строковый литерал, содержащие имя столбца.  Допускается полное имя, например `t.name`.  Принимается ровно один аргумент, и он должен быть одним строковым значением; конкатенация, например `'t.' || $column_name`, второй аргумент или числовое значение отклоняются с ошибкой `IDENTIFIER requires one string value`.  Пустое значение или значение, содержащее инструкцию, отклоняется с ошибкой `invalid identifier value`, а для неопределенной переменной возвращается ошибка о том, что она не определена.  Все эти проверки выполняются до любого запроса к провайдеру.

## Возвращаемое значение {#return-values}

В инструкции после синтаксического анализа функция заменяется ссылкой на указанный столбец, поэтому она возвращает значение этого столбца.  Имена преобразуются в идентификаторы и никогда не вставляются в текст SQL.  Полученную ссылку на столбец можно использовать везде, где допустим столбец: в списке выборки, в предложениях `WHERE`, `GROUP BY` и `ORDER BY`, в аргументах функций, а также внутри CTE и вложенных запросов.

* * *

## Примеры {#examples}

### Проекция и сортировка по столбцу, имя которого задано в переменной {#project-and-order-by-a-column-named-in-a-variable}

И возвращаемый столбец, и столбец для сортировки берутся из `column_name`, а имя выходного столбца берется из `output_name`.  Функция `IDENTIFIER` нужна в списке выборки и в `ORDER BY`, потому что `$column_name` сам по себе в этих позициях был бы строкой `'name'`, а не столбцом.  Для псевдонима вместо этого используется форма в двойных кавычках.  После определения значений переменных инструкция совпадает с `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Фильтрация по столбцу, имя которого задано в переменной {#filter-on-a-column-named-in-a-variable}

Полученная ссылка работает не только в проекции, но и в предикате.  Здесь и поле для фильтрации, и значение для сравнения берутся из переменных: `IDENTIFIER($filter_column)` становится столбцом `status`, а `$filter_value` передается как строковый литерал `'RUNNING'`.  Эти две формы намеренно различаются: первая задает имя столбца, а вторая - значение.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Уточнение столбца псевдонимом таблицы {#qualify-the-column-with-a-table-alias}

Значение с точкой соответствует полной ссылке на столбец.  Таблице назначен псевдоним `w`, причем сам псевдоним берется из переменной, а `IDENTIFIER($column_name)` становится `w.name`.  Эту форму следует использовать, когда столбец с одним и тем же именем есть по обе стороны соединения и ссылка иначе была бы неоднозначной.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Отклоняемые значения {#rejected-values}

Поскольку значение становится именем, а не текстом запроса, любое значение, которое не является допустимым именем, отклоняется до планирования инструкции, и ни один запрос не доходит до провайдера.  Первые две инструкции ниже завершаются ошибкой `invalid identifier value`, а третья - ошибкой `IDENTIFIER requires one string value`.

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

> Переменная `$variable` в предложении `FROM` соответствует отношению и без `IDENTIFIER`, а форма в двойных кавычках `"$variable"` используется для псевдонимов, имен столбцов в DDL, списков столбцов `INSERT` и целей `UPDATE`.
