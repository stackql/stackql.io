---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variáveis de sessão
  - identificador
description: "Função IDENTIFIER no StackQL: resolver uma variável de sessão com um nome de coluna em uma referência de coluna, para que variáveis nomeiem colunas em projeções e ORDER BY."
image: "/img/stackql-featured-image.png"
---
Resolve um valor de string, normalmente uma variável de sessão, em um nome de coluna.  Use-a onde uma `$variable` isolada seria lida como um valor escalar.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` SET `]](/language-spec/set) [[ Variáveis de sessão ]](/getting-started/session-variables)

* * * 

## Sintaxe {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Argumentos {#arguments}

__*variable*__  
Uma variável de sessão, ou um literal de string, que contém um nome de coluna.  Um nome qualificado, como `t.name`, é aceito.  A função aceita exatamente um argumento, que deve ser um único valor de string; uma concatenação como `'t.' || $column_name`, um segundo argumento ou um valor numérico é rejeitado com `IDENTIFIER requires one string value`.  Um valor vazio, ou um valor que contém uma instrução, é rejeitado com `invalid identifier value`, e uma variável não definida falha como não definida.  Todas essas verificações ocorrem antes de qualquer solicitação ao provider.

## Valor(es) retornado(s) {#return-values}

Na instrução analisada, a função é substituída por uma referência à coluna nomeada, portanto ela retorna o valor dessa coluna.  Os nomes são convertidos em identificadores e nunca são inseridos no texto SQL.  A referência de coluna resultante pode ser usada em qualquer lugar onde uma coluna pode: na lista de seleção, nas cláusulas `WHERE`, `GROUP BY` e `ORDER BY`, em argumentos de funções e dentro de CTEs e subconsultas.

* * *

## Exemplos {#examples}

### Projetar e ordenar por uma coluna nomeada em uma variável {#project-and-order-by-a-column-named-in-a-variable}

Tanto a coluna a retornar quanto a coluna de ordenação vêm de `column_name`, e a coluna de saída é renomeada com o valor de `output_name`.  `IDENTIFIER` é necessária na lista de seleção e em `ORDER BY` porque uma `$column_name` isolada nessas posições seria a string `'name'`, não a coluna.  O alias usa, em vez disso, a forma entre aspas duplas.  Depois que as variáveis são resolvidas, a instrução é igual a `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Filtrar por uma coluna nomeada em uma variável {#filter-on-a-column-named-in-a-variable}

A referência resolvida funciona tanto em um predicado quanto em uma projeção.  Aqui, o campo a filtrar e o valor a comparar vêm ambos de variáveis: `IDENTIFIER($filter_column)` se torna a coluna `status`, enquanto `$filter_value` é passada como o literal de string `'RUNNING'`.  As duas formas são diferentes de propósito, porque a primeira nomeia uma coluna e a segunda fornece um valor.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Qualificar a coluna com um alias de tabela {#qualify-the-column-with-a-table-alias}

Um valor com ponto é resolvido em uma referência de coluna qualificada.  A tabela recebe o alias `w`, que também vem de uma variável, e `IDENTIFIER($column_name)` se torna `w.name`.  Essa é a forma a usar quando o mesmo nome de coluna existe nos dois lados de um join e a referência, de outra forma, seria ambígua.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Valores rejeitados {#rejected-values}

Como o valor se torna um nome, e não texto de consulta, tudo o que não é um nome utilizável é recusado antes de a instrução ser planejada, e nenhuma solicitação chega ao provider.  As duas primeiras instruções abaixo falham com `invalid identifier value` e a terceira com `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Uma `$variable` em uma cláusula `FROM` é resolvida em uma relação sem `IDENTIFIER`, e uma `"$variable"` entre aspas duplas é usada para aliases, nomes de colunas em DDL, listas de colunas de `INSERT` e destinos de `UPDATE`.
