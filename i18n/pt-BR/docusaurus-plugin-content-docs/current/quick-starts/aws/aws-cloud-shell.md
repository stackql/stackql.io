---
title: StackQL no AWS CloudShell
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
description: "Execute o StackQL no AWS CloudShell como o usuário conectado: baixe o pacote para Linux, use o shell do stackql, assuma uma role do IAM e execute consultas em lote com exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Usar o StackQL no AWS CloudShell

Neste guia, vamos demonstrar o uso do StackQL, uma poderosa ferramenta de desenvolvimento que permite consultar e implantar infraestrutura e recursos na nuvem com sintaxe SQL, no AWS CloudShell, usando o contexto de usuário da sessão de login no console da AWS ou assumindo outra role do IAM.    

Testado com <span class="tutorial_tested_on">backend SQL incorporado</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>O que é o <b>AWS CloudShell</b>?</summary>

> O AWS CloudShell é um serviço de shell pré-autenticado e baseado em navegador, oferecido pela Amazon Web Services (AWS).  Ele permite que desenvolvedores e profissionais de TI gerenciem seus recursos na nuvem diretamente do navegador web.  Com o AWS CloudShell, os usuários podem executar rapidamente comandos relacionados aos serviços da AWS sem instalar nem configurar a AWS CLI (Command Line Interface) em suas máquinas locais.  Esse serviço oferece um ambiente pronto para uso, com ferramentas comuns de desenvolvimento e gerenciamento pré-instaladas, como a AWS CLI, Python, Node.js e outras, o que facilita a interação com os serviços da AWS e a automação de tarefas.  O AWS CloudShell é integrado diretamente ao AWS Management Console e oferece uma experiência fluida para executar tarefas administrativas, explorar os serviços da AWS e gerenciar a infraestrutura.  Como o AWS CloudShell autentica os usuários automaticamente pelo login no console da AWS, ele simplifica a execução de comandos e scripts no contexto da conta da AWS deles, aumentando a produtividade e a eficiência das operações na nuvem.

</details>

## Como funciona {#how-it-works}

O pacote Linux do StackQL inclui scripts para usar o `stackql` diretamente nos ambientes de cloud shell integrados dos principais provedores de nuvem.  Para a AWS, o script é __`stackql-aws-cloud-shell.sh`__.  O uso do script __`stackql-aws-cloud-shell.sh`__ é mostrado aqui:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

O script `stackql-aws-cloud-shell.sh` baixa automaticamente o provider da AWS mais recente para o StackQL.  O código-fonte dos scripts de cloud shell incluídos está em [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Baixar o pacote Linux do `stackql` no AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Para começar, no AWS CloudShell, baixe o binário mais recente do StackQL para Linux com os seguintes comandos:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Isso baixa e extrai o binário `stackql`, junto com o script `stackql-aws-cloud-shell.sh`, para o diretório home do seu ambiente do AWS CloudShell.  Agora está tudo pronto!  

## Usar o `stackql shell` no AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

Iniciar um shell de comandos do StackQL autenticado no AWS CloudShell é tão simples quanto:  

```shell
sh stackql-aws-cloud-shell.sh
```

Agora você pode começar a executar consultas interativas da AWS baseadas em SQL com o provider do StackQL, como mostrado aqui:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Seu navegador não oferece suporte à tag video.
</video>

## Assumir uma role do IAM com o StackQL {#assuming-an-iam-role-using-stackql}

Assumir uma role do IAM na AWS permite que você execute ações e acesse recursos que talvez não conseguisse com as permissões padrão do seu usuário.  Isso é particularmente útil quando você precisa de acesso temporário a recursos ou de executar ações com permissões diferentes das da sua conta principal.  Com o StackQL, você pode assumir uma role do IAM de forma simples para consultar e gerenciar recursos da AWS com as permissões concedidas por essa role.

<details>
<summary>Entender como assumir uma role do IAM</summary>

> Assumir uma role do IAM é uma forma segura de conceder acesso temporário a recursos da AWS.  Quando você assume uma role, o AWS STS fornece credenciais de segurança temporárias que permitem acessar os serviços e recursos da AWS que a role tem permissão para usar.  Esse mecanismo é ideal para acesso entre contas, acesso temporário para usuários ou para seguir o princípio do menor privilégio.

> As roles podem ser assumidas pelo AWS Management Console, pela AWS CLI ou pelos SDKs da AWS, e diretamente no StackQL. Isso garante que usuários e aplicações tenham somente as permissões necessárias, pelo tempo necessário, o que fortalece a postura de segurança do seu ambiente da AWS.

</details>

Para assumir uma role do IAM com o StackQL no AWS CloudShell, você pode usar a opção `--role-arn` com o script `stackql-aws-cloud-shell.sh`.  Essa opção exige o Amazon Resource Name (ARN) da role que você deseja assumir.  Quando ela é fornecida, o StackQL usa o AWS Security Token Service (STS) para assumir a role especificada antes de executar qualquer consulta ou comando.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Ao assumir uma role, você pode usar o StackQL para interagir com recursos da AWS aos quais seu usuário padrão talvez não tenha acesso, o que permite gerenciar seu ambiente na nuvem de forma mais flexível e segura.  

Ao executar comandos ou scripts do StackQL com uma role do IAM assumida, verifique se a role tem as permissões necessárias para executar as ações desejadas e acessar os recursos especificados.  Essa abordagem facilita o controle de acesso granular e está alinhada às práticas recomendadas da AWS para segurança e governança.  

## Executar o `stackql exec` (operações em lote) no AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

As consultas do StackQL também podem ser executadas com o comando `exec`; essa é uma opção valiosa para:

- especificar formatos de saída alternativos, como `json` ou `csv`
- gerar arquivos de saída com os resultados das consultas do StackQL
- usar consultas do StackQL fornecidas por meio de arquivos de entrada (por convenção, arquivos `.iql`)
- executar operações complexas, incluindo o carregamento de variáveis ou o pré-processamento de consultas (com `json` ou `jsonnet`)

Exemplos de uso do `exec` no AWS CloudShell são mostrados aqui:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

O exemplo acima executa a consulta do StackQL especificada e gera um arquivo `CSV` de saída, que pode ser baixado do AWS CloudShell.  Além disso, você pode fornecer uma role do IAM com o argumento `--role-arn` para assumir outra role na sua consulta ou operação de mutação; um exemplo é mostrado aqui (neste caso, a consulta vem de um arquivo de entrada):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Mais informações sobre o comando `exec` estão [__aqui__](/command-line-usage/exec).
