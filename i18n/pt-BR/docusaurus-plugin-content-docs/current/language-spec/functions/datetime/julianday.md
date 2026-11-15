---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função JULIANDAY no StackQL: o número fracionário de dias desde o meio-dia de 24 de novembro de 4714 a.C. para um valor de tempo, útil em aritmética de datas."
image: "/img/stackql-featured-image.png"
---
Retorna o dia juliano - o número de dias desde o meio-dia em Greenwich de 24 de novembro de 4714 a.C. [(calendário gregoriano proléptico)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de data ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*datetime_expression*__  
O valor de data e hora.

__*modifier*__  
Zero ou mais modificadores a serem aplicados à *datetime_expression*.  Para obter mais informações sobre modificadores, consulte [**Modificadores de data**](/language-spec/functions/datetime/date_modifiers)

## Valor(es) retornado(s) {#return-values}

Retorna o [dia juliano](https://en.wikipedia.org/wiki/Julian_day) da *datetime_expression* de entrada com o *modifier* aplicado: o número de dias, incluindo a fração correspondente à hora do dia, desde o meio-dia em Greenwich de 24 de novembro de 4714 a.C. no [calendário gregoriano proléptico](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Exemplos {#examples}

### Calcular os dias entre duas datas {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Use a função [**ROUND**](/language-spec/functions/math/round) para converter números de ponto flutuante no inteiro mais próximo.

:::

Para obter mais informações, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
