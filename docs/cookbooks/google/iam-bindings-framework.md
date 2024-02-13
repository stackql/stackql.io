---
title: IAM Bindings
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

# Google GitOps IAM bindings framework using StackQL

In this guide, we will demonstrate a GitOps framework for IAM Bindings (entitlements) management in GCP using StackQL, a powerful dev tool that enables querying and deploying cloud infrastructure and resources using SQL syntax.  

Tested with <span class="cookbook_tested_on">default sql backend</span> <span class="cookbook_tested_on">macos</span> <span class="cookbook_tested_on">linux</span> <span class="cookbook_tested_on">powershell</span>

## Google IAM background

Google Cloud Identity and Access Management (IAM) is a framework for managing access to Google Cloud resources, allowing administrators to define who can take specific actions on resources. It operates around identities (like users and service accounts), roles, and permissions, where roles assigned to these identities govern their access rights. This guide delves into managing IAM bindings, which link these roles to identities, using StackQL, a tool that simplifies cloud resource management with SQL syntax.  

IAM bindings can be created at any of the following levels:

- organization level
- folder level
- project level
- resource level (Buckets, BigQuery Datasets, etc)
- billing account level

IAM bindings applied at organization or folder level are inherited by all child objects.

## Declarative binding definitions and GitOps

The guide will show you how to master IAM bindings in your source code repository, making it the source of truth for state and history, a concept known as GitOps, and how to use StackQL to apply changes using CI/CD workflows.   

## How it works

The framework that we will walk you through will deploy IAM bindings across all nodes in your GCP heirarchy, as well as creating custom roles (with curated permissions) - as opposed to predefined roles such as `roles/bigquery.dataEditor` for example.

### Project directory structure

The following directory tree is used for this bindings deployment framework:

```
├── iql
│   ├── iam.iql
│   ├── ...
├── data
│   ├── iam.jsonnet
│   ├── iam
│   │   ├── bindings.json
│   │   ├── custom_roles.json
```

### `iql/iam.iql` file

This is the main file which will be run by the `stackql`, using `data/iam.jsonnet` this will render and apply the transpiled policy bindings generated from the data in `data/iam/bindings.json`

> this file should not need to be modified




## `data/iam.jsonnet`

This file is preprocessed by `iql/iam.iql` sourcing data from `data/iam/bindings.json`

> this file should not need to be modified

## `data/iam/bindings.json`

> this file __will need to be modified__ to make policy binding changes

This is the master source of policy and binding data, the data structure maps one or many principals (groups, serviceaccount, etc) to one or many roles (predefined or custom) at a particular level (parent) as shown below:

```json
  "<< level >>": [
    {
      "members": [
        "<< principal >>",
        ...
      ],
      "roles": [
        "<< role >>",
        ...
      ]
    },
```

for example:

```json
  "folders": {
    "nonprod": [
      {
        "members": [
          "serviceAccount:terraform-nonprod@vcdi-terraform.iam.gserviceaccount.com"
        ],
        "roles": [
          "roles/compute.networkAdmin",
          "roles/resourcemanager.folderViewer",
          "roles/resourcemanager.projectCreator",
          "roles/storage.admin",
          "roles/artifactregistry.admin",
          "roles/container.serviceAgent",
          "roles/iam.securityAdmin",
          "roles/bigquery.admin",
          "roles/serviceusage.serviceUsageAdmin",
          "roles/cloudsql.admin",
          "roles/iam.serviceAccountAdmin",
          "roles/pubsub.admin",
          "roles/cloudfunctions.developer"
        ]
      }
    ],
```

# Deployment

This process is intentionally not run through a CI/CD pipeline, as the changes are infrequent, and we wish not to give a service account the levels of priveleges required to deploy this.  This is to be run by a priveleged adminstrator who is a member of the gcp-org-administrators Google group.  

The user needs to be authenticated to GCP, using `gcloud auth login` prior to running this script

Run the following command to perform a dryrun:
```
infraql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

The resultant data in this file can be exectuted as individual statements in `infraql shell` or as a batch by running:

```
infraql exec -i iam-TEMPLATED.iql \
```
Alternatively, you could take the policies (JSON payloads) generated by this script and run them using the `gcloud iam` command.

