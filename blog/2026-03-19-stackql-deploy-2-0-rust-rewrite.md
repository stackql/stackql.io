---
slug: stackql-deploy-2-0-rust-rewrite
title: stackql-deploy 2.0 - Rewritten in Rust
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-deploy-rust-featured-image.png"
description: stackql-deploy 2.0 is a full rewrite in Rust - single static binary, no Python runtime, embedded StackQL server, and same CLI interface.
keywords: [stackql, stackql-deploy, rust, infrastructure-as-code, cloud, iac]
tags: [stackql, stackql-deploy, rust, infrastructure-as-code]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import { Box } from '@mui/material';

`stackql-deploy` 2.0 is a full rewrite in Rust. The Python package (`stackql-deploy` on PyPi) is archived at `1.9.4`. CLI interface and stack file format are unchanged - no migration required.

## Why Rust

The move to Rust was primarily about distribution and operational simplicity. Rust also brings stronger guarantees around performance and memory safety. Running everything in-process without Foreign Function Interface (FFI) boundaries simplifies the architecture while maintaining predictable resource usage.

## Embedded Postgres Wire Protocol Server

The most significant functional change in 2.0 is that `stackql-deploy` now runs the StackQL engine as an embedded in-process server over a local postgres wire protocol connection rather than shelling out to the StackQL binary as an external process.

There is nothing to start, stop, or configure. The server is lifecycle-managed by `stackql-deploy` itself and binds to `localhost` only - no port is exposed on the network, no inbound firewall rules needed in CI.

The previous model spawned a new StackQL process per operation. The embedded server keeps a persistent connection for the duration of a deployment run. For stacks with many resources, the reduction in process spawn overhead is noticeable - particularly on Windows where process creation is expensive.

## Additional Features Added

In addition to added the architectural change to use the embedded server, several other workflow improvements were added including:

- **Enabling resource scoped variable exports in `/*+ exists */` queries**: When an exists query returns a named field (e.g. `vpc_id`) instead of `count`, the value is captured as a resource-scoped variable (`this.vpc_id`) and made available to all subsequent queries for that resource (e.g. statecheck, exports). This eliminates the need for redundant lookups to resolve resource identifiers between query stages.
- **Support for capturing `RETURNING` payloads from DML operations**: `INSERT`, `UPDATE`, and `DELETE` statements can include a `RETURNING` clause. Fields from the response can be mapped to resource-scoped variables via `return_vals` in the manifest, keyed by operation (`create`, `update`, `delete`). This allows identifiers assigned by the provider during creation to be used immediately without a round-trip query.
- **Additional template filters**: Including the `to_aws_tag_filters` filter, which converts `global_tags` to the AWS Resource Groups Tagging API `TagFilters` format, and type-preserving YAML-to-JSON serialization that maintains string types through the rendering pipeline.
- **Improved logging and exception handling**: Enhanced visibility simplifying troubleshooting.

## Installation

<Tabs
  defaultValue="linux-macos"
  values={[
    { label: 'Linux / macOS', value: 'linux-macos' },
    { label: 'Windows', value: 'windows' },
    { label: 'GitHub Releases', value: 'github-releases' },
    { label: 'GitHub Actions', value: 'github-actions' },
    { label: 'Cargo', value: 'cargo' },
  ]}
>
<TabItem value="linux-macos">

<Box sx={{ border: '1px solid #e0e0e0', borderRadius: '4px', p: 3, mb: 2 }}>

