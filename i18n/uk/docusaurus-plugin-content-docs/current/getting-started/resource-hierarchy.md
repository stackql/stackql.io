---
title: Ієрархія ресурсів
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Ієрархія ресурсів StackQL: як провайдери, сервіси, ресурси й методи відображають хмарні API та API SaaS на таблиці SQL, до яких можна виконувати запити."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Див. також:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Об'єкти в StackQL організовано в таку ієрархію; нижче показано приклад із провайдером Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Ієрархія ресурсів StackQL"/>

### Використання об'єктної нотації StackQL {#using-the-stackql-object-notation}

Ця ієрархія виражається в об'єктній нотації `<provider>.<service>.<resource>` у контексті певного провайдера; ось приклад використання цієї нотації в інструкції [`SELECT`](/language-spec/select):  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Якщо ім'я ресурсу або сервісу містить крапку (`.`), візьміть ім'я ресурсу в символи зворотного апострофа (__`__), наприклад:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Перегляд сервісів провайдера {#showing-services-in-a-provider}

Сервіси провайдера можна перелічити командою [`SHOW SERVICES`](/language-spec/show), як показано тут:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Перегляд ресурсів сервісу {#showing-resources-within-a-service}

Так само ресурси сервісу можна перелічити командою [`SHOW RESOURCES`](/language-spec/show), як показано тут:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Перегляд полів ресурсу {#showing-fields-within-a-resource}

Поля ресурсу можна вивести за допомогою інструкції [`DESCRIBE`](/language-spec/describe), як показано тут:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Перегляд методів ресурсу, які можна виконати {#showing-executable-methods-for-a-resource}

Доступні методи ресурсу можна вивести командою [`SHOW METHODS`](/language-spec/show), як показано тут:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
