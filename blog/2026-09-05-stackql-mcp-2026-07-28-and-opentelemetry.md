---
slug: stackql-mcp-2026-07-28-and-opentelemetry
title: "StackQL update: MCP protocol revision 2026-07-28 and OpenTelemetry output"
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-mcp-server-featured-image.png"
description: "StackQL v0.11 brings the MCP server up to protocol revision 2026-07-28, and adds an OpenTelemetry (OTLP/JSON) output format for the agent audit log so any OTel collector can consume it without a custom parser."
keywords: [stackql, mcp, model context protocol, 2026-07-28, opentelemetry, otlp, observability, ai agents, claude, audit log, genai semantic conventions]
tags: [stackql, mcp, model context protocol, opentelemetry, ai agents, ai]
---

[__StackQL v0.11__](https://github.com/stackql/stackql/releases/tag/v0.11.669) is out. The [__StackQL MCP server__](/docs/command-line-usage/mcp): now includes the current Model Context Protocol revision, `2026-07-28`, alongside every earlier revision it already supported, and the audit log that records what an agent did can be written as OpenTelemetry log records instead of the bespoke JSONL format. Both are available today through every install channel.

<!-- truncate -->

## MCP protocol revision 2026-07-28

`2026-07-28` is the largest revision of the protocol since it launched. Relevant features for the StackQL MCP server incldue:

- The `initialize` / `notifications/initialized` handshake is gone. Every request carries the protocol version and the client's capabilities in its `_meta` block, and a client discovers a server with a single `server/discover` call.
- Protocol-level sessions are gone from Streamable HTTP. There is no `Mcp-Session-Id` header, and `tools/list`, `prompts/list` and `resources/list` must return the same answer on every connection.
- Server-to-client requests such as elicitation no longer happen mid-call. A server that needs the user's input returns an `input_required` result, and the client retries the call with the answer attached (multi round-trip requests, SEP-2322).

The StackQL MCP server negotiates per client, so a mixed fleet works against one server:

| Revision | Lifecycle | Approval prompt in `safe` / `delete_safe` mode |
|---|---|---|
| `2026-07-28` | No handshake; version and capabilities in `_meta` on every request | `input_required` result, retried with `inputResponses` |
| `2025-11-25`, `2025-06-18` | `initialize` handshake | Server-initiated `elicitation/create` request |
| `2025-03-26`, `2024-11-05` | as above | as above |

The gated-write flow is the headline feature of the server, so it was the acceptance test for this work: a mutation in `safe` mode still stops for approval, on both revisions, over both transports, and the audit record still says whether the user accepted, declined or dismissed the prompt. Nothing changes in how you run the server or in the SQL an agent writes.

### stdio

The stdio transport serves every revision on one process. A current client's first request is served without a handshake, and an older client's `initialize` still works. If you use Claude Desktop, the npm or PyPI launchers, or the Docker image, there is nothing to configure.

### Streamable HTTP and the `stateless` option

Over HTTP the protocol removed sessions, and the two models cannot share one listener. By default the server keeps the stateful, session-per-client model, which serves revisions up to `2025-11-25`. A `2026-07-28` client learns that from `server/discover` and negotiates down, so existing HTTP integrations keep their sessions and their approval prompts unchanged.

To serve `2026-07-28` natively over HTTP, set `stateless` on the server:

```bash
stackql mcp --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9992", "stateless": true} }'
```

A sessionless server issues no `Mcp-Session-Id`, keeps the list endpoints connection-invariant, and runs the approval round trip through `input_required`. It still accepts an older client's `initialize` and serves reads to it, but it cannot retain the elicitation capability that client declared at handshake time, so an older client cannot approve gated writes on a sessionless server. Pick `stateless` for current-revision hosts, and leave the default for a fleet that still includes older clients. The [__protocol revision support__](/docs/command-line-usage/mcp#protocol-revision-support) section of the docs has the full matrix, alongside the [__server modes__](/docs/command-line-usage/mcp#server-modes) it interacts with.

## OpenTelemetry output for the audit log

Every tool call the server handles writes one audit record: the tool, the server mode, the gate decision, the verbatim SQL, the duration and any error. Until now that record was a line of StackQL-specific JSON, which meant a custom parser between the file and whatever you wanted to do with it.

This release adds a second format. `--mcp.log.format=otel` writes the same records as OpenTelemetry log records, in the OTLP/JSON encoding, one export payload per line:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel
```

The default, `jsonl`, is unchanged byte for byte, and the format can also be set in `mcp.config` as `"audit": {"format": "otel"}`. The destination is the same rotating file either way. The [__log format__](/docs/command-line-usage/mcp#log-format) reference lists every option and attribute.

### What a record looks like

A `SELECT` run through the server produces a record like this (pretty-printed; the file holds it on one line):

```json
{
  "resourceLogs": [{
    "resource": {"attributes": [
      {"key": "service.name", "value": {"stringValue": "stackql"}},
      {"key": "service.version", "value": {"stringValue": "0.10.623"}}
    ]},
    "scopeLogs": [{
      "scope": {"name": "github.com/stackql/stackql/pkg/mcp_server/audit", "version": "1.0.0"},
      "logRecords": [{
        "timeUnixNano": "1788652800123456789",
        "observedTimeUnixNano": "1788652800123987654",
        "severityNumber": 9,
        "severityText": "INFO",
        "body": {"stringValue": "execute_tool run_select_query"},
        "attributes": [
          {"key": "gen_ai.operation.name", "value": {"stringValue": "execute_tool"}},
          {"key": "mcp.method.name", "value": {"stringValue": "tools/call"}},
          {"key": "gen_ai.tool.name", "value": {"stringValue": "run_select_query"}},
          {"key": "gen_ai.tool.call.id", "value": {"stringValue": "3f1c9a6b2e8d4a70"}},
          {"key": "mcp.protocol.version", "value": {"stringValue": "2026-07-28"}},
          {"key": "stackql.mode", "value": {"stringValue": "safe"}},
          {"key": "stackql.decision", "value": {"stringValue": "allow"}},
          {"key": "stackql.query", "value": {"stringValue": "select name, id from google.storage.buckets where project = 'stackql-demo'"}},
          {"key": "stackql.query_class", "value": {"stringValue": "select"}},
          {"key": "stackql.duration_ms", "value": {"intValue": "412"}},
          {"key": "stackql.rows_returned", "value": {"intValue": "5"}}
        ],
        "traceId": "16c62eeb8bb8c6e30917626336d61829",
        "spanId": "ff6b2aa55252ac0e"
      }],
      "schemaUrl": "https://opentelemetry.io/schemas/1.44.0"
    }],
    "schemaUrl": "https://opentelemetry.io/schemas/1.44.0"
  }]
}
```

The attribute names follow the OpenTelemetry [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) and [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) semantic conventions: `gen_ai.operation.name` is `execute_tool`, `gen_ai.tool.name` is the MCP tool, `gen_ai.tool.call.id` ties records of one call together, and `mcp.method.name`, `mcp.protocol.version` and `mcp.session.id` describe the protocol exchange. StackQL's own facts sit under a `stackql.` prefix: the verbatim `stackql.query`, its `stackql.query_class`, the `stackql.mode` and `stackql.decision` of the gate, `stackql.duration_ms`, `stackql.rows_returned`, and `stackql.provider` for metadata tools. A refused or failed call is an `ERROR` record with `error.type` and `error.message`.

A call that went through the approval gate produces a second record for the elicitation decision, sharing the same `gen_ai.tool.call.id` and trace id, so an accepted or declined write is visible as two correlated events.

The design inlcudes:

- __The attribute set is a versioned interface.__ The GenAI conventions are still Development status upstream, so the set StackQL emits is pinned: the instrumentation scope version (`1.0.0`) names the schema, the `schemaUrl` pins the stable conventions, and a test in the repository asserts the exact attribute list. When it changes, the scope version changes with it.
- __Redaction is identical in both formats.__ The audit answers "what did the agent do", not "what did the agent see". Result rows are never written to either format; only the statement that produced them is. The regression suite checks that a value returned to the client appears in neither log.

### Trace correlation

If the calling agent propagates W3C trace context in `params._meta` (a `traceparent` key, as the MCP conventions describe), the records carry that trace and span. When it does not, the server generates one trace id per MCP session, so every record from one agent session still correlates in your backend.

### Feeding a collector

The line format is exactly what the OpenTelemetry Collector's `otlp_json_file` receiver reads, so the stream reaches an OTLP pipeline with no transform processor. This configuration was verified against otelcol-contrib 0.160.0:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Swap the `debug` exporter for `otlphttp`, or for whichever backend you run, and the agent's activity lands next to the rest of your telemetry with the same resource, scope and trace semantics as everything else. Sinks and dashboards on top of the stream are the next step; this release makes the stream standards-shaped.

### For developers: a general output option

The OpenTelemetry encoding is not tied to the MCP server. It is implemented as a decorator in the generic `pkg/sink` package that wraps any record sink: a payload can describe its own log records, and any other JSON-shaped record is decorated automatically, with its top-level fields as typed attributes and the record as the body. The MCP audit is the first consumer; other StackQL log and result channels can adopt the same option without new plumbing.

## Get it

- Release notes and binaries: [__v0.10.623__](https://github.com/stackql/stackql/releases/tag/v0.10.623)
- Install channels (Claude Desktop bundle, npm, PyPI, Docker, GitHub Action): [__Installing the MCP server__](/docs/installing-stackql#installing-the-mcp-server)
- Protocol revisions, `stateless`, modes and the audit log: [__stackql mcp__](/docs/command-line-usage/mcp)
- The new flag in the global reference: [__`--mcp.log.format`__](/docs/command-line-usage/global-flags)
- Source: [__github.com/stackql/stackql__](https://github.com/stackql/stackql)

⭐ Star us on [__GitHub__](https://github.com/stackql/stackql) and let us know what your agents build.
