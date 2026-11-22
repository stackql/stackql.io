---
title: Иерархия ресурсов
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Иерархия ресурсов StackQL: как провайдеры, сервисы, ресурсы и методы отображают API облачных платформ и SaaS-сервисов на SQL-таблицы, доступные для запросов."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

См. также:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Объекты в StackQL представлены в виде следующей иерархии; ниже показан пример с провайдером Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Иерархия ресурсов StackQL"/>

### Использование объектной нотации StackQL {#using-the-stackql-object-notation}

Эта иерархия выражается в объектной нотации `<provider>.<service>.<resource>` в контексте конкретного провайдера. Пример использования этой нотации в инструкции [`SELECT`](/language-spec/select) показан здесь:  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Если имя ресурса или сервиса содержит точку (`.`), заключите имя ресурса в символы обратной кавычки (__`__), например:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Просмотр сервисов провайдера {#showing-services-in-a-provider}

Список сервисов провайдера можно вывести с помощью команды [`SHOW SERVICES`](/language-spec/show), как показано здесь:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Просмотр ресурсов сервиса {#showing-resources-within-a-service}

Аналогично список ресурсов сервиса можно вывести с помощью [`SHOW RESOURCES`](/language-spec/show), как показано здесь:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Просмотр полей ресурса {#showing-fields-within-a-resource}

Поля ресурса можно отобразить с помощью инструкции [`DESCRIBE`](/language-spec/describe), как показано здесь:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Просмотр исполняемых методов ресурса {#showing-executable-methods-for-a-resource}

Доступные для ресурса методы можно отобразить с помощью команды [`SHOW METHODS`](/language-spec/show), как показано здесь:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
