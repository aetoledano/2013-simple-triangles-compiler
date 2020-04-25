

package Lexer;


public enum TokenKind {
    Graphics,
    LeftParen,
    RigthParen,
    LeftKey,
    RigthKey,
    
    Id,
    
    Triangle,
    Int,
    
    InitTriangle,
    Move,
    
    IntLiteral,
    SemiColon,
    Comma,
    Assignment,    
    Error,
    EOT    
}
