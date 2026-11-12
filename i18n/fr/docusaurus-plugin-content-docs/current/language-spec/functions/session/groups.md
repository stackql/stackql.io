---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - gestion des accès
  - groups
  - annuaire des principaux
description: "Fonction GROUPS dans StackQL : renvoyer les groupes d'annuaire de l'appelant authentifié sous forme de tableau JSON lorsque la gestion des accès est activée."
image: "/img/stackql-featured-image.png"
---
Renvoie les appartenances de l'appelant authentifié aux groupes de l'annuaire sous forme de tableau JSON.  Nécessite une session démarrée avec la [gestion des accès](/command-line-usage/access-management) activée.  Disponible à partir de StackQL `v1.0`.

Voir aussi :  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Gestion des accès ]](/command-line-usage/access-management)

* * * 

## Syntaxe {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Arguments {#arguments}

Aucun.

## Valeur(s) renvoyée(s) {#return-values}

Renvoie un tableau JSON d'ID de groupe issus de l'instantané de l'annuaire des principaux pris pour l'instruction en cours, par exemple `["readers"]`.  L'appartenance est actualisée à chaque instruction, de sorte qu'une révocation est prise en compte dès l'instruction suivante.  La fonction échoue dans une session démarrée sans `--access.config`.

* * *

## Exemples {#examples}

### Examiner l'identité établie {#inspect-the-established-identity}

Ensemble, les deux fonctions affichent l'ID du principal de l'appelant et ses groupes actuels dans l'annuaire.  Comme l'appartenance est relue à chaque instruction, exécuter cette requête après une modification de l'annuaire confirme que la modification est visible pour la session.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
