---
title: Usar o StackQL com o Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ia
  - infrastructure-as-code
  - multinuvem
description: Use o StackQL com o Claude Desktop para consultar e provisionar recursos na nuvem em linguagem natural
image: "/img/stackql-featured-image.png"
---

O StackQL se integra ao Claude Desktop por meio do Model Context Protocol (MCP), o que permite consultar e provisionar recursos na nuvem em vários provedores de nuvem usando linguagem natural. Essa combinação poderosa permite interagir com a sua infraestrutura na nuvem de forma conversacional, aproveitando internamente a interface baseada em SQL do StackQL.

Há três opções de instalação, apresentadas aqui na ordem recomendada: instalar a partir do Connector Directory da Anthropic, instalar a partir de um bundle baixado ou registrar manualmente um binário do StackQL instalado localmente.

## Instalar a partir do Connector Directory (recomendado) {#install-from-the-connector-directory-recommended}

A maneira mais fácil de usar o servidor MCP do StackQL com o Claude Desktop é instalá-lo a partir do Connector Directory da Anthropic. No Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**, pesquise **StackQL** e clique em **Install**. Não há nada para baixar e não é necessária uma instalação separada do StackQL.

![StackQL no Connector Directory da Anthropic](/img/stackql-anthropic-directory-listing.png)

