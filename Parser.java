public class Parser {

    private Scanner scan;
    private Token currentToken;

    // Acumula o "código" gerado (push/add/sub/pop/print)
    private StringBuilder output = new StringBuilder();

    public Parser(byte[] input) {
        scan = new Scanner(input);
        currentToken = scan.nextToken();
    }

    private void emit(String s) {
        output.append(s).append(System.lineSeparator());
    }

    public String output() {
        return output.toString();
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

    // Programa: uma sequência de statements
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
        emit("print");
        match(TokenType.SEMICOLON);
    }

    // letStatement -> 'let' IDENT '=' expr ';'
    private void letStatement () {
        match(TokenType.LET);
        String id = currentToken.lexeme;   // captura nome da var
        match(TokenType.IDENT);
        match(TokenType.EQ);
        expr();
        emit("pop " + id);                 // atribuicao vira "pop <id>"
        match(TokenType.SEMICOLON);
    }

    // expr -> term oper
    private void expr() {
        term();
        oper();
    }

    // oper -> + term oper | - term oper | ε
    private void oper() {
        if (currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            term();
            emit("add");
            oper();
        } else if (currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            term();
            emit("sub");
            oper();
        }
        // ε
    }

    // term -> number | identifier
    private void term () {
        if (currentToken.type == TokenType.NUMBER) {
            number();
        } else if (currentToken.type == TokenType.IDENT) {
            emit("push " + currentToken.lexeme);
            match(TokenType.IDENT);
        } else {
            throw new Error("syntax error");
        }
    }

    // number -> [0-9]+
    private void number () {
        emit("push " + currentToken.lexeme);
        match(TokenType.NUMBER);
    }
}
