lexer grammar WACCLexer;

//program keywords
BEGIN: 'begin';
END: 'end';

//boolean literals
TRUE: 'true' ;
FALSE: 'false' ;

//null
NULL: 'null' ;

//function keywords
IS: 'is';
CALL: 'call';

//statement keywords
SKIP_STAT: 'skip';
READ: 'read';
FREE: 'free';
RETURN: 'return';
EXIT: 'exit';
PRINT: 'print';
PRINTLN: 'println';
IF: 'if';
THEN: 'then';
ELSE: 'else';
FI: 'fi';
WHILE: 'while';
DO: 'do';
DONE: 'done';
SEMI: ';';

//operators
PLUS: '+' ;
MINUS: '-' ;
EQUALS: '=';
MULTI: '*';
DIV: '/';
PERCENTAGE: '%';
GT: '>';
GTE: '>=';
LT: '<';
LTE: '<=';
EQUIV: '==';
NOTEQUIV: '!=';
AND: '&&';
OR: '||';

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE: '[';
CLOSE_SQUARE: ']';

//other characters
COMMA: ',';
UNDERSCORE: '_';
EXCLAMATION: '!';
HASH: '#' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//numbers
DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;

//letters
LOWER_CASE: 'a' .. 'z';
UPPER_CASE: 'A' .. 'Z';

//base types
INT_DEC: 'int';
BOOL_DEC: 'bool';
CHAR_DEC: 'char';
STRING_DEC: 'string';

//pairs
PAIR_DEC: 'pair';
FST: 'fst';
SND: 'snd';
NEWPAIR: 'newpair';

EOL: '\n';

