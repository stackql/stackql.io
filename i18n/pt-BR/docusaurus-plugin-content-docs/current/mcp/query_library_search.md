---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: Ferramenta MCP que pesquisa templates de consulta do StackQL com curadoria por intenção em linguagem natural, provider, serviço e tags
image: "/img/stackql-featured-image.png"
---

Pesquisa na [Query Library do StackQL](pathname:///docs/query-library/) os templates que correspondem a uma intenção em linguagem natural. Consulte-a antes de escrever SQL do zero e, em seguida, passe o `id` de um resultado para [`query_library_get`](/mcp/query_library_get) para inspecionar o template e validar os parâmetros dele.

A ferramenta apenas recupera conteúdo da biblioteca. Ela não executa SQL, não chama APIs na nuvem e não exige credenciais de provider.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`intent`|Sim|Descrição da tarefa em linguagem natural.|
|`provider`|Não|Filtra por provider, por exemplo `aws`.|
|`service`|Não|Filtra por serviço, por exemplo `s3`.|
|`tags`|Não|Array de tags. Uma entrada precisa corresponder a todas as tags fornecidas.|
|`include_mutations`|Não|Inclui templates de mutação. Valor padrão: `false`; ignorado no modo `read_only`, em que as entradas de mutação são sempre excluídas.|
|`limit`|Não|Número máximo de resultados. Valor padrão: `5` quando omitido ou não positivo; limitado a `20`.|
|`format`|Não|Renderização do texto: `markdown` ou `json`. Quando omitido, usa a configuração de renderização do servidor (`markdown` por padrão).|

* * *

## Saída {#output}

A visualização de texto padrão é uma tabela classificada com `id`, `title`, `mutation`, `required_params` e `score`, seguida da citação da origem e, quando houver, das orientações para o caso sem correspondência. O payload estruturado contém:

| Campo | Descrição |
|--|--|
|`hits`|Entradas classificadas com `id`, `title`, `description` opcional, `providers`, `services` e `required_params`, além de `mutation` e `score`. Use `query_library_get` em seguida para obter as declarações de parâmetros completas.|
|`miss`|`true` quando não há resultados ou quando a pontuação mais alta fica abaixo do limite de correspondência. Correspondências mais próximas com pontuação baixa ainda podem ser retornadas.|
|`guidance`|No caso sem correspondência, direciona o agente às instruções do servidor, às ferramentas de descoberta e a `validate_select_query` para escrever uma consulta.|
|`source_tier`|Origem do conteúdo: `primary`, `fallback` ou `snapshot`.|
|`source_url`|URL ou caminho incorporado do catálogo usado, para citação.|
|`stale`|Quando `true`, o conteúdo foi servido do cache ou do snapshot incorporado após uma falha ao buscá-lo.|

Entradas obsoletas são excluídas. O servidor mantém o catálogo em cache e, em vez da biblioteca hospedada, pode recorrer à fonte dela no GitHub e a um snapshot incorporado.

* * *

## Controle {#gating}

Permitida em todos os modos do servidor, sujeita à lista de permissões de ferramentas configurada. Não são necessárias aprovação nem credenciais de provider. Pesquisar templates de mutação não autoriza a execução deles.

* * *

## Exemplo {#example}

Argumentos da ferramenta:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Use um `id` de `hits`, como `aws/s3/buckets-list`, com [`query_library_get`](/mcp/query_library_get). Se `miss` for `true`, inspecione os templates mais próximos para obter orientação ou use [`list_methods`](/mcp/list_methods) e [`describe_method`](/mcp/describe_method) antes de escrever o SQL.
