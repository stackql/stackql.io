---
title: ユーティリティコマンド
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "clear や sleep などの StackQL シェルのユーティリティコマンドについて、その構文と、対話型セッションやスクリプトでの使いどころを説明します。"
image: "/img/stackql-featured-image.png"
---

StackQL のユーティリティコマンドを次の表に示します。

| ユーティリティコマンド | 説明 | 例 |
|--|--|--|
| `clear` | シェルから結果とコマンドを消去する（セミコロンで終える必要はない） | `clear` または `CLEAR` |
| `sleep n` | `n` ミリ秒間スリープする  | `sleep 5000` または `SLEEP 5000` |
