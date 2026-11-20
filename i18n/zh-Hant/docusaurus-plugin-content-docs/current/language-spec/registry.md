---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REGISTRY：列出 Provider 登錄庫中的 Provider 與版本，並將最新版或特定版本提取到您的安裝中。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

列出已設定之 Provider 登錄庫中可用的 Provider，或將 Provider 提取到本機安裝中。

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> 自 `v1.0` 起，可以要求 Provider 文件在讀取時帶有有效的 ML-DSA-65 簽章。驗證預設為關閉，可透過 [`--registry`](/command-line-usage/global-flags#--registry) 旗標的 `verifyConfig` 鍵啟用。

* * * 

## 語法 {#syntax}

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

## 範例 {#examples}

### 列出可用的 Provider {#list-available-providers}
擷取每個可用 Provider 最新版本的清單。
```sql
REGISTRY LIST;
```
### 列出特定 Provider 的所有版本 {#list-all-versions-for-a-specific-provider}
擷取特定 Provider 所有可用版本的清單。
```sql
REGISTRY LIST google;
```

### 安裝 Provider 的最新版本 {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### 安裝 Provider 的特定版本 {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
