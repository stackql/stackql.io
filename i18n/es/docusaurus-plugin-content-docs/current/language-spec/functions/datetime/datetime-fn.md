---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función DATETIME en StackQL: calcular un valor de fecha y hora a partir de una entrada y uno o más modificadores, devuelto como YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Devuelve un valor de fecha y hora calculado a partir de una fecha y hora de entrada y uno o más modificadores.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de fecha ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
El valor de fecha y hora.

__*modifier*__  
Cero o más modificadores que se aplican a *datetime_expression*.  Para obtener más información sobre los modificadores, consulte [**Modificadores de fecha**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena que representa la fecha y hora de la expresión *datetime_expression* de entrada con el *modifier* aplicado.

* * *

## Ejemplos {#examples}

### Convertir UTC a hora local con el modificador `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir una fecha y hora a UTC {#convert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### Calcular una fecha relativa a otra fecha {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obtener el siguiente día de la semana especificado posterior a una fecha dada {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obtener el inicio del mes de una fecha dada {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir una marca de tiempo de Unix en un valor de fecha y hora {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Para convertir un valor de fecha y hora en una marca de tiempo de época Unix, consulte [**STRFTIME**](/language-spec/functions/datetime/strftime).  Para calcular diferencias entre dos objetos de fecha y hora, consulte [**STRFTIME**](/language-spec/functions/datetime/strftime) o [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Para obtener más información, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
