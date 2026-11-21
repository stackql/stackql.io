---
title: Вбудовані сервери MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - вбудований
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Вбудуйте сервер MCP StackQL безпосередньо у скомпільовані застосунки на Go, Rust, Swift, Kotlin, .NET і Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

Сервер MCP StackQL можна вбудувати безпосередньо у скомпільовані застосунки - без `npx`, без окремого встановлення й без залежності від середовища виконання. Підписаний двійковий файл завантажується й перевіряється під час першого запуску (режим sidecar) або включається через вендоринг у двійковий файл вашого застосунку, щоб отримати один автономний виконуваний файл.

Усі шість бібліотек дотримуються одного контракту: режим `read_only` за замовчуванням, запуск незалежно від поточного робочого каталогу (cwd), спільний кеш двійкових файлів у каталозі `~/.stackql/mcp-server-bin/` і ті самі чотири цільові платформи (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Також доступні нативні [завантаження CLI і MCPB](/installing-stackql) для Windows ARM64. Засоби визначення платформи в бібліотеках для Go, Rust і .NET, випущені у v0.12.778, і далі вибирають у Windows лише `windows-x64`; новий пакет не додає до цих бібліотек автоматичного вибору ARM64.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Вбудовування через go:embed або як sidecar; офіційний SDK MCP для Go. Демонстраційний застосунок: sandboxctl, інфраструктурний консьєрж на вимогу.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Режим sidecar або вендоринг через include_bytes!; клієнт rmcp. Демонстраційний застосунок: auditron, термінальний помічник для перевірки відповідності вимогам.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Включення нотаризованого двійкового файлу до підписаного застосунку macOS; офіційний SDK MCP для Swift. Демонстраційний застосунок: CloudLens, хмарний вартовий у рядку меню.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Бібліотека + плагін Gradle; офіційний SDK MCP для Kotlin. Демонстраційний застосунок: costgate, контроль витрат для CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Режим sidecar або пакет, включений через вендоринг; офіційний SDK MCP для C#. Демонстраційний застосунок: driftwatch, фоновий процес для перевірки дрейфу за розкладом.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Нагляд OTP на цільовій платформі Erlang/BEAM; завантажує й перевіряє підписаний двійковий файл. Демонстраційний застосунок: pipewatch (заплановано).'
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

## Спільний контракт {#common-contract}

- Режим `read_only` за замовчуванням і явне ввімкнення режиму `safe`, `delete_safe` або `full_access`.
- Двійковий файл нотаризовано Apple у macOS і підписано Authenticode у Windows.
- Закріплені хеші sha256 публікуються для кожного випуску.
- mcp-name `io.github.stackql/stackql-mcp` у реєстрі Official MCP Registry.

Див. довідник [Інструменти MCP](/mcp) і [способи встановлення](/installing-stackql).
