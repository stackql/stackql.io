// Registers the site's custom navbar item types on top of theme-classic's.
// A navbar entry in docusaurus.config.js with `type: 'custom-<name>'` is
// rendered by the component mapped here (Docusaurus validates nothing else
// about such entries, so the component owns its own props).
import ComponentTypes from '@theme-original/NavbarItem/ComponentTypes';
import ProvidersDropdownNavbarItem from './ProvidersDropdownNavbarItem';

export default {
  ...ComponentTypes,
  'custom-providersDropdown': ProvidersDropdownNavbarItem,
};
