---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función JULIANDAY en StackQL: el número fraccionario de días desde el mediodía del 24 de noviembre de 4714 a. C. para un valor de tiempo, útil para la aritmética de fechas."
image: "/img/stackql-featured-image.png"
---
Devuelve el día juliano - el número de días transcurridos desde el mediodía en Greenwich del 24 de noviembre de 4714 a. C. [(calendario gregoriano proléptico)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de fecha ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
El valor de fecha y hora.

__*modifier*__  
Cero o más modificadores que se aplican a *datetime_expression*.  Para obtener más información sobre los modificadores, consulte [**Modificadores de fecha**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) devuelto(s) {#return-values}

Devuelve el [día juliano](https://en.wikipedia.org/wiki/Julian_day) de la expresión *datetime_expression* de entrada con el *modifier* aplicado: el número de días, incluida la fracción correspondiente a la hora del día, transcurridos desde el mediodía en Greenwich del 24 de noviembre de 4714 a. C. en el [calendario gregoriano proléptico](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Ejemplos {#examples}

### Calcular los días entre dos fechas {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Use la función [**ROUND**](/language-spec/functions/math/round) para convertir números de punto flotante al entero más cercano.

:::

Para obtener más información, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
