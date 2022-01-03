grammar SELECT;

import common;

selectStatement
    : SELECT 
     (DISTINCT)?
    (star='*' | 
    fullFieldName (AS? alias)? | 
    functionCall (AS? alias)?)*
    (
    FROM resourceOrSubQuery (',' resourceOrSubQuery)*
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
