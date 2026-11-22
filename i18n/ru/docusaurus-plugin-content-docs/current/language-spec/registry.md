---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "REGISTRY в StackQL: вывод списка провайдеров и их версий в реестре провайдеров и загрузка последней или конкретной версии в вашу установку."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Выводит список провайдеров, доступных в настроенном реестре провайдеров, или загружает провайдер в локальную установку.  

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> Начиная с `v1.0`, от документов провайдеров можно потребовать наличия действительной подписи ML-DSA-65 при их чтении. Проверка по умолчанию отключена и включается ключом `verifyConfig` флага [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Синтаксис {#syntax}

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

## Примеры {#examples}

### Вывод списка доступных провайдеров {#list-available-providers}
Получает список последних версий каждого доступного провайдера.  
```sql
REGISTRY LIST;
```
### Вывод списка всех версий конкретного провайдера {#list-all-versions-for-a-specific-provider}
Получает список всех доступных версий конкретного провайдера.    
```sql
REGISTRY LIST google;
```

### Установка последней версии провайдера {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Установка конкретной версии провайдера {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
