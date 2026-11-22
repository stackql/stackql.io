---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление синуса угла, заданного в радианах, с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет синус указанного угла, заданного в радианах.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*radian_value*__  
Числовое выражение, задающее угол в радианах, для которого вычисляется синус.

## Возвращаемое значение {#return-values}
Возвращает синус указанного угла.

* * *

## Примеры {#examples}

### Вычисление синуса π/2 радиан {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Этот запрос возвращает `1`, так как синус π/2 радиан равен 1.

### Вычисление синуса π радиан {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Этот запрос вычисляет синус π радиан и возвращает примерно `0`, так как синус π равен 0.
