---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variáveis de sessão
  - unset
  - sessões de provider
  - segredos
description: "UNSET no StackQL: remover variáveis de sessão ou limpar aliases de providers, campos de credenciais e segredos configurados com SET para a sessão atual."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Remove uma variável de sessão ou limpa a configuração em tempo de execução definida com [`SET`](/language-spec/set) para a sessão atual.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` SET `]](/language-spec/set) [[ Variáveis de sessão ]](/getting-started/session-variables) [[ Sessões de provider ]](/getting-started/provider-sessions)

* * * 

## Sintaxe {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Qualquer alvo aceito por `SET`: um nome de variável simples ou entre aspas duplas, ou um caminho de configuração entre aspas duplas e prefixado com cifrão.

__*SESSION*__  
Opcional, e o único escopo que existe: `UNSET SESSION name` e `UNSET name` são a mesma instrução, porque toda variável e toda configuração pertencem à sessão atual.  `UNSET GLOBAL` é rejeitado com `configuration scope "global" is not supported; use SESSION`.

`UNSET name` equivale a `SET name = DEFAULT`.  Os alvos de uma lista são removidos em ordem, e remover um nome que não está definido não é um erro.  Desreferenciar uma variável depois que ela foi removida falha antes que qualquer solicitação seja feita ao provider.

| Alvo | Efeito |
|--|--|
| `name` | Remove a variável de sessão. |
| `"$.auth.<provider>.<field>"` | Exclui esse campo do contexto de autenticação da sessão do provider.  O valor em vigor antes da substituição não é restaurado. |
| `"$.providers.<alias>"` | Remove a vinculação do alias do provider. |
| `"$.secrets.<name>"` | Exclui o segredo do repositório da sessão.  Para um repositório persistente do sistema operacional, isso exclui o valor armazenado. |
| `"$.secret_store"` | Descarta a vinculação atual do repositório e começa com uma memória de sessão vazia.  Os valores já persistidos em um repositório do sistema operacional não são excluídos. |

* * *

## Exemplos {#examples}

### Remover uma variável {#remove-a-variable}

A variável é removida da sessão, então a consulta comentada falha com `variable "$region" is not defined` em vez de ser executada com um valor desatualizado ou vazio.  Defina o nome novamente para reutilizá-lo.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Remover vários alvos {#remove-several-targets}

Uma instrução pode remover variáveis e caminhos de configuração ao mesmo tempo, e os alvos são processados em ordem.  Aqui, uma variável simples, uma variável com um nome com pontos e uma substituição de credencial são removidas em uma única lista.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### Tornar explícito o escopo da sessão {#make-the-session-scope-explicit}

Estas duas instruções fazem a mesma coisa: `SESSION` nomeia o único escopo que existe.  A forma explícita é adequada para scripts que também são executados em bancos de dados que distinguem o estado da sessão do estado global.  A instrução comentada falha porque não existe estado compartilhado.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Limpar uma substituição de credencial {#clear-a-credential-override}

O campo definido anteriormente com `SET "$.auth.github.password_var"` é excluído do contexto de autenticação da sessão do provider do GitHub, e os outros campos desse contexto não são alterados.  O valor em vigor antes da substituição não é restaurado, então defina o campo novamente se o provider ainda precisar dele.

```sql
UNSET "$.auth.github.password_var";
```

### Remover um alias de provider e seu segredo {#remove-a-provider-alias-and-its-secret}

O alias `production` foi vinculado com `SET "$.providers.production"` e faz referência, por meio de `auth_secret`, a um segredo armazenado em `$.secrets.production`.  Remover os dois em uma única instrução desfaz a vinculação, de modo que `production.s3.buckets` deixa de ser uma relação válida, e exclui o segredo do repositório da sessão.  `SESSION` é opcional aqui, como em todos os lugares.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
