---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Ferramenta MCP que executa uma operação de ciclo de vida EXEC do StackQL
image: "/img/stackql-featured-image.png"
---

Executa uma operação de ciclo de vida `EXEC` do StackQL - as ações de provider que não são CRUD (iniciar/parar uma VM, rotacionar uma chave, acionar um workflow etc.).  **Efeitos colaterais reais.**

Controlado pelo [modo](/command-line-usage/mcp#server-modes) do servidor:

| Modo | `EXEC` de ciclo de vida |
|--|--|
|`read_only`|recusado|
|`safe` (padrão)|requer aprovação (elicitação)|
|`delete_safe`|requer aprovação|
|`full_access`|permitido|

<br/>

:::info

Um payload com várias instruções é controlado pela instrução mais privilegiada que ele contém

:::

Quando a aprovação é necessária e o cliente não anunciou a elicitação, a chamada é recusada.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC na especificação da linguagem ]](/language-spec/exec)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`sql`|Sim|Uma instrução `EXEC` do StackQL direcionada a um método de ciclo de vida.|
|`source`|Não|ID do item da Query Library quando o SQL veio de [`query_library_get`](/mcp/query_library_get). Serve apenas para atribuição; não altera a execução nem as aprovações.|
|`format`|Não|Renderização do texto: `markdown` ou `json`. Quando omitido, usa a configuração de renderização do servidor.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

O cliente MCP exibirá uma solicitação de aprovação antes de a operação ser enviada, a menos que o servidor esteja em execução no modo `full_access`.

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
