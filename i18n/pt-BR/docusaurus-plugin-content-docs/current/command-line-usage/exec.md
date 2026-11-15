---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "O comando stackql exec executa uma instrução do StackQL ou um script de instruções, com opções para formato de saída, autenticação e registro de providers."
image: "/img/stackql-featured-image.png"
---

Comando usado para executar uma consulta do StackQL, seja como uma instrução, seja como um lote de instruções de um script do StackQL (arquivo IQL).  

* * * 

### Sintaxe {#syntax}

`stackql exec [query] [flags]`

* * *

### Argumentos {#arguments}

| Argumento | Descrição | Exemplo |
|--|--|--|
|`query`|Consulta do StackQL a ser realizada<br/>ou comando a ser executado | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Observe que a consulta deve estar entre aspas simples ou duplas_ |    


### Opções {#flags}

| Opção | Descrição |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Modo de saída, os valores válidos incluem: `table` (valor padrão), `json`, `jsonl` (JSON delimitado por quebras de linha, `ndjson` é aceito como alias), `csv`, `text` e `otel` (registros de log do OpenTelemetry, consulte [Modos de saída](/getting-started/output-modes#otel-output)), exemplo: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Executa a consulta a partir do arquivo especificado, se necessário; se ele for especificado, qualquer consulta fornecida como argumento é ignorada, exemplo: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Dados de origem para o comando, usando um arquivo `json` ou `jsonnet`, exemplo: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Opção de execução de teste; somente o pré-processador é executado e a saída é retornada |
|<span class="nowrap">`--var`</span>|Variáveis externas (podem ser variáveis de ambiente ou segredos) usadas pelos arquivos jsonnet especificados com a opção `--iqldata`, exemplo `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Arquivo no qual os resultados são gravados, se necessário (o comportamento padrão é exibir os resultados somente no console), exemplo `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Exibe as informações de ajuda|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Executa as consultas no modo detalhado, com saída adicional enviada para stdout; se a opção `-f` for selecionada, essas informações de log adicionais são gravadas no arquivo de saída junto com os resultados da consulta|
<br/>
<br/>
> consulte [Opções globais](/command-line-usage/global-flags) para ver opções adicionais


* * *

### Exemplos {#examples}

Exibir os resultados da consulta no console em formato de tabela:
```shell
stackql exec "SHOW services IN google"
```

Exibir os resultados da consulta no console em formato json:
```shell
stackql exec "SHOW services IN google" -o json
```

Gravar os resultados da consulta em um arquivo em formato json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Executar uma consulta a partir de um arquivo de entrada (arquivo IQL), gravando os resultados em um arquivo em formato json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Status de saída {#exit-status}

`exec` termina com o status `0` depois que a entrada é processada; os erros de instruções individuais são gravados no console, portanto os scripts devem inspecionar a saída.  A partir da `v1.0`, ele termina com o status `1` quando uma instrução de configuração [`SET`](/language-spec/set) ou [`UNSET`](/language-spec/unset) falha, quando o [gerenciamento de acesso](/command-line-usage/access-management) está habilitado e uma instrução é recusada ou quando uma execução de IaC em versão prévia informa um recurso com falha.
