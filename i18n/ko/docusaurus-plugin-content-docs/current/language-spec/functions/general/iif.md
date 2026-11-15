---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 IIF 함수: 조건이 참인지에 따라 두 값 중 하나를 반환하는 인라인 if-then-else 식입니다."
image: "/img/stackql-featured-image.png"
---
입력 식의 평가 결과에 따라 값을 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> `iif()` 함수는 논리적으로 `CASE` 식 `CASE WHEN expression THEN result_if_true ELSE result_if_false END`와 같습니다.

## 인수 {#arguments}

__*expression*__  
평가할 부울 식입니다.

:::tip 

*expression* 인수는 `JSON_EXTRACT`와 같은 중첩 함수일 수 있습니다.

:::

__*result_if_true*__  
*expression* 인수가 `true`로 평가될 때 반환할 결과입니다.

__*result_if_false*__  
*expression* 인수가 `false`로 평가될 때 반환할 결과입니다.

## 반환 값 {#return-values}
*result_if_true* 또는 *result_if_false* 인수의 데이터 형식입니다.

* * *

## 예제 {#examples}

### compute engine 인스턴스의 디스크 크기 분류 {#catgorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif) 문서를 참조하세요.
