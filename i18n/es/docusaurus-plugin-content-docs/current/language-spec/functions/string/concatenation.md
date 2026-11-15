---
title: Operador de concatenación
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "El operador de concatenación || en StackQL: unir dos o más valores de cadena en una sola cadena."
image: "/img/stackql-featured-image.png"
---
Devuelve la cadena resultante de la concatenación, o unión, de dos o más valores de cadena.  

Consulte también:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaxis {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Argumentos {#arguments}

__*character_expression*__  
Una cadena literal o una columna de cadena.

## Valor(es) devuelto(s) {#return-values}

Devuelve una cadena.

:::note

A partir de StackQL `v1.0`, el analizador sintáctico trata `||` como el operador de concatenación, con mayor precedencia que los operadores de comparación, de modo que `WHERE id = prefix || suffix` compara `id` con la cadena unida.  Las versiones anteriores interpretaban `||` como el componente léxico del `OR` booleano.  Use `OR` para la disyunción booleana.  También se pueden concatenar [variables de sesión](/getting-started/session-variables): `$prefix || '-' || $suffix`.

:::

* * *

## Ejemplos {#examples}

### Concatenar dos campos {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Para obtener más información, consulte [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
