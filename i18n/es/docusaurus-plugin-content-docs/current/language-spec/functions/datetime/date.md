---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función DATE en StackQL: devolver una fecha como cadena YYYY-MM-DD a partir de un valor de tiempo y modificadores opcionales."
image: "/img/stackql-featured-image.png"
---
Devuelve una cadena de fecha con el formato YYYY-MM-DD.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de fecha ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
El valor de fecha y hora.

__*modifier*__  
Cero o más modificadores que se aplican a *datetime_expression*.  Para obtener más información sobre los modificadores, consulte [**Modificadores de fecha**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena que representa el componente de fecha de la expresión *datetime_expression* de entrada con el *modifier* aplicado.

* * *

## Ejemplos {#examples}

### Devolver el componente de fecha de un valor de fecha y hora {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Devolver el componente de fecha local de un valor de fecha y hora {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
