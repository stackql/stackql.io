---
title: 字句構造
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の字句構造：パーサーが文、識別子、引用符、リテラル、コメント、空白文字を字句に分割する方法。"
image: "/img/stackql-featured-image.png"
---

関連項目：  
[[ StackQL リソース階層 ]](/getting-started/resource-hierarchy) [[ キーワード ]](/language-spec/keywords) [[ データ型 ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


StackQL の字句構造と文法は、MySQL の言語実装（ANSI SQL-92 標準を基にしたもの）をモデルにしており、いくつかの拡張が加えられています。StackQL の文は、クラウドリソースのクエリ（`SELECT` の文法を使用）や、`INSERT`、`UPDATE`、`REPLACE`、`DELETE` などの標準的なデータ操作言語（DML）文によるインフラストラクチャの作成、変更、削除に使用します。また、インスタンスの起動や、AWS S3 のバケットに対するオブジェクトの取得や格納などの DML 以外の操作は、組み込みのプロバイダープロシージャ（DBMS のストアドプロシージャに相当）として実装されています。組み込みのプロバイダープロシージャは `EXEC` 文で呼び出します。

StackQL の SQL ドメイン固有言語では、宣言的なインフラストラクチャプログラミングと手続き的なインフラストラクチャプログラミングの両方が可能です。

* * * 

## 構文 {#syntax}
StackQL の操作は、セミコロン（`;`）で終わる 1 つ以上の文で構成されます。文には、キーワード、識別子、パラメーター、値が含まれます。GCP で 10GB のディスクを作成する StackQL の文の例は次のとおりです。

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

上記の例では、_`INSERT`_、_`INTO`_、_`SELECT`_ がキーワード、_google.compute.disks_ が識別子、_project_、_zone_、_name_、_sizeGb_ がパラメーター、_'myProject'_、_'australia-southeast1-a'_、_'my10GbDisk'_、_10_ が値です。

## キーワード {#keywords}
キーワードは大文字と小文字を区別しません。つまり、`INSERT` と `insert` は同じものとして扱われます。ただし、慣例として大文字で書かれることがよくあります。キーワードの完全な一覧は「キーワード」セクションにあります。

## 識別子 {#identifiers}
識別子は、クラウドベンダーのリソース階層内のオブジェクトを表すシンボルです。識別子は複数の部分で構成されます。識別子の詳細については、「StackQL リソース階層」のトピックを参照してください。

## パラメーターと値 {#parameters-and-values}
パラメーターはリソースによって指定されます。パラメーターは、特定のクラウドベンダーのリソースに対して `DESCRIBE` 文を使用すると確認できます。値は、パラメーターに対応する型付きのスカラーオブジェクトまたは複合オブジェクトです。パラメーターと値の詳細については、`DESCRIBE` と「データ型」のトピックを参照してください。

## 変数 {#variables}
`$region` のようにドル記号を接頭辞に持つ識別子は、[`SET`](/language-spec/set) で割り当てたセッション変数を参照します。この識別子は、文の計画が作成される前に、変数の型付きの値に置き換えられます。単一引用符で囲んだ `'$region'` は文字列リテラルです。`FROM` 句では `$variable` はリレーション名に解決され、式の位置では `IDENTIFIER($variable)` が列名に解決されます。`"$.auth.aws.password"` のように二重引用符で囲み、ドル記号を接頭辞に持つパスは、`SET` と `UNSET` が受け付けるランタイム設定のターゲットです。セッション変数は StackQL `v1.0` 以降で利用できます。[セッション変数](/getting-started/session-variables)を参照してください。

## 演算子 {#operators}
`||` は文字列連結演算子で、比較演算子よりも結合の優先順位が高くなります。`OR` はブール値の論理和です。`v1.0` より前の StackQL リリースでは、`||` は `OR` として字句解析されていました。

## コメント {#comments}
StackQL は、StackQL の文内で複数行コメントとインラインコメントをサポートしています。コメントの例を次に示します。

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
