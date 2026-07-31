[![Netlify Status](https://api.netlify.com/api/v1/badges/ad26d902-9cb1-43be-90d9-284e8c7ac687/deploy-status)](https://app.netlify.com/sites/stackql-io/deploys)

# Website

This website is built using [Docusaurus 2](https://docusaurus.io/), a modern static website generator.

### Installation

```
yarn
```

### Local Development

```
yarn start
```

This command starts a local development server and opens up a browser window. Most changes are reflected live without having to restart the server.

### Build

```
export ALGOLIA_API_KEY="<your algolia api key>"
export ALGOLIA_APP_ID="<your algolia app id>"
export ALGOLIA_INDEX_NAME="<your algolia index name>"
export ALGOLIA_AGENTID="<your algolia agentid>"
export HUBSPOT_ACCT_ID="<your hubspot account id>"
export HUBSPOT_FORM_ID="<your hubspot form id>"
export HUBSPOT_REGION="<your hubspot region>"
export MAPS_API_KEY="<your google maps api key>"
export SMARTLOOK_PROJECT_KEY="<your smartlook project key>"
yarn build
```

or PowerShell:  

```
$env:ALGOLIA_API_KEY="<your algolia api key>"
$env:ALGOLIA_APP_ID="<your algolia app id>"
$env:ALGOLIA_INDEX_NAME="<your algolia index name>"
$env:ALGOLIA_AGENTID="<your algolia agent id>"
$env:HUBSPOT_ACCT_ID="<your hubspot account id>"
$env:HUBSPOT_FORM_ID="<your hubspot form id>"
$env:HUBSPOT_REGION="<your hubspot region>"
$env:MAPS_API_KEY="<your google maps api key>"
$env:SMARTLOOK_PROJECT_KEY="<your smartlook project key>"
yarn build
```

This command generates static content into the `build` directory and can be served using any static contents hosting service.

### Building the AI surface locally

The AI/machine surface (`.md` companions, `llms.txt`, JSON-LD) is only produced by a full production build - `yarn start` skips all of it because the AEO plugins run in `postBuild`. Run a full production build and serve it; Algolia values can be dummies for local testing:

```
ALGOLIA_APP_ID=dummy ALGOLIA_API_KEY=dummy ALGOLIA_INDEX_NAME=dummy yarn build
yarn serve
```

or PowerShell:

```
$env:ALGOLIA_APP_ID="dummy"; $env:ALGOLIA_API_KEY="dummy"; $env:ALGOLIA_INDEX_NAME="dummy"
yarn build
yarn serve
```

The served site at `http://localhost:3000` now includes `/llms.txt` and the `.md` twin for every doc and blog page.

The StackQL query library lives in its own repo (query-library.stackql.io) and is served under the canonical path `https://stackql.io/docs/query-library/*` via a Netlify 200 proxy rewrite (see `netlify.toml`). The path 404s when serving this site locally - that is expected, the rewrite only exists on Netlify.

### Deployment

```
$ GIT_USER=<Your GitHub username> USE_SSH=true yarn deploy
```

If you are using GitHub pages for hosting, this command is a convenient way to build the website and push to the `gh-pages` branch.
