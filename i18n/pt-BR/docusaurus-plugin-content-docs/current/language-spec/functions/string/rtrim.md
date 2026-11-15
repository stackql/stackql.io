---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função RTRIM no StackQL: remover espaços, ou qualquer caractere de um conjunto informado, do lado direito de uma string."
image: "/img/stackql-featured-image.png"
---
Retorna uma string formada pela remoção de espaços, ou de qualquer caractere de um conjunto informado, do lado direito de uma string de entrada.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Uma string literal ou coluna de string.

__*character*__  
Uma string opcional de caracteres: cada caractere dela é removido do final de *character_expression*.  Se for omitida, os espaços são removidos (não as tabulações nem as quebras de linha).

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Remover espaços finais de uma coluna de string {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
