/**
 * Make heading anchors inside <Tabs> reachable.
 *
 * Docusaurus keeps every inactive <TabItem> panel in the DOM with the `hidden`
 * attribute (unless the Tabs are `lazy`). The table of contents still lists the
 * headings inside those panels, but Docusaurus scrolls to a hash target with
 * element.scrollIntoView(), which is a no-op for a hidden element. So clicking
 * a TOC entry, or opening a deep link, for a heading inside a non-default tab
 * did nothing.
 *
 * This module finds the tab panel(s) hiding the target, activates the matching
 * tab(s) outermost first, then scrolls to the target once it is visible.
 *
 * Triggers:
 * - onRouteDidUpdate: hash changes, back/forward, links from other pages.
 * - click on a same-page hash link: covers clicking a TOC entry whose hash is
 *   already in the URL, which is not a navigation and fires no route update.
 * - initial page load: handled by src/theme/Root, which waits for the
 *   post-hydration remount of every <Tabs> (that remount resets their state).
 */
import ExecutionEnvironment from '@docusaurus/ExecutionEnvironment';

// Each attempt reveals one level of nesting, so this bounds the tab depth.
const MAX_ATTEMPTS = 6;

function hashToId(hash) {
  if (!hash || hash.length < 2) return null;
  try {
    return decodeURIComponent(hash.slice(1));
  } catch (e) {
    return null;
  }
}

// Outermost hidden tab panel above `el`. Everything above that panel is
// visible, so its tab list can be clicked without side effects.
function outermostHiddenPanel(el) {
  let found = null;
  let panel = el.closest('[role="tabpanel"][hidden]');
  while (panel) {
    found = panel;
    panel = panel.parentElement
      ? panel.parentElement.closest('[role="tabpanel"][hidden]')
      : null;
  }
  return found;
}

// Tabs and panels are rendered in the same order, so map them by index.
// `.tabs-container` is the stable public class on the Tabs wrapper.
function activatePanel(panel) {
  const container = panel.closest('.tabs-container');
  const tabList = container
    ? Array.from(container.children).find(
        (child) => child.getAttribute('role') === 'tablist',
      )
    : null;
  if (!tabList || !panel.parentElement) return false;
  const index = Array.from(panel.parentElement.children).indexOf(panel);
  const tab = tabList.children[index];
  if (!tab) return false;
  tab.click();
  return true;
}

export function revealTabAnchor(hash, attempt = 0) {
  const id = hashToId(hash);
  const target = id ? document.getElementById(id) : null;
  if (!target) return;
  const panel = outermostHiddenPanel(target);
  if (!panel) {
    // Nothing hidden on the first pass means the browser or Docusaurus already
    // scrolled. After a reveal we scroll ourselves. Tabs restores the clicked
    // tab's screen position in a microtask after re-rendering; a frame has
    // passed by now, so that cannot undo this scroll.
    if (attempt > 0) target.scrollIntoView();
    return;
  }
  if (attempt >= MAX_ATTEMPTS || !activatePanel(panel)) return;
  // Let React commit the tab change (and reveal any nested Tabs) first.
  window.requestAnimationFrame(() => revealTabAnchor(hash, attempt + 1));
}

export function onRouteDidUpdate({location, previousLocation}) {
  // Initial load is handled by src/theme/Root (see the header comment).
  if (!previousLocation) return;
  revealTabAnchor(location.hash);
}

if (ExecutionEnvironment.canUseDOM) {
  document.addEventListener('click', (event) => {
    if (
      event.defaultPrevented ||
      event.button !== 0 ||
      event.metaKey ||
      event.ctrlKey ||
      event.shiftKey ||
      event.altKey
    ) {
      return;
    }
    const anchor =
      event.target instanceof Element ? event.target.closest('a[href]') : null;
    if (!anchor || (anchor.target && anchor.target !== '_self')) return;
    let url;
    try {
      url = new URL(anchor.getAttribute('href'), window.location.href);
    } catch (e) {
      return;
    }
    if (
      !url.hash ||
      url.origin !== window.location.origin ||
      url.pathname !== window.location.pathname
    ) {
      return;
    }
    // Run after the browser's own fragment navigation.
    window.setTimeout(() => revealTabAnchor(url.hash), 0);
  });
}
