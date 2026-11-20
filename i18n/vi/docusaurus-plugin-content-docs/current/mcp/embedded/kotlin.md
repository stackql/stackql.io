---
title: Kotlin / JVM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - kotlin
  - jvm
  - gradle
description: Nhúng máy chủ MCP StackQL vào một ứng dụng Kotlin/JVM dưới dạng thư viện hoặc plugin Gradle
image: "/img/stackql-featured-image.png"
---

# Kotlin / JVM

Nhúng máy chủ MCP StackQL vào một ứng dụng Kotlin hoặc JVM, dưới dạng thư viện hoặc plugin Gradle. Ứng dụng khách là SDK MCP chính thức cho Kotlin.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

Thêm thư viện `io.stackql:stackql-mcp`:

```kotlin
dependencies {
    implementation("io.stackql:stackql-mcp:0.1.0")
}
```

Yêu cầu JDK 17 và Kotlin 2.x. Ứng dụng khách đến từ SDK MCP chính thức cho Kotlin.

* * *

## Bắt đầu nhanh {#quick-start}

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

API gồm `StackqlMcp.builder()` với `.mode()`, `.auth()` và `.start()`, enum `Mode` và `LaunchArgs.authFor()`.

* * *

## Chế độ {#modes}

Builder mặc định dùng `Mode.ReadOnly`. Gọi `.mode()` với `Mode.Safe`, `Mode.DeleteSafe` hoặc `Mode.FullAccess` để nâng mức quyền. Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Thư viện tải xuống, xác minh và lưu tệp nhị phân đã ký vào cache dùng chung tại `~/.stackql/mcp-server-bin/`, không phụ thuộc vào thư mục làm việc. Plugin Gradle đưa cùng cơ chế khởi chạy đó vào bản dựng của bạn, nhờ vậy các tác vụ CI có thể chạy truy vấn StackQL mà không cần bước cài đặt riêng.

* * *

## Ứng dụng minh họa {#demo-app}

**costgate** kiểm soát việc triển khai hạ tầng dựa trên ngân sách chi phí. Ứng dụng được phát hành cả dưới dạng CLI lẫn dưới dạng plugin Gradle.

Dưới dạng CLI:

```bash
costgate check --intent examples/costgate.yaml --explain
```

Dưới dạng plugin Gradle:

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

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-kotlin](https://github.com/stackql/stackql-mcp-kotlin)
