---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Ferramenta MCP que retorna o contrato de E/S completo de um método de acesso do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna o contrato de E/S completo de um único método de acesso - sempre EXTENDED, ou seja, as entradas, o esquema do corpo da solicitação e o formato completo da saída.  É aqui que um agente obtém os nomes das colunas: cada linha traz um `param_type` com o valor `input_required` (um predicado `WHERE` obrigatório e de correspondência exata), `input_optional` ou `output` (um campo que um `SELECT` ou um `RETURNING` pode referenciar).  Um recurso não tem uma lista única de campos, porque cada método retorna seu próprio formato; por isso, chame esta ferramenta para o método escolhido em [`list_methods`](/mcp/list_methods) antes da primeira consulta a ele.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Sim|Nome do provider.|
|`service`|Sim|Serviço desse provider.|
|`resource`|Sim|Recurso em `provider.service`.|
|`method`|Sim|Nome do método conforme retornado por [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Os identificadores não podem conter espaços em branco, pontos e vírgulas nem barras invertidas; uma entrada desse tipo é recusada antes que qualquer instrução seja montada.

:::

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV | Uma linha por campo de entrada e de saída: nome, tipo, `param_type` (`input_required`, `input_optional` ou `output`), formato e descrição |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Somente leitura.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
