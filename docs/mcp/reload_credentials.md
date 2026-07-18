---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: MCP tool re-sourcing credentials from the --env.file dotenv file and reporting per-provider resolution status
image: "/img/stackql-featured-image.png"
---

Re-sources the dotenv-style file nominated by the [`--env.file`](/docs/command-line-usage/global-flags) flag into the server's process environment, then reports credential resolution status for each provider.  This solves the fixed-at-spawn environment problem: credentials written or rotated on disk mid-session reach the running server without a restart.

The env file overlays the process environment rather than replacing it: credential resolution always reads the live process environment at request time, so variables already present at spawn (from the shell or the client's `env` block) keep working with or without `--env.file`.  On reload, only keys present in the file with non-empty values are written and nothing is ever unset, so a failed or partial reload preserves previously working credentials.

Secret values are never returned, logged, or audited - the tool reports variable names and statuses only.  Without `--env.file` configured the tool degrades to a pure status probe.  When a query fails on credential resolution, the MCP error carries a hint directing the agent to call `reload_credentials` and retry, so agentic clients self-heal.

:::note

Available in StackQL releases after `v0.10.542`.

:::

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ Credential (re)sourcing ]](/docs/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/docs/mcp/server_info) [[ Claude Desktop ]](/docs/getting-started/claude-desktop)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|No|Scopes the status report to one provider.  When omitted, every provider is reported.|

* * *

## Output

| Renderer | Shape |
|--|--|
| Table | Env-file sourcing result plus one row per provider with credential resolution status |

<br/>

The structured payload contains:

| Field | Description |
|--|--|
|`env_file`|The configured `--env.file` path (empty when none is configured).|
|`env_file_sourced`|`true` when the file was found and sourced on this call.|
|`sourced_vars`|Names of the environment variables set from the file - names only, never values.|
|`providers`|One row per provider (see below).|

<br/>

Each `providers` row has:

| Column | Description |
|--|--|
|`provider`|Provider name.|
|`auth_type`|Authentication type configured for the provider.|
|`sourced_from`|Where the credential resolved from, e.g. `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline`, or `none`.|
|`status`|`ok`, `unresolved`, or `not_checked`.|
|`detail`|Error text when `unresolved`.|

<br/>

Auth types that authenticate outside the credential-bytes path (interactive gcloud, azure CLI, oauth2 client credentials, assume-role chains, OCI signing, null) report `not_checked`.

* * *

## Gating

Allowed in every server mode, including `read_only` - the tool mutates nothing beyond the server's own process environment.

Not supported in `reverse_proxy` backend mode: queries execute in the remote stackql server process, so the tool returns an error directing the user to reload credentials on the backing server.

* * *

## Example

Try this prompt with any MCP client with the StackQL MCP server registered (started with `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
