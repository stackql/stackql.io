---
slug: google-provider-december-2025-update
title: Google Provider Update - December 2025
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-google-provider-featured-image.png"
description: Major update to the StackQL Google provider featuring a new Speech-to-Text v2 service, enhanced Vertex AI with RAG capabilities, and significant improvements across BigQuery, Spanner, and 15+ other Google Cloud services.
keywords: [stackql, google cloud, gcp, provider, vertex ai, speech-to-text, bigquery, spanner]
tags: [stackql, google cloud, gcp, provider, vertex ai, speech-to-text, bigquery, spanner]
---

We've released a major update to the [__StackQL Google provider__](https://stackql.io/docs/providers) with a new service, enhanced AI/ML capabilities, and improvements across 177 service files.

## New Service: Speech-to-Text v2

The `speechv2` service brings Cloud Speech-to-Text API v2 to StackQL with 6 resources:

| Resource | Description |
|----------|-------------|
| `recognizers` | Manage speech recognition configurations with create, list, get, patch, delete, undelete, recognize, and batch_recognize methods |
| `custom_classes` | Create custom vocabulary classes for improved recognition accuracy |
| `phrase_sets` | Define phrase hints to boost recognition of specific terms |
| `config` | Manage location-level Speech-to-Text configuration |
| `locations` | Query available service locations |
| `operations` | Track long-running operations |

Key features include support for multiple audio encodings (WAV, FLAC, MP3, OGG, WebM, MP4/AAC), translation capabilities, denoiser config, and KMS encryption support.

## Vertex AI / AI Platform

The largest update in this release with 87,000+ line changes introduces powerful new RAG and evaluation capabilities:

- **RAG Resources**: `rag_corpora`, `rag_files`, `rag_engine_config` for Retrieval-Augmented Generation
- **Conversational AI**: New `chat` resource
- **Model Evaluation**: `evaluation_sets` and `evaluation_items` for systematic model assessment
- **New Resources**: `science`, `invoke`, and `openapi` resources
- **Performance**: Enhanced `cache_config` for caching configurations

## Discovery Engine

Major enhancements (50,000+ line changes) for search and conversational AI:

- New `assistants` resource
- New `sitemaps` resource for site search
- New `custom_models` resource
- Enhanced `sessions` and `answers` for conversational search
- New `authorized_views` and `authorized_view_sets` for access control

## Contact Center AI Insights

Quality assurance and analytics improvements (20,000+ line changes):

- New `qa_questions` and `qa_question_tags` for quality assurance workflows
- New `analysis_rules` resource
- New `segments` resource
- New `authorized_views` with IAM policy support
- New `datasets` and `views` resources

## BigQuery

Enhanced governance and access control (18,000+ line changes):

- New `routines_iam_policies` for stored procedure/function IAM
- Enhanced `row_access_policies`

## Healthcare API

Expanded metrics and data mapping (15,000+ line changes):

- New `data_mapper_workspaces_iam_policies`
- Enhanced metrics: `hl7_v2_store_metrics`, `dicom_store_metrics`, `series_metrics`, `study_metrics`
- New `instances_storage_info` resource

## Cloud Spanner

Backup and security enhancements (14,000+ line changes):

- New `backup_schedules` with IAM support
- New `databases_split_points` resource
- New `database_roles` with IAM policies

## Cloud SQL Admin

New integration and management features (12,000+ line changes):

- New `instances_entra_id_certificate` for Microsoft Entra ID integration
- New `instances_disk_shrink_config`
- New `instances_latest_recovery_time`

## GKE On-Prem

Enhanced IAM across VMware and Bare Metal clusters (9,000+ line changes):

- Enhanced VMware cluster resources with IAM policies
- Enhanced Bare Metal cluster resources with IAM policies
- New `vmware_node_pools` and `bare_metal_node_pools` with IAM

## Developer Connect

Git integration improvements (3,500+ line changes):

- New `git_repository_links_git_refs` resource
- New `users_self` and `users_access_token` resources
- New token resources: `read_token`, `read_write_token`

## Text-to-Speech

Enhanced `voices` and `text` resources with new capabilities.

## Get Started

Update to the latest Google provider:

```bash
stackql registry pull google
```

Let us know your thoughts! Visit us and give us a star on [__GitHub__](https://github.com/stackql/stackql).