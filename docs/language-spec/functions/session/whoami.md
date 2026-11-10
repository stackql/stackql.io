---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - access management
  - whoami
  - principal
description: "WHOAMI function in StackQL: return the principal ID of the authenticated caller when access management is enabled."
image: "/img/stackql-featured-image.png"
---
Returns the principal ID of the authenticated caller.  Requires a session started with [access management](/command-line-usage/access-management) enabled.  Available in StackQL `v1.0` and later.

See also:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Access Management ]](/command-line-usage/access-management)

* * * 

## Syntax {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Arguments {#arguments}

None.

## Return Value(s) {#return-values}

Returns the principal ID string established for the current statement.  The function fails in a session started without `--access.config`.

* * *

## Examples {#examples}

### Inspect the established identity {#inspect-the-established-identity}

Both functions take no arguments and describe the caller that the server authenticated for this statement.  `whoami()` returns the principal ID and `groups()` the caller's directory groups, so running them together is a quick check that a token or certificate maps to the expected principal before anything else is run.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
