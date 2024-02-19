---
title: StackQL in AWS Cloud Shell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
  - cspm
  - aws
  - amazon web services
description: Query and Deploy AWS Cloud Infrastructure and Resources using SQL
image: "/img/cookbooks/aws/stackql-aws-provider-featured-image.png"
---

# Using StackQL in AWS Cloud Shell

In this guide, we will demonstrate using StackQL, a powerful dev tool that enables querying and deploying cloud infrastructure and resources using SQL syntax from within AWS Cloud Shell, using the user context of the login session to the AWS Console or assuming another IAM Role.    

Tested with <span class="cookbook_tested_on">embedded sql backend</span> <span class="cookbook_tested_on">linux</span>

<details>

<summary>What is <b>AWS Cloud Shell</b>?</summary>

> AWS Cloud Shell is a browser-based, pre-authenticated shell service provided by Amazon Web Services (AWS).  It allows developers and IT professionals to manage their cloud resources directly from their web browsers.  With AWS Cloud Shell, users can quickly run commands related to AWS services without installing or configuring the AWS CLI (Command Line Interface) on their local machines.  This service provides a ready-to-use environment with typical development and management tools pre-installed, such as the AWS CLI, Python, Node.js, and more, making it easier to interact with AWS services and automate tasks.  AWS Cloud Shell is directly integrated with the AWS Management Console, offering a seamless experience for executing administrative tasks, exploring AWS services, and managing infrastructure.  Since AWS Cloud Shell automatically authenticates users through their AWS Console login, it simplifies the process of running commands and scripts in the context of their AWS account, enhancing productivity and efficiency in cloud operations.

</details>

## How it works

The StackQL linux package includes scripts to use `stackql` directly within major cloud providers built-in cloud shell environments.  For AWS the script is __`stackql-aws-cloud-shell.sh`__.  Usage for the __`stackql-aws-cloud-shell.sh`__ script is shown here:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

The `stackql-aws-cloud-shell.sh` script automatically downloads the latest AWS provider for StackQL.  Source code for the included cloud shell scripts can be found at [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Download the `stackql` Linux package from AWS Cloud Shell

To get started, from AWS Cloud Shell, download the latest Linux binary for StackQL, using the following commands:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

This will download and extract the `stackql` binary along with the `stackql-aws-cloud-shell.sh` script to the home directory of your AWS Cloud Shell environment.  Now you're ready to go!  

## Using the `stackql shell` from AWS Cloud Shell

Launching an authenticated StackQL command shell from within AWS Cloud Shell is as easy as:  

```shell
sh stackql-aws-cloud-shell.sh
```

You can now start running interactive SQL-based AWS queries using the StackQL provider, as shown here:  

<video width="720" height="480" controls>
  <source src="/img/cookbooks/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Your browser does not support the video tag.
</video>

## Assuming an IAM Role using StackQL

Assuming an IAM Role in AWS allows you to perform actions and access resources that you might not be able to with your default user permissions.  This is particularly useful when you need temporary access to resources or perform actions with permissions different from your primary account.  With StackQL, you can seamlessly assume an IAM Role to query and manage AWS resources under the permissions granted by that role.

<details>
<summary>Understanding IAM Role Assumption</summary>

> Assuming an IAM Role is a secure way to grant temporary access to AWS resources.  When you assume a role, AWS STS provides you with temporary security credentials that allow you to access AWS services and resources the role has permission to use.  This mechanism is ideal for cross-account access, temporary access for users, or adhering to the principle of least privilege.

> Roles can be assumed using the AWS Management Console, AWS CLI, or AWS CLI, and directly within StackQL. This ensures that users and applications only have the necessary permissions for the duration required, enhancing the security posture of your AWS environment.

</details>

To assume an IAM Role using StackQL in AWS Cloud Shell, you can use the `--role-arn` option with the `stackql-aws-cloud-shell.sh` script.  This option requires the Amazon Resource Name (ARN) of the role you wish to assume.  When provided, StackQL will use AWS Security Token Service (STS) to assume the specified role before executing any queries or commands.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

By assuming a role, you can leverage StackQL to interact with AWS resources that your default user might not have access to, enabling a more flexible and secure way to manage your cloud environment.  

When executing StackQL commands or scripts with an assumed IAM Role, ensure the role has the necessary permissions to perform the desired actions and access the specified resources.  This approach facilitates granular access control and aligns with AWS best practices for security and governance.  

## Running `stackql exec` (batch operations) from AWS Cloud Shell

StackQL queries can be executed using the `exec` command as well; this is a valuable option for:

- specifying alternate output formats such as `json` or `csv`
- producing output files for the results of StackQL queries
- using StackQL queries provided through input files (`.iql` files by convention)
- performing complex operations, including sourcing variables or preprocessing queries (using `json` or `jsonnet`)

Examples using `exec` within AWS Cloud Shell are shown here:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

The above example runs the StackQL query specified and produces an out `CSV` file, which can be downloaded from the AWS Cloud Shell.  Additionally, you can supply an IAM role using the `--role-arn` argument to assume another role for your query or mutation operation; an example is shown here (in this case, we have sourced the query from an input file):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

More information on the `exec` command can be found [__here__](/docs/command-line-usage/exec)
