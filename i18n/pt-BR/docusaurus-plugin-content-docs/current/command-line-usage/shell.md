---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O comando stackql shell inicia o shell de comandos interativo do StackQL para executar consultas e comandos de provider nas suas contas na nuvem e SaaS."
image: "/img/stackql-featured-image.png"
---

Comando usado para iniciar o shell de comandos interativo do StackQL.  Para obter mais informações, consulte [Usar o shell interativo do StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Sintaxe {#syntax}

`stackql shell [flags]`

* * *

### Opções {#flags}

| Opção | Descrição |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Exibe as informações de ajuda|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Executa as consultas no modo detalhado, com saída adicional enviada para stdout; se a opção global `-f` (`--outfile`) estiver definida, essas informações de log adicionais são gravadas no arquivo de saída junto com os resultados da consulta|
<br/>
<br/>
> consulte [Opções globais](/command-line-usage/global-flags) para ver opções adicionais

* * *

### Exemplos {#examples}

Iniciar o shell do StackQL usando um arquivo de chave de conta de serviço para autenticação.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
