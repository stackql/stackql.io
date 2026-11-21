// Regional variants of a locale's flag in the language switcher: a visitor
// in Australia, New Zealand, South Africa or the United Kingdom sees that
// country's flag next to "EN" instead of the US one, and a visitor in a
// Spanish-speaking country of the Americas (or in Equatorial Guinea) sees
// that country's flag next to "ES" instead of Spain's, a visitor in an
// Arab League country other than Saudi Arabia sees that country's flag next
// to "AR", and a visitor in Taiwan or Macau sees that flag next to "ZH-HANT"
// instead of Hong Kong's. Cosmetic only: the locale, its URL and its content
// are the same for everyone.
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
//
// Spanish (es) works the same way with Spain as the default: es-MX decides
// wherever the machine is, otherwise a time zone of a Spanish-speaking
// country does, whatever the browser language (an en-US or es-ES browser in
// Bogota gets the Colombian flag, as an en-US browser in Sydney gets the
// Australian one). es-ES, es-419 (Latin America), es-US and a bare "es" name
// no country here, so outside those time zones they keep the flag of Spain.
// Mexico and Argentina have no common prefix, so every zone is listed;
// Chrome reports five Argentine zones under their pre-1993 names
// (America/Buenos_Aires), Firefox under America/Argentina/.
//
// Arabic (ar) has Saudi Arabia as the default and a variant for each other
// member of the Arab League. ar-SA is what Windows and most browsers report
// when plain "Arabic" is chosen, so like es-ES it names no country here:
// an ar-SA browser in Cairo gets the Egyptian flag. ar-EG, ar-MA and the
// other regions decide wherever the machine is. Each country has one zone
// (two for Palestine); browsers report the per-country CLDR names
// (Asia/Kuwait, not the IANA link target Asia/Riyadh).
//
// Traditional Chinese (zh-Hant) has Hong Kong as the default and variants
// for Taiwan and Macau. Its browser languages are zh-TW, zh-HK, zh-MO and
// the script-tagged zh-Hant-TW and zh-Hant-HK, so `lang` says which language
// subtag to match (the locale code itself never appears in a browser
// language) and the region may be any two-letter subtag of the tag. zh-HK
// and zh-MO decide wherever the machine is; HK maps to "", which settles on
// the default flag without consulting the time zone. zh-TW is the setting
// behind a plain "Traditional Chinese" on macOS and in Chrome, used in Hong
// Kong as well, so like en-GB it is a loose region: a zh-TW browser in Hong
// Kong keeps the Hong Kong flag (its time zone maps to ""), one in Taipei or
// with no Chinese time zone gets the Taiwan flag.
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
  es: {
    regions: {
      MX: 'mx', AR: 'ar', CO: 'co', CL: 'cl', PE: 'pe', VE: 've', EC: 'ec',
      GT: 'gt', CU: 'cu', BO: 'bo', DO: 'do', HN: 'hn', PY: 'py', SV: 'sv',
      NI: 'ni', CR: 'cr', PA: 'pa', UY: 'uy', PR: 'pr', GQ: 'gq',
    },
    timeZones: [
      ['America/Mexico_City', 'mx'],
      ['America/Monterrey', 'mx'],
      ['America/Merida', 'mx'],
      ['America/Cancun', 'mx'],
      ['America/Tijuana', 'mx'],
      ['America/Hermosillo', 'mx'],
      ['America/Mazatlan', 'mx'],
      ['America/Chihuahua', 'mx'],
      ['America/Ciudad_Juarez', 'mx'],
      ['America/Ojinaga', 'mx'],
      ['America/Matamoros', 'mx'],
      ['America/Bahia_Banderas', 'mx'],
      ['America/Argentina/', 'ar'],
      ['America/Buenos_Aires', 'ar'],
      ['America/Cordoba', 'ar'],
      ['America/Mendoza', 'ar'],
      ['America/Catamarca', 'ar'],
      ['America/Jujuy', 'ar'],
      ['America/Bogota', 'co'],
      ['America/Santiago', 'cl'],
      ['America/Punta_Arenas', 'cl'],
      ['America/Coyhaique', 'cl'],
      ['Pacific/Easter', 'cl'],
      ['America/Lima', 'pe'],
      ['America/Caracas', 've'],
      ['America/Guayaquil', 'ec'],
      ['Pacific/Galapagos', 'ec'],
      ['America/Guatemala', 'gt'],
      ['America/Havana', 'cu'],
      ['America/La_Paz', 'bo'],
      ['America/Santo_Domingo', 'do'],
      ['America/Tegucigalpa', 'hn'],
      ['America/Asuncion', 'py'],
      ['America/El_Salvador', 'sv'],
      ['America/Managua', 'ni'],
      ['America/Costa_Rica', 'cr'],
      ['America/Panama', 'pa'],
      ['America/Montevideo', 'uy'],
      ['America/Puerto_Rico', 'pr'],
      ['Africa/Malabo', 'gq'],
    ],
  },
  ar: {
    regions: {
      AE: 'ae', BH: 'bh', DZ: 'dz', DJ: 'dj', EG: 'eg', IQ: 'iq', JO: 'jo',
      KM: 'km', KW: 'kw', LB: 'lb', LY: 'ly', MA: 'ma', MR: 'mr', OM: 'om',
      PS: 'ps', QA: 'qa', SO: 'so', SD: 'sd', SY: 'sy', TN: 'tn', YE: 'ye',
    },
    timeZones: [
      ['Asia/Dubai', 'ae'],
      ['Asia/Bahrain', 'bh'],
      ['Africa/Algiers', 'dz'],
      ['Africa/Djibouti', 'dj'],
      ['Africa/Cairo', 'eg'],
      ['Asia/Baghdad', 'iq'],
      ['Asia/Amman', 'jo'],
      ['Indian/Comoro', 'km'],
      ['Asia/Kuwait', 'kw'],
      ['Asia/Beirut', 'lb'],
      ['Africa/Tripoli', 'ly'],
      ['Africa/Casablanca', 'ma'],
      ['Africa/Nouakchott', 'mr'],
      ['Asia/Muscat', 'om'],
      ['Asia/Gaza', 'ps'],
      ['Asia/Hebron', 'ps'],
      ['Asia/Qatar', 'qa'],
      ['Africa/Mogadishu', 'so'],
      ['Africa/Khartoum', 'sd'],
      ['Asia/Damascus', 'sy'],
      ['Africa/Tunis', 'tn'],
      ['Asia/Aden', 'ye'],
    ],
  },
  'zh-Hant': {
    lang: 'zh',
    regions: { HK: '', MO: 'mo' },
    timeZones: [
      ['Asia/Taipei', 'tw'],
      ['Asia/Macau', 'mo'],
      ['Asia/Macao', 'mo'],
      ['Asia/Hong_Kong', ''],
    ],
    looseRegions: { TW: 'tw' },
  },
};

