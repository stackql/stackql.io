---
title: Как использовать StackQL с Claude
description: Зарегистрируйте StackQL как MCP-сервер в Claude Desktop или Claude Code, чтобы Claude на естественном языке выполнял запросы к облачной инфраструктуре AWS, Azure, Google Cloud, GitHub и других провайдеров и управлял ею.
keywords: [stackql, claude, claude desktop, claude code, mcp, anthropic, облачная инфраструктура]
proficiencyLevel: Beginner
faq:
  - question: Нужно ли Claude знать синтаксис StackQL?
    answer: Специального обучения не требуется. Claude узнает схему провайдера во время выполнения с помощью MCP-инструментов обнаружения (list_providers, list_methods, describe_method) и пишет стандартный SQL, которым он уже свободно владеет. Вы общаетесь с Claude на естественном языке, а он переводит сказанное в запросы StackQL.
  - question: Запросит ли Claude подтверждение перед изменением моей инфраструктуры?
    answer: Да, при конфигурации по умолчанию. MCP-сервер StackQL по умолчанию работает в режиме safe, а Claude Desktop объявляет возможность elicitation протокола MCP, поэтому каждая инструкция INSERT, UPDATE или DELETE и каждая операция жизненного цикла вызывает запрос подтверждения, в котором SQL показан до выполнения. Задайте для mode значение read_only, чтобы полностью запретить изменение данных.
  - question: Где указываются мои учетные данные для облака?
    answer: В блоке env записи stackql в файле конфигурации MCP или в среде, которую наследует процесс сервера. В выпусках StackQL после v0.10.542 вместо этого можно передать файл учетных данных в формате dotenv с помощью флага --env.file; инструмент reload_credentials может повторно загрузить его без перезапуска Claude Desktop. Учетные данные остаются на вашей машине вместе с двоичным файлом StackQL и не отправляются модели.
---

# Как использовать StackQL с Claude

Claude подключается к StackQL через Model Context Protocol: зарегистрируйте `stackql` как MCP-сервер в Claude Desktop (или Claude Code), и Claude сможет проводить инвентаризацию и аудит облачных ресурсов и - с вашего подтверждения - изменять их у всех провайдеров, которые поддерживает StackQL, по командам на естественном языке.

## Шаги {#steps}

1. **Установите StackQL** и убедитесь, что он доступен через PATH:

```bash
stackql --version
```

2. **Отредактируйте файл конфигурации Claude Desktop**:

- Windows: `%APPDATA%\Claude\claude_desktop_config.json`
- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Linux: `~/.config/Claude/claude_desktop_config.json`

3. **Добавьте запись сервера**, указав учетные данные для используемых провайдеров:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--mcp.config",
        "{\"server\": {\"transport\": \"stdio\", \"mode\": \"safe\"}}"
      ],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "STACKQL_GITHUB_USERNAME": "your-github-username",
        "STACKQL_GITHUB_PASSWORD": "your-github-personal-access-token"
      }
    }
  }
}
```

Указывайте только нужные провайдеры (`AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET` для Azure, `GOOGLE_CREDENTIALS` для Google Cloud, `DATABRICKS_CLIENT_ID`/`DATABRICKS_CLIENT_SECRET` для Databricks). Никогда не добавляйте этот файл в систему управления версиями.

4. **Перезапустите Claude Desktop.** Сервер запускается автоматически, когда начинается беседа.

5. **Задавайте вопросы на естественном языке.** Примеры, которые напрямую соответствуют операциям StackQL:

- "Перечисли все экземпляры EC2 в us-east-1 с публичными IP-адресами"
- "В каких репозиториях нашей организации GitHub отключена защита веток?"
- "Создай бакет S3 с именем analytics-data в us-west-2"

Для операций чтения Claude узнает схему и сразу выполняет инструкции `SELECT`. При создании бакета режим `safe` вызывает запрос подтверждения, в котором показана точная инструкция `INSERT`, прежде чем что-либо будет выполнено.

## Управление тем, что может делать Claude {#controlling-what-claude-can-do}

Поле `mode` в конфигурации задает контракт: `read_only` для ассистентов, которые только проводят инвентаризацию, `safe` (по умолчанию) для изменений с подтверждением, `delete_safe`, чтобы разрешить создание и обновление, но требовать подтверждения для удаления и операций жизненного цикла `EXEC`, и `full_access` только для доверенной автоматизации. Каждый вызов, который выполняет Claude, записывается в журнал аудита сервера в формате JSONL независимо от режима.

## Связанные концепции {#related-concepts}

- [Как использовать StackQL с ИИ-агентами](/ai/how-tos/use-stackql-with-ai-agents) - общая настройка MCP, включая транспорт HTTP
- [Архитектура MCP-сервера StackQL](/ai/architecture/stackql-mcp-architecture) - подробно о режимах, elicitation и аудите
- [Как настроить аутентификацию StackQL в AWS](/ai/how-tos/authenticate-stackql-to-aws) - справочник по учетным данным для каждого провайдера
- [Что такое агентная инфраструктура?](/ai/canonical-definitions/what-is-agentic-infrastructure) - более общий подход
- [StackQL и собственные MCP-серверы: сравнение](/ai/comparisons/stackql-vs-custom-mcp-servers) - почему один универсальный сервер лучше множества специализированных
