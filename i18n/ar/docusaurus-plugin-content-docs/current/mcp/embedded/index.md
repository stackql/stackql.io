---
title: خوادم MCP المضمنة
hide_title: false
hide_table_of_contents: true
slug: /mcp/embedded
keywords:
  - stackql
  - mcp
  - model context protocol
  - مضمن
  - go
  - rust
  - swift
  - kotlin
  - dotnet
  - gleam
description: ضمّن خادم StackQL MCP مباشرة في التطبيقات المجمعة بلغات Go وRust وSwift وKotlin و.NET وGleam
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import React from 'react';
import { FaRobot } from 'react-icons/fa';

يمكن تضمين خادم MCP الخاص بـ StackQL مباشرة في التطبيقات المجمعة - دون `npx` ودون تثبيت منفصل ودون أي تبعية لوقت التشغيل. يتم تنزيل الملف الثنائي الموقع رقميًا والتحقق منه عند التشغيل الأول (وضع sidecar)، أو تضمينه كنسخة محلية في الملف الثنائي لتطبيقك للحصول على ملف تنفيذي واحد مستقل.

تشترك المكتبات الست جميعها في عقد واحد: الوضع `read_only` افتراضيًا، وتشغيل مستقل عن دليل العمل الحالي، وذاكرة تخزين مؤقت مشتركة للملف الثنائي في `~/.stackql/mcp-server-bin/`، والمنصات المستهدفة الأربع نفسها (`linux-x64` و`linux-arm64` و`windows-x64` و`darwin-universal`).

تتوفر أيضًا [تنزيلات CLI وMCPB](/installing-stackql) الأصلية لنظام Windows ARM64. لا تزال أدوات حل المنصة في Go وRust و.NET التي صدرت في الإصدار v0.12.778 تختار `windows-x64` فقط على Windows؛ ولا يضيف bundle الجديد اختيار ARM64 التلقائي إلى تلك المكتبات.

{/* Embedded language libraries - single source of truth */}
export const EMBEDDED_LIBRARIES = [
  {
    name: 'Go',
    href: '/mcp/embedded/go',
    description: 'التضمين عبر go:embed أو sidecar؛ Go MCP SDK الرسمي. التطبيق التوضيحي: sandboxctl، مساعد للبنية التحتية عند الطلب.'
  },
  {
    name: 'Rust',
    href: '/mcp/embedded/rust',
    description: 'sidecar أو تضمين نسخة محلية عبر include_bytes!؛ العميل rmcp. التطبيق التوضيحي: auditron، مساعد امتثال في الوحدة الطرفية.'
  },
  {
    name: 'Swift',
    href: '/mcp/embedded/swift',
    description: 'تضمين الملف الثنائي الموثق في تطبيق macOS موقع رقميًا؛ Swift MCP SDK الرسمي. التطبيق التوضيحي: CloudLens، حارس سحابي في شريط القوائم.'
  },
  {
    name: 'Kotlin / JVM',
    href: '/mcp/embedded/kotlin',
    description: 'مكتبة + مكون Gradle إضافي؛ Kotlin MCP SDK الرسمي. التطبيق التوضيحي: costgate، أداة لتقييد التكاليف في CI/CD.'
  },
  {
    name: '.NET / C#',
    href: '/mcp/embedded/dotnet',
    description: 'sidecar أو bundle مضمن كنسخة محلية؛ C# MCP SDK الرسمي. التطبيق التوضيحي: driftwatch، خدمة خلفية مجدولة لفحص الانحراف.'
  },
  {
    name: 'Gleam',
    href: '/mcp/embedded/gleam',
    description: 'يخضع لإشراف OTP على الهدف Erlang/BEAM؛ يقوم بتنزيل الملف الثنائي الموقع رقميًا والتحقق منه. التطبيق التوضيحي: pipewatch (مخطط له).'
  }
];

<DocCardList
  items={EMBEDDED_LIBRARIES.map(lib => ({
    type: 'link',
    label: lib.name,
    href: lib.href,
    description: lib.description,
    customProps: { iconComponent: <FaRobot size={18} className="homeCardIcon" /> }
  }))}
/>

## العقد المشترك {#common-contract}

- الوضع `read_only` افتراضيًا، مع تفعيل صريح للأوضاع `safe` أو `delete_safe` أو `full_access`.
- الملف الثنائي موثق من Apple على macOS وموقع رقميًا باستخدام Authenticode على Windows.
- يتم نشر قيم sha256 المقفلة لكل إصدار.
- الاسم mcp-name هو `io.github.stackql/stackql-mcp` في Official MCP Registry.

راجع [مرجع أدوات MCP](/mcp) و[خيارات التثبيت](/installing-stackql).
