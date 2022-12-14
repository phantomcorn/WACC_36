parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN  func* stat  END EOF;

//functions
func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
param: param_type IDENT;
param_list: param (COMMA param)*;
param_type: GENERIC_DEC | type;
//statements
stat: SKIP_STAT #skip
| type IDENT EQUALS assign_rhs #declaration
| assign_lhs EQUALS assign_rhs #assignment
| READ assign_lhs #read
| FREE expr #free
| RETURN expr #return
| EXIT expr #exit
| PRINT expr #print
| PRINTLN expr #println
| IF expr THEN stat ELSE stat FI #if
| WHILE expr DO stat DONE #while
| BEGIN stat END #begin
| stat SEMI stat #composition
;

//assignments
assign_lhs: IDENT #AssignVar
| array_elem #AssignArrayElem
| pair_elem #AssignLhsPairElem;

assign_rhs: expr #assignExpr
| array_literal #arrayLiteral
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES #assignPair
| pair_elem #assignPairElem
| CALL IDENT OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES #assignFunc
| expr S_IF expr S_THEN expr #assignSideIf
| incrDecr #incrementDecrementRhs
;

arg_list: expr (COMMA expr)*;
pair_elem: (FST | SND) expr;

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
expr: int_literal #intLiteral
| arithmeticSideEffect #arithSideEffect
| expr binop1 expr #binaryOp1
| expr binop2 expr #binaryOp2
| expr binop3 expr #binaryOp3
| expr binop4 expr #binaryOp4
| expr binop5 expr #binaryOp5
| expr binop6 expr #binaryOp6
| expr binop7 expr #binaryOp7
| incrDecr #incrementDecrement
| unary_op expr #unaryOp
| bool_literal #boolLiteral
| CHAR_LITERAL #charLiteral
| STRING_LITERAL #stringLiteral
| pair_literal #pairLiteral
| IDENT #identifier
| array_elem #arrayElem
| OPEN_PARENTHESES expr CLOSE_PARENTHESES #parens
;


unary_op: EXCLAMATION
| MINUS
| LEN
| ORD
| CHR
| BITWISE_NOT
;

incrDecr : assign_lhs incr_decr_literal #postIncrDecr
| incr_decr_literal assign_lhs  #preIncrDecr
;

arithmeticSideEffect : assign_lhs (binop2|binop1) EQUALS expr;

binop1: PERCENTAGE
| DIV
| MULTI;

binop2: MINUS
| PLUS;

binop3: LTE
| LT
| GTE
| GT
| LEFT_SHIFT
| RIGHT_SHIFT;

binop4: NOTEQUIV
| EQUIV;

binop5: AND;

binop6: OR;

binop7: BITWISE_AND | BITWISE_OR | BITWISE_XOR;

incr_decr_literal : PLUS PLUS
| MINUS MINUS
;

array_elem: IDENT (OPEN_SQUARE expr CLOSE_SQUARE)+;

//literals
int_literal: (PLUS | MINUS)? INT_LITERAL;
bool_literal: TRUE | FALSE;
array_literal: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;
pair_literal: NULL;
