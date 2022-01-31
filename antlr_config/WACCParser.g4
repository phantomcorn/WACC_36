parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

binaryOper: PLUS | MINUS ;

expr: expr binaryOper expr
| INTEGER
| OPEN_PARENTHESES expr CLOSE_PARENTHESES
;

//types
base_type: INT_DEC | BOOL_DEC | CHAR_DEC | STRING_DEC;
array_type: (base_type | pair_type) (OPEN_SQUARE CLOSE_SQUARE)+;
pair_elem_type: base_type | array_type | PAIR_DEC;
pair_type: PAIR_DEC OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;
type: base_type | pair_type | array_type;

// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
