---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gerenciamento de acesso
  - groups
  - diretório de principals
description: "Função GROUPS no StackQL: retornar os grupos de diretório do chamador autenticado como um array JSON quando o gerenciamento de acesso está habilitado."
image: "/img/stackql-featured-image.png"
---
Retorna as associações a grupos de diretório do chamador autenticado como um array JSON.  Requer uma sessão iniciada com o [gerenciamento de acesso](/command-line-usage/access-management) habilitado.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Gerenciamento de acesso ]](/command-line-usage/access-management)

* * * 

## Sintaxe {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Argumentos {#arguments}

Nenhum.

## Valor(es) retornado(s) {#return-values}

Retorna um array JSON de IDs de grupo do snapshot do diretório de principals obtido para a instrução atual, por exemplo `["readers"]`.  As associações são atualizadas a cada instrução, de modo que uma revogação se reflete na instrução seguinte.  A função falha em uma sessão iniciada sem `--access.config`.

* * *

## Exemplos {#examples}

### Inspecionar a identidade estabelecida {#inspect-the-established-identity}

Juntas, as duas funções mostram o ID do principal do chamador e os seus grupos de diretório atuais.  Como as associações são lidas novamente a cada instrução, executar esta instrução após uma alteração no diretório confirma que a alteração está visível para a sessão.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
