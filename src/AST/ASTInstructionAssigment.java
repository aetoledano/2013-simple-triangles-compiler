package AST;

public class ASTInstructionAssigment extends ASTInstruction {

    private ASTIdentifierReference identifier;
    private ASTSymbol expression;

    public ASTInstructionAssigment(ASTIdentifierReference identifier, ASTSymbol expression, int position) {
        super(position);
        this.identifier = identifier;
        this.expression = expression;
    }

    public ASTIdentifierReference getIdentifier() {
        return identifier;
    }

    public ASTSymbol getExpression() {
        return expression;
    }

    public Object Visit(Visitor visitor) {
        return visitor.Visit(this);
    }
}
