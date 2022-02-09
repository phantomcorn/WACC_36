parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN  func* stat  END EOF;

//functions
func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
param: type IDENT;
param_list: param (COMMA param)*;

//statements
stat: SKIP_STAT #skip
| type IDENT EQUALS assign_rhs #declaration
| assign_lhs EQUALS assign_rhs #assignment
| READ assign_lhs #read
| FREE expr6 #free
| RETURN expr6 #return
| EXIT expr6 #exit
| PRINT expr6 #print
| PRINTLN expr6 #println
| IF expr6 THEN stat ELSE stat FI #if
| WHILE expr6 DO stat DONE #while
| BEGIN stat END #begin
| stat SEMI stat #composition
;

//assignments
assign_lhs: IDENT #AssignVar
| array_elem #AssignArrayElem
| pair_elem #AssignLhsPairElem;

assign_rhs: expr6 #assignExpr
| array_literal #arrayLiteral
| NEWPAIR OPEN_PARENTHESES expr6 COMMA expr6 CLOSE_PARENTHESES #assignPair
| pair_elem #assignPairElem
| CALL IDENT OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES #assignFunc
;

arg_list: expr6 (COMMA expr6)*;
pair_elem: (FST | SND) expr6;

//types
type: base_type #BaseType
| pair_type #PairType
| type OPEN_SQUARE CLOSE_SQUARE #ArrayType;

base_type: INT_DEC #IntType
| BOOL_DEC #BoolType
| CHAR_DEC #CharType
| STRING_DEC #StringType;

array_type: type OPEN_SQUARE CLOSE_SQUARE;
pair_type: PAIR_DEC OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;

pair_elem_type: base_type #PairBaseType
| array_type #PairArrayType
| PAIR_DEC #PairPairType;

//expressions
expr6: expr5 binop6 expr6 | expr5 ;

expr5: expr4 binop5 expr5 | expr4 ;

expr4: expr3 binop4 expr4 | expr3 ;

expr3: expr2 binop3 expr3 | expr2 ;

expr2: expr binop2 expr2 | expr ;

expr: expr binop1 expr #binaryOp
| int_literal #intLiteral
| bool_literal #boolLiteral
| CHAR_LITERAL #charLiteral
| STRING_LITERAL #stringLiteral
| pair_literal #pairLiteral
| IDENT #identifier
| array_elem #arrayElem
| unary_op expr6 #unaryOp
| OPEN_PARENTHESES expr6 CLOSE_PARENTHESES #parens;


unary_op: EXCLAMATION
| MINUS
| LEN
| ORD
| CHR
;

binop1: MULTI
| DIV
| PERCENTAGE;

binop2: PLUS
| MINUS;

binop3: GT
| GTE
| LT
| LTE;

binop4: EQUIV
| NOTEQUIV;

binop5: AND;

binop6: OR;

array_elem: IDENT (OPEN_SQUARE expr6 CLOSE_SQUARE)+;

//literals
int_literal: (PLUS | MINUS)? INT_LITERAL;
bool_literal: TRUE | FALSE;
array_literal: OPEN_SQUARE (expr6 (COMMA expr6)*)? CLOSE_SQUARE;
pair_literal: NULL;
