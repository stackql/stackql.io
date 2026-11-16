---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REGISTRY：プロバイダーレジストリのプロバイダーとバージョンを一覧表示し、最新バージョンまたは特定のバージョンをインストール環境にプルします。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

設定されたプロバイダーレジストリで利用可能なプロバイダーを一覧表示するか、プロバイダーをローカルのインストール環境にプルします。  

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> `v1.0` 以降では、プロバイダードキュメントの読み込み時に有効な ML-DSA-65 署名を必須にできます。検証はデフォルトで無効で、[`--registry`](/command-line-usage/global-flags#--registry) フラグの `verifyConfig` キーで有効にします。

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### 利用可能なプロバイダーを一覧表示する {#list-available-providers}
利用可能な各プロバイダーの最新バージョンの一覧を取得します。  
```sql
REGISTRY LIST;
```
### 特定のプロバイダーのすべてのバージョンを一覧表示する {#list-all-versions-for-a-specific-provider}
特定のプロバイダーで利用可能なすべてのバージョンの一覧を取得します。    
```sql
REGISTRY LIST google;
```

### プロバイダーの最新バージョンをインストールする {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### プロバイダーの特定のバージョンをインストールする {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
