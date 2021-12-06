---
title: USE
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
//import Gist from 'react-gist';

Changes the context to a specified provider or service.  

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*useStatement::=*

<RailroadDiagram 
type="use"
/>

&nbsp;  
&nbsp;  

```sql
USE <multipartIdentifier>;
```

* * *

## Examples

### USE a provider
USE statement to set the current context to be a specific provider.

```sql
-- Use the Google provider
USE google;
```
<!--
<Gist id="9b9985dbf8163ade22b71f2ccf20cb51" 
/>
-->