// Flags and short codes for the locale dropdown
// (src/theme/NavbarItem/LocaleDropdownNavbarItem). Hand-rolled inline SVGs,
// 4:3 like the flag-icons set, so no icon library and no emoji (Windows
// renders flag emoji as letters). A locale with no flag here shows its code
// in a small badge instead. Add an entry when a locale is added to
// i18nConfig in docusaurus.config.js.
import React from 'react';

// The code shown next to the flag. Not the BCP 47 tag: the short form a
// reader expects to see in a language switcher.
export const CODES = {
  en: 'EN',
  de: 'DE',
  fr: 'FR',
  'zh-Hans': 'CN',
  ja: 'JP',
  ko: 'KR',
  pl: 'PL',
};

export function codeFor(locale) {
  return CODES[locale] || locale.split('-')[0].toUpperCase();
}

// A five-pointed star as SVG polygon points.
function star(cx, cy, r, rotation = -90) {
  const points = [];
  for (let i = 0; i < 10; i++) {
    const radius = i % 2 === 0 ? r : r * 0.382;
    const angle = ((rotation + i * 36) * Math.PI) / 180;
    points.push(`${(cx + radius * Math.cos(angle)).toFixed(1)},${(cy + radius * Math.sin(angle)).toFixed(1)}`);
  }
  return points.join(' ');
}

const svgProps = (className) => ({
  className,
  viewBox: '0 0 640 480',
  xmlns: 'http://www.w3.org/2000/svg',
  'aria-hidden': 'true',
  focusable: 'false',
});

const FLAGS = {
  en: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#bd3d44" d="M0 0h640v480H0z" />
      <path
        stroke="#fff"
        strokeWidth="37"
        d="M0 55.3h640M0 129h640M0 203h640M0 277h640M0 351h640M0 425h640"
      />
      <path fill="#192f5d" d="M0 0h364.8v258.5H0z" />
      <g fill="#fff">
        {Array.from({length: 9}, (_, row) =>
          Array.from({length: row % 2 === 0 ? 6 : 5}, (__, col) => (
            <circle
              key={`${row}-${col}`}
              cx={30 + col * 61 + (row % 2 === 0 ? 0 : 30.5)}
              cy={27 + row * 26}
              r="9"
            />
          )),
        )}
      </g>
    </svg>
  ),
  de: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fc0" d="M0 320h640v160H0z" />
      <path fill="#000" d="M0 0h640v160H0z" />
      <path fill="#d00" d="M0 160h640v160H0z" />
    </svg>
  ),
  fr: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 0h640v480H0z" />
      <path fill="#000091" d="M0 0h213.3v480H0z" />
      <path fill="#e1000f" d="M426.7 0H640v480H426.7z" />
    </svg>
  ),
  'zh-Hans': (className) => (
    <svg {...svgProps(className)}>
      <path fill="#ee1c25" d="M0 0h640v480H0z" />
      <g fill="#ff0">
        <polygon points={star(120, 120, 72)} />
        <polygon points={star(240, 48, 24, -90 + 23)} />
        <polygon points={star(288, 96, 24, -90 + 45)} />
        <polygon points={star(288, 168, 24, -90 + 68)} />
        <polygon points={star(240, 216, 24, -90 + 90)} />
      </g>
    </svg>
  ),
  ja: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 0h640v480H0z" />
      <circle cx="320" cy="240" r="144" fill="#bc002d" />
    </svg>
  ),
  pl: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 0h640v240H0z" />
      <path fill="#dc143c" d="M0 240h640v240H0z" />
    </svg>
  ),
};

// The flag of a locale. `data-flag-locale` is what the regional variants
// hook onto: styles.module.css swaps the artwork when <html> carries
// data-flag-<locale> (set before first paint, see src/lib/regionalFlags.js).
export function Flag({locale, className}) {
  const render = FLAGS[locale];
  return render
    ? React.cloneElement(render(className), {'data-flag-locale': locale})
    : null;
}
