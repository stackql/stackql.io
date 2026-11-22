---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: Ferramenta MCP que recarrega as credenciais do arquivo dotenv de --env.file e informa o status de resolução por provider
image: "/img/stackql-featured-image.png"
---

Recarrega no ambiente do processo do servidor o arquivo no estilo dotenv indicado pela opção [`--env.file`](/command-line-usage/global-flags) e, em seguida, informa o status de resolução das credenciais de cada provider.  Isso resolve o problema do ambiente fixado na criação do processo: credenciais gravadas ou rotacionadas em disco no meio da sessão chegam ao servidor em execução sem que ele precise ser reiniciado.

O arquivo de ambiente se sobrepõe ao ambiente do processo em vez de substituí-lo: a resolução de credenciais sempre lê o ambiente atual do processo no momento da solicitação, então as variáveis já presentes na criação do processo (do shell ou do bloco `env` do cliente) continuam funcionando com ou sem `--env.file`.  Na recarga, somente as chaves presentes no arquivo com valores não vazios são gravadas e nenhuma variável é removida, então uma recarga com falha ou parcial preserva as credenciais que já funcionavam.

Os valores dos segredos nunca são retornados, registrados em log nem auditados - a ferramenta informa somente nomes de variáveis e status.  Sem `--env.file` configurado, a ferramenta se reduz a uma simples verificação de status.  Quando uma consulta falha na resolução de credenciais, o erro MCP traz uma indicação que orienta o agente a chamar `reload_credentials` e tentar novamente, para que os clientes agênticos se recuperem sozinhos.

:::note

Disponível nas versões do StackQL posteriores à `v0.10.542`.

:::

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ (Re)carregamento de credenciais ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Não|Restringe o relatório de status a um provider.  Quando omitido, todos os providers são incluídos no relatório.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | Resultado do carregamento do arquivo de ambiente e uma linha por provider com o status de resolução das credenciais |

<br/>

O payload estruturado contém:

| Campo | Descrição |
|--|--|
|`env_file`|O caminho de `--env.file` configurado (vazio quando nenhum está configurado).|
|`env_file_sourced`|`true` quando o arquivo foi encontrado e carregado nesta chamada.|
|`sourced_vars`|Nomes das variáveis de ambiente definidas a partir do arquivo - somente nomes, nunca valores.|
|`providers`|Uma linha por provider (veja abaixo).|

<br/>

Cada linha de `providers` tem:

| Coluna | Descrição |
|--|--|
|`provider`|Nome do provider.|
|`auth_type`|Tipo de autenticação configurado para o provider.|
|`sourced_from`|De onde a credencial foi resolvida, por exemplo `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` ou `none`.|
|`status`|`ok`, `unresolved` ou `not_checked`.|
|`detail`|Texto do erro quando o status é `unresolved`.|

<br/>

Os tipos de autenticação que se autenticam fora do caminho dos bytes de credenciais (gcloud interativo, Azure CLI, credenciais de cliente oauth2, cadeias de roles assumidas, assinatura OCI, null) informam `not_checked`.

* * *

## Controle {#gating}

Permitida em todos os modos do servidor, incluindo `read_only` - a ferramenta não altera nada além do próprio ambiente do processo do servidor.

Não é compatível com o modo de backend `reverse_proxy`: as consultas são executadas no processo do servidor stackql remoto, então a ferramenta retorna um erro que orienta o usuário a recarregar as credenciais no servidor subjacente.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado (iniciado com `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