// The inline script for <head>. Written for old browsers (no arrow
// functions, no optional chaining) and wrapped so that any error leaves the
// page exactly as it was served. g() returns the variant a browser language
// of the locale's language (`lang`, default the locale code) names through
// one of its two-letter subtags, or undefined; a variant of "" (and a time
// zone mapped to "") decides on the default flag, so the later signals are
// not consulted. The attribute is data-flag-<locale> in lower case
// (data-flag-zh-hant), which is what HTML makes of any attribute name.
function headScript(config = REGIONAL_FLAGS) {
  return `!function(){try{var c=${JSON.stringify(config)},d=document.documentElement,n=navigator,l=n.languages&&n.languages.length?n.languages:[n.language||""],z="",u;try{z=Intl.DateTimeFormat().resolvedOptions().timeZone||""}catch(e){}var g=function(k,m){for(var i=0,p,j,r,L=(c[k].lang||k).toLowerCase();m&&i<l.length;i++){p=String(l[i]).split("-");if(p[0].toLowerCase()!==L)continue;for(j=1;j<p.length;j++){r=p[j].toUpperCase();if(r.length===2&&m.hasOwnProperty(r))return m[r]}}return u};for(var k in c){var f=g(k,c[k].regions),t=c[k].timeZones||[],i;for(i=0;i<t.length&&f===u;i++)if(z.indexOf(t[i][0])===0)f=t[i][1];if(f===u)f=g(k,c[k].looseRegions);if(f)d.setAttribute("data-flag-"+k.toLowerCase(),f)}}catch(e){}}();`;
}

module.exports = { REGIONAL_FLAGS, headScript };
