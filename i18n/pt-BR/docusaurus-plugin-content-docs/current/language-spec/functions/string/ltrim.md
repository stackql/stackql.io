---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Função LTRIM no StackQL: remover espaços, ou qualquer caractere de um conjunto informado, do lado esquerdo de uma string."
image: "/img/stackql-featured-image.png"
---
Retorna uma string formada pela remoção de espaços, ou de qualquer caractere de um conjunto informado, do lado esquerdo de uma string de entrada.  

Veja também:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Sintaxe {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Uma string literal ou coluna de string.

__*character*__  
Uma string opcional de caracteres: cada caractere dela é removido do início de *character_expression*.  Se for omitida, os espaços são removidos (não as tabulações nem as quebras de linha).

## Valor(es) retornado(s) {#return-values}
Retorna uma string.

* * *

## Exemplos {#examples}

### Remover espaços iniciais de uma coluna de string {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Para obter mais informações, consulte [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
