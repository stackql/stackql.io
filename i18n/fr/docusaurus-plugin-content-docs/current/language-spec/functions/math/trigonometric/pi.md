---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: Récupérer la constante mathématique π (pi) avec SQL dans StackQL.
image: "/img/stackql-featured-image.png"
---
Renvoie la constante mathématique π (pi), c'est-à-dire le rapport entre la circonférence d'un cercle et son diamètre.

Voir aussi :  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntaxe {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Arguments {#arguments}

Cette fonction ne nécessite aucun argument.

## Valeur(s) renvoyée(s) {#return-values}
Renvoie la constante π, approximativement égale à 3.14159.

* * *

## Exemples {#examples}

### Récupérer la valeur de π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Cette requête affiche la valeur de π, soit environ `3.14159`, utile dans divers calculs mathématiques impliquant des cercles et d'autres fonctions géométriques.
