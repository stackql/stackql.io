---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: Ferramenta MCP que recupera um template de consulta do StackQL e valida os parâmetros fornecidos para retornar o SQL renderizado sem executá-lo
image: "/img/stackql-featured-image.png"
---

Recupera uma entrada da [Query Library](pathname:///docs/query-library/) pelo `id` retornado por [`query_library_search`](/mcp/query_library_search).

Com `params` omitido ou vazio, ela retorna o template bruto, as declarações de parâmetros e as notas. Com um objeto `params` não vazio, o servidor valida os valores e os substitui no template. Ela retorna SQL somente quando a renderização é bem-sucedida. O modelo não precisa substituir os valores por conta própria.

Esta ferramenta não executa SQL nem verifica credenciais na nuvem. Essa validação de parâmetros é diferente da feita por [`validate_select_query`](/mcp/validate_select_query), que analisa e planeja um `SELECT` com base no provider instalado.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`id`|Sim|ID da consulta obtido nos resultados da pesquisa, por exemplo `aws/s3/buckets-list`.|
|`params`|Não|Objeto que mapeia os nomes dos parâmetros declarados para valores. Omita-o ou passe `{}` para inspecionar o template sem renderizá-lo.|
|`format`|Não|Renderização do texto: `markdown` ou `json`. Quando omitido, usa a configuração de renderização do servidor (`markdown` por padrão).|

Parâmetros desconhecidos são rejeitados. Parâmetros obrigatórios sem valor fornecido nem valor padrão são informados em `missing_params`. Os valores fornecidos são verificados em relação aos tipos e às restrições declarados; placeholders não resolvidos são erros.

* * *

## Saída {#output}

A visualização de texto padrão é um bloco KV com a identidade, o status de mutação, a próxima ferramenta, a validade, os links da origem e, quando houver, o template, o SQL renderizado ou os erros. Use o payload estruturado ou `format: "json"` para obter os metadados completos:

| Campo | Descrição |
|--|--|
|`id`, `title`, `description`|Identidade e resumo da entrada.|
|`mutation`, `verb`|Se a entrada altera recursos e, quando declarado, o tipo de operação dela (`select`, `mutation` ou `lifecycle`).|
|`next_tool`|Ferramenta de execução da entrada: `run_select_query`, `run_mutation_query` ou `run_lifecycle_operation`. A recuperação não a chama.|
|`params`|Declarações de parâmetros com nomes, tipos, indicadores de obrigatoriedade e, quando houver, valores padrão, descrições, exemplos, enums ou padrões.|
|`template`, `notes`|Template bruto e notas do autor, retornados quando `params` é omitido ou está vazio.|
|`rendered`, `valid`|Se a renderização foi tentada e se foi bem-sucedida. Ambos são `false` ao inspecionar o template bruto.|
|`sql`|SQL renderizado, presente somente após uma renderização bem-sucedida.|
|`missing_params`, `unknown_params`, `errors`|Detalhes da validação quando a renderização falha. Não execute o SQL até que eles sejam resolvidos.|
|`auth`, `permissions`|Quando declaradas, as variáveis de ambiente de credenciais e as permissões nativas do provider necessárias para a execução. A recuperação em si não exige nenhuma delas.|
|`cost`, `outputs`, `related`|Quando declarados, indicações de fan-out e de custo, descrições das colunas do resultado e IDs de entradas relacionadas.|
|`doc_url`|Página da biblioteca legível por humanos.|
|`source_tier`, `source_url`, `stale`|Procedência do conteúdo: `primary`, `fallback` ou `snapshot`, a URL ou o caminho incorporado correspondente e se o conteúdo foi servido do cache ou do snapshot após uma falha ao buscá-lo.|

Um ID desconhecido ou malformado retorna um erro. Use `query_library_search` para encontrar IDs válidos.

* * *

## Controle {#gating}

Permitida em todos os modos do servidor, sujeita à lista de permissões de ferramentas configurada. Recuperar ou renderizar um template de mutação ou de ciclo de vida não tem efeito do lado da nuvem e não requer aprovação. Executar o SQL retornado é uma chamada separada, regida pelo [modo](/command-line-usage/mcp#server-modes) do servidor.

* * *

## Exemplo {#example}

Primeiro, inspecione uma entrada:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Em seguida, forneça a região de roteamento que ela exige:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Depois de verificar `rendered` e `valid`, valide o `SELECT` retornado com [`validate_select_query`](/mcp/validate_select_query). Execute-o com a ferramenta indicada em `next_tool`, passando o `sql` retornado e definindo `source` como o `id` do item da biblioteca, para fins de atribuição. As credenciais e as permissões do provider são necessárias no momento da execução.
