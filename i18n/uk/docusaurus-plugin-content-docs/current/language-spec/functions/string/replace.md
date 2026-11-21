---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція REPLACE у StackQL: заміна кожного входження підрядка в рядку іншим рядком."
image: "/img/stackql-featured-image.png"
---
Повертає копію рядка, у якій кожне входження підрядка замінено іншим рядком.

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*character_expression*__  
Рядковий літерал або рядковий стовпець, у якому потрібно виконати заміну.

__*pattern*__  
Підрядок, який потрібно знайти в початковому рядку.

> Якщо *pattern* є порожнім рядком або його не знайдено в *character_expression*, то *character_expression* повертається без змін. 

__*replacement*__  
Рядок, яким виконується заміна.

> Якщо *replacement* спочатку не є рядком, перед обробкою його приводять до рядка UTF-8.

> Для порівнянь використовується порядок зіставлення `BINARY`.

## Значення, що повертається {#return-values}
Повертає рядок.

* * *

## Приклади {#examples}

### Заміна шаблону рядка в стовпці {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Докладніше див. [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
