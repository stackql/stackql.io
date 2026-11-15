---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função ROUND no StackQL: arredondar um argumento numérico para um número especificado de casas decimais, por padrão zero."
image: "/img/stackql-featured-image.png"
---
Arredonda um argumento numérico.  

Veja também:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*numeric_expression*__  
Uma expressão da categoria de tipos de dados numéricos exatos ou numéricos aproximados.

__*precision*__  
O número de dígitos à direita do ponto decimal para o qual a *numeric_expression* será arredondada.

> Se o argumento *precision* for omitido, o valor assumido é 0.

## Valor(es) retornado(s) {#return-values}
Retorna um valor numérico.

* * *

## Exemplos {#examples}

### Arredondar um número de ponto flutuante para duas casas decimais {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
