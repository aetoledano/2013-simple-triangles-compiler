/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexer;

import Errors.*;
import Stream.SourceStream;
import SymbolTable.*;
import java.io.IOException;
import java.util.Hashtable;

/**
 *
 * @author frodo
 */
public class Lexer {

    char c; //currentChar   
    SourceStream input;
    SymbolTable symbolsTable;
    Hashtable<String, TokenKind> keywordsTable;
    Errors.ErrorReporter errorReporter;

    public Lexer(SourceStream input, SymbolTable symbolsTable, ErrorReporter errorReporter) throws IOException {
        this.input = input;
        this.symbolsTable = symbolsTable;
        this.errorReporter = errorReporter;
        c = input.Read();
        keywordsTable = new Hashtable<String, TokenKind>();
        FillKeywordtable();
    }

    public SymbolTable getSymbolsTable() {
        return symbolsTable;
    }

    private void FillKeywordtable() {
        keywordsTable.put("graphics", TokenKind.Graphics);
        keywordsTable.put("triangle", TokenKind.Triangle);
        keywordsTable.put("int", TokenKind.Int);
        keywordsTable.put("move", TokenKind.Move);
        keywordsTable.put("inittriangle", TokenKind.InitTriangle);
    }

    boolean isLetter() {
        return Character.isLetter(c) || c == '_';
    }

    boolean isDigit() {
        return Character.isDigit(c);
    }

    void consume() throws IOException {
        c = input.Read();
    }

    void WS() throws IOException {
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            consume();
        }
    }

    Token ID() throws IOException {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isLetter() || isDigit());
        String lexeme = buf.toString();
        TokenKind key = keywordsTable.get(lexeme);
        if (key != null) {
            return new Token(key, lexeme, input.getCurrentLine());
        }
        return new Token(TokenKind.Id, lexeme, input.getCurrentLine(), symbolsTable.add(lexeme, TokenKind.Id));
    }

    Token Literal() throws IOException {
        StringBuilder buf = new StringBuilder();
        TokenKind kind = TokenKind.IntLiteral;
        do {
            buf.append(c);
            consume();
        } while (isDigit());
        return new Token(kind, buf.toString(), input.getCurrentLine(), symbolsTable.add(buf.toString(), kind));
    }

    public Token nextToken() throws IOException {
        while (c != '\0') {
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                WS(); // skip
            }
            switch (c) { // which token approaches?
                case '(':
                    consume();
                    return new Token(TokenKind.LeftParen, "(", input.getCurrentLine());
                case ')':
                    consume();
                    return new Token(TokenKind.RigthParen, ")", input.getCurrentLine());
                case '{':
                    consume();
                    return new Token(TokenKind.LeftKey, "{", input.getCurrentLine());
                case '}':
                    consume();
                    return new Token(TokenKind.RigthKey, "}", input.getCurrentLine());
                case ';':
                    consume();
                    return new Token(TokenKind.SemiColon, ";", input.getCurrentLine());
                case ',':
                    consume();
                    return new Token(TokenKind.Comma, ",", input.getCurrentLine());
                case '=':
                    consume();
                    return new Token(TokenKind.Assignment, "=", input.getCurrentLine());
                case '\0':
                    break;
                default: {
                    if (isLetter()) {
                        return ID(); // match ID
                    } else {
                        if (isDigit()) {
                            return Literal(); //match Literal
                        } else {
                            errorReporter.add(new LexicalError(
                                    input.getCurrentLine(), "Lexical Error: Unexpected Char \"" + c+"\""));
                            Token tk_error = new Token(TokenKind.Error, String.valueOf(c), input.getCurrentLine());
                            consume();
                            return tk_error;
                        }
                    }
                }
            }
        }
        return new Token(TokenKind.EOT, "\0", input.getCurrentLine());
    }

    public ErrorReporter getErrorList() {
        return errorReporter;
    }
}
