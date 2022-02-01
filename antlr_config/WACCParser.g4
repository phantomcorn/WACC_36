parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN  func* stat  END ;

//functions
func: type ident OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
param: type ident;
param_list: param (COMMA param)*;

//statements
stat: SKIP_STAT 
| type ident EQUALS assign_rhs
| assign_lhs EQUALS assign_rhs
| READ assign_rhs
| FREE expr
| RETURN expr
| EXIT expr
| PRINT expr
| PRINTLN expr
| IF expr THEN stat ELSE stat FI
| WHILE expr DO stat DONE
| BEGIN stat END
| stat SEMI stat;

//assignments
assign_lhs: ident | array_elem | pair_elem ;
assign_rhs: expr
| array_literal
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
| pair_elem
| CALL ident OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES ;

arg_list: expr (COMMA expr)*;
pair_elem: (FST | SND) expr;

//types
type: base_type | pair_type | array_type;
base_type: INT_DEC | BOOL_DEC | CHAR_DEC | STRING_DEC;
array_type: (base_type | pair_type) (OPEN_SQUARE CLOSE_SQUARE)+;
pair_type: PAIR_DEC OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;
pair_elem_type: base_type | array_type | PAIR_DEC;

//expressions
expr: expr binary_op expr
| int_literal
| bool_literal
| char_literal
| string_literal
| array_literal
| pair_literal
| ident
| array_elem
| unary_op expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES;

unary_op: EXCLAMATION | MINUS | LEN | ORD | CHR;
binary_op: MULTI | DIV | PERCENTAGE | PLUS | MINUS | GT | GTE | LT | LTE | EQUIV | NOTEQUIV | AND | OR;

ident: (UNDERSCORE | LOWER_CASE | UPPER_CASE) (UNDERSCORE | LOWER_CASE | UPPER_CASE | DIGIT)*;
array_elem: ident (OPEN_SQUARE expr CLOSE_SQUARE)+;

//literals
int_literal: (int_sign)? (DIGIT)+;
int_sign: PLUS | MINUS;
bool_literal: TRUE | FALSE;
char_literal: SINGLE_QUOTATION (LOWER_CASE | UPPER_CASE) SINGLE_QUOTATION;
string_literal: DOUBLE_QUOTATION (LOWER_CASE | UPPER_CASE)* DOUBLE_QUOTATION;
array_literal: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;
pair_literal: NULL;
