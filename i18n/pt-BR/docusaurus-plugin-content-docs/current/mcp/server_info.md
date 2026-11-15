---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Ferramenta MCP que retorna a identidade e o runtime do servidor MCP do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna a identidade e o runtime do servidor MCP do StackQL: versão, mecanismo SQL subjacente, local do registro de providers, modo do servidor e o indicador legado de somente leitura.  Chame esta ferramenta uma vez no início da sessão para que o agente saiba a que está conectado.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Entradas {#inputs}

Nenhuma.

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV (bloco de chave/valor em markdown para o LLM) | Payload estruturado com os campos abaixo |

<br/><br/>

| Campo | Descrição |
|--|--|
|`version`|Versão do build do StackQL.|
|`commit`|Hash curto do commit Git.|
|`build_date`|Carimbo de data/hora do build em RFC3339.|
|`platform`|`os/arch` (por exemplo, `linux/amd64`).|
|`transport`|`http` ou `stdio`.|
|`sql_backend`|Mecanismo SQL subjacente (por exemplo, `sqlite3`).|
|`provider_registry`|URL do registro de providers.|
|`mode`|Modo do servidor ativo - `read_only`, `safe`, `delete_safe` ou `full_access`.|
|`is_read_only`|Indicador booleano legado (`true` se e somente se `mode == read_only`).|

* * *

## Controle {#gating}

Sempre permitida em todos os modos do servidor.  Não é auditada como mutação.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
