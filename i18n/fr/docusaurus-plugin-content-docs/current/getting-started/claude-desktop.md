---
title: Utiliser StackQL avec Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ia
  - infrastructure-as-code
  - multi-cloud
description: Utilisez StackQL avec Claude Desktop pour interroger et provisionner des ressources cloud en langage naturel
image: "/img/stackql-featured-image.png"
---

StackQL s'intègre à Claude Desktop via le Model Context Protocol (MCP), ce qui vous permet d'interroger et de provisionner des ressources cloud chez plusieurs fournisseurs cloud en langage naturel. Cette combinaison puissante vous permet d'interagir avec votre infrastructure cloud de manière conversationnelle tout en vous appuyant, en coulisses, sur l'interface SQL de StackQL.

Il existe trois options d'installation, présentées ici dans l'ordre recommandé : installer depuis le Connector Directory d'Anthropic, installer depuis un bundle téléchargé, ou enregistrer manuellement un binaire StackQL installé localement.

## Installer depuis le Connector Directory (recommandé) {#install-from-the-connector-directory-recommended}

Le moyen le plus simple d'utiliser le serveur MCP StackQL avec Claude Desktop est de l'installer depuis le Connector Directory d'Anthropic. Dans Claude Desktop : **Settings -> Connectors -> Add -> Browse Connectors**, recherchez **StackQL**, puis cliquez sur **Install**. Il n'y a rien à télécharger et aucune installation séparée de StackQL n'est requise.

