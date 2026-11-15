---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gerenciamento de acesso
  - whoami
  - principal
description: "Função WHOAMI no StackQL: retornar o ID do principal do chamador autenticado quando o gerenciamento de acesso está habilitado."
image: "/img/stackql-featured-image.png"
---
Retorna o ID do principal do chamador autenticado.  Requer uma sessão iniciada com o [gerenciamento de acesso](/command-line-usage/access-management) habilitado.  Disponível a partir do StackQL `v1.0`.

Veja também:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Gerenciamento de acesso ]](/command-line-usage/access-management)

* * * 

## Sintaxe {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Argumentos {#arguments}

Nenhum.

## Valor(es) retornado(s) {#return-values}

Retorna a string do ID do principal estabelecido para a instrução atual.  A função falha em uma sessão iniciada sem `--access.config`.

* * *

## Exemplos {#examples}

### Inspecionar a identidade estabelecida {#inspect-the-established-identity}

As duas funções não recebem argumentos e descrevem o chamador que o servidor autenticou para esta instrução.  `whoami()` retorna o ID do principal e `groups()`, os grupos de diretório do chamador, de modo que executá-las juntas é uma verificação rápida de que um token ou certificado corresponde ao principal esperado antes de executar qualquer outra coisa.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
