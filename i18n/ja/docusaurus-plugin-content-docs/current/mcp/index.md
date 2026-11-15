---
title: MCP ツール
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - ツール
description: StackQL MCP サーバーが公開するすべてのツールの目的別一覧
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import ThemedImage from '@theme/ThemedImage';
import React from 'react';
import { FaGithub, FaRobot } from 'react-icons/fa';
import badgeStyles from '@site/docs/mcp/badges.module.css';

{/* MCP Tool Map - single source of truth */}
export const MCP_TOOL_CATEGORIES = [
  {
    id: 'server',
    name: 'サーバー',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: '接続中の MCP サーバーの識別情報とランタイム'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: '--env.file の dotenv ファイルから認証情報を再読み込みし、プロバイダーごとの状態を報告'
      }
    ]
  },
  {
    id: 'discovery',
    name: '検出',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'ローカルの StackQL キャッシュにプル済みのプロバイダー'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'プロバイダー配下で利用可能なサービス'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'provider.service 配下で利用可能なリソース'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'リソースで利用可能なアクセスメソッド'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: '1 つのアクセスメソッドの完全な I/O コントラクト：入力フィールドと出力フィールド'
      }
    ]
  },
  {
    id: 'registry',
    name: 'レジストリ',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'レジストリで利用可能なプロバイダーとバージョン'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'レジストリからローカルキャッシュにプロバイダーをインストール'
      }
    ]
  },
  {
    id: 'query-library',
    name: 'Query Library',
    tools: [
      {
        name: 'query_library_search',
        href: '/mcp/query_library_search',
        description: 'キュレーション済みのクエリテンプレートを意図、プロバイダー、サービス、タグで検索'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'テンプレートを取得してパラメーターを検証し、実行せずに SQL をレンダリング'
      }
    ]
  },
  {
    id: 'query',
    name: 'クエリ',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'SELECT を実行せずに解析し、計画を作成'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'プロバイダーに対して SELECT を実行'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'ミューテーションとライフサイクル',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'プロバイダーに対して INSERT、UPDATE、REPLACE、DELETE を実行'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'StackQL の EXEC ライフサイクル操作を実行'
      }
    ]
  }
];

{/* Custom TOC - generated from the tool map */}
export const CustomTOC = () => {
  return (
    <div className="table-of-contents table-of-contents__left-border">
      <ul className="toc-headings">
        {MCP_TOOL_CATEGORIES.map(category => (
          <li key={category.id}>
            <a href={`#${category.id}`}>{category.name}</a>
            <ul>
              {category.tools.map(tool => (
                <li key={tool.name}>
                  <a href={tool.href}>{tool.name}</a>
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

{/* Main content rendered from the tool map */}
export const ToolContent = () => {
  return (
    <>
      <blockquote>
        StackQL <a href="/command-line-usage/mcp">MCP サーバー</a>が公開するツールは次のとおりです。ミューテーションツールとライフサイクルツールを利用できるかどうかは、サーバーモード（<code>read_only</code>、<code>safe</code>、<code>delete_safe</code>、<code>full_access</code>）によって異なります。MCP サーバーをローカルにインストールする方法については、<a href="/installing-stackql#prebuilt-mcpb-bundle">こちら</a>を参照してください。
      </blockquote>

      {MCP_TOOL_CATEGORIES.map(category => (
        <div key={category.id}>
          <h2 id={category.id}>{category.name}</h2>
          <DocCardList
            items={category.tools.map(tool => ({
              type: 'link',
              label: tool.name,
              href: tool.href,
              description: tool.description,
              customProps: { iconComponent: <FaRobot size={18} className="homeCardIcon" /> }
            }))}
          />
        </div>
      ))}
    </>
  );
};

<div className="row">
  <div className="col col--9">
    <div style={{ display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: '12px', marginBottom: '1rem' }}>
      <a href="https://claude.ai/directory/ant.dir.gh.stackql.stackql" target="_blank" rel="noopener noreferrer" style={{ display: 'inline-flex' }}>
        <ThemedImage
          alt="Claude AI Directory に掲載された StackQL MCP サーバー"
          width="220"
          height="54"
          sources={{
            light: 'https://www.claudeai.directory/badge/stackql-mcp-server',
            dark: 'https://www.claudeai.directory/badge/stackql-mcp-server?theme=dark',
          }}
        />
      </a>
      <a
        href="https://github.com/mcp?search=stackql"
        target="_blank"
        rel="noopener noreferrer"
        className={badgeStyles.badge}
      >
        <FaGithub size={28} aria-hidden="true" />
        <span className={badgeStyles.label}>
          <span className={badgeStyles.eyebrow}>掲載先</span>
          <strong className={badgeStyles.title}>GitHub MCP Registry</strong>
        </span>
      </a>
    </div>
    <ToolContent />
  </div>
  <div className="col col--3">
    <CustomTOC />
  </div>
</div>
