---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Ferramenta MCP que analisa e planeja um SELECT do StackQL sem executá-lo
image: "/img/stackql-featured-image.png"
---

Analisa e planeja uma única instrução `SELECT` sem executá-la.  Retorna `{valid, errors}`.  É útil quando um agente quer verificar se a consulta compila e se vincula ao método descoberto antes de se comprometer com uma chamada real à API.

:::info

Somente `SELECT`, e exatamente uma instrução: um payload com mais de uma instrução é recusado com `validate_select_query accepts exactly one statement`.

:::

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`sql`|Sim|Uma única instrução `SELECT`.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV | `valid` (bool) e `errors` (array de strings, vazio quando válido) |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Nenhuma chamada de API é feita.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
