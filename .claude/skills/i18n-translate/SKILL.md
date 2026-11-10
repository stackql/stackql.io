---
name: i18n-translate
description: Translate one stackql.io source page (docs/, ai-content/, blog/<section>/ or src/pages/) into a target locale under i18n/<locale>/, following the site's translation rules and i18n/glossary.md, then verify it with scripts/i18n/verify.js. Use when asked to translate a page, to update a translation after its English source changed, or to work through the yarn i18n:status backlog for a locale. Arguments: <source path> <locale>, e.g. docs/installing-stackql.md de.
---

# Translate a page

Arguments: `<source path> <locale>` (repo-relative source file, locale code as configured in `scripts/i18n/config.js`). With no arguments, run `yarn i18n:status` and work through the pending pages of the locale the user named.

## Where the translation goes

The target mirrors the source under `i18n/<locale>/`:

| Source | Target |
|---|---|
| `docs/<path>` | `i18n/<locale>/docusaurus-plugin-content-docs/current/<path>` |
| `ai-content/<path>` | `i18n/<locale>/docusaurus-plugin-content-docs-ai/current/<path>` |
| `blog/<section>/<path>` | `i18n/<locale>/docusaurus-plugin-content-blog-<section>/<path>` |
| `src/pages/<path>` | `i18n/<locale>/docusaurus-plugin-content-pages/<path>` |

Same file name, same extension. Only `.md` and `.mdx` files are translated; images and `_category_.yml` are shared.

## Steps

1. Read `i18n/glossary.md`: the "All locales" section and the section for the target locale. Use its terminology and style throughout.
2. Read the source file.
3. If the target file already exists, run `node scripts/i18n/diff.js --locale <locale> <source path>`:
   - A unified diff: read the existing translation and apply only those hunks to it, keeping its wording everywhere else. Do not retranslate the page.
   - "no recorded translation" (exit 3): the existing file was never verified; retranslate the whole page from the source.
   - "unchanged": the translation is current. Run the verify step and report; do not rewrite the file.
4. Otherwise translate the whole page.
5. Write the target file with LF line endings and one trailing newline.
6. Run `node scripts/i18n/verify.js --locale <locale> <source path>`. Fix every error it reports and run it again until it passes. A warning about inline code or length is worth a look but does not need to be cleared.
7. Do not run `yarn i18n:mark`; the caller marks verified files.
8. Report in one line: the file, whether it was created or updated from a diff, and the verify result.

## What to translate

- Prose: paragraphs, list items, table cells, admonition bodies, image alt text, link text (not link targets), JSX text content and the visible string props of components: `label` and `title` on tabs and cards, `text` on download and link buttons (`BinaryDownloadLink`, `DropDownBinaryDownloadLink`, `CodeLink` and the like), `alttext`/`alt` on images, and the `name`/`description` strings of data arrays that a page exports to render cards (keep every key, id and href).
- Headings: translate the text, keep the `{#id}` suffix exactly as in the source. Every heading in the source has one; the id is what links and the table of contents use.
- Front matter: `title`, `description`, `sidebar_label`, `keywords` (each keyword), `faq` questions and answers, `howTo` names and step text, `image` alt text if any. Keep `description` one sentence.
- Admonition titles given inline (`:::note[Title]`) and the `summary` of `<details>`.

## What stays exactly as it is

- Front matter keys and their order; the values of `id`, `slug`, `sidebar_position`, `hide_*`, `tags`, `date`, `authors`, `image`, `proficiencyLevel`, `dependencies`, `softwareApplication`, `draft`, `unlisted`, `last_update`.
- Fenced code blocks (including the info string and any `title=` meta), inline code, HTML comments including `<!-- truncate -->`.
- Import and export lines, JSX tags and attribute names and values (except the text props named above), MDX expressions in braces.
- Link targets and reference definitions, image paths, URLs, anchors, `pathname://` links.
- Admonition markers (`:::note`, `:::tip`, `:::`), tables' structure, horizontal rules, line structure of lists.
- Product, provider, command, flag, file and variable names (the glossary lists them).
- Heading ids, as above.

## Quality

- Technical-documentation register of the locale (the glossary says which); no marketing tone, no additions, no omissions, no summaries.
- Keep sentences as short as the source. Keep the same paragraph breaks.
- Where the source quotes command output or error text, leave it in English.
- A term with a fixed rendering in the glossary is rendered that way every time.
