public class Parser {

    private Scanner scan;
    private Token currentToken;

    public Parser(byte[] input) {
        scan = new Scanner(input);
        currentToken = scan.nextToken();
    }

    private void nextToken() {
        currentToken = scan.nextToken();
    }

    private void consume(TokenType t) {
        if (currentToken.type == t) {
            nextToken();
        } else {
            throw new Error("syntax error: expected " + t + " got " + currentToken);
        }
    }

    public void parse() {
        expr();
        if (currentToken.type != TokenType.EOF) {
            throw new Error("syntax error: trailing input " + currentToken);
        }
    }

    // expr -> number oper
    private void expr() {
        number();
        oper();
    }

    // number -> [0-9]+  
    private void number() {
        if (currentToken.type == TokenType.NUMBER) {
            System.out.println("push " + currentToken.lexeme);
            consume(TokenType.NUMBER);
        } else {
            throw new Error("syntax error: number expected, got " + currentToken);
        }
    }

    // oper -> + number oper | - number oper | epsilon
    private void oper() {
        if (currentToken.type == TokenType.PLUS) {
            consume(TokenType.PLUS);
            number();
            System.out.println("add");
            oper();
        } else if (currentToken.type == TokenType.MINUS) {
            consume(TokenType.MINUS);
            number();
            System.out.println("sub");
            oper();
        }
    }
}
