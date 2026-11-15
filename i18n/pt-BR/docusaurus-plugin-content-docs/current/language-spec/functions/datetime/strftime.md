---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função STRFTIME no StackQL: formatar um valor de data e hora com uma string de formato, usando as mesmas substituições da função strftime do C."
image: "/img/stackql-featured-image.png"
---
Retorna um valor de data e hora formatado com base em um formato especificado.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Modificadores de data ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*format*__  
O formato desejado para o valor de data e hora especificado pelo valor *datetime_expression*.  Consulte a tabela de formatos abaixo.

__*datetime_expression*__  
O valor de data ou hora a ser formatado.

__*modifier*__  
Zero ou mais modificadores a serem aplicados à *datetime_expression*.  Para obter mais informações sobre modificadores, consulte [**Modificadores de data**](/language-spec/functions/datetime/date_modifiers)

### Formatos {#formats}

| Formato | Descrição                            |
|---------|--------------------------------------|
| %d      | dia do mês: 01-31                    |
| %f      | segundos fracionários: SS.SSS        |
| %H      | hora: 00-24                          |
| %j      | dia do ano: 001-366                  |
| %J      | número do dia juliano                |
| %m      | mês: 01-12                           |
| %M      | minuto: 00-59                        |
| %s      | segundos desde 1970-01-01            |
| %S      | segundos: 00-59                      |
| %w      | dia da semana 0-6 com domingo==0     |
| %W      | semana do ano: 00-53                 |
| %Y      | ano: 0000-9999                       |
| %%      | escapar um símbolo de porcentagem: % |

## Valor(es) retornado(s) {#return-values}

Retorna uma string formatada que representa o formato de data e hora desejado.

* * *

## Exemplos {#examples}

### Retornar uma data no formato dos EUA {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Converter um valor de data e hora em um carimbo de data/hora de época {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Calcular os dias entre duas datas {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
