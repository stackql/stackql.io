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

## Publishing model

- `build_id` in `manifest.json` is a content hash of the compiled library, not the
  site build id. It changes when and only when library content changes, and MCP
  servers use it as their cache key.
- The site serves the artifacts at `https://stackql.io/docs/query-library/`; the
  raw fallback is
  `https://raw.githubusercontent.com/stackql/stackql.io/main/static/docs/query-library/`.
  Both paths are contract surfaces for deployed MCP servers. Do not move them.
