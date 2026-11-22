---
title: Как выполнять запросы к экземплярам AWS EC2 с помощью StackQL
description: Выполняйте запросы к экземплярам EC2 как к SQL-таблице aws.ec2.instances - фильтруйте по региону, выбирайте атрибуты (тип экземпляра, IP-адреса) и агрегируйте по регионам с GROUP BY.
keywords: [stackql, aws, ec2, запросы к экземплярам, sql, инвентаризация облачных ресурсов, несколько регионов]
proficiencyLevel: Beginner
faq:
  - question: Почему при запросах к экземплярам EC2 обязательно указывать регион?
    answer: API DescribeInstances в EC2 работает в пределах региона, поэтому region - обязательный параметр базового вызова. StackQL представляет обязательные параметры API как обязательные предикаты в предложении WHERE. Если указать region IN (...) с несколькими регионами, запрос распределяется на параллельные вызовы API для каждого региона, результаты которых объединяются в один результирующий набор.
  - question: Какие столбцы доступны в таблице aws.ec2.instances?
    answer: Среди столбцов - instance_id, instance_type, image_id, launch_time, private_ip_address, public_ip_address, private_dns_name, public_dns_name, subnet_id, vpc_id, key_name, а также структурированные столбцы, такие как state, placement, security_groups, network_interfaces и tags. Чтобы получить полный список, выполните DESCRIBE aws.ec2.instances.
  - question: Можно ли останавливать, запускать или завершать экземпляры с помощью StackQL?
    answer: Да. Завершение соответствует DELETE, а действия жизненного цикла (start_instances, stop_instances, reboot_instances, monitor_instances) - методы EXEC того же ресурса. Инструкция SHOW METHODS IN aws.ec2.instances выводит каждую операцию, ее глагол SQL и обязательные параметры.
---

# Как выполнять запросы к экземплярам AWS EC2 с помощью StackQL

Экземпляры EC2 представлены в виде таблицы `aws.ec2.instances`. Любое поле, которое возвращает API `DescribeInstances` в EC2, - это столбец, доступный для запросов, а `region` - обязательный предикат `WHERE`, так как API работает в пределах региона.

## Предварительные требования {#prerequisites}

- Учетные данные AWS, экспортированные как переменные среды, - см. [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws)
- Установленный провайдер AWS: `REGISTRY PULL aws;`

## Простой запрос {#basic-query}

Выведите список экземпляров в одном регионе с их типом и адресами:

```sql
SELECT
  instance_id,
  instance_type,
  private_ip_address,
  public_ip_address,
  launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

## Изучение схемы перед запросом {#discover-the-schema-first}

Две команды для получения метаданных делают ресурс самоописываемым - документация AWS не нужна:

```sql
DESCRIBE aws.ec2.instances;
SHOW METHODS IN aws.ec2.instances;
```

Команда `DESCRIBE` выводит доступные столбцы. Команда `SHOW METHODS` выводит операции (метод SELECT `describe_instances`, метод DELETE `terminate_instances` и методы жизненного цикла EXEC, такие как `start_instances`, `stop_instances` и `reboot_instances`) с обязательными параметрами для каждой из них. Эти команды работают без учетных данных.

## Агрегирование по нескольким регионам {#multi-region-aggregation}

Предикат `region IN (...)` параллельно выполняет по одному вызову API на каждый регион и объединяет результаты - к региональному сервису можно обращаться как к глобальной таблице:

```sql
SELECT region, instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region, instance_type;
```

Этот подход лежит в основе инвентаризации в масштабе всей учетной записи и эталонов для выявления дрейфа: результат - актуальный ответ, а не снимок.

## Фильтрация по атрибутам {#filtering-on-attributes}

Стандартные предикаты SQL применимы к любому столбцу. Например, экземпляры, доступные из интернета:

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1'
AND public_ip_address IS NOT NULL;
```

Структурированные столбцы, такие как `state` и `tags`, возвращают JSON, который можно разобрать с помощью JSON-функций настроенного SQL-бэкенда.

## Связанные концепции {#related-concepts}

- [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws) - настройка учетных данных
- [Как выполнять запросы к бакетам S3 с помощью StackQL](/ai/how-tos/query-s3-buckets-with-stackql) - подход для S3, который немного отличается
- [Что такое дрейф инфраструктуры?](/ai/canonical-definitions/what-is-infrastructure-drift) - использование этих запросов в качестве эталона
- [Что такое инфраструктура, доступная для запросов?](/ai/canonical-definitions/what-is-queryable-infrastructure) - модель, на которой основана абстракция таблиц
- [Начало работы со StackQL](/ai/tutorials/getting-started-with-stackql) - полная настройка от начала до конца
