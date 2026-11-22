---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Преобразование угла из радианов в градусы с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Преобразует указанный угол из радианов в градусы.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*angle_in_radians*__  
Числовое выражение, представляющее угол в радианах.

## Возвращаемое значение {#return-values}
Возвращает величину этого угла в градусах.

* * *

## Примеры {#examples}

### Преобразование π радиан в градусы {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Этот запрос возвращает примерно `180`, так как при преобразовании π радиан (примерно `3.14159`) получается около 180 градусов.

### Преобразование π/2 радиан в градусы {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Этот запрос преобразует π/2 радиан (примерно `1.5708`) в градусы и возвращает около `90`, поскольку π/2 радиан соответствует углу в 90 градусов.
