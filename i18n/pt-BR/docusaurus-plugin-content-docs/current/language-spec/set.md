---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variáveis de sessão
  - set
  - sessões de provider
  - segredos
description: "SET no StackQL: atribuir variáveis de sessão tipadas e configurar aliases de provider, campos de credenciais e segredos da sessão atual com alvos prefixados por cifrão."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Atribui uma variável de sessão ou define configurações de runtime para a sessão atual.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` UNSET `]](/language-spec/unset) [[ Variáveis de sessão ]](/getting-started/session-variables) [[ Sessões de provider ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Sintaxe {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Um identificador simples (`region`), um nome entre aspas duplas (`"providers.saved"`) ou um caminho de configuração entre aspas duplas com prefixo de cifrão (`"$.auth.aws.password"`).

__*literal*__  
Uma string, um número, um booleano ou `null`.  As strings ficam entre aspas simples.  Arrays e objetos não são valores escalares; armazene um documento JSON como uma string.

__*DEFAULT*__  
Redefine o alvo, o equivalente a [`UNSET`](/language-spec/unset).  Um `null` explícito é um valor armazenado, não uma redefinição.

__*reference*__  
Uma referência `"$name"` entre aspas duplas a uma variável existente, por exemplo `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Opcional, e o único escopo que o StackQL tem.  `SET SESSION name = ...` e `SET name = ...` são a mesma instrução: toda atribuição pertence à sessão atual e a nada mais.  A palavra-chave existe para que os scripts tenham a mesma leitura que em bancos de dados nos quais `SET SESSION` e `SET GLOBAL` são diferentes.  `SET GLOBAL` é analisada sem erro, mas é rejeitada com `configuration scope "global" is not supported; use SESSION`, porque não há estado compartilhado entre sessões.

As atribuições de uma lista de `SET` são aplicadas em ordem e não formam uma transação: se a segunda atribuição de uma lista falhar, a primeira continua em vigor.

## Alvos {#targets}

| Alvo | Efeito |
|--|--|
| `name` ou `"name"` | Armazena uma variável de sessão escalar, desreferenciada como `$name` nas instruções seguintes.  Pontos, por si só, não selecionam configuração: `"providers.saved"` é uma variável chamada `providers.saved`.  A grafia antiga `"variables.name"` é um alias de `name`. |
| `"$.auth.<provider>.<field>"` | Define um campo do contexto de autenticação do provider nesta sessão.  Os campos são as chaves do objeto [`--auth`](/command-line-usage/global-flags) desse provider, por exemplo `credentialsenvvar`, `password` ou `token_url`. |
| `"$.providers.<alias>"` | Vincula um alias de provider com credenciais, rótulo de tenancy, valores padrão de parâmetros e ajustes de desempenho próprios.  O valor é um documento JSON em uma string; consulte [Sessões de provider](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Armazena um segredo no repositório de segredos da sessão.  O valor é uma string literal, normalmente um documento JSON `auth`, referenciado a partir de um alias de provider como `auth_secret`. |
| `"$.secret_store"` | Seleciona um repositório de segredos persistente do sistema operacional, `{"backend":"os","namespace":"..."}`.  Requer a opção de operador `--allow-persistent-secrets`. |

Todas as formas afetam somente a sessão atual.  Conexões independentes com um servidor [`srv`](/command-line-usage/srv) não veem as atribuições umas das outras, e cada solicitação MCP é uma sessão própria; por isso, configure e consulte em um único lote de instruções ao usar o servidor MCP.

* * *

## Exemplos {#examples}

### Definir variáveis de sessão {#set-session-variables}

Cada instrução armazena um escalar com o nome informado, e o valor mantém seu tipo: `region` é uma string, `minimum_count` um inteiro e `enabled` um booleano.  `null` é armazenado como valor, então `$optional_value` é resolvida depois como `NULL` do SQL em vez de falhar por não estar definida.  A última instrução atribui duas variáveis em uma única lista.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Tornar explícito o escopo da sessão {#make-the-session-scope-explicit}

As duas primeiras instruções são idênticas: `SESSION` nomeia o único escopo que existe, e as duas atribuições duram até que a variável seja removida ou a sessão termine.  Use a palavra-chave quando um script precisar ter a mesma leitura que em um banco de dados que diferencia configurações de sessão e configurações globais.  A instrução comentada aparece para contraste: ela não atribui nada e falha com `configuration scope "global" is not supported; use SESSION`, porque nenhum estado é compartilhado entre sessões.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Usar uma variável em uma consulta {#use-a-variable-in-a-query}

Desreferencie uma variável com `$name`, sem aspas, em qualquer posição de expressão.  Os valores mantêm seu tipo: as strings são escapadas como literais SQL, e números, booleanos e `null` são repassados sem alteração.  As variáveis em uma cláusula `WHERE` são resolvidas antes que a solicitação seja planejada, então são enviadas ao provider como literais.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Redefinir uma variável {#reset-a-variable}

`DEFAULT` remove a variável, exatamente como [`UNSET`](/language-spec/unset) faz, então um `$region` posterior falha com `variable "$region" is not defined` até que ela seja definida novamente; a consulta comentada mostra essa falha.  Isso é diferente de atribuir `null`, que mantém a variável com o valor `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Copiar um valor de outra variável {#copy-a-value-from-another-variable}

O lado direito pode ser uma referência entre aspas duplas a uma variável existente, o que copia o valor que ela contém naquele momento.  Aqui, uma vinculação de provider é primeiro armazenada como uma variável comum chamada `providers.saved` (os pontos não a tornam configuração) e depois ativada ao ser copiada para o caminho `$.providers.production`.  Remover a cópia salva depois não afeta a vinculação ativa.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Substituir um campo de credencial nesta sessão {#override-a-credential-field-for-this-session}

O contexto de autenticação de um provider é o objeto fornecido para ele por meio de [`--auth`](/command-line-usage/global-flags) na inicialização.  Um caminho sob `$.auth` substitui um campo desse objeto pelo restante da sessão e mantém os outros campos como estavam.  Aqui, o provider do GitHub passa a apontar para outra variável de ambiente, que contém um token de somente leitura; a variável é lida do ambiente do processo do StackQL, e as consultas seguintes em `github.*` a usam até que o campo seja removido ou a sessão termine.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Vincular um alias de provider {#bind-a-provider-alias}

Um alias de provider é uma segunda instância de um provider, configurada de forma independente, usada no lugar do nome do provider nas consultas.  O valor é um documento JSON em uma string: `provider` nomeia o provider subjacente, `tenancy` rotula a conta, `auth` é um bloco de credenciais no formato de alias (para a AWS, `access_key_id_env_var` e `secret_access_key_env_var`, não as chaves `keyIDenvvar` e `credentialsenvvar` de `--auth`), e `params` fornece valores padrão para parâmetros de solicitação, como `region`.  As consultas em `production.s3.buckets` passam então a ser executadas com essas credenciais, enquanto `aws.s3.buckets` mantém a configuração de inicialização.  A lista completa de chaves está em [Sessões de provider](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
