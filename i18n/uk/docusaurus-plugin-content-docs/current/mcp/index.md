---
title: Інструменти MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - інструменти
description: Усі інструменти, які надає сервер MCP StackQL, згруповані за призначенням
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
    name: 'Сервер',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'Ідентичність і середовище виконання підключеного сервера MCP'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Повторне завантаження облікових даних із файлу dotenv, вказаного в --env.file, і звіт про стан для кожного провайдера'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Виявлення',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Провайдери, завантажені в локальний кеш StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Сервіси, доступні в провайдері'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Ресурси, доступні в provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Методи доступу, які підтримує ресурс'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Повний контракт вхідних і вихідних даних одного методу доступу: вхідні параметри й вихідні поля'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Реєстр',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Провайдери та версії, доступні в реєстрі'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Встановлення провайдера з реєстру в локальний кеш'
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
        description: 'Пошук відібраних шаблонів запитів за наміром, провайдером, сервісом або тегами'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Отримання шаблону й перевірка параметрів для формування SQL без його виконання'
      }
    ]
  },
  {
    id: 'query',
    name: 'Запити',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Синтаксичний аналіз і планування інструкції SELECT без її виконання'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Виконання інструкції SELECT для провайдера'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Змінення даних і життєвий цикл',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Виконання інструкцій INSERT, UPDATE, REPLACE або DELETE для провайдера'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Виконання операції життєвого циклу StackQL за допомогою EXEC'
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
        Це інструменти, які надає <a href="/command-line-usage/mcp">сервер MCP</a> StackQL.  Доступність інструментів змінення даних і життєвого циклу залежить від режиму сервера (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Відомості про локальне встановлення сервера MCP див. у <a href="/installing-stackql#prebuilt-mcpb-bundle">цьому розділі</a>.
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
          alt="Сервер MCP StackQL - опубліковано в Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>ОПУБЛІКОВАНО В</span>
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
