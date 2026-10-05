---
slug: exploring-gcp-roles-with-stackql
title: Exploring GCP Roles with StackQL
authors:	
  - jeffreyaven
image: "/img/blog/infraql-gcp-roles.png"
description: This article provides a primer on roles in GCP with demonstrations of creating and querying roles using StackQL.
keywords: [stackql, infracoding, IaC, infrastructure as code, gcp, iam]
tags: [stackql, infracoding, IaC, infrastructure as code, gcp, iam]
hide_table_of_contents: false
---

<head>
<meta name="author" content="Jeffrey Aven" />
</head>

Understanding roles is integral to applying the principal of least privilege to GCP environments.

## A quick primer on roles in GCP 
A __Role__ in GCP is a collection of permissions to services and APIs on the platform.  Roles are "bound" to principals or members (users, groups and service accounts).  

These bindings are referred to as "policies" which are scoped at a particular level - organisation, folder, project, resource.  

There are three types of roles - __*Primitive Roles*__, __*Predefined Roles*__ and __*Custom Roles*__.  

### Primitive (or Basic) Roles
These are legacy roles set at a GCP project level which include Owner, Editor, and Viewer.  These are generally considered to be excessive in terms of permissions and their use should be minimised if not avoided altogether.

### Predefined Roles
These are roles with fine grained access to discrete services in GCP.  Google has put these together for your convenience.  In most cases predefined roles are the preferred mechanism to assign permissions to members.

### Custom Roles
Custom roles can be created with a curated collection of permissions if required, reasons for doing so include:
- if the permissions in predefined roles are excessive for your security posture
- if you want to combine permissions across different services, and cannot find a suitable predefined role although it is preferred to assign multiple predefined roles to a given member

## Anatomy of an IAM Policy
An __IAM Policy__ is a collection of bindings of one or more members (user, group or service account) to a role (primitive, predefined or custom).  Policies are normally expressed as JSON objects as shown here:

```json
{
  "bindings": [
    {
      "members": [
        "group:project-admins@my-cloud-identity-domain.com"
      ],
      "role": "roles/owner"
    },
    {
      "members": [
        "serviceAccount:provisioner@my-project.iam.gserviceaccount.com",
		"user:javen@avensolutions.com"
      ],
      "role": "roles/resourcemanager.folderViewer"
    }
  ]
}
```
> Groups are Google Groups created in Cloud Identity or Google Workspace (formerly known as G-Suite)

Application of policies is an atomic operation, which will overwrite any existing policy attached to an entity (org, folder, project, resource).

## Querying Roles with StackQL
Predefined and primitive roles are defined in the `roles` resource in StackQL (`google.iam.roles`) - which returns the following fields (as returned by `DESCRIBE google.iam.roles`):

| Name | Description |
| :---  | :--- |
| __`name`__ | Name of the role in the format `roles/[{service}.]{role}`<br />for predefined or basic roles, or qualified for custom roles,<br />e.g. `organizations/{org_id}/roles/[{service}.]{role}` |
| __`description`__ | An optional, human-readable description for the role |
| __`includedPermissions`__ |  An array of permissions this role grants (only displayed with<br />`VIEW = 'full'`) |
| __`etag`__ | Output only, used internally for consistency |
| __`title`__ | An optional, human-readable title for the role (visible in the<br />Console) |
| __`deleted`__ | A read only boolean field showing the current deleted state<br />of the role |
| __`stage`__ | The current launch stage of the role, e.g. `ALPHA` |  

### Get the name for a role

Often, you may know the "friendly" *title* for a role like __"Logs Bucket Writer"__, but you need the actual role name to use in an Iam policy - which is `roles/logging.bucketWriter`.  A simple query to find this using StackQL is shown here:  

```sql
SELECT name
FROM google.iam.roles
WHERE title = 'Logs Bucket Writer';
/* RETURNS:
|----------------------------|
|            name            |
|----------------------------|
| roles/logging.bucketWriter |
|----------------------------|
*/
```

Conversely, if you have the name but want the friendly title you could use:  

```sql
SELECT title
FROM google.iam.roles
WHERE name = 'roles/logging.bucketWriter';
/* RETURNS:
|--------------------|
|       title        |
|--------------------|
| Logs Bucket Writer |
|--------------------|
*/
```

Wildcards can also be used with the `LIKE` operator, for example to get the `name` and `title` for each predefined role in the `logging` service you could run:

```sql
SELECT name, title
FROM google.iam.roles
WHERE name LIKE 'roles/logging.%';
```

### Get the permissions for a role
To return the includedPermissions you need to add the following `WHERE` clause:

```sql
WHERE view = 'FULL'
```

An example query to list the permissions for a given role is shown here:  

```sql
SELECT includedPermissions
FROM google.iam.roles
WHERE view = 'FULL' AND
name = 'roles/cloudfunctions.viewer';
/* RETURNS
["cloudbuild.builds.get","cloudbuild.builds.list",...]
*/
```

A more common challenge is that you know a particular permission such as `cloudfunctions.functions.get` and you want to know which roles contain this permission you could run the following query:  

```sql
SELECT name, title
FROM google.iam.roles
WHERE view = 'FULL'
AND includedPermissions LIKE '%cloudfunctions.functions.get%';
```

## Creating custom roles and more...
In forthcoming articles, we will demonstrate how you can create custom roles using StackQL `INSERT` operations, as well as how you can construct a simple IAM framework to manage and provision access to resources in GCP, stay tuned!