The canonical install URL detects your OS and redirects to the latest release asset automatically.  You can also download directly from your browser at [__get-stackql-deploy.io__](https://get-stackql-deploy.io).
```bash
curl -L https://get-stackql-deploy.io | tar xzf -
```

</Box>

</TabItem>
<TabItem value="windows">

<Box sx={{ border: '1px solid #e0e0e0', borderRadius: '4px', p: 3, mb: 2 }}>

Use the PowerShell script below or download directly from your browser at [__get-stackql-deploy.io__](https://get-stackql-deploy.io).

```powershell
Invoke-WebRequest https://get-stackql-deploy.io -OutFile stackql-deploy.zip
Expand-Archive stackql-deploy.zip -DestinationPath .
```

</Box>

</TabItem>
<TabItem value="github-releases">

<Box sx={{ border: '1px solid #e0e0e0', borderRadius: '4px', p: 3, mb: 2 }}>

Pre-built binaries are attached to every release on the [releases page](https://github.com/stackql/stackql-deploy/releases). A `SHA256SUMS` file is included for verification.

| Platform | Asset |
|----------|-------|
| Linux x86_64 | `stackql-deploy-linux-x86_64.tar.gz` |
| Linux arm64 | `stackql-deploy-linux-arm64.tar.gz` |
| macOS Universal (Apple Silicon + Intel) | `stackql-deploy-macos-universal.tar.gz` |
| Windows x86_64 | `stackql-deploy-windows-x86_64.zip` |

</Box>

</TabItem>
<TabItem value="github-actions">

<Box sx={{ border: '1px solid #e0e0e0', borderRadius: '4px', p: 3, mb: 2 }}>

Use the [`stackql/stackql-deploy-action`](https://github.com/marketplace/actions/stackql-deploy) to run `build` or `test` commands in your CI pipeline. Provider credentials are passed via environment variables sourced from GitHub Actions Secrets.

**Deploy a stack:**
```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    env:
      GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }}
    steps:
      - uses: actions/checkout@v4
      - uses: stackql/stackql-deploy-action@v2
        with:
          command: 'build'
          stack_dir: 'examples/my-stack'
          stack_env: 'prod'
          env_vars: 'GOOGLE_PROJECT=my-project'
```

**Deploy and capture outputs:**
```yaml
      - name: Deploy Stack
        id: stackql-deploy
        uses: stackql/stackql-deploy-action@v2
        with:
          command: 'build'
          stack_dir: 'examples/my-stack'
          stack_env: 'prod'
          output_file: 'deployment-outputs.json'
          env_vars: 'GOOGLE_PROJECT=my-project'

      - name: Use outputs
        run: |
          echo '${{ steps.stackql-deploy.outputs.deployment_outputs }}' | jq .
```

See the [__stackql-deploy-action repo__](https://github.com/stackql/stackql-deploy-action) or the [__GitHub Action Marketplace__](https://github.com/marketplace/actions/stackql-deploy) for the full input/output reference.

</Box>

</TabItem>
<TabItem value="cargo">

<Box sx={{ border: '1px solid #e0e0e0', borderRadius: '4px', p: 3, mb: 2 }}>

If you have the Rust toolchain installed (`rustup` is the recommended way to get it on any platform), `cargo install` builds and installs the binary directly from [crates.io](https://crates.io/crates/stackql-deploy).
```bash
cargo install stackql-deploy
```

</Box>

</TabItem>
</Tabs>

## Usage

The CLI interface is unchanged from the Python version:

```bash
# deploy a stack
stackql-deploy build my-stack prod \
  --e GOOGLE_PROJECT=${GOOGLE_PROJECT}

# test a stack
stackql-deploy test my-stack prod \
  --e GOOGLE_PROJECT=${GOOGLE_PROJECT}

# tear down a stack
stackql-deploy teardown my-stack prod \
  --e GOOGLE_PROJECT=${GOOGLE_PROJECT}

# dry run
stackql-deploy build my-stack prod \
  --e GOOGLE_PROJECT=${GOOGLE_PROJECT} \
  --dry-run
```

Stack files and `stackql_manifest.yml` structure are unaffected - no migration work needed.

## Python Package Deprecation

`stackql-deploy 1.9.4` on PyPi is the final Python release. The Python source repository is archived. If you have `pip install stackql-deploy` in any scripts or CI pipelines, replace it with one of the install methods above. The `1.9.4` package remains on PyPi and installable, but will not receive updates.

## Links

- GitHub: [github.com/stackql/stackql-deploy](https://github.com/stackql/stackql-deploy)
- crates.io: [crates.io/crates/stackql-deploy](https://crates.io/crates/stackql-deploy)
- StackQL docs: [stackql.io/docs](https://stackql.io/docs)
