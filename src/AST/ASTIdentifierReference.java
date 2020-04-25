package AST;

public class ASTIdentifierReference extends ASTIdentifier {

    public ASTIdentifierReference(String lexeme, int entry, int position) {
        super(lexeme, entry, position);
    }

    public Object Visit(Visitor visitor) {
        return visitor.Visit(this);
    }
}
