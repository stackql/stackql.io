---
title: MCP 工具
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - 工具
description: StackQL MCP 伺服器公開的所有工具，依用途分組
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
    name: '伺服器',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: '已連線 MCP 伺服器的身分與執行階段資訊'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: '從 --env.file 指定的 dotenv 檔案重新載入憑證，並回報各 Provider 的狀態'
      }
    ]
  },
  {
    id: 'discovery',
    name: '探索',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: '已提取至本機 StackQL 快取的 Provider'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Provider 下可用的服務'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'provider.service 下可用的資源'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: '資源可用的存取方法'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: '單一存取方法的完整 I/O 契約：輸入與輸出欄位'
      }
    ]
  },
  {
    id: 'registry',
    name: '登錄庫',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: '登錄庫中可用的 Provider 與版本'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: '將 Provider 從登錄庫安裝到本機快取'
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
        description: '依意圖、Provider、服務或標籤尋找精選的查詢範本'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: '擷取範本並驗證參數，以轉譯 SQL 而不執行'
      }
    ]
  },
  {
    id: 'query',
    name: '查詢',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: '剖析並規劃 SELECT，但不執行'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: '對 Provider 執行 SELECT'
      }
    ]
  },
  {
    id: 'mutation',
    name: '變更與生命週期',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: '對 Provider 執行 INSERT、UPDATE、REPLACE 或 DELETE'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: '執行 StackQL EXEC 生命週期作業'
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
        以下是 StackQL <a href="/command-line-usage/mcp">MCP 伺服器</a>公開的工具。變更與生命週期工具是否可用，取決於伺服器模式（<code>read_only</code>、<code>safe</code>、<code>delete_safe</code>、<code>full_access</code>）。如需在本機安裝 MCP 伺服器的資訊，請參閱<a href="/installing-stackql#prebuilt-mcpb-bundle">這裡</a>。
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
          alt="StackQL MCP 伺服器：已列於 Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>已列於</span>
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
