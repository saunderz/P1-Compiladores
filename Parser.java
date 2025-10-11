public class Parser {
    private final Lexer lexer;
    private Token current;

    public Parser(byte[] input) {
        this.lexer = new Lexer(input);
        this.current = lexer.next();
    }

    private void consume(TokenType t) {
        if (current.type == t) {
            current = lexer.next();
        } else {
            throw new Error("syntax error: expected " + t + " got " + current);
        }
    }

    public void parse() {
        expr();
        if (current.type != TokenType.EOF) {
            throw new Error("syntax error: trailing input " + current);
        }
    }

    private void expr() {
        number();
        oper();
    }

    // number -> [0-9]+ 
    private void number() {
        if (current.type == TokenType.NUMBER) {
            System.out.println("push " + current.lexeme);
            consume(TokenType.NUMBER);
        } else {
            throw new Error("syntax error: number expected, got " + current);
        }
    }

    private void oper() {
        if (current.type == TokenType.PLUS) {
            consume(TokenType.PLUS);
            number();
            System.out.println("add");
            oper();
        } else if (current.type == TokenType.MINUS) {
            consume(TokenType.MINUS);
            number();
            System.out.println("sub");
            oper();
        } else {
            // nada a fazer
        }
    }
}
