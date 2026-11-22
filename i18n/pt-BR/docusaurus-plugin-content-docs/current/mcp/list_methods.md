---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Ferramenta MCP que lista os métodos de acesso de um recurso do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna os métodos de acesso (operações HTTP) vinculados a um recurso - os métodos de leitura, as mutações e as operações de ciclo de vida disponíveis.  Esta é a ferramenta que um agente deve chamar **antes de escrever qualquer consulta**, porque os parâmetros `WHERE` obrigatórios são inferidos a partir do método de leitura escolhido.  Escolha o método cujo verbo SQL corresponda à intenção e cujos parâmetros obrigatórios possam ser fornecidos e, em seguida, chame [`describe_method`](/mcp/describe_method) para esse método, para obter o contrato completo, incluindo os campos de saída.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Sim|Nome do provider.|
|`service`|Sim|Serviço desse provider.|
|`resource`|Sim|Recurso em `provider.service`.|

<br/>

:::info

Os identificadores não podem conter espaços em branco, pontos e vírgulas nem barras invertidas; uma entrada desse tipo é recusada antes que qualquer instrução seja montada.

:::

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | Uma linha por método de acesso, incluindo o verbo SQL ao qual ele corresponde e os parâmetros obrigatórios |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Somente leitura.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
