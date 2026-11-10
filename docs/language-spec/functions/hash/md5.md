---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "MD5 function in StackQL: return the lowercase hexadecimal MD5 digest of a string, for fingerprinting and change detection rather than security."
image: "/img/stackql-featured-image.png"
---
Returns the MD5 digest of a string as 32 lowercase hexadecimal characters.  Available in StackQL `v1.0` and later.

See also:  
[[` SELECT `]](/language-spec/select) [[ Concatenation ]](/language-spec/functions/string/concatenation)

* * * 

## Syntax {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expression*__  
A string, or a value coerced to text.  A `NULL` argument returns `NULL`.

## Return Value(s) {#return-values}

Returns a 32 character lowercase hexadecimal string.

> `MD5` is provided for fingerprinting and change detection.  It is not a password or security primitive.  On the PostgreSQL backend the database's own `md5` function is used.

* * *

## Examples {#examples}

### Fingerprint a derived value {#fingerprint-a-derived-value}

The variables are substituted as typed values, the integer is converted to text and joined to the suffix, and the digest is taken over the lower-cased result.  The same inputs always produce the same 32-character hexadecimal string, so the value can be used as a stable key.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Detect configuration changes {#detect-configuration-changes}

Hashing a serialised JSON column gives a short value to store and compare between runs.  A bucket whose digest differs from the last run has a changed IAM configuration, without the whole document having to be stored or diffed.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
