---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление остатка от деления одного числа на другое с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет остаток от деления *x* на *y* и возвращает результат операции деления по модулю. Это похоже на использование оператора '%', но работает и с нецелыми аргументами.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*x*__  
Делимое, числовое выражение.

__*y*__  
Делитель, также числовое выражение.

## Возвращаемое значение {#return-values}
Возвращает остаток от деления *x* на *y*.

* * *

## Примеры {#examples}

### Вычисление остатка от деления 10 на 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
