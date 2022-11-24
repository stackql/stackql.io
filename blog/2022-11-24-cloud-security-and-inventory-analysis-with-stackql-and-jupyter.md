---
slug: cloud-security-and-inventory-analysis-with-stackql-and-jupyter
title: Cloud security and inventory analysis with StackQL and Jupyter
hide_table_of_contents: false
authors:	
  - jeffreyaven
draft: false
image: "/img/blog/stackql-jupyter-featured-image.png"
description: StackQL can be used with Jupyter to provide valuable insights into your cloud and SaaS estates, whether for security posture management, cross-cloud entitlements reporting, cost optimization, or asset/inventory management.
keywords: [stackql, jupyter, python, ipython, multicloud, asset management, cloud security, analysis, analytics]
tags: [stackql, jupyter, python, ipython, multicloud, asset management, cloud security, analysis, analytics]
---

import Gist from 'react-gist';

> StackQL is an intelligent API client which uses SQL as a front-end language with support for multi-cloud and SaaS provider environments, you can find more information at [__github.com/stackql/stackql__](https://github.com/stackql/stackql) 

StackQL can provide valuable insights into your cloud and SaaS estates, whether for security posture management, cross-cloud entitlements reporting, cost optimization, or asset/inventory management.  

As an interactive analysis tool, Jupyter notebooks can leverage StackQL to provide sources for cloud and SaaS provider data.  

[![GCP Nodes](/img/blog/stackql-jupyter.png)](/img/blog/stackql-jupyter.png)

We've recently added magic function support for running StackQL queries in Jupyter notebooks, making the integration between StackQL and Jupyter more seamless. StackQL magic and be used on a line in a cell or the entire cell itself, as shown here:  

<Gist id="1ea3d93bd20ce547b1b01e2489ecd000" 
/>

The stackql-jupyter-demo Docker image is available from [__Docker Hub__](https://hub.docker.com/r/stackql/stackql-jupyter-demo). You can find instructions to run using the Docker Hub image and instructions to run using `docker-compose` at [__github.com/stackql/stackql-jupyter-demo__](https://github.com/stackql/stackql-jupyter-demo).  

Give us your feedback!  