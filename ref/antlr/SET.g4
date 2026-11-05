grammar SET;

import common;

setStatement
    : SET SESSION? setExpression (',' setExpression)* ';'
    ;

setExpression
    : (variableName | configurationPath) '=' (literal | DEFAULT | variableReference)
    ;
