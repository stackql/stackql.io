---
title: AUTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Authenticate to a specified provider.  

See also:  
[[ Authenticating to a Provider ]](/docs/getting-started/authenticating)

* * * 

## Syntax

*authStatement::=*

<RailroadDiagram 
type="auth"
/>

&nbsp;  
&nbsp;  

```sql
AUTH { LOGIN | REVOKE } <provider> [ INTERACTIVE ];
```

* * *

## Examples

### Interactive authentication in the Shell
Authenticate to the Google provider interactively in the Shell

```sql
AUTH LOGIN google INTERACTIVE;
```

### Logging out from a provider
Logout from the Google provider in the shell

```sql
AUTH REVOKE google;
```