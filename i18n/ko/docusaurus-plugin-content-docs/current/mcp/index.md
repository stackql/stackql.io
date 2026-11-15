---
title: MCP 도구
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - 도구
description: StackQL MCP 서버가 제공하는 모든 도구를 용도별로 분류한 목록
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
    name: '서버',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: '연결된 MCP 서버의 ID 및 런타임'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: '--env.file dotenv 파일에서 자격 증명을 다시 로드하고 프로바이더별 상태를 보고'
      }
    ]
  },
  {
    id: 'discovery',
    name: '탐색',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: '로컬 StackQL 캐시로 가져온 프로바이더'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: '프로바이더 아래에서 사용할 수 있는 서비스'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'provider.service 아래에서 사용할 수 있는 리소스'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: '리소스에 사용할 수 있는 액세스 메서드'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: '단일 액세스 메서드의 전체 I/O 계약: 입력 및 출력 필드'
      }
    ]
  },
  {
    id: 'registry',
    name: '레지스트리',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: '레지스트리에서 사용할 수 있는 프로바이더 및 버전'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: '레지스트리에서 로컬 캐시로 프로바이더 설치'
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
        description: '의도, 프로바이더, 서비스 또는 태그로 큐레이션된 쿼리 템플릿 검색'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: '템플릿을 조회하고 매개 변수를 검증하여 실행 없이 SQL 렌더링'
      }
    ]
  },
  {
    id: 'query',
    name: '쿼리',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: '실행하지 않고 SELECT 문의 구문 분석 및 실행 계획 수립'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: '프로바이더를 대상으로 SELECT 문 실행'
      }
    ]
  },
  {
    id: 'mutation',
    name: '변경 작업 및 수명 주기',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: '프로바이더를 대상으로 INSERT, UPDATE, REPLACE 또는 DELETE 문 실행'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'StackQL EXEC 수명 주기 작업 실행'
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
        다음은 StackQL <a href="/command-line-usage/mcp">MCP 서버</a>가 제공하는 도구입니다. 변경 작업 도구와 수명 주기 도구의 사용 가능 여부는 서버 모드(<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>)에 따라 다릅니다. MCP 서버를 로컬에 설치하는 방법은 <a href="/installing-stackql#prebuilt-mcpb-bundle">여기</a>를 참조하세요.
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
          alt="Claude AI Directory에 등록된 StackQL MCP 서버"
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
          <span className={badgeStyles.eyebrow}>등록처</span>
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
