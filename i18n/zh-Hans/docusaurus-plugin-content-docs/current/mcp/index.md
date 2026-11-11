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
description: StackQL MCP 服务器公开的所有工具，按用途分组
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
    name: '服务器',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: '已连接的 MCP 服务器的标识和运行时'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: '从 --env.file 指定的 dotenv 文件重新加载凭据，并报告每个 Provider 的状态'
      }
    ]
  },
  {
    id: 'discovery',
    name: '发现',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: '已拉取到本地 StackQL 缓存中的 Provider'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: '某个 Provider 下可用的服务'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: '某个 provider.service 下可用的资源'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: '某个资源可用的访问方法'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: '单个访问方法的完整 I/O 契约：输入和输出字段'
      }
    ]
  },
  {
    id: 'registry',
    name: '注册表',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: '注册表中可用的 Provider 及其版本'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: '将 Provider 从注册表安装到本地缓存'
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
        description: '按意图、Provider、服务或标签查找精选的查询模板'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: '获取模板并验证参数，以渲染 SQL 而不执行它'
      }
    ]
  },
  {
    id: 'query',
    name: '查询',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: '解析并规划 SELECT 而不执行它'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: '对某个 Provider 执行 SELECT'
      }
    ]
  },
  {
    id: 'mutation',
    name: '变更与生命周期',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: '对某个 Provider 运行 INSERT、UPDATE、REPLACE 或 DELETE'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: '执行 StackQL EXEC 生命周期操作'
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
        以下是 StackQL <a href="/command-line-usage/mcp">MCP 服务器</a>公开的工具。变更和生命周期工具是否可用取决于服务器模式（<code>read_only</code>、<code>safe</code>、<code>delete_safe</code>、<code>full_access</code>）。有关在本地安装 MCP 服务器的信息，请参阅<a href="/installing-stackql#prebuilt-mcpb-bundle">此处</a>。
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
          alt="StackQL MCP 服务器，已收录于 Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>已收录于</span>
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
