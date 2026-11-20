---
title: Công cụ MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - công cụ
description: Tất cả công cụ do máy chủ MCP StackQL cung cấp, được nhóm theo mục đích
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
    name: 'Máy chủ',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'Danh tính và runtime của máy chủ MCP đang kết nối'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Tải lại thông tin xác thực từ tệp dotenv của --env.file và báo cáo trạng thái theo từng provider'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Khám phá',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Các provider đã tải xuống vào bộ nhớ đệm cục bộ của StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Các dịch vụ hiện có trong một provider'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Các tài nguyên hiện có trong một provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Các phương thức truy cập hiện có cho một tài nguyên'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Hợp đồng I/O đầy đủ của một phương thức truy cập: đầu vào và các trường đầu ra'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Registry',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Các provider và phiên bản hiện có trong registry'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Cài đặt một provider từ registry vào bộ nhớ đệm cục bộ'
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
        description: 'Tìm các template truy vấn được tuyển chọn theo ý định, provider, dịch vụ hoặc tag'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Lấy một template và kiểm tra tính hợp lệ của tham số để tạo SQL mà không thực thi'
      }
    ]
  },
  {
    id: 'query',
    name: 'Truy vấn',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Phân tích cú pháp và lập kế hoạch cho một câu lệnh SELECT mà không thực thi'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Thực thi một câu lệnh SELECT trên một provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Thay đổi dữ liệu & vòng đời',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Chạy INSERT, UPDATE, REPLACE hoặc DELETE trên một provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Thực thi một thao tác vòng đời EXEC của StackQL'
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
        Đây là các công cụ do <a href="/command-line-usage/mcp">máy chủ MCP</a> của StackQL cung cấp.  Khả năng sử dụng các công cụ thay đổi dữ liệu và công cụ vòng đời phụ thuộc vào chế độ máy chủ (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Để biết thông tin về cách cài đặt máy chủ MCP cục bộ, hãy xem <a href="/installing-stackql#prebuilt-mcpb-bundle">tại đây</a>.
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
          alt="Máy chủ MCP StackQL - Có mặt trên Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>CÓ MẶT TRÊN</span>
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
