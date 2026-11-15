---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Ferramenta MCP que executa INSERT, UPDATE, REPLACE ou DELETE em um provider por meio do StackQL
image: "/img/stackql-featured-image.png"
---

Executa `INSERT`, `UPDATE`, `REPLACE` ou `DELETE` no provider.  **Efeitos colaterais reais** - uma chamada bem-sucedida altera o estado na nuvem.

Controlado pelo [modo](/command-line-usage/mcp#server-modes) do servidor:

| Modo | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|recusado|recusado|
|`safe` (padrão)|requer aprovação (elicitação)|requer aprovação|
|`delete_safe`|permitido|requer aprovação|
|`full_access`|permitido|permitido|

<br/>

:::info

Um payload com várias instruções é controlado pela instrução mais privilegiada que ele contém

:::

Quando uma mutação requer aprovação e o cliente não anunciou a capacidade de elicitação do MCP, a chamada é recusada.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT na especificação da linguagem ]](/language-spec/insert) [[ DELETE na especificação da linguagem ]](/language-spec/delete)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`sql`|Sim|Uma instrução `INSERT`, `UPDATE`, `REPLACE` ou `DELETE`.|
|`source`|Não|ID do item da Query Library quando o SQL veio de [`query_library_get`](/mcp/query_library_get). Serve apenas para atribuição; não altera a execução nem as aprovações.|
|`format`|Não|Renderização do texto: `markdown` ou `json`. Quando omitido, usa a configuração de renderização do servidor.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV | `{messages: [...], timestamp}` - confirmação de envio, não o corpo da resposta da API |

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

O cliente MCP exibirá uma solicitação de aprovação antes de a exclusão ser enviada, a menos que o servidor esteja em execução no modo `full_access`.

O clipe a seguir mostra o fluxo de elicitação quando o servidor MCP do StackQL está em execução no modo padrão `safe` - o cliente recebe uma solicitação para aprovar o `DELETE` antes que qualquer alteração do lado da nuvem seja enviada.  Você pode reproduzir isso no seu próprio servidor MCP do StackQL com o [MCP Inspector](https://github.com/modelcontextprotocol/inspector) oficial, usando `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
