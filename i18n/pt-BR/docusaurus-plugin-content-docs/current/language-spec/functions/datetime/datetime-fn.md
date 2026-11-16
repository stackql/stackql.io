---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função DATETIME no StackQL: calcular um valor de data e hora a partir de uma entrada e de um ou mais modificadores, retornado como YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Retorna um valor de data e hora calculado a partir de um valor de data e hora de entrada e de um ou mais modificadores.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de data ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
O valor de data e hora.

__*modifier*__  
Zero ou mais modificadores a serem aplicados à *datetime_expression*.  Para obter mais informações sobre modificadores, consulte [**Modificadores de data**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) retornado(s) {#return-values}

Retorna uma string que representa o valor de data e hora da *datetime_expression* de entrada com o *modifier* aplicado.

* * *

## Exemplos {#examples}

### Converter UTC em hora local com o modificador `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Converter um valor de data e hora em UTC {#convert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### Calcular uma data relativa a outra data {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obter o próximo dia da semana especificado após uma data {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obter o início do mês de uma data {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Converter um carimbo de data/hora Unix em um valor de data e hora {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Para converter um valor de data e hora em um carimbo de data/hora de época Unix, consulte [**STRFTIME**](/language-spec/functions/datetime/strftime).  Para calcular diferenças entre dois objetos de data e hora, consulte [**STRFTIME**](/language-spec/functions/datetime/strftime) ou [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Para obter mais informações, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
