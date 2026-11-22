---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление косинуса угла, заданного в радианах, с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет косинус указанного угла, заданного в радианах.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*radian_value*__  
Числовое выражение, задающее угол в радианах, для которого вычисляется косинус.

## Возвращаемое значение {#return-values}
Возвращает косинус указанного угла.

* * *

## Примеры {#examples}

### Вычисление косинуса π/3 радиан {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Этот запрос возвращает `0.5000000000000001` - приближенное значение с плавающей запятой для косинуса π/3 радиан, который равен 0.5.

### Вычисление косинуса π радиан {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Этот запрос вычисляет косинус π радиан и возвращает `-1`, поскольку косинус π равен -1.
