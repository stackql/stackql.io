---
title: Gerenciamento de acesso
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gerenciamento de acesso
  - autenticação
  - autorização
  - jwt
  - mtls
  - token bearer
description: "Gerenciamento de acesso de chamadores com ativação explícita no StackQL: autenticar com JWT, tokens bearer, TLS mútuo ou identidade local do SO, mapear para principals e grupos de um diretório e autorizar instruções com regras de política."
image: "/img/stackql-featured-image.png"
---

O gerenciamento de acesso de chamadores autentica o chamador de uma sessão do StackQL e autoriza cada instrução com base em uma política.  Ele é independente das credenciais dos providers, que continuam sendo fornecidas por meio de `--auth`, de variáveis de ambiente ou de [sessões de provider](/getting-started/provider-sessions).  Ele vem desabilitado por padrão, e as implantações que não o habilitam se comportam como antes.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[ Opções globais ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Habilitar o gerenciamento de acesso {#enabling-access-management}

O gerenciamento de acesso é ativado ao iniciar o StackQL com a opção global `--access.config` apontando para um arquivo de configuração JSON.  O exemplo inicia um shell interativo no modo protegido; `exec` e `mcp` são habilitados da mesma forma.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` recebe um envelope JSON com uma única chave `path`; um caminho de arquivo simples também é aceito.  A configuração é carregada uma única vez na inicialização, portanto faça a rotação de chaves ou de políticas por meio de uma reinicialização controlada.

Enquanto o gerenciamento de acesso está habilitado, somente `exec`, `shell`, `mcp` e `version` são executados; os demais comandos, incluindo `registry`, recusam a execução em vez de contornar o caminho protegido.  `srv` e o tipo de servidor MCP `reverse_proxy` também recusam, porque a dependência do protocolo de comunicação do PostgreSQL ainda não expõe evidências de autenticação por conexão.  As instruções SQL `AUTH` e `AUTH REVOKE` são recusadas no modo protegido; em vez disso, provisione as credenciais dos providers pela configuração do host.  Um `exec` protegido que falha retorna o status de saída `1` depois de apresentar o erro.

## Modos de autenticação {#authentication-modes}

O arquivo de configuração seleciona um `type`: `jwt` (o valor padrão quando omitido), `bearer`, `mtls` ou `local_os`.  A configuração é JSON estrito: campos desconhecidos, e campos que pertencem a outro modo, são rejeitados.  Todos os modos compartilham as mesmas `rules` e o mesmo [diretório de principals](#principal-directory).

### JWT {#jwt}

O chamador apresenta um token assinado, que é verificado offline com as chaves fixadas na configuração.  Uma configuração mínima indica o emissor e o público que o token deve conter, lista uma chave pública e permite que os membros do grupo `analysts` executem instruções de leitura (os membros do grupo `readers` podem fazer isso sem uma regra, consulte [Regras de política](#policy-rules)):

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

O emissor, o público e as chaves públicas são fixados na configuração; não há descoberta OIDC.  Os algoritmos aceitos são `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` e `ES512`, e as chaves RSA devem ter pelo menos 2048 bits.  Cada chave fica vinculada ao algoritmo configurado.  Tokens com um ID de chave desconhecido, uma assinatura inválida, uma expiração ausente ou já passada, um `nbf` ou `iat` no futuro, ou um emissor ou público incorretos são rejeitados.  Claims de grupo e URLs contidas no token não são usadas para confiança nem para associação no diretório.

### Token bearer estático {#static-bearer-token}

O chamador apresenta um token opaco, e a configuração lista o hash SHA-256 de cada token aceito com o subject que ele identifica.  Isso é adequado para um conjunto pequeno e fixo de identidades de serviço em que não se justifica executar um emissor de tokens:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Somente o hash SHA-256 de cada token é configurado, e os hashes devem ser únicos.  Alterar os hashes configurados exige uma reinicialização.

### TLS mútuo {#mutual-tls}

O chamador é identificado pelo certificado de cliente que apresenta no transporte MCP por HTTPS.  A configuração contém a CA que deve ter emitido o certificado e indica qual subject alternative name contém a identidade:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

A cadeia de certificados do cliente é verificada com `client_ca_pem`, e o subject da identidade é obtido de exatamente um SAN do tipo configurado, `uri_san` ou `dns_san`.  O Common Name não é usado como alternativa.  O TLS mútuo está disponível somente no transporte MCP por HTTPS.

### Identidade local do SO {#local-os-identity}

Nenhuma credencial é trocada: o subject é a identidade do sistema operacional com a qual o processo do StackQL é executado.  A configuração precisa apenas de um rótulo `issuer` e das regras:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

O subject da identidade é o UID efetivo do Unix ou o SID do Windows do processo do StackQL, incluindo UIDs numéricos de contêiner sem entrada no passwd.  Nenhuma credencial é apresentada e `STACKQL_CALLER_TOKEN` é ignorado.  Este modo é somente local: ele é recusado para chamadores HTTP.  A associação a grupos do sistema operacional não concede acesso; somente a associação no diretório concede.

### Prazo da sessão {#session-deadline}

Por padrão, as consultas não têm um prazo derivado da autenticação.  As credenciais são verificadas e a associação no diretório é atualizada a cada nova instrução, de modo que uma credencial expirada não pode iniciar novo trabalho, mas a expiração não interrompe uma instrução que já foi autorizada.  Defina `"enforce_session_deadline": true` para cancelar em `max_session_seconds` (valor padrão `300`, máximo `86400`), reduzido pela expiração do JWT ou do certificado.  Isso não renova tokens.

## Diretório de principals {#principal-directory}

As identidades são mapeadas para principals, e os principals para grupos, em quatro tabelas no backend SQLite ou PostgreSQL configurado:

| Tabela | Conteúdo |
|--|--|
| `stackql_access_principals` | ID do principal e um objeto JSON de atributos do tipo string.  Um atributo `tenant` define o namespace do estado de IaC em versão prévia. |
| `stackql_access_identities` | Par `(issuer, subject)` verificado para o ID do principal. |
| `stackql_access_groups` | IDs de grupo. |
| `stackql_access_memberships` | Vinculações de principal a grupo. |

Provisione as tabelas, as identidades e as associações por meio de uma conexão confiável de administração de banco de dados antes de atender consultas protegidas:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Use o emissor e o subject reais do modo: para a identidade local do SO, isso pode ser `('this-host', '1000')`, e para o TLS mútuo, `('client-certificates', 'spiffe://example/alice')`.  Selecione o backend explicitamente:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

O diretório produz um snapshot imutável para cada instrução.  A revogação é um `DELETE FROM stackql_access_memberships` na conexão de administração; a instrução seguinte vê o novo snapshot.  Identidades desconhecidas, um esquema ausente, dados malformados e indisponibilidades do diretório negam o acesso.  O SQL protegido comum não pode acessar as tabelas do diretório.  Mantenha as gravações de associações na conexão de administração, proteja o arquivo do banco de dados e use privilégios de banco de dados separados para administração e para runtime quando o backend oferecer suporte a isso.

## Regras de política {#policy-rules}

Cada regra concede uma `action` sobre um `resource` a um `subject` no formato `group:<group id>`.  Use os identificadores compilados de métodos e operações dos documentos de provider confiáveis; um curinga de operação concede métodos de mutação além de leituras.

O grupo `readers` detém implicitamente a capacidade de leitura SQL segura do host.  Leituras feitas por principals fora desse grupo exigem `host/sql/read`, e outras instruções exigem `execute` em `host/sql/<statement kind>`.  Operações de provider e o uso de credenciais sempre exigem regras explícitas, e uma permissão concedida sobre uma coleção não concede os métodos subjacentes a ela.  Verificações adicionais do host:

| Recurso | Ação | Escopo |
|--|--|--|
| `host/sql/read` | `execute` | Instruções de leitura fora do grupo implícito de leitores |
| `host/sql/exec` | `execute` | `EXEC` nativo, incluindo `EXEC` aninhado |
| `host/iac/run`, `host/iac/destroy` | `execute` | As relações de IaC em versão prévia, apesar da sintaxe `SELECT` delas |
| `host/function/<name>` | `execute` | Funções fora da pequena lista de permissões de funções reconhecidamente puras |
| `host/backend` | `read` | Relações de backend armazenadas e externas opacas |
| `host/credentials` | `reload` | A ferramenta MCP `reload_credentials` |
| `session/variables` | `read` | Desreferência de [variáveis de sessão](/getting-started/session-variables) e segredos |
| `session/config` | `write` | `SET` e `UNSET` |

A expansão de consultas armazenadas é verificada antes da execução, e a reutilização do cache de planos e de linhas é desabilitada nos caminhos protegidos.

## Pontos de entrada {#entry-points}

| Autenticação | `exec`, `shell`, MCP stdio | MCP por HTTPS |
|--|--|--|
| JWT | Variável de ambiente `STACKQL_CALLER_TOKEN` | Cabeçalho de solicitação `Authorization: Bearer` |
| Bearer estático | Variável de ambiente `STACKQL_CALLER_TOKEN` | Cabeçalho de solicitação `Authorization: Bearer` |
| SO local | UID ou SID efetivo do processo; nenhuma credencial | Recusado |
| TLS mútuo | Recusado: nenhuma conexão TLS verificada | Cadeia de cliente verificada e um SAN configurado |

Forneça as credenciais de token locais pelo ambiente do processo, nunca como um literal SQL ou um argumento de linha de comando.  Por HTTPS, o verificador de chamadores substitui o controle compartilhado por token bearer `auth_token_env_var` descrito em [Autenticação de clientes HTTP](/command-line-usage/mcp#http-client-authentication); a configuração do certificado e da chave TLS do servidor é obrigatória, e uma solicitação sem credenciais nunca recorre ao token do ambiente do servidor como alternativa.

## Inspecionar o chamador {#inspecting-the-caller}

Em uma sessão autenticada, `whoami()` retorna o ID do principal e `groups()` retorna os grupos do diretório como um array JSON:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

As duas funções exigem uma sessão iniciada com `--access.config`.

## Demonstrações passo a passo {#walkthroughs}

O repositório do StackQL inclui [quatro demonstrações passo a passo executáveis](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), uma por modo de autenticação, conduzidas por um script Python que provisiona um diretório de demonstração e verifica uma consulta.  Elas não precisam de uma conta na nuvem e são executadas pelo conjunto de testes do projeto.
