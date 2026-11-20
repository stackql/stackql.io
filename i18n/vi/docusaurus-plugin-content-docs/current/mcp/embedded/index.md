---
title: Máy chủ MCP nhúng
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: Nhúng máy chủ MCP StackQL trực tiếp vào các ứng dụng đã biên dịch bằng Go, Rust, Swift, Kotlin, .NET và Gleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

Máy chủ MCP của StackQL có thể được nhúng trực tiếp vào các ứng dụng đã biên dịch - không cần `npx`, không cần cài đặt riêng, không có phần phụ thuộc runtime. Tệp nhị phân đã ký được tải xuống và xác minh trong lần chạy đầu tiên (chế độ sidecar) hoặc được đưa vào tệp nhị phân ứng dụng của bạn theo kiểu vendoring để có một tệp thực thi độc lập duy nhất.

Cả sáu thư viện dùng chung một hợp đồng: chế độ `read_only` theo mặc định, khởi chạy không phụ thuộc vào thư mục làm việc hiện tại, cache tệp nhị phân dùng chung tại `~/.stackql/mcp-server-bin/` và cùng bốn nền tảng đích (`linux-x64`, `linux-arm64`, `windows-x64`, `darwin-universal`).

Hiện cũng đã có các [bản tải xuống CLI và MCPB](/installing-stackql) gốc cho Windows ARM64. Các trình phân giải nền tảng của Go, Rust và .NET được phát hành trong v0.12.778 vẫn chỉ chọn `windows-x64` trên Windows; bundle mới không bổ sung khả năng tự động chọn ARM64 cho các thư viện đó.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'Nhúng qua go:embed hoặc sidecar; SDK MCP chính thức cho Go. Ứng dụng minh họa: sandboxctl, trợ lý hạ tầng theo yêu cầu.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'Sidecar hoặc vendoring bằng include_bytes!; ứng dụng khách rmcp. Ứng dụng minh họa: auditron, trợ lý tuân thủ trên terminal.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'Đóng gói tệp nhị phân đã được công chứng trong một ứng dụng macOS đã được ký; SDK MCP chính thức cho Swift. Ứng dụng minh họa: CloudLens, trình canh gác đám mây trên thanh menu.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'Thư viện + plugin Gradle; SDK MCP chính thức cho Kotlin. Ứng dụng minh họa: costgate, cổng kiểm soát chi phí cho CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'Sidecar hoặc bundle vendored; SDK MCP chính thức cho C#. Ứng dụng minh họa: driftwatch, worker kiểm tra sai lệch theo lịch.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Được giám sát bởi OTP trên đích biên dịch Erlang/BEAM; tải xuống và xác minh tệp nhị phân đã ký. Ứng dụng minh họa: pipewatch (dự kiến).'
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

## Hợp đồng chung {#common-contract}

- Chế độ `read_only` theo mặc định, cần bật một cách rõ ràng để dùng `safe`, `delete_safe` hoặc `full_access`.
- Tệp nhị phân được Apple công chứng trên macOS và được ký Authenticode trên Windows.
- Các giá trị ghim sha256 được công bố cho mỗi bản phát hành.
- mcp-name `io.github.stackql/stackql-mcp` trên Official MCP Registry.

Xem [tài liệu tham khảo công cụ MCP](/mcp) và [các cách cài đặt](/installing-stackql).
