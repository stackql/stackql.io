---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - swift
  - macos
description: 공증된 StackQL MCP 서버 바이너리를 서명된 macOS 앱에 번들로 포함합니다
image: "/img/stackql-featured-image.png"
---

# Swift

StackQL MCP 서버를 macOS 애플리케이션에 내장합니다. 서명 및 공증된 바이너리를 `.app` 번들 안에 포함할 수 있으며, 클라이언트는 공식 Swift MCP SDK입니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

`Package.swift` 파일에 패키지를 추가합니다:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

macOS 13 이상과 Swift 6.1(Xcode 16.3 이상)이 필요합니다. 유일한 종속성은 공식 Swift MCP SDK입니다.

* * *

## 빠른 시작 {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

API는 `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)`, `.stop()` 메서드로 구성됩니다. `Options` 형식에는 `mode`, `auth`, `binaryOverride`, `allowDownload` 속성이 포함됩니다. 외부 하네스의 경우 `StackQLServer.resolveCommand(_:)` 메서드가 확인된 실행 명령을 반환합니다.

* * *

## 모드 {#modes}

`Options.mode` 속성의 기본값은 `.readOnly`입니다. 승격하려면 `.safe`, `.deleteSafe` 또는 `.fullAccess` 값으로 설정하세요. 승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

`darwin-universal` 바이너리는 Developer ID로 서명되고 Apple 공증을 받았으므로, 앱 자체의 공증을 유효하게 유지하면서 서명된 macOS `.app` 번들 안에 포함할 수 있습니다. 이는 Swift 라이브러리만의 특징입니다. 첫 실행 시 다운로드 없이, 자체 포함되고 공증된 단일 애플리케이션을 제공할 수 있습니다.

바이너리 확인 순서:

1. 명시적 `binaryOverride` 값.
2. 번들된 `.app` 리소스.
3. `~/.stackql/mcp-server-bin/` 디렉터리의 공유 캐시.
4. sha256 검증을 포함한 다운로드(`allowDownload` 속성이 설정된 경우).

* * *

## 데모 앱 {#demo-app}

**CloudLens**는 공증된 바이너리를 내장하고 `read_only` 검사 모음을 실행하는 메뉴 막대 클라우드 센티널입니다.

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
