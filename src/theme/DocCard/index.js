/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import {
  useDocById,
  findFirstSidebarItemLink,
} from '@docusaurus/plugin-content-docs/client';
import {usePluralForm} from '@docusaurus/theme-common';
import isInternalUrl from '@docusaurus/isInternalUrl';
import {translate} from '@docusaurus/Translate';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

import useBaseUrl from '@docusaurus/useBaseUrl';

function useCategoryItemsPlural() {
  const {selectMessage} = usePluralForm();
  return (count) =>
    selectMessage(
      count,
      translate(
        {
          message: '1 item|{count} items',
          id: 'theme.docs.DocCard.categoryDescription.plurals',
          description:
            'The default description for a category card in the generated index about how many items this category includes',
        },
        {count},
      ),
    );
}
// `target` is only set for external links the card should keep in the same
// tab (the provider tiles on docs/providers.md); Link opens external hrefs
// in a new tab by default. `rel` drops `noreferrer` in that case so the
// microsite still sees this site as the referrer.
function CardContainer({className, href, target, children}) {
  return (
    <Link
      href={href}
      {...(target && {target, rel: 'noopener'})}
      className={clsx('card padding--lg', styles.cardContainer, className)}>
      {children}
    </Link>
  );
}
function CardLayout({className, href, target, icon, title, description}) {
  return (
    <CardContainer href={href} target={target} className={className}>
      <Heading
        as="h2"
        className={clsx('text--truncate', styles.cardTitle)}
        title={title}>
        {icon} {title}
      </Heading>
      {description && (
        <p
          className={clsx('text--truncate', styles.cardDescription)}
          title={description}>
          {description}
        </p>
      )}
    </CardContainer>
  );
}
/**
 * Resolve a card's icon from the sidebar item's customProps, falling back to
 * the theme default. Link items (the tiles on docs/providers.md, the homepage
 * cards) and category items (the per-provider Quick Starts groups in
 * sidebars.js) both support:
 *   iconComponent  a React node, e.g. a react-icons component
 *   icon           an image path under static/, e.g. '/img/providers/aws/favicon.ico'
 *   invertOnDark   with `icon`, invert the image in dark mode
 *   emoji          a string rendered in place of the default emoji
 * Link items additionally support:
 *   target         anchor target for an external href, e.g. '_self' to stay
 *                  in the same tab (the provider tiles); unset means the
 *                  Link default (new tab for external URLs)
 */
function useItemIcon(item, defaultIcon) {
  const cp = item?.customProps || {};
  // Hooks must run unconditionally, so resolve the path even when unused.
  const src = useBaseUrl(cp.icon || '');
  if (cp.iconComponent) return cp.iconComponent;
  if (cp.icon) {
    const className = clsx(styles.cardIcon, cp.invertOnDark && styles.cardIconInvert);
    return <img src={src} alt="" className={className} />;
  }
  if (cp.emoji) return <span className={styles.cardEmoji}>{cp.emoji}</span>;
  return defaultIcon;
}

function CardCategory({item}) {
  const href = findFirstSidebarItemLink(item);
  const categoryItemsPlural = useCategoryItemsPlural();
  const icon = useItemIcon(item, '🗃️');
  // Unexpected: categories that don't have a link have been filtered upfront
  if (!href) {
    return null;
  }
  return (
    <CardLayout
      className={item.className}
      href={href}
      icon={icon}
      title={item.label}
      description={item.description ?? categoryItemsPlural(item.items.length)}
    />
  );
}

function CardLink({item}) {
  const defaultIcon = isInternalUrl(item.href) ? '📄️' : '🔗';
  const icon = useItemIcon(item, defaultIcon);

  const doc = useDocById(item.docId ?? undefined);
  return (
    <CardLayout
      className={item.className}
      href={item.href}
      target={item.customProps?.target}
      icon={icon}
      title={item.label}
      description={item.description ?? doc?.description}
    />
  );
}
export default function DocCard({item}) {
  switch (item.type) {
    case 'link':
      return <CardLink item={item} />;
    case 'category':
      return <CardCategory item={item} />;
    default:
      throw new Error(`unknown item type ${JSON.stringify(item)}`);
  }
}
