---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - access management
  - groups
  - principal directory
description: "GROUPS function in StackQL: return the directory groups of the authenticated caller as a JSON array when access management is enabled."
image: "/img/stackql-featured-image.png"
---
Returns the directory group memberships of the authenticated caller as a JSON array.  Requires a session started with [access management](/command-line-usage/access-management) enabled.  Available in StackQL `v1.0` and later.

See also:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Access Management ]](/command-line-usage/access-management)

* * * 

## Syntax {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Arguments {#arguments}

None.

## Return Value(s) {#return-values}

Returns a JSON array of group IDs from the principal directory snapshot taken for the current statement, for example `["readers"]`.  Membership is refreshed for each statement, so a revocation is reflected by the next statement.  The function fails in a session started without `--access.config`.

* * *

## Examples {#examples}

### Inspect the established identity {#inspect-the-established-identity}

The two functions together show the caller's principal ID and its current directory groups.  Because membership is read afresh for every statement, running this after a directory change confirms that the change is visible to the session.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
