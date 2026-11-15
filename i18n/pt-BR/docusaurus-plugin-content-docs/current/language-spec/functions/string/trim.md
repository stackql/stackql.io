---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função TRIM no StackQL: remover os espaços, ou qualquer caractere de um conjunto indicado, das duas extremidades de uma string."
image: "/img/stackql-featured-image.png"
---
Retorna uma string formada pela remoção dos espaços, ou de qualquer caractere de um conjunto indicado, das duas extremidades de uma string de entrada.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Uma string literal ou uma coluna de string.

__*character*__  
Uma string opcional de caracteres: cada caractere dela é removido do início e do final de *character_expression*.  Se for omitida, os espaços são removidos (não as tabulações nem as quebras de linha).

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Remover os espaços iniciais e finais de uma coluna de string {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
