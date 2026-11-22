---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Функция INSTR в StackQL: позиция первого вхождения одной строки в другую с отсчетом от единицы или 0, если строка не найдена."
image: "/img/stackql-featured-image.png"
---
Возвращает позицию символа, с которого начинается первое вхождение строки в другую строку, или 0, если искомая строка не найдена.

См. также:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*string*__  
Исходная строка, в которой выполняется поиск.

__*searchstring*__  
Искомая подстрока.

> Если и *string*, и *searchstring* являются значениями BLOB, то `instr(<string>, <searchstring>)` возвращает число байтов перед первым вхождением *searchstring*, увеличенное на единицу, или 0, если *searchstring* нигде не встречается в *string*. Если оба аргумента отличны от `NULL` и не являются значениями BLOB, то оба интерпретируются как строки. 

## Возвращаемое значение {#return-values}
Возвращает целое число.

> Если значение *string* или *searchstring* равно `NULL`, то результат равен `NULL`.

* * *

## Примеры {#examples}

### Поиск строки в поле {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

Подробнее см. [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
