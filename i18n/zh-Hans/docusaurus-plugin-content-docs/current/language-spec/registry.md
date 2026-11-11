---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REGISTRY：列出 Provider 注册表中的 Provider 及其版本，并将最新版本或指定版本拉取到你的安装中。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

列出已配置的 Provider 注册表中可用的 Provider，或将 Provider 拉取到本地安装中。  

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> 从 `v1.0` 起，可以要求 Provider 文档在读取时带有有效的 ML-DSA-65 签名。验证默认关闭，可通过 [`--registry`](/command-line-usage/global-flags#--registry) 标志的 `verifyConfig` 键启用。

* * * 

## 语法 {#syntax}

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

## 示例 {#examples}

### 列出可用的 Provider {#list-available-providers}
获取每个可用 Provider 的最新版本列表。  
```sql
REGISTRY LIST;
```
### 列出特定 Provider 的所有版本 {#list-all-versions-for-a-specific-provider}
获取特定 Provider 的所有可用版本列表。    
```sql
REGISTRY LIST google;
```

### 安装 Provider 的最新版本 {#install-the-latest-version-of-a-provider}
```
REGISTRY PULL okta;
```

### 安装 Provider 的特定版本 {#install-a-specific-version-of-a-provider}
```
REGISTRY PULL azure v23.01.00104;
```
