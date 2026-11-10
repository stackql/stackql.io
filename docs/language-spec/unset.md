---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - session variables
  - unset
  - provider sessions
  - secrets
description: "UNSET in StackQL: remove session variables, or clear provider aliases, credential fields and secrets configured with SET for the current session."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Removes a session variable, or clears runtime configuration set with [`SET`](/language-spec/set) for the current session.  Available in StackQL `v1.0` and later.

See also:  
[[` SET `]](/language-spec/set) [[ Session Variables ]](/getting-started/session-variables) [[ Provider Sessions ]](/getting-started/provider-sessions)

* * * 

## Syntax {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Any target accepted by `SET`: a plain or double-quoted variable name, or a double-quoted, dollar-prefixed configuration path.

__*SESSION*__  
Optional, and the only scope there is: `UNSET SESSION name` and `UNSET name` are the same statement, because every variable and configuration setting belongs to the current session.  `UNSET GLOBAL` is rejected with `configuration scope "global" is not supported; use SESSION`.

`UNSET name` is equivalent to `SET name = DEFAULT`.  Targets in one list are removed in order, and removing a name that is not set is not an error.  Dereferencing a variable after it has been unset fails before any provider request is made.

| Target | Effect |
|--|--|
| `name` | Removes the session variable. |
| `"$.auth.<provider>.<field>"` | Deletes that field from the provider's session authentication context.  The value in force before the override is not restored. |
| `"$.providers.<alias>"` | Removes the provider alias binding. |
| `"$.secrets.<name>"` | Deletes the secret from the session's store.  For a persistent OS store this deletes the stored value. |
| `"$.secret_store"` | Discards the current store binding and starts with empty session memory.  Values already persisted in an OS store are not deleted. |

* * *

## Examples {#examples}

### Remove a variable {#remove-a-variable}

The variable is removed from the session, so the commented-out query fails with `variable "$region" is not defined` rather than running with a stale or empty value.  Set the name again to reuse it.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Remove several targets {#remove-several-targets}

One statement can remove variables and configuration paths together, and the targets are processed in order.  Here a plain variable, a variable with a dotted name and a credential override are removed in one list.

```sql
UNSET region, "providers.saved", "$.auth.github.credentialsenvvar";
```

### Make the session scope explicit {#make-the-session-scope-explicit}

These two statements do the same thing: `SESSION` names the only scope there is.  The explicit form suits scripts that also run on databases which distinguish session from global state.  The commented-out statement fails because no shared state exists.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Clear a credential override {#clear-a-credential-override}

The field set earlier with `SET "$.auth.github.credentialsenvvar"` is deleted from the GitHub provider's session authentication context, and the other fields of that context are untouched.  The value in force before the override is not restored, so set the field again if the provider still needs it.

```sql
UNSET "$.auth.github.credentialsenvvar";
```

### Remove a provider alias and its secret {#remove-a-provider-alias-and-its-secret}

The alias `production` was bound with `SET "$.providers.production"` and refers, through `auth_secret`, to a secret stored at `$.secrets.production`.  Removing both in one statement drops the binding, so `production.s3.buckets` is no longer a valid relation, and deletes the secret from the session store.  `SESSION` is optional here, as everywhere.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
