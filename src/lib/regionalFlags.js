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

// The two signals are combined, in this order:
//   1. regions       a browser language (navigator.languages, "en-AU") that
//                    names one of these regions decides, wherever the
//                    machine is. Nobody sets en-AU by accident, so an en-AU
//                    browser in London still gets the Australian flag.
//   2. timeZones     otherwise the time zone, which says where the machine
//                    is. Browsers in these countries are very often left on
//                    en-US or en-GB.
//   3. looseRegions  otherwise a language that names one of these regions.
//                    en-GB is the setting much of the English-speaking world
//                    outside the US runs on, so it only counts when the time
//                    zone says nothing: a browser in Melbourne set to en-GB
//                    is in Australia (it showed the UK flag while en-GB
//                    decided on its own), one in New York set to en-GB gets
//                    the UK flag.
// Time zones are matched by IANA name prefix, not by UTC offset: an offset
// such as +11 is shared with other countries and moves with daylight saving.
const REGIONAL_FLAGS = {
  en: {
    regions: { AU: 'au', NZ: 'nz', ZA: 'za' },
    timeZones: [
      ['Australia/', 'au'],
      ['Pacific/Auckland', 'nz'],
      ['Pacific/Chatham', 'nz'],
      ['Africa/Johannesburg', 'za'],
      ['Europe/London', 'gb'],
    ],
    looseRegions: { GB: 'gb' },
  },
};

// The inline script for <head>. Written for old browsers (no arrow
// functions, no optional chaining) and wrapped so that any error leaves the
// page exactly as it was served.
function headScript(config = REGIONAL_FLAGS) {
  return `!function(){try{var c=${JSON.stringify(config)},d=document.documentElement,n=navigator,l=n.languages&&n.languages.length?n.languages:[n.language||""],z="";try{z=Intl.DateTimeFormat().resolvedOptions().timeZone||""}catch(e){}var g=function(k,m){for(var i=0,p;m&&i<l.length;i++){p=String(l[i]).split("-");if(p[0].toLowerCase()===k&&p[1]&&m[p[1].toUpperCase()])return m[p[1].toUpperCase()]}return""};for(var k in c){var f=g(k,c[k].regions),t=c[k].timeZones||[],i;for(i=0;i<t.length&&!f;i++)if(z.indexOf(t[i][0])===0)f=t[i][1];f=f||g(k,c[k].looseRegions);if(f)d.setAttribute("data-flag-"+k,f)}}catch(e){}}();`;
}

module.exports = { REGIONAL_FLAGS, headScript };
