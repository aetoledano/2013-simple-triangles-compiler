package AST;

public interface Visitor {

    public Object Visit(AST a);

    public Object Visit(ASTExpression a);

    public Object Visit(ASTIdentifier a);

    public Object Visit(ASTIdentifierDeclaration a);

    public Object Visit(ASTIdentifierReference a);

    public Object Visit(ASTIdentifierValue a);

    public Object Visit(ASTInstruction a);

    public Object Visit(ASTInstructionAssigment a);

    public Object Visit(ASTInstructionDeclaration a);

    public Object Visit(ASTInstructionInitialization a);

    public Object Visit(ASTInstructionMove a);
    
    public Object Visit (ASTProgram a);
    
    public Object Visit(ASTSymbol a);

    public Object Visit(ASTLiteralInt a);
}
