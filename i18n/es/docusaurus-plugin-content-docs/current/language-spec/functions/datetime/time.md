---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función TIME en StackQL: devolver una hora como cadena HH:MM:SS a partir de un valor de tiempo y modificadores opcionales."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena de hora con el formato HH:MM:SS.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de fecha ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
El valor de fecha y hora.

__*modifier*__  
Cero o más modificadores que se aplican a *datetime_expression*.  Para obtener más información sobre los modificadores, consulte [**Modificadores de fecha**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena que representa el componente de hora de la expresión *datetime_expression* de entrada con el *modifier* aplicado.

* * *

## Ejemplos {#examples}

### Devolver el componente de hora de un valor de fecha y hora {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Devolver el componente de hora local de un valor de fecha y hora {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
