---
title: 組み込み MCP サーバー
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: StackQL MCP サーバーを Go、Rust、Swift、Kotlin、.NET、Gleam のコンパイル済みアプリケーションに直接組み込みます
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

StackQL の MCP サーバーは、コンパイル済みアプリケーションに直接組み込むことができます。`npx` も、個別のインストールも、ランタイム依存関係も必要ありません。署名済みバイナリは、初回実行時にダウンロードされて検証される（サイドカーモード）か、アプリのバイナリにベンダリングされて単一の自己完結型実行可能ファイルになります。

6 つのライブラリはすべて共通のコントラクトに従います。デフォルトで `read_only` モードであること、作業ディレクトリに依存しない起動、`~/.stackql/mcp-server-bin/` の共有バイナリキャッシュ、同じ 4 つのプラットフォームターゲット（`linux-x64`、`linux-arm64`、`windows-x64`、`darwin-universal`）です。

ネイティブの Windows ARM64 向けの [CLI と MCPB のダウンロード](/installing-stackql)も利用できます。v0.12.778 に含まれる Go、Rust、.NET のプラットフォームリゾルバーは、Windows では引き続き `windows-x64` のみを選択します。新しいバンドルによって、これらのライブラリに ARM64 の自動選択が追加されることはありません。

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'go:embed またはサイドカーで組み込み。公式 Go MCP SDK。デモ：sandboxctl（オンデマンドのインフラストラクチャコンシェルジュ）。'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'サイドカーまたは include_bytes! によるベンダリング。rmcp クライアント。デモ：auditron（ターミナル向けのコンプライアンスコパイロット）。'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: '公証済みバイナリを署名済み macOS アプリにバンドル。公式 Swift MCP SDK。デモ：CloudLens（メニューバー常駐のクラウドセンチネル）。'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'ライブラリ + Gradle プラグイン。公式 Kotlin MCP SDK。デモ：costgate（CI/CD 向けのコストゲート）。'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'サイドカーまたはベンダリングされたバンドル。公式 C# MCP SDK。デモ：driftwatch（スケジュール実行のドリフトチェックワーカー）。'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'Erlang/BEAM ターゲット上で OTP のスーパービジョン下で動作。署名済みバイナリをダウンロードして検証。デモ：pipewatch（予定）。'
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

## 共通コントラクト {#common-contract}

- デフォルトは `read_only` モードで、`safe`、`delete_safe`、`full_access` には明示的なオプトインが必要です。
- バイナリは、macOS では Apple の公証済み、Windows では Authenticode 署名済みです。
- 固定用の sha256 値はリリースごとに公開されます。
- Official MCP Registry 上の mcp-name は `io.github.stackql/stackql-mcp` です。

[MCP ツールリファレンス](/mcp)と[インストール方法](/installing-stackql)を参照してください。
