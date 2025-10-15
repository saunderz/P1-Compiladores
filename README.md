# P1-Compiladores — Partes 1 a 8

Projeto didático de **Tradução Dirigida por Sintaxe (SDT)** que evolui, passo a passo, de um tradutor simples para um **interpretador** funcional.  
Até a Parte 8, não foi usado Maven nem `package` — compilação direta com `javac`.

---

## ✅ Requisitos
- **Java JDK 17+** no `PATH`
- (Opcional) **Git** para versionamento

Verifique:
```bash
java -version
javac -version
```

---

## 🚀 Execução rápida (estado final — Parte 8)

Dentro do diretório do projeto:
```bash
javac -encoding UTF-8 TokenType.java Token.java Scanner.java Parser.java Interpretador.java Main.java
java Main
```

---

## 📦 Estrutura do repositório (final)
```
P1-Compiladores/
├─ Main.java
├─ Parser.java
├─ Scanner.java
├─ Token.java
├─ TokenType.java
└─ Interpretador.java
```
> Se mantiver arquivos das partes anteriores, compile apenas os necessários de cada parte.

---

## 🧩 Parte 1 — Um simples tradutor (sem analisador léxico)
Traduz expressões infixadas com dígitos únicos (0–9) e `+`/`-` para ações: `push <dígito>`, `add`, `sub`.

**Gramática**
```
expr  -> digit oper
oper  -> + digit oper
      |  - digit oper
      |  ε
digit -> 0 | … | 9
```

**Como rodar**
```bash
javac Main.java Parser.java
java Main
```

**Exemplo** (entrada → saída)
```
Entrada: 8+5-7+9
Saída:
push 8
push 5
add
push 7
sub
push 9
add
```

---

## 🔎 Parte 2 — Analisador léxico (números com vários dígitos)
Introduz lexer com tokens: `NUMBER`, `PLUS`, `MINUS`, `EOF`.  
Parser passa a consumir tokens e mantém ações (`push`/`add`/`sub`).

**Gramática**
```
expr   -> number oper
oper   -> + number oper
       |  - number oper
       |  ε
number -> [0-9]+
```

**Como rodar**
```bash
javac TokenType.java Token.java Lexer.java Parser.java Main.java
java Main
```

**Exemplo** ("45+89-876")
```
push 45
push 89
add
push 876
sub
```

---

## 🧱 Parte 3 — Refatoração: extraindo o analisador léxico
Move leitura de caracteres do `Parser` para um `Scanner` ad hoc (cada char ainda é um “token”).  
`Parser` não tem mais `peek()` e consome de `Scanner`.

**Como rodar**
```bash
javac Scanner.java Parser.java Main.java
java Main
```

---

## 🧾 Parte 4 — Suportando token NUMBER
`Scanner` passa a agrupar dígitos e retorna `Token` tipado (`NUMBER`/`PLUS`/`MINUS`/`EOF`).  
`Parser` consome `Token` e imprime ações.

**Como rodar**
```bash
javac -encoding UTF-8 TokenType.java Token.java Scanner.java Parser.java Main.java
java Main
```

**Exemplo** ("289-85+0+69")
```
push 289
push 85
sub
push 0
add
push 69
add
```

---

## 🔢 Parte 5 — Parser por number, com espaços em branco
Atualiza gramática do parser para `number` e o `Scanner` passa a ignorar whitespace.

**Gramática**
```
expr   -> number oper
oper   -> + number oper
       |  - number oper
       |  ε
number -> [0-9]+
```

**Exemplo** ("45 + 89 - 876")
```
push 45
push 89
add
push 876
sub
```

---

## 🔤 Parte 6 — Variáveis, `let`, `=` e `;`
Suporte a identificadores e palavra-chave `let`, mais símbolos `=` e `;`.  
Parser aceita termos como **número** ou **identificador**.

**Gramática**
```
letStatement -> 'let' identifier '=' expr ';'
expr         -> term oper
oper         -> + term oper
             | - term oper
             |  ε
term         -> number | identifier
number       -> [0-9]+
```

**Exemplos**
```
Entrada: let a = 42 + 5 - 8;
Saída:
push 42
push 5
add
push 8
sub
pop a
```
```
Entrada: let a = 45 + preco - 876;
Saída:
push 45
push preco
add
push 876
sub
pop a
```

---

## 🖨️ Parte 7 — Comando `print` e múltiplos statements
Palavra-chave `print`; o programa agora é uma sequência de statements:
```
statement  -> printStatement | letStatement
printStmt  -> 'print' expr ';'
statements -> statement*
```

**Exemplo**
```
let a = 42 + 5 - 8;
let b = 56 + 8;
print a + b + 6;
```
**Saída**
```
push 42
push 5
add
push 8
sub
pop a
push 56
push 8
add
pop b
push a
push b
add
push 6
add
print
```

---

## 🧠 Parte 8 — Um simples interpretador (stack machine)
O `Parser` deixa de imprimir e passa a **gerar código** (via `emit` para um `StringBuilder`).  
O `Interpretador` lê esse código linha a linha e executa em uma pilha com variáveis.

**Instruções suportadas**
- `PUSH <n|id>` — empilha número literal ou valor da variável  
- `ADD` / `SUB` — opera topo da pilha  
- `POP <id>` — armazena topo da pilha em variável  
- `PRINT` — imprime topo da pilha

**Como rodar (final)**
```bash
javac -encoding UTF-8 TokenType.java Token.java Scanner.java Parser.java Interpretador.java Main.java
java Main
```

**Exemplo final**
```
let a = 42 + 2;
let b = 15 + 3;
print a + b;
```
**Saída**
```
62
```

---

## 🗂️ Resumo por arquivo (final)
- **Scanner.java**: léxico ad hoc; ignora espaços; reconhece `NUMBER`, `IDENT`, `LET`, `PRINT`, `+`, `-`, `=`, `;`, `EOF`.
- **Token.java / TokenType.java**: representação dos tokens (tipo + lexema).
- **Parser.java**: descida recursiva; gera código com `emit(...)`; expõe `output()`; reconhece `statements`, `let`, `print`, `expr`.
- **Interpretador.java**: executa a sequência de instruções geradas pelo `Parser` (pilha + mapa de variáveis).
- **Main.java**: ponto de entrada; alimenta `Parser` com a string do programa, passa a saída ao `Interpretador` e executa.

---

## 🧪 Testes rápidos
Troque a string de input em `Main.java` por qualquer programa válido, compile e rode.  
Exemplo “modo direto” (sem parser), útil para validar o interpretador:
```
push 10
push 20
add
pop a
push 45
push a
sub
print
```
**Saída**
```
35
```

