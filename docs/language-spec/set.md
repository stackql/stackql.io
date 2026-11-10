---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - session variables
  - set
  - provider sessions
  - secrets
description: "SET in StackQL: assign typed session variables, and configure provider aliases, credential fields and secrets for the current session using dollar-prefixed targets."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Assigns a session variable, or configures runtime settings for the current session.  Available in StackQL `v1.0` and later.

See also:  
[[` UNSET `]](/language-spec/unset) [[ Session Variables ]](/getting-started/session-variables) [[ Provider Sessions ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Syntax {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
A plain identifier (`region`), a double-quoted name (`"providers.saved"`) or a double-quoted, dollar-prefixed configuration path (`"$.auth.aws.password"`).

__*literal*__  
A string, number, boolean or `null`.  Strings are single quoted.  Arrays and objects are not scalar values; hold a JSON document as a string.

__*DEFAULT*__  
Resets the target, equivalent to [`UNSET`](/language-spec/unset).  An explicit `null` is a stored value, not a reset.

__*reference*__  
A double-quoted `"$name"` reference to an existing variable, for example `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Optional, and the only scope StackQL has.  `SET SESSION name = ...` and `SET name = ...` are the same statement: every assignment belongs to the current session and to nothing else.  The keyword exists so that scripts read the same way as they do on databases where `SET SESSION` and `SET GLOBAL` differ.  `SET GLOBAL` parses but is rejected with `configuration scope "global" is not supported; use SESSION`, because there is no state shared across sessions.

Assignments in one `SET` list apply in order and are not a transaction: if the second assignment in a list fails, the first stays in effect.

## Targets {#targets}

| Target | Effect |
|--|--|
| `name` or `"name"` | Stores a scalar session variable, dereferenced as `$name` in later statements.  Dots alone do not select configuration: `"providers.saved"` is a variable named `providers.saved`.  The older `"variables.name"` spelling is an alias for `name`. |
| `"$.auth.<provider>.<field>"` | Sets one field of the provider's authentication context for this session.  Fields are the keys of the [`--auth`](/command-line-usage/global-flags) object for that provider, for example `credentialsenvvar`, `password` or `token_url`. |
| `"$.providers.<alias>"` | Binds a provider alias with its own credentials, tenancy label, parameter defaults and tuning.  The value is a JSON document in a string, see [Provider Sessions](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Stores a secret in the session's secret store.  The value is a literal string, typically a JSON `auth` document, referenced from a provider alias as `auth_secret`. |
| `"$.secret_store"` | Selects a persistent OS secret store, `{"backend":"os","namespace":"..."}`.  Requires the operator flag `--allow-persistent-secrets`. |

Every form affects the current session only.  Independent connections to a [`srv`](/command-line-usage/srv) server do not see each other's assignments, and each MCP request is its own session, so configure and query in one statement batch when using the MCP server.

* * *

## Examples {#examples}

### Set session variables {#set-session-variables}

Each statement stores one scalar under the given name, and the value keeps its type: `region` is a string, `minimum_count` an integer and `enabled` a boolean.  `null` is stored as a value, so `$optional_value` later resolves to SQL `NULL` instead of failing as undefined.  The last statement assigns two variables in one list.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Make the session scope explicit {#make-the-session-scope-explicit}

The first two statements are identical: `SESSION` names the only scope there is, and both assignments last until the variable is unset or the session ends.  Use the keyword when a script should read the same way as it does on a database that distinguishes session from global settings.  The commented-out statement is shown for contrast: it assigns nothing and fails with `configuration scope "global" is not supported; use SESSION`, because no state is shared between sessions.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Use a variable in a query {#use-a-variable-in-a-query}

Dereference a variable with `$name`, without quotes, in any expression position.  Values keep their type: strings are escaped as SQL literals, and numbers, booleans and `null` are passed through.  Variables in a `WHERE` clause are resolved before the request is planned, so they are pushed to the provider like literals.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Reset a variable {#reset-a-variable}

`DEFAULT` removes the variable, exactly as [`UNSET`](/language-spec/unset) does, so a later `$region` fails with `variable "$region" is not defined` until it is set again; the commented-out query shows that failure.  This is different from assigning `null`, which leaves the variable in place with a `NULL` value.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Copy a value from another variable {#copy-a-value-from-another-variable}

The right-hand side can be a double-quoted reference to an existing variable, which copies the value it holds at that moment.  Here a provider binding is first stored as an ordinary variable named `providers.saved` (the dots do not make it configuration), then activated by copying it to the `$.providers.production` path.  Removing the saved copy afterwards does not affect the active binding.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Override a credential field for this session {#override-a-credential-field-for-this-session}

A provider's authentication context is the object supplied for it through [`--auth`](/command-line-usage/global-flags) at startup.  A path under `$.auth` replaces one field of that object for the rest of the session and leaves the other fields as they were.  Here the GitHub provider is pointed at a different environment variable, holding a read-only token; the variable is read from the StackQL process environment, and later queries against `github.*` use it until the field is unset or the session ends.

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### Bind a provider alias {#bind-a-provider-alias}

A provider alias is a second, independently configured instance of a provider, used in place of the provider name in queries.  The value is a JSON document in a string: `provider` names the underlying provider, `tenancy` labels the account, `auth` is a credential block in the same shape as `--auth`, and `params` supplies default values for request parameters such as `region`.  Queries against `production.s3.buckets` then run with those credentials, while `aws.s3.buckets` keeps the startup configuration.  The full list of keys is in [Provider Sessions](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
