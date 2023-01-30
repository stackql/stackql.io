---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

Command used to launch StackQL as a service, which can then be accessed by clients using [Postgres wire protocol](https://www.postgresql.org/docs/current/protocol.html) clients authenticated using mTLS.

* * * 

### Syntax

`stackql srv [server_options] [flags]`

* * *

:::note

Although the StackQL server uses the Postgres wire protocol, it is not a Postgres server, nor does it require one.  The wire protocol is used by the client to communicate with the server which uses StackQL to perform queries or DML operations against remote cloud and SaaS providers and return results back to the client.

:::

### Server Options

| Option | Description |
|--|--|
|`--pgsrv.address`|Address the server is listening on (typically `0.0.0.0`)|
|`--pgsrv.port`|Port the server is listening on (e.g. `5444`)|
|`--pgsrv.tls`|mTLS configuration object, see the [example](#examples) below|

:::tip

You should start the server with the `--auth` argument to enable authenticated queries to your selected providers, see [Authenticating to a Provider](/docs/getting-started/authenticating) for more information.

:::

### Flags

| Flag | Description |
|--|--|
|`-H,--help`|Print help information|
|`-v,--verbose`|Run queries in verbose mode with additional output sent to stdout, if the `-f` option is selected<br/>this additional logging information will be written to the output file along with the query results|
&nbsp;  
&nbsp;  
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

* * *

### Examples

Launch a StackQL server process and connect using a `psql` command line client.  

First download the `openssl.cnf` configuration file from [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Next generate the certificate and key files to be used by the server and client for mutual TLS authentication (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Now set environment variables, if the client and server are on the same host, these can be shared.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Set up an authentication object for whatever providers the server will need access to, for instance for GitHub.  

```bash
export GITHUB_CREDS=$(echo -n 'youruser:ghp_youraccesstoken' | base64)
AUTH='{ "github": { "type": "basic", "credentialsenvvar": "GITHUB_CREDS" } }'
```

Now start the server in one terminal.  

```bash
stackql srv --auth="${AUTH}" \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

In another terminal, connect to the server using the `psql` command line client.  

```bash
psql 127.0.0.1
```

Now run a StackQL query against an authenticated provider.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```