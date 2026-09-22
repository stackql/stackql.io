import React, {useEffect} from 'react';
import useIsBrowser from '@docusaurus/useIsBrowser';
import {revealTabAnchor} from '../../clientModules/revealTabAnchors';

/**
 * Site-wide wrapper. Its one job is the initial-load half of the
 * "anchors inside <Tabs>" fix (see src/clientModules/revealTabAnchors.js).
 *
 * Docusaurus remounts every <Tabs> once hydration completes (they are keyed on
 * useIsBrowser), which resets the selected tab. The client module's
 * onRouteDidUpdate fires before that remount on initial load, so a deep link
 * to a heading inside a non-default tab is handled here, after the remount.
 */
export default function Root({children}) {
  const isBrowser = useIsBrowser();
  useEffect(() => {
    if (isBrowser) {
      revealTabAnchor(window.location.hash);
    }
  }, [isBrowser]);
  return <>{children}</>;
}
