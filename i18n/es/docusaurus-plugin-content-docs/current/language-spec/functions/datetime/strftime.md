---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "Función STRFTIME en StackQL: dar formato a un valor de fecha y hora con una cadena de formato, mediante las mismas sustituciones que la función strftime de C."
image: "/img/stackql-featured-image.png"
---
Devuelve un valor de fecha y hora con formato según un formato especificado.  

Consulte también:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de fecha ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*format*__  
El formato deseado para el valor de fecha y hora especificado por el valor de *datetime_expression*.  Consulte la tabla de formatos a continuación.

__*datetime_expression*__  
El valor de fecha u hora al que se va a dar formato.

__*modifier*__  
Cero o más modificadores que se aplican a *datetime_expression*.  Para obtener más información sobre los modificadores, consulte [**Modificadores de fecha**](/language-spec/functions/datetime/date_modifiers)

### Formatos {#formats}

| Formato | Descripción                          |
|---------|--------------------------------------|
| %d      | día del mes: 01-31                   |
| %f      | segundos fraccionarios: SS.SSS       |
| %H      | hora: 00-24                          |
| %j      | día del año: 001-366                 |
| %J      | número de día juliano                |
| %m      | mes: 01-12                           |
| %M      | minuto: 00-59                        |
| %s      | segundos desde 1970-01-01            |
| %S      | segundos: 00-59                      |
| %w      | día de la semana 0-6, con domingo==0 |
| %W      | semana del año: 00-53                |
| %Y      | año: 0000-9999                       |
| %%      | escapar un símbolo de porcentaje: %  |

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena con formato que representa el formato de fecha y hora deseado.

* * *

## Ejemplos {#examples}

### Devolver una fecha en formato estadounidense {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Convertir un valor de fecha y hora en una marca de tiempo de época {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Calcular los días entre dos fechas {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obtener más información, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
