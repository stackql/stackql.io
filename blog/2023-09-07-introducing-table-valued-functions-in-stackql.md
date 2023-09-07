---
slug: introducing-table-valued-functions-in-stackql
title: Introducing Table Valued Functions in StackQL
hide_table_of_contents: false
authors:  
  - kieranrimmer
image: "/img/blog/stackql-featured-image.png"
keywords: [stackql, analytics]
tags: [stackql, analytics]
---

Many provider query responses include columns which are arrays, the iam policy related resources in google are a classic example of this.  for example, this query:

```sql
select * 
from google.cloudresourcemanager.projects_iam_policies 
where projectsId = 'stackql';
```

produces..

```
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|                               
| condition | members                                                                                                                                                                                    | role         |                               
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|                               
| null      | ["serviceAccount:1234567890-compute@developer.gserviceaccount.com","serviceAccount:1234567890@cloudservices.gserviceaccount.com","serviceAccount:stackql@appspot.gserviceaccount.com"] | roles/editor |                               
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|                               
| null      | ["serviceAccount:1234567890-compute@developer.gserviceaccount.com","serviceAccount:1234567890@cloudservices.gserviceaccount.com","serviceAccount:stackql@appspot.gserviceaccount.com"] | roles/editor |                               
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|                               
| null      | ["serviceAccount:1234567890-compute@developer.gserviceaccount.com","serviceAccount:1234567890@cloudservices.gserviceaccount.com","serviceAccount:stackql@appspot.gserviceaccount.com"] | roles/editor |                               
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|                               
```

What you want to do is unnest each member in members for each role binding (and condition if applicable)

Enter the table valued function [__`json_each`__](/docs/language-spec/functions/json/json_each).

The [__`json_each`__](/docs/language-spec/functions/json/json_each) function accepts a field (optionally with a json path expression) and returns a table object with fields that can be projected in your result set, for example (querying the same underlying resource as above), this...

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 1) as member_type,
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
``` 

now provides something much more useful from an analytic perspective:

```
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
|                 role                 |  member_type   |                                   member                                    |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/appengine.serviceAgent         | serviceAccount | service-1234567890@gcp-gae-service.iam.gserviceaccount.com                |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/cloudbuild.builds.builder      | serviceAccount | 1234567890@cloudbuild.gserviceaccount.com                                 |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/cloudbuild.serviceAgent        | serviceAccount | service-1234567890@gcp-sa-cloudbuild.iam.gserviceaccount.com              |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/compute.serviceAgent           | serviceAccount | service-1234567890@compute-system.iam.gserviceaccount.com                 |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/container.serviceAgent         | serviceAccount | service-1234567890@container-engine-robot.iam.gserviceaccount.com         |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
| roles/containerregistry.ServiceAgent | serviceAccount | service-1234567890@containerregistry.iam.gserviceaccount.com              |
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/dataflow.serviceAgent          | serviceAccount | service-1234567890@dataflow-service-producer-prod.iam.gserviceaccount.com |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/dataproc.serviceAgent          | serviceAccount | service-1234567890@dataproc-accounts.iam.gserviceaccount.com              |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/editor                         | serviceAccount | 1234567890-compute@developer.gserviceaccount.com                          |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/editor                         | serviceAccount | 1234567890@cloudservices.gserviceaccount.com                              |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/editor                         | serviceAccount | stackql@appspot.gserviceaccount.com                                         |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/firebaserules.system           | serviceAccount | service-1234567890@firebase-rules.iam.gserviceaccount.com                 |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/firestore.serviceAgent         | serviceAccount | service-1234567890@gcp-sa-firestore.iam.gserviceaccount.com               |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/owner                          | serviceAccount | stackql-provisioner@stackql.iam.gserviceaccount.com                         |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/owner                          | serviceAccount | t1-804@stackql.iam.gserviceaccount.com                                      |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/owner                          | user           | javen@stackql.io                                                            |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/owner                          | user           | krimmer@stackql.io                                                          |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/pubsub.serviceAgent            | serviceAccount | service-1234567890@gcp-sa-pubsub.iam.gserviceaccount.com                  |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|                                                                                    
| roles/viewer                         | serviceAccount | testing-admin@stackql.iam.gserviceaccount.com                               |                                                                                    
|--------------------------------------|----------------|-----------------------------------------------------------------------------|
```

`__json_each__`is available from  version 0.5.418 or stackql onwards, this function can also be used in StackQL GitHub Actions such as [__`stackql-exec`__](https://github.com/marketplace/actions/stackql-studios-stackql-exec) or [__`stackql-assert`__](https://github.com/marketplace/actions/stackql-studios-stackql-assert) and in Python and Pandas using [__`pystackql`__](https://pystackql.readthedocs.io/en/latest/).