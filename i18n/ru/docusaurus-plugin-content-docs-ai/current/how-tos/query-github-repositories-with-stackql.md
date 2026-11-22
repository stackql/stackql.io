---
title: Как выполнять запросы к репозиториям GitHub с помощью StackQL
description: Выполняйте запросы к репозиториям GitHub как к SQL-таблице github.repos.repos - выводите репозитории организации или пользователя, фильтруйте по языку, видимости и активности и меняйте настройки через SQL.
keywords: [stackql, github, репозитории, sql, github api, devops, управление]
proficiencyLevel: Beginner
faq:
  - question: Нужны ли учетные данные для запросов к общедоступным репозиториям GitHub?
    answer: Нет. К общедоступным организациям и репозиториям можно выполнять запросы без аутентификации с учетом ограничения частоты запросов GitHub для неаутентифицированных клиентов - 60 запросов к API в час. Чтобы получить более высокие лимиты и доступ к закрытым данным, задайте STACKQL_GITHUB_USERNAME и STACKQL_GITHUB_PASSWORD (персональный токен доступа).
  - question: К каким еще ресурсам GitHub StackQL может выполнять запросы?
    answer: Провайдер github охватывает документированный REST API - в частности, сервис github.repos предоставляет ветки, коммиты, выпуски, участников совместной работы, веб-хуки, развертывания, среды, теги, команды и статистику трафика. Инструкция SHOW RESOURCES IN github.repos выводит их все.
  - question: Может ли StackQL изменять настройки репозиториев?
    answer: Да. Ресурс github.repos.repos предоставляет методы UPDATE (требует owner и repo), INSERT (create_in_org) и DELETE. Поля тела запроса задаются через столбцы с префиксом data__, а обязательные параметры каждого метода выводит инструкция SHOW METHODS IN github.repos.repos.
---

# Как выполнять запросы к репозиториям GitHub с помощью StackQL

Репозитории GitHub представлены в виде таблицы `github.repos.repos`. Получение списка по организации или пользователю соответствует операциям REST API GitHub, которые выбираются предложением `WHERE`: `org = '...'` выбирает операцию получения списка репозиториев организации, `username = '...'` - операцию получения списка репозиториев пользователя.

## Предварительные требования {#prerequisites}

- Установленный провайдер GitHub: `REGISTRY PULL github;`
- Необязательные учетные данные для доступа к закрытым данным и более высоких ограничений частоты запросов - см. [Как настроить аутентификацию StackQL в GitHub](/ai/how-tos/authenticate-stackql-to-github)

## Получение списка репозиториев организации {#list-an-organizations-repositories}

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

Для общедоступной организации `stackql` этот запрос возвращает строки, такие как репозиторий движка `stackql` (Go, 861 звезда на момент написания) и `stackql-provider-registry` - актуальные данные API без каких-либо снимков.

## Фильтрация по атрибутам репозитория {#filter-on-repository-attributes}

Стандартные предикаты SQL применимы к любому столбцу:

```sql
SELECT name, visibility, default_branch, archived
FROM github.repos.repos
WHERE org = 'my-org'
AND archived = false
AND visibility = 'public';
```

Среди столбцов, полезных для управления репозиториями, - `private`, `visibility`, `archived`, `fork`, `default_branch`, `delete_branch_on_merge`, `has_wiki`, `topics`, `created_at`, `pushed_at` и `security_and_analysis`. Инструкция `DESCRIBE github.repos.repos` выводит их все.

## Не только список репозиториев {#beyond-the-repository-list}

Сервис `github.repos` предоставляет остальные данные репозиториев в виде соседних таблиц - `github.repos.branches`, `github.repos.commits`, `github.repos.releases`, `github.repos.collaborators`, `github.repos.webhooks`, `github.repos.deployments` и другие. Чтобы найти их, выполните:

```sql
SHOW RESOURCES IN github.repos;
```

## Обновление репозиториев {#updating-repositories}

Настройки репозитория меняются инструкциями `UPDATE`; метод требует `owner` и `repo`, а поля тела запроса используют префикс `data__`:

```sql
UPDATE github.repos.repos
SET data__delete_branch_on_merge = true
WHERE owner = 'my-org' AND repo = 'my-repo';
```

Если применить это ко всему результирующему набору запроса со списком репозиториев, получится управление всем парком репозиториев двумя инструкциями - подход, который естественно сочетается с межпровайдерными запросами, соединяющими данные GitHub с облачными ресурсами.

## Связанные концепции {#related-concepts}

- [Как настроить аутентификацию StackQL в GitHub](/ai/how-tos/authenticate-stackql-to-github) - токены и ограничения частоты запросов
- [Что такое SQL для API?](/ai/canonical-definitions/what-is-sql-for-apis) - сопоставление REST и SQL, которое здесь используется
- [Начало работы со StackQL](/ai/tutorials/getting-started-with-stackql) - GitHub как первый провайдер, не требующий учетных данных
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - агенты, выполняющие эти запросы через MCP
- [StackQL и Steampipe: сравнение](/ai/comparisons/stackql-vs-steampipe) - сравнение с альтернативами только для чтения
