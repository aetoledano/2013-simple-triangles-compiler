
package AST;

public class ASTInstructionMove extends ASTInstruction {
    
    ASTIdentifierReference idR;
    ASTSymbol x;
    ASTSymbol y;

    public ASTInstructionMove(ASTIdentifierReference idR, ASTSymbol x, ASTSymbol y, int position) {
        super(position);
        this.idR = idR;
        this.x = x;
        this.y = y;
    }

    public ASTIdentifierReference getIdR() {
        return idR;
    }

    public ASTSymbol getX() {
        return x;
    }

    public ASTSymbol getY() {
        return y;
    }
    
    public Object Visit(Visitor visitor) {
        return visitor.Visit(this);
    }

}
