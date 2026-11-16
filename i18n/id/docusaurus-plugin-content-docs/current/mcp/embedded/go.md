---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - go
description: Sematkan server MCP StackQL di aplikasi Go melalui go:embed atau sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Sematkan server MCP StackQL di aplikasi Go terkompilasi, baik sebagai sidecar yang diunduh maupun disertakan dalam biner Anda melalui vendoring dengan `go:embed`. Kliennya adalah SDK MCP resmi untuk Go.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Mulai cepat {#quick-start}

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

API library ini adalah `StartServer(ctx, Options{Binary, Mode})`; klien yang dikembalikan berasal dari SDK MCP resmi untuk Go.

* * *

## Mode {#modes}

Secara default, server dimulai dalam mode `read_only`. Atur `Options.Mode` untuk melakukan eskalasi ke `safe`, `delete_safe`, atau `full_access`. Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Lakukan vendoring biner dengan `go:embed` melalui langkah pengambilan `go:generate` untuk menghasilkan satu file executable mandiri:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Langkah pengambilan ini mengunduh biner yang ditandatangani untuk platform target dan memverifikasinya dengan sha256, lalu menulisnya ke lokasi yang dapat dijangkau `go:embed` untuk dimasukkan ke build Anda. Saat runtime, `StackqlMCPBinary()` mengembalikan biner tersemat sehingga tidak ada unduhan saat pertama kali dijalankan.

* * *

## Aplikasi demo {#demo-app}

**sandboxctl** adalah concierge infrastruktur: server tersemat ditambah loop agen Claude dalam satu biner yang merencanakan, memeriksa biaya, dan menyediakan sandbox cloud sementara di balik gerbang persetujuan.

* * *

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
