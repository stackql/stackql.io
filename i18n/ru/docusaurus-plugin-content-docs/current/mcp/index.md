---
title: MCP-инструменты
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - инструменты
description: Все инструменты MCP-сервера StackQL, сгруппированные по назначению
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
        description: 'Идентификационные сведения и среда выполнения подключенного MCP-сервера'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Повторная загрузка учетных данных из файла dotenv, заданного флагом --env.file, и отчет о состоянии по каждому провайдеру'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Обнаружение',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Провайдеры, загруженные в локальный кэш StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Сервисы, доступные в провайдере'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Ресурсы, доступные в provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Методы доступа, которые поддерживает ресурс'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Полный контракт входных и выходных данных одного метода доступа: входные параметры и выходные поля'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Реестр',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Провайдеры и версии, доступные в реестре'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Установка провайдера из реестра в локальный кэш'
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
        description: 'Поиск отобранных шаблонов запросов по намерению, провайдеру, сервису или тегам'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Получение шаблона и проверка параметров для формирования SQL без его выполнения'
      }
    ]
  },
  {
    id: 'query',
    name: 'Запросы',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Синтаксический анализ и планирование инструкции SELECT без ее выполнения'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Выполнение инструкции SELECT для провайдера'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Изменение данных и жизненный цикл',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Выполнение инструкций INSERT, UPDATE, REPLACE или DELETE для провайдера'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Выполнение операции жизненного цикла StackQL с помощью EXEC'
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
        Это инструменты, которые предоставляет <a href="/command-line-usage/mcp">MCP-сервер</a> StackQL.  Доступность инструментов изменения данных и жизненного цикла зависит от режима сервера (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Сведения о локальной установке MCP-сервера см. в <a href="/installing-stackql#prebuilt-mcpb-bundle">этом разделе</a>.
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
          alt="MCP-сервер StackQL - опубликован в Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>ОПУБЛИКОВАН В</span>
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
