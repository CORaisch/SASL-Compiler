package AST;

public enum AST_NodeType {
	APPLICATION, ABSTRACTION, IDENTIFIER, WHERE, SEMICOLON, HD, TL, PLUS, MINUS, MUL,
	DIV, AND, OR, NOT, NUM, SASL_STRING, BOOL, COND, EQ, COLON, NIL, NEQ, LT, GT, LEQ, 
	GEQ, ARGUMENT,
	
	BUILTIN, CONSTANT_NUM, CONSTANT_STRING, CONSTANT_BOOL, S, K ,I, Y,U
}
