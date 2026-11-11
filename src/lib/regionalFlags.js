// Regional variants of a locale's flag in the language switcher: a visitor
// in Australia, New Zealand, South Africa or the United Kingdom sees that
// country's flag next to "EN" instead of the US one. Cosmetic only: the
// locale, its URL and its content are the same for everyone.
//
// How it works. The site is static, so every page is served with the default
// flag (inline SVG, in src/theme/NavbarItem/LocaleDropdownNavbarItem/flags.js).
// headScript() below is a few hundred bytes of inline script in <head> that
// runs before the first paint, picks a variant from what the browser says
// about itself, and sets <html data-flag-<locale>="<variant>">. The switcher's
// stylesheet (styles.module.css next to the component) then paints that
// variant's artwork over the default flag. Nothing is fetched, nothing is
// sent anywhere, and nothing moves on the page.
//
// It falls back to the default flag when the browser's region is not listed
// here, when the browser offers no language or time zone, when the script
// throws, and when a variant named here has no rule in the stylesheet.
//
// Adding a variant: one entry here (regions and, if the country has its own
// time zones, timeZones) and one pair of rules in the component's
// styles.module.css, which holds the artwork as an SVG data URI. Every byte
// here is in every page, so keep the lists short.
//
// CommonJS on purpose: docusaurus.config.js requires it.

const REGIONAL_FLAGS = {
  en: {
    // The region of a browser language (navigator.languages, "en-AU"), tried
    // first, in the browser's order of preference.
    regions: { AU: 'au', NZ: 'nz', ZA: 'za', GB: 'gb' },
    // IANA time zone prefixes, tried when no language decides. Many browsers
    // in these countries are left on en-US, and the time zone still says
    // where the machine is.
    timeZones: [
      ['Australia/', 'au'],
      ['Pacific/Auckland', 'nz'],
      ['Pacific/Chatham', 'nz'],
      ['Africa/Johannesburg', 'za'],
      ['Europe/London', 'gb'],
    ],
  },
};

// The inline script for <head>. Written for old browsers (no arrow
// functions, no optional chaining) and wrapped so that any error leaves the
// page exactly as it was served.
function headScript(config = REGIONAL_FLAGS) {
  return `!function(){try{var c=${JSON.stringify(config)},d=document.documentElement,n=navigator,l=n.languages&&n.languages.length?n.languages:[n.language||""],z="";try{z=Intl.DateTimeFormat().resolvedOptions().timeZone||""}catch(e){}for(var k in c){var f="",i,p;for(i=0;i<l.length&&!f;i++){p=String(l[i]).split("-");if(p[0].toLowerCase()===k&&p[1])f=c[k].regions[p[1].toUpperCase()]||""}for(i=0;i<c[k].timeZones.length&&!f;i++)if(z.indexOf(c[k].timeZones[i][0])===0)f=c[k].timeZones[i][1];if(f)d.setAttribute("data-flag-"+k,f)}}catch(e){}}();`;
}

module.exports = { REGIONAL_FLAGS, headScript };
