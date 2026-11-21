---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Ferramenta MCP que lista os providers e as versões disponíveis no registro de providers do StackQL
image: "/img/stackql-featured-image.png"
---

Retorna os providers (e suas versões) disponíveis no registro de providers do StackQL configurado.  É diferente de [`list_providers`](/mcp/list_providers), que informa apenas os providers já baixados no cache local.

Passe o argumento opcional `provider` para listar as versões publicadas de um único provider.  Use em conjunto com [`pull_provider`](/mcp/pull_provider) quando o agente precisar instalar algo que ainda não está disponível localmente.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Comando registry ]](/command-line-usage/registry)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Não|Nome do provider.  Quando informado, o resultado se restringe às versões publicadas desse provider; quando omitido, todas as entradas do registro são retornadas.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | Uma linha por entrada do registro (provider e metadados de versão) |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Sem efeito no plano de controle nem no plano de dados da nuvem - apenas o registro é lido.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
