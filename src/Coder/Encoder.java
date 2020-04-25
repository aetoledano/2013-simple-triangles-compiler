package Coder;

import Runtime.*;
import AST.*;
import Lexer.Types;
import Runtime.RuntimeMove;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import SymbolTable.SymbolInfo;
import SymbolTable.SymbolTable;

public class Encoder implements Visitor {

    List<RuntimeEntity> code;
    SymbolTable st;
    int currentMemDir;

    public List<RuntimeEntity> getCode() {
        return code;
    }

    public Encoder(SymbolTable st) {
        code = new LinkedList<RuntimeEntity>();
        this.st = st;
        currentMemDir = 0;
    }

    public Object Visit(ASTProgram a) {
        ListIterator<ASTInstruction> It = a.getInstructions().listIterator();
        while (It.hasNext()) {
            It.next().Visit(this);
        }
        code.add(new RuntimeHalt());
        return null;
    }

    public Object Visit(ASTIdentifierDeclaration a) {
        SymbolInfo info = st.entry(a.Entry());
        if (info.getType() == Types.Int) {
            code.add(new RuntimeNewInt());
            info.setAddress(currentMemDir++);
        } else if (info.getType() == Types.Triangle) {
            code.add(new RuntimeNewTriangle());
            info.setAddress(currentMemDir++);
        }
        return null;
    }

    public Object Visit(ASTIdentifierReference a) {
        SymbolInfo info = st.entry(a.Entry());
        IntValue address = new IntValue(info.getAddress());
        code.add(new RuntimeCargaDir());
        code.add(address);
        return null;
    }

    public Object Visit(ASTIdentifierValue a) {
        SymbolInfo info = st.entry(a.Entry());
        IntValue address = new IntValue(info.getAddress());
        code.add(new RuntimeCarga());
        code.add(address);
        return null;
    }

    public Object Visit(ASTInstructionAssigment a) {
        a.getIdentifier().Visit(this);
        a.getExpression().Visit(this);
        code.add(new RuntimeStore());
        return null;
    }

    public Object Visit(ASTInstructionDeclaration a) {
        List<ASTIdentifierDeclaration> ids = a.getIdentifiers();
        for (ASTIdentifierDeclaration toy : ids) {
            toy.Visit(this);
        }
        return null;
    }

    public Object Visit(ASTInstructionInitialization a) {
        a.getId().Visit(this);
        a.getY().Visit(this);
        a.getX().Visit(this);
        a.getDim().Visit(this);
        IntValue cant = new IntValue(3);
        code.add(new RuntimeInitTriangle());
        code.add(cant);
        code.add(new RuntimeStore());
        return null;
    }

    public Object Visit(ASTInstructionMove a) {
        a.getIdR().Visit(this);
        a.getY().Visit(this);
        a.getX().Visit(this);
        code.add(new RuntimeMove());
        return null;
    }

    public Object Visit(ASTLiteralInt a) {
        SymbolInfo info = st.entry(a.Entry());
        if( info .getAddress() == -1)
        {
            code.add( new RuntimeNewInt ());
            info.setAddress( currentMemDir );
            code.add( new RuntimeCargaDir ());
            IntValue address = new IntValue ( currentMemDir );
            code.add( address );
            IntValue value = new IntValue (Integer.parseInt(info.getLexeme()));
            code.add( new RuntimePush ());
            code.add( value );
            code.add( new RuntimeStore ());
            currentMemDir ++;
        }
        IntValue address = new IntValue (info.getAddress());
        code.add( new RuntimeCarga ());
        code.add( address );
        return null;
    }

    //to be implemented if needed
    public Object Visit(AST a) {
        return null;
    }

    public Object Visit(ASTInstruction a) {
        return null;
    }

    public Object Visit(ASTSymbol a) {
        return null;
    }

    public Object Visit(ASTExpression a) {
        return null;
    }

    public Object Visit(ASTIdentifier a) {
        return null;
    }

}
