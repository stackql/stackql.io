---
slug: /ai-agents
title: Agentes de IA
hide_title: false
keywords:
  - stackql
  - agentes de IA
  - mcp
  - model context protocol
  - agéntico
description: Conecte agentes de IA a servicios en la nube y de SaaS con StackQL mediante servidores MCP, herramientas, MCP integrado y consultas SQL.
image: "/img/stackql-featured-image.png"
---

StackQL ofrece a los agentes de IA la misma interfaz SQL que usan las personas para consultar, aprovisionar y operar servicios en la nube y de SaaS. Un agente descubre lo que expone un provider, valida una consulta antes de ejecutarla, lee el estado en vivo con `SELECT` y lo modifica con `INSERT`, `UPDATE` y `DELETE`, sin archivo de estado y sin un SDK por cada nube.

## Cómo se conectan los agentes {#how-agents-connect}

- **MCP sobre stdio** para clientes de editor y de escritorio como Claude Desktop, Cursor y Continue. El propio cliente inicia `stackql mcp --mcp.server.type=stdio`. Consulte [Servidor MCP](/command-line-usage/mcp).
- **MCP sobre HTTP** para agentes independientes y automatización que se conectan a un proceso `stackql mcp` o `stackql srv` de larga duración, con autenticación mediante token bearer y TLS opcional.
- **MCP integrado** para aplicaciones compiladas: el servidor se ejecuta en proceso en Go, Rust, Swift, Kotlin, .NET y Gleam. Consulte [MCP integrado](/mcp/embedded).
- **Protocolo de red de PostgreSQL** mediante [`stackql srv`](/command-line-usage/srv) para marcos de agentes, notebooks y herramientas de BI que ya hablan Postgres.

## Inicio rápido con Claude Desktop {#quick-start-with-claude-desktop}

Instale desde el Connector Directory de Anthropic (recomendado) o desde el MCP Bundle descargable, o registre el servidor manualmente en `claude_desktop_config.json`:

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

Incluya solo las credenciales de los providers que vaya a usar. Las instrucciones completas, incluidas las descargas del bundle y el uso de un archivo de credenciales, se encuentran en [Usar StackQL con Claude Desktop](/getting-started/claude-desktop).

## Qué puede hacer un agente {#what-an-agent-can-do}

El servidor MCP expone un conjunto de herramientas pequeño y tipado. Cada herramienta devuelve una vista renderizada para el modelo y una carga útil estructurada para los clientes programáticos.

- **Descubrir**: `list_providers`, `list_services`, `list_resources`, `list_methods` y `describe_method` recorren la jerarquía del provider y devuelven las entradas y los campos de salida de cada operación, de modo que los parámetros de `WHERE` obligatorios se conocen antes de escribir una consulta.
- **Validar y leer**: `validate_select_query` analiza y planifica un `SELECT` sin ejecutarlo; `run_select_query` lo ejecuta.
- **Modificar**: `run_mutation_query` ejecuta `INSERT`, `UPDATE`, `REPLACE` y `DELETE`; `run_lifecycle_operation` ejecuta operaciones de ciclo de vida `EXEC`. Ambas están controladas por el modo del servidor que se describe más abajo.
- **Mantenimiento**: `server_info`, `list_registry`, `pull_provider` y `reload_credentials`.

La referencia completa, con las entradas, el comportamiento del control y un prompt de ejemplo por herramienta, está en [Herramientas MCP](/mcp).

## Barreras de protección {#guardrails}

El modo del servidor establece el contrato de seguridad de toda la sesión:

| Modo | `SELECT` / metadatos | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | permitir | rechazar | rechazar |
| `safe` (valor predeterminado) | permitir | requiere aprobación | requiere aprobación |
| `delete_safe` | permitir | permitir | requiere aprobación |
| `full_access` | permitir | permitir | permitir |

"Requiere aprobación" usa la elicitación de MCP: los clientes que la admiten (Claude Desktop, Cursor, Continue) muestran al usuario el nombre de la herramienta, la clase de consulta y el SQL, y esperan una decisión antes de ejecutar nada; los clientes que no la anuncian reciben un rechazo. Cada llamada a una herramienta se escribe en un log de auditoría, los listeners HTTP requieren un token bearer salvo que se abran explícitamente con `allow_unauthenticated`, y las credenciales se obtienen del entorno o de un archivo dotenv sin que los valores secretos se devuelvan nunca al cliente. Los detalles están en [Modos del servidor](/command-line-usage/mcp#server-modes), [Autenticación de clientes HTTP](/command-line-usage/mcp#http-client-authentication) y [Log de auditoría](/command-line-usage/mcp#audit-log).

## Pasos siguientes {#next-steps}

- [Servidor MCP](/command-line-usage/mcp) - modos de despliegue, configuración, modos del servidor, log de auditoría y opciones.
- [Herramientas MCP](/mcp) - la referencia completa de las herramientas, con entradas, control y prompts de ejemplo.
- [MCP integrado](/mcp/embedded) - ejecute el servidor dentro de su propia aplicación Go, Rust, Swift, Kotlin, .NET o Gleam.
- [Query Library](pathname:///docs/query-library/) - SQL listo para ejecutar para preguntas habituales sobre operaciones en la nube, por provider.
- [Referencia de IA](/ai) - conceptos, guías prácticas, comparaciones y solución de problemas escritos para los agentes y para las personas que los crean.
