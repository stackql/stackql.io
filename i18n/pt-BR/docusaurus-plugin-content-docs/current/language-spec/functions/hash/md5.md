---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "Função MD5 no StackQL: retornar o hash MD5 hexadecimal em minúsculas de uma string, para impressões digitais e detecção de alterações, não para segurança."
image: "/img/stackql-featured-image.png"
---
Retorna o hash MD5 de uma string como 32 caracteres hexadecimais em minúsculas.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` SELECT `]](/language-spec/select) [[ Concatenação ]](/language-spec/functions/string/concatenation)

* * * 

## Sintaxe {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*expression*__  
Uma string, ou um valor convertido em texto.  Um argumento `NULL` retorna `NULL`.

## Valor(es) retornado(s) {#return-values}

Retorna uma string hexadecimal de 32 caracteres em minúsculas.

> `MD5` serve para gerar impressões digitais e detectar alterações.  Ela não é uma primitiva de senha nem de segurança.  No backend PostgreSQL, é usada a própria função `md5` do banco de dados.

* * *

## Exemplos {#examples}

### Gerar a impressão digital de um valor derivado {#fingerprint-a-derived-value}

As variáveis são substituídas como valores tipados, o inteiro é convertido em texto e unido ao sufixo, e o hash é calculado sobre o resultado em minúsculas.  As mesmas entradas sempre produzem a mesma string hexadecimal de 32 caracteres, portanto o valor pode ser usado como uma chave estável.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Detectar alterações de configuração {#detect-configuration-changes}

Calcular o hash de uma coluna JSON serializada gera um valor curto para armazenar e comparar entre execuções.  Um bucket cujo hash difere do da última execução teve a configuração do IAM alterada, sem que seja preciso armazenar ou comparar o documento inteiro.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
