---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O comando stackql mcp executa o StackQL como servidor Model Context Protocol para agentes de IA: modos de implantação e de segurança, recarga de credenciais e log de auditoria."
image: "/img/stackql-featured-image.png"
---

Comando usado para iniciar o StackQL como servidor Model Context Protocol (MCP), o que permite que agentes e assistentes de IA interajam com a infraestrutura na nuvem usando as capacidades de consulta do StackQL.

:::info[O que é o MCP?]

O [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) é um protocolo aberto que padroniza a forma como aplicações de IA interagem com fontes de dados e ferramentas externas.  Ao executar o StackQL como servidor MCP, você permite que agentes de IA (Claude, Cursor, Continue, agentes personalizados etc.) consultem e gerenciem a infraestrutura na nuvem em vários providers usando linguagem natural.

:::

* * *

### Sintaxe {#syntax}

`stackql mcp [flags]` para um servidor MCP independente.

`stackql srv [flags]` para executar o servidor MCP junto com o servidor do protocolo de comunicação do PostgreSQL no mesmo processo.

* * *

### Modos de implantação {#deployment-modes}

O servidor MCP do StackQL pode ser implantado em três configurações diferentes, conforme os requisitos de arquitetura.  Os exemplos abaixo ativam explicitamente o serviço sem autenticação com `"allow_unauthenticated": true`, o que serve para o desenvolvimento local; para qualquer coisa que outro processo ou host possa alcançar, configure um token bearer (consulte [Autenticação de clientes HTTP](#http-client-authentication)).

#### 1. Servidor MCP independente {#1-standalone-mcp-server}

Execute o StackQL como servidor MCP dedicado em uma porta especificada.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Caso de uso:** quando você só precisa de acesso pelo protocolo MCP e não precisa de compatibilidade com o protocolo de comunicação do PostgreSQL.

#### 2. Servidor MCP + PostgreSQL (em memória) {#2-mcp--postgresql-server-in-memory}

Execute os servidores MCP e PostgreSQL ao mesmo tempo, com comunicação em memória entre eles.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Caso de uso:** quando você precisa tanto de acesso pelo protocolo MCP para agentes de IA quanto do protocolo de comunicação do PostgreSQL para clientes de banco de dados tradicionais, com desempenho máximo graças à comunicação em memória.

#### 3. Servidor MCP + PostgreSQL (proxy reverso) {#3-mcp--postgresql-server-reverse-proxy}

Execute os dois servidores com comunicação via TCP, com suporte a implantações distribuídas e criptografia TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Com criptografia TLS:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

O TLS criptografa o transporte, mas não autentica o cliente, então este exemplo também exige o token bearer armazenado em `STACKQL_MCP_TOKEN`.

**Caso de uso:** quando você precisa separar as cargas de trabalho do MCP e do PostgreSQL em processos ou hosts diferentes, ou quando precisa de criptografia TLS para o endpoint MCP.

* * *

### Opções de configuração {#configuration-options}

#### Tipo de servidor MCP {#mcp-server-type}

| Tipo | Descrição |
|--|--|
|`http`|Modo de servidor HTTP direto - as solicitações MCP são tratadas no próprio processo pelo mecanismo do StackQL.|
|`reverse_proxy`|Modo proxy reverso - as solicitações MCP são encaminhadas a um servidor PostgreSQL de backend pelo DSN configurado.  Usado com `stackql srv`.|
|`stdio`|Transporte de entrada/saída padrão - o servidor lê as solicitações de `stdin` e grava as respostas em `stdout`.  Usado por clientes MCP incorporados a editores (Claude Desktop, Cursor etc.).|

#### Formato do log do MCP {#mcp-log-format}

A opção `--mcp.log.format` seleciona a codificação do log de auditoria: `jsonl` (padrão) ou `otel` (registros de log OTLP/JSON do OpenTelemetry).  Ela substitui `server.audit.format` no objeto de configuração.  Consulte [Formato do log](#log-format).

#### Versão do protocolo MCP {#mcp-protocol-version}

A opção `--mcp.protocol.version` define a revisão mais recente do protocolo que o servidor anuncia: `auto` (padrão, todas as revisões compatíveis), `2026-07-28` (somente sem sessão) ou uma revisão anterior, como `2025-11-25` (essa revisão e as anteriores).  Ela substitui `server.protocol_version` no objeto de configuração.  Consulte [Fixar a revisão](#pinning-the-revision).

#### Objeto de configuração do MCP {#mcp-configuration-object}

A opção `--mcp.config` aceita um objeto JSON com a estrutura a seguir.

##### Configuração do servidor {#server-configuration}

| Campo | Descrição | Obrigatório |
|--|--|--|
|`server.transport`|Protocolo de transporte - `http` ou `stdio`.|Sim|
|`server.address`|Endereço e porta aos quais o servidor MCP é vinculado (por exemplo, `127.0.0.1:9912`).  Somente para o transporte HTTP.|Para `http`|
|`server.tls_cert_file`|Caminho do arquivo de certificado TLS para HTTPS.|Não|
|`server.tls_key_file`|Caminho do arquivo de chave privada TLS para HTTPS.|Não|
|`server.auth_token_env_var`|Nome de uma variável de ambiente que contém um token bearer pré-compartilhado.  Quando definido, toda solicitação HTTP deve incluir `Authorization: Bearer <token>`; o servidor se recusa a iniciar se a variável não estiver definida ou estiver vazia.  Consulte [Autenticação de clientes HTTP](#http-client-authentication).|Este ou `allow_unauthenticated`, para `http`|
|`server.allow_unauthenticated`|Ativação explícita para servir o transporte HTTP sem autenticação de clientes.  Valor padrão: `false`.|Este ou `auth_token_env_var`, para `http`|
|`server.mode`|Contrato de segurança que controla as operações de mutação e de ciclo de vida.  Um destes: `read_only`, `safe` (padrão), `delete_safe`, `full_access`.  Consulte [Modos do servidor](#server-modes).|Não|
|`server.read_only`|Alias booleano **legado** de `mode: read_only`.  Quando os dois campos estão definidos, `mode` prevalece.|Não|
|`server.stateless`|Serve o Streamable HTTP sem sessões (sem `Mcp-Session-Id`), que é como a revisão `2026-07-28` do protocolo é servida por HTTP.  Valor padrão: `false`.  Ignorado para `stdio`.  Consulte [Suporte a revisões do protocolo](#protocol-revision-support).|Não|
|`server.protocol_version`|Revisão mais recente do protocolo anunciada: `auto` (padrão), `2026-07-28` ou uma revisão anterior, como `2025-11-25`.  `2026-07-28` implica `stateless` para o Streamable HTTP.  Consulte [Fixar a revisão](#pinning-the-revision).|Não|
|`server.audit`|Configuração do subsistema de auditoria, incluindo `audit.format` (`jsonl` ou `otel`).  Consulte [Log de auditoria](#audit-log).|Não|

##### Configuração do backend (somente no modo proxy reverso) {#backend-configuration-reverse-proxy-mode-only}

| Campo | Descrição | Obrigatório |
|--|--|--|
|`backend.dsn`|String de conexão do PostgreSQL para o servidor StackQL de backend.|Sim (para `reverse_proxy`)|

<br />

:::info

O DSN do backend deve incluir o parâmetro `default_query_exec_mode=simple_protocol` para obter a melhor compatibilidade.

:::

##### Listas de permissões {#allowlists}

| Campo | Descrição |
|--|--|
|`enabled_tools`|Array de nomes de ferramentas que o servidor tem permissão para publicar.  Quando omitido ou vazio, todas as ferramentas integradas são registradas.  Usado para expor um subconjunto restrito (por exemplo, um servidor de inventário somente leitura).|
|`enabled_prompts`|Array de nomes de prompts.  Mesma semântica de `enabled_tools`, mas para a superfície de prompts.|

<br/>

Exemplo - um servidor que publica somente `server_info` e `list_providers`:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Suporte a revisões do protocolo {#protocol-revision-support}

A revisão `2026-07-28` do protocolo, `server.stateless` e o formato de log de auditoria `otel` estão disponíveis a partir do StackQL `v0.11.660`.  `server.protocol_version` e a opção `--mcp.protocol.version` estão disponíveis a partir do StackQL `v0.12.732`.

O servidor fala todas as revisões do Model Context Protocol compatíveis com o [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) e negocia por cliente, de modo que uma frota de clientes variados funciona com um único servidor.

| Revisão | Ciclo de vida | Solicitação de aprovação (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (anunciada)|Sem handshake: `server/discover` e, em seguida, toda solicitação inclui `io.modelcontextprotocol/protocolVersion` e `io.modelcontextprotocol/clientCapabilities` em `_meta` (além dos cabeçalhos `Mcp-Protocol-Version`, `Mcp-Method` e `Mcp-Name` em HTTP).|Resultado `input_required`, que o cliente repete com `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|Handshake `initialize` / `notifications/initialized`.|Solicitação `elicitation/create` iniciada pelo servidor.|
|`2025-03-26`, `2024-11-05`|Como acima.|Como acima.|

<br />

O **stdio** serve todas as revisões em um único processo: a primeira solicitação de um cliente `2026-07-28` é servida sem handshake, e o `initialize` de um cliente mais antigo continua funcionando.  Não é preciso configurar nada para o Claude Desktop, os inicializadores npm / PyPI ou a imagem Docker.

O **Streamable HTTP** usa por padrão o modelo com estado, com uma sessão por cliente (`Mcp-Session-Id`), que o SDK serve para as revisões até `2025-11-25`.  Uma solicitação `2026-07-28` recebe como resposta o erro JSON-RPC `-32022` (versão de protocolo sem suporte; um simples HTTP 400 nas versões até `v0.12.718`), cujo `data.supported` lista as revisões com handshake, e `server/discover` anuncia o mesmo; assim, o cliente negocia uma revisão inferior, e as integrações HTTP existentes mantêm suas sessões e suas solicitações de aprovação inalteradas.  Defina `server.stateless` (ou fixe a revisão em `2026-07-28`, [abaixo](#pinning-the-revision)) para servir `2026-07-28` nativamente por HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Um servidor sem sessão não emite `Mcp-Session-Id`, responde a `GET` e `DELETE` com 405 (não há sessão a encerrar), mantém `tools/list`, `prompts/list` e `resources/list` invariáveis entre conexões e executa a ida e volta da aprovação por meio de `input_required`.  Ele ainda aceita o `initialize` de um cliente mais antigo e atende às leituras desse cliente, mas não consegue reter a capacidade de elicitação que esse cliente declarou na inicialização (cada solicitação recebe uma sessão efêmera); por isso, clientes mais antigos não conseguem aprovar gravações sujeitas a aprovação em um servidor sem sessão.  Escolha `stateless` para hosts da revisão atual e o padrão para uma frota que ainda inclua clientes mais antigos.

O servidor não mantém estado entre chamadas: o modo, a auditoria e as credenciais dos providers são configurações no nível do processo, então nada precisa ser movido para handles explícitos.

#### Fixar a revisão {#pinning-the-revision}

Por padrão, o servidor anuncia todas as revisões compatíveis e negocia por cliente.  A opção `--mcp.protocol.version`, ou `server.protocol_version` no objeto de configuração (a opção prevalece), define a revisão mais recente anunciada:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Valor | Anunciadas | Efeito |
|--|--|--|
|`auto` (padrão) ou ausente|Todas as revisões compatíveis.|Negociação como acima: a revisão mais alta que os dois lados falam.|
|`2026-07-28`|Somente `2026-07-28`.|Somente sem sessão, e `stateless` fica implícito para o Streamable HTTP.  O `initialize` de um cliente mais antigo recebe `2025-11-25` como resposta, o sinal para esse cliente se desconectar em vez de interpretar a resposta como o novo ciclo de vida.|
|`2025-11-25` (ou qualquer revisão anterior)|Essa revisão e todas as anteriores.|Somente o ciclo de vida com handshake: uma solicitação `2026-07-28` recebe o erro JSON-RPC `-32022` com `data.supported` listando as revisões anunciadas, para que o cliente possa renegociar.  Clientes mais antigos com handshake mantêm a própria revisão.|

<br />

Qualquer outro valor falha na validação da configuração na inicialização, e o erro informa os valores válidos.

#### Limites do transporte {#transport-limits}

Os corpos das solicitações do Streamable HTTP são limitados a 4 MiB (HTTP 413 acima disso), JSON aninhado com mais de 1000 níveis é rejeitado antes da análise, e um único quadro `stdio` é limitado a 8 MiB.  Nenhum desses limites é configurável; todos ficam muito acima de qualquer instrução SQL que o servidor aceita.

* * *

### (Re)carregamento de credenciais (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` e a ferramenta `reload_credentials` estão disponíveis nas versões do StackQL posteriores à `v0.10.542`.

:::

O bloco de ambiente de um processo é fixado quando o processo é criado.  Isso cria dois problemas para os servidores MCP:

- Um servidor MCP `stdio` iniciado por um cliente de desktop (Claude Desktop, Cursor etc.) sem as variáveis de ambiente das credenciais nunca consegue vê-las - o cliente passa o próprio ambiente para o subprocesso.
- Credenciais de curta duração rotacionadas nunca chegam a uma sessão de servidor de longa duração.

A opção `--env.file` resolve as duas lacunas ao indicar um arquivo no estilo dotenv que funciona como repositório mutável de credenciais:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Semântica do carregamento {#sourcing-semantics}

O arquivo é carregado no ambiente do processo na inicialização, em todos os pontos de entrada (`exec`, `shell`, `srv`, `registry`, `mcp`):

- Quando a opção está vazia ou não definida, nada acontece - as credenciais são resolvidas sob demanda a partir do ambiente do processo no momento da solicitação, exatamente o comportamento histórico.
- Um arquivo ausente no caminho informado é tolerado e não tem efeito (o arquivo pode ser criado depois).
- Um arquivo malformado ou ilegível causa um erro fatal na inicialização.
- Somente as chaves com valores não vazios são definidas; as variáveis de ambiente existentes do processo **são** sobrescritas (o arquivo é a fonte da verdade); nada é removido.
- A opção existe somente na linha de comando - ela não é lida do `.stackqlrc`.

O formato do arquivo é um `KEY=VALUE` por linha.  Comentários com `#`, linhas em branco, um prefixo `export ` opcional, aspas simples ou duplas opcionais em torno do valor e finais de linha CRLF são todos tolerados:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

A ferramenta [`reload_credentials`](/mcp/reload_credentials) recarrega o arquivo `--env.file` no ambiente do processo no meio da sessão e, em seguida, informa o status de resolução das credenciais de cada provider.  Os valores secretos nunca são retornados, registrados em log nem auditados - somente nomes e status.  Sem `--env.file` configurado, a ferramenta se reduz a uma simples verificação de status.

Quando uma consulta falha na resolução de credenciais, o erro MCP traz uma indicação que orienta o agente a chamar `reload_credentials` e tentar de novo; assim, os clientes agênticos se recuperam sozinhos: grave a credencial no arquivo, peça ao agente que recarregue, e a consulta que falhou é bem-sucedida na nova tentativa - sem precisar reiniciar o servidor.

`reload_credentials` não tem suporte no modo de backend `reverse_proxy` - as consultas são executadas no processo remoto do servidor stackql, então a ferramenta retorna um erro que orienta o usuário a recarregar no servidor de backend.

#### Windows e Claude Desktop {#windows-and-claude-desktop}

O `setx` grava no registro, não nos processos em execução, e o Claude Desktop passa o próprio ambiente (desatualizado) para os subprocessos MCP - por isso, alterações de credenciais normalmente exigem reiniciar completamente o Claude Desktop.  Com `--env.file`, o arquivo de credenciais pode ser criado ou rotacionado a qualquer momento, e `reload_credentials` o carrega sem nenhuma reinicialização.  Consulte [Usar o StackQL com o Claude Desktop](/getting-started/claude-desktop) para ver uma configuração completa.

* * *

### Autenticação de clientes HTTP {#http-client-authentication}

:::note

`server.auth_token_env_var` e `server.allow_unauthenticated` estão disponíveis a partir do StackQL `v0.12.742`.

:::

O transporte HTTP exige autenticação de clientes.  `server.auth_token_env_var` indica uma variável de ambiente que contém um token bearer pré-compartilhado; toda solicitação deve incluir `Authorization: Bearer <token>`, ou recebe `401` como resposta.  O token é lido do ambiente, então nunca aparece em `--mcp.config` nem nos argumentos do processo.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Autenticação do servidor obrigatória]

Servir sem token exige ativação explícita: defina `"allow_unauthenticated": true` em `server`.  Sem nenhuma das duas chaves, o servidor encerra na inicialização com uma mensagem que cita as duas; ele também encerra se a variável do token não estiver definida ou estiver vazia.  A partir da versão `v0.12.742`, você precisa adicionar `auth_token_env_var` ou `"allow_unauthenticated": true` a `server` ao usar o transporte `http`.

:::

O TLS (`tls_cert_file` / `tls_key_file`) criptografa o transporte, mas não autentica o cliente; combine-o com um token em qualquer listener além do loopback.  Solicitações de navegador entre origens (um `Origin` que não corresponde, ou um `Sec-Fetch-Site` entre sites) são recusadas com `403`, independentemente da autenticação.  O `stackql_mcp_client` incluído não envia o cabeçalho `Authorization`, então um servidor que ele aciona precisa ativar explicitamente o serviço sem autenticação.

A partir da `v1.0`, iniciar o servidor com [`--access.config`](/command-line-usage/access-management) substitui o controle por token compartilhado pela autenticação por chamador: cada solicitação HTTPS é verificada com o esquema configurado (JWT, bearer estático ou TLS mútuo) e autorizada de acordo com as regras de política.  Nesse caso, o TLS do servidor é obrigatório, a autenticação `local_os` é recusada por HTTP, e uma solicitação sem credenciais nunca recorre ao token do ambiente do servidor.  O gerenciamento de acesso não está disponível com o tipo de servidor `reverse_proxy`.

* * *

### Modos do servidor {#server-modes}

`server.mode` escolhe um de quatro contratos de segurança.  Os quatro permitem `SELECT` e leituras de metadados; eles diferem na forma como tratam mutações e operações de ciclo de vida.

| Modo | `SELECT` / metadados | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (ciclo de vida) |
|--|--|--|--|--|
|`read_only`|permite|recusa|recusa|recusa|
|`safe` (padrão)|permite|requer aprovação|requer aprovação|requer aprovação|
|`delete_safe`|permite|permite|requer aprovação|requer aprovação|
|`full_access`|permite|permite|permite|permite|

<br />

**Recusa** retorna imediatamente um erro ao cliente.

**Requer aprovação** usa o fluxo de elicitação do MCP, conforme a [revisão do protocolo](#protocol-revision-support) negociada:

- Se o cliente anunciou a capacidade de elicitação (na inicialização, ou nas capacidades do cliente em `_meta` de cada solicitação na `2026-07-28`), o servidor pede ao usuário que aprove a ação com uma mensagem curta (nome da ferramenta, classe da consulta, SQL).  Na `2026-07-28`, a solicitação de aprovação é retornada como um resultado `input_required` que contém uma solicitação de entrada `elicitation/create` com o id `stackql_approval`, e o cliente repete a chamada com a resposta em `inputResponses`; nas revisões anteriores, o próprio servidor envia a solicitação `elicitation/create` no meio da chamada.  O usuário aceita, recusa ou cancela.
- Se o cliente **não** anunciou a elicitação, a ferramenta é recusada com uma mensagem que explica a lacuna e indica ao operador o modo `full_access`.

Quem responde à aprovação é o cliente; por isso, ela protege contra um agente que age sem o seu usuário, não contra um chamador não confiável.  Combine o transporte HTTP com a [autenticação de clientes](#http-client-authentication) quando os chamadores não forem confiáveis.

O modo é global por servidor.  Não há substituição por ferramenta.

A partir da `v1.0`, [`run_select_query`](/mcp/run_select_query) recusa `INSERT`, `UPDATE`, `REPLACE`, `DELETE` e `EXEC` em todos os modos, incluindo `full_access`; o erro indica `run_mutation_query` ou `run_lifecycle_operation`.  Toda ferramenta também traz um `title` e as quatro indicações de comportamento do MCP como booleanos explícitos (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), derivadas da mesma classificação que o controle de políticas aplica.  `openWorldHint` é `true` para `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query` e `run_lifecycle_operation`, que acessam endpoints do registro ou dos providers, e `false` para as ferramentas que leem somente o estado local.  As indicações são apenas orientativas para a UX do cliente; a aplicação continua a cargo do controle de políticas.

:::info[Controle de payloads]

A partir da versão `v0.12.742`, um payload com várias instruções é controlado pela sua instrução mais privilegiada em todos os modos; assim, `SELECT 1; DELETE ...` é tratado como um `DELETE`, e o registro de auditoria traz `query_class` `mutation_delete`.  `validate_select_query` aceita exatamente uma instrução.

:::

#### Mudança de comportamento do modo padrão {#default-mode-behaviour-change}

As versões anteriores aplicavam a segurança somente por meio de uma única opção `read_only: true/false`, cujo valor padrão era "sem aplicação".  O padrão atual (`mode: safe`) faz com que as mutações exijam aprovação do usuário desde o início.  Os operadores que usam um cliente com suporte a elicitação (Claude Desktop, Cursor, Continue etc.) verão uma solicitação de aprovação por mutação.  Os operadores que executam automações, ou o `stackql_mcp_client` incluído (que não anuncia elicitação), precisam ativar explicitamente um modo menos restritivo: `mode: delete_safe` para executar `INSERT`, `UPDATE` e `REPLACE`, ou `mode: full_access` para executar também `DELETE` e operações de ciclo de vida.

A chave legada `read_only: true` em JSON / YAML ainda é aceita por compatibilidade com versões anteriores e é tratada como equivalente a `mode: read_only`.

#### Exemplos {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Log de auditoria {#audit-log}

Cada chamada de ferramenta grava um registro no destino de auditoria configurado, em JSONL por padrão ou como registros de log do OpenTelemetry (consulte [Formato do log](#log-format)).  A auditoria fica **ativada por padrão**.  A auditoria responde "o que o agente fez", não "o que o agente viu" - as linhas de resultado de instruções `SELECT` não são registradas, de propósito, em nenhum dos dois formatos.

#### O que é registrado {#what-gets-recorded}

| Campo | Descrição |
|--|--|
|`timestamp`|Horário de relógio do início da chamada (RFC3339).|
|`tool`|Nome da ferramenta (por exemplo, `run_select_query`).|
|`mode`|Modo do servidor em vigor no momento da chamada.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.  Para um payload com várias instruções, a classe da mais privilegiada.|
|`sql`|String SQL das ferramentas de consulta (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Campos da hierarquia para as ferramentas de metadados (`list_*`, `describe_method`); SQL + `row_limit` para as ferramentas de consulta.|
|`duration_ms`|Duração em tempo de relógio do controle + manipulador.|
|`error`|Mensagem de erro, se a ferramenta falhou ou foi recusada.|

<br />

As linhas de resultado de instruções `SELECT` são excluídas deliberadamente - elas podem ser muito grandes e conter dados confidenciais.

#### Destino de arquivo {#file-sink}

O único destino disponível hoje é `file`.  Um objeto JSON por linha, com fsync após cada registro e rotação no estilo lumberjack por tamanho / idade / número de backups.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Há duas formas de especificar o local:

- `audit.file.path` - um caminho de arquivo completo (absoluto ou relativo ao cwd).
- `audit.file.dir` - um diretório; o destino escolhe o nome base `stackql_mcp_server_<RFC3339-utc-second>.log` dentro dele.

Quando nenhum dos dois está definido em `mcp.config`, o servidor MCP usa o cwd como valor padrão de `dir`, para que os operadores existentes vejam o mesmo comportamento de antes.  O próprio pacote genérico de destino subjacente se recusa a escolher um diretório silenciosamente - a decisão de "onde os logs vão parar" é sempre do chamador.

O caminho absoluto resolvido é registrado em stderr na inicialização como `sink file: /path/to/file.log`.

#### Formato do log {#log-format}

O fluxo de auditoria tem duas codificações, gravadas no mesmo destino de arquivo:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

A opção substitui `server.audit.format` em `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` é compatível byte a byte com os registros descritos acima.  `otel` grava um objeto OTLP/JSON `LogsData` por linha, o formato que o receptor `otlp_json_file` do OpenTelemetry Collector ingere como está (verificado com o otelcol-contrib 0.160.0); assim, o fluxo chega a qualquer pipeline OTLP sem processador de transformação:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Cada chamada de ferramenta produz um registro de log; uma chamada que passou pelo controle de aprovação produz um segundo registro para a decisão de elicitação.  Os registros de uma mesma chamada compartilham `gen_ai.tool.call.id` e um ID de trace.  Um `traceparent` W3C fornecido pelo chamador em `params._meta` é respeitado; caso contrário, um ID de trace é gerado por sessão MCP, para que os registros de uma sessão de agente se correlacionem.

O conjunto de atributos emitidos é uma interface versionada: o escopo de instrumentação é `github.com/stackql/stackql/pkg/mcp_server/audit` na versão `1.0.0`, e `schemaUrl` fixa as convenções estáveis em `https://opentelemetry.io/schemas/1.44.0`.  Os atributos `gen_ai.*` e `mcp.*` seguem as convenções semânticas [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) e [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), que estão no status Development; a versão do escopo é incrementada quando o conjunto muda.

| Atributo | Registro | Valor |
|--|--|--|
|`service.name`, `service.version`|recurso|`stackql`, a versão do StackQL|
|`gen_ai.operation.name`|chamada de ferramenta|`execute_tool`|
|`mcp.method.name`|ambos|`tools/call` no registro da chamada de ferramenta, `elicitation/create` no registro da decisão|
|`gen_ai.tool.name`|ambos|Nome da ferramenta|
|`gen_ai.tool.call.id`|ambos|Gerado por chamada, compartilhado pelos dois registros|
|`mcp.protocol.version`|ambos|Revisão negociada|
|`mcp.session.id`|ambos|ID da sessão (ausente em solicitações HTTP sem sessão)|
|`stackql.mode`|ambos|Modo do servidor|
|`stackql.decision`|ambos|O valor de `decision` da tabela acima|
|`stackql.query`|chamada de ferramenta|SQL literal, para as ferramentas de consulta|
|`stackql.query_class`|chamada de ferramenta|O valor de `query_class`|
|`stackql.provider`|chamada de ferramenta|Provider, para as ferramentas de metadados|
|`stackql.query.source`|chamada de ferramenta|ID do item da Query Library, quando fornecido|
|`stackql.duration_ms`|chamada de ferramenta|Duração em tempo de relógio|
|`stackql.rows_returned`|chamada de ferramenta|Número de linhas, para as ferramentas que retornam linhas|
|`error.type`, `error.message`|chamada de ferramenta|Em caso de falha: a decisão de recusa, ou `tool_error` depois do controle|

<br />

O corpo é `execute_tool <tool>` / `elicitation <tool>`; a severidade é `INFO`, ou `ERROR` quando a chamada falhou ou foi recusada.  O contrato de ocultação é idêntico nos dois formatos: os valores dos resultados nunca são serializados, somente a instrução que os produziu.

#### Modos de falha {#failure-modes}

Quando o destino retorna um erro, o comportamento da resposta depende de `audit.failure_mode`.

| failure_mode | Efeito |
|--|--|
|`strict` (padrão)|A chamada de ferramenta retorna o erro de auditoria ao cliente, mesmo que a ferramenta subjacente tenha sido bem-sucedida.  É intencional: melhor uma resposta ambígua ao cliente do que um `DELETE` não detectado.|
|`strict_mutations`|Leituras de `SELECT` / metadados prosseguem com uma nota em stderr; mutações e operações de ciclo de vida expõem o erro de auditoria.|
|`best_effort`|Sempre registra em stderr e prossegue.|

#### Sequenciamento {#sequencing}

A gravação da auditoria acontece **depois** que a ferramenta é executada (ou barrada pelo controle), mas **antes** que a resposta volte ao cliente.  No modo strict, uma falha na gravação da auditoria em um DELETE bem-sucedido significa que a linha foi excluída, mas o cliente recebe um erro - intencionalmente, para que nenhuma mutação passe sem auditoria.

#### Desabilitar a auditoria {#disabling-audit}

Para desabilitar a auditoria por completo, como nas versões anteriores à `v0.10.474`, que não tinham auditoria:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Ferramentas MCP disponíveis {#available-mcp-tools}

Ao ser executado como servidor MCP, o StackQL expõe as ferramentas a seguir, sujeitas à lista de permissões `enabled_tools` configurada. Cada uma retorna tanto uma visualização em texto renderizada (para o LLM) quanto um payload estruturado e tipado (para clientes programáticos). A renderização de texto padrão é uma tabela markdown para resultados uniformes com várias linhas, ou um bloco KV em markdown para resultados esparsos ou de um único registro. As ferramentas com argumentos aceitam uma substituição opcional `format` (`markdown` ou `json`); caso contrário, o servidor usa a própria configuração de renderização. As entradas abaixo listam os argumentos específicos de cada tarefa; `?` marca as entradas opcionais.

Clique no nome de qualquer ferramenta para ver a página de referência completa, com as entradas, o comportamento de controle e um prompt de exemplo.

| Ferramenta | Renderizador | Descrição | Entradas |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Identidade e runtime do servidor: versão do stackql, mecanismo SQL de backend, local do registro de providers, modo, opção de somente leitura.  Chame sob demanda, por exemplo para diagnosticar uma operação recusada ou sujeita a controle, ou para verificar um comportamento específico de uma versão.|nenhuma|
|[`list_providers`](/mcp/list_providers)|Tabela|Providers já baixados para o cache local - o topo da hierarquia.|nenhuma|
|[`list_services`](/mcp/list_services)|Tabela|Serviços de um provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Tabela|Recursos de um `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Tabela|Métodos de acesso (operações HTTP) de um recurso, com o verbo SQL ao qual cada um corresponde e seus parâmetros obrigatórios.  Chame antes de escrever qualquer consulta - é aqui que os parâmetros obrigatórios do `WHERE` são inferidos.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Contrato de E/S completo de um método (sempre EXTENDED): entradas obrigatórias e opcionais, e os campos de saída que um `SELECT` pode referenciar.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Tabela|Encontrar templates de consulta selecionados pela intenção em linguagem natural. Somente leitura; os itens de mutação exigem ativação explícita e ficam excluídos no modo `read_only`.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Recuperar um template e as declarações dos seus parâmetros. Com `params` não vazio, validar os valores e retornar o SQL renderizado mais `next_tool`, sem executar.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Analisar e planejar um único `SELECT` sem executar.  Retorna `{valid, errors}`.  Somente `SELECT`; exatamente uma instrução.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Tabela|Executar um `SELECT`.  Retorna `{rows}`.  Somente leituras.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|Executar `INSERT`/`UPDATE`/`REPLACE`/`DELETE` no provider.  **Efeitos colaterais reais.** Retorna `{messages, timestamp}`.  Controlada pelo [modo](#server-modes) do servidor.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Executar uma operação de ciclo de vida `EXEC` do stackql.  Retorna `{messages, timestamp}`.  Controlada pelo [modo](#server-modes) do servidor.|`sql`|
|[`list_registry`](/mcp/list_registry)|Tabela|Providers (e suas versões) disponíveis no registro configurado.  Diferente de `list_providers`, que lista somente os providers já baixados.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Instalar um único provider do registro no cache local.  Afeta somente o estado do cache local - sem efeito no plano de controle ou no plano de dados da nuvem.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Tabela|Recarregar as credenciais do arquivo dotenv `--env.file` no ambiente do processo e informar o status de resolução de cada provider (`ok`, `unresolved`, `not_checked`).  Nunca retorna valores secretos.  Permitida em todos os modos.|`provider?`|

Consulte `query_library_search` antes de escrever SQL do zero e, em seguida, use `query_library_get` para inspecionar e renderizar um template correspondente. As ferramentas de consulta também aceitam um `source` opcional com o ID do item da biblioteca, apenas para atribuição; a execução continua sujeita ao modo do servidor e à política de aprovação normais.

:::note

`describe_resource` foi removida no StackQL `v0.12.732`; clientes MCP, prompts ou instruções de agentes que a chamavam devem chamar [`list_methods`](/mcp/list_methods) seguida de `describe_method`.  Um recurso não tem uma lista única de campos, porque cada método de acesso retorna o próprio formato; por isso, os nomes das colunas vêm de [`describe_method`](/mcp/describe_method) para o método ao qual a consulta é encaminhada.  A instrução SQL [`DESCRIBE`](/language-spec/describe) não mudou.

:::

### Prompts MCP disponíveis {#available-mcp-prompts}

Um prompt estático é publicado.

| Prompt | Descrição |
|--|--|
|`write_safe_select`|Orientações para escrever consultas `SELECT` seguras em recursos do stackql.  Explica como usar `SHOW METHODS IN <provider>.<service>.<resource>` para descobrir o método de leitura certo e os parâmetros obrigatórios do `WHERE`.|

* * *

### Opções {#flags}

| Opção | Descrição |
|--|--|
|`--mcp.server.type`|Tipo de servidor MCP: `http`, `stdio` ou `reverse_proxy` (este último é usado com `stackql srv`).|
|`--mcp.config`|Objeto de configuração JSON do servidor MCP.  YAML também é aceito.|
|`--mcp.log.format`|Codificação do log de auditoria: `jsonl` (padrão) ou `otel` (registros de log OTLP/JSON do OpenTelemetry).  Substitui `server.audit.format`.  Consulte [Formato do log](#log-format).|
|`--mcp.protocol.version`|Revisão mais recente do protocolo anunciada: `auto` (padrão), `2026-07-28` ou uma revisão anterior, como `2025-11-25`.  Consulte [Fixar a revisão](#pinning-the-revision).|
|`--env.file`|Arquivo de credenciais no estilo dotenv, carregado na inicialização e recarregado sob demanda pela ferramenta `reload_credentials`.  Consulte [(Re)carregamento de credenciais](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|Porta TCP do servidor do protocolo de comunicação do PostgreSQL (usada com `stackql srv`).|
|`-h`, `--help`|Exibe as informações de ajuda.|
|`-v`, `--verbose`|Executa no modo detalhado, com saída adicional.|

&nbsp;
&nbsp;
> consulte [Opções globais](/command-line-usage/global-flags) para ver opções adicionais

:::info

Você precisa definir as variáveis de ambiente necessárias para a autenticação do provider antes de iniciar o servidor MCP, ou indicar um arquivo de credenciais no estilo dotenv com [`--env.file`](#credential-resourcing---envfile--reload_credentials).  Para obter mais informações, consulte [Usar um provider](/getting-started/using-a-provider).

:::

* * *

### Exemplos {#examples-1}

#### Servidor MCP independente básico {#basic-standalone-mcp-server}

Inicie um servidor MCP independente com autenticação do provider e um token bearer para os clientes:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor MCP com servidor PostgreSQL (em memória) {#mcp-server-with-postgresql-server-in-memory}

Execute os servidores MCP e PostgreSQL para ter acesso pelos dois protocolos:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor de inventário somente leitura com auditoria em um caminho conhecido {#read-only-inventory-server-with-audit-to-a-known-path}

Útil para um agente de varredura de inventário que nunca deve fazer mutações:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor de automação com acesso total {#full-access-automation-server}

Para pipelines de automação confiáveis que precisam de mutações e não conseguem responder a solicitações de elicitação.  O pipeline apresenta o token bearer armazenado em `STACKQL_MCP_TOKEN`:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Servidor MCP seguro com TLS {#secure-mcp-server-with-tls}

Inicie um servidor MCP com criptografia TLS no modo proxy reverso:

Primeiro, gere certificados TLS (autoassinados, para desenvolvimento):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Em seguida, inicie o servidor:

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Testar o servidor MCP {#testing-your-mcp-server}

O repositório inclui um cliente MCP de desenvolvimento (`stackql_mcp_client`) para scripts e testes de regressão.  Compile-o com:

```bash
python cicd/python/build.py --build-mcp-client
```

Isso gera `./build/stackql_mcp_client`.

Observe que o `stackql_mcp_client` não envia o cabeçalho `Authorization`, então o servidor de destino precisa definir `"allow_unauthenticated": true` (como fazem os exemplos acima); um servidor protegido por token responde a ele com `401`.  Ele também **não** anuncia a capacidade de elicitação do MCP - ele serve para scripts não interativos.  Com um servidor `safe`, ele recebe a recusa "client does not support elicitation" nas chamadas de mutação e de ciclo de vida; com um servidor `delete_safe`, nas chamadas `DELETE` e `EXEC`.  Para testes interativos com solicitações de elicitação, use um cliente MCP incorporado a um editor (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation (a DELETE) - succeeds only against a full_access server:
# stackql_mcp_client does not support elicitation, so safe and delete_safe
# servers refuse it.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Respostas de exemplo {#example-responses}

`server_info` (em um servidor no modo padrão, StackQL `v1.0.888` no Windows via stdio):

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (em um servidor `full_access`):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Linha de exemplo do log de auditoria para a mesma mutação:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Integração com assistentes de IA {#integration-with-ai-assistants}

Para integrar o servidor MCP do StackQL a um assistente de IA, registre `stackql` como servidor MCP na configuração do assistente.  A maioria dos clientes MCP incorporados a editores executa o servidor por `stdio`; para esses, use `--mcp.server.type=stdio`, e o assistente inicia o processo diretamente.  Agentes independentes que falam HTTP podem se conectar a um processo `stackql mcp --mcp.server.type=http` de longa duração.

Para o Claude Desktop, incluindo a listagem no Anthropic Connector Directory (o método de instalação recomendado), o MCP Bundle (`.mcpb`) para download e a configuração manual, consulte [Usar o StackQL com o Claude Desktop](/getting-started/claude-desktop).

:::note

O local do arquivo de configuração varia conforme o sistema operacional e o assistente de IA.  Consulte a documentação do seu assistente de IA para saber o caminho e o formato corretos.

:::

* * *

### Considerações de arquitetura {#architecture-considerations}

Ao escolher um modo de implantação, considere:

1. **Independente (`stackql mcp`)**: a configuração mais simples, ideal para desenvolvimento e para clientes incorporados a editores por `stdio`.
2. **Servidor duplo em memória (`stackql srv` com `mcp.server.type=http`)**: um único processo serve tanto o protocolo MCP quanto o protocolo de comunicação do PostgreSQL.  Melhor desempenho, adequado para a maioria das implantações em produção.
3. **Proxy reverso (`stackql srv` com `mcp.server.type=reverse_proxy`)**:
   - permite separar as cargas de trabalho em processos ou hosts
   - oferece suporte a criptografia TLS para endpoints MCP seguros
   - permite escalar as interfaces MCP e PostgreSQL de forma independente
   - oferece flexibilidade para a integração com CAs corporativas (observação: o suporte a CAs corporativas é experimental)

E, separadamente, sobre segurança:

- Comece com o padrão `mode: safe`.  Os clientes com suporte a elicitação (Claude Desktop, Cursor, Continue) pedem confirmação ao usuário antes de qualquer mutação.
- Fixe `mode: read_only` para agentes de inventário ou de análise que nunca devem gravar.
- Fixe `mode: delete_safe` quando quiser liberdade para criar/atualizar, mas quiser um humano no circuito para as operações destrutivas.
- Reserve `mode: full_access` para pipelines de automação confiáveis, e somente em conjunto com um log de auditoria que alguém revise.
- Defina `server.auth_token_env_var` em qualquer listener HTTP que outros processos ou hosts possam alcançar; `allow_unauthenticated` é para desenvolvimento local.  O TLS sozinho não autentica clientes.

:::caution

Ao usar TLS com autoridades certificadoras (CAs) corporativas, pode ser necessária configuração adicional.  Essa funcionalidade é experimental e pode exigir ajustes de acordo com a implementação específica da sua CA.

:::

* * *

### Leitura adicional {#further-reading}

- A referência para desenvolvedores do pacote é o [README de `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) no repositório stackql.
- A documentação técnica voltada a colaboradores fica em [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) no repositório stackql e inclui respostas de exemplo completas para todas as ferramentas.
