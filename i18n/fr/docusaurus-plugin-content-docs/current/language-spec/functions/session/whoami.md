---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestion des accès
  - whoami
  - principal
description: "Fonction WHOAMI dans StackQL : renvoyer l'ID du principal de l'appelant authentifié lorsque la gestion des accès est activée."
image: "/img/stackql-featured-image.png"
---
Renvoie l'ID du principal de l'appelant authentifié.  Nécessite une session démarrée avec la [gestion des accès](/command-line-usage/access-management) activée.  Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Gestion des accès ]](/command-line-usage/access-management)

* * * 

## Syntaxe {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Arguments {#arguments}

Aucun.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie l'ID du principal, sous forme de chaîne de caractères, établi pour l'instruction en cours.  La fonction échoue dans une session démarrée sans `--access.config`.

* * *

## Exemples {#examples}

### Examiner l'identité établie {#inspect-the-established-identity}

Les deux fonctions ne prennent aucun argument et décrivent l'appelant que le serveur a authentifié pour cette instruction.  `whoami()` renvoie l'ID du principal et `groups()` les groupes de l'appelant dans l'annuaire ; les exécuter ensemble permet donc de vérifier rapidement qu'un jeton ou un certificat correspond bien au principal attendu avant d'exécuter quoi que ce soit d'autre.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
