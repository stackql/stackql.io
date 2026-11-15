---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Ferramenta MCP que executa um SELECT do StackQL em um provider
image: "/img/stackql-featured-image.png"
---

Executa uma instrução `SELECT` nos providers configurados e retorna as linhas do resultado.  Somente leitura - mutações e operações de ciclo de vida não são aceitas; para elas, use [`run_mutation_query`](/mcp/run_mutation_query) ou [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

As linhas do resultado são retornadas ao cliente, mas ficam excluídas propositalmente do log de auditoria.

Veja também:
[[ Visão geral do MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT na especificação da linguagem ]](/language-spec/select)

* * *

## Entradas {#inputs}

| Argumento | Obrigatório | Descrição |
|--|--|--|
|`sql`|Sim|Uma instrução `SELECT`.  As instruções de metadados (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) são classificadas como somente leitura pelo controle de políticas e também são aceitas; `DESC` é reconhecido a partir do StackQL `v0.12.732`.|
|`row_limit`|Não|Limite do número de linhas retornadas ao cliente.  `0` (padrão) significa sem limite do lado do cliente.|
|`source`|Não|ID do item da Query Library quando o SQL veio de [`query_library_get`](/mcp/query_library_get). Serve apenas para atribuição; não altera a execução nem as aprovações.|
|`format`|Não|Renderização do texto: `markdown` ou `json`. Quando omitido, usa a configuração de renderização do servidor.|

* * *

## Saída {#output}

| Renderizador | Formato |
|--|--|
| Tabela | `{rows: [...]}` - um objeto por linha, indexado pelo nome da coluna |

* * *

## Controle {#gating}

Permitida em todos os modos do servidor (`read_only`, `safe`, `delete_safe`, `full_access`) quando todas as instruções do payload são somente leitura.  A partir da `v1.0`, esta ferramenta recusa `INSERT`, `UPDATE`, `REPLACE`, `DELETE` e `EXEC` em todos os modos, incluindo `full_access`, com um erro que indica [`run_mutation_query`](/mcp/run_mutation_query) ou [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Um payload com várias instruções é classificado pela instrução mais privilegiada que ele contém, então uma mutação depois de um `SELECT` inicial é recusada da mesma forma.  A ferramenta declara `destructiveHint: false` e `openWorldHint: true`.

* * *

## Exemplo {#example}

Experimente este prompt em qualquer cliente MCP compatível com elicitação que tenha o servidor MCP do StackQL registrado:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Veja um exemplo no Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
