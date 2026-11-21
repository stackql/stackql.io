---
slug: /ai-agents
title: Agentes de IA
hide_title: false
keywords:
  - stackql
  - agentes de IA
  - mcp
  - model context protocol
  - agêntico
description: Conecte agentes de IA a serviços na nuvem e SaaS com o StackQL por meio de servidores MCP, ferramentas, integrações incorporadas e consultas SQL.
image: "/img/stackql-featured-image.png"
---

O StackQL oferece aos agentes de IA a mesma interface SQL que as pessoas usam para consultar, provisionar e operar serviços na nuvem e SaaS. Um agente descobre o que um provider expõe, valida uma consulta antes de executá-la, lê o estado em tempo real com `SELECT` e o altera com `INSERT`, `UPDATE` e `DELETE`, sem arquivo de estado e sem um SDK por nuvem.

## Como os agentes se conectam {#how-agents-connect}

- **MCP sobre stdio** para clientes de editor e de desktop, como o Claude Desktop, o Cursor e o Continue. O próprio cliente inicia `stackql mcp --mcp.server.type=stdio`. Consulte [Servidor MCP](/command-line-usage/mcp).
- **MCP sobre HTTP** para agentes independentes e automações que se conectam a um processo `stackql mcp` ou `stackql srv` de longa duração, com autenticação por token bearer e TLS opcional.
- **MCP incorporado** para aplicações compiladas: o servidor é executado no mesmo processo, em Go, Rust, Swift, Kotlin, .NET e Gleam. Consulte [MCP incorporado](/mcp/embedded).
- **Protocolo de comunicação do PostgreSQL** por meio do [`stackql srv`](/command-line-usage/srv) para frameworks de agentes, notebooks e ferramentas de BI que já falam Postgres.

## Início rápido com o Claude Desktop {#quick-start-with-claude-desktop}

Instale pelo Anthropic Connector Directory (recomendado) ou pelo MCP Bundle disponível para download, ou registre o servidor manualmente em `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

Inclua apenas as credenciais dos providers que você pretende usar. As instruções completas, incluindo os downloads do bundle e o uso de um arquivo de credenciais, estão em [Usar o StackQL com o Claude Desktop](/getting-started/claude-desktop).

## O que um agente pode fazer {#what-an-agent-can-do}

O servidor MCP expõe um conjunto de ferramentas pequeno e tipado. Cada ferramenta retorna uma visualização renderizada para o modelo e um payload estruturado para clientes programáticos.

- **Descobrir**: `list_providers`, `list_services`, `list_resources`, `list_methods` e `describe_method` percorrem a hierarquia do provider e retornam as entradas e os campos de saída de cada operação, de modo que os parâmetros `WHERE` obrigatórios são conhecidos antes de a consulta ser escrita.
- **Validar e ler**: `validate_select_query` analisa e planeja um `SELECT` sem executá-lo; `run_select_query` o executa.
- **Alterar**: `run_mutation_query` executa `INSERT`, `UPDATE`, `REPLACE` e `DELETE`; `run_lifecycle_operation` executa operações de ciclo de vida `EXEC`. Ambas são controladas pelo modo do servidor, descrito abaixo.
- **Manutenção**: `server_info`, `list_registry`, `pull_provider` e `reload_credentials`.

A referência completa, com entradas, comportamento de controle e um prompt de exemplo por ferramenta, está em [Ferramentas MCP](/mcp).

## Barreiras de proteção {#guardrails}

O modo do servidor define o contrato de segurança de toda a sessão:

| Modo | `SELECT` / metadados | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | permite | recusa | recusa |
| `safe` (padrão) | permite | requer aprovação | requer aprovação |
| `delete_safe` | permite | permite | requer aprovação |
| `full_access` | permite | permite | permite |

"Requer aprovação" usa a elicitação do MCP: os clientes compatíveis com ela (Claude Desktop, Cursor, Continue) mostram ao usuário o nome da ferramenta, a classe da consulta e o SQL e aguardam uma decisão antes de qualquer execução; os clientes que não a anunciam são recusados. Cada chamada de ferramenta é gravada em um log de auditoria, os listeners HTTP exigem um token bearer, a menos que sejam abertos explicitamente com `allow_unauthenticated`, e as credenciais vêm do ambiente ou de um arquivo dotenv, sem que nenhum valor de segredo seja retornado ao cliente. Os detalhes estão em [Modos do servidor](/command-line-usage/mcp#server-modes), [Autenticação de clientes HTTP](/command-line-usage/mcp#http-client-authentication) e [Log de auditoria](/command-line-usage/mcp#audit-log).

## Próximos passos {#next-steps}

- [Servidor MCP](/command-line-usage/mcp) - modos de implantação, configuração, modos do servidor, log de auditoria e opções.
- [Ferramentas MCP](/mcp) - a referência completa das ferramentas, com entradas, controle e prompts de exemplo.
- [MCP incorporado](/mcp/embedded) - execute o servidor dentro da sua própria aplicação em Go, Rust, Swift, Kotlin, .NET ou Gleam.
- [Query Library](pathname:///docs/query-library/) - SQL pronto para executar, por provider, para perguntas comuns de operações na nuvem.
- [Referência de IA](/ai) - conceitos, guias práticos, comparações e solução de problemas escritos para agentes e para as pessoas que os criam.
