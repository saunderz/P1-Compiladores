public class Main {
    public static void main(String[] args) {

        // Exemplo 1 (do texto): sem espacos extras
        // String input = "let a = 42 + 5;";

        // Exemplo 2: com espacos (scanner ignora corretamente)
        // String input = "let a = 42 + 5 - 8;";

        // Exemplo 3: com variavel no meio da expr
        // String input = "let a = 45  + preco - 876;";

        String input = "let a = 45  + preco - 876;";

        Parser p = new Parser(input.getBytes());
        p.parse();
    }
}
