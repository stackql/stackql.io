---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Получение математической константы π (пи) с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Возвращает математическую константу π (пи) - отношение длины окружности к ее диаметру.

См. также:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Аргументы {#arguments}

Эта функция не требует аргументов.

## Возвращаемое значение {#return-values}
Возвращает константу π, приблизительно равную 3.14159.

* * *

## Примеры {#examples}

### Получение значения π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Этот запрос выводит значение π, примерно равное `3.14159`, которое полезно в различных математических вычислениях, связанных с окружностями, и в других геометрических функциях.
