---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O comando stackql srv executa o StackQL como servidor do protocolo de comunicação do PostgreSQL, para que psql, ferramentas de BI e drivers consultem APIs na nuvem e SaaS com SQL."
image: "/img/stackql-featured-image.png"
---

Comando usado para iniciar o StackQL como serviço, que pode então ser acessado por clientes do [protocolo de comunicação do Postgres](https://www.postgresql.org/docs/current/protocol.html) autenticados com mTLS. O comando `srv` também pode executar um servidor [Model Context Protocol (MCP)](/command-line-usage/mcp) junto com o servidor do protocolo de comunicação do PostgreSQL, o que permite o acesso pelos dois protocolos tanto a clientes de banco de dados tradicionais quanto a agentes de IA.

* * * 

### Sintaxe {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Embora o servidor do StackQL use o protocolo de comunicação do Postgres, ele não é um servidor Postgres nem precisa de um.  O cliente usa o protocolo de comunicação para se comunicar com o servidor, que usa o StackQL para executar consultas ou operações DML em provedores remotos de nuvem e de SaaS e retornar os resultados ao cliente.

:::

Os parâmetros enviados pelo protocolo de consulta estendido (`Parse` / `Bind` / `Execute`, como usado pelo `psycopg` 3, pelo `pgx` e pelo JDBC) são vinculados no lado do servidor como literais de string.  A partir da `v0.12.742`, um placeholder `$n` dentro de um literal de string, de um identificador entre aspas ou de um comentário não é alterado, as barras invertidas nos valores dos parâmetros são escapadas e um valor de texto `NULL` continua sendo a string `NULL` (o `NULL` do SQL é transmitido separadamente).

### Opções do servidor {#server-options}

| Opção | Descrição |
|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Endereço em que o servidor escuta (normalmente `0.0.0.0`)|
|<span class="nowrap">`--pgsrv.port`</span>|Porta em que o servidor escuta (por exemplo, `5444`)|
|<span class="nowrap">`--pgsrv.tls`</span>|Objeto de configuração do mTLS; consulte o [exemplo](#examples) abaixo|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Nível de log (valor padrão: `WARN`)|

<br/>

> consulte [Opções globais](/command-line-usage/global-flags) para ver outras opções

### Opções do servidor MCP {#mcp-server-options}

Ao executar o comando `srv` com suporte a MCP, as seguintes opções adicionais estão disponíveis:

| Opção | Descrição |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Tipo de servidor MCP: `http` (em memória) ou `reverse_proxy` (baseado em TCP)|
|<span class="nowrap">`--mcp.config`</span>|Objeto de configuração JSON do servidor MCP (consulte a [documentação do MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Codificação do log de auditoria do MCP: `jsonl` (padrão) ou `otel` (registros de log OTLP/JSON); substitui `server.audit.format` no objeto de configuração|
|<span class="nowrap">`--mcp.protocol.version`</span>|Revisão mais recente do protocolo MCP anunciada: `auto` (padrão), `2026-07-28` (somente sem sessão) ou uma revisão mais antiga, como `2025-11-25`; substitui `server.protocol_version` no objeto de configuração|

<br/>

:::info[Alteração incompatível na v0.12.742]

A partir da `v0.12.742`, o transporte HTTP do MCP (`--mcp.server.type=http` ou `reverse_proxy`) se recusa a iniciar, a menos que o objeto de configuração indique um token bearer com `server.auth_token_env_var` ou dispense explicitamente a autenticação com `"allow_unauthenticated": true`.  Consulte [Autenticação de clientes HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Consulte a [documentação do comando MCP](/command-line-usage/mcp) para obter informações detalhadas sobre como configurar e usar o servidor MCP, incluindo modos de implantação, opções de configuração e integração com assistentes de IA.

:::

:::info

Você precisa definir as variáveis de ambiente necessárias para a autenticação do provider antes de iniciar o servidor; consulte [Usar um provider](/getting-started/using-a-provider) para obter mais informações.

:::

Para servidores de longa duração, a opção [`--env.file`](/command-line-usage/global-flags) permite criar ou rotacionar as credenciais em um arquivo no estilo dotenv no disco antes da inicialização, em vez de exportar variáveis no shell:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Para o `stackql srv`, o arquivo é lido uma vez na inicialização; para recarregar as credenciais no meio da sessão, use a ferramenta MCP [`reload_credentials`](/mcp/reload_credentials).

### Opções {#flags}

| Opção | Descrição |
|--|--|
|`-h,--help`|Exibe as informações de ajuda|
|`-v,--verbose`|Executa consultas no modo detalhado, com saída adicional enviada para stdout; se a opção global `-f` (`--outfile`) estiver definida,<br/>essas informações adicionais de log são gravadas no arquivo de saída junto com os resultados da consulta|

<br/>

> consulte [Opções globais](/command-line-usage/global-flags) para ver outras opções

* * *

### Exemplos {#examples}

Inicie um processo do servidor do StackQL e conecte-se a ele com um cliente de linha de comando `psql`.  

Primeiro, baixe o arquivo de configuração `openssl.cnf` de [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Em seguida, gere os arquivos de certificado e de chave que o servidor e o cliente vão usar para a autenticação TLS mútua (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Agora defina as variáveis de ambiente; se o cliente e o servidor estiverem no mesmo host, elas podem ser compartilhadas.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Configure um objeto de autenticação para cada provider ao qual o servidor precisará ter acesso, por exemplo, para o GitHub.  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Agora inicie o servidor em um terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

Em outro terminal, conecte-se ao servidor com o cliente de linha de comando `psql`.  

```bash
psql 127.0.0.1
```

Agora execute uma consulta do StackQL em um provider autenticado.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```
