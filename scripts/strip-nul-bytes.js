#!/usr/bin/env node
// Removes stray NUL bytes (U+0000) from the built HTML.
//
// The server-side render that Docusaurus uses to write each page (React 18's
// renderToPipeableStream read through a stream consumer, see
// node_modules/@docusaurus/core/lib/client/renderToHtml.js) leaves a NUL
// byte directly before a multi-byte UTF-8 character (Cyrillic, Arabic, CJK
// and so on) wherever that character falls at a boundary of its output
// buffer. The character itself is intact: the
// NUL is an extra byte, so deleting it restores the text. Browsers drop a NUL
// in body text but turn one inside an attribute (aria-label, title) into
// U+FFFD, and crawlers and LLM fetchers read the raw bytes. Before this step a
// build of every locale carried about 700 of them in some 450 pages; English
// pages are rarely hit, translated pages often. NUL is never valid in HTML,
// so removing every one is safe.
//
// Runs as the "postbuild" script, after `docusaurus build` and every plugin's
// postBuild hook have finished, so it never races a plugin that rewrites the
// same files. Usage: node scripts/strip-nul-bytes.js [build dir]

const fs = require('fs');
const path = require('path');

const root = path.resolve(process.argv[2] || 'build');
if (!fs.existsSync(root)) {
  console.error(`strip-nul-bytes: ${root} does not exist`);
  process.exit(1);
}

function* htmlFiles(dir) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, entry.name);
    if (entry.isDirectory()) yield* htmlFiles(p);
    else if (entry.name.endsWith('.html')) yield p;
  }
}

let files = 0;
let bytes = 0;
for (const file of htmlFiles(root)) {
  const buf = fs.readFileSync(file);
  if (!buf.includes(0)) continue;
  const out = Buffer.allocUnsafe(buf.length);
  let n = 0;
  for (let i = 0; i < buf.length; i++) {
    if (buf[i] !== 0) out[n++] = buf[i];
  }
  fs.writeFileSync(file, out.subarray(0, n));
  files += 1;
  bytes += buf.length - n;
}
console.log(`strip-nul-bytes: removed ${bytes} NUL byte(s) from ${files} HTML file(s) under ${path.relative(process.cwd(), root) || '.'}`);
