parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

binaryOper: PLUS | MINUS ;

//types
base_type: INT_DEC | BOOL_DEC | CHAR_DEC | STRING_DEC;
array_type: (base_type | pair_type) (OPEN_SQUARE CLOSE_SQUARE)+;
pair_elem_type: base_type | array_type | PAIR_DEC;
pair_type: PAIR_DEC OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;
type: base_type | pair_type | array_type;

//expressions
expr: expr binaryOper expr
| INTEGER
| ident
| array_elem
| unary_op expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES
;

ident: (UNDERSCORE | LOWER_CASE | UPPER_CASE) (UNDERSCORE | LOWER_CASE | UPPER_CASE | DIGIT)*;
array_elem: ident (OPEN_PARENTHESES expr CLOSE_PARENTHESES)+;
unary_op: EXCLAMATION | MINUS | LEN | ORD | CHR;


// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
