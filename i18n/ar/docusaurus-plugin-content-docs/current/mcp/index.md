---
title: أدوات MCP
hide_title: false
hide_table_of_contents: true
slug: /mcp
keywords:
  - stackql
  - mcp
  - model context protocol
  - أدوات
description: جميع الأدوات التي يوفرها خادم StackQL MCP، مجمعة حسب الغرض
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
    name: 'الخادم',
    tools: [
      {
        name: 'server_info',
        href: '/mcp/server_info',
        description: 'هوية خادم MCP المتصل ووقت تشغيله'
      },
      {
        name: 'reload_credentials',
        href: '/mcp/reload_credentials',
        description: 'إعادة تحميل بيانات الاعتماد من ملف dotenv المحدد بالعلامة --env.file والإبلاغ عن الحالة لكل موفر'
      }
    ]
  },
  {
    id: 'discovery',
    name: 'الاكتشاف',
    tools: [
      {
        name: 'list_providers',
        href: '/mcp/list_providers',
        description: 'الموفرون الذين تم سحبهم إلى ذاكرة التخزين المؤقت المحلية لـ StackQL'
      },
      {
        name: 'list_services',
        href: '/mcp/list_services',
        description: 'الخدمات المتاحة ضمن موفر'
      },
      {
        name: 'list_resources',
        href: '/mcp/list_resources',
        description: 'الموارد المتاحة ضمن provider.service'
      },
      {
        name: 'list_methods',
        href: '/mcp/list_methods',
        description: 'أساليب الوصول المتاحة لمورد'
      },
      {
        name: 'describe_method',
        href: '/mcp/describe_method',
        description: 'عقد الإدخال والإخراج الكامل لأسلوب وصول واحد: الإدخالات وحقول الإخراج'
      }
    ]
  },
  {
    id: 'registry',
    name: 'السجل',
    tools: [
      {
        name: 'list_registry',
        href: '/mcp/list_registry',
        description: 'الموفرون والإصدارات المتاحة في السجل'
      },
      {
        name: 'pull_provider',
        href: '/mcp/pull_provider',
        description: 'تثبيت موفر من السجل في ذاكرة التخزين المؤقت المحلية'
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
        description: 'البحث عن قوالب استعلامات منتقاة حسب الغرض أو الموفر أو الخدمة أو الوسوم'
      },
      {
        name: 'query_library_get',
        href: '/mcp/query_library_get',
        description: 'استرداد قالب والتحقق من صحة المعلمات لإنشاء SQL من القالب دون تنفيذه'
      }
    ]
  },
  {
    id: 'query',
    name: 'الاستعلام',
    tools: [
      {
        name: 'validate_select_query',
        href: '/mcp/validate_select_query',
        description: 'تحليل جملة SELECT وتخطيطها دون تنفيذها'
      },
      {
        name: 'run_select_query',
        href: '/mcp/run_select_query',
        description: 'تنفيذ جملة SELECT على موفر'
      }
    ]
  },
  {
    id: 'mutation',
    name: 'تعديل البيانات ودورة الحياة',
    tools: [
      {
        name: 'run_mutation_query',
        href: '/mcp/run_mutation_query',
        description: 'تشغيل INSERT أو UPDATE أو REPLACE أو DELETE على موفر'
      },
      {
        name: 'run_lifecycle_operation',
        href: '/mcp/run_lifecycle_operation',
        description: 'تنفيذ عملية دورة حياة EXEC في StackQL'
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
        هذه هي الأدوات التي يوفرها <a href="/command-line-usage/mcp">خادم MCP</a> الخاص بـ StackQL. يعتمد توفر أدوات تعديل البيانات ودورة الحياة على وضع الخادم (<code>read_only</code> و<code>safe</code> و<code>delete_safe</code> و<code>full_access</code>). تجد معلومات حول تثبيت خادم MCP محليًا <a href="/installing-stackql#prebuilt-mcpb-bundle">هنا</a>.
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
          alt="خادم StackQL MCP - مدرج في Claude AI Directory"
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
          <span className={badgeStyles.eyebrow}>مدرج في</span>
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
