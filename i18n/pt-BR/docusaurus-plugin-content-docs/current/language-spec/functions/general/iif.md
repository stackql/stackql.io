---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função IIF no StackQL: retornar um de dois valores dependendo de uma condição ser verdadeira, uma expressão if-then-else em linha."
image: "/img/stackql-featured-image.png"
---
Retorna um valor com base na avaliação de uma expressão de entrada.  

Veja também:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaxe {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> A função `iif()` é logicamente equivalente à expressão `CASE` `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Argumentos {#arguments}

__*expression*__  
Uma expressão booleana a ser avaliada.

:::tip 

O argumento *expression* pode ser uma função aninhada, como `JSON_EXTRACT`.

:::

__*result_if_true*__  
O resultado a ser retornado se *expression* for avaliada como `true`.

__*result_if_false*__  
O resultado a ser retornado se *expression* for avaliada como `false`.

## Valor(es) retornado(s) {#return-values}
O tipo de dados dos argumentos *result_if_true* ou *result_if_false*.

* * *

## Exemplos {#examples}

### Categorizar os tamanhos de disco de instâncias do Compute Engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
