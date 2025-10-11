public class Lexer {
    private final String src;
    private int i = 0;

    public Lexer(byte[] input) {
        this.src = input == null ? "" : new String(input);
    }

    private boolean done() {
        return i >= src.length();
    }

    private char peek() {
        return done() ? '\0' : src.charAt(i);
    }

    private char advance() {
        return done() ? '\0' : src.charAt(i++);
    }

    private void skipWhitespace() {
        while (!done()) {
            char c = peek();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                advance();
            } else break;
        }
    }

    public Token next() {
        skipWhitespace();
        if (done()) return new Token(TokenType.EOF, "");

        char c = peek();

        if (c == '+') { advance(); return new Token(TokenType.PLUS, "+"); }
        if (c == '-') { advance(); return new Token(TokenType.MINUS, "-"); }

        if (Character.isDigit(c)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(peek())) sb.append(advance());
            return new Token(TokenType.NUMBER, sb.toString()); // [0-9]+
        }

        throw new Error("lexical error: unexpected char '" + c + "'");
    }
}
