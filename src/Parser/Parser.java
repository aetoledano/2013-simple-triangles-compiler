/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import AST.*;
import Errors.ErrorReporter;
import Errors.SyntacticError;
import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenKind;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import Lexer.Types;

public class Parser {
    
    Lexer input; //from were do we get the tokens
    Token lookahead; //current token
    ErrorReporter errorReporter;

    /*
     <PROG> -> grafico(){ <listainstrucciones> }
 
     <listainstrucciones>-> <instruccion>;<listainstrucciones> | e 
     <instruccion> -> <declaracion> | <inicializacion> | <mover> | <asignacion>
 
 
     <declaracion> -> <tipo> <listaid>
     <listaid> -> id<masid>
     <masid> -> , <listaid> | e 
     <tipo> -> triangulo | int
 

     <asig> -> id = <algo>
     <algo> ->  id| litEnt
     <mover> -> move id , (<algo>,<algo>); 
     <inicializacion> ->inittriangulo id , (<algo>, <algo>),<algo>;  
       
     */
    public Parser(Lexer input, ErrorReporter errorReporter) throws IOException {
        this.input = input;
        this.errorReporter = errorReporter;
        lookahead = input.nextToken();
    }
    
    private void consume() throws IOException {
        lookahead = input.nextToken();
    }
    
    public void match(TokenKind tk_expected) throws IOException {
        if (tk_expected == lookahead.getKind()) {
            consume();
        } else {
            errorReporter.add(new SyntacticError(lookahead.getLine(), "Sintactic Error: Expected \"" + toHuman(tk_expected) + "\" found \"" + toHuman(lookahead.getKind())+"\"" ));
        }
    }

    // <program> -> <instr_list>
    public ASTProgram Parse() throws IOException {
        return new ASTProgram(ParseGrafico(), lookahead.getLine());
    }
    
    private List<ASTInstruction> ParseGrafico() throws IOException {
        // <PROG> -> grafico(){ <listainstrucciones> }
        match(TokenKind.Graphics);
        match(TokenKind.LeftParen);
        match(TokenKind.RigthParen);
        match(TokenKind.LeftKey);
        List<ASTInstruction> instructions = listaInstrucciones();
        match(TokenKind.RigthKey);
        return instructions;
    }

    // <instr_list> -> <instruction> tk pto coma <mas_instr>
    private List<ASTInstruction> listaInstrucciones() throws IOException {
        //<instruccion>;<listainstrucciones> | e 
        LinkedList<ASTInstruction> instructions = new LinkedList<ASTInstruction>();
        ASTInstruction i;
        while (lookahead.getKind() == TokenKind.Triangle
                || lookahead.getKind() == TokenKind.Int
                || lookahead.getKind() == TokenKind.Id
                || lookahead.getKind() == TokenKind.InitTriangle
                || lookahead.getKind() == TokenKind.Move) {
            i = Instruction();
            match(TokenKind.SemiColon);
            instructions.add(i);
        }
        return instructions;
    }
    
    private ASTInstruction Instruction() throws IOException {
        //<instruccion> -> <declaracion> | <inizializacion> | <mover> | <asignacion>
        if (lookahead.getKind() == TokenKind.Triangle || lookahead.getKind() == TokenKind.Int) {
            return Declaracion();
        } else if (lookahead.getKind() == TokenKind.InitTriangle) {
            return Inicializacion();
        } else if (lookahead.getKind() == TokenKind.Move) {
            return Mover();
        } else if (lookahead.getKind() == TokenKind.Id) {
            return Asignacion();
        } else {
            return null;
        }
    }
    
    public ASTInstructionDeclaration Declaracion() throws IOException {
        //<declaracion> -> <tipo> <listaid>
        Types type;
        if (lookahead.getKind() == TokenKind.Int) {
            type = Types.Int;
            match(TokenKind.Int);
        } else {
            type = Types.Triangle;
            match(TokenKind.Triangle);
        }
        if (lookahead.getKind() == TokenKind.Id) {
            LinkedList<ASTIdentifierDeclaration> identifiers = ListaId();
            if (identifiers != null) {
                return new ASTInstructionDeclaration(identifiers, type, lookahead.getLine());
            }
        }else{
            match(TokenKind.Id);
        }
        return null;
        
    }
    
