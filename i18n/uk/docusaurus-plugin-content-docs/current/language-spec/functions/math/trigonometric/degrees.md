---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Перетворення кута з радіанів у градуси за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Перетворює вказаний кут із радіанів у градуси.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*angle_in_radians*__  
Числовий вираз, що визначає кут у радіанах.

## Значення, що повертається {#return-values}
Повертає еквівалентне значення кута в градусах.

* * *

## Приклади {#examples}

### Перетворення π радіан у градуси {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Цей запит повертає приблизно `180`, оскільки перетворення π радіан (приблизно `3.14159`) дає приблизно 180 градусів.

### Перетворення π/2 радіана в градуси {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Цей запит перетворює π/2 радіана (приблизно `1.5708`) у градуси й повертає приблизно `90`, оскільки π/2 радіана еквівалентно куту 90 градусів.
