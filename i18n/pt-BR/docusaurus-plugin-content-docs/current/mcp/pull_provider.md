---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Ferramenta MCP que instala um provider do StackQL a partir do registro no cache local
image: "/img/stackql-featured-image.png"
---

Instala um único provider do registro do StackQL no cache local do approot.  Quando `version` é omitido, a versão publicada mais recente é baixada.

Grava apenas o estado do cache local - não há efeito no plano de controle nem no plano de dados da nuvem, por isso esta ferramenta é permitida em todos os modos do servidor (incluindo `read_only`).  Use [`list_registry`](/mcp/list_registry) para descobrir o nome do provider e as versões disponíveis antes de baixá-lo.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Comando registry ]](/command-line-usage/registry)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`provider`|Sim|Nome do provider conforme publicado no registro (por exemplo, `aws`, `google`, `cloudflare`).|
|`version`|Não|Versão publicada a ser baixada.  Quando omitida, a versão publicada mais recente é instalada.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| KV | Confirmação de que o provider (e a versão) foi instalado no cache local |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor.  Sem efeitos no lado da nuvem; apenas o estado do cache local muda.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP que tenha o servidor MCP do StackQL registrado:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
