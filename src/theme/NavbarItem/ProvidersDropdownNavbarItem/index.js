// Navbar item type `custom-providersDropdown`: the two-level "Providers"
// menu, built from the provider catalog (src/configs/providers.json, read
// by src/lib/providers.js). Level one is the catalog's categories, in
// catalog order, each linking to its section on /providers. Level two is
// the category's providers, each linking straight to its microsite
// (https://<slug>-provider.stackql.io/) in the same tab, with no redirect
// hop. Adding a provider or a category to the JSON is all it takes to
// extend the menu.
//
// Registered in ../ComponentTypes.js; used once, by the "Providers" entry
// in docusaurus.config.js, which passes `label`, `to` and `position`.
//
// Desktop: the Infima dropdown markup of theme-classic's
// DropdownNavbarItem/Desktop, with a flyout submenu per category that
// opens on hover or keyboard focus (CSS in styles.module.css). Mobile:
// the sidebar's collapsible list markup of DropdownNavbarItem/Mobile, one
// collapsible per category. Keep both in step with theme-classic when
// Docusaurus is upgraded.
import React, {useEffect, useRef, useState} from 'react';
import clsx from 'clsx';
import {Collapsible, useCollapsible} from '@docusaurus/theme-common';
import {isSamePath, useLocalPathname} from '@docusaurus/theme-common/internal';
import NavbarNavLink from '@theme/NavbarItem/NavbarNavLink';
import {PROVIDER_CATEGORIES} from '@site/src/lib/providers';
import styles from './styles.module.css';

const categoryHref = (category) => `/providers#${category.id}`;

const escapeHtml = (text) =>
  text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

// A provider row is an external link to the microsite. `html` rather than
// `label` keeps NavbarNavLink from adding the external-link icon (the
// microsites are part of the same product); `target` keeps it in this tab
// (Link opens external hrefs in a new one by default) and `rel` drops
// `noreferrer` so the microsite still sees this site as the referrer.
const providerLinkProps = (provider) => ({
  href: provider.href,
  html: escapeHtml(provider.name),
  target: '_self',
  rel: 'noopener',
});

// ---------------------------------------------------------------- desktop

function DesktopCategory({category}) {
  return (
    <li className={styles.category}>
      <NavbarNavLink
        className={clsx('dropdown__link', styles.categoryLink)}
        isDropdownLink
        to={categoryHref(category)}
        label={category.name}
        aria-haspopup="true"
      />
      <ul className={styles.submenu} aria-label={category.name}>
        {category.providers.map((provider) => (
          <li key={provider.slug}>
            <NavbarNavLink
              className="dropdown__link"
              isDropdownLink
              {...providerLinkProps(provider)}
            />
          </li>
        ))}
      </ul>
    </li>
  );
}

function ProvidersDropdownDesktop({position, className, onClick, ...props}) {
  const dropdownRef = useRef(null);
  const [showDropdown, setShowDropdown] = useState(false);
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!dropdownRef.current || dropdownRef.current.contains(event.target)) {
        return;
      }
      setShowDropdown(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    document.addEventListener('touchstart', handleClickOutside);
    document.addEventListener('focusin', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('touchstart', handleClickOutside);
      document.removeEventListener('focusin', handleClickOutside);
    };
  }, [dropdownRef]);
  return (
    <div
      ref={dropdownRef}
      className={clsx('navbar__item', 'dropdown', 'dropdown--hoverable', {
        'dropdown--right': position === 'right',
        'dropdown--show': showDropdown,
      })}>
      <NavbarNavLink
        aria-haspopup="true"
        aria-expanded={showDropdown}
        role="button"
        // # hash makes the <a> focusable when there is no link target
        href={props.to ? undefined : '#'}
        className={clsx('navbar__link', className)}
        {...props}
        onClick={props.to ? undefined : (e) => e.preventDefault()}
        onKeyDown={(e) => {
          if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            setShowDropdown(!showDropdown);
          }
        }}>
        {props.children ?? props.label}
      </NavbarNavLink>
      <ul className={clsx('dropdown__menu', styles.menu)}>
        {PROVIDER_CATEGORIES.map((category) => (
          <DesktopCategory key={category.id} category={category} />
        ))}
      </ul>
    </div>
  );
}

// ----------------------------------------------------------------- mobile

function CollapseButton({collapsed, onClick}) {
  return (
    <button
      aria-label={collapsed ? 'Expand the dropdown' : 'Collapse the dropdown'}
      aria-expanded={!collapsed}
      type="button"
      className="clean-btn menu__caret"
      onClick={onClick}
    />
  );
}

// One collapsible row of the mobile sidebar. With `to`, the label is a
// link that navigates and toggles (as a Docusaurus mobile dropdown does);
// without it, the label only toggles. `active` expands the row initially
// and highlights it after a navigation.
function MobileSublist({label, to, active, children}) {
  const {collapsed, toggleCollapsed, setCollapsed} = useCollapsible({
    initialState: () => !active,
  });
  useEffect(() => {
    if (active) {
      setCollapsed(false);
    }
  }, [active, setCollapsed]);
  const href = to ? undefined : '#';
  return (
    <li
      className={clsx('menu__list-item', {
        'menu__list-item--collapsed': collapsed,
      })}>
      <div
        className={clsx('menu__list-item-collapsible', {
          'menu__list-item-collapsible--active': active,
        })}>
        <NavbarNavLink
          role="button"
          className={clsx(
            'menu__link menu__link--sublist',
            styles.mobileSublistLink,
          )}
          href={href}
          to={to}
          label={label}
          onClick={(e) => {
            if (href === '#') {
              e.preventDefault();
            }
            toggleCollapsed();
          }}
        />
        <CollapseButton
          collapsed={collapsed}
          onClick={(e) => {
            e.preventDefault();
            toggleCollapsed();
          }}
        />
      </div>
      <Collapsible lazy as="ul" className="menu__list" collapsed={collapsed}>
        {children}
      </Collapsible>
    </li>
  );
}

function ProvidersDropdownMobile({position, className, onClick, ...props}) {
  const localPathname = useLocalPathname();
  // Only the catalog page itself can be the current page: the provider
  // rows leave the site, so no category is ever "active".
  const isActive = isSamePath(props.to, localPathname);
  return (
    <MobileSublist label={props.label} to={props.to} active={isActive}>
      {PROVIDER_CATEGORIES.map((category) => (
        <MobileSublist key={category.id} label={category.name} active={false}>
          {category.providers.map((provider) => (
            <li key={provider.slug} className="menu__list-item">
              <NavbarNavLink
                className="menu__link"
                {...providerLinkProps(provider)}
                onClick={onClick}
              />
            </li>
          ))}
        </MobileSublist>
      ))}
    </MobileSublist>
  );
}

export default function ProvidersDropdownNavbarItem({mobile = false, ...props}) {
  const Comp = mobile ? ProvidersDropdownMobile : ProvidersDropdownDesktop;
  return <Comp {...props} />;
}
