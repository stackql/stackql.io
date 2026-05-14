grammar DESCRIBE;

import common;

describeStatement
    : DESCRIBE METHOD? EXTENDED? multipartIdentifier ';'
    ;