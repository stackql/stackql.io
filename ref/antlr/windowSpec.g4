grammar windowSpec;

windowSpec
    : (PARTITION BY fullFieldName (COMMA fullFieldName)*)?
      (ORDER BY fullFieldName (COMMA fullFieldName)* (ASC | DESC)?)?
      ((ROWS | RANGE | GROUPS) 
        ( (UNBOUNDED PRECEDING | number PRECEDING | CURRENT ROW)
        | BETWEEN 
          (UNBOUNDED PRECEDING | number PRECEDING | CURRENT ROW | number FOLLOWING)
          AND
          (number PRECEDING | CURRENT ROW | number FOLLOWING | UNBOUNDED FOLLOWING)
        )
      )?
    ;

fullFieldName
    : IDENTIFIER (DOT IDENTIFIER)*
    ;

number
    : INTEGER
    ;

// Lexer rules
PARTITION: 'PARTITION';
ORDER: 'ORDER';
BY: 'BY';
ASC: 'ASC';
DESC: 'DESC';
ROWS: 'ROWS';
RANGE: 'RANGE';
GROUPS: 'GROUPS';
BETWEEN: 'BETWEEN';
AND: 'AND';
UNBOUNDED: 'UNBOUNDED';
PRECEDING: 'PRECEDING';
FOLLOWING: 'FOLLOWING';
CURRENT: 'CURRENT';
ROW: 'ROW';
COMMA: ',';
DOT: '.';
INTEGER: [0-9]+;
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
WS: [ \t\r\n]