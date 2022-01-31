lexer grammar WACCLexer;

//operators
PLUS: '+' ;
MINUS: '-' ;
EQUALS: '=';

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE: '[';
CLOSE_SQUARE: ']';

COMMA: ',';

//numbers
fragment DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;

//base types
INT_DEC: 'int';
BOOL_DEC: 'bool';
CHAR_DEC: 'char';
STRING_DEC: 'string';

//pairs
PAIR_DEC: 'pair';
