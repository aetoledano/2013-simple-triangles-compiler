package AST;

public abstract class ASTSymbol extends ASTExpression {

    private int entry;

    public int Entry() {
        return entry;
    }

    public ASTSymbol(int entry, int position) {
        super(position);
        this.entry = entry;
    }
}
