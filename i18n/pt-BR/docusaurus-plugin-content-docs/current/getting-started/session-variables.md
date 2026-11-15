---
title: Variáveis de sessão
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variáveis de sessão
  - set
  - unset
  - identifier
description: "Variáveis de sessão no StackQL: definir valores escalares tipados com SET, usá-los com $name em predicados e expressões e como nomes de relações e colunas com IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

Veja também:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Sessões de provider ]](/getting-started/provider-sessions) [[ Usar variáveis ]](/getting-started/variables)

As variáveis de sessão são valores escalares mantidos pela sessão atual e referenciados em instruções posteriores.  Elas estão disponíveis a partir do StackQL `v1.0` e funcionam no shell interativo, em scripts `exec`, pelo protocolo de comunicação do PostgreSQL e pelo servidor MCP.

As variáveis de sessão são diferentes das variáveis de template descritas em [Usar variáveis](/getting-started/variables).  As variáveis de template são fornecidas ao `stackql exec` por meio de `--iqldata` e `--var` e são renderizadas no texto do script antes de ele ser analisado.  As variáveis de sessão são resolvidas dentro da instrução analisada, mantêm o seu tipo e podem ser alteradas entre instruções.

## Definir e remover variáveis {#setting-and-removing-variables}

[`SET`](/language-spec/set) atribui uma string, um número, um booleano ou `null`.  [`UNSET`](/language-spec/unset) remove uma variável; `SET name = DEFAULT` é equivalente.  Um `null` explícito é um valor armazenado, não uma redefinição.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Arrays e objetos não são valores escalares.  Para guardar um documento JSON, armazene-o como string:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Desreferenciar uma variável {#dereferencing-a-variable}

Use `$name`, sem aspas, em qualquer posição de expressão.  As strings recebem escape como literais SQL; números, booleanos e `null` mantêm o seu tipo.  Um `'$name'` entre aspas simples é o texto literal `$name`, não uma desreferência.  Uma referência a uma variável que não existe falha antes de qualquer solicitação ao provider.

Os valores resolvidos podem ser usados em expressões SQL comuns, incluindo `CAST`, `CONVERT`, concatenação com `||` e chamadas de função:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

As atribuições aceitam somente literais escalares ou referências diretas; aplique conversões e outras expressões onde a variável é usada, não na instrução `SET`.

## Variáveis em predicados {#variables-in-predicates}

Uma variável em uma cláusula `WHERE` é resolvida antes de a instrução ser planejada, então ela é encaminhada à API do provider exatamente como um literal seria.  Por isso, os parâmetros obrigatórios podem ser fornecidos a partir de variáveis:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Os valores tipados são comparados com o seu tipo.  `WHERE active = $enabled` com `SET enabled = false` filtra pelo booleano, e `WHERE id = CAST($some_id AS TEXT)` é a forma a usar quando a coluna do provider é uma string e a variável é um número.

## Variáveis como nomes de objetos {#variables-as-object-names}

As variáveis podem nomear relações e colunas, além de valores.  Os nomes são convertidos em identificadores na instrução analisada e nunca são inseridos no texto SQL, então um valor que contém uma instrução, está vazio ou não é uma string é rejeitado antes de qualquer acesso ao provider.

| Posição | Forma |
|--|--|
| Cláusula `FROM` | `FROM $relation_name` ou, por componente, `FROM $provider.$service.$resource` |
| Coluna em uma expressão | `IDENTIFIER($column_name)`, consulte [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Alias de coluna ou de tabela | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| Nomes de colunas em DDL, listas de colunas de `INSERT`, destinos de `UPDATE` | a forma entre aspas duplas `"$name"` |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Joins, CTEs e subconsultas resolvem as suas variáveis de relação da mesma forma.  Valores de relação com pontos têm até quatro componentes.  As variáveis não são fragmentos SQL arbitrários: não é possível substituir uma palavra-chave, uma lista de colunas separadas por vírgulas ou uma cláusula `WHERE` inteira.

## Escopo e isolamento {#scope-and-isolation}

As variáveis pertencem à sessão que as definiu.  Conexões separadas a um servidor `stackql srv` têm variáveis separadas, assim como invocações separadas de `exec`.  Cada solicitação MCP é uma sessão própria, então um `SET` e a consulta que o usa precisam ser enviados no mesmo lote de instruções.  Novas sessões começam sem variáveis.

A sessão é o único escopo.  `SET SESSION` e `UNSET SESSION` são aceitos e significam exatamente o mesmo que `SET` e `UNSET`; a palavra-chave existe para scripts que devem ser lidos da mesma forma que em bancos de dados onde as configurações de sessão e as globais são diferentes.  `GLOBAL` é rejeitado, porque não há estado compartilhado entre sessões.

As instruções preparadas resolvem as variáveis quando são executadas, não quando são preparadas, e um plano que contém valores de sessão vinculados não é reutilizado entre execuções.

Com o [gerenciamento de acesso](/command-line-usage/access-management) habilitado, ler uma variável exige `session/variables:read`, e definir ou redefinir uma exige `session/config:write`.  Resolver uma variável para um nome de objeto não concede nenhuma permissão sobre esse objeto; a relação resolvida é autorizada antes da execução, como de costume.
