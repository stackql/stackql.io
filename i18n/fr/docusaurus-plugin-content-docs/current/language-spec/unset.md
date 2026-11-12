---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de session
  - unset
  - sessions de provider
  - secrets
description: "UNSET dans StackQL : supprimer des variables de session ou effacer les alias de provider, champs d'identifiants et secrets configurés avec SET pour la session en cours."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Supprime une variable de session ou efface la configuration d'exécution définie avec [`SET`](/language-spec/set) pour la session en cours. Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` SET `]](/language-spec/set) [[ Variables de session ]](/getting-started/session-variables) [[ Sessions de provider ]](/getting-started/provider-sessions)

* * * 

## Syntaxe {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Toute cible acceptée par `SET` : un nom de variable simple ou entre guillemets doubles, ou un chemin de configuration entre guillemets doubles et préfixé par un dollar.

__*SESSION*__  
Facultatif, et la seule portée qui existe : `UNSET SESSION name` et `UNSET name` sont la même instruction, car chaque variable et chaque paramètre de configuration appartient à la session en cours. `UNSET GLOBAL` est rejetée avec `configuration scope "global" is not supported; use SESSION`.

`UNSET name` équivaut à `SET name = DEFAULT`. Les cibles d'une même liste sont supprimées dans l'ordre, et supprimer un nom qui n'est pas défini n'est pas une erreur. Déréférencer une variable après sa suppression échoue avant l'envoi de toute requête au provider.

| Cible | Effet |
|--|--|
| `name` | Supprime la variable de session. |
| `"$.auth.<provider>.<field>"` | Supprime ce champ du contexte d'authentification de session du provider. La valeur en vigueur avant le remplacement n'est pas restaurée. |
| `"$.providers.<alias>"` | Supprime la liaison de l'alias de provider. |
| `"$.secrets.<name>"` | Supprime le secret du magasin de la session. Pour un magasin persistant du système d'exploitation, cela supprime la valeur stockée. |
| `"$.secret_store"` | Abandonne la liaison du magasin actuel et repart d'une mémoire de session vide. Les valeurs déjà persistées dans un magasin du système d'exploitation ne sont pas supprimées. |

* * *

## Exemples {#examples}

### Supprimer une variable {#remove-a-variable}

La variable est supprimée de la session ; la requête en commentaire échoue donc avec `variable "$region" is not defined` au lieu de s'exécuter avec une valeur obsolète ou vide. Définissez de nouveau le nom pour le réutiliser.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Supprimer plusieurs cibles {#remove-several-targets}

Une même instruction peut supprimer à la fois des variables et des chemins de configuration, et les cibles sont traitées dans l'ordre. Ici, une variable simple, une variable dont le nom contient des points et un remplacement d'identifiants sont supprimés dans une même liste.

```sql
UNSET region, "providers.saved", "$.auth.github.credentialsenvvar";
```

### Rendre explicite la portée de session {#make-the-session-scope-explicit}

Ces deux instructions font la même chose : `SESSION` désigne la seule portée qui existe. La forme explicite convient aux scripts qui s'exécutent aussi sur des bases de données qui distinguent l'état de session de l'état global. L'instruction en commentaire échoue car aucun état partagé n'existe.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Effacer un remplacement d'identifiants {#clear-a-credential-override}

Le champ défini précédemment avec `SET "$.auth.github.credentialsenvvar"` est supprimé du contexte d'authentification de session du provider GitHub, et les autres champs de ce contexte restent inchangés. La valeur en vigueur avant le remplacement n'est pas restaurée ; définissez donc de nouveau le champ si le provider en a encore besoin.

```sql
UNSET "$.auth.github.credentialsenvvar";
```

### Supprimer un alias de provider et son secret {#remove-a-provider-alias-and-its-secret}

L'alias `production` a été lié avec `SET "$.providers.production"` et fait référence, via `auth_secret`, à un secret stocké dans `$.secrets.production`. Supprimer les deux dans une même instruction retire la liaison, de sorte que `production.s3.buckets` n'est plus une relation valide, et supprime le secret du magasin de la session. `SESSION` est facultatif ici, comme partout.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
