---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Преобразование угла из градусов в радианы с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Преобразует указанный угол из градусов в радианы.

См. также:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*angle_in_degrees*__  
Числовое выражение, представляющее угол в градусах.

## Возвращаемое значение {#return-values}
Возвращает величину этого угла в радианах.

* * *

## Примеры {#examples}

### Преобразование 180 градусов в радианы {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Этот запрос возвращает π (примерно `3.14159`), так как при преобразовании 180 градусов в радианы получается π радиан.

### Преобразование 90 градусов в радианы {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Этот запрос преобразует 90 градусов в радианы и возвращает π/2, то есть примерно `1.5708`, поскольку 90 градусов - это половина от 180 градусов.