![StackQL dans le Connector Directory d'Anthropic](/img/stackql-anthropic-directory-listing.png)

Vous pouvez aussi consulter la fiche directement sur [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Cette opération installe le serveur MCP StackQL en mode in-process pour votre plateforme : la fiche fournit un bundle multiplateforme combiné contenant des binaires signés pour macOS (universel), Windows x64, ainsi que Linux x64 et arm64, et Claude Desktop sélectionne automatiquement celui qui convient à votre machine. Pour Windows ARM64 natif, installez le bundle `windows-arm64` distinct depuis les [téléchargements ci-dessous](#install-from-a-downloaded-bundle). Les binaires Windows sont signés avec Authenticode et le binaire macOS est notarisé par Apple - ce sont les mêmes binaires que ceux de la version autonome de StackQL.

![Installation du connecteur StackQL](/img/stackql-anthropic-directory-install.png)

Un connecteur installé depuis le Connector Directory hérite de l'environnement de Claude Desktop ; définissez donc les variables d'environnement de vos providers au niveau du système d'exploitation avant de lancer Claude Desktop - consultez [Identifiants propres à chaque provider](#provider-specific-credentials). Claude Desktop capture son environnement au lancement : après avoir modifié des variables, vous devez quitter complètement Claude Desktop (y compris l'icône de la zone de notification sous Windows) puis le relancer. Demander à l'agent d'exécuter [`reload_credentials`](/mcp/reload_credentials) renvoie l'état des identifiants par provider et constitue le moyen le plus rapide de confirmer ce que le serveur voit et ne voit pas.

Claude Desktop annonce la capacité d'élicitation MCP ; le mode du serveur par défaut (`safe`) vous demande donc une approbation à chaque mutation - consultez [Modes du serveur](/command-line-usage/mcp#server-modes).

## Installer depuis un bundle téléchargé {#install-from-a-downloaded-bundle}

Utilisez cette option d'installation pour épingler une version précise, installer sans accès au Connector Directory ou vérifier vous-même les sommes de contrôle avant l'installation.

Le MCP Bundle préconstruit (`.mcpb`) est un paquet installable en un clic qui contient le binaire `stackql` signé et la configuration du serveur. Aucune installation séparée de StackQL n'est requise.

Téléchargez le bundle correspondant à votre plateforme depuis la dernière version :

| Plateforme | Bundle |
|---|---|
| macOS (universel) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (natif) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Ensuite, dans Claude Desktop : **Settings -> Extensions -> Install Extension**, puis sélectionnez le fichier téléchargé. Le binaire embarqué est signé avec Authenticode (Windows) et notarisé par Apple (macOS) ; chaque bundle est accompagné d'une somme de contrôle `.sha256` sur la [page de la version](https://github.com/stackql/stackql/releases/latest). Pour vérifier avant l'installation :

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Le serveur est également référencé dans le [MCP Registry officiel](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) sous le nom `io.github.stackql/stackql-mcp`, qui épingle ces sommes de contrôle.

## Configuration manuelle {#manual-configuration}

Si StackQL est déjà installé, vous pouvez enregistrer le serveur MCP StackQL directement dans le fichier de configuration de Claude Desktop.

### Prérequis {#prerequisites}

Avant de configurer StackQL avec Claude Desktop, vérifiez que vous disposez des éléments suivants :

1. **StackQL installé** - Téléchargez et installez StackQL depuis [stackql.io](https://stackql.io/downloads) ou utilisez un gestionnaire de paquets
2. **Claude Desktop** - Téléchargez-le depuis le [site d'Anthropic](https://claude.ai/desktop)
3. **Identifiants des fournisseurs cloud** - Clés d'API ou identifiants des fournisseurs cloud avec lesquels vous souhaitez travailler

Vérifiez que StackQL figure dans le PATH de votre système en exécutant :

```bash
stackql --version
```

### Configurer le serveur MCP {#setting-up-the-mcp-server}

Pour que Claude Desktop puisse communiquer avec StackQL, vous devez configurer le serveur MCP dans votre fichier de configuration Claude Desktop.

#### Windows {#windows}

Modifiez le fichier de configuration situé à l'emplacement suivant :
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Modifiez le fichier de configuration situé à l'emplacement suivant :
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Modifiez le fichier de configuration situé à l'emplacement suivant :
```
~/.config/Claude/claude_desktop_config.json
```

### Exemple de configuration {#configuration-example}

Ajoutez la configuration suivante à votre fichier `claude_desktop_config.json` :

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
Vous n'avez besoin d'inclure que les variables d'environnement des fournisseurs cloud que vous comptez utiliser. Supprimez de la configuration les identifiants des providers inutilisés.
:::

:::warning
N'ajoutez jamais votre fichier `claude_desktop_config.json` contenant de vrais identifiants à un système de gestion de versions. Appliquez des pratiques sécurisées de gestion des identifiants.
:::

Claude Desktop annonce la capacité d'élicitation MCP ; le mode du serveur par défaut (`safe`) vous demande donc une approbation à chaque mutation. Passez à `mode: read_only` pour un accès limité à l'inventaire ou à `mode: full_access` pour désactiver les demandes d'approbation - consultez [Modes du serveur](/command-line-usage/mcp#server-modes).

### Utiliser un fichier d'identifiants (`--env.file`) {#using-a-credentials-file---envfile}

Une alternative au bloc `env` - et celle qui est recommandée sous Windows - consiste à conserver les identifiants dans un fichier de type dotenv et à le transmettre avec l'option [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) :

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

Avec un fichier d'identifiants minimal :

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Pourquoi cette approche est préférable au bloc `env` :

- Les modifications du bloc `env` nécessitent un redémarrage complet de Claude Desktop (y compris la fermeture de l'icône de la zone de notification) avant que le sous-processus MCP ne les voie, et sous Windows `setx` écrit dans le registre - la modification n'atteint jamais le processus Desktop en cours d'exécution.
- Le fichier `--env.file` peut être créé ou renouvelé à tout moment - il n'a même pas besoin d'exister au démarrage du serveur - et demander à l'agent d'exécuter [`reload_credentials`](/mcp/reload_credentials) prend en compte les modifications sans aucun redémarrage.

:::note

`--env.file` et `reload_credentials` sont disponibles dans les versions de StackQL postérieures à `v0.10.542`.

:::

### Identifiants propres à chaque provider {#provider-specific-credentials}

Les méthodes d'authentification diffèrent selon le fournisseur cloud ; consultez la documentation des providers ([https://stackql.io/providers](/providers)) pour connaître les variables d'environnement requises par le ou les providers souhaités.


### Redémarrer Claude Desktop {#restarting-claude-desktop}

Après avoir mis à jour le fichier de configuration, redémarrez Claude Desktop pour charger la nouvelle configuration du serveur MCP. Le serveur MCP StackQL démarre automatiquement lorsque vous commencez une nouvelle conversation.

## Ce que vous pouvez faire {#what-you-can-do}

Une fois la configuration terminée, vous pouvez utiliser le langage naturel dans Claude Desktop pour :

### Interroger des ressources cloud {#query-cloud-resources}

Demandez à Claude de récupérer des informations sur votre infrastructure cloud :

- "Montre-moi toutes mes instances de calcul GCP dans la région us-central1"
- "Liste tous les buckets S3 de mon compte AWS"
- "Quelles machines virtuelles Azure sont en cours d'exécution dans mon abonnement ?"
- "Montre-moi tous les pods Kubernetes de l'espace de noms default"

### Provisionner des ressources {#provision-resources}

Créez et déployez des ressources cloud à l'aide de commandes conversationnelles :

- "Crée un bucket S3 nommé 'my-data-bucket' dans us-west-2"
- "Déploie un bucket GCP Cloud Storage avec la gestion des versions activée"
- "Lance une petite instance EC2 dans us-east-1"
- "Crée un groupe de ressources Azure dans westus2"

### Analyser et auditer {#analyze-and-audit}

Réalisez des audits de sécurité et des contrôles de conformité :

- "Montre-moi tous les buckets S3 accessibles publiquement"
- "Liste les utilisateurs IAM qui ne se sont pas connectés depuis 90 jours"
- "Trouve toutes les instances GCP sans labels appropriés"
- "Identifie les VM Azure dont la sauvegarde n'est pas activée"

### Opérations multicloud {#cross-cloud-operations}

Interrogez et comparez des ressources chez plusieurs fournisseurs cloud :

- "Montre-moi toutes les instances de calcul sur AWS, GCP et Azure"
- "Compare les coûts de stockage entre AWS S3 et GCP Cloud Storage"
- "Liste toutes les bases de données de tous les fournisseurs cloud"

### Infrastructure as code {#infrastructure-as-code}

Générez et exécutez des requêtes de déploiement d'infrastructure :

- "Crée une stack de ressources comprenant un VPC, un sous-réseau et une instance EC2"
- "Déploie un cluster GKE complet avec des pools de nœuds"
- "Mets en place un déploiement multirégion dans AWS"

### Modifier des ressources {#modify-resources}

Mettez à jour des ressources cloud existantes :

- "Ajoute des tags à toutes les instances EC2 qui n'en ont pas"
- "Active le chiffrement du bucket S3 'my-bucket'"
- "Mets à jour le type de machine de l'instance GCP 'web-server-1'"
- "Passe l'ACL de mon bucket Cloud Storage en privé"

### Supprimer des ressources {#delete-resources}

Supprimez les ressources cloud dont vous n'avez plus besoin :

- "Supprime le bucket S3 'temp-bucket'"
- "Supprime toutes les instances EC2 arrêtées"
- "Nettoie les disques GCP inutilisés"

## Exemple de conversation {#example-conversation}

Voici un exemple d'interaction avec Claude Desktop à l'aide de StackQL :

**Vous :** "Peux-tu me montrer toutes mes instances de calcul GCP ?"

**Claude :** *Exécute une requête StackQL et renvoie les résultats dans un tableau mis en forme indiquant les noms des instances, les zones, les types de machine et l'état*

**Vous :** "Crée un nouveau bucket Cloud Storage nommé 'analytics-data-2025' dans la région us-central1 avec un accès uniforme au niveau du bucket"

**Claude :** *Exécute une instruction StackQL INSERT pour créer le bucket et confirme la création*

**Vous :** "Liste maintenant tous mes buckets Cloud Storage"

**Claude :** *Exécute une requête SELECT et affiche tous les buckets, y compris celui qui vient d'être créé*

## Astuces et bonnes pratiques {#tips-and-best-practices}

1. **Soyez précis** : plus votre demande est précise, meilleurs sont les résultats. Indiquez si possible les noms des ressources, les régions et des attributs précis.

2. **Utilisez le langage naturel** : vous n'avez pas besoin de connaître la syntaxe SQL - Claude traduit vos demandes en langage naturel en requêtes StackQL.

3. **Vérifiez avant d'exécuter** : pour les opérations destructives (DELETE, UPDATE), Claude vous montre généralement ce qui va être exécuté avant de poursuivre.

4. **Opérations en plusieurs étapes** : vous pouvez enchaîner des opérations au sein d'une même conversation. Claude conserve le contexte tout au long de la session.

5. **Gestion des erreurs** : si une requête échoue, Claude peut vous aider à diagnostiquer le problème et proposer des corrections à partir du message d'erreur.

6. **Explorez les possibilités** : demandez à Claude "Que puis-je faire avec StackQL ?" pour obtenir des suggestions adaptées à vos providers configurés.

## Dépannage {#troubleshooting}

### Le serveur MCP ne démarre pas {#mcp-server-not-starting}

Si Claude Desktop ne parvient pas à se connecter au serveur MCP StackQL :

1. Vérifiez que StackQL figure dans votre PATH
2. Vérifiez que votre fichier `claude_desktop_config.json` est un JSON valide
3. Assurez-vous que le chemin du fichier dans la configuration est correct pour votre système d'exploitation
4. Recherchez d'éventuels messages d'erreur dans les journaux de Claude Desktop

### Erreurs d'authentification {#authentication-errors}

Si vous recevez des erreurs d'authentification :

1. Vérifiez que les identifiants de votre fournisseur cloud sont corrects
2. Assurez-vous que les fichiers d'identifiants existent aux chemins indiqués
3. Vérifiez que les variables d'environnement sont correctement formatées dans la configuration - demander à l'agent d'exécuter [`reload_credentials`](/mcp/reload_credentials) renvoie l'état des identifiants par provider et constitue le moyen le plus rapide de voir quelle variable manque
4. Confirmez que vos identifiants disposent des permissions nécessaires

### Erreurs de permission {#permission-errors}

Si des opérations échouent en raison de permissions insuffisantes :

1. Vérifiez que votre compte de service ou votre utilisateur IAM dispose des permissions requises
2. Vérifiez les politiques IAM propres à chaque provider
3. Assurez-vous que les services d'API sont activés dans vos projets cloud

## En savoir plus {#learn-more}

- [Référence de la commande MCP de StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Spécification du langage](/language-spec/select)
- [Documentation du Model Context Protocol](https://modelcontextprotocol.io)

## Considérations de sécurité {#security-considerations}

:::caution
- Stockez les identifiants de manière sécurisée et ne les ajoutez jamais à un système de gestion de versions
- Appliquez le principe du moindre privilège aux comptes de service
- Renouvelez régulièrement les identifiants et les clés d'API
- Surveillez les journaux du serveur MCP pour détecter toute activité inhabituelle
- Envisagez d'utiliser les outils de gestion des identifiants des fournisseurs cloud (par exemple Secret Manager, Parameter Store)
:::
