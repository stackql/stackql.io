---
title: Authenticating to a Provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

See also:  
[[` exec `]](/docs/command-line-usage/exec) [[` shell `]](/docs/command-line-usage/shell) [[` AUTH `]](/docs/language-spec/auth)

Authenticating to a cloud provider is often the first step in an StackQL routine. Meta commands such as [`SHOW`](/docs/language-spec/show) and [`DESCRIBE`](/docs/language-spec/describe) can be run against a provider without authenticating, but [`SELECT`](/docs/language-spec/select), [`INSERT`](/docs/language-spec/insert) and [`EXEC`](/docs/language-spec/exec) commands require an authenticated session.

Authentication can be done in one of two ways, interactive (which requires a browser on the machine you are using) and non-interactive (using a service account key file or API credentials).

The asterix (`*`) at the StackQL prompt indicates that the session is authenticated successfully.

### Authenticating Interactively

Interactive authentication can be used in the StackQL Command Shell and is invoked by using the [`AUTH LOGIN`](/docs/language-spec/auth) command. A browser window will open on the machine with a login and consent screen, follow the instructions provided. Once consent is granted, you can return to the shell, note the asterix next to the StackQL prompt. To check your authentication status use the [`SHOW AUTH`](/docs/language-spec/show) command.

### Authenticating Non-Interactively

Non-interactive authentication is accomplished using a service account key file, specified using the `--keyfilepath `command line argument. This form of authentication can be used with the shell and exec commands. An example is shown here:

```shell
./stackql exec -i iqlscripts/attach-disk.iql \
--keyfilepath /mnt/c/tmp/stackql-demo.json
```

### Revoking an Authenticated Session

It may be necessary to re-authenticate using a different user or service account, if so you can use the [`AUTH REVOKE`](/docs/language-spec/auth) command. An example of this is shown below:
