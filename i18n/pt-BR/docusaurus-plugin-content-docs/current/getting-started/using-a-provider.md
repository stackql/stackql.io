---
title: Usar um provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "Usar um provider no StackQL: baixá-lo do registro, autenticar-se com variáveis de ambiente ou credenciais e consultar seus serviços e recursos."
image: "/img/stackql-featured-image.png"
---

Veja também:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

As consultas do StackQL podem ser executadas em vários provedores de nuvem diferentes, entre eles Google Cloud Platform, Amazon Web Services, Microsoft Azure, DigitalOcean e outros. As especificações dos providers do StackQL são baixadas do [__StackQL Provider Registry__](/providers); isso pode ser feito com o comando [__`REGISTRY PULL`__](/language-spec/registry) do StackQL ou com o utilitário de linha de comando [__`stackql registry`__](/command-line-usage/registry).


## Instalar um provider {#installing-a-provider}

Para ver a lista de providers disponíveis, execute o comando a seguir:  

```sql
REGISTRY LIST;
```

Para instalar um provider, execute o comando a seguir:

```sql
REGISTRY PULL okta;
```

Para ver os providers instalados no sistema atual, execute o comando a seguir:

```sql
SHOW PROVIDERS;
```

## Autenticar-se em um provider {#authenticating-to-a-provider}

Autenticar-se em um provedor de nuvem costuma ser o primeiro passo em uma rotina do StackQL. Metacomandos como [`SHOW`](/language-spec/show) e [`DESCRIBE`](/language-spec/describe) podem ser executados em um provider sem autenticação, mas os comandos [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) e [`EXEC`](/language-spec/exec) geralmente exigem uma sessão autenticada.  

A autenticação usa variáveis de ambiente definidas no sistema (ou obtidas como segredos em um sistema de CI); cada provider tem as suas próprias variáveis padrão exclusivas para autenticação, então consulte a documentação do seu provider na [documentação do StackQL Provider Registry](/providers).  As variáveis também podem ser carregadas de um arquivo no estilo dotenv na inicialização, com a opção global [`--env.file`](/command-line-usage/global-flags).  

:::tip

Você pode se autenticar em vários providers no mesmo shell interativo ou na mesma rotina em lote não interativa definindo as variáveis de autenticação de cada provider, por exemplo:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Isso é especialmente útil quando você faz join de dados de vários providers em uma única consulta.

:::
