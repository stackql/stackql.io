---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função TIME no StackQL: retornar uma hora como string HH:MM:SS a partir de um valor de tempo e de modificadores opcionais."
image: "/img/stackql-featured-image.png"
---
Retorna uma string de hora no formato HH:MM:SS.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de data ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
O valor de data e hora.

__*modifier*__  
Zero ou mais modificadores a serem aplicados à *datetime_expression*.  Para obter mais informações sobre modificadores, consulte [**Modificadores de data**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) retornado(s) {#return-values}

Retorna uma string que representa o componente de hora da *datetime_expression* de entrada com o *modifier* aplicado.

* * *

## Exemplos {#examples}

### Retornar o componente de hora de um valor de data e hora {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Retornar o componente de hora local de um valor de data e hora {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
