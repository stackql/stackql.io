---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція INSTR у StackQL: позиція першого входження одного рядка в інший з відліком від одиниці або 0, якщо його не знайдено."
image: "/img/stackql-featured-image.png"
---
Повертає позицію символу першого входження рядка в інший рядок або 0, якщо шуканий рядок не знайдено.

Див. також:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*string*__  
Вихідний рядок, у якому виконується пошук.

__*searchstring*__  
Підрядок, який потрібно знайти.

> Якщо *string* і *searchstring* обидва є значеннями BLOB, то `instr(<string>, <searchstring>)` повертає кількість байтів перед першим входженням *searchstring*, збільшену на одиницю, або 0, якщо *searchstring* не трапляється ніде в *string*. Якщо обидва аргументи не є `NULL` і не є значеннями BLOB, то обидва інтерпретуються як рядки. 

## Значення, що повертається {#return-values}
Повертає ціле число.

> Якщо значення *string* або *searchstring* дорівнює `NULL`, то результатом є `NULL`.

* * *

## Приклади {#examples}

### Пошук рядка в полі {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
