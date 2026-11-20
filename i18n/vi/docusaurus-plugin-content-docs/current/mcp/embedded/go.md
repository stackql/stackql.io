---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - go
description: Nhúng máy chủ MCP StackQL vào một ứng dụng Go qua go:embed hoặc sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Nhúng máy chủ MCP StackQL vào một ứng dụng Go đã biên dịch, dưới dạng sidecar được tải xuống hoặc được đưa vào tệp nhị phân của bạn theo kiểu vendoring bằng `go:embed`. Ứng dụng khách là SDK MCP chính thức cho Go.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Bắt đầu nhanh {#quick-start}

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

API là `StartServer(ctx, Options{Binary, Mode})`; ứng dụng khách được trả về đến từ SDK MCP chính thức cho Go.

* * *

## Chế độ {#modes}

Theo mặc định, máy chủ khởi động ở chế độ `read_only`. Đặt `Options.Mode` để nâng mức quyền lên `safe`, `delete_safe` hoặc `full_access`. Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Đưa tệp nhị phân vào theo kiểu vendoring bằng `go:embed` thông qua một bước tải `go:generate`, tạo ra một tệp thực thi độc lập duy nhất:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Bước tải sẽ tải xuống và xác minh sha256 tệp nhị phân đã ký cho nền tảng đích, sau đó ghi tệp vào vị trí mà `go:embed` có thể đưa vào bản dựng của bạn. Khi chạy, `StackqlMCPBinary()` trả về tệp nhị phân đã nhúng nên không có bước tải xuống trong lần chạy đầu tiên.

* * *

## Ứng dụng minh họa {#demo-app}

**sandboxctl** là một trợ lý hạ tầng: một máy chủ nhúng cùng vòng lặp tác nhân Claude trong một tệp nhị phân, có khả năng lập kế hoạch, kiểm tra chi phí và cấp phát các sandbox đám mây tạm thời sau một cổng phê duyệt.

* * *

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
