---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - kotlin
  - jvm
  - gradle
description: StackQL MCP 서버를 라이브러리 또는 Gradle 플러그인으로 Kotlin/JVM 애플리케이션에 내장합니다
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

StackQL MCP 서버를 라이브러리 또는 Gradle 플러그인으로 Kotlin 또는 JVM 애플리케이션에 내장합니다. 클라이언트는 공식 Kotlin MCP SDK입니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

`io.stackql:stackql-mcp` 라이브러리를 추가합니다:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

JDK 17 및 Kotlin 2.x가 필요합니다. 클라이언트는 공식 Kotlin MCP SDK에서 제공됩니다.

* * *

## 빠른 시작 {#quick-start}

```kotlin
import io.stackql.mcp.LaunchArgs
import io.stackql.mcp.Mode
import io.stackql.mcp.StackqlMcp

suspend fun main() {
    val server = StackqlMcp.builder()
        .mode(Mode.ReadOnly)
        .auth(LaunchArgs.authFor("github", "null_auth"))
        .start()

    server.use {
        val tools = server.client.listTools().tools
        println("${tools.size} tools available")
    }
}
```

API는 `StackqlMcp.builder()` 메서드와 `.mode()`, `.auth()`, `.start()` 메서드, `Mode` 열거형, `LaunchArgs.authFor()` 메서드로 구성됩니다.

* * *

## 모드 {#modes}

빌더의 기본값은 `Mode.ReadOnly`입니다. 승격하려면 `Mode.Safe`, `Mode.DeleteSafe` 또는 `Mode.FullAccess` 값으로 `.mode()` 메서드를 호출하세요. 승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

라이브러리는 작업 디렉터리와 관계없이 서명된 바이너리를 다운로드하고 검증한 다음 `~/.stackql/mcp-server-bin/` 디렉터리의 공유 캐시에 캐시합니다. Gradle 플러그인은 동일한 실행 방식을 빌드에 연결하므로 CI 작업에서 별도 설치 단계 없이 StackQL 쿼리를 실행할 수 있습니다.

* * *

## 데모 앱 {#demo-app}

**costgate**는 비용 예산을 기준으로 인프라 배포를 게이트 제어합니다. CLI와 Gradle 플러그인으로 모두 제공됩니다.

CLI로 사용하는 경우:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Gradle 플러그인으로 사용하는 경우:

```kotlin
plugins {
    id("io.stackql.costgate") version "0.1.0"
}

costgate {
    intent.set(layout.projectDirectory.file("costgate.yaml"))
    explain.set(true)
}
```

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
