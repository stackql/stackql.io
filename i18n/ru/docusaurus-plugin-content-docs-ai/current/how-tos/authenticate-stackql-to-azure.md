---
title: Как настроить аутентификацию StackQL в Azure
description: StackQL выполняет аутентификацию в Azure с помощью субъекта-службы Microsoft Entra ID через переменные среды AZURE_TENANT_ID, AZURE_CLIENT_ID и AZURE_CLIENT_SECRET.
keywords: [stackql, аутентификация azure, субъект-служба, entra id, переменные среды]
proficiencyLevel: Beginner
faq:
  - question: Какая роль Azure нужна StackQL?
    answer: Роль, которая требуется вашим запросам, с настолько узкой областью действия, насколько это практически возможно. Для запросов инвентаризации и аудита достаточно встроенной роли Reader в области подписки; для изменения данных нужна роль Contributor или пользовательская роль для целевых типов ресурсов.
  - question: Почему в запросах к Azure нужно указывать subscriptionId в предложении WHERE?
    answer: API Azure Resource Manager ограничены областью подписки (а часто и группы ресурсов), поэтому эти значения представлены как обязательные параметры базового вызова API, а не как настройки аутентификации. Инструкция SHOW METHODS IN azure.compute.virtual_machines выводит обязательные параметры для каждого метода.
  - question: Работает ли StackQL в Azure Cloud Shell?
    answer: Да. StackQL можно запускать в Azure Cloud Shell, как любой двоичный файл для Linux; передайте учетные данные субъекта-службы через переменные среды, описанные на этой странице.
---

# Как настроить аутентификацию StackQL в Azure

StackQL выполняет аутентификацию в Azure с помощью субъекта-службы Microsoft Entra ID (ранее Azure AD), который задается через три переменные среды: `AZURE_TENANT_ID`, `AZURE_CLIENT_ID` и `AZURE_CLIENT_SECRET`.

## Шаги {#steps}

1. Создайте субъект-службу и назначьте роль в целевой подписке (для запросов с доступом только для чтения достаточно роли `Reader`):

```bash
az ad sp create-for-rbac --name stackql-sp --role Reader \
  --scopes /subscriptions/<subscription-id>
```

2. Экспортируйте возвращенные учетные данные:

```bash
export AZURE_TENANT_ID=your-tenant-id
export AZURE_CLIENT_ID=your-client-id
export AZURE_CLIENT_SECRET=your-client-secret
```

3. Загрузите провайдер Azure (только при первом использовании):

```sql
REGISTRY PULL azure;
```

4. Выполните проверочный запрос:

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

Предикат `subscriptionId` - это обязательный параметр маршрутизации API Azure, а не часть аутентификации. Методам, привязанным к группе ресурсов, дополнительно нужен параметр `resourceGroupName`; выполните инструкцию `SHOW METHODS IN azure.compute.virtual_machines`, чтобы увидеть обязательные параметры каждого метода доступа (для `list_all` нужен только `subscriptionId`; для `list` нужен также `resourceGroupName`; для `get` нужен `vmName`).

## Где действуют переменные {#where-the-variables-apply}

Переменные работают одинаково в командах `stackql shell`, `stackql exec`, `stackql srv` (сервер с сетевым протоколом PostgreSQL) и `stackql mcp`. Для MCP-клиентов, таких как Claude Desktop, поместите их в блок `env` записи сервера в файле `claude_desktop_config.json`. В CI загружайте их из хранилища секретов конвейера.

## Связанные концепции {#related-concepts}

- [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws) - аналогичное руководство для AWS
- [Как настроить аутентификацию StackQL в Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) - аналогичное руководство для GCP
- [Как использовать StackQL с Claude](/ai/how-tos/use-stackql-with-claude) - передача учетных данных Azure в MCP-сеанс
- [Плоскость управления и плоскость данных](/ai/canonical-definitions/control-plane-vs-data-plane) - что охватывают API Resource Manager
- [Распространенные ошибки StackQL](/ai/troubleshooting/common-stackql-errors) - включая сбои аутентификации
