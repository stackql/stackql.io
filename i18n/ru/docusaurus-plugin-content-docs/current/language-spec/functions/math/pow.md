---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Возведение одного числа в степень другого с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет *x* в степени *y*.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*x*__  
Основание степени, числовое выражение.

__*y*__  
Показатель степени, в которую возводится основание *x*, также числовое выражение.

## Возвращаемое значение {#return-values}
Возвращает результат возведения *x* в степень *y*.

* * *

## Примеры {#examples}

### Вычисление x в степени y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
