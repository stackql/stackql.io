---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função DATE no StackQL: retornar uma data como string YYYY-MM-DD a partir de um valor de tempo e de modificadores opcionais."
image: "/img/stackql-featured-image.png"
---
Retorna uma string de data no formato YYYY-MM-DD.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de data ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
O valor de data e hora.

__*modifier*__  
Zero ou mais modificadores a serem aplicados à *datetime_expression*.  Para obter mais informações sobre modificadores, consulte [**Modificadores de data**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) retornado(s) {#return-values}

Retorna uma string que representa o componente de data da *datetime_expression* de entrada com o *modifier* aplicado.

* * *

## Exemplos {#examples}

### Retornar o componente de data de um valor de data e hora {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Retornar o componente de data local de um valor de data e hora {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
