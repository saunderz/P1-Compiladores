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

    private void match(TokenType t) {
        if (currentToken.type == t) {
            nextToken();
        } else {
            throw new Error("syntax error");
        }
    }

    // sentential symbol: program -> statements
    public void parse() {
        statements();
        if (currentToken.type != TokenType.EOF) {
            throw new Error("syntax error");
        }
    }

    // statements -> statement*
    private void statements() {
        while (currentToken.type != TokenType.EOF) {
            statement();
        }
    }

    // statement -> printStatement | letStatement
    private void statement() {
        if (currentToken.type == TokenType.PRINT) {
            printStatement();
        } else if (currentToken.type == TokenType.LET) {
            letStatement();
        } else {
            throw new Error("syntax error");
        }
    }

    // printStatement -> 'print' expr ';'
    private void printStatement () {
        match(TokenType.PRINT);
        expr();
        System.out.println("print");
        match(TokenType.SEMICOLON);
    }

    // letStatement -> 'let' IDENT '=' expr ';'
    private void letStatement () {
        match(TokenType.LET);
        String id = currentToken.lexeme;   // capture var name
        match(TokenType.IDENT);
        match(TokenType.EQ);
        expr();
        System.out.println("pop " + id);   // translation for assignment
        match(TokenType.SEMICOLON);
    }

    // expr -> term oper
    private void expr() {
        term();
        oper();
    }

    // oper -> + term oper | - term oper | epsilon
    private void oper() {
        if (currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            term();
            System.out.println("add");
            oper();
        } else if (currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            term();
            System.out.println("sub");
            oper();
        }
        // epsilon
    }

    // term -> number | identifier
    private void term () {
        if (currentToken.type == TokenType.NUMBER) {
            number();
        } else if (currentToken.type == TokenType.IDENT) {
            System.out.println("push " + currentToken.lexeme);
            match(TokenType.IDENT);
        } else {
            throw new Error("syntax error");
        }
    }

    // number -> [0-9]+
    private void number () {
        System.out.println("push " + currentToken.lexeme);
        match(TokenType.NUMBER);
    }
}
