---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O comando stackql registry lista, baixa e gerencia providers do registro de providers do StackQL pela linha de comando."
image: "/img/stackql-featured-image.png"
---

Comando usado para interagir com o StackQL Provider Registry.  O local do registro, e se as assinaturas dos documentos de provider são verificadas, são configurados com a opção global [`--registry`](/command-line-usage/global-flags#--registry).  O comando `registry` não está disponível enquanto o [gerenciamento de acesso](/command-line-usage/access-management) está habilitado; em vez dele, use `REGISTRY PULL` em uma sessão `exec` ou `shell` protegida.

* * * 

### Sintaxe {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Argumentos {#arguments}

| Argumento | Descrição | Exemplo |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Operação de registro a ser executada (`list` ou `pull`) | `list` ou `pull` |    
|<span class="nowrap">`provider`</span>|Provider a ser instalado com o subcomando `pull`| `okta` |
|<span class="nowrap">`version`</span>|Versão opcional do provider a ser instalada com o subcomando `pull` *(versão do documento de provider do StackQL)* | `v23.03.00121` |


### Opções {#flags}

| Opção | Descrição |
|--|--|
|<span class="nowrap">`-H, --help`</span>|Exibe as informações de ajuda|
<br/>
<br/>
> consulte [Opções globais](/command-line-usage/global-flags) para ver opções adicionais

* * *

### Exemplos {#examples}

Listar os providers disponíveis no registro público:
```shell
stackql registry list
```

Baixar a versão mais recente de um provider do registro público:
```shell
stackql registry pull okta
```
