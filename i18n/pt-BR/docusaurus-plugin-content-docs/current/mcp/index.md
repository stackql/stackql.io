---
title: Ferramentas MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - ferramentas
description: Todas as ferramentas expostas pelo servidor MCP do StackQL, agrupadas por finalidade
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
    name: 'Servidor',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'Identidade e runtime do servidor MCP conectado'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Recarregar as credenciais do arquivo dotenv de --env.file e informar o status de cada provider'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Descoberta',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Providers baixados no cache local do StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Serviços disponíveis em um provider'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Recursos disponíveis em um provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Métodos de acesso disponíveis para um recurso'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Contrato completo de E/S de um método de acesso: entradas e campos de saída'
      }
    ]
  },
  {
    id: 'registry',
    name: 'Registro',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Providers e versões disponíveis no registro'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Instalar um provider do registro no cache local'
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
        description: 'Encontrar templates de consulta selecionados por intenção, provider, serviço ou tags'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Recuperar um template e validar os parâmetros para renderizar o SQL sem executá-lo'
      }
    ]
  },
  {
    id: 'query',
    name: 'Consulta',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'Analisar e planejar um SELECT sem executá-lo'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Executar um SELECT em um provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutação e ciclo de vida',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Executar INSERT, UPDATE, REPLACE ou DELETE em um provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Executar uma operação de ciclo de vida EXEC do StackQL'
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
        Estas são as ferramentas expostas pelo <a href="/command-line-usage/mcp">servidor MCP</a> do StackQL.  A disponibilidade das ferramentas de mutação e de ciclo de vida depende do modo do servidor (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Para obter informações sobre como instalar o servidor MCP localmente, consulte <a href="/installing-stackql#prebuilt-mcpb-bundle">esta seção</a>.
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
          alt="Servidor MCP do StackQL - Listado no Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>LISTADO NO</span>
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
