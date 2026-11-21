---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Перетворення кута з градусів у радіани за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Перетворює вказаний кут із градусів у радіани.

Див. також:  
[[`SELECT`]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*angle_in_degrees*__  
Числовий вираз, що визначає кут у градусах.

## Значення, що повертається {#return-values}
Повертає еквівалентне значення кута в радіанах.

* * *

## Приклади {#examples}

### Перетворення 180 градусів у радіани {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Цей запит повертає π (приблизно `3.14159`), оскільки перетворення 180 градусів у радіани дає π радіан.

### Перетворення 90 градусів у радіани {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Цей запит перетворює 90 градусів у радіани й повертає π/2, або приблизно `1.5708`, оскільки 90 градусів - це половина від 180 градусів.
