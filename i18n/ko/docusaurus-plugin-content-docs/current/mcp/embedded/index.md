---
title: 임베디드 MCP 서버
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: StackQL MCP 서버를 Go, Rust, Swift, Kotlin, .NET, Gleam의 컴파일된 애플리케이션에 직접 내장합니다
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

StackQL의 MCP 서버는 컴파일된 애플리케이션에 직접 내장할 수 있습니다. `npx` 명령도, 별도 설치도, 런타임 종속성도 필요하지 않습니다. 서명된 바이너리는 첫 실행 시 다운로드되어 검증되거나(사이드카 모드), 앱 바이너리에 벤더링되어 자체 포함된 단일 실행 파일이 됩니다.

6개 라이브러리는 모두 하나의 계약을 공유합니다. 기본값인 `read_only` 모드, cwd에 의존하지 않는 실행, `~/.stackql/mcp-server-bin/` 디렉터리의 공유 바이너리 캐시, 동일한 4개 플랫폼 대상(`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`)입니다.

네이티브 Windows ARM64용 [CLI 및 MCPB 다운로드](/installing-stackql)도 제공됩니다. v0.12.778에 포함된 Go, Rust 및 .NET 플랫폼 확인자는 Windows에서 여전히 `windows-x64` 대상만 선택합니다. 새 번들이 이러한 라이브러리에 ARM64 자동 선택 기능을 추가하지는 않습니다.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'go:embed 또는 사이드카로 내장. 공식 Go MCP SDK. 데모: 온디맨드 인프라 컨시어지인 sandboxctl.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: '사이드카 또는 include_bytes! 벤더링. rmcp 클라이언트. 데모: 터미널 규정 준수 코파일럿인 auditron.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: '공증된 바이너리를 서명된 macOS 앱에 번들로 포함. 공식 Swift MCP SDK. 데모: 메뉴 막대 클라우드 센티널인 CloudLens.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: '라이브러리 + Gradle 플러그인. 공식 Kotlin MCP SDK. 데모: CI/CD용 비용 게이트인 costgate.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: '사이드카 또는 벤더링된 번들. 공식 C# MCP SDK. 데모: 예약 실행 드리프트 검사 워커인 driftwatch.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Erlang/BEAM 대상에서 OTP 슈퍼비전 아래 실행. 서명된 바이너리를 다운로드하고 검증. 데모: pipewatch(예정).'
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

## 공통 계약 {#common-contract}

- 기본값은 `read_only` 모드이며, `safe`, `delete_safe` 또는 `full_access` 모드는 명시적 옵트인이 필요합니다.
- 바이너리는 macOS에서 Apple 공증을 받았고 Windows에서 Authenticode로 서명되었습니다.
- sha256 고정 값은 릴리스마다 게시됩니다.
- Official MCP Registry의 mcp-name은 `io.github.stackql/stackql-mcp`입니다.

[MCP 도구 참조](/mcp) 및 [설치 방법](/installing-stackql)을 참조하세요.
