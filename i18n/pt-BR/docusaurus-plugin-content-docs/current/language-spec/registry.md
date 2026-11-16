---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "REGISTRY no StackQL: listar os providers e as versões do registro de providers e baixar a versão mais recente ou uma versão específica para a sua instalação."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Lista os providers disponíveis no registro de providers configurado ou baixa um provider para a instalação local.  

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> A partir da `v1.0`, é possível exigir que os documentos de provider tenham uma assinatura ML-DSA-65 válida quando são lidos.  A verificação fica desabilitada por padrão e é habilitada com a chave `verifyConfig` da opção [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Sintaxe {#syntax}

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## Exemplos {#examples}

### Listar os providers disponíveis {#list-available-providers}
Recupera a lista das versões mais recentes de cada provider disponível.  
```sql
REGISTRY LIST;
```
### Listar todas as versões de um provider específico {#list-all-versions-for-a-specific-provider}
Recupera a lista de todas as versões disponíveis de um provider específico.    
```sql
REGISTRY LIST google;
```

### Instalar a versão mais recente de um provider {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Instalar uma versão específica de um provider {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
