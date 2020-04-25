package AST;

public class ASTLiteralInt extends ASTSymbol {

    private String lexeme;

    public ASTLiteralInt(String lexeme, int entry, int position) {
        super(entry, position);
        this.lexeme = lexeme;
    }

    public int Value() {
        return Integer.parseInt(lexeme);
    }

    public Object Visit(Visitor visitor) {
        return visitor.Visit(this);
    }
}
