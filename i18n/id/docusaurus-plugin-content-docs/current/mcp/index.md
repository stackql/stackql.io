---
title: Alat MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - alat
description: Semua alat yang diekspos oleh server MCP StackQL, dikelompokkan menurut tujuannya
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
    name: 'Server',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'Identitas dan runtime server MCP yang terhubung'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Memuat ulang kredensial dari file dotenv --env.file dan melaporkan status per provider'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Penemuan',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Provider yang telah diunduh ke cache StackQL lokal'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Layanan yang tersedia di bawah suatu provider'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Sumber daya yang tersedia di bawah suatu provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Metode akses yang tersedia untuk suatu sumber daya'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Kontrak I/O lengkap dari satu metode akses: input dan field output'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Registri',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Provider dan versi yang tersedia di registri'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Menginstal provider dari registri ke cache lokal'
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
        description: 'Menemukan template kueri terkurasi berdasarkan maksud, provider, layanan, atau tag'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Mengambil template dan memvalidasi parameter untuk merender SQL tanpa menjalankannya'
      }
    ]
  },
  {
    id: 'query',
    name: 'Kueri',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Mengurai dan merencanakan SELECT tanpa menjalankannya'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Menjalankan SELECT terhadap provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutasi & siklus hidup',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Menjalankan INSERT, UPDATE, REPLACE, atau DELETE terhadap provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Menjalankan operasi siklus hidup EXEC StackQL'
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
        Berikut adalah alat yang diekspos oleh <a href="/command-line-usage/mcp">server MCP</a> StackQL.  Ketersediaan alat mutasi dan siklus hidup bergantung pada mode server (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Untuk informasi tentang cara menginstal server MCP secara lokal, lihat <a href="/installing-stackql#prebuilt-mcpb-bundle">bagian ini</a>.
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
          alt="Server MCP StackQL - Terdaftar di Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>TERDAFTAR DI</span>
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
