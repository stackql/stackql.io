---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Вычисление тангенса угла, заданного в радианах, с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Вычисляет тангенс указанного угла, заданного в радианах.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*radian_value*__  
Числовое выражение, задающее угол в радианах, для которого вычисляется тангенс.

## Возвращаемое значение {#return-values}
Возвращает тангенс указанного угла.

* * *

## Примеры {#examples}

### Вычисление тангенса π/4 радиан {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Этот запрос возвращает `1`, так как тангенс π/4 радиан равен 1.

### Вычисление тангенса π радиан {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Этот запрос вычисляет тангенс π радиан и возвращает примерно `0`, поскольку тангенс π очень близок к 0.