    private LinkedList<ASTIdentifierDeclaration> ListaId() throws IOException {
        LinkedList<ASTIdentifierDeclaration> identifiers = new LinkedList<ASTIdentifierDeclaration>();
        ASTIdentifierDeclaration id = new ASTIdentifierDeclaration(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
        
        match(TokenKind.Id);
        identifiers.add(id);
        LinkedList<ASTIdentifierDeclaration> rest = MasId();
        if (rest != null) {
            identifiers.addAll(rest);
            return identifiers;
        } else {
            return null;
        }
    }
    
    private LinkedList<ASTIdentifierDeclaration> MasId() throws IOException {

        //<masid> -> , <listaid> | e 
        if (lookahead.getKind() == TokenKind.Comma) {
            match(TokenKind.Comma);
            if (lookahead.getKind() == TokenKind.Id) {
                return ListaId();
            } else {
                match(TokenKind.Id);
            }
            return null;
        } else {
            return new LinkedList<ASTIdentifierDeclaration>();
        }
    }
    
    private ASTInstructionInitialization Inicializacion() throws IOException {

        //<inicializacion> ->inittriangulo id , (<algo>, <algo>),<algo>  
        match(TokenKind.InitTriangle);
        if (lookahead.getKind() == TokenKind.Id) {
            ASTIdentifierReference id = new ASTIdentifierReference(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
            match(TokenKind.Id);
            match(TokenKind.Comma);
            match(TokenKind.LeftParen);
            ASTSymbol xpos = Algo();
            if (xpos != null) {
                match(TokenKind.Comma);
                ASTSymbol ypos = Algo();
                if (ypos != null) {
                    match(TokenKind.RigthParen);
                    match(TokenKind.Comma);
                    ASTSymbol dim = Algo();
                    if (dim != null) {
                        return new ASTInstructionInitialization(id, xpos, ypos, dim, lookahead.getLine());
                    }
                    return null;
                }
                return null;
            }
            return null;
        } else {
            match(TokenKind.Id);
            return null;
        }
        
    }
    
    private ASTSymbol Algo() throws IOException {
        if (lookahead.getKind() == TokenKind.Id) {
            ASTIdentifierValue i = new ASTIdentifierValue(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
            match(TokenKind.Id);
            return i;
        } else if (lookahead.getKind() == TokenKind.IntLiteral) {
            ASTLiteralInt i = new ASTLiteralInt(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
            match(TokenKind.IntLiteral);
            return i;
        } else {
            match(TokenKind.Id);
            match(TokenKind.IntLiteral);
            return null;
        }
        
    }
    
    private ASTInstructionMove Mover() throws IOException {
        //<mover> -> move id , (<algo>,<algo>) 
        match(TokenKind.Move);
        if (lookahead.getKind() == TokenKind.Id) {
            ASTIdentifierReference idR = new ASTIdentifierReference(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
            match(TokenKind.Id);
            match(TokenKind.Comma);
            match(TokenKind.LeftParen);
            ASTSymbol xpos = Algo();
            if (xpos != null) {
                match(TokenKind.Comma);
                ASTSymbol ypos = Algo();
                if (ypos != null) {
                    match(TokenKind.RigthParen);
                    return new ASTInstructionMove(idR,xpos,ypos,idR.Position());
                }
                return null;
            }
            return null;
        } else {
            match(TokenKind.Id);
            return null;
        }
    }
    
    private ASTInstructionAssigment Asignacion() throws IOException {
        // <asig> -> id = <algo>

        if (lookahead.getKind() == TokenKind.Id) {
            ASTIdentifierReference id = new ASTIdentifierReference(lookahead.getLexeme(), lookahead.getEntry(), lookahead.getLine());
            match(TokenKind.Id);
            match(TokenKind.Assignment);
            ASTSymbol value = Algo();
            if (value != null) {
                return new ASTInstructionAssigment(id, value, id.Position());
            }
            return null;
        }
        return null;
    }
    
    public Lexer getInput() {
        return input;
    }
    
    private String toHuman(TokenKind token){
        if (token == TokenKind.Assignment){
            return "assignment";
        }
        if (token == TokenKind.Comma){
            return "comma";
        }
        if (token == TokenKind.EOT){
            return "Code End";
        }
        if (token == TokenKind.Graphics){
            return "graphics";
        }
        if (token == TokenKind.Id){
            return "identifier";
        }
        if (token == TokenKind.InitTriangle){
            return "instruction \"inittriangle\"";
        }
        if (token == TokenKind.Int){
            return "integer";
        }
        if (token == TokenKind.IntLiteral){
            return "integer value";
        }
        if (token == TokenKind.LeftKey){
            return "{";
        }
        if (token == TokenKind.LeftParen){
            return "(";
        }
        if (token == TokenKind.Move){
            return "instruction move";
        }
        if (token == TokenKind.RigthKey){
            return "}";
        }
        if (token == TokenKind.RigthParen){
            return ")";
        }
        if (token == TokenKind.SemiColon){
            return ";";
        }
        if (token == TokenKind.Triangle){
            return "triangle";
        }
        return TokenKind.Error.toString(); 
    }
}
