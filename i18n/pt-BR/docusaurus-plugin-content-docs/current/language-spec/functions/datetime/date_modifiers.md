---
title: Modificadores de data
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Modificadores de data e hora no StackQL: deslocar resultados de DATE, TIME, DATETIME, JULIANDAY e STRFTIME em dias, meses ou anos, ao início de um período, a um dia da semana etc."
image: "/img/stackql-featured-image.png"
---

## Visão geral {#overview}

Nas funções de data e hora do StackQL, um valor de data ou hora pode ser seguido de zero ou mais modificadores que alteram a data, a hora ou ambas. Cada modificador é uma transformação aplicada ao valor de tempo à sua esquerda. Os modificadores são aplicados em ordem, da esquerda para a direita. Os modificadores disponíveis são os seguintes:

| Modificador          | Descrição                                                                    |
|----------------------|------------------------------------------------------------------------------|
| NNN day(s)           | Adicionar ±NNN dias a uma data e hora                                        |
| NNN hour(s)          | Adicionar ±NNN horas a uma data e hora                                       |
| NNN minute(s)        | Adicionar ±NNN minutos a uma data e hora                                     |
| NNN[.NNNN] second(s) | Adicionar ±NNN segundos a uma data e hora ou subtraí-los dela                |
| NNN month(s)         | Adicionar ±NNN meses a uma data e hora                                       |
| NNN year(s)          | Adicionar ±NNN anos a uma data e hora                                        |
| start of month       | Retroceder ao início do mês                                                  |
| start of year        | Retroceder ao início do ano                                                  |
| start of day         | Retroceder ao início do dia                                                  |
| weekday N            | Avançar uma data até a próxima data cujo número do dia da semana seja N; uma data que já cai no dia da semana N é retornada sem alteração |
| localtime            | Retornar a hora local                                                        |
| utc                  | Retornar a hora UTC                                                          |
| unixepoch            | Tempo Unix                                                                   |

## Exemplos {#examples}

### Retornar a data e a hora atuais com o valor de tempo `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Converter UTC em hora local com o modificador `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Converter uma hora local em UTC {#convert-datetime-to-utc}

O modificador `utc` trata a entrada como uma hora local na máquina que executa o StackQL e a converte em UTC.  Um valor que já está em UTC, como `'now'` ou um carimbo de data/hora com o sufixo `Z`, é retornado sem alterações.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

Em uma máquina cujo fuso horário é UTC+11, esta consulta retorna `2026-11-20 06:59:24`.

### Calcular uma data relativa a outra data {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Obter o dia da semana especificado em uma data ou após ela {#get-the-next-specified-weekday-after-a-given-date}

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
