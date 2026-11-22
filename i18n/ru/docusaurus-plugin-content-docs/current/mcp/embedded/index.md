---
title: Встроенные MCP-серверы
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - встроенный
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Встройте MCP-сервер StackQL непосредственно в скомпилированные приложения на Go, Rust, Swift, Kotlin, .NET и Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

MCP-сервер StackQL можно встроить непосредственно в скомпилированные приложения - без `npx`, без отдельной установки, без зависимости от среды выполнения. Подписанный двоичный файл скачивается и проверяется при первом запуске (режим sidecar) либо включается в двоичный файл вашего приложения через вендоринг, чтобы получился один автономный исполняемый файл.

Все шесть библиотек следуют одному контракту: режим `read_only` по умолчанию, запуск, не зависящий от текущего рабочего каталога (cwd), общий кэш двоичных файлов в каталоге `~/.stackql/mcp-server-bin/` и одни и те же четыре целевые платформы (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Для Windows ARM64 также доступны для скачивания нативные [версии CLI и MCPB](/installing-stackql). Механизмы определения платформы в библиотеках для Go, Rust и .NET, выпущенные в v0.12.778, по-прежнему выбирают в Windows только `windows-x64`; новый пакет не добавляет в эти библиотеки автоматический выбор ARM64.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Встраивание через go:embed или в режиме sidecar; официальный MCP SDK для Go. Демонстрационное приложение: sandboxctl, инфраструктурный консьерж по запросу.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Режим sidecar или вендоринг через include_bytes!; клиент rmcp. Демонстрационное приложение: auditron, терминальный помощник для проверки соответствия требованиям.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Включение нотариально заверенного двоичного файла в подписанное приложение macOS; официальный MCP SDK для Swift. Демонстрационное приложение: CloudLens, облачный страж в строке меню.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Библиотека + плагин Gradle; официальный MCP SDK для Kotlin. Демонстрационное приложение: costgate, контроль затрат для CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Режим sidecar или пакет, включенный через вендоринг; официальный MCP SDK для C#. Демонстрационное приложение: driftwatch, фоновый процесс для проверки дрейфа по расписанию.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Работа под супервизором OTP на целевой платформе Erlang/BEAM; скачивает и проверяет подписанный двоичный файл. Демонстрационное приложение: pipewatch (запланировано).'
  }
];

<DocCardList
  items={EMBEDDED_LIBRARIES.map(lib => ({
    type: 'link',
    label: lib.name,
    href: lib.href,
    description: lib.description,
    customProps: { iconComponent: <FaRobot size={18} className="homeCardIcon" /> }
  }))}
/>

## Общий контракт {#common-contract}

- Режим `read_only` по умолчанию с явным включением режимов `safe`, `delete_safe` или `full_access`.
- Двоичный файл нотариально заверен Apple для macOS и подписан Authenticode для Windows.
- Закрепленные хэши sha256 публикуются для каждого выпуска.
- mcp-name `io.github.stackql/stackql-mcp` в реестре Official MCP Registry.

См. справочник [MCP-инструменты](/mcp) и [способы установки](/installing-stackql).