Você também pode ver a listagem diretamente em [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Isso instala o servidor MCP do StackQL em processo para a sua plataforma: a listagem disponibiliza um bundle multiplataforma combinado que contém binários assinados para macOS universal, Windows x64 e Linux x64 e arm64, e o Claude Desktop seleciona automaticamente o binário certo para a sua máquina. Para Windows ARM64 nativo, instale o bundle `windows-arm64` separado a partir dos [downloads abaixo](#install-from-a-downloaded-bundle). Os binários do Windows são assinados com Authenticode e o binário do macOS é notarizado pela Apple - os mesmos binários da versão independente do StackQL.

![Instalação do conector do StackQL](/img/stackql-anthropic-directory-install.png)

Um conector instalado pelo diretório herda o ambiente do Claude Desktop, portanto defina as variáveis de ambiente dos seus providers no nível do sistema operacional antes de iniciar o Claude Desktop - consulte [Credenciais específicas de cada provider](#provider-specific-credentials). O Claude Desktop captura o ambiente na inicialização: depois de alterar variáveis, você precisa encerrar completamente o Claude Desktop (incluindo o ícone da bandeja do sistema no Windows) e iniciá-lo novamente. Pedir ao agente que execute [`reload_credentials`](/mcp/reload_credentials) informa o status das credenciais de cada provider e é a maneira mais rápida de confirmar o que o servidor consegue e não consegue ver.

O Claude Desktop anuncia a capacidade de elicitação do MCP, portanto o modo padrão do servidor (`safe`) solicitará a sua aprovação a cada mutação - consulte [Modos do servidor](/command-line-usage/mcp#server-modes).

## Instalar a partir de um bundle baixado {#install-from-a-downloaded-bundle}

Use esta opção de instalação para fixar uma versão específica, instalar sem acesso ao Connector Directory ou verificar você mesmo as somas de verificação antes de instalar.

O MCP Bundle (`.mcpb`) pré-compilado é um pacote instalável com um clique que contém o binário `stackql` assinado e a configuração do servidor. Não é necessária uma instalação separada do StackQL.

Baixe o bundle para a sua plataforma a partir da versão mais recente:

| Plataforma | Bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (nativo) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Em seguida, no Claude Desktop: **Settings -> Extensions -> Install Extension** e selecione o arquivo baixado. O binário incorporado é assinado com Authenticode (Windows) e notarizado pela Apple (macOS); cada bundle tem uma soma de verificação `.sha256` ao lado, na [página da versão](https://github.com/stackql/stackql/releases/latest). Para verificar antes de instalar:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

O servidor também está listado no [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) como `io.github.stackql/stackql-mcp`, que fixa essas somas de verificação.

## Configuração manual {#manual-configuration}

Se você já tem o StackQL instalado, pode registrar o servidor MCP do StackQL diretamente no arquivo de configuração do Claude Desktop.

### Pré-requisitos {#prerequisites}

Antes de configurar o StackQL com o Claude Desktop, verifique se você tem:

1. **StackQL instalado** - Baixe e instale o StackQL a partir de [stackql.io](/installing-stackql) ou use um gerenciador de pacotes
2. **Claude Desktop** - Baixe-o do [site da Anthropic](https://claude.ai/desktop)
3. **Credenciais de provedores de nuvem** - Chaves de API ou credenciais dos provedores de nuvem com que você quer trabalhar

Verifique se o StackQL está no PATH do sistema executando:

```bash
stackql --version
```

### Configurar o servidor MCP {#setting-up-the-mcp-server}

Para que o Claude Desktop possa se comunicar com o StackQL, você precisa configurar o servidor MCP no arquivo de configuração do Claude Desktop.

#### Windows {#windows}

Edite o arquivo de configuração localizado em:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Edite o arquivo de configuração localizado em:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Edite o arquivo de configuração localizado em:
```
~/.config/Claude/claude_desktop_config.json
```

### Exemplo de configuração {#configuration-example}

Adicione a configuração a seguir ao seu arquivo `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Você só precisa incluir as variáveis de ambiente dos provedores de nuvem que pretende usar. Remova da configuração as credenciais dos providers que você não usar.
:::

:::warning
Nunca faça commit do seu arquivo `claude_desktop_config.json` com credenciais reais no controle de versão. Use práticas seguras de gerenciamento de credenciais.
:::

O Claude Desktop anuncia a capacidade de elicitação do MCP, portanto o modo padrão do servidor (`safe`) solicitará a sua aprovação a cada mutação. Mude para `mode: read_only` para ter acesso somente ao inventário ou para `mode: full_access` para dispensar as solicitações de aprovação - consulte [Modos do servidor](/command-line-usage/mcp#server-modes).

### Usar um arquivo de credenciais (`--env.file`) {#using-a-credentials-file---envfile}

Uma alternativa ao bloco `env` - e, no Windows, a recomendada - é manter as credenciais em um arquivo no estilo dotenv e passá-lo com a opção [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

Com um arquivo de credenciais mínimo:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Por que isso é melhor que o bloco `env`:

- Alterações no bloco `env` exigem uma reinicialização completa do Claude Desktop (incluindo encerrar o ícone da bandeja do sistema) antes que o subprocesso MCP as veja, e no Windows o `setx` grava no registro - ele nunca chega ao processo do Desktop em execução.
- O arquivo do `--env.file` pode ser criado ou rotacionado a qualquer momento - ele nem precisa existir quando o servidor inicia - e pedir ao agente que execute [`reload_credentials`](/mcp/reload_credentials) carrega as alterações sem nenhuma reinicialização.

:::note

`--env.file` e `reload_credentials` estão disponíveis nas versões do StackQL posteriores à `v0.10.542`.

:::

### Credenciais específicas de cada provider {#provider-specific-credentials}

Diferentes provedores de nuvem exigem métodos de autenticação diferentes; consulte a documentação dos providers ([https://stackql.io/providers](/providers)) para conhecer as variáveis de ambiente necessárias para o provider ou os providers desejados.


### Reiniciar o Claude Desktop {#restarting-claude-desktop}

Depois de atualizar o arquivo de configuração, reinicie o Claude Desktop para carregar a nova configuração do servidor MCP. O servidor MCP do StackQL será iniciado automaticamente quando você começar uma nova conversa.

## O que você pode fazer {#what-you-can-do}

Depois de configurado, você pode usar linguagem natural no Claude Desktop para:

### Consultar recursos na nuvem {#query-cloud-resources}

Peça ao Claude que obtenha informações sobre a sua infraestrutura na nuvem:

- "Mostre todas as minhas instâncias de computação do GCP na região us-central1"
- "Liste todos os buckets do S3 na minha conta da AWS"
- "Quais máquinas virtuais do Azure estão em execução na minha assinatura?"
- "Mostre todos os pods do Kubernetes no namespace default"

### Provisionar recursos {#provision-resources}

Crie e implante recursos na nuvem usando comandos conversacionais:

- "Crie um bucket do S3 chamado 'my-data-bucket' em us-west-2"
- "Implante um bucket do Cloud Storage do GCP com o versionamento habilitado"
- "Inicie uma instância EC2 pequena em us-east-1"
- "Crie um grupo de recursos do Azure em westus2"

### Analisar e auditar {#analyze-and-audit}

Realize auditorias de segurança e verificações de conformidade:

- "Mostre todos os buckets do S3 com acesso público"
- "Liste os usuários do IAM que não fazem login há 90 dias"
- "Encontre todas as instâncias do GCP sem os rótulos adequados"
- "Identifique as VMs do Azure sem backup habilitado"

### Operações entre nuvens {#cross-cloud-operations}

Consulte e compare recursos em vários provedores de nuvem:

- "Mostre todas as instâncias de computação na AWS, no GCP e no Azure"
- "Compare os custos de armazenamento entre o AWS S3 e o GCP Cloud Storage"
- "Liste todos os bancos de dados em todos os provedores de nuvem"

### Infraestrutura como código {#infrastructure-as-code}

Gere e execute consultas de implantação de infraestrutura:

- "Crie um stack de recursos que inclua uma VPC, uma sub-rede e uma instância EC2"
- "Implante um cluster do GKE completo com pools de nós"
- "Configure uma implantação multirregião na AWS"

### Modificar recursos {#modify-resources}

Atualize recursos na nuvem existentes:

- "Adicione tags a todas as instâncias EC2 sem tags"
- "Habilite a criptografia do bucket do S3 'my-bucket'"
- "Atualize o tipo de máquina da instância do GCP 'web-server-1'"
- "Mude a ACL do meu bucket do Cloud Storage para privada"

### Excluir recursos {#delete-resources}

Remova recursos na nuvem quando não forem mais necessários:

- "Exclua o bucket do S3 'temp-bucket'"
- "Remova todas as instâncias EC2 interrompidas"
- "Limpe os discos do GCP não utilizados"

## Exemplo de conversa {#example-conversation}

Veja um exemplo de como você pode interagir com o Claude Desktop usando o StackQL:

**Você:** "Você pode me mostrar todas as minhas instâncias de computação do GCP?"

**Claude:** *Executa uma consulta do StackQL e retorna os resultados em uma tabela formatada que mostra os nomes das instâncias, as zonas, os tipos de máquina e o status*

**Você:** "Crie um novo bucket do Cloud Storage chamado 'analytics-data-2025' na região us-central1 com acesso uniforme no nível do bucket"

**Claude:** *Executa uma instrução INSERT do StackQL para criar o bucket e confirma a criação*

**Você:** "Agora liste todos os meus buckets do Cloud Storage"

**Claude:** *Executa uma consulta SELECT e mostra todos os buckets, incluindo o recém-criado*

## Dicas e práticas recomendadas {#tips-and-best-practices}

1. **Seja específico**: quanto mais específica for a sua solicitação, melhores serão os resultados. Inclua nomes de recursos, regiões e atributos específicos sempre que possível.

2. **Use linguagem natural**: você não precisa conhecer a sintaxe SQL - o Claude traduz as suas solicitações em linguagem natural para consultas do StackQL.

3. **Revise antes de executar**: em operações destrutivas (DELETE, UPDATE), o Claude normalmente mostra o que será executado antes de prosseguir.

4. **Operações em várias etapas**: você pode encadear operações em uma única conversa. O Claude mantém o contexto ao longo de toda a sessão.

5. **Tratamento de erros**: se uma consulta falhar, o Claude pode ajudar a solucionar o problema e sugerir correções com base na mensagem de erro.

6. **Explore as capacidades**: pergunte ao Claude "O que posso fazer com o StackQL?" para receber sugestões específicas para os providers que você configurou.

## Solução de problemas {#troubleshooting}

### O servidor MCP não inicia {#mcp-server-not-starting}

Se o Claude Desktop não conseguir se conectar ao servidor MCP do StackQL:

1. Verifique se o StackQL está no seu PATH
2. Verifique se o seu arquivo `claude_desktop_config.json` é um JSON válido
3. Confirme que o caminho do arquivo na configuração está correto para o seu sistema operacional
4. Consulte os logs do Claude Desktop em busca de mensagens de erro

### Erros de autenticação {#authentication-errors}

Se você receber erros de autenticação:

1. Verifique se as credenciais do seu provedor de nuvem estão corretas
2. Confirme que os arquivos de credenciais existem nos caminhos especificados
3. Verifique se as variáveis de ambiente estão formatadas corretamente na configuração - pedir ao agente que execute [`reload_credentials`](/mcp/reload_credentials) informa o status das credenciais de cada provider e é a maneira mais rápida de ver qual variável está faltando
4. Confirme que as suas credenciais têm as permissões necessárias

### Erros de permissão {#permission-errors}

Se as operações falharem por falta de permissões:

1. Verifique se a sua conta de serviço ou usuário do IAM tem as permissões necessárias
2. Confira as políticas do IAM específicas de cada provedor de nuvem
3. Confirme que os serviços de API estão habilitados nos seus projetos na nuvem

## Saiba mais {#learn-more}

- [Referência do comando MCP do StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Especificação da linguagem](/language-spec/select)
- [Documentação do Model Context Protocol](https://modelcontextprotocol.io)

## Considerações de segurança {#security-considerations}

:::caution
- Armazene as credenciais com segurança e nunca faça commit delas no controle de versão
- Use princípios de acesso com privilégio mínimo para as contas de serviço
- Rotacione regularmente as credenciais e as chaves de API
- Monitore os logs do servidor MCP para detectar atividades incomuns
- Considere usar as ferramentas de gerenciamento de credenciais dos provedores de nuvem (por exemplo, Secret Manager, Parameter Store)
:::
