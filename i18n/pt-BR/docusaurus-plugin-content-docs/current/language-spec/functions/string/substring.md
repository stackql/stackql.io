---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função SUBSTRING no StackQL (alias SUBSTR): retornar parte de uma string ou de um valor binário pela posição inicial e pelo comprimento."
image: "/img/stackql-featured-image.png"
---
Retorna parte de uma expressão de caracteres ou binária.  `SUBSTR` é um alias de `SUBSTRING`.

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Tipos de dados ]](/language-spec/data-types)

* * * 

:::info

Para extrair um campo de um objeto JSON, use a função [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Sintaxe {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Uma string ou um BLOB.

> Se *expression* for uma string, os índices de caracteres se referem a caracteres UTF-8 reais. Se *expression* for um BLOB, os índices se referem a bytes.

__*start*__  
Um inteiro que representa o primeiro caractere ou byte da subsequência da expressão de entrada a ser retornada.  Se *start* for negativo, o primeiro caractere da substring é encontrado contando a partir da direita, e não da esquerda.

> O caractere mais à esquerda de *expression* é o número 1.

__*length*__  
Um valor inteiro opcional que representa o comprimento da subsequência a ser retornada após o valor *start*.  Se *length* for omitido, a função retorna todos os caracteres até o final da string ou do BLOB especificado por *expression*.

> Se *length* for negativo, a função retorna os caracteres que precedem *start*, em quantidade igual ao valor absoluto de *length*: `SUBSTR('abcdef', 4, -2)` retorna `bc`.

## Valor(es) retornado(s) {#return-values}

Retorna uma string ou um valor BLOB, dependendo do tipo de *expression*.

* * *

## Exemplos {#examples}

### Retornar os primeiros *n* caracteres de uma coluna {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Truncar os primeiros *n* caracteres de uma coluna {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Retornar os últimos *n* caracteres de uma coluna {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
