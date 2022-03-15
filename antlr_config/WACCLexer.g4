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

//numbers
fragment DIGIT: '0'..'9' ;
INT_LITERAL: DIGIT+ ;

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

//bitwise operators
BITWISE_AND: '&';
BITWISE_OR: '|';
BITWISE_XOR: '^';
BITWISE_NOT: '~';
LEFT_SHIFT: '<<';
RIGHT_SHIFT: '>>';

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE: '[';
CLOSE_SQUARE: ']';

//other characters
COMMA: ',';
fragment UNDERSCORE: '_';
EXCLAMATION: '!';
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;
COLON: ':';

//letters
fragment LOWER_CASE: 'a' .. 'z';
fragment UPPER_CASE: 'A' .. 'Z';
fragment CHARACTER: ~('\\' | '\'' | '"') | ('\\0' | '\\b' | '\\t' | '\\n' | '\\f' | '\\r' | '\\"' | '\\\'' | '\\\\');
CHAR_LITERAL: SINGLE_QUOTATION CHARACTER SINGLE_QUOTATION;
STRING_LITERAL: DOUBLE_QUOTATION CHARACTER* DOUBLE_QUOTATION;

//base types
INT_DEC: 'int';
BOOL_DEC: 'bool';
CHAR_DEC: 'char';
STRING_DEC: 'string';

//quotation marks
fragment SINGLE_QUOTATION: '\'';
fragment DOUBLE_QUOTATION: '"';

//pairs
PAIR_DEC: 'pair';
FST: 'fst';
SND: 'snd';
NEWPAIR: 'newpair';

IDENT: (UNDERSCORE | LOWER_CASE | UPPER_CASE) (UNDERSCORE | LOWER_CASE | UPPER_CASE | DIGIT)*;
COMMENT: '#' (~'\n')* '\n' -> skip;
WHITESPACE: ('\n' | ' ' | '\t' | '\r') -> skip;

