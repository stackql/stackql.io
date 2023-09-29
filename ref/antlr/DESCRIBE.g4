grammar DESCRIBE;

import common;

describeStatement
    : DESCRIBE EXTENDED? multipartIdentifier ';'
    ;