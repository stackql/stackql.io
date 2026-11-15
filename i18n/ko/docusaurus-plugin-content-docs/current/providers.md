---
title: 사용 가능한 프로바이더
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 프로바이더 카탈로그: SQL로 쿼리하고 관리할 수 있는 모든 클라우드 및 SaaS 프로바이더를 범주별로 분류하고 각 참조 사이트로 연결합니다."
image: "/img/stackql-featured-image.png"
---

import DocCardList from '@theme/DocCardList';
import Heading from '@theme/Heading';
import React from 'react';

{/* Provider catalog: src/configs/providers.json is the single source of truth for the
    tiles below, the navbar Providers dropdown and the /providers/<slug> and
    /registry/<name> 301s in netlify.toml. Add providers there, not here.
    Tiles and TOC entries link straight to each microsite (provider.href), in
    the same tab, rather than through a redirect. */}
import { PROVIDER_CATEGORIES } from '@site/src/lib/providers';

{/* Custom TOC Component - Now generated from data */}
export const CustomTOC = () => {
  return (
    <div className="table-of-contents table-of-contents__left-border">
      <ul className="toc-headings">
        {PROVIDER_CATEGORIES.map(category => (
          <li key={category.id}>
            <a href={`#${category.id}`}>{category.name}</a>
            <ul>
              {category.providers.map(provider => (
                <li key={provider.name}>
                  <a href={provider.href}>{provider.name}</a>
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

{/* Main content component using the data map */}
export const ProviderContent = () => {
  return (
    <>
      <blockquote>
        찾는 프로바이더가 없습니까? <a 
          href="https://github.com/stackql/stackql-provider-registry/issues/new?template=feature_request.md&title=%5BFEATURE%5D%20New%20Provider" 
          target="_blank" 
          rel="noopener noreferrer">문의하세요</a>
      </blockquote>

      {PROVIDER_CATEGORIES.map(category => (
        <div key={category.id}>
          {/* theme Heading, not a bare h2: it registers the id with the
              broken-anchor checker (the navbar Providers menu links to
              /providers#<category id> from every page) and offsets the
              anchor below the sticky navbar */}
          <Heading as="h2" id={category.id}>{category.name}</Heading>
          <DocCardList
            items={category.providers.map(provider => ({
              type: 'link',
              label: provider.name,
              href: provider.href,
              // target: the microsites are part of the same product, so open
              // them in this tab (DocCard defaults external links to _blank)
              customProps: { icon: provider.icon, invertOnDark: provider.invertOnDark, target: '_self' }
            }))}
          />
        </div>
      ))}
    </>
  );
};

<div className="row">
  <div className="col col--9">
    <ProviderContent />
  </div>
  <div className="col col--3">
    <CustomTOC />
  </div>
</div>
