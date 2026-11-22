---
title: Как выполнять запросы к бакетам S3 с помощью StackQL
description: Получайте список бакетов S3 и их регионов из aws.s3.buckets и конфигурацию каждого бакета - шифрование, управление версиями, блокировку публичного доступа - из таблицы Cloud Control awscc.s3.buckets по Identifier.
keywords: [stackql, aws, s3, бакеты, sql, облачная безопасность, публичный доступ]
proficiencyLevel: Beginner
faq:
  - question: Почему в запросах к бакетам S3 используется region = 'us-east-1', если бакеты глобальные?
    answer: Имена бакетов глобально уникальны, но параметр маршрутизации region тем не менее обязателен для метода получения списка; us-east-1 возвращает список бакетов учетной записи. Домашний регион каждого бакета - это столбец bucket_region в этом списке.
  - question: Как получить конфигурацию конкретного бакета?
    answer: Выполните запрос к таблице Cloud Control awscc.s3.buckets, указав имя бакета в качестве Identifier. Строка содержит конфигурацию, например шифрование, управление версиями, блокировку публичного доступа, ведение журналов и теги.
  - question: Может ли StackQL создавать или удалять бакеты S3?
    answer: Да. Ресурс aws.s3.buckets предоставляет методы INSERT (create_bucket) и DELETE (delete_bucket), а таблица Cloud Control awscc.s3.buckets - методы INSERT (create_resource), UPDATE (update_resource) и DELETE (delete_resource). Инструкция SHOW INSERT INTO awscc.s3.buckets формирует шаблон с доступными для записи свойствами.
---

# Как выполнять запросы к бакетам S3 с помощью StackQL

Бакеты S3 представлены двумя связанными таблицами: `aws.s3.buckets` выводит список бакетов в учетной записи, а `awscc.s3.buckets` возвращает полную конфигурацию одного бакета, заданного параметром `Identifier`. Первая построена на S3 API в провайдере `aws`, вторая - на AWS Cloud Control API в провайдере `awscc`.

## Предварительные требования {#prerequisites}

- Экспортированные учетные данные AWS - см. [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws)
- Установленные провайдеры AWS: `REGISTRY PULL aws;` и `REGISTRY PULL awscc;`

## Получение списка бакетов {#list-buckets}

```sql
SELECT name, bucket_region, creation_date
FROM aws.s3.buckets
WHERE region = 'us-east-1';
```

Предикат `region` - обязательный параметр маршрутизации базового вызова API; `us-east-1` возвращает бакеты учетной записи.

## Просмотр одного бакета {#inspect-one-bucket}

Таблица Cloud Control возвращает атрибуты конфигурации бакета с указанным именем:

```sql
SELECT
  bucket_name,
  bucket_encryption,
  versioning_configuration,
  public_access_block_configuration
FROM awscc.s3.buckets
WHERE region = 'us-east-1'
AND Identifier = 'my-bucket-name';
```

Столбцы конфигурации возвращают структурированный JSON; инструкция `DESCRIBE awscc.s3.buckets` выводит все доступные столбцы, включая `logging_configuration`, `lifecycle_configuration`, `object_lock_configuration`, `tags` и `arn`.

## Подход к аудиту: перечисление, затем проверка {#audit-pattern-enumerate-then-inspect}

Аудит конфигурации в масштабе всей учетной записи сочетает обе таблицы: получите список имен бакетов, затем выполните запрос к таблице сведений для каждого бакета. В скриптах (циклы в оболочке или Python через pystackql) перебирайте результат списка и собирайте строки сведений - проверки отдельных бакетов, например "включено ли управление версиями?" или "полностью ли настроена блокировка публичного доступа?", становятся предикатами для строк собранных результатов. Это описанный в документации подход к инвентаризации S3 в руководствах StackQL по AWS.

## Создание бакетов {#creating-buckets}

Доступные для записи свойства можно узнать тем же способом, что и доступные для чтения:

```sql
SHOW INSERT INTO awscc.s3.buckets;
```

Эта инструкция формирует шаблон `INSERT` со всеми доступными для записи полями ресурса в виде документа желаемого состояния, который принимает Cloud Control. Чтобы ограничиться только обязательными полями, используйте подсказку запроса `/*+ REQUIRED */`.

## Связанные концепции {#related-concepts}

- [Как выполнять запросы к экземплярам AWS EC2 с помощью StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - подход для региональных сервисов
- [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws) - настройка учетных данных
- [Что такое SQL для API?](/ai/canonical-definitions/what-is-sql-for-apis) - почему список и сведения - это отдельные методы
- [Что такое инфраструктура, доступная для запросов?](/ai/canonical-definitions/what-is-queryable-infrastructure) - сценарий аудита в контексте
- [Распространенные ошибки StackQL](/ai/troubleshooting/common-stackql-errors) - разбор ошибок из-за пропущенных обязательных параметров
