parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

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
| IF expr THEN expr ELSE expr FI
| WHILE expr DO stat DONE
| BEGIN stat END
| comment stat
| (WHITESPACE)+ stat
| stat SEMI stat;

//assignments
assign_lhs: ident | array_elem | pair_elem ;
assign_rhs: expr;

int_sign: PLUS | MINUS;

digit: DIGIT;

//literals
int_literal: (int_sign)? (digit)+;

bool_literal: TRUE | FALSE;

char_literal: SINGLE_QUOTATION (LOWER_CASE | UPPER_CASE) SINGLE_QUOTATION;

string_literal: DOUBLE_QUOTATION (LOWER_CASE | UPPER_CASE)* DOUBLE_QUOTATION;

array_literal: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;

pair_literal: NULL;

array_liter:
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
| pair_elem
| CALL ident OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES ;

//types
base_type: INT_DEC | BOOL_DEC | CHAR_DEC | STRING_DEC;
array_type: (base_type | pair_type) (OPEN_SQUARE CLOSE_SQUARE)+;
pair_elem_type: base_type | array_type | PAIR_DEC;
pair_type: PAIR_DEC OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;
type: base_type | pair_type | array_type;

//functions
func: type ident OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
arg_list: expr (COMMA expr)*;
param: type ident;
param_list: param (COMMA param)*;

//expressions
expr: expr binary_op expr
| INTEGER
| ident
| array_elem
| unary_op expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES;

pair_elem: (FST | SND) expr;
array_elem: ident (OPEN_PARENTHESES expr CLOSE_PARENTHESES)+;
ident: (UNDERSCORE | LOWER_CASE | UPPER_CASE) (UNDERSCORE | LOWER_CASE | UPPER_CASE | DIGIT)*;
unary_op: EXCLAMATION | MINUS | LEN | ORD | CHR;
binary_op: MULTI | DIV | PERCENTAGE | PLUS | MINUS | GT | GTE | LT | LTE | EQUIV | NOTEQUIV | AND| OR;

// EOF indicates that the program must consume to the end of the input.
prog: (whitespace|comment)* BEGIN (whitespace|comment)* func* stat (whitespace|comment)* END ;
whitespace: WHITESPACE | EOL;
comment: HASH (~EOL | WHITESPACE)* EOL;
