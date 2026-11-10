---
slug: /ai-agents
title: KI-Agenten
hide_title: false
keywords:
  - stackql
  - ki-agenten
  - mcp
  - model context protocol
  - agentisch
description: KI-Agenten über MCP-Server, Tools, eingebettete Integrationen und SQL-Abfragen mit StackQL an Cloud- und SaaS-Services anbinden.
image: "/img/stackql-featured-image.png"
---

StackQL gibt KI-Agenten dieselbe SQL-Schnittstelle, die Menschen zum Abfragen, Bereitstellen und Betreiben von Cloud- und SaaS-Services verwenden. Ein Agent ermittelt, was ein Provider bereitstellt, validiert eine Abfrage vor der Ausführung, liest den Live-Zustand mit `SELECT` und ändert ihn mit `INSERT`, `UPDATE` und `DELETE`, ohne State-Datei und ohne SDK pro Cloud.

## Wie sich Agenten verbinden {#how-agents-connect}

- **MCP über stdio** für Editor- und Desktop-Clients wie Claude Desktop, Cursor und Continue. Der Client startet `stackql mcp --mcp.server.type=stdio` selbst. Siehe [MCP-Server](/command-line-usage/mcp).
- **MCP über HTTP** für eigenständige Agenten und Automatisierung, die sich mit einem dauerhaft laufenden `stackql mcp`- oder `stackql srv`-Prozess verbinden, mit Bearer-Token-Authentifizierung und optionalem TLS.
- **Eingebettetes MCP** für kompilierte Anwendungen: Der Server läuft im Prozess in Go, Rust, Swift, Kotlin, .NET und Gleam. Siehe [Eingebettetes MCP](/mcp/embedded).
- **PostgreSQL-Wire-Protokoll** über [`stackql srv`](/command-line-usage/srv) für Agenten-Frameworks, Notebooks und BI-Tools, die bereits Postgres sprechen.

## Schnellstart mit Claude Desktop {#quick-start-with-claude-desktop}

Installieren Sie aus dem Anthropic Connector Directory (empfohlen) oder dem herunterladbaren MCP Bundle, oder registrieren Sie den Server von Hand in `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

Geben Sie nur die Anmeldedaten für die Provider an, die Sie verwenden möchten. Die vollständige Anleitung, einschließlich der Bundle-Downloads und der Verwendung einer Datei mit Anmeldedaten, finden Sie unter [StackQL mit Claude Desktop verwenden](/getting-started/claude-desktop).

## Was ein Agent tun kann {#what-an-agent-can-do}

Der MCP-Server stellt einen kleinen, typisierten Satz von Tools bereit. Jedes Tool liefert eine gerenderte Ansicht für das Modell und eine strukturierte Payload für programmatische Clients.

- **Entdecken**: `list_providers`, `list_services`, `list_resources`, `list_methods` und `describe_method` durchlaufen die Provider-Hierarchie und geben die Eingaben und Ausgabefelder jeder Operation zurück, sodass erforderliche `WHERE`-Parameter bekannt sind, bevor eine Abfrage geschrieben wird.
- **Validieren und lesen**: `validate_select_query` parst und plant ein `SELECT`, ohne es auszuführen; `run_select_query` führt es aus.
- **Ändern**: `run_mutation_query` führt `INSERT`, `UPDATE`, `REPLACE` und `DELETE` aus; `run_lifecycle_operation` führt `EXEC`-Lebenszyklusoperationen aus. Beide unterliegen dem unten beschriebenen Servermodus.
- **Verwaltung**: `server_info`, `list_registry`, `pull_provider` und `reload_credentials`.

Die vollständige Referenz mit Eingaben, Freigabeverhalten und einem Beispiel-Prompt pro Tool finden Sie unter [MCP-Tools](/mcp).

## Schutzmechanismen {#guardrails}

Der Servermodus legt den Sicherheitsvertrag für die gesamte Sitzung fest:

| Modus | `SELECT` / Metadaten | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | erlaubt | abgelehnt | abgelehnt |
| `safe` (Standard) | erlaubt | Freigabe erforderlich | Freigabe erforderlich |
| `delete_safe` | erlaubt | erlaubt | Freigabe erforderlich |
| `full_access` | erlaubt | erlaubt | erlaubt |

"Freigabe erforderlich" verwendet MCP-Elicitation: Clients, die sie unterstützen (Claude Desktop, Cursor, Continue), zeigen dem Benutzer den Tool-Namen, die Abfrageklasse und das SQL an und warten auf eine Entscheidung, bevor etwas ausgeführt wird; Clients, die sie nicht ankündigen, werden abgelehnt. Jeder Tool-Aufruf wird in ein Audit-Log geschrieben, HTTP-Listener erfordern ein Bearer-Token, und Anmeldedaten stammen aus der Umgebung oder einer dotenv-Datei, ohne dass geheime Werte jemals an den Client zurückgegeben werden. Details finden Sie unter [Servermodi](/command-line-usage/mcp#server-modes), [HTTP-Client-Authentifizierung](/command-line-usage/mcp#http-client-authentication) und [Audit-Log](/command-line-usage/mcp#audit-log).

## Nächste Schritte {#next-steps}

- [MCP-Server](/command-line-usage/mcp) - Deployment-Modi, Konfiguration, Servermodi, Audit-Log und Flags.
- [MCP-Tools](/mcp) - die vollständige Tool-Referenz mit Eingaben, Freigabeverhalten und Beispiel-Prompts.
- [Eingebettetes MCP](/mcp/embedded) - den Server in Ihrer eigenen Go-, Rust-, Swift-, Kotlin-, .NET- oder Gleam-Anwendung ausführen.
- [Query Library](pathname:///docs/query-library/) - sofort ausführbares SQL für gängige Fragen des Cloud-Betriebs, nach Provider.
- [KI-Referenz](/ai) - Konzepte, Anleitungen, Vergleiche und Fehlerbehebung, geschrieben für Agenten und die Menschen, die sie bauen.
