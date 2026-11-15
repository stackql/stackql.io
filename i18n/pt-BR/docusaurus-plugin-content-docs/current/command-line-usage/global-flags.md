---
title: Opções globais
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Opções globais do binário stackql: autenticação, formato de saída, local do registro, logs e configurações do backend SQL, disponíveis em todos os comandos."
image: "/img/stackql-featured-image.png"
---

As opções globais especificam o comportamento do programa em tempo de execução para a aplicação StackQL; essas opções são definidas na linha de comando quando a aplicação StackQL é iniciada (no modo interativo/shell ou no modo exec não interativo) ou são obtidas do arquivo de inicialização do StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)).  As opções incluem:  

| Opção | Tipo de dados | Descrição | Valor padrão |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Configuração de acesso de chamadores, um envelope JSON `{"path": "/path/to/access.json"}` (um caminho de arquivo simples também é aceito).  Habilita o [gerenciamento de acesso](/command-line-usage/access-management): os chamadores são autenticados por JWT, token bearer estático, TLS mútuo ou identidade local do SO, e as instruções são autorizadas com base em regras de política.  As credenciais de token locais são lidas de `STACKQL_CALLER_TOKEN`.  Disponível a partir do StackQL `v1.0`. | |
| <span class="nowrap">`--acid`</span> | string | String JSON / YAML que representa a configuração ACID | `{}` |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Permite que uma sessão selecione um repositório de segredos persistente do SO (macOS Keychain, Linux Secret Service ou Windows Credential Manager) com `SET "$.secret_store"`.  Repositórios em arquivo simples são recusados mesmo com a opção.  Consulte [Sessões de provider e segredos](/getting-started/provider-sessions#persistent-os-secret-stores).  Disponível a partir do StackQL `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Permite que valores de segredos e de credenciais sejam desreferenciados em expressões SQL (`"$secrets.name"`, `"$auth.provider.field"`), com a consequência de que eles podem aparecer em resultados, logs e artefatos de consulta persistidos.  Não é necessária para a autenticação normal de providers.  Consulte [Valores de segredos no SQL](/getting-started/provider-sessions#secret-values-in-sql).  Disponível a partir do StackQL `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Tempo limite das solicitações de API em segundos, 0 para nenhum tempo limite | `45` |
| <span class="nowrap">`--approot`</span> | string | Caminho raiz da configuração e do cache da aplicação | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Modo de arquivo da configuração e do cache da aplicação | `493` |
| <span class="nowrap">`--auth`</span> | string | Pares chave-valor do contexto `auth` em formato json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Número inicial de chaves do cache | `100` |
| <span class="nowrap">`--configfile`</span> | string | Caminho completo do arquivo de configuração | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Arquivo `cpuprofile`, nenhum se vazio | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Profundidade máxima de dependências do fluxo de dados para uma determinada consulta | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | Número máximo de componentes fracamente conexos do fluxo de dados para uma determinada consulta | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | String JSON / YAML para configurar o tratamento das consultas de manutenção do SGBD | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Delimitador para a saída csv. Somente um caractere.  Ignorado em todas as saídas que não sejam csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Opção `dryrun`; executa somente o pré-processador, e a saída processada pelo template é retornada | `false` |
| <span class="nowrap">`--env.file`</span> | string | Arquivo opcional no estilo dotenv carregado no ambiente do processo na inicialização (todos os comandos); a ferramenta MCP [`reload_credentials`](/mcp/reload_credentials) o carrega novamente sob demanda. Um arquivo ausente é tolerado; chaves com valores vazios são ignoradas; variáveis existentes são sobrescritas. Não é carregado a partir do `.stackqlrc`. Disponível nas versões do StackQL posteriores à `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Limite de concorrência para a execução de consultas | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Prefixo de alias de exportação (namespace ou esquema) | |
| <span class="nowrap">`--gc`</span> | string | String JSON / YAML que representa a configuração de GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Desabilita os cabeçalhos de coluna, válido somente com `--output csv` ou `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Exibe informações das solicitações http no terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Host do proxy (deixe em branco para não usar proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Senha do proxy | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Porta do proxy; qualquer número menor ou igual a 0 resulta na porta padrão do esquema em questão (por exemplo, `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Esquema do proxy, `http` ou `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Usuário do proxy | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Número máximo de resultados por solicitação http; qualquer número menor ou igual a 0 remove a limitação | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Número máximo de páginas de resultados retornadas por recurso; qualquer número menor ou igual a 0 remove a limitação | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Profundidade máxima para consultas indiretas: views e subconsultas | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Arquivo de entrada (arquivo IQL) do qual as consultas são lidas | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Arquivo de contexto (dados) para o uso de templates (arquivo `json` ou `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Nível de log, deve ser um de {`info`, `warn`, `debug`, `fatal`} | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Objeto de configuração JSON / YAML do [servidor MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` e `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Codificação do log de auditoria do MCP, `jsonl` ou `otel` (registros de log OTLP/JSON); substitui `server.audit.format` em `--mcp.config`.  Consulte [Formato do log](/command-line-usage/mcp#log-format).  Disponível a partir do StackQL `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Revisão mais recente do protocolo MCP anunciada: `auto`, `2026-07-28` (somente sem sessão) ou uma revisão mais antiga, como `2025-11-25`; substitui `server.protocol_version` em `--mcp.config`.  Consulte [Fixar a revisão](/command-line-usage/mcp#pinning-the-revision).  Disponível a partir do StackQL `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Tipo de servidor MCP, um de {`http`, `stdio`, `reverse_proxy`}.  Consulte [Tipo de servidor MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL dos documentos de metadados em cache, em segundos | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | String JSON / YAML que representa namespaces para cache, views etc | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Trabalha offline, usando dados em cache | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Arquivo de saída no qual os resultados são gravados | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Formato de saída, deve ser um de {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} (`ndjson` é aceito como alias de `jsonl`).  `otel` emite registros de log do OpenTelemetry, consulte [Modos de saída](/getting-started/output-modes#otel-output); disponível a partir do StackQL `v0.11.660`.  Com um exportador configurado em `--otel.config`, os registros `otel` também são enviados para esse endpoint | `table` |
| <span class="nowrap">`--otel.config`</span> | string | String JSON / YAML que configura o formato de saída `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` e `exporter.batch_size` enviam os registros para um endpoint de logs OTLP/HTTP, consulte [Enviar para um endpoint OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Disponível a partir do StackQL `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | String JSON que habilita funcionalidades em versão prévia.  `{"unstable": true}` expõe as relações `stackql_unstable_<provider>.<service>.<resource>` orientadas por documentos, que o `omnisdk` resolve e executa diretamente a partir dos documentos de provider, com as linhas transmitidas à medida que são produzidas; as demais chaves (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) ajustam esse backend.  As funcionalidades em versão prévia não são estáveis: o comportamento, a sintaxe e a saída podem mudar ou deixar de funcionar entre versões sem aviso prévio, portanto não dependa delas em produção.  A partir da `v1.0`, a mesma ativação explícita expõe as relações `stackql_preview.iac.run` e `stackql_preview.iac.destroy`, que aplicam e desmontam um conjunto declarado de recursos por meio do `omnisdk`, com o estado mantido na raiz da aplicação.  Consulte [Funcionalidades em versão prévia](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--querycachesize`</span> | integer | Tamanho, em número de entradas, do cache LRU de planos de consulta | `10000` |
| <span class="nowrap">`--registry`</span> | string | String JSON / YAML que configura o local do registro de providers e a verificação de assinatura dos documentos de provider | consulte [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | String JSON / YAML que representa a configuração da sessão | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | String JSON / YAML que representa a configuração do sistema de backend SQL | consulte [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | String JSON / YAML que representa a configuração do armazenamento de transações | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Caminho para o bundle de CAs; se não for especificado, os padrões do sistema são usados |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Permite confiar em certificados inseguros (não recomendado) |  |
| <span class="nowrap">`--var`</span> | string | Variáveis externas fornecidas como pares chave-valor para uso em consultas do StackQL por meio de arquivos de dados `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Saída detalhada | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Ajuda contextual do stackql | |
| <span class="nowrap">`--version`</span> | | Exibe a versão do programa StackQL | |

## `--registry` {#--registry}

A opção `--registry` configura de onde os documentos de provider são lidos e se as assinaturas deles são verificadas.  O valor é uma string JSON com as seguintes chaves:

| Chave | Descrição | Valor padrão |
| -- | -- | -- |
| `url` | URL do registro.  Quando ela é omitida, o StackQL usa o registro público de providers do StackQL. | registro público |
| `localDocRoot` | Um diretório local com documentos de provider, lidos no lugar do download a partir do registro.  Usado ao [desenvolver um provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Prefixo de caminho, sob a raiz do registro, onde ficam os documentos de origem dos providers. | `src` |
| `verifyConfig.nopVerify` | `true` ignora a verificação de assinatura.  Defina como `false` para exigir uma assinatura ML-DSA-65 (FIPS 204) incorporada em todos os documentos de provider e de serviço lidos, incluindo as relações em versão prévia baseadas em documentos. | `true` |
| `verifyConfig.publicKeys` | Um objeto JSON que mapeia IDs de chave para chaves públicas ML-DSA-65 codificadas em hexadecimal, para registros privados.  Ele substitui as chaves de produção incorporadas; um objeto vazio explícito é um erro. | chaves incorporadas |

### Exemplo: exigir documentos de provider assinados {#example-require-signed-provider-documents}

Ativa a verificação para todos os documentos de provider e de serviço lidos do registro público, com as chaves de produção incorporadas:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Exemplo: ler documentos não assinados de um diretório local {#example-read-unsigned-documents-from-a-local-directory}

Aponta o registro para um diretório de documentos de provider em desenvolvimento.  Esses documentos não são assinados, portanto a verificação permanece desativada:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

A verificação de assinatura está disponível a partir do StackQL `v1.0` e vem desativada por padrão.  As assinaturas cobrem os bytes YAML exatos de um documento, portanto qualquer edição, incluindo uma alteração de formatação ou de quebra de linha, as invalida; assine novamente com a ferramenta `pqsig sign-doc` do registro.  Documentos de desenvolvimento locais não assinados devem manter `nopVerify: true`.  As antigas opções de certificado e os arquivos sidecar `.sig` não são mais usados, e os arquivos compactados publicados somente com sidecars precisam ser publicados novamente antes do uso com verificação.  A verificação autentica os documentos quando eles são lidos; o download e a extração de arquivos compactados não estão cobertos.

:::

## `--sqlBackend` {#--sqlbackend}

A opção `--sqlBackend` configura um backend SQL externo para o StackQL, o que permite usar um mecanismo de banco de dados externo (como o PostgreSQL) em vez do mecanismo SQL incorporado padrão. Isso é útil para aproveitar um banco de dados externo no processamento de consultas, no armazenamento persistente ou na integração com uma infraestrutura de banco de dados existente.

O valor é uma string JSON com as seguintes chaves:

| Chave | Descrição | Valores de exemplo |
| -- | -- | -- |
| `dbEngine` | O tipo de mecanismo de banco de dados | `postgres_tcp` |
| `sqlDialect` | O dialeto SQL a ser usado | `postgres` |
| `dsn` | O nome da fonte de dados (string de conexão) do banco de dados | Consulte os exemplos abaixo |

### Exemplo: usar um backend PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Exemplo: usar a interface PostgreSQL do Databricks {#example-using-databricks-postgresql-interface}

O Databricks expõe uma interface compatível com PostgreSQL que pode ser usada como backend SQL do StackQL. O DSN usa o formato padrão de string de conexão do PostgreSQL com as suas credenciais do Databricks:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Caracteres especiais no nome de usuário ou na senha (como `@`) devem ser codificados para URL no DSN (por exemplo, `@` se torna `%40`).
:::
