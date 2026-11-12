---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - variables de session
  - set
  - sessions de provider
  - secrets
description: "SET dans StackQL : affecter des variables de session typées et configurer alias de provider, champs d'identifiants et secrets de la session via des cibles préfixées d'un dollar."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Affecte une variable de session ou configure des paramètres d'exécution pour la session en cours. Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` UNSET `]](/language-spec/unset) [[ Variables de session ]](/getting-started/session-variables) [[ Sessions de provider ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Syntaxe {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Un identifiant simple (`region`), un nom entre guillemets doubles (`"providers.saved"`) ou un chemin de configuration entre guillemets doubles et préfixé par un dollar (`"$.auth.aws.password"`).

__*literal*__  
Une chaîne de caractères, un nombre, un booléen ou `null`. Les chaînes sont entre guillemets simples. Les tableaux et les objets ne sont pas des valeurs scalaires ; conservez un document JSON sous forme de chaîne.

__*DEFAULT*__  
Réinitialise la cible, ce qui équivaut à [`UNSET`](/language-spec/unset). Un `null` explicite est une valeur stockée, pas une réinitialisation.

__*reference*__  
Une référence `"$name"` entre guillemets doubles à une variable existante, par exemple `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Facultatif, et la seule portée dont dispose StackQL. `SET SESSION name = ...` et `SET name = ...` sont la même instruction : chaque affectation appartient à la session en cours et à rien d'autre. Le mot-clé existe pour que les scripts se lisent de la même façon que sur les bases de données où `SET SESSION` et `SET GLOBAL` diffèrent. `SET GLOBAL` est analysée sans erreur mais rejetée avec `configuration scope "global" is not supported; use SESSION`, car aucun état n'est partagé entre les sessions.

Les affectations d'une même liste `SET` s'appliquent dans l'ordre et ne forment pas une transaction : si la deuxième affectation d'une liste échoue, la première reste en vigueur.

## Cibles {#targets}

| Cible | Effet |
|--|--|
| `name` ou `"name"` | Stocke une variable de session scalaire, déréférencée sous la forme `$name` dans les instructions suivantes. Les points seuls ne désignent pas de la configuration : `"providers.saved"` est une variable nommée `providers.saved`. L'ancienne écriture `"variables.name"` est un alias de `name`. |
| `"$.auth.<provider>.<field>"` | Définit un champ du contexte d'authentification du provider pour cette session. Les champs sont les clés de l'objet [`--auth`](/command-line-usage/global-flags) de ce provider, par exemple `credentialsenvvar`, `password` ou `token_url`. |
| `"$.providers.<alias>"` | Lie un alias de provider doté de ses propres identifiants, de son libellé de tenancy, de ses valeurs par défaut de paramètres et de ses réglages. La valeur est un document JSON dans une chaîne ; consultez [Sessions de provider](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Stocke un secret dans le magasin de secrets de la session. La valeur est une chaîne littérale, généralement un document JSON `auth`, référencée depuis un alias de provider via `auth_secret`. |
| `"$.secret_store"` | Sélectionne un magasin de secrets persistant du système d'exploitation, `{"backend":"os","namespace":"..."}`. Nécessite l'option d'exploitant `--allow-persistent-secrets`. |

Chaque forme n'a d'effet que sur la session en cours. Les connexions indépendantes à un serveur [`srv`](/command-line-usage/srv) ne voient pas les affectations les unes des autres, et chaque requête MCP est sa propre session ; configurez et interrogez donc dans un même lot d'instructions lorsque vous utilisez le serveur MCP.

* * *

## Exemples {#examples}

### Définir des variables de session {#set-session-variables}

Chaque instruction stocke un scalaire sous le nom indiqué, et la valeur conserve son type : `region` est une chaîne de caractères, `minimum_count` un entier et `enabled` un booléen. `null` est stocké comme une valeur ; `$optional_value` est donc ensuite résolu en `NULL` SQL au lieu d'échouer comme non défini. La dernière instruction affecte deux variables dans une même liste.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Rendre explicite la portée de session {#make-the-session-scope-explicit}

Les deux premières instructions sont identiques : `SESSION` désigne la seule portée qui existe, et les deux affectations durent jusqu'à ce que la variable soit supprimée ou que la session se termine. Utilisez le mot-clé lorsqu'un script doit se lire de la même façon que sur une base de données qui distingue les paramètres de session des paramètres globaux. L'instruction en commentaire est présentée à titre de contraste : elle n'affecte rien et échoue avec `configuration scope "global" is not supported; use SESSION`, car aucun état n'est partagé entre les sessions.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Utiliser une variable dans une requête {#use-a-variable-in-a-query}

Déréférencez une variable avec `$name`, sans guillemets, à n'importe quelle position d'expression. Les valeurs conservent leur type : les chaînes sont échappées comme des littéraux SQL, et les nombres, les booléens et `null` sont transmis tels quels. Les variables d'une clause `WHERE` sont résolues avant la planification de la requête ; elles sont donc envoyées au provider comme des littéraux.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Réinitialiser une variable {#reset-a-variable}

`DEFAULT` supprime la variable, exactement comme [`UNSET`](/language-spec/unset), de sorte qu'un `$region` ultérieur échoue avec `variable "$region" is not defined` jusqu'à ce que la variable soit de nouveau définie ; la requête en commentaire montre cet échec. Ce comportement diffère de l'affectation de `null`, qui laisse la variable en place avec une valeur `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Copier une valeur depuis une autre variable {#copy-a-value-from-another-variable}

Le membre de droite peut être une référence entre guillemets doubles à une variable existante, ce qui copie la valeur qu'elle contient à cet instant. Ici, une liaison de provider est d'abord stockée dans une variable ordinaire nommée `providers.saved` (les points n'en font pas de la configuration), puis activée en la copiant vers le chemin `$.providers.production`. Supprimer ensuite la copie enregistrée n'a pas d'effet sur la liaison active.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Remplacer un champ d'identifiants pour cette session {#override-a-credential-field-for-this-session}

Le contexte d'authentification d'un provider est l'objet qui lui est fourni via [`--auth`](/command-line-usage/global-flags) au démarrage. Un chemin sous `$.auth` remplace un champ de cet objet pour le reste de la session et laisse les autres champs inchangés. Ici, le provider GitHub est dirigé vers une autre variable d'environnement, qui contient un jeton en lecture seule ; la variable est lue dans l'environnement du processus StackQL, et les requêtes ultérieures sur `github.*` l'utilisent jusqu'à ce que le champ soit supprimé ou que la session se termine.

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### Lier un alias de provider {#bind-a-provider-alias}

Un alias de provider est une seconde instance d'un provider, configurée indépendamment et utilisée à la place du nom du provider dans les requêtes. La valeur est un document JSON dans une chaîne : `provider` désigne le provider sous-jacent, `tenancy` étiquette le compte, `auth` est un bloc d'identifiants de la même forme que `--auth`, et `params` fournit des valeurs par défaut pour des paramètres de requête tels que `region`. Les requêtes sur `production.s3.buckets` s'exécutent alors avec ces identifiants, tandis que `aws.s3.buckets` conserve la configuration de démarrage. La liste complète des clés se trouve dans [Sessions de provider](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
