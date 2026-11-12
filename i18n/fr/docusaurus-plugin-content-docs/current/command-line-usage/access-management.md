---
title: Gestion des accès
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestion des accès
  - authentification
  - autorisation
  - jwt
  - mtls
  - jeton bearer
description: "Gestion des accès des appelants StackQL, à activation explicite : authentification par JWT, jeton bearer, TLS mutuel ou identité locale de l'OS, annuaire de principaux et de groupes, règles de politique autorisant les instructions."
image: "/img/stackql-featured-image.png"
---

La gestion des accès des appelants authentifie l'appelant d'une session StackQL et autorise chaque instruction au regard d'une politique.  Elle est indépendante des identifiants des providers, qui restent fournis par `--auth`, par des variables d'environnement ou par des [sessions de provider](/getting-started/provider-sessions).  Elle est désactivée par défaut, et les déploiements qui ne l'activent pas se comportent comme auparavant.  Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[ Options globales ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Activer la gestion des accès {#enabling-access-management}

La gestion des accès s'active en démarrant StackQL avec l'option globale `--access.config` pointant vers un fichier de configuration JSON.  L'exemple démarre un shell interactif en mode protégé ; `exec` et `mcp` s'activent de la même manière.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` prend une enveloppe JSON avec une seule clé `path` ; un simple chemin de fichier est également accepté.  La configuration est chargée une seule fois au démarrage, effectuez donc la rotation des clés ou de la politique par un redémarrage contrôlé.

Tant que la gestion des accès est activée, seules les commandes `exec`, `shell`, `mcp` et `version` s'exécutent ; les autres commandes, y compris `registry`, refusent de s'exécuter plutôt que de contourner le chemin protégé.  `srv` et le type de serveur MCP `reverse_proxy` refusent également, car la dépendance du protocole filaire PostgreSQL n'expose pas encore de preuve d'authentification par connexion.  Les instructions SQL `AUTH` et `AUTH REVOKE` sont refusées en mode protégé ; provisionnez plutôt les identifiants des providers par la configuration de l'hôte.  Un `exec` protégé qui échoue renvoie le code de sortie `1` après avoir affiché l'erreur.

## Modes d'authentification {#authentication-modes}

Le fichier de configuration sélectionne un `type` : `jwt` (valeur par défaut s'il est omis), `bearer`, `mtls` ou `local_os`.  La configuration est du JSON strict : les champs inconnus et les champs appartenant à un autre mode sont rejetés.  Tous les modes partagent les mêmes `rules` et le même [annuaire des principaux](#principal-directory).

### JWT {#jwt}

L'appelant présente un jeton signé, qui est vérifié hors ligne avec les clés épinglées dans la configuration.  Une configuration minimale nomme l'émetteur et l'audience que le jeton doit porter, liste une clé publique et accorde au groupe `readers` l'accès en lecture :

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

L'émetteur, l'audience et les clés publiques sont épinglés dans la configuration ; il n'y a pas de découverte OIDC.  Les algorithmes acceptés sont `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` et `ES512`, et les clés RSA doivent compter au moins 2048 bits.  Chaque clé est liée à l'algorithme configuré pour elle.  Les jetons dont l'ID de clé est inconnu, la signature invalide, l'expiration absente ou dépassée, le `nbf` ou le `iat` dans le futur, ou l'émetteur ou l'audience incorrects sont rejetés.  Les revendications de groupe et les URL portées par le jeton ne servent ni à établir la confiance ni à déterminer l'appartenance à l'annuaire.

### Jeton bearer statique {#static-bearer-token}

L'appelant présente un jeton opaque, et la configuration liste l'empreinte SHA-256 de chaque jeton accepté avec le sujet qu'il identifie.  Ce mode convient à un petit ensemble fixe d'identités de service, lorsque l'exploitation d'un émetteur de jetons ne se justifie pas :

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Seule l'empreinte SHA-256 de chaque jeton est configurée, et les empreintes doivent être uniques.  Modifier les empreintes configurées nécessite un redémarrage.

### TLS mutuel {#mutual-tls}

L'appelant est identifié par le certificat client qu'il présente sur le transport MCP HTTPS.  La configuration contient la CA qui doit avoir émis le certificat et indique quel nom alternatif du sujet (SAN) porte l'identité :

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

La chaîne de certificats du client est vérifiée avec `client_ca_pem` et le sujet de l'identité est tiré d'exactement un SAN du type configuré, `uri_san` ou `dns_san`.  Il n'y a pas de repli sur le Common Name.  Le TLS mutuel n'est disponible que sur le transport MCP HTTPS.

### Identité locale de l'OS {#local-os-identity}

Aucun identifiant n'est échangé : le sujet est l'identité du système d'exploitation sous laquelle s'exécute le processus StackQL.  La configuration ne nécessite qu'un libellé `issuer` et les règles :

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Le sujet de l'identité est l'UID Unix effectif ou le SID Windows du processus StackQL, y compris les UID numériques de conteneur sans entrée passwd.  Aucun identifiant n'est présenté et `STACKQL_CALLER_TOKEN` est ignoré.  Ce mode est uniquement local : il est refusé pour les appelants HTTP.  L'appartenance à des groupes du système d'exploitation n'accorde aucun accès ; seule l'appartenance à l'annuaire le fait.

### Échéance de session {#session-deadline}

Par défaut, les requêtes n'ont pas d'échéance dérivée de l'authentification.  Les identifiants sont vérifiés et l'appartenance à l'annuaire est actualisée à chaque nouvelle instruction : un identifiant expiré ne peut donc pas lancer de nouveau travail, mais l'expiration n'interrompt pas une instruction déjà autorisée.  Définissez `"enforce_session_deadline": true` pour annuler à `max_session_seconds` (valeur par défaut `300`, maximum `86400`), échéance raccourcie par l'expiration du JWT ou du certificat.  Cela ne renouvelle pas les jetons.

## Annuaire des principaux {#principal-directory}

Les identités sont associées à des principaux, et les principaux à des groupes, dans quatre tables du backend SQLite ou PostgreSQL configuré :

| Table | Contenu |
|--|--|
| `stackql_access_principals` | ID du principal et objet JSON d'attributs de type chaîne de caractères.  Un attribut `tenant` sert d'espace de noms à l'état IaC en préversion. |
| `stackql_access_identities` | Correspondance entre une paire `(issuer, subject)` vérifiée et l'ID du principal. |
| `stackql_access_groups` | ID de groupe. |
| `stackql_access_memberships` | Liaisons entre principal et groupe. |

Provisionnez les tables, les identités et les appartenances par une connexion d'administration de base de données de confiance avant de servir des requêtes protégées :

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Utilisez l'émetteur et le sujet réels du mode : pour l'identité locale de l'OS, ce peut être `('this-host', '1000')`, et pour le TLS mutuel `('client-certificates', 'spiffe://example/alice')`.  Sélectionnez le backend explicitement :

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

L'annuaire produit un instantané immuable pour chaque instruction.  La révocation est un `DELETE FROM stackql_access_memberships` sur la connexion d'administration ; l'instruction suivante voit le nouvel instantané.  Les identités inconnues, un schéma absent, des données mal formées et les indisponibilités de l'annuaire entraînent tous un refus d'accès.  Le SQL protégé ordinaire ne peut pas accéder aux tables de l'annuaire.  Réservez les écritures d'appartenance à la connexion d'administration, protégez le fichier de base de données et utilisez des privilèges de base de données distincts pour l'administration et pour l'exécution lorsque le backend le permet.

## Règles de politique {#policy-rules}

Chaque règle accorde une `action` sur une `resource` à un `subject` de la forme `group:<group id>`.  Utilisez les identifiants compilés de méthode et d'opération issus de documents de provider de confiance ; un caractère générique d'opération accorde les méthodes de mutation aussi bien que les lectures.

Le groupe `readers` détient implicitement la capacité SQL de lecture sûre de l'hôte.  Les lectures effectuées par des principaux extérieurs à ce groupe nécessitent `host/sql/read`, et les autres instructions nécessitent `execute` sur `host/sql/<statement kind>`.  Les opérations de provider et l'utilisation des identifiants nécessitent toujours des règles explicites, et une autorisation accordée sur une collection n'accorde pas ses méthodes sous-jacentes.  Vérifications supplémentaires de l'hôte :

| Ressource | Action | Portée |
|--|--|--|
| `host/sql/read` | `execute` | Instructions de lecture en dehors du groupe de lecteurs implicite |
| `host/sql/exec` | `execute` | `EXEC` natif, y compris les `EXEC` imbriqués |
| `host/iac/run`, `host/iac/destroy` | `execute` | Les relations IaC en préversion, malgré leur syntaxe `SELECT` |
| `host/function/<name>` | `execute` | Fonctions hors de la courte liste d'autorisation des fonctions réputées pures |
| `host/backend` | `read` | Relations opaques du backend, stockées et externes |
| `host/credentials` | `reload` | L'outil MCP `reload_credentials` |
| `session/variables` | `read` | Déréférencement des [variables de session](/getting-started/session-variables) et des secrets |
| `session/config` | `write` | `SET` et `UNSET` |

L'expansion des requêtes stockées est vérifiée avant l'exécution, et la réutilisation des caches de plans et de lignes est désactivée sur les chemins protégés.

## Points d'entrée {#entry-points}

| Authentification | `exec`, `shell`, MCP stdio | MCP HTTPS |
|--|--|--|
| JWT | Variable d'environnement `STACKQL_CALLER_TOKEN` | En-tête de requête `Authorization: Bearer` |
| Jeton bearer statique | Variable d'environnement `STACKQL_CALLER_TOKEN` | En-tête de requête `Authorization: Bearer` |
| OS local | UID ou SID effectif du processus ; aucun identifiant | Refusé |
| TLS mutuel | Refusé : aucune connexion TLS vérifiée | Chaîne client vérifiée et un SAN configuré |

Fournissez les identifiants locaux de type jeton par l'environnement du processus, jamais sous forme de littéral SQL ni d'argument de ligne de commande.  En HTTPS, le vérificateur d'appelant remplace le contrôle par jeton bearer partagé `auth_token_env_var` décrit dans [Authentification des clients HTTP](/command-line-usage/mcp#http-client-authentication) ; la configuration du certificat et de la clé TLS du serveur est obligatoire, et une requête HTTP sans identifiants ne se rabat jamais sur le jeton d'environnement du serveur.

## Inspecter l'appelant {#inspecting-the-caller}

Dans une session authentifiée, `whoami()` renvoie l'ID du principal et `groups()` renvoie les groupes de l'annuaire sous forme de tableau JSON :

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Les deux fonctions nécessitent une session démarrée avec `--access.config`.

## Parcours guidés {#walkthroughs}

Le dépôt StackQL fournit [quatre parcours guidés exécutables](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), un par mode d'authentification, pilotés par un script Python qui provisionne un annuaire de démonstration et vérifie une requête.  Ils ne nécessitent aucun compte cloud et sont exécutés par la suite de tests du projet.
