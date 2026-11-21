// Order of the language switcher menu (the locale dropdown in the navbar,
// src/theme/NavbarItem/LocaleDropdownNavbarItem). Loosely based on the
// GitHub Octoverse ranking of developer populations by country. Config, not
// code: reorder or extend this list, nothing else.
//
// The menu lists the page's locale first, then English when the page is in
// another language, then every other locale in the order below. English is
// therefore first on an English page and second on every other page.
//
// The list also holds the locales that are planned but not built yet, so a
// new locale lands in its place without touching the order. A locale that
// is not in i18nConfig.locales (docusaurus.config.js) is skipped; a locale
// that is built but missing here fails the config load.
//
// CommonJS on purpose: docusaurus.config.js and scripts/seo-aeo require it.
const LOCALE_MENU_ORDER = [
  'en', //      EN       English
  'zh-Hans', // ZH       简体中文
  'pt-BR', //   PT       Português (Brasil)
  'es', //      ES       Español
  'ja', //      JA       日本語
  'de', //      DE       Deutsch
  'id', //      ID       Bahasa Indonesia
  'ru', //      RU       Русский (planned)
  'fr', //      FR       Français
  'ko', //      KO       한국어
  'zh-Hant', // ZH-HANT  繁體中文
  'uk', //      UK       Українська
  'vi', //      VI       Tiếng Việt
  'hi', //      HI       हिन्दी (planned)
  'ar', //      AR       العربية
];

// The built locales in menu order for a page in currentLocale.
function orderLocales(locales, currentLocale, defaultLocale = 'en') {
  const first = [currentLocale];
  if (currentLocale !== defaultLocale && locales.includes(defaultLocale)) first.push(defaultLocale);
  const rest = LOCALE_MENU_ORDER.filter((locale) => locales.includes(locale) && !first.includes(locale));
  return [...first, ...rest];
}

// The built locales that have no place in LOCALE_MENU_ORDER.
function missingFromMenuOrder(locales) {
  return locales.filter((locale) => !LOCALE_MENU_ORDER.includes(locale));
}

module.exports = { LOCALE_MENU_ORDER, orderLocales, missingFromMenuOrder };
