package Checker;

import AST.*;
import Errors.*;
import SymbolTable.SymbolInfo;
import SymbolTable.SymbolTable;
import java.util.ListIterator;
import Lexer.Types;
import java.util.Iterator;

public class Checker implements Visitor {

    private SymbolTable symbolsTable;
    private ErrorReporter errorReporter;

    public Checker(SymbolTable symbolsTable, ErrorReporter errorReporter) {
        this.symbolsTable = symbolsTable;
        this.errorReporter = errorReporter;
    }

    //metodos de la interfaz visitor
    public Object Visit(ASTProgram a) {
        ListIterator<ASTInstruction> it = a.getInstructions().listIterator();
        ASTInstruction toy;
        while (it.hasNext()) {
            toy = it.next();
            if (toy == null) {
                System.out.println("ERR: An null AST node has been found!");
            } else {
                toy.Visit(this);
            }
        }
        return null;
    }

    //hijos de expression
    public Object Visit(ASTLiteralInt a) {
        return Lexer.Types.Int;
    }

    public Object Visit(ASTIdentifierDeclaration a) {
        SymbolInfo symbol = symbolsTable.entry(a.Entry());
        if (symbol.isDeclared()) {
            errorReporter.add(new SemanticError(a.getPosition(), "Semantic Error: variable \"" + a.Lexeme() + "\" already declared"));
        } else {
            symbol.setDeclared(true);
        }
        return null;
    }

    public Object Visit(ASTIdentifierReference a) {
        SymbolInfo symbol = symbolsTable.entry(a.Entry());
        if (!symbol.isDeclared()) {
            errorReporter.add(new SemanticError(a.getPosition(), "Semantic Error: assigment to variable \"" + a.Lexeme() + "\" undeclared"));
        } else {
            if (symbol.getType() == Types.Int)
                symbol.setInit(true);
        }
        return symbol.isDeclared() ? symbol.getType() : Types.Undefined;
    }

    public Object Visit(ASTIdentifierValue a) {
        SymbolInfo info = symbolsTable.entry(a.Entry());
        if (info.getDeclared() && info.isInit()) {
            return info.getType();
        }
        if (!info.isDeclared()) {
            errorReporter.add(new SemanticError(a.getPosition(), "Semantic Error: Variable \"" + a.Lexeme() + "\" undeclared"));
            return Types.Undefined;
        }
        if (!info.isInit()) {
            errorReporter.add(new SemanticError(a.getPosition(), "Semantic Error: Using uninitialized variable \"" + a.Lexeme() + "\""));
            return info.getType();
        }
        return Types.Undefined;
    }

    //hijos de instruction
    public Object Visit(ASTInstructionAssigment a) {
        Types t1 = (Types) a.getExpression().Visit(this);
        Types t2 = (Types) a.getIdentifier().Visit(this);
        if (t1 != t2) {
            errorReporter.add(new SemanticError(a.getIdentifier().getPosition(), "Semantic Error: Incompatible types assigment"));
        }
        return null;
    }

    public Object Visit(ASTInstructionMove a) {
        Types t = (Types) a.getIdR().Visit(this);
        SymbolInfo info = symbolsTable.entry(a.getIdR().Entry());
        if (!info.isInit()){
            errorReporter.add(new SemanticError(a.getPosition(), "Semantic Error: Using uninitialized variable \"" + a.getIdR().Lexeme() + "\""));
        }
        Types x = (Types) a.getX().Visit(this);
        Types y = (Types) a.getY().Visit(this);

        if (t != Types.Triangle) {
            errorReporter.add(new SemanticError(a.Position(), "Semantic Error: Using incompatibles types "));
        }
        if (x != y || x != Types.Int || y != Types.Int) {
            errorReporter.add(new SemanticError(a.Position(), "Semantic Error: Using incompatibles types "));
        }
        return null;
    }

    
    public Object Visit(ASTInstructionDeclaration a) {
        Iterator<ASTIdentifierDeclaration> it = a.getIdentifiers().iterator();
        ASTIdentifierDeclaration current;
        SymbolInfo sym;
        while (it.hasNext()) {
            current = it.next();
            current.Visit(this);
            sym = symbolsTable.entry(current.Entry());
            sym.setType(a.getType());
        }
        return null;
    }

    public Object Visit(ASTInstructionInitialization a) {
        Types t = (Types) a.getId().Visit(this);
        Types dim = (Types) a.getDim().Visit(this);
        Types x = (Types) a.getX().Visit(this);
        Types y = (Types) a.getY().Visit(this);
        if (t != Types.Triangle) {
            errorReporter.add(new SemanticError(a.Position(), "Semantic Error: Using incompatibles types "));
        }
        if (x != y || dim != Types.Int || x != Types.Int || y != Types.Int) {
            errorReporter.add(new SemanticError(a.Position(), "Semantic Error: Using incompatibles types "));
        }
        SymbolInfo symbol = symbolsTable.entry(a.getId().Entry());
        if (!symbol.isInit()){
            symbol.setInit(true);
        }
        return null;
    }

    //not implememented yet
    public Object Visit(AST a) {
        return null;
    }

    public Object Visit(ASTInstruction a) {
        return null;
    }

    public Object Visit(ASTExpression a) {
        return null;
    }

    public Object Visit(ASTSymbol a) {
        return null;
    }

    public Object Visit(ASTIdentifier a) {
        return null;
    }

}
