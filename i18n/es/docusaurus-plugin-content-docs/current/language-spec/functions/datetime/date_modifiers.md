---
title: Modificadores de fecha
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Modificadores de fecha y hora en StackQL: desplazar los resultados de DATE, TIME, DATETIME, JULIANDAY y STRFTIME en días, meses o años, al inicio de un período, a un día de la semana y más."
image: "/img/stackql-featured-image.png"
---

## Descripción general {#overview}

Los valores de fecha u hora de las funciones de fecha y hora de StackQL pueden ir seguidos de cero o más modificadores que alteran la fecha, la hora o ambas. Cada modificador es una transformación que se aplica al valor de tiempo situado a su izquierda. Los modificadores se aplican en orden, de izquierda a derecha. Los modificadores disponibles son los siguientes:

| Modificador          | Descripción                                                                      |
|----------------------|----------------------------------------------------------------------------------|
| NNN day(s)           | Sumar ±NNN días a una fecha y hora                                               |
| NNN hour(s)          | Sumar ±NNN horas a una fecha y hora                                              |
| NNN minute(s)        | Sumar ±NNN minutos a una fecha y hora                                            |
| NNN[.NNNN] second(s) | Sumar ±NNN segundos a una fecha y hora o restarlos de ella                       |
| NNN month(s)         | Sumar ±NNN meses a una fecha y hora                                              |
| NNN year(s)          | Sumar ±NNN años a una fecha y hora                                               |
| start of month       | Retroceder al inicio del mes                                                     |
| start of year        | Retroceder al inicio del año                                                     |
| start of day         | Retroceder al inicio del día                                                     |
| weekday N            | Avanzar una fecha hasta la siguiente fecha cuyo número de día de la semana sea N |
| localtime            | Devolver la hora local                                                           |
| utc                  | Devolver la hora UTC                                                             |
| unixepoch            | Tiempo Unix                                                                      |

## Ejemplos {#examples}

### Devolver la fecha y hora actuales con el valor de tiempo `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir UTC a hora local con el modificador `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convertir una hora local a UTC {#convert-datetime-to-utc}

El modificador `utc` trata su entrada como una hora local del equipo que ejecuta StackQL y la convierte a UTC.  Un valor que ya está en UTC, como `'now'` o una marca de tiempo con el sufijo `Z`, se devuelve sin cambios.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

En un equipo cuya zona horaria es UTC+11, esta consulta devuelve `2026-11-20 06:59:24`.

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
