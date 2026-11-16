---
title: Estrutura léxica
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Estrutura léxica do StackQL: como o analisador sintático divide em tokens léxicos instruções, identificadores, aspas, literais, comentários e espaços em branco."
image: "/img/stackql-featured-image.png"
---

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy) [[ Palavras-chave ]](/language-spec/keywords) [[ Tipos de dados ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


A estrutura léxica e a gramática do StackQL seguem o modelo da implementação da linguagem do MySQL (uma adaptação do padrão ANSI SQL-92), com algumas extensões.  As instruções do StackQL servem para consultar recursos na nuvem (com a gramática de `SELECT`) ou para criar, modificar ou excluir infraestrutura com instruções padrão da linguagem de manipulação de dados (DML), como `INSERT`, `UPDATE`, `REPLACE` e `DELETE`.  Além disso, operações que não são DML, como, por exemplo, iniciar uma instância ou obter um objeto de um bucket do AWS S3 ou colocar um objeto nele, são implementadas como procedimentos integrados do provider (semelhantes aos procedimentos armazenados de um SGBD).  Os procedimentos integrados do provider são invocados com a instrução `EXEC`.

A linguagem SQL de domínio específico do StackQL permite programar infraestrutura de forma tanto declarativa quanto procedural.

* * * 

## Sintaxe {#syntax}
As operações do StackQL são compostas por uma ou mais instruções terminadas em ponto e vírgula (`;`).  As instruções incluem palavras-chave, identificadores, parâmetros e valores; um exemplo de instrução do StackQL usada para criar um disco de 10GB no GCP seria:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

No exemplo acima, _`INSERT`_, _`INTO`_ e _`SELECT`_ são palavras-chave, _google.compute.disks_ é um identificador, _project_, _zone_, _name_, _sizeGb_ são parâmetros e _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ são valores.

## Palavras-chave {#keywords}
As palavras-chave não diferenciam maiúsculas de minúsculas, ou seja, `INSERT` e `insert` são tratadas da mesma forma; no entanto, por convenção, elas costumam ser escritas em maiúsculas. A lista completa de palavras-chave está na seção Palavras-chave.

## Identificadores {#identifiers}
Os identificadores são símbolos que representam objetos na hierarquia de recursos de um provedor de nuvem.  Os identificadores são compostos por várias partes; para obter mais informações sobre identificadores, consulte o tópico Hierarquia de recursos do StackQL.

## Parâmetros e valores {#parameters-and-values}
Os parâmetros são especificados pelo recurso; você pode encontrá-los com uma instrução `DESCRIBE` para um determinado recurso do provedor de nuvem.  Os valores são objetos escalares ou complexos tipados, que correspondem aos parâmetros.  Para obter mais informações sobre parâmetros e valores, consulte os tópicos `DESCRIBE` e Tipos de dados.

## Variáveis {#variables}
Um identificador com prefixo de cifrão, como `$region`, desreferencia uma variável de sessão atribuída com [`SET`](/language-spec/set); ele é substituído pelo valor tipado da variável antes que a instrução seja planejada.  Um `'$region'` entre aspas simples é um literal de string.  Em uma cláusula `FROM`, uma `$variable` é resolvida para um nome de relação, e `IDENTIFIER($variable)` é resolvido para um nome de coluna em posições de expressão.  Um caminho entre aspas duplas com prefixo de cifrão, como `"$.auth.aws.password"`, é um alvo de configuração de runtime aceito por `SET` e `UNSET`.  As variáveis de sessão estão disponíveis a partir do StackQL `v1.0`; consulte [Variáveis de sessão](/getting-started/session-variables).

## Operadores {#operators}
`||` é o operador de concatenação de strings e tem precedência maior que os operadores de comparação.  `OR` é a disjunção booleana.  As versões do StackQL anteriores à `v1.0` reconheciam `||` como o token léxico `OR`.

## Comentários {#comments}
O StackQL oferece suporte a comentários de várias linhas e comentários em linha nas instruções do StackQL; veja exemplos de comentários a seguir:

```sql
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
