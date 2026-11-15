---
title: Herramientas MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - herramientas
description: Todas las herramientas que expone el servidor MCP de StackQL, agrupadas por finalidad
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
        description: 'Identidad y runtime del servidor MCP conectado'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'Volver a cargar las credenciales desde el archivo dotenv de --env.file e informar del estado de cada provider'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Descubrimiento',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Providers descargados en la caché local de StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Servicios disponibles en un provider'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Recursos disponibles en un provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'Métodos de acceso disponibles para un recurso'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'Contrato de E/S completo de un método de acceso: entradas y campos de salida'
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
        description: 'Providers y versiones disponibles en el registro'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Instalar un provider del registro en la caché local'
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
        description: 'Buscar plantillas de consulta seleccionadas por intención, provider, servicio o tags'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'Recuperar una plantilla y validar los parámetros para renderizar el SQL sin ejecutarlo'
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
        description: 'Analizar y planificar un SELECT sin ejecutarlo'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Ejecutar un SELECT en un provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutación y ciclo de vida',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Ejecutar INSERT, UPDATE, REPLACE o DELETE en un provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Ejecutar una operación de ciclo de vida EXEC de StackQL'
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
        Estas son las herramientas que expone el <a href="/command-line-usage/mcp">servidor MCP</a> de StackQL.  La disponibilidad de las herramientas de mutación y de ciclo de vida depende del modo del servidor (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Para obtener información sobre cómo instalar el servidor MCP en local, consulte <a href="/installing-stackql#prebuilt-mcpb-bundle">esta sección</a>.
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
          alt="Servidor MCP de StackQL - Incluido en el Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>INCLUIDO EN</span>
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
