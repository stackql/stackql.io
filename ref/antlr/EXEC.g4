grammar EXEC;

import common;

execStatement
    : EXEC queryHint? multipartIdentifier '.' providerMethodName parameterExpression (parameterExpression)* ';'
    ;