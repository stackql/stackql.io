// Flags and language codes for the locale dropdown
// (src/theme/NavbarItem/LocaleDropdownNavbarItem). Hand-rolled inline SVGs,
// 4:3 like the flag-icons set, so no icon library and no emoji (Windows
// renders flag emoji as letters). A locale with no flag here shows its code
// in a small badge instead. Add a flag when a locale is added to i18nConfig
// in docusaurus.config.js.
import React from 'react';

// The code shown next to the flag: the language subtag of the locale in
// upper case (en -> EN, ar -> AR, id -> ID, ja -> JA, ko -> KO, pt-BR -> PT, vi -> VI, zh-Hans -> ZH), never a country
// code. The flag already stands for a country, and the regional flag
// variants (src/lib/regionalFlags.js) can change it, while the language
// stays the same. If two locales ever share a language (zh-Hans and
// zh-Hant), give them distinct codes here.
export function codeFor(locale) {
  return locale.split('-')[0].toUpperCase();
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

// South Korea: the angle of this 4:3 box's diagonals, on which the taeguk's
// axis and the four trigrams sit (the flag itself is 3:2; flag-icons fits it
// to 4:3 the same way).
const KR_DIAGONAL = ((Math.atan2(3, 4) * 180) / Math.PI).toFixed(2);

// One trigram as a path, pointing along +x: three bars from the inside out,
// `true` for a solid bar and `false` for a broken one. Official proportions
// for a 480-unit height: bars 120 long and 20 thick with 10 between them,
// the inner bar 180 from the centre (a quarter of the taeguk's diameter
// outside its edge).
function trigram(bars) {
  return bars
    .map((solid, i) => {
      const x = 180 + i * 30;
      return solid ? `M${x} -60h20v120h-20z` : `M${x} -60h20v55h-20zM${x} 5h20v55h-20z`;
    })
    .join('');
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
  // Saudi Arabia, the default for Arabic (a visitor in another Arab League
  // country sees that country's flag, src/lib/regionalFlags.js). The Shahada
  // and the sword are reduced to white strokes that read at 20 by 15 pixels;
  // the calligraphy itself cannot be drawn at that size.
  ar: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#006c35" d="M0 0h640v480H0z" />
      <path
        fill="#fff"
        d="M126 250h388v20H126zM144 124h18v126h-18zM188 156h18v94h-18zM232 112h18v138h-18zM274 170h18v80h-18zM318 128h18v122h-18zM360 156h18v94h-18zM404 112h18v138h-18zM446 166h18v84h-18zM488 132h18v118h-18zM116 350l28-16h332v32H144zM476 318h18v64h-18zM494 340h44v20h-44z"
      />
      <path fill="none" stroke="#fff" strokeWidth="16" d="M170 272q30 36 60 0M340 272q30 36 60 0" />
      <circle cx="210" cy="96" r="11" fill="#fff" />
      <circle cx="340" cy="92" r="11" fill="#fff" />
      <circle cx="466" cy="100" r="11" fill="#fff" />
    </svg>
  ),
  de: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fc0" d="M0 320h640v160H0z" />
      <path fill="#000" d="M0 0h640v160H0z" />
      <path fill="#d00" d="M0 160h640v160H0z" />
    </svg>
  ),
  // Spain, with the coat of arms reduced to a shield: at 20 by 15 pixels it
  // is two pixels wide. A visitor in a Spanish-speaking country of the
  // Americas sees that country's flag instead (src/lib/regionalFlags.js).
  es: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#aa151b" d="M0 0h640v480H0z" />
      <path fill="#f1bf00" d="M0 120h640v240H0z" />
      <path fill="#aa151b" d="M172 172h64v20h-64zm-8 32h80v52a40 40 0 0 1-80 0z" />
    </svg>
  ),
  fr: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 0h640v480H0z" />
      <path fill="#000091" d="M0 0h213.3v480H0z" />
      <path fill="#e1000f" d="M426.7 0H640v480H426.7z" />
    </svg>
  ),
  // Indonesia: red over white, the halves equal (the flag is 3:2; the 4:3
  // box keeps the proportion of the bands).
  id: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 240h640v240H0z" />
      <path fill="#e70011" d="M0 0h640v240H0z" />
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
  // Taeguk: red above the top-left to bottom-right diagonal, blue below,
  // the diameter half the height. Trigrams: geon (three solid) top left,
  // gam top right, ri bottom left, gon (three broken) bottom right.
  ko: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#fff" d="M0 0h640v480H0z" />
      <g transform={`translate(320 240) rotate(${KR_DIAGONAL})`}>
        <circle r="120" fill="#0047a0" />
        <path fill="#cd2e3a" d="M-120 0A120 120 0 0 1 120 0A60 60 0 0 0 0 0A60 60 0 0 1-120 0z" />
      </g>
      <g fill="#000" transform="translate(320 240)">
        <path transform={`rotate(${180 + Number(KR_DIAGONAL)})`} d={trigram([true, true, true])} />
        <path transform={`rotate(${-KR_DIAGONAL})`} d={trigram([false, true, false])} />
        <path transform={`rotate(${180 - KR_DIAGONAL})`} d={trigram([true, false, true])} />
        <path transform={`rotate(${KR_DIAGONAL})`} d={trigram([false, false, false])} />
      </g>
    </svg>
  ),
  // Brazil: the rhombus 1.7 of 20 units from the sides and 1.7 of 14 from
  // top and bottom (the flag is 10:7; flag-icons fits it to 4:3 the same
  // way), the globe kept round with a radius of a quarter of the height.
  // The band is the part of the globe between two arcs of radius 8 and 8.5
  // units centred 2 units left of the globe's vertical axis on the bottom
  // edge (the 1971 construction), drawn as a path so no clipPath id is
  // needed. The stars and the motto are left out: at 20 by 15 pixels they
  // are below a pixel.
  'pt-BR': (className) => (
    <svg {...svgProps(className)}>
      <path fill="#009c3b" d="M0 0h640v480H0z" />
      <path fill="#ffdf00" d="M54.4 240 320 58.3 585.6 240 320 421.7z" />
      <circle cx="320" cy="240" r="120" fill="#002776" />
      <path
        fill="#fff"
        d="M210.25 191.5A291.43 291.43 0 0 1 438.81 256.8A120 120 0 0 1 434.52 275.78A274.29 274.29 0 0 0 203.86 209.88A120 120 0 0 1 210.25 191.5z"
      />
    </svg>
  ),
  // Vietnam: a yellow star on red, centred, its points on a circle whose
  // radius is a fifth of the flag's length (the flag is 3:2, so 144 for a
  // height of 480; the 4:3 box keeps the star's size relative to the
  // height).
  vi: (className) => (
    <svg {...svgProps(className)}>
      <path fill="#da251d" d="M0 0h640v480H0z" />
      <polygon fill="#ff0" points={star(320, 240, 144)} />
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
