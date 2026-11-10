---
title: MCP-Tools
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - Tools
description: Alle vom StackQL-MCP-Server bereitgestellten Tools, nach Zweck gruppiert
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
        description: 'Identität und Laufzeitumgebung des verbundenen MCP-Servers'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Anmeldedaten erneut aus der dotenv-Datei von --env.file einlesen und den Status pro Provider melden'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Discovery',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'In den lokalen StackQL-Cache geladene Provider'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Unter einem Provider verfügbare Services'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Unter einem provider.service verfügbare Ressourcen'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Für eine Ressource verfügbare Zugriffsmethoden'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Vollständiger I/O-Vertrag einer Zugriffsmethode: Eingaben und Ausgabefelder'
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
        description: 'In der Registry verfügbare Provider und Versionen'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Einen Provider aus der Registry in den lokalen Cache installieren'
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
        description: 'Kuratierte Abfragevorlagen nach Absicht, Provider, Service oder Tags finden'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Eine Vorlage abrufen und Parameter validieren, um SQL zu rendern, ohne es auszuführen'
      }
    ]
  },
  {
    id: 'query',
    name: 'Abfrage',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Ein SELECT parsen und planen, ohne es auszuführen'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Ein SELECT gegen einen Provider ausführen'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutation & Lebenszyklus',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'INSERT, UPDATE, REPLACE oder DELETE gegen einen Provider ausführen'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Eine StackQL-EXEC-Lebenszyklusoperation ausführen'
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
        Dies sind die Tools, die der <a href="/command-line-usage/mcp">MCP-Server</a> von StackQL bereitstellt.  Die Verfügbarkeit der Mutations- und Lebenszyklus-Tools hängt vom Servermodus ab (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Informationen zur lokalen Installation des MCP-Servers finden Sie <a href="/installing-stackql#prebuilt-mcpb-bundle">hier</a>.
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
          alt="StackQL-MCP-Server - gelistet im Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>GELISTET IN</span>
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
