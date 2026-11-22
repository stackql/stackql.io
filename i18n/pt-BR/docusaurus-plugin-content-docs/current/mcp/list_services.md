---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Ferramenta MCP que lista os serviços de um provider do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna os serviços expostos por um único provider - o segundo nível da [hierarquia de recursos](/getting-started/resource-hierarchy) do StackQL.  Chame esta ferramenta depois de [`list_providers`](/mcp/list_providers) para reduzir a superfície de busca antes de se aprofundar nos recursos.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Sim|Nome do provider (por exemplo, `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Os identificadores não podem conter espaços em branco, pontos e vírgulas nem barras invertidas; uma entrada desse tipo é recusada antes que qualquer instrução seja montada.

:::

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | Uma linha por serviço do provider informado |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Somente leitura.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado:

```
List the services available in Google Cloud
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
