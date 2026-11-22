---
title: Начало работы со StackQL
description: Установите StackQL, выполните первый запрос к актуальным общедоступным данным GitHub без учетных данных, затем пройдите аутентификацию у поставщика облачных услуг и изучите схему с помощью SHOW и DESCRIBE.
keywords: [stackql, начало работы, руководство, установка, первый запрос, github, aws]
proficiencyLevel: Beginner
faq:
  - question: Как быстрее всего попробовать StackQL без учетных данных для облака?
    answer: Выполните запрос к общедоступным данным GitHub. Загрузите провайдер github и выполните выборку из github.repos.repos с предикатом org - данные общедоступных организаций можно читать без аутентификации в пределах анонимных лимитов GitHub, поэтому весь путь от установки до актуальных строк из API занимает меньше пяти минут.
  - question: Нужно ли определять схему перед выполнением запросов?
    answer: Нет. Схемы поставляются вместе с определениями провайдеров. SHOW PROVIDERS, SHOW SERVICES, SHOW RESOURCES, SHOW METHODS и DESCRIBE показывают весь интерфейс провайдера во время выполнения, включая параметры WHERE, обязательные для каждого метода.
  - question: Как запускать StackQL из Python или инструмента BI?
    answer: Используйте pystackql (pip install pystackql) для интеграции с Python и pandas или запустите stackql srv и подключите любой клиент, совместимый с PostgreSQL, или инструмент BI по сетевому протоколу Postgres.
---

# Начало работы со StackQL

Это руководство проводит вас от установки до запросов к актуальным данным в три этапа: запрос без аутентификации к общедоступным данным GitHub (учетные данные не нужны), обнаружение схемы с помощью метакоманд и запрос с аутентификацией к облаку AWS.

## 1. Установка {#1-install}

macOS (Homebrew):

```bash
brew install stackql
```

Linux:

```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
```

Установка в Windows, пакеты и образы Docker описаны на [странице установки](/installing-stackql). Проверьте установку командой `stackql --version`, затем запустите интерактивную оболочку:

```bash
stackql shell
```

## 2. Первый запрос - учетные данные не нужны {#2-first-query---no-credentials-required}

Загрузите провайдер GitHub и выполните запрос к общедоступной организации:

```sql
REGISTRY PULL github;

SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

Возвращаемые строки - включая сам репозиторий движка `stackql` - это актуальные ответы API GitHub, преобразованные в реляционную форму во время выполнения запроса. Ничего не синхронизировалось, не кэшировалось и не импортировалось. (Доступ к GitHub без аутентификации ограничен 60 запросами к API в час; [персональный токен доступа](/ai/how-tos/authenticate-stackql-to-github) повышает этот лимит.)

## 3. Обнаружение схемы {#3-discover-the-schema}

Интерфейс провайдера описывает сам себя - так вы (или ИИ-агент) узнаете, к чему можно выполнять запросы, не читая документацию провайдера:

```sql
SHOW PROVIDERS;                        -- installed providers
SHOW SERVICES IN github;               -- services in a provider
SHOW RESOURCES IN github.repos;        -- tables in a service
SHOW METHODS IN github.repos.repos;    -- operations + required parameters
DESCRIBE github.repos.repos;           -- columns
```

Главное, что стоит запомнить, - `SHOW METHODS`: его столбец `RequiredParams` показывает, что именно нужно каждой операции в предложении `WHERE`, а также какой глагол SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`, `EXEC`) ее вызывает.

## 4. Запрос к поставщику облачных услуг {#4-query-a-cloud-provider}

Экспортируйте учетные данные (показан пример для AWS; для [Azure](/ai/how-tos/authenticate-stackql-to-azure) и [Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) порядок такой же), загрузите провайдер и выполните запрос:

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

```sql
REGISTRY PULL aws;

SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

Агрегатные запросы автоматически распределяются на несколько вызовов - этот запрос выполняется как параллельные вызовы API для каждого региона:

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

## 5. Что дальше {#5-where-to-go-next}

- **Изменение данных**: `SHOW INSERT INTO <resource>` формирует шаблоны создания; `INSERT`/`UPDATE`/`DELETE` работают с теми же таблицами, к которым вы выполняете запросы.
- **Режим сервера**: `stackql srv` предоставляет сетевой протокол PostgreSQL для psql, DBeaver, инструментов BI и pandas.
- **ИИ-агенты**: `stackql mcp` запускает встроенный MCP-сервер - см. [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents).
- **Python**: `pip install pystackql` для использования в блокнотах и конвейерах.

## Связанные концепции {#related-concepts}

- [Что такое StackQL?](/ai/canonical-definitions/what-is-stackql) - каноническое определение
- [Как выполнять запросы к репозиториям GitHub с помощью StackQL](/ai/how-tos/query-github-repositories-with-stackql) - первый запрос, подробнее
- [Как выполнять запросы к экземплярам AWS EC2 с помощью StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - облачный запрос, подробнее
- [Распространенные ошибки StackQL](/ai/troubleshooting/common-stackql-errors) - если что-то не работает
- [StackQL: часто задаваемые вопросы](/ai/faqs/stackql-faq) - распространенные вопросы
