---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Ferramenta MCP que lista os recursos de um provider.service do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna os recursos de um `provider.service` - o terceiro nível da [hierarquia de recursos](/getting-started/resource-hierarchy) do StackQL.  Os recursos são os alvos que aparecem na cláusula `FROM` de uma consulta do StackQL.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Sim|Nome do provider.|
|`service`|Sim|Serviço desse provider.|

<br/>

:::info

Os identificadores não podem conter espaços em branco, pontos e vírgulas nem barras invertidas; uma entrada desse tipo é recusada antes que qualquer instrução seja montada.

:::

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | Uma linha por recurso de `provider.service` |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Somente leitura.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
List the resources in the google.storage service
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
