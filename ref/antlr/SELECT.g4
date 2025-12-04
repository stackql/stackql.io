grammar SELECT;

import common;

selectStatement
    : withClause?
      SELECT
      (DISTINCT)?
      (star='*' |
      fullFieldName (AS? alias)? |
      functionCall (AS? alias)? |
      windowFunctionCall (AS? alias)?)*
      (
      FROM resource (( OUTER )? JOIN resource ON expression)?
      (WHERE expression)?
      (
      GROUP BY
      groupByItem (',' groupByItem)*
      (WITH ROLLUP)?
      )?
      (HAVING havingExpr=expression)?
      )?
      (ORDER BY fullFieldName (',' fullFieldName)*)? (ASC|DESC)?
      (LIMIT number)?
      ';'
    ;

withClause
    : WITH RECURSIVE? cteDefinition (',' cteDefinition)*
    ;

cteDefinition
    : cteName ('(' columnList ')')? AS '(' selectStatement ')'
    ;

windowFunctionCall
    : windowFunction OVER '(' windowSpec ')'
    ;

windowFunction
    : ROW_NUMBER '(' ')'
    | RANK '(' ')'
    | DENSE_RANK '(' ')'
    | PERCENT_RANK '(' ')'
    | CUME_DIST '(' ')'
    | NTILE '(' number ')'
    | LAG '(' expression (',' number (',' expression)?)? ')'
    | LEAD '(' expression (',' number (',' expression)?)? ')'
    | FIRST_VALUE '(' expression ')'
    | LAST_VALUE '(' expression ')'
    | NTH_VALUE '(' expression ',' number ')'
    | aggregateFunction
    ;

windowSpec
    : (PARTITION BY fullFieldName (',' fullFieldName)*)?
      (ORDER BY fullFieldName (',' fullFieldName)*)? (ASC|DESC)?
      frameClause?
    ;

frameClause
    : ROWS BETWEEN frameStart AND frameEnd
    ;

frameStart
    : UNBOUNDED PRECEDING
    | number PRECEDING
    | CURRENT ROW
    ;

frameEnd
    : UNBOUNDED FOLLOWING
    | number FOLLOWING
    | CURRENT ROW
    ;

aggregateFunction: 'aggregateFunction';
cteName: 'cteName';
columnList: 'columnList';
