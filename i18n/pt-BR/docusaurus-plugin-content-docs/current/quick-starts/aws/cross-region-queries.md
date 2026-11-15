---
title: Consultas entre regiões
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
  - cspm
  - aws
  - amazon web services
description: "Consulte e agregue recursos da AWS de várias regiões em uma única instrução do StackQL, com listas de regiões e execução assíncrona em vez de loops por região."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Consultas assíncronas entre regiões com o StackQL

Neste guia, vamos demonstrar uma funcionalidade poderosa do StackQL: a capacidade de consultar e agregar recursos de várias regiões da AWS.  Isso envolve inúmeras solicitações de API para diferentes endpoints, que são executadas de forma assíncrona.  Tudo isso acontece de forma fluida e transparente para o usuário.  

> O StackQL é uma poderosa ferramenta de desenvolvimento que permite consultar e implantar infraestrutura e recursos na nuvem com sintaxe SQL.  

Testado com <span class="tutorial_tested_on">backend SQL incorporado</span> <span class="tutorial_tested_on">backend SQL postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Entender as regiões e os recursos da AWS {#understanding-aws-regions-and-resources}

Os recursos da AWS ficam hospedados em vários locais do mundo.  Esses locais são compostos de regiões e zonas de disponibilidade, projetadas para permitir que os usuários coloquem instâncias e armazenem dados em diferentes locais.

## O problema das regiões da AWS... {#the-problem-with-aws-regions}

Ao trabalhar com a AWS, é fundamental entender que o AWS Management Console oferece uma visão dos recursos com escopo regional, o que permite que os usuários interajam com os recursos e os gerenciem em uma região específica de cada vez.  Da mesma forma, os comandos da AWS Command Line Interface (CLI) e os Software Development Kits (SDKs) usam a região como ponto de entrada das operações, o que exige especificar uma região para executar comandos ou programas.  

> O modelo regional dos serviços na nuvem introduz complexidade quando os recursos abrangem várias regiões.  Além disso, violações de segurança, incluindo ataques de mineração de criptomoedas, costumam usar recursos em diversas áreas, fora da região principal que as organizações normalmente usam.

## Especificar regiões nas consultas do StackQL {#specifying-regions-in-stackql-queries}

Com o StackQL, você pode especificar regiões para direcionar as consultas a recursos em diferentes locais geográficos.  Isso é especialmente útil para gerenciar recursos distribuídos pelo mundo todo.

### Exemplo: listar instâncias do EC2 em várias regiões {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Essa consulta retorna uma lista de instâncias do EC2 das regiões `us-east-1` (Norte da Virgínia) e `eu-west-1` (Irlanda), o que mostra como o StackQL pode obter dados de várias regiões em uma única consulta.  Isso também pode ser usado com consultas de agregação, por exemplo:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## O poder das consultas assíncronas {#the-power-of-asynchronous-queries}

O StackQL executa essas consultas entre regiões de forma assíncrona, fazendo várias solicitações de API em paralelo à AWS.  Isso garante uma obtenção de dados eficiente, sem esperar que a solicitação de uma região termine antes de passar para a próxima.

## Conclusão {#conclusion}

As consultas assíncronas entre regiões do StackQL oferecem um método robusto e eficiente para gerenciar recursos da AWS em diferentes locais geográficos.  Ao usar uma sintaxe semelhante à do SQL para essas consultas, o StackQL simplifica tarefas complexas de gerenciamento de nuvem, o que o torna uma ferramenta inestimável para desenvolvedores e administradores de nuvem que buscam otimizar sua infraestrutura da AWS.
