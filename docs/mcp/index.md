---
title: MCP Tools
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - tools
description: All tools exposed by the StackQL MCP server, grouped by purpose
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

{/* MCP Tool Map - single source of truth */}
export const MCP_TOOL_CATEGORIES = [
  {
    id: 'server',
    name: 'Server',
    tools: [
      {
        name: 'server_info',
        href: '/docs/mcp/server_info',
        description: 'Identity and runtime of the connected MCP server'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Discovery',
    tools: [
      {
        name: 'list_providers',
        href: '/docs/mcp/list_providers',
        description: 'Providers pulled into the local StackQL cache'
      },
      {
        name: 'list_services',
        href: '/docs/mcp/list_services',
        description: 'Services available under a provider'
      },
      {
        name: 'list_resources',
        href: '/docs/mcp/list_resources',
        description: 'Resources available under a provider.service'
      },
      {
        name: 'list_methods',
        href: '/docs/mcp/list_methods',
        description: 'Access methods available for a resource'
      },
      {
        name: 'describe_resource',
        href: '/docs/mcp/describe_resource',
        description: 'Output fields of a resource'
      },
      {
        name: 'describe_method',
        href: '/docs/mcp/describe_method',
        description: 'Full I/O contract of one access method'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Registry',
    tools: [
      {
        name: 'list_registry',
        href: '/docs/mcp/list_registry',
        description: 'Providers and versions available in the registry'
      },
      {
        name: 'pull_provider',
        href: '/docs/mcp/pull_provider',
        description: 'Install a provider from the registry into the local cache'
      }
    ]
  },
  {
    id: 'query',
    name: 'Query',
    tools: [
      {
        name: 'validate_select_query',
        href: '/docs/mcp/validate_select_query',
        description: 'Parse and plan a SELECT without executing it'
      },
      {
        name: 'run_select_query',
        href: '/docs/mcp/run_select_query',
        description: 'Execute a SELECT against a provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutation & Lifecycle',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/docs/mcp/run_mutation_query',
        description: 'Run INSERT, UPDATE, REPLACE, or DELETE against a provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/docs/mcp/run_lifecycle_operation',
        description: 'Execute a StackQL EXEC lifecycle operation'
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
        These are the tools exposed by the StackQL <a href="/docs/command-line-usage/mcp">MCP server</a>.  Availability of mutation and lifecycle tools depends on the server mode (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>).
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
    <ToolContent />
  </div>
  <div className="col col--3">
    <CustomTOC />
  </div>
</div>
