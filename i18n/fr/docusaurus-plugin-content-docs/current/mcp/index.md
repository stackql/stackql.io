---
title: Outils MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - outils
description: Tous les outils exposés par le serveur MCP StackQL, regroupés par finalité
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
    name: 'Serveur',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'Identité et runtime du serveur MCP connecté'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: "Recharger les identifiants depuis le fichier dotenv indiqué par --env.file et signaler l'état de chaque provider"
      }
    ]
  },
  {
    id: 'discovery',
    name: 'Découverte',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'Providers téléchargés dans le cache local de StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'Services disponibles pour un provider'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'Ressources disponibles pour un provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: "Méthodes d'accès disponibles pour une ressource"
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: "Contrat d'E/S complet d'une méthode d'accès : entrées et champs de sortie"
      }
    ]
  },
  {
    id: 'registry',
    name: 'Registre',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'Providers et versions disponibles dans le registre'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'Installer un provider du registre dans le cache local'
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
        description: 'Rechercher des modèles de requête sélectionnés, par intention, provider, service ou tags'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: "Récupérer un modèle et valider les paramètres pour obtenir le SQL rendu sans l'exécuter"
      }
    ]
  },
  {
    id: 'query',
    name: 'Requête',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: "Analyser et planifier un SELECT sans l'exécuter"
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'Exécuter un SELECT sur un provider'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'Mutation et cycle de vie',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'Exécuter INSERT, UPDATE, REPLACE ou DELETE sur un provider'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'Exécuter une opération de cycle de vie EXEC de StackQL'
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
        Voici les outils exposés par le <a href="/command-line-usage/mcp">serveur MCP</a> de StackQL.  La disponibilité des outils de mutation et de cycle de vie dépend du mode du serveur (<code>read_only</code>, <code>safe</code>, <code>delete_safe</code>, <code>full_access</code>). Pour savoir comment installer le serveur MCP en local, consultez <a href="/installing-stackql#prebuilt-mcpb-bundle">cette section</a>.
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
          alt="Serveur MCP StackQL - référencé dans le Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>RÉFÉRENCÉ DANS LE</span>
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
