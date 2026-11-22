---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "REGISTRY у StackQL: виведення списку провайдерів та їхніх версій у реєстрі провайдерів і завантаження останньої чи певної версії у ваше встановлення."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Виводить список провайдерів, доступних у налаштованому реєстрі провайдерів, або завантажує провайдер у локальне встановлення.  

Див. також:  
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> Починаючи з `v1.0`, можна вимагати, щоб документи провайдерів під час читання мали дійсний підпис ML-DSA-65.  Перевірку за замовчуванням вимкнено; її вмикають ключем `verifyConfig` прапорця [`--registry`](/command-line-usage/global-flags#--registry).

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

## Приклади {#examples}

### Виведення списку доступних провайдерів {#list-available-providers}
Отримує список останніх версій кожного доступного провайдера.  
```sql
REGISTRY LIST;
```
### Виведення списку всіх версій певного провайдера {#list-all-versions-for-a-specific-provider}
Отримує список усіх доступних версій певного провайдера.    
```sql
REGISTRY LIST google;
```

### Встановлення останньої версії провайдера {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Встановлення певної версії провайдера {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v24.10.00267;
```
