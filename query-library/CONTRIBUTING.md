# Contributing to the StackQL Query Library

The query library is a set of curated, parameterized StackQL queries published from
this repo and consumed by the stackql MCP server's `query_library_search` and
`query_library_get` tools, and by humans at
[stackql.io/docs/query-library](https://stackql.io/docs/query-library).

Each entry is one Markdown file with YAML front matter under `queries/`. A build
step compiles all entries into the artifacts the MCP server fetches (`index.json`,
`manifest.json`, per-query `.json` and `.md` files) under
`static/docs/query-library/`. Generated artifacts are committed so the raw GitHub
fallback tier works without a site deploy.

## Entry ids

The id of an entry is its path under `queries/` without the `.md` extension, and it
must have exactly three segments: `provider/service/slug`, for example
`aws/ec2/regions-enabled`. Segments are lowercase `a-z0-9_-`.

Ids are permanent. They are cached by MCP servers and referenced by `related`
lists. To rename an entry, add the new file and flip the old one to
`status: deprecated` with a note pointing at the replacement. Never delete or move
a published id.

## Authoring an entry

1. Copy `templates/query-template.md` to `queries/<provider>/<service>/<slug>.md`.
2. Fill in the front matter. `schema/front-matter.schema.json` is the source of
   truth for every field; the main rules are:
   - `format: md` is mandatory (it keeps braces in prose from breaking the site build).
   - `verb` routes execution: `select`, `mutation` or `lifecycle`.
   - `intent_keywords` is the primary retrieval field. Write them exactly as users
     phrase asks ("list all s3 buckets", "is my bucket public").
   - Every `{{placeholder}}` in the template must be declared in `params`, and
     every declared param must appear in the template.
   - Params without a `default` need an `example`; CI uses it to render the
     template for the parse check.
   - `permissions` (optional) lists the provider-native authorization actions
     the template's wire calls require - distinct from `auth`, which names the
     credential env vars for identity. Use the provider's own IAM syntax
     exactly: AWS `service:Action`, Azure `Microsoft.<RP>/<type>/<verb>`, GCP
     `service.resource.verb`. Declare only what the calls actually need (this
     feeds least-privilege policy generation, so over-declaration defeats its
     purpose), and when unsure omit the field entirely - a wrong permission
     list is worse than none, because agents relay it verbatim to operators as
     403 remediation guidance. AWS actions usually mirror the underlying API
     operation (ListUsers -> `iam:ListUsers`); entries that go through Cloud
     Control need both the `cloudcontrol:*` action and the underlying service
     actions.
   - Do not add an `id` key. The id is derived from the file path (`id` is a
     reserved Docusaurus front matter key).
3. Write the body:
   - one intro paragraph
   - a `## Query` section whose first ` ```sql ` fence is the template
   - an optional `## Notes` section (compact prose; it becomes the `notes` field
     in the emitted JSON)
4. New entries start as `status: draft`. Flip to `stable` once the query has been
   executed against a live provider (record the date in `last_verified`).

## Validating and building

```bash
pip install -r query-library/scripts/requirements.txt

# structural validation (schema, placeholder parity, ids, related refs)
python query-library/scripts/validate.py

# regenerate the published artifacts (run before committing)
python query-library/scripts/build-artifacts.py
```

Commit the regenerated files under `static/docs/query-library/` together with your
entry. CI fails the PR if the committed artifacts do not match the sources.

## What CI checks

Per PR:

- schema validation and placeholder/param parity in both directions
- unique ids and titles, `related` ids resolve
- template render check with the stackql binary (no execution)
- committed artifacts under `static/docs/query-library/` are up to date

Nightly:

- read-only (`verb: select`) stable entries are executed against sandbox
  credentials; `last_verified` is updated on success
- failing entries are flipped to `status: draft` and an issue is opened
- aspirational: run the read-only subset under a role built from each entry's
  declared `permissions` rather than a broad read-only role, turning the
  declaration into a tested claim (a 403 then means the declaration is
  incomplete and flags the entry, same as a functional failure)

## Human surface

The rendered site generates its browse pages from the built artifacts, so a
new provider or entry appears with no extra step once artifacts are rebuilt
(`build-artifacts.py` also writes the per-provider `.mdx` stubs - commit them
with the artifacts; the CI freshness gate checks both):

- `/docs/query-library` - landing page with one card per provider, driven by
  the generated `providers.json`
- `/docs/query-library/<provider>` - entry table per provider (generated
  `<provider>.mdx` stub in this directory)
- `/docs/query-library/queries/<id>` - the rendered entry, with a metadata
  panel (verb, providers, credentials, cost warning, last_verified) and
  related-entry links driven entirely by front matter

All three levels share the query library sidebar, generated per provider
directory by `sidebars-query-library.js`.

For a proper display name and blurb on a brand-new provider's card, add it to
`src/configs/providers-data.json`; until then a capitalized fallback renders.
Logos resolve favicon-first from `static/img/providers/<provider>/`
(`favicon.svg`, `favicon.png`, `favicon.ico`, then `<provider>.png`); drop a
favicon file there to change the mark. `_account`/`_workspace` suffixed
providers fall back to the base brand directory.

## Publishing model

- `build_id` in `manifest.json` is a content hash of the compiled library, not the
  site build id. It changes when and only when library content changes, and MCP
  servers use it as their cache key.
- The site serves the artifacts at `https://stackql.io/docs/query-library/`; the
  raw fallback is
  `https://raw.githubusercontent.com/stackql/stackql.io/main/static/docs/query-library/`.
  Both paths are contract surfaces for deployed MCP servers. Do not move them.
