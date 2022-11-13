---
slug: stackql-middleware-and-playground-available
title: StackQL Middleware and Playground Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
draft: false
image: "/img/stackql-cover.png"
description: The StackQL Middleware Server along with the StackQL Playground App allow users to explore and query resources from API providers using SQL.
keywords: [stackql, stackql middleware, stackql playground, middleware, stackql provider registry, multicloud, asset management, cloud security]
tags: [stackql, stackql middleware, stackql playground, middleware, stackql provider registry, multicloud, asset management, cloud security]
---

Proud to announce the release of the first version of our middleware server and playground for StackQL.  

> StackQL allows you to query and interact with cloud services and APIs using SQL grammar and an ORM which is a direct reflection of a provider API, no database is required or implemented  

## StackQL Middleware Server

Our [__middleware solution__](https://github.com/stackql/stackql-middleware) allows you to use StackQL as a query language to interact with APIs, much like GraphQL - however, the query DSL is SQL, providing a friendlier, more data-centric experience for developers.  As shown in the example below, developers can `POST` queries to a `/stackql` endpoint; the queries are parsed and executed by a StackQL runner via the middleware server.  

<iframe width="560" height="315" src="https://www.youtube.com/embed/IQ5UdKDsizQ" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

As the StackQL middleware API server and runner are stateless, the solution is horizontally scalable.  With cache and authorization to be implemented, the solution provides a flexible, robust, scalable, performant back end for applications.  

Furthermore, using SQL semantics, developers can JOIN data between API providers and perform projections, filtering, aggregation, windowing operations, and more on simple or complex data types.  

## StackQL Playground

The [__StackQL Playground__](https://github.com/stackql/stackql-playground) is a TypeScript app that connects to a StackQL Middleware Server, which provides access to backend APIs using SQL.  Features of the playground include...  

### Explore API Providers

The StackQL Playground allows you to explore and query providers, services, resources, fields, and methods. 

### Submit Queries

Using the Playground you can submit queries to the `/stackql` endpoint of the StackQL Middleware Server.  

### View, Sort, and Filter Results

Query results can be sorted or filtered in the grid result set in the StackQL Playground app, JSON results can also be viewed or copied from the JSON results tab.  

### Save Results as CSV or JSON

Users can save results to local `CSV` or `JSON` files as well.  

### Generate Types

Furthermore, after modeling a query you can export the TypeScript types using the __Get Types__ button.

Huge thanks to [__Yuncheng Yang__](https://github.com/FabioYyc) for the work he put in on this!  

You can find the complete code [here](https://github.com/stackql/stackql-middleware/blob/main/demo.sh) to launch an environment using `docker-compose`, which includes the StackQL Middleware Server, a StackQL runner (runs the queries on the back end), and the StackQL Playground app.  

Let us know what you think.  
