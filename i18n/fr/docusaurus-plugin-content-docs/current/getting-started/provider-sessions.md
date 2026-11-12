---
title: Sessions de provider et secrets
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - sessions de provider
  - multicompte
  - identifiants
  - secrets
  - keychain
description: "Sessions de provider dans StackQL : alias avec identifiants, tenancy et paramètres par défaut propres, remplacement de champs d'identifiants, secrets en mémoire ou dans un magasin de secrets de l'OS."
image: "/img/stackql-featured-image.png"
---

Voir aussi :  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Variables de session ]](/getting-started/session-variables) [[ Options globales ]](/command-line-usage/global-flags)

Une session peut lier un provider sous un alias doté de ses propres identifiants, de son libellé de tenancy, de ses valeurs par défaut de paramètres et de ses limites de concurrence des requêtes HTTP, de sorte que plusieurs comptes ou tenants d'un même provider peuvent être interrogés dans une seule session, et dans une seule instruction. Les champs d'identifiants peuvent aussi être remplacés pour la session, et les secrets peuvent être conservés dans la mémoire de la session ou, après activation explicite par l'exploitant, dans le magasin de secrets du système d'exploitation. Tout cela se configure avec [`SET`](/language-spec/set) et [`UNSET`](/language-spec/unset) et est disponible à partir de StackQL `v1.0`.

Tout ce qui est décrit ici est limité à la session en cours. Des connexions indépendantes à un serveur `stackql srv` ne partagent ni liaisons ni secrets, et chaque requête MCP constitue sa propre session ; configurez et interrogez donc dans un même lot d'instructions lorsque vous utilisez le serveur MCP.

## Alias de provider {#provider-aliases}

`SET "$.providers.<alias>"` lie un alias à un provider. La valeur est un document JSON contenu dans une chaîne de caractères :

| Clé | Obligatoire | Description |
|--|--|--|
| `provider` | Oui | Le provider canonique vers lequel l'alias est résolu, par exemple `aws`. |
| `tenancy` | Oui | Un libellé de regroupement, tel qu'un nom de compte ou de tenant. StackQL ne le vérifie pas auprès du provider. |
| `auth` | L'un de `auth` ou `auth_secret` | Un document d'authentification de même forme que l'entrée du provider dans l'objet [`--auth`](/command-line-usage/global-flags). L'alias n'emprunte pas les identifiants du provider canonique. |
| `auth_secret` | L'un de `auth` ou `auth_secret` | Le nom d'un [secret de session](#session-secrets) contenant le document d'authentification. |
| `params` | Non | Valeurs par défaut des paramètres déclarés du provider, y compris les paramètres de serveur déclarés. Un prédicat dans la requête remplace une valeur par défaut. |
| `tuning` | Non | Limites `max_concurrent` et `max_per_host` pour les requêtes HTTP de l'alias. Les alias qui partagent un provider et une tenancy partagent ces contrôles et doivent déclarer un réglage identique. |
<br/>
L'exemple suivant lie deux comptes AWS sous des alias distincts, chacun lisant ses clés dans des variables d'environnement de noms différents, et interroge les deux dans une seule instruction :

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Les références à des variables d'environnement sont résolues à partir de l'environnement du processus StackQL ; il n'existe pas d'environnement local à la session. Les identifiants AWS temporaires nécessitent aussi le champ du jeton de session ou la référence à sa variable d'environnement.

Un alias se comporte comme son provider pour les instructions de métadonnées et de données : `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` et `EXEC` acceptent tous l'alias à la place du nom du provider. Les alias ne s'étendent pas aux relations natives intégrées ni aux relations IaC en préversion. Les branches d'un `UNION ALL` qui utilisent des alias différents s'exécutent séquentiellement.

Chaque requête prend un instantané détaché des identifiants de l'alias. La modification d'une liaison affecte les requêtes ultérieures, pas celle qui est déjà en cours d'exécution. `UNSET "$.providers.<alias>"` supprime la liaison.

## Remplacer des champs d'identifiants {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` modifie un champ du contexte d'authentification d'un provider pour la session ; les noms de champ sont les clés de l'entrée `--auth` du provider :

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

Comme pour les alias, la modification s'applique aux requêtes démarrées après le `SET`. Les requêtes sur un provider dont l'authentification a été supprimée, ou dont l'alias n'a pas de `auth` utilisable, échouent avant l'envoi de toute requête HTTP.

## Secrets de session {#session-secrets}

Par défaut, chaque session dispose d'un magasin de secrets uniquement en mémoire, sans aucune configuration. `SET "$.secrets.<name>"` stocke une chaîne littérale, généralement un document d'authentification JSON, et un alias de provider y fait référence avec `auth_secret` :

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

La rotation d'un secret affecte les requêtes ultérieures ; sa suppression fait échouer les utilisations ultérieures. Une nouvelle session reçoit un magasin neuf et vide, et la fin de la session le libère. `UNSET "$.secret_store"` abandonne la liaison de magasin en cours et repart d'une mémoire de session vide.

L'utilisation normale des identifiants, ainsi que les instructions `SET` qui stockent des variables ou de la configuration, n'écrivent pas de secrets dans le backend SQL. Placer des données sensibles dans une variable ou une table ordinaire relève de la responsabilité de l'appelant.

## Magasins de secrets persistants du système d'exploitation {#persistent-os-secret-stores}

Le stockage persistant est désactivé par défaut et ne peut être activé que par l'exploitant, en démarrant StackQL avec `--allow-persistent-secrets` ; SQL ne peut pas l'accorder. Une fois l'option définie, une session sélectionne un magasin du système d'exploitation avec :

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

Le magasin est le Keychain de macOS, le Secret Service de Linux (`secret-tool` avec un service de session opérationnel) ou le Credential Manager de Windows. Seul le backend `os` est accepté : les magasins sous forme de fichier plat, les chemins de fichier et tout repli implicite sur un fichier sont rejetés même lorsque l'option est définie, et un mécanisme du système d'exploitation indisponible provoque un échec plutôt qu'un repli sur un fichier.

Les valeurs écrites dans un magasin du système d'exploitation survivent à la session SQL et se suppriment explicitement avec `UNSET "$.secrets.<name>"`. La réinitialisation de la liaison de magasin ne supprime pas les valeurs déjà persistées. L'accès au magasin est régi par l'identité du processus StackQL auprès du système d'exploitation ; un espace de noms n'est pas un contrôle d'accès par utilisateur, ne traitez donc pas cette activation explicite comme un coffre-fort multiutilisateur.

## Valeurs de secrets dans SQL {#secret-values-in-sql}

Les valeurs de secrets ne sont jamais déréférencées dans SQL, sauf si l'exploitant démarre StackQL avec `--allow-secret-dereference`. Avec cette option, des références telles que `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` et `"$providers.production.auth.credentials"` sont résolues dans les expressions :

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Dans une session démarrée sans cette option, la même instruction est refusée avant toute évaluation, comme le montre la copie mise en commentaire. L'option vaut reconnaissance du fait que des valeurs explicitement déréférencées peuvent se retrouver dans la zone de transit SQL, les résultats, les journaux et les artefacts de requête persistés. L'authentification normale d'un provider ne la nécessite pas.

## Permissions {#permissions}

Lorsque la [gestion des accès](/command-line-usage/access-management) est activée, la lecture des variables et des secrets nécessite `session/variables:read`, et `SET` ou `UNSET` nécessite `session/config:write`. Un alias résolu est autorisé en tant que son provider canonique avant l'exécution. La propriété et l'octroi de droits par variable et par secret ne sont pas implémentés.
