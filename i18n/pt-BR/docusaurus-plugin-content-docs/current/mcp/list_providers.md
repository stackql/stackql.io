---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Ferramenta MCP que lista os providers para nuvem e SaaS disponíveis no servidor MCP do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna os providers já baixados no cache local do StackQL - o topo da [hierarquia de recursos](/getting-started/resource-hierarchy) do StackQL.  Use esta ferramenta como ponto de entrada quando um agente precisar descobrir o que pode consultar.

Para os providers disponíveis no *registro* (incluindo os que ainda não foram baixados), consulte [`list_registry`](/mcp/list_registry).

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Entradas {#inputs}

Nenhuma.

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela (tabela em markdown para o LLM) | Uma linha por provider |

<br />

Cada linha contém o nome do provider e os metadados de versão conforme publicados pelo registro.

* * *

## Controle {#gating}

Permitida em todos os modos do servidor (`read_only`, `safe`, `delete_safe`, `full_access`).  Somente leitura.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
Which cloud providers are available for you to query right now?
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
