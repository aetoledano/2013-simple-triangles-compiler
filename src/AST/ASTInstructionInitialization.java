package AST;

public class ASTInstructionInitialization extends ASTInstruction {

    ASTIdentifierReference id;
    ASTSymbol x;
    ASTSymbol y;
    ASTSymbol dim;

    public ASTInstructionInitialization(ASTIdentifierReference id, ASTSymbol x, ASTSymbol y, ASTSymbol dim, int position) {
        super(position);
        this.id = id;
        this.x = x;
        this.y = y;
        this.dim = dim;
    }

    public ASTIdentifierReference getId() {
        return id;
    }

    public ASTSymbol getX() {
        return x;
    }

    public ASTSymbol getY() {
        return y;
    }

    public ASTSymbol getDim() {
        return dim;
    }

    public Object Visit(Visitor visitor) {
        return visitor.Visit(this);
    }
}
