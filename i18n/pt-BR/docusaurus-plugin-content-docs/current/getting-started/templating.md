---
title: Criar templates de infraestrutura
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Gerar templates INSERT para qualquer recurso com SHOW INSERT no StackQL, completos ou só com os parâmetros obrigatórios, como ponto de partida para definir infraestrutura."
image: "/img/stackql-featured-image.png"
---
Veja também:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Usar variáveis ]](/getting-started/variables)

Os recursos na nuvem são implantados com o StackQL por meio de instruções `INSERT`.  Os templates `INSERT` para diferentes recursos na nuvem podem ser gerados com o comando `SHOW INSERT`.

### Gerar um template `INSERT` com o comando `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Veja aqui um exemplo de geração de um template:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Isso pode ser executado pelo `shell` ou com um comando `exec`, como mostrado aqui:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Retorna:__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> Esse template pode então ser usado com um arquivo de dados `json` ou `jsonnet` para fornecer valores em tempo de execução; para obter mais informações, consulte [Usar variáveis](/getting-started/variables).

### Gerar um template `INSERT` somente com os parâmetros obrigatórios {#generating-an-insert-template-with-only-required-parameters}

Para gerar um template `INSERT` do StackQL somente com os atributos obrigatórios, use a dica de consulta `/*+ REQUIRED */`, como mostrado aqui:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Isso pode ser executado pelo `shell` ou com um comando `exec`, como mostrado aqui:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Retorna:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
