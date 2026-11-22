---
title: Как настроить аутентификацию StackQL в Google Cloud
description: StackQL выполняет аутентификацию в Google Cloud с помощью ключа сервисного аккаунта, который передается через переменную среды GOOGLE_CREDENTIALS, содержащую JSON-содержимое ключа.
keywords: [stackql, аутентификация google cloud, gcp, сервисный аккаунт, учетные данные google]
proficiencyLevel: Beginner
faq:
  - question: GOOGLE_CREDENTIALS принимает путь к файлу или содержимое ключа?
    answer: Содержимое ключа. Документированный способ - export GOOGLE_CREDENTIALS=$(cat /path/to/key.json), который помещает в переменную сам JSON-документ. Кроме того, флаг --auth может ссылаться на ключ по пути к файлу с помощью параметра credentialsfilepath.
  - question: Какая роль IAM нужна сервисному аккаунту?
    answer: Роль, которая следует из ваших запросов. Роль roles/viewer в области проекта охватывает запросы инвентаризации и аудита только для чтения; для изменения данных, например INSERT и DELETE, нужны соответствующие роли editor или роли администратора конкретного сервиса.
  - question: Почему в запросах к Google нужно указывать проект (а часто и зону) в предложении WHERE?
    answer: API Google Cloud ограничены областью проекта, а зональные сервисы - зоной. Это обязательные параметры вызова API. Для google.compute.instances методу aggregated_list нужен только project, а методу list нужны project и zone - выполните SHOW METHODS IN google.compute.instances, чтобы увидеть оба метода.
---

# Как настроить аутентификацию StackQL в Google Cloud

StackQL выполняет аутентификацию в Google Cloud с помощью ключа сервисного аккаунта, который передается в виде JSON-содержимого в переменной среды `GOOGLE_CREDENTIALS`.

## Шаги {#steps}

1. Создайте сервисный аккаунт и назначьте ему роль в целевом проекте (для запросов с доступом только для чтения достаточно `roles/viewer`), затем создайте и скачайте JSON-ключ:

```bash
gcloud iam service-accounts keys create stackql-key.json \
  --iam-account=stackql-sa@my-project.iam.gserviceaccount.com
```

2. Экспортируйте содержимое ключа:

```bash
export GOOGLE_CREDENTIALS=$(cat ./stackql-key.json)
```

Другой способ - передать путь к ключу с помощью флага `--auth`, что удобно для развертываний сервера и MCP:

```bash
stackql shell \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/stackql-key.json"}}'
```

3. Загрузите провайдер Google (только при первом использовании):

```sql
REGISTRY PULL google;
```

4. Выполните проверочный запрос:

```sql
SELECT name, status, machineType
FROM google.compute.instances
WHERE project = 'my-project';
```

Здесь используется метод доступа `aggregated_list`, которому нужен только параметр `project` и который возвращает экземпляры во всех зонах. Зональному методу `list` нужны и `project`, и `zone`; инструкция `SHOW METHODS IN google.compute.instances` показывает обязательные параметры каждого метода.

## Где действуют учетные данные {#where-the-credentials-apply}

Та же переменная (или объект `--auth`) работает в командах `stackql shell`, `stackql exec`, `stackql srv` и `stackql mcp`. Для Claude Desktop и подобных MCP-клиентов задайте переменную `GOOGLE_CREDENTIALS` в блоке `env` конфигурации сервера. В CI загружайте ключ из хранилища секретов платформы - никогда не добавляйте его в репозиторий.

## Связанные концепции {#related-concepts}

- [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws) - аналогичное руководство для AWS
- [Как настроить аутентификацию StackQL в Azure](/ai/how-tos/authenticate-stackql-to-azure) - аналогичное руководство для Azure
- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - учетные данные в развертываниях MCP
- [Что такое SQL для API?](/ai/canonical-definitions/what-is-sql-for-apis) - почему обязательные параметры появляются в предложениях WHERE
- [Распространенные ошибки StackQL](/ai/troubleshooting/common-stackql-errors) - включая сбои аутентификации
