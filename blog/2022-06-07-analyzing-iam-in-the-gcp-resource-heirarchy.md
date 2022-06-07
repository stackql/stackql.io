---
slug: analyzing-iam-in-the-gcp-resource-heirarchy
title: Analyzing IAM in the GCP Resource Hierarchy
authors:	
  - jeffreyaven
draft: false
image: "/img/blog/stackql-provider-for-netlify-released.png"
description: Data-centric approach to understanding entitlements across a GCP org with a complex hierarchy.
keywords: [gcp, stackql, stackql provider registry, entitlements, iam, uam, user access management, user access review]
tags: [gcp, stackql, stackql provider registry, entitlements, iam, uam, user access management, user access review]
---

import Gist from 'react-gist';

> StackQL allows you to query and interact with your cloud and SaaS assets using a simple SQL framework

Understanding entitlements across a GCP org with a complex hierarchy is a challenge.  I have taken and data-centric approach to this in this article.  

Prerequisites include setting up a Jupyter environment with StackQL (done here using Docker): [stackql-jupyter-demo](https://github.com/stackql/stackql-jupyter-demo).  You will also need a service account and associated key with the `roles/iam.securityReviewer` role.  

I've broken the notebook bits down to explain...  

## Setup

This step includes importing the required libraries (`pandas` etc.) and instantiating a StackQL client with the service account creds you created before.  You will supply your root node here using the `org_id` and `org_name` variables.  

<Gist id="dfc1230a1a1b25660504afc78ce17a50" 
/>

Next we will create some helper functions; these will help us enumerate nodes in the GCP org resource hierarchy and fetch and unnest IAM policies.  

<Gist id="14e9e9151056852592051de439fbe4e2" 
/>

## Get all nodes in the resource hierarchy

Create a dataframe containing all nodes in the resource hierarchy, including the root node (the organization), each folder with its subfolders, and projects.  The functions used will search each folder in the hierarchy to find its subfolders and projects using a depth-first search approach.  

<Gist id="fd2e3e222d156ce83c29345a5c01d0f4" 
/>

Inspecting the output, it looks like this:  

[![GCP Nodes](/img/blog/gcp-iam-jupyter-nodes-screenshot.png)](/img/blog/gcp-iam-jupyter-nodes-screenshot.png)

## Create a dataset including each node and its associated IAM policies

This step will fetch all of the policies applied at each node in the data structure we created in the previous step.  

The IAM policies response from `SELECT role, members FROM google.cloudresourcemanager.project_iam_policies ...` presents some challenges as `members` is a nested list which we need to unnest (or explode) along with the associated role and conditions (if they exist).  

This bit of massaging will give us a SQL-friendly model we can use for analysis and join with another data source (such as a list of identities from an identity provider).  

<Gist id="424858d73aa4e7e0a85b4bfe7158ed12" 
/>

# Inspecting the Final Output

We can now peek at the final data set, which looks like this:  

[![GCP Nodes with IAM Policies](/img/blog/gcp-iam-jupyter-iam-screenshot.png)](/img/blog/gcp-iam-jupyter-iam-screenshot.png)

What's next?  You could now join this with data from your IdP, or other SaaS services to correlate entitlements across your entire estate.  You could also drill into specific service accounts, users, or groups.  Queries are run in real-time, so you can refresh the data by simply rerunning the cells.  

Welcome your feedback by getting in touch or raising issues at [__stackql/stackql__](https://github.com/stackql/stackql) or [__stackql/stackql-provider-registry__](https://github.com/stackql/stackql-provider-registry), give us some ⭐️ love while you are there!  

Enjoy!  