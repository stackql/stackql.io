---
title: Sessões de provider e segredos
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - sessões de provider
  - várias contas
  - credenciais
  - segredos
  - keychain
description: "Sessões de provider no StackQL: vincular aliases com credenciais, tenancy e valores padrão próprios, substituir campos de credenciais e guardar segredos na memória ou no SO."
image: "/img/stackql-featured-image.png"
---

Veja também:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Variáveis de sessão ]](/getting-started/session-variables) [[ Opções globais ]](/command-line-usage/global-flags)

Uma sessão pode vincular um provider a um alias com credenciais, rótulo de tenancy, valores padrão de parâmetros e limites de concorrência de solicitações próprios, de modo que várias contas ou tenants do mesmo provider podem ser consultados em uma sessão, e em uma instrução.  Os campos de credenciais também podem ser substituídos para a sessão, e os segredos podem ficar na memória da sessão ou, com uma ativação explícita do operador, no repositório de segredos do sistema operacional.  Tudo isso é configurado com [`SET`](/language-spec/set) e [`UNSET`](/language-spec/unset) e está disponível a partir do StackQL `v1.0`.

Tudo o que é descrito aqui tem como escopo a sessão atual.  Conexões independentes a um servidor `stackql srv` não compartilham vinculações nem segredos, e cada solicitação MCP é uma sessão própria; por isso, ao usar o servidor MCP, configure e consulte em um único lote de instruções.

## Aliases de provider {#provider-aliases}

`SET "$.providers.<alias>"` vincula um alias a um provider.  O valor é um documento JSON contido em uma string:

| Chave | Obrigatório | Descrição |
|--|--|--|
| `provider` | Sim | O provider canônico para o qual o alias é resolvido, por exemplo `aws`. |
| `tenancy` | Sim | Um rótulo de agrupamento, como o nome de uma conta ou de um tenant.  O StackQL não o verifica junto ao provider. |
| `auth` | Um entre `auth` e `auth_secret` | Um documento de autenticação no formato de alias, que difere da entrada do provider no objeto [`--auth`](/command-line-usage/global-flags): para a AWS, as chaves são `access_key_id_env_var` e `secret_access_key_env_var`, não `keyIDenvvar` e `credentialsenvvar`, e uma chave que o formato de alias não reconhece gera o erro `invalid session configuration field or type`.  O alias não usa as credenciais do provider canônico. |
| `auth_secret` | Um entre `auth` e `auth_secret` | O nome de um [segredo de sessão](#session-secrets) que contém o documento de autenticação. |
| `params` | Não | Valores padrão para os parâmetros declarados do provider, incluindo os parâmetros de servidor declarados.  Um predicado na consulta substitui um valor padrão. |
| `tuning` | Não | Limites `max_concurrent` e `max_per_host` para as solicitações do alias.  Aliases que compartilham provider e tenancy compartilham esses controles e precisam declarar o mesmo tuning. |
<br/>
O exemplo a seguir vincula duas contas da AWS a aliases separados, cada um lendo as suas chaves de variáveis de ambiente com nomes diferentes, e consulta as duas em uma instrução:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

As referências a variáveis de ambiente são resolvidas a partir do ambiente do processo do StackQL; não existe um ambiente local da sessão.  Credenciais temporárias da AWS também precisam do campo de token de sessão ou da referência à variável de ambiente correspondente.

Um alias se comporta como o seu provider em instruções de metadados e de dados: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` e `EXEC` aceitam o alias no lugar do nome do provider.  Os aliases não se estendem às relações nativas integradas nem às relações de IaC em versão prévia.  Ramos de `UNION ALL` que usam aliases diferentes são executados em sequência.

Cada consulta tira um snapshot independente das credenciais do alias.  Alterar uma vinculação afeta as consultas posteriores, não uma que já esteja em execução.  `UNSET "$.providers.<alias>"` remove a vinculação.

## Substituir campos de credenciais {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` altera um campo do contexto de autenticação de um provider para a sessão; os nomes dos campos são as chaves da entrada do provider em `--auth`:

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.password_var";
```

Assim como nos aliases, a alteração se aplica às consultas iniciadas depois do `SET`.  Consultas a um provider cuja autenticação foi removida, ou cujo alias não tem um `auth` utilizável, falham antes de qualquer solicitação ser enviada.

## Segredos de sessão {#session-secrets}

Por padrão, toda sessão tem um repositório de segredos somente em memória, sem necessidade de configuração.  `SET "$.secrets.<name>"` armazena uma string literal, normalmente um documento de autenticação JSON, e um alias de provider faz referência a ela com `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Fazer a rotação de um segredo afeta as consultas posteriores; excluí-lo faz os usos posteriores falharem.  Uma nova sessão recebe um repositório novo e vazio, e encerrar a sessão o libera.  `UNSET "$.secret_store"` descarta a vinculação atual do repositório e recomeça com a memória da sessão vazia.

O uso normal de credenciais e as instruções `SET` que armazenam variáveis ou configuração não gravam segredos no backend SQL.  Colocar dados sensíveis em uma variável ou tabela comum é responsabilidade do chamador.

## Repositórios de segredos persistentes do sistema operacional {#persistent-os-secret-stores}

O armazenamento persistente vem desabilitado por padrão e só pode ser habilitado pelo operador, ao iniciar o StackQL com `--allow-persistent-secrets`; o SQL não pode concedê-lo.  Com a opção definida, uma sessão seleciona um repositório do sistema operacional com:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

O repositório é o macOS Keychain, o Linux Secret Service (`secret-tool` com um serviço de sessão em funcionamento) ou o Windows Credential Manager.  Somente o backend `os` é aceito: repositórios em arquivo simples, caminhos de arquivo e qualquer fallback implícito para arquivo são rejeitados mesmo com a opção definida, e um mecanismo do sistema operacional indisponível gera uma falha em vez de recorrer a um arquivo.

Os valores gravados em um repositório do sistema operacional sobrevivem à sessão SQL e são excluídos explicitamente com `UNSET "$.secrets.<name>"`.  Redefinir a vinculação do repositório não exclui os valores já persistidos.  O acesso ao repositório é determinado pela identidade do processo do StackQL no sistema operacional; um namespace não é um controle de acesso por usuário, então não trate essa ativação explícita como um repositório de segredos multiusuário.

## Valores de segredos no SQL {#secret-values-in-sql}

Os valores de segredos nunca são desreferenciados no SQL, a menos que o operador inicie o StackQL com `--allow-secret-dereference`.  Com essa opção, referências como `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` e `"$providers.production.auth.credentials"` são resolvidas em expressões:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Em uma sessão iniciada sem a opção, a mesma instrução é recusada antes de qualquer avaliação, como mostra a cópia comentada.  A opção é um reconhecimento de que valores desreferenciados explicitamente podem entrar na preparação (staging) do SQL, nos resultados, nos logs e nos artefatos de consulta persistidos.  A autenticação normal dos providers não precisa dela.

## Permissões {#permissions}

Com o [gerenciamento de acesso](/command-line-usage/access-management) habilitado, ler variáveis e segredos exige `session/variables:read`, e `SET` ou `UNSET` exige `session/config:write`.  Um alias resolvido é autorizado como o seu provider canônico antes da execução.  Propriedade e concessões por variável e por segredo não estão implementadas.
